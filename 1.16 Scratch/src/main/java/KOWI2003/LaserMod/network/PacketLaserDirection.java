package KOWI2003.LaserMod.network;

import java.util.function.Supplier;

import KOWI2003.LaserMod.tileentities.TileEntityAdvancedLaser;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketLaserDirection {

	public BlockPos pos;
	public Vector2f eularRotation;
	
	public PacketLaserDirection(PacketBuffer buf) {
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt()); 
		this.eularRotation = new Vector2f(buf.readFloat(), buf.readFloat());
	}
	
	public PacketLaserDirection(BlockPos pos, Vector2f eularRotation) {
		this.pos = pos;
		this.eularRotation = eularRotation;
	}
	
	public void toBytes(PacketBuffer buf) {
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		buf.writeFloat(eularRotation.x);
		buf.writeFloat(eularRotation.y);
	}
	
	public void handle(Supplier<NetworkEvent.Context> ctx) {
	    ctx.get().enqueueWork(() -> {
	        // Work that needs to be thread-safe (most work)
	        ServerPlayerEntity sender = ctx.get().getSender(); // the client that sent this packet
	        
	        TileEntity tileentity = sender.getLevel().getBlockEntity(pos);
	        if(tileentity instanceof TileEntityAdvancedLaser) {
	        	TileEntityAdvancedLaser te = (TileEntityAdvancedLaser)tileentity;
	        	te.setRotationEular(eularRotation);
	        }
	    });
	    ctx.get().setPacketHandled(true);
	    //return true;
	}
	
}