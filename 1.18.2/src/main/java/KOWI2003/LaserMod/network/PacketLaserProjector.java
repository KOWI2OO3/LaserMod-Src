package KOWI2003.LaserMod.network;

import java.util.function.Supplier;

import KOWI2003.LaserMod.tileentities.TileEntityLaserProjector;
import KOWI2003.LaserMod.tileentities.TileEntityLaserProjector.PROJECTOR_MODES;
import KOWI2003.LaserMod.utils.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

public class PacketLaserProjector {

	public BlockPos pos;
	public String field;
	public String textValue;
	public boolean boolValue;
	
	public PacketLaserProjector(FriendlyByteBuf buf) {
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt()); 
		this.field = buf.readUtf();
		this.boolValue = buf.readBoolean();
		this.textValue = buf.readUtf();
	}
	
	public PacketLaserProjector(BlockPos pos, String field, boolean boolValue) {
		this.pos = pos;
		this.boolValue = boolValue;
		this.field = field;
		this.textValue = "";
	}
	
	public PacketLaserProjector(BlockPos pos, String field, String textValue) {
		this.pos = pos;
		this.boolValue = false;
		this.field = field;
		this.textValue = textValue;
	}
	
	public void toBytes(FriendlyByteBuf buf) {
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		buf.writeUtf(field);
		buf.writeBoolean(boolValue);
		buf.writeUtf(textValue);
	}
	
	public void handle(Supplier<NetworkEvent.Context> ctx) {
	    ctx.get().enqueueWork(() -> {
	        // Work that needs to be thread-safe (most work)
	        ServerPlayer sender = ctx.get().getSender(); // the client that sent this packet
	        
	        BlockEntity tileentity = sender.getLevel().getBlockEntity(pos);
	        if(tileentity instanceof TileEntityLaserProjector) {
	        	TileEntityLaserProjector te = (TileEntityLaserProjector)tileentity;
	        	if(field.equals("text")) {
	        		te.text = textValue;
	        		if(te.mode == PROJECTOR_MODES.PLAYER) {
	        			if(te.liveModel)
	        				te.playerToRender = Utils.getPlayer(te.getLevel(), te.text);
	        			else
	        				te.profile = Utils.updateProfileConsumer(te.text, te.profile);
	        			te.isProfileChecked = false;
	        		}
	        	}else if(field.equals("doesRotate"))
	        		te.doesRotate = boolValue;
	        	else if(field.equals("liveModel")) {
	        		te.liveModel = boolValue;
	        		if(te.mode == PROJECTOR_MODES.PLAYER) {
	        			if(te.liveModel)
	        				te.playerToRender = Utils.getPlayer(te.getLevel(), te.text);
	        			else
	        				te.profile = Utils.updateProfileConsumer(te.text, te.profile);
	        		}
	        	}else if(field.equals("isChild"))
	        		te.isChild = boolValue;
	    		te.sync();
	        }
	    });
	    ctx.get().setPacketHandled(true);
	    //return true;
	}
}
