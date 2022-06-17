package KOWI2003.LaserMod.network;

import java.util.function.Supplier;

import KOWI2003.LaserMod.tileentities.TileEntityModStation;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketModStation {
	public BlockPos pos;
	public String upgardeName;
	public boolean add;

	public PacketModStation(PacketBuffer buf) {
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt()); 
		this.upgardeName = buf.readUtf();
		this.add = buf.readBoolean();
	}
	
	public PacketModStation(BlockPos pos, String upgradeName, boolean add) {
		this.pos = pos;
		this.upgardeName = upgradeName;
		this.add = add;
	}
	
	public void toBytes(PacketBuffer buf) {
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		buf.writeUtf(upgardeName);
		buf.writeBoolean(add);
	}
	
	public void handle(Supplier<NetworkEvent.Context> ctx) {
	    ctx.get().enqueueWork(() -> {
	        // Work that needs to be thread-safe (most work)
	        ServerPlayerEntity sender = ctx.get().getSender(); // the client that sent this packet
	        
	        TileEntity tileentity = sender.getLevel().getBlockEntity(pos);
	        if(tileentity instanceof TileEntityModStation) {
	        	TileEntityModStation te = (TileEntityModStation)tileentity;
	        	if(add) {
	        		te.addUpgrade();
	        	}else
	        		te.removeUpgrade(upgardeName);
	        }
	    });
	    ctx.get().setPacketHandled(true);
	}
}