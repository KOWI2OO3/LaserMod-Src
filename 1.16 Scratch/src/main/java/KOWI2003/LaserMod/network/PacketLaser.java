package KOWI2003.LaserMod.network;

import java.util.function.Supplier;

import KOWI2003.LaserMod.tileentities.TileEntityLaser;
import KOWI2003.LaserMod.tileentities.TileEntityModStation;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketLaser {

	public BlockPos pos;
	public float red,green,blue;
	
	public PacketLaser(PacketBuffer buf) {
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt()); 
		this.red = buf.readFloat();
		this.green = buf.readFloat();
		this.blue = buf.readFloat();
	}
	
	public PacketLaser(BlockPos pos, float red, float green, float blue) {
		this.pos = pos;
		this.red = red;
		this.green = green;
		this.blue = blue;
	}
	
	public void toBytes(PacketBuffer buf) {
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		buf.writeFloat(red);
		buf.writeFloat(green);
		buf.writeFloat(blue);
	}
	
	public void handle(Supplier<NetworkEvent.Context> ctx) {
	    ctx.get().enqueueWork(() -> {
	        // Work that needs to be thread-safe (most work)
	        ServerPlayerEntity sender = ctx.get().getSender(); // the client that sent this packet
	        
	        TileEntity tileentity = sender.getLevel().getBlockEntity(pos);
	        if(tileentity instanceof TileEntityLaser) {
	        	TileEntityLaser te = (TileEntityLaser)tileentity;
	        	te.red = red;
	        	te.green = green;
	        	te.blue = blue;
	        }else if(tileentity instanceof TileEntityModStation) {
	        	((TileEntityModStation)tileentity).setColor(red, green, blue);
	        }
	    });
	    ctx.get().setPacketHandled(true);
	    //return true;
	}
	
}
