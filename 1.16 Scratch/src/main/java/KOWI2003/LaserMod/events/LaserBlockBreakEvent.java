package KOWI2003.LaserMod.events;

import java.util.LinkedList;
import java.util.List;

import KOWI2003.LaserMod.LaserProperties;
import KOWI2003.LaserMod.items.ItemLaserToolBase;
import KOWI2003.LaserMod.utils.LaserItemUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class LaserBlockBreakEvent {

	@SubscribeEvent
	public void onBlockBreak(BlockEvent.BreakEvent event) {
		PlayerEntity player = event.getPlayer();
		BlockPos pos = event.getPos();
		BlockState state = event.getState();
		IWorld level = event.getWorld();
		
		ItemStack item = player.inventory.getSelected();
		
		if(item.getItem() instanceof ItemLaserToolBase) {
			ItemLaserToolBase tool = (ItemLaserToolBase)item.getItem();
			LaserProperties props = LaserItemUtils.getProperties(item);
			System.out.println("speed: " + tool.getDestroySpeed(item, state));
			if(props.hasUpgarde("fire")) {
				if(player.level instanceof ServerWorld) {
					List<ItemStack> newDrops = new LinkedList<>();
					List<ItemStack> drops = Block.getDrops(state, (ServerWorld)player.level, pos, player.level.getBlockEntity(pos));
					List<FurnaceRecipe> recipes = player.level.getRecipeManager().getAllRecipesFor(IRecipeType.SMELTING);
					for (ItemStack stack : drops) {
						boolean found = false;
						for (FurnaceRecipe recipe : recipes) {
							for(Ingredient ing : recipe.getIngredients()) {
								for(ItemStack input : ing.getItems()) {
									if(input.getItem() == stack.getItem()) {
										ItemStack temp = recipe.getResultItem().copy();
										temp.setCount(stack.getCount());
										newDrops.add(temp);
										found = true;
										break;
									}
								}
								if(found)
									break;
							}
							if(found)
								break;
						}
						if(!found) {
							newDrops.add(stack);
						}
					}
					event.setCanceled(false);
					level.setBlock(pos, Blocks.AIR.defaultBlockState(), 0);
					if(!player.isCreative())
					for (ItemStack drop : newDrops) {
						Block.popResource(player.level, pos, drop);
					}
				}
			}
		}
		
//		event.setCanceled(true);
	}
	
}
