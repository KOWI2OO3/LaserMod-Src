package KOWI2003.LaserMod.tileentities;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import KOWI2003.LaserMod.LaserProperties.Properties;
import KOWI2003.LaserMod.init.ModTileTypes;
import KOWI2003.LaserMod.utils.MathUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3f;

public class TileEntityAdvancedLaser extends TileEntityLaser {

	public static final float minAngle = -22.5f;
	public static final float maxHeight = 1f;
	public static final float minHeight = -1f;
	
	private Vector2f rotation;
	public float height;
	
	protected BlockPos StuckPos;
	
	private boolean debugLine = false;
	
	public TileEntityAdvancedLaser() {
		super(ModTileTypes.ADVANCED_LASER);
		rotation = new Vector2f(0, 0);
		height = 0f;
	}
	
	public Vector2f getRotation() {
		return new Vector2f((float)Math.toRadians(rotation.x), (float)Math.toRadians(rotation.y));
	}
	
	public Vector2f getEularRotation() {
		return new Vector2f(rotation.x, rotation.y);
	}
	
	@Override
	public void tick() {
		super.tick();
	}
	
	public void setHeight(float h) {
		this.height = Math.min(maxHeight, Math.max(minHeight, h));
		sync();
	}
	
	@Override
	public void updateLaser() {
		if(rotation.x == 0 && rotation.y == 0) {
			super.updateLaser();
			return;
		}
		
		Vector3f dir = getForward();
		float stepSize = 0.1f;
		double distBlockEnter = 0;
		BlockPos lastPos = getBlockPos();
		BlockPos stuckPos = getStuckPos();
		if(canPassThrough(stuckPos.getX(), stuckPos.getY(), stuckPos.getZ(), level.getBlockState(stuckPos))) {
			distance = properties.getProperty(Properties.MAX_DISTANCE);
			StuckPos = null;
		}
		
		Vector3f up = getDirection().step();
		
		float height = this.height/10f;
		
		for(float i = 0; Math.abs(i) <= Math.abs(Math.round(distance)); i += stepSize) {
			float x = dir.x() * i + getBlockPos().getX() + 0.5f - up.x() * 0.2f + up.x() * height;
			float y = dir.y() * i + getBlockPos().getY() + 0.5f - up.y() * 0.2f + up.y() * height;
			float z = dir.z() * i + getBlockPos().getZ() + 0.5f - up.z() * 0.2f + up.z() * height;
			if(debugLine)
				level.addParticle(RedstoneParticleData.REDSTONE, x, y, z, 0.1f, 0.1f, 0.1f);			//Proper Laser Display
			
			BlockPos pos = new BlockPos(Math.round(x - 0.5f), Math.round(y - 0.5f), Math.round(z - 0.5f));
			if(!pos.equals(lastPos)) {
				lastPos = pos;
				distBlockEnter = i;
			}
			BlockState state = level.getBlockState(pos);
			if(state != getBlockState()) {
				
				if(!canPassThrough(x, y, z, state)) {
					updateLaserInteractables(i, pos);
					distance = getRenderDistance(x, y, z, state, i);
//					distance = i;
					stuckDistance = i;
					break;
				}
				if(i >= stuckDistance-0.5f && canPassThrough(x, y, z, state)) {
					distance = properties.getProperty(Properties.MAX_DISTANCE);
				}
			}
		}
	}
	
	public Vector3f UP() {
		Vector3f up = new Vector3f(0, 1, 0);
		up = MathUtils.rotateVector(up, new Vector3f(), Vector3f.XP.rotationDegrees(getDirection().step().y() * 90f - 90f));
		return up;
	}
	
	public Vector3f RIGHT() {
		Vector3f up = new Vector3f(0, 0, 1);
		up = MathUtils.rotateVector(up, new Vector3f(), Vector3f.XP.rotationDegrees(getDirection().step().y() * 90f - 90f));
		return up;
	}
	
	public boolean updateLaserOnPos(double x, double y, double z, float i) {
		return true;
	}
	
	public void setDirection(Vector3f direction) {
		Vector3f dir = direction.copy();
		dir.sub(getLaserPosVector());
		dir.normalize();
		Vector3f rotation = MathUtils.forwardToEuler(dir, MathUtils.normalVectorFrom(dir));

//		rotation = MathUtils.mulVector(rotation, 10);
//		setRotationEular(new Vector2f((float)rotation.x(), -(float)rotation.y()));
	}
	
	public void setDirectionDirect(Vector3f direction) {
		direction.normalize();
		Vector3f rotation = MathUtils.forwardToEuler(direction, MathUtils.normalVectorFrom(direction));
		rotation = MathUtils.mulVector(rotation, 10);
		setRotationEular(new Vector2f((float)rotation.x(), -(float)rotation.y()));
	}
	
	public Vector3f getLaserPosVector() {
		Vector3f up = getDirection().step();
		return new Vector3f(
				getBlockPos().getX() + 0.5f - up.x() * 0.2f + up.x() * height,
				getBlockPos().getY() + 0.5f - up.y() * 0.2f + up.y() * height,
				getBlockPos().getZ() + 0.5f - up.z() * 0.2f + up.z() * height);
	}
	
	@Override
	public BlockPos getStuckPos() {
		Vector3f dir = getForward();
		return new BlockPos(Math.round(getBlockPos().getX() + dir.x() * distance), 
				Math.round(getBlockPos().getY() + dir.y() * distance), 
				Math.round(getBlockPos().getZ() + dir.z() * distance));
	}
	
	@Override
	public double getRenderDistance(BlockPos pos, BlockState newState, double distance) {
		return super.getRenderDistance(pos, newState, distance) - 0.35f;
	}
	
	public boolean canPassThrough(double x, double y, double z, BlockState state) {
		Vector3f dir = getForward();
		
		if(state.getBlock() == Blocks.AIR || state.getMaterial() == Material.GLASS || state.getBlock() == Blocks.FIRE || state.getBlock() == Blocks.VOID_AIR)
			return true;
		
		BlockPos pos = new BlockPos(Math.round(x), Math.round(y), Math.round(z));
		VoxelShape shape = state.getCollisionShape(getLevel(), pos);
		
		Iterable<AxisAlignedBB> aabbs = shape.toAabbs();
		
		x = x - pos.getX() < 0 ? Math.abs(x - pos.getX() + 1) : x - pos.getX();
		y = y - pos.getY() < 0 ? Math.abs(y - pos.getY() + 1) : y - pos.getY();
		z = z - pos.getZ() < 0 ? Math.abs(z - pos.getZ() + 1) : z - pos.getZ();
		
		if(!shape.isEmpty())
		if(shape.bounds().contains(x, y, z)) {
			for(double i = 0; i <= 4; i+=0.001) {
				double x2 = x + dir.x() * i;
				double y2 = y + dir.y() * i;
				double z2 = z + dir.z() * i;
				for (AxisAlignedBB aabb : aabbs) {
					if(aabb.contains(x2, y2, z2)) {
						return false;
					}
				}
			}
		}
		
		return true;
	}
	
	public double getRenderDistance(double x, double y, double z, BlockState state, double distance) {
		Vector3f dir = getForward();
		float stepSize = 0.1f;
		BlockPos pos = new BlockPos(Math.round(x), Math.round(y), Math.round(z));
		
		VoxelShape shape = state.getCollisionShape(getLevel(), pos);
		
		Iterable<AxisAlignedBB> aabbs = shape.toAabbs();
		
		double value = 0;
		double xx = x - pos.getX() < 0 ? Math.abs(x - pos.getX() + 1) : x - pos.getX();
		double yy = y - pos.getY() < 0 ? Math.abs(y - pos.getY() + 1) : y - pos.getY();
		double zz = z - pos.getZ() < 0 ? Math.abs(z - pos.getZ() + 1) : z - pos.getZ();
		
		if(shape.bounds().contains(xx, yy, zz)) {
			for(double i = 0; i <= 1.5f; i+=0.001) {
				double x2 = xx + dir.x() * i;
				double y2 = yy + dir.y() * i;
				double z2 = zz + dir.z() * i;
				for (AxisAlignedBB aabb : aabbs) {
					if(aabb.contains(x2, y2, z2)) {
						value = MathUtils.getLenght(new Vector3f((float)(x2 - (0.5 * Math.abs(1-dir.x()))), (float)(y2 - (0.5 * Math.abs(1-dir.y())))
								, (float)(z2 - (0.5 * Math.abs(1-dir.z())))));
						return distance + value - (y2 > 0.7f ? dir.y() : 0);
					}
				}
			}
		}
		
		return distance;
	}
	
	private void updateLaserInteractables(float distance, BlockPos newPos) {
		if(distance < stuckDistance)
			handleTurnOffForInteractable();
		
		TileEntity te = getLevel().getBlockEntity(newPos);
		if(te != null)
			if(te instanceof ILaserInteractable)
				((ILaserInteractable)te).interactWithLaser(this);
	}
	
	@Override
	public CompoundNBT save(CompoundNBT nbt) {
		CompoundNBT rotNBT = new CompoundNBT();
		rotNBT.putFloat("X", rotation.x);
		rotNBT.putFloat("Y", rotation.y);
		nbt.put("Rotation", rotNBT);
		nbt.putFloat("Height", height);
		nbt.putBoolean("DebugLine", debugLine);
		return super.save(nbt);
	}
	
	@Override
	public void load(BlockState state, CompoundNBT nbt) {
		if(nbt.contains("Rotation")) {
			CompoundNBT rotNBT = nbt.getCompound("Rotation");
			if(rotNBT.contains("X") && rotNBT.contains("Y"))
				rotation = new Vector2f(rotNBT.getFloat("X"), rotNBT.getFloat("Y"));
		}
		if(nbt.contains("Height"))
			height = nbt.getFloat("Height");
		if(nbt.contains("DebugLine"))
			debugLine = nbt.getBoolean("DebugLine");
		super.load(state, nbt);
	}
	
	public void setRotationEular(Vector2f rotation) {
		float x = Math.min(Math.abs(minAngle), Math.max(minAngle, rotation.x));
		float y = Math.min(Math.abs(minAngle), Math.max(minAngle, rotation.y));
		this.rotation = new Vector2f(x, y);
		sync();
	}
	
	public Vector3f getForward() {
		if(rotation.x == 0 && rotation.y == 0)
			return super.getForward();
		Vector3f dir = new Vector3f(0, 1, 0);

		Direction facing = getDirection();
		
		Vector2f rotation = getRotation();
		dir = MathUtils.rotateVector(dir, new Vector3f(), new Vector3f((float)rotation.x, 0, (float)rotation.y));
		Quaternion rot1 = Vector3f.XP.rotationDegrees(facing.step().y() * 90f - 90f);
		Quaternion rot2 = Vector3f.ZP.rotationDegrees((Math.abs(facing.step().y())-1) * (facing.toYRot() + 180f));
		
		dir = MathUtils.rotateVector(dir, new Vector3f(), rot2);
		dir = MathUtils.rotateVector(dir, new Vector3f(), rot1);
		return dir;
	}
	
	@Override
	public <T extends Entity> List<T> getEntitiesInLaser(Class<? extends T> entityType) {
		Set<T> entities = new HashSet<>();
		Vector3f dir = getForward();
		float stepSize = 0.1f;
		double distBlockEnter = 0;
		
		float hitDistance = 0.2f;
		
		Vector3f up = getDirection().step();
		
		float height = this.height/10f;
		
		for(float i = 0; Math.abs(i) <= Math.abs(Math.round(distance)); i += stepSize) {
			float x = dir.x() * i + getBlockPos().getX() + 0.5f - up.x() * 0.2f + up.x() * height/10f;
			float y = dir.y() * i + getBlockPos().getY() + 0.5f - up.y() * 0.2f + up.y() * height/10f;
			float z = dir.z() * i + getBlockPos().getZ() + 0.5f - up.z() * 0.2f + up.z() * height/10f;
			
			hitDistance = stepSize;
			AxisAlignedBB aabb = new AxisAlignedBB(x - hitDistance, y - hitDistance, z - hitDistance, x + hitDistance, y + hitDistance, z + hitDistance);
			
			entities.addAll(level.getEntitiesOfClass(entityType, aabb));
		}
		List<T> ents = new LinkedList<T>();
		for (T t : entities) {
			ents.add(t);
		}
		return ents;
	}
	
	@Override
	public Direction getDirection() {
		return super.getDirection();
	}
	
}
