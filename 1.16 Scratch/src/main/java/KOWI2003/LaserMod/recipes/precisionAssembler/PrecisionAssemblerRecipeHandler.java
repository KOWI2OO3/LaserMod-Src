package KOWI2003.LaserMod.recipes.precisionAssembler;

import java.util.ArrayList;
import java.util.List;

import KOWI2003.LaserMod.init.ModBlocks;
import KOWI2003.LaserMod.init.ModItems;
import KOWI2003.LaserMod.init.ModUpgrades;
import KOWI2003.LaserMod.tileentities.TileEntityPrecisionAssembler;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.items.ItemStackHandler;

public class PrecisionAssemblerRecipeHandler {

	static List<IPrecisionAssemblerRecipe> recipes = new ArrayList<IPrecisionAssemblerRecipe>();
	
	public static void registerRecipes() {
		registerRecipe(1, new ItemStack(ModItems.CircuitBoard), new ItemStack(ModItems.SiliconBase), new ItemStack(Items.IRON_INGOT), ItemStack.EMPTY, new ItemStack(Items.REDSTONE));
		registerRecipeShapeless(1, new ItemStack(ModUpgrades.Speed), new ItemStack(ModItems.CircuitBoard), new ItemStack(Items.FEATHER), new ItemStack(Items.REDSTONE), new ItemStack(Items.GLOWSTONE_DUST));
		registerRecipeShapeless(1, new ItemStack(ModUpgrades.Mining), new ItemStack(ModItems.CircuitBoard), new ItemStack(Items.IRON_PICKAXE));
		registerRecipeShapeless(1, new ItemStack(ModUpgrades.Fire), new ItemStack(ModItems.CircuitBoard), new ItemStack(Items.FLINT_AND_STEEL));
		registerRecipeShapeless(1, new ItemStack(ModUpgrades.Color), new ItemStack(ModItems.CircuitBoard), new ItemStack(Items.RED_DYE), new ItemStack(Items.LAPIS_LAZULI), new ItemStack(Items.GREEN_DYE));
		registerRecipeShapeless(1, new ItemStack(ModUpgrades.Damage1), new ItemStack(ModItems.CircuitBoard), new ItemStack(Items.WOODEN_SWORD));
		registerRecipeShapeless(1, new ItemStack(ModUpgrades.Damage2), new ItemStack(ModItems.CircuitBoard), new ItemStack(Items.STONE_SWORD));
		registerRecipeShapeless(1, new ItemStack(ModUpgrades.Damage3), new ItemStack(ModItems.CircuitBoard), new ItemStack(Items.IRON_SWORD));
		registerRecipeShapeless(1, new ItemStack(ModUpgrades.Damage4), new ItemStack(ModItems.CircuitBoard), new ItemStack(Items.DIAMOND_SWORD));
		registerRecipeShapeless(1, new ItemStack(ModUpgrades.Damage5), new ItemStack(ModItems.CircuitBoard), new ItemStack(Items.NETHERITE_SWORD));
		registerRecipeShapeless(1, new ItemStack(ModUpgrades.NoDamage), new ItemStack(ModItems.CircuitBoard), ItemTags.WOOL);
		registerRecipeShapeless(1, new ItemStack(ModUpgrades.Push), new ItemStack(ModItems.CircuitBoard), new ItemStack(Blocks.PISTON));
		registerRecipeShapeless(1, new ItemStack(ModUpgrades.Pull), new ItemStack(ModItems.CircuitBoard), new ItemStack(Blocks.STICKY_PISTON));
		registerRecipeShapeless(1, new ItemStack(ModUpgrades.Distance), new ItemStack(ModItems.CircuitBoard), new ItemStack(Blocks.GLASS), new ItemStack(ModItems.LaserCrystal), new ItemStack(Blocks.GLASS));
		registerRecipeShapeless(1, new ItemStack(ModUpgrades.Distance2), new ItemStack(ModUpgrades.Distance), new ItemStack(Blocks.GLASS), new ItemStack(ModItems.LaserCrystal), new ItemStack(Blocks.GLASS));
		registerRecipeShapeless(1, new ItemStack(ModUpgrades.Distance3), new ItemStack(ModUpgrades.Distance2), new ItemStack(Blocks.GLASS), new ItemStack(ModItems.LaserCrystal), new ItemStack(Blocks.GLASS));
		registerRecipeShapeless(1, new ItemStack(ModUpgrades.Mode), new ItemStack(ModItems.CircuitBoard), new ItemStack(Blocks.GLASS), new ItemStack(ModItems.LaserCrystal));
		registerRecipe(1, new ItemStack(ModItems.IR_Glasses), new ItemStack(Items.IRON_INGOT), new ItemStack(Blocks.BLACK_STAINED_GLASS), new ItemStack(Items.IRON_INGOT), new ItemStack(Blocks.BLACK_STAINED_GLASS));
		
		registerRecipe(1, ModItems.LaserSword.getDefaultInstance() ,new ItemStack(ModItems.LaserToolShell), new ItemStack(Items.IRON_SWORD), new ItemStack(ModItems.LaserCrystal), new ItemStack(ModItems.LaserCrystal));
		registerRecipe(1, ModItems.LaserPickaxe.getDefaultInstance() ,new ItemStack(ModItems.LaserToolShell), new ItemStack(Items.IRON_PICKAXE), new ItemStack(ModItems.LaserCrystal), new ItemStack(ModItems.LaserCrystal));
		registerRecipe(1, ModItems.LaserAxe.getDefaultInstance() ,new ItemStack(ModItems.LaserToolShell), new ItemStack(Items.IRON_AXE), new ItemStack(ModItems.LaserCrystal), new ItemStack(ModItems.LaserCrystal));
		registerRecipe(1, ModItems.LaserShovel.getDefaultInstance() ,new ItemStack(ModItems.LaserToolShell), new ItemStack(Items.IRON_SHOVEL), new ItemStack(ModItems.LaserCrystal), new ItemStack(ModItems.LaserCrystal));
		registerRecipe(1, ModItems.LaserHoe.getDefaultInstance() ,new ItemStack(ModItems.LaserToolShell), new ItemStack(Items.IRON_HOE), new ItemStack(ModItems.LaserCrystal), new ItemStack(ModItems.LaserCrystal));
		
		registerRecipe(1, new ItemStack(ModBlocks.LaserProjector), new ItemStack(ModBlocks.Laser), new ItemStack(Items.IRON_INGOT), new ItemStack(ModItems.CircuitBoard), new ItemStack(ModItems.LaserCrystal));
		registerRecipe(1, new ItemStack(ModBlocks.ModStation), new ItemStack(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE), new ItemStack(Items.IRON_INGOT), new ItemStack(ModItems.CircuitBoard), new ItemStack(Items.IRON_INGOT));
		registerRecipe(1, ModItems.Linker.getDefaultInstance(), new ItemStack(Items.IRON_INGOT), new ItemStack(Items.IRON_INGOT), new ItemStack(ModItems.CircuitBoard), new ItemStack(Items.REDSTONE));
		registerRecipe(1, new ItemStack(ModBlocks.LaserController), new ItemStack(Blocks.IRON_BLOCK), new ItemStack(Items.LEVER), new ItemStack(ModItems.CircuitBoard), new ItemStack(Items.REDSTONE));
		registerRecipe(1, new ItemStack(ModBlocks.Mirror), new ItemStack(Blocks.GLASS), new ItemStack(Items.IRON_INGOT), new ItemStack(Items.IRON_INGOT), new ItemStack(Items.IRON_INGOT));
		registerRecipe(1, new ItemStack(ModItems.SiliconBase), ItemStack.EMPTY, new ItemStack(ModItems.Silicon, 2));
	}
	
	public static void registerRecipe(IPrecisionAssemblerRecipe recipe) {
		recipes.add(recipe);
	}
	
	public static void registerRecipe(float speed, ItemStack output, Object inputBase, Object... inputs) {
		recipes.add(new PrecisionAssemblerRecipeBase(output, speed, inputBase, inputs));
	}
	
	public static void registerRecipeShapeless(float speed, ItemStack output, Object inputBase, Object... inputs) {
		recipes.add(new PrecisionAssemblerRecipeShapeless(output, speed, inputBase, inputs));
	}
	
	public static IPrecisionAssemblerRecipe getRecipe(TileEntityPrecisionAssembler te) { 
		for (IPrecisionAssemblerRecipe recipe : recipes) {
			if(recipe.isRecipeValid(te.getHandler()))
				return recipe;
		}
		return null;
	}
	
	public static void handleRecipeEnd(IPrecisionAssemblerRecipe recipe, TileEntityPrecisionAssembler te) {
		if(!recipe.isRecipeValid(te.getHandler()))
			return;
		ItemStackHandler handler = te.getHandler();
		Object[] inputs = recipe.getInputs();
		if (recipe instanceof PrecisionAssemblerRecipeShapeless) {
			for(int i = 0; i < inputs.length; i++) {
				Object obj = inputs[i];
				if(obj instanceof ItemStack) {
					ItemStack input = ((ItemStack)obj).copy();
					for (int j = 0; j < handler.getSlots() - 2; j++) {
						ItemStack slot = handler.getStackInSlot(j).copy();
						if(slot.getItem() == input.getItem()) {
							int newCount = slot.getCount() - input.getCount();
							if(newCount <= 0)
								slot = ItemStack.EMPTY;
							else
								slot.setCount(slot.getCount() - input.getCount());
							handler.setStackInSlot(j, slot);
						}
					}
				}else if(obj instanceof ITag<?>) {
					for (int j = 0; j < handler.getSlots() - 2; j++) {
						ItemStack slot = handler.getStackInSlot(j).copy();
						if(slot.getItem().is((ITag<Item>)obj)) {
							int newCount = slot.getCount() - 1;
							if(newCount <= 0)
								slot = ItemStack.EMPTY;
							else
								slot.setCount(slot.getCount() - 1);
							handler.setStackInSlot(j, slot);
						}
					}
				}
			}
		}else {
			for (int i = 0; i < Math.min(inputs.length, handler.getSlots() - 2); i++) {
				Object obj = inputs[i];
				if(obj instanceof ItemStack) {
					ItemStack input = ((ItemStack)obj).copy();
					ItemStack slot = handler.getStackInSlot(i).copy();
					int newCount = slot.getCount() - input.getCount();
					if(newCount <= 0)
						slot = ItemStack.EMPTY;
					else
						slot.setCount(slot.getCount() - input.getCount());
					handler.setStackInSlot(i, slot);
				}else if(obj instanceof ITag<?>) {
					ItemStack slot = handler.getStackInSlot(i).copy();
					if(slot.getItem().is((ITag<Item>)obj)) {
						int newCount = slot.getCount() - 1;
						if(newCount <= 0)
							slot = ItemStack.EMPTY;
						else
							slot.setCount(slot.getCount() - 1);
						handler.setStackInSlot(i, slot);
					}
				}
			}
		}
		Object obj = recipe.getInputBase();
		ItemStack slot = handler.getStackInSlot(3).copy();
		if(obj instanceof ItemStack) {
			ItemStack inputBase = (ItemStack) obj;
			slot = handler.getStackInSlot(3).copy();
			int newCount = slot.getCount() - inputBase.getCount();
			if(newCount <= 0)
				slot = ItemStack.EMPTY;
			else
				slot.setCount(slot.getCount() - inputBase.getCount());
			handler.setStackInSlot(3, slot);
		}else if(obj instanceof ITag<?>) {
			if(slot.getItem().is((ITag<Item>)obj)) {
				int newCount = slot.getCount() - 1;
				if(newCount <= 0)
					slot = ItemStack.EMPTY;
				else
					slot.setCount(slot.getCount() - 1);
				handler.setStackInSlot(3, slot);
			}
		}
		
		ItemStack output = recipe.getOutput();
		slot = handler.getStackInSlot(4).copy();
		if(!slot.isEmpty())
			output.setCount(output.getCount() + slot.getCount());
		handler.setStackInSlot(4, output);
		te.handler = handler;
	}
	
	public static List<IPrecisionAssemblerRecipe> getAllRecipes() {
		return recipes;
	}
}
