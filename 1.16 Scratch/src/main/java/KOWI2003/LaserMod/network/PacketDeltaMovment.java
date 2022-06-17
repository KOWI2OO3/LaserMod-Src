package KOWI2003.LaserMod.network;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketDeltaMovment {

	public Vector3d movement;
	
	public PacketDeltaMovment(PacketBuffer buf) {
		movement = new Vector3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
	}
	
	public PacketDeltaMovment(Vector3d movement) {
		this.movement = movement;
	}
	
	public void toBytes(PacketBuffer buf) {
		buf.writeDouble(movement.x());
		buf.writeDouble(movement.y());
		buf.writeDouble(movement.z());
	}
	
	public void handle(Supplier<NetworkEvent.Context> ctx) {
	    ctx.get().enqueueWork(() -> {
	        // Work that needs to be thread-safe (most work)
	    	
	        //ServerPlayer sender = ctx.get().getSender(); // the client that sent this packet
	    	
	        if(ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT) {
	        	Minecraft.getInstance().player.setDeltaMovement(movement);
	        }else {
	        	ServerPlayerEntity sender = ctx.get().getSender();
	        	sender.setDeltaMovement(movement);
	        	PacketHandler.sendToClient(new PacketDeltaMovment(movement), sender);
	        }
	    });
	    ctx.get().setPacketHandled(true);
	    //return true;
	}
	
}
