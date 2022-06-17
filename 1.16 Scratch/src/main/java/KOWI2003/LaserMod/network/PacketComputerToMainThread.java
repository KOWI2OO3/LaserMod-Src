package KOWI2003.LaserMod.network;

import java.util.function.Supplier;

import KOWI2003.LaserMod.tileentities.TileEntityDeviceHub;
import KOWI2003.LaserMod.tileentities.TileEntityRemoteCC;
import KOWI2003.LaserMod.utils.Utils;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketComputerToMainThread {

	public BlockPos pos;
	public int index;
	public Object[] args;
	public boolean isDevice;
	
	public PacketComputerToMainThread(PacketBuffer buf) {
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		index = buf.readInt();
		isDevice = buf.readBoolean();
		args = Utils.getObjectArray(buf.readNbt());
	}
	
	public PacketComputerToMainThread(BlockPos pos, int index, Object[] args) {
		this(pos, index, args, false);
	}
	
	public PacketComputerToMainThread(BlockPos pos, int index, Object[] args, boolean isDevice) {
		this.pos = pos;
		this.index = index;
		this.args = args;
		this.isDevice = isDevice;
	}
	
	public void toBytes(PacketBuffer buf) {
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		buf.writeInt(index);
		buf.writeBoolean(isDevice);
		buf.writeNbt(Utils.putObjectArray(args));
	}
	
	public void handle(Supplier<NetworkEvent.Context> ctx) {
	    ctx.get().enqueueWork(() -> {
	        // Work that needs to be thread-safe (most work)
	        ServerPlayerEntity sender = ctx.get().getSender(); // the client that sent this packet
	        
	        TileEntity tileentity = sender.getLevel().getBlockEntity(pos);
	        if(tileentity instanceof TileEntityRemoteCC) {
	        	TileEntityRemoteCC te = (TileEntityRemoteCC)tileentity;
	        	te.callMethode(index, args);
	        }
	        if(tileentity instanceof TileEntityDeviceHub) {
	        	TileEntityDeviceHub te = (TileEntityDeviceHub)tileentity;
	        	te.callMethod(index, args);
	        }
	    });
	    ctx.get().setPacketHandled(true);
	    //return true;
	}
	
}
