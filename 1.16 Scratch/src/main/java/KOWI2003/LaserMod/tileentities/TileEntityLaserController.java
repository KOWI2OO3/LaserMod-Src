package KOWI2003.LaserMod.tileentities;

import KOWI2003.LaserMod.init.ModTileTypes;
import KOWI2003.LaserMod.utils.TileEntityUtils;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;

public class TileEntityLaserController extends TileEntity implements ITickableTileEntity {

	BlockPos controlPos = null;
	
	public TileEntityLaserController() {
		super(ModTileTypes.LASER_CONTROLLER);
	}
	
	public TileEntityLaserController(TileEntityType<?> tileEntityType) {
		super(tileEntityType);
	}
	
	public boolean isConnected() {
		return controlPos != null;
	}
	
	@Override
	public CompoundNBT save(CompoundNBT nbt) {
		CompoundNBT controlPosNBT = new CompoundNBT();
		if(controlPos != null) {
			controlPosNBT.putInt("x", controlPos.getX());
			controlPosNBT.putInt("y", controlPos.getY());
			controlPosNBT.putInt("z", controlPos.getZ());
		}
		nbt.put("controlPosition", controlPosNBT);
		return super.save(nbt);
	}
	
	@Override
	public void load(BlockState state, CompoundNBT nbt) {
		if(nbt.contains("controlPosition")) {
			CompoundNBT controlPosNBT = nbt.getCompound("controlPosition");
			if(controlPosNBT.contains("x") && controlPosNBT.contains("y") && controlPosNBT.contains("z")) {
				controlPos = new BlockPos(controlPosNBT.getInt("x"), controlPosNBT.getInt("y"), controlPosNBT.getInt("z"));
			}else
				controlPos = null;
		}
		super.load(state, nbt);
	}

	@Override
	public void tick() {
		sync();
		if(!(getControlTileEntity() instanceof TileEntityLaser || getControlTileEntity() instanceof TileEntityLaserProjector)) {
			controlPos = null;
		}
	}
	
	public TileEntity getControlTileEntity() {
		if(controlPos == null)
			return null;
		return level.getBlockEntity(controlPos);
	}
	
	public void link(BlockPos pos) {
		if(isConnected())
			Disconnect();
		this.controlPos = pos;
		TileEntity tileentity = getControlTileEntity();
		if(tileentity instanceof TileEntityLaser) {
			((TileEntityLaser)tileentity).isRemoteControlled = true;
			((TileEntityLaser)tileentity).sync();
		}if(tileentity instanceof TileEntityLaserProjector) {
			((TileEntityLaserProjector)tileentity).isRemoteControlled = true;
			((TileEntityLaserProjector)tileentity).sync();
		}sync();
	}
	
	public void Disconnect() {
		TileEntity tileentity = getControlTileEntity();
		if(tileentity instanceof TileEntityLaser) {
			((TileEntityLaser)tileentity).isRemoteControlled = false;
			((TileEntityLaser)tileentity).sync();
		}if(tileentity instanceof TileEntityLaserProjector) {
			((TileEntityLaserProjector)tileentity).isRemoteControlled = false;
			((TileEntityLaserProjector)tileentity).sync();
		}controlPos = null;
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
}
