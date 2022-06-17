package KOWI2003.LaserMod.tileentities;

import KOWI2003.LaserMod.blocks.BlockRotatable;
import KOWI2003.LaserMod.init.ModTileTypes;
import KOWI2003.LaserMod.utils.TileEntityUtils;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;

public class TileEntityLaserCatcher extends TileEntity implements ILaserInteractable,IColorable {

	public boolean isHit = false;
	TileEntityLaser te;
	
	public TileEntityLaserCatcher() {
		super(ModTileTypes.LASER_CATCHER);
	}

	@Override
	public CompoundNBT save(CompoundNBT nbt) {
		nbt.putBoolean("isHit", isHit);
		return super.save(nbt);
	}
	
	@Override
	public void load(BlockState state, CompoundNBT nbt) {
		isHit = nbt.getBoolean("isHit");
		super.load(state, nbt);
	}
	
	@Override
	public void interactWithLaser(ILaserAccess te) {
		if(getBlockState().getValue(BlockRotatable.FACING) == te.getDirection()) {
			isHit = true;
			this.te = te.getLaser();
			getLevel().blockUpdated(getBlockPos(), getBlockState().getBlock());
			TileEntityUtils.syncToClient(this);
		}
	}

	@Override
	public void onLaserInteractStop(ILaserAccess te) {
		isHit = false;
		this.te = null;
		getLevel().blockUpdated(getBlockPos(), getBlockState().getBlock());
		TileEntityUtils.syncToClient(this);
	}

	@Override
	public float[] getColor(int index) {
		if(isHit) {
			if(te != null)
				return new float[] {te.red, te.green, te.blue};
		}
		return new float[] {1f, 1f, 1f};
	}
	
	@Override
	public void setColor(int index, float[] color) {}
}
