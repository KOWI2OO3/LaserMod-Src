package KOWI2003.LaserMod.tileentities;

import java.util.List;

import KOWI2003.LaserMod.DamageSourceLaser;
import KOWI2003.LaserMod.LaserProperties;
import KOWI2003.LaserMod.LaserProperties.Properties;
import KOWI2003.LaserMod.blocks.BlockLaser;
import KOWI2003.LaserMod.blocks.BlockRotatable;
import KOWI2003.LaserMod.config.ModConfig;
import KOWI2003.LaserMod.container.ContainerLaser;
import KOWI2003.LaserMod.init.ModSounds;
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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.ItemStackHandler;


public class TileEntityLaser extends TileEntity implements ITickableTileEntity, INamedContainerProvider, IColorable, ILaserAccess {

	public boolean active = false;
	public float red = 1.0f;
	public float green, blue = 0.0f;
	public double distance = ModConfig.GetConfig().defaultLaserDistance;
	public int soundCooldown = 0;
	public MODE mode = MODE.NORMAL;
	
	LaserProperties properties;
	
	protected double stuckDistance = 10;
	public boolean isRemoteControlled = false;

	
	public TileEntityLaser(TileEntityType<? extends TileEntity> advancedLaser) {
		super(advancedLaser);
		red = 1.0f;
		green = 0.0f;
		blue = 0.0f;
		properties = new LaserProperties();
		properties.setProperty(Properties.DAMAGE, ModConfig.GetConfig().defaultLaserDamage);
		properties.setProperty(Properties.MAX_DISTANCE, ModConfig.GetConfig().defaultLaserDistance);
	}
	public TileEntityLaser() {
		super(ModTileTypes.LASER);
		red = 1.0f;
		green = 0.0f;
		blue = 0.0f;
		properties = new LaserProperties();
		properties.setProperty(Properties.DAMAGE, 3);
		properties.setProperty(Properties.MAX_DISTANCE, 10);
	}

	@Override
	public void tick() {
		if(!isRemoteControlled)
			if(active != Utils.isBlockPowered(getBlockPos(), getLevel())) {
				setActive(Utils.isBlockPowered(getBlockPos(), getLevel()));
			}
			
		if(active) {
			if(ModConfig.useSounds) {
				soundCooldown--;
				if(soundCooldown <= 0) {
					soundCooldown = 29;
					level.playSound(null, getBlockPos(), 
							ModSounds.LASER_ACTIVE.get(), SoundCategory.BLOCKS, 0.1F, 1F);
				}
			}
		}
		
		sync();
		if(active) {
			updateLaserAbilities();
			updateLaser();
		}
	}
	
	public void setActive(boolean value) {
		active = value;
		if(!active) {
			handleTurnOffForInteractable();
			if(ModConfig.useSounds)
				level.playSound(null, getBlockPos(), ModSounds.LASER_DEACTIVATE.get(), SoundCategory.BLOCKS, 1F, 0.68F);
		}else
			if(ModConfig.useSounds)
				level.playSound(null, getBlockPos(), ModSounds.LASER_ACTIVATE.get(), SoundCategory.BLOCKS, 1F, 0.68F);
		soundCooldown = 29;
		sync();
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
	public CompoundNBT save(CompoundNBT nbt) {
		nbt.putBoolean("Powered", active);
		nbt.putFloat("Red", red);
		nbt.putFloat("Green", green);
		nbt.putFloat("Blue", blue);
		nbt.putDouble("Distance", distance);
		nbt.putString("Mode", mode.getName());
		nbt.putBoolean("isRemoteControlled", isRemoteControlled);
		properties.save(nbt);
		return super.save(nbt);
	}
	
	@Override
	public void load(BlockState state, CompoundNBT nbt) {
		active = nbt.getBoolean("Powered");
		red = nbt.getFloat("Red");
		green = nbt.getFloat("Green");
		blue = nbt.getFloat("Blue");
		distance = nbt.getDouble("Distance");
		mode = MODE.getMode(nbt.getString("Mode"));
		if(nbt.contains("isRemoteControlled"))
			isRemoteControlled = nbt.getBoolean("isRemoteControlled");
		properties.load(nbt);
		super.load(state, nbt);
	}
	
	public void setColor(float red, float green, float blue) {
		if(properties.hasUpgarde("color")) {
			this.red = red > 1.0f ? 1.0f : red < 0.0f ? 0.0f : red;
			this.green = green > 1.0f ? 1.0f : green < 0.0f ? 0.0f : green;
			this.blue = blue > 1.0f ? 1.0f : blue < 0.0f ? 0.0f : blue;
		}else {
			this.red = 1.0f;
			this.green = this.blue = 0f;
		}
		sync();
	}
	
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
	
	public List<LivingEntity> getLivingEntitiesInLaser() {
		return getEntitiesInLaser(LivingEntity.class); 
	}
	
	public <T extends Entity> List<T> getEntitiesInLaser(Class<? extends T> entityType) {
		AxisAlignedBB aabb = new AxisAlignedBB(getBlockPos());
		Vector3f dir = getDir();
		dir = MathUtils.mulVector(dir, (float) distance-1);
		aabb = aabb.expandTowards(new Vector3d(dir.x(), dir.y(), dir.z()));
		return getLevel().getEntitiesOfClass(entityType, aabb);
	}
	
	public void updateLaserAbilities() {
		List<LivingEntity> entities = getEntitiesInLaser(LivingEntity.class);
		for (LivingEntity entity : entities) {
			entity.hurt(new DamageSourceLaser("laser", properties.hasUpgarde("fire")), properties.getProperty(Properties.DAMAGE));
		}
		
		updateLaserUpgrades();
	}
	
	public Vector3f getDir() {
		return new Vector3f(getBlockState().getValue(BlockLaser.FACING).getStepX(), getBlockState().getValue(BlockLaser.FACING).getStepY(), getBlockState().getValue(BlockLaser.FACING).getStepZ());
	}
	
	public void updateLaserUpgrades() {
		List<ItemUpgradeBase> upgrades = properties.getUpgrades();
		if(upgrades.size() > 0)
			for(ItemUpgradeBase upgrade : upgrades) {
				if(upgrade != null && getBlockPos() != getStuckPos())
					upgrade.runLaserBlock(this, getStuckPos());
			}
	}
	
	public BlockPos getStuckPos() {
		return Utils.offset(getBlockPos(), getBlockState().getValue(BlockLaser.FACING), (float) stuckDistance);
	}
	
	public void updateLaser() {
		if(stuckDistance > properties.getProperty(Properties.MAX_DISTANCE))
			stuckDistance = properties.getProperty(Properties.MAX_DISTANCE);
		
		for(int i = 1; i <= Math.round(distance); i++) {
			BlockState state = getBlockState();
			Direction facing = state.getValue(BlockLaser.FACING);
			BlockPos newPos = Utils.offset(getBlockPos(), facing, i);
			BlockState newState = getLevel().getBlockState(newPos);
			if(!canPassThrough(newPos, newState)) {
				updateLaserInteractables(i, newPos);
				distance = getRenderDistance(newPos, newState, i);
				stuckDistance = i;
				break;
			}
			if(i == stuckDistance && canPassThrough(newPos, newState)) {
				distance = properties.getProperty(Properties.MAX_DISTANCE);
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
		BlockPos pos = Utils.offset(getBlockPos(), getBlockState().getValue(BlockLaser.FACING), (float) stuckDistance);
		TileEntity te = getLevel().getBlockEntity(pos);
		if(te != null)
			if(te instanceof ILaserInteractable)
				((ILaserInteractable)te).onLaserInteractStop(this);
	}
	
	public boolean canPassThrough(BlockPos pos, BlockState newState) {
		Direction facing = getBlockState().getValue(BlockLaser.FACING);
		
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
	
	public double getRenderDistance(BlockPos pos, BlockState newState, double distance) {
		Direction facing = getBlockState().getValue(BlockLaser.FACING);
		
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
	
	public LaserProperties getProperties() {
		return properties;
	}
	
	//Inventory Stuff
	public ItemStackHandler createHandler() {
		ItemStackHandler inv = new ItemStackHandler(9) {
			@Override
			public void setStackInSlot(int slot, ItemStack stack) {
				if(stack.getItem() instanceof ItemUpgradeBase)
					acceptsItem((ItemUpgradeBase)stack.getItem(), false);
				super.setStackInSlot(slot, stack);
			}
		};
		List<ItemUpgradeBase> items = properties.getUpgrades();
		for(int i = 0; i < items.size(); i++) {
			inv.setStackInSlot(i, new ItemStack(items.get(i)));
		}
		return inv;
	}
	
	public boolean acceptsItem(ItemUpgradeBase upgrade, boolean simulate) {
		return upgrade.isUsefullForLaser() && (simulate ? properties.doesAllow(upgrade) : properties.addUpgrade(upgrade));
	}
	
	public boolean remove(ItemUpgradeBase upgarde, boolean simulate) {
		return simulate ? properties.hasUpgarde(upgarde) : properties.removeUpgrade(upgarde) != null;
	}
	
	@Override
	public Container createMenu(int windowId, PlayerInventory playerInv, PlayerEntity player) {
		return new ContainerLaser(windowId, playerInv, this);
	}
	
	@Override
	public ITextComponent getDisplayName() {
		return new TranslationTextComponent("container.lasermod.laser");
	}
	
public static enum MODE {
	NORMAL("NORMAL", 0),
	POWER("POWER", 1),
	INVISIBLE("INVISIBLE", 2),
	BEAM("BEAM", 3),
	NEW_POWER("NEW_POWER", 4);
	
	private int id;
	private String name;
	
	private MODE(String name, int id) {
		this.id = id;
		this.name = name;
	}
	
	public static MODE getMode(String name) {
		return MODE.valueOf(name);
	}
	
	public static MODE getMode(int id) {
		return MODE.values()[id];
	}
	
	public String getName() {
		return name;
	}
	
	public String getFormalName() {
		return Utils.getFormalText(name);
	}
	
	public MODE fromFormalName(String name) {
		return getMode(Utils.fromFormalText(name));
	}
	
	public int getID() {
		return id;
	}
}

	@Override
	public float[] getColor(int index) {
		return new float[] {red, green, blue};
	}

	@Override
	public Direction getDirection() {
		return getBlockState().getValue(BlockRotatable.FACING);
	}
	
	@Override
	public Vector3f getForward() {
		return getDirection().step();
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
	public TileEntityLaser getLaser() {
		return this;
	}
	@Override
	public void setColor(int index, float[] color) {
		setColor(
				color.length > 0 ? color[0] : 1.0f, 
				color.length > 1 ? color[1] : 1.0f, 
				color.length > 2 ? color[2] : 1.0f);
	}

}
