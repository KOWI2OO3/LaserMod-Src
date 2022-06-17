package KOWI2003.LaserMod.tileentities;

import java.util.List;

import KOWI2003.LaserMod.DamageSourceLaser;
import KOWI2003.LaserMod.LaserProperties;
import KOWI2003.LaserMod.LaserProperties.Properties;
import KOWI2003.LaserMod.blocks.BlockHorizontal;
import KOWI2003.LaserMod.init.ModTileTypes;
import KOWI2003.LaserMod.items.ItemUpgradeBase;
import KOWI2003.LaserMod.utils.MathUtils;
import KOWI2003.LaserMod.utils.TileEntityUtils;
import KOWI2003.LaserMod.utils.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TileEntityMirror extends TileEntity implements ILaserInteractable, ITickableTileEntity, ILaserAccess {

	public double distance = 10;
	private double stuckDistance = 10;
	TileEntityLaser laser;
	Direction redirectDirection = Direction.NORTH;
	boolean clockwise = true;
	
	public TileEntityMirror() {
		super(ModTileTypes.MIRROR);
	}
	
	@Override
	public double getViewDistance()
	{
		return 16384*2d;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public AxisAlignedBB getRenderBoundingBox()
	{
		return INFINITE_EXTENT_AABB;
	}
	
	@Override
	public void tick() {
		sync();
		if(isActive()) {
			updateLaserAbilities();
			updateLaser(redirectDirection);
		}
	}
	
	public TileEntityLaser getLaser() {
		return laser;
	}
	
	public Direction getDirection() {
		return redirectDirection;
	}
	
	@Override
	public void interactWithLaser(ILaserAccess te) {
		if(te.getDirection().getOpposite() == getBlockState().getValue(BlockHorizontal.FACING) || 
				te.getDirection().getOpposite() == getBlockState().getValue(BlockHorizontal.FACING).getCounterClockWise()) {
			if(laser != te)
				laser = te.getLaser();
			redirectDirection = te.getDirection().getOpposite() == getBlockState().getValue(BlockHorizontal.FACING) ? te.getDirection().getClockWise() : te.getDirection().getCounterClockWise();
		}
	}

	@Override
	public void onLaserInteractStop(ILaserAccess te) {
		if(te.getDirection().getOpposite() == getBlockState().getValue(BlockHorizontal.FACING) || 
				te.getDirection().getOpposite() == getBlockState().getValue(BlockHorizontal.FACING).getCounterClockWise()) {
			handleTurnOffForInteractable();
		}
		laser = null;
		redirectDirection = Direction.NORTH;
	}
	
	public boolean isActive() {
		return hasLaser() && laser.active;
	}
	
	public boolean hasLaser() {
		return laser != null;
	}
	
/*	=====================================================================
 * 						LASER STUFF
 * 	===================================================================== */
	public List<LivingEntity> getLivingEntitiesInLaser(Direction direction) {
		return getEntitiesInLaser(LivingEntity.class, direction); 
	}
	
	public <T extends Entity> List<T> getEntitiesInLaser(Class<? extends T> entityType, Direction direction) {
		AxisAlignedBB aabb = new AxisAlignedBB(getBlockPos());
		Vector3f dir = MathUtils.mulVector(MathUtils.getVectorFromDir(direction),(float) distance-1);
		aabb = aabb.expandTowards(new Vector3d(dir.x(), dir.y(), dir.z()));
		return getLevel().getEntitiesOfClass(entityType, aabb);
	}
	
	public void updateLaser(Direction direction) {
		for(int i = 1; i <= Math.round(distance); i++) {
			BlockState state = getBlockState();
			Direction facing = direction;
			BlockPos newPos = Utils.offset(getBlockPos(), facing, i);
			BlockState newState = getLevel().getBlockState(newPos);
			if(!canPassThrough(newPos, newState, facing)) {
				updateLaserInteractables(i, newPos);
				distance = getRenderDistance(newPos, newState, i, facing);
				stuckDistance = i;
				break;
			}
			if(i == stuckDistance && canPassThrough(newPos, newState, facing)) {
				distance = laser.getProperties().getProperty(Properties.MAX_DISTANCE);
			}
		}
	}
	
	private void updateLaserInteractables(int distance, BlockPos newPos) {
		if(distance < stuckDistance)
			handleTurnOffForInteractable();
		
		TileEntity te = getLevel().getBlockEntity(newPos);
		if(te != null)
			if(te instanceof ILaserInteractable)
				((ILaserInteractable)te).interactWithLaser(this);
	}
	
	public void handleTurnOffForInteractable() {
		BlockPos pos = Utils.offset(getBlockPos(), redirectDirection, (float) stuckDistance);
		TileEntity te = getLevel().getBlockEntity(pos);
		if(te != null)
			if(te instanceof ILaserInteractable)
				((ILaserInteractable)te).onLaserInteractStop(this);
	}
	
	public boolean canPassThrough(BlockPos pos, BlockState newState, Direction direction) {
		Direction facing = direction;
		
		double bbX = facing.getStepX();
		double bbY = facing.getStepY();
		double bbZ = facing.getStepZ();
		
		BlockPos newPos = Utils.offset(pos, facing, 1);
		VoxelShape AABB = newState.getCollisionShape(getLevel(), newPos);
		RayTraceResult RTR = AxisAlignedBB.clip(AABB.toAabbs(), new Vector3d(pos.getX() + .5 - (.5 * bbX), pos.getY()+.5 - (.5 * bbY), pos.getZ()+ .5 - (.5 * bbZ)), new Vector3d(newPos.getX() + .5 + (.5 * bbX), newPos.getY() + .5 + (.5 * bbY), newPos.getZ() + .5 + (.5 * bbZ)), newPos);
		
		if(newState.getBlock() == Blocks.AIR || newState.getMaterial() == Material.GLASS || newState.getBlock() == Blocks.FIRE || newState.getBlock() == Blocks.VOID_AIR)
			return true;
		
		try {
			if(AABB.bounds().intersects(0.5 - (.5 * Math.abs(bbX)), 0.5 - (.5 * Math.abs(bbY)), 0.5 - (.5 * Math.abs(bbZ)), 0.5 + (.5 * Math.abs(bbX)), 0.5 + (.5 * Math.abs(bbY)), 0.5 + (.5 * Math.abs(bbZ)))) {
				if(RTR == null)
					return true;
				return RTR.getLocation() != Vector3d.ZERO && RTR.getType() == Type.MISS;
			}
		}catch (Exception e) {}
		
		return true;
	}
	
	public double getRenderDistance(BlockPos pos, BlockState newState, double distance, Direction direction) {
		Direction facing = direction;
		
		double value = 0;
		
		double bbX = facing.getStepX();
		double bbY = facing.getStepY();
		double bbZ = facing.getStepZ();
		
		BlockPos newPos = Utils.offset(pos, facing, 1);
		VoxelShape AABB = newState.getCollisionShape(getLevel(), newPos);
		RayTraceResult RTR = AxisAlignedBB.clip(AABB.toAabbs(), new Vector3d(pos.getX() + .5 - (.5 * bbX), pos.getY()+.5 - (.5 * bbY), pos.getZ()+ .5 - (.5 * bbZ)), new Vector3d(newPos.getX() + .5 + (.5 * bbX), newPos.getY() + .5 + (.5 * bbY), newPos.getZ() + .5 + (.5 * bbZ)), newPos);
		
		try {
			if(AABB != null)
				if(AABB.bounds().intersects(0.5 - (.5 * Math.abs(bbX)), 0.5 - (.5 * Math.abs(bbY)), 0.5 - (.5 * Math.abs(bbZ)), 0.5 + (.5 * Math.abs(bbX)), 0.5 + (.5 * Math.abs(bbY)), 0.5 + (.5 * Math.abs(bbZ)))) {
					if(RTR == null)
						return distance;
					if(RTR.getLocation() != Vector3d.ZERO) {
						Vector3d vector = new Vector3d(RTR.getLocation().x * bbX, RTR.getLocation().y * bbY, RTR.getLocation().z * bbZ);
						if(vector.x() != 0)
							value = vector.x();
						if(vector.y() != 0)
							value = vector.y();
						if(vector.z() != 0)
							value = vector.z();
					}
				}
		}catch (Exception e) {}
		return Math.abs(value - (Math.floor(value))) + distance;
	}
	
	public void updateLaserAbilities() {
		AxisAlignedBB aabb = new AxisAlignedBB(getBlockPos());
		Vector3f dir = MathUtils.getVectorFromDir(redirectDirection);
		dir = MathUtils.mulVector(dir, (float) distance-1);
		aabb = aabb.expandTowards(new Vector3d(dir.x(), dir.y(), dir.z()));
		List<LivingEntity> entities = getLevel().getEntitiesOfClass(LivingEntity.class, aabb);
		for (LivingEntity entity : entities) {
			entity.hurt(new DamageSourceLaser("laser", laser.getProperties().hasUpgarde("fire")), laser.getProperties().getProperty(Properties.DAMAGE));
		}
		
		updateLaserUpgrades();
	}
	
	public void updateLaserUpgrades() {
		List<ItemUpgradeBase> upgrades = laser.getProperties().getUpgrades();
		if(upgrades.size() > 0)
			for(ItemUpgradeBase upgrade : upgrades) {
				if(upgrade != null && getBlockPos() != getStuckPos())
					upgrade.runLaserBlock(this, getStuckPos());
			}
	}
	
	public BlockPos getStuckPos() {
		return Utils.offset(getBlockPos(), redirectDirection, (float) stuckDistance);
	}
	
/*	=====================================================================
 * 						LASER STUFF END
 * 	===================================================================== */
	
	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		return new SUpdateTileEntityPacket(getBlockPos(), 0, getUpdateTag());
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		this.load(getLevel().getBlockState(pkt.getPos()), pkt.getTag());
	}
	
	@Override
	public CompoundNBT getUpdateTag() {
		return save(new CompoundNBT());
	}
	
	@Override
	public void handleUpdateTag(BlockState state, CompoundNBT tag) {
		this.load(state, tag);
	}
	
	@Override
	public CompoundNBT getTileData() {
		return this.serializeNBT();
	}
	
	public void sync()
    {
        TileEntityUtils.syncToClient(this);
    }

	@Override
	public LaserProperties getProperties() {
		return laser.getProperties();
	}

	@Override
	public BlockPos getPos() {
		return getBlockPos();
	}

	@Override
	public TileEntity getTileEntity() {
		return this;
	}

	@Override
	public <T extends Entity> List<T> getEntitiesInLaser(Class<? extends T> entityType) {
		return getEntitiesInLaser(entityType, redirectDirection);
	}

	@Override
	public Vector3f getForward() {
		return getDirection().step();
	}
	
}
