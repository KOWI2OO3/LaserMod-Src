package KOWI2003.LaserMod.network;

import java.util.function.Supplier;

import KOWI2003.LaserMod.tileentities.IColorable;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketSyncIColor {

	public BlockPos pos;
	public float[] color;
	public int index;
	
	public PacketSyncIColor(PacketBuffer buf) {
		color = new float[] {buf.readFloat(), buf.readFloat(), buf.readFloat()};
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		index = buf.readInt();
	}
	
	public PacketSyncIColor(BlockPos pos, float[] color) {
		this(pos, color, 0);
	}
	
	public PacketSyncIColor(BlockPos pos, float[] color, int index) {
		this.color = color;
		this.pos = pos;
		this.index = index;
	}
	
	public void toBytes(PacketBuffer buf) {
		buf.writeFloat(color[0]);
		buf.writeFloat(color[1]);
		buf.writeFloat(color[2]);
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		buf.writeInt(index);
	}
	
	public void handle(Supplier<NetworkEvent.Context> ctx) {
	    ctx.get().enqueueWork(() -> {
	        // Work that needs to be thread-safe (most work)
	    	
	        if(ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT)
	        {
	        	World level = Minecraft.getInstance().player.getCommandSenderWorld();
	        	TileEntity tile = level.getBlockEntity(pos);
	        	if(tile instanceof IColorable)
	        		((IColorable)tile).setColor(index, color);
	        }else {
	        	ServerPlayerEntity sender = ctx.get().getSender(); // the client that sent this packet
	        	ServerWorld level = sender.getLevel();
	        	TileEntity tile = level.getBlockEntity(pos);
	        	if(tile instanceof IColorable)
	        		((IColorable)tile).setColor(index, color);
	        	
	        	for(PlayerEntity player : level.players())
					if(player instanceof ServerPlayerEntity)
						PacketHandler.sendToClient(new PacketSyncIColor(pos, color, index), (ServerPlayerEntity)player);
	        }
	        
	    });
	    ctx.get().setPacketHandled(true);
	    //return true;
	}
}
