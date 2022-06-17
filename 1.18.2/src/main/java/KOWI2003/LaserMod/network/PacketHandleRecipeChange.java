package KOWI2003.LaserMod.network;

import java.util.function.Supplier;

import KOWI2003.LaserMod.tileentities.TileEntityLaserCrafter;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

public class PacketHandleRecipeChange {
	
	public static final int ADD = 0x00;
	public static final int REMOVE = 0x01;
	
	public BlockPos pos;
	public String recipeID;
	public int operation;
	
	public PacketHandleRecipeChange(FriendlyByteBuf buf) {
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt()); 
		this.recipeID = buf.readUtf();
		this.operation = buf.readInt();
	}
	
	public PacketHandleRecipeChange(BlockPos pos, String recipeID, int operation) {
		this.pos = pos;
		this.recipeID = recipeID;
		this.operation = operation;
	}
	
	public void toBytes(FriendlyByteBuf buf) {
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		buf.writeUtf(recipeID);
		buf.writeInt(operation);
	}
	
	public void handle(Supplier<NetworkEvent.Context> ctx) {
	    ctx.get().enqueueWork(() -> {
	        // Work that needs to be thread-safe (most work)
	        ServerPlayer sender = ctx.get().getSender(); // the client that sent this packet
	        
	        BlockEntity tileentity = sender.getLevel().getBlockEntity(pos);
	        if(tileentity instanceof TileEntityLaserCrafter) {
	        	TileEntityLaserCrafter te = (TileEntityLaserCrafter)tileentity;
	        	
	        	for (CraftingRecipe recipe : sender.getLevel().getRecipeManager().getAllRecipesFor(RecipeType.CRAFTING)) {
					if(recipe.getId().toString().equals(recipeID)) {
						if(operation == ADD)
							te.recipes.add(recipe);
						if(operation == REMOVE && te.recipes.contains(recipe))
							te.recipes.remove(recipe);
			        	te.sync();
			        	break;
					}
				}
	        }
	    });
	    ctx.get().setPacketHandled(true);
	    //return true;
	}
}
