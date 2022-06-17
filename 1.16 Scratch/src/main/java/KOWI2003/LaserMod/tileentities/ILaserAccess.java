package KOWI2003.LaserMod.tileentities;

import java.util.List;

import KOWI2003.LaserMod.LaserProperties;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3f;

public interface ILaserAccess {

	Direction getDirection();
	LaserProperties getProperties();
	BlockPos getPos();
	TileEntity getTileEntity();
	TileEntityLaser getLaser();
	BlockPos getStuckPos();
	Vector3f getForward();
	<T extends Entity> List<T> getEntitiesInLaser(Class<? extends T> entityType);
}
