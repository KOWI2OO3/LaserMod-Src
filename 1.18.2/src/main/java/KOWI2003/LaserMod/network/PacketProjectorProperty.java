package KOWI2003.LaserMod.network;

import java.util.function.Supplier;

import KOWI2003.LaserMod.tileentities.TileEntityLaserProjector;
import KOWI2003.LaserMod.tileentities.TileEntityLaserProjector.ProjectorProperties.PROJECTOR_PROPERTY;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

public class PacketProjectorProperty {

	public BlockPos pos;
	public int ordinal;
	public float value;
	
	public PacketProjectorProperty(FriendlyByteBuf buf) {
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt()); 
		this.ordinal = buf.readInt();
		this.value = buf.readFloat();
	}
	
	public PacketProjectorProperty(BlockPos pos, int ordinal, float value) {
		this.pos = pos;
		this.value = value;
		this.ordinal = ordinal;
	}
	
	public void toBytes(FriendlyByteBuf buf) {
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		buf.writeInt(ordinal);
		buf.writeFloat(value);
	}
	
	public void handle(Supplier<NetworkEvent.Context> ctx) {
	    ctx.get().enqueueWork(() -> {
	        // Work that needs to be thread-safe (most work)
	        ServerPlayer sender = ctx.get().getSender(); // the client that sent this packet
	        
	        BlockEntity tileentity = sender.getLevel().getBlockEntity(pos);
	        if(tileentity instanceof TileEntityLaserProjector) {
	        	TileEntityLaserProjector te = (TileEntityLaserProjector)tileentity;
	        	if(ordinal == -1) {
	        		if(value < 0)
	        			te.properties.resetAll();
	        		else {
	        			for (PROJECTOR_PROPERTY property : PROJECTOR_PROPERTY.values()) {
	        				te.properties.setProperty(property, value);
						}
	        		}
	        	}else
	        		te.properties.setProperty(PROJECTOR_PROPERTY.values()[ordinal], value);
	    		te.sync();
	        }
	    });
	    ctx.get().setPacketHandled(true);
	    //return true;
	}
}
