package KOWI2003.LaserMod.tileentities;

import java.util.LinkedList;
import java.util.List;

import KOWI2003.LaserMod.blocks.BlockHorizontal;
import KOWI2003.LaserMod.container.ContainerLaserCrafter;
import KOWI2003.LaserMod.init.ModTileTypes;
import KOWI2003.LaserMod.utils.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullSupplier;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityLaserCrafter extends SyncableBlockEntity implements BlockEntityTicker<TileEntityLaserCrafter>, MenuProvider {

	public ItemStackHandler inv;
	public ItemStackHandler output;
	
	public List<Recipe<?>> recipes;
	public boolean loadRecipes = false;
	
	public List<String> recipeIDs;
	
	public TileEntityLaserCrafter(BlockPos pos, BlockState state) {
		super(ModTileTypes.LASER_CRAFTER, pos, state);
		recipes = new LinkedList<Recipe<?>>();
		recipeIDs = new LinkedList<String>();
		inv = new ItemStackHandler(18);
		output = new ItemStackHandler(1) {
			@Override
			public boolean isItemValid(int slot, ItemStack stack) {
				return true;
			}
		};
	}

	@Override
	public void tick(Level level, BlockPos pos, BlockState state, TileEntityLaserCrafter tile) {
		if(recipeIDs.size() > 0)
			loadRecipeFromTemporaryList();
		
		Direction dir = getBlockState().getValue(BlockHorizontal.FACING);
		if(!output.getStackInSlot(0).isEmpty()) {
			BlockPos dropPos = getBlockPos().relative(dir.getClockWise(), 1);
			Direction outputDir = dir.getClockWise();
			if(level.getBlockState(dropPos).getBlock() == Blocks.AIR) {
				double offset = .7;
				ItemEntity item = new ItemEntity(level, getBlockPos().getX()+ .5 + outputDir.getStepX()*offset, getBlockPos().getY() + .3 + outputDir.getStepY()*offset, getBlockPos().getZ() + .5 + outputDir.getStepZ()*offset, output.getStackInSlot(0));
				item.setDeltaMovement(0, 0, 0);
				level.addFreshEntity(item);
				item.setDeltaMovement(0, 0, 0);
				output.setStackInSlot(0, ItemStack.EMPTY);
			}else if(level.getBlockState(dropPos).hasBlockEntity()) {
				BlockEntity te = level.getBlockEntity(dropPos);
				LazyOptional<IItemHandler> optional = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, outputDir.getOpposite());
				if(optional != null) {
					IItemHandler otherInv = optional.orElse(null);
					if(otherInv != null) {
						ItemStack stack = output.getStackInSlot(0);
						for(int i = 0; i < otherInv.getSlots(); i++) {
							stack = otherInv.insertItem(i, stack, false);
							output.setStackInSlot(0, stack);
							if(stack == ItemStack.EMPTY)
								break;
						}
					}
				}
			}
		}
		
		for (Recipe<?> recipe : recipes) {
			if(isRecipeValid(recipe)) {
				output.insertItem(0, recipe.getResultItem().copy(), false);
				break;
			}
		}
	}
	
	//Furnace Upgrade??? use fire Upgrade
	//Standard Crafting Recipes
	//TODO: Worker, Laser Requirements
	public boolean isRecipeValid(Recipe<?> recipe) {
		if(!(output.getStackInSlot(0).isEmpty() || (output.getStackInSlot(0).getItem() == recipe.getResultItem().getItem() && 
				output.getStackInSlot(0).getCount() + recipe.getResultItem().getCount() <= output.getStackInSlot(0).getMaxStackSize())))
			return false;
		boolean isRecipeValid = true;
		List<ItemStack> removed = new LinkedList<ItemStack>();
		for(Ingredient ingredient : recipe.getIngredients()) {
			boolean isfound = ingredient.getItems().length <= 0;
			if(isfound)
				continue;
			
			for(int i = 0; i < inv.getSlots(); i++) {
				if(ingredient.test(inv.getStackInSlot(i))) {
					ItemStack temp = inv.getStackInSlot(i).copy();
					temp.setCount(ingredient.getItems()[0].getCount());
					removed.add(temp);
					isfound = true;
					inv.extractItem(i, temp.getCount(), false);
					break;
				}
			}
			
			if(!isfound)
				isRecipeValid = false;
		}
		if(!isRecipeValid) {
			for (ItemStack itemStack : removed) {
				ItemStack stack = itemStack.copy();
				for(int i = 0; i < inv.getSlots(); i++) {
					stack = inv.insertItem(i, itemStack, false);
					if(stack.isEmpty())
						break;
				}
			}
		}else {
			for (ItemStack itemStack : removed) {
				if(itemStack.hasContainerItem()) {
					ItemStack temp = itemStack.getContainerItem();
					for (int i = 0; i < inv.getSlots(); i++) {
						temp = inv.insertItem(i, temp, false);
						if(temp.isEmpty())
							break;
					}
				}
			}
		}
		return isRecipeValid;
	}
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		Direction dir = getBlockState().getValue(BlockHorizontal.FACING);
		if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
		if(side == dir.getClockWise())
			//output
			return LazyOptional.of(new NonNullSupplier<T>() {
				@Override
				public T get() {
					return (T)output;
				}
			});
		if(side == dir.getCounterClockWise())
			//input
			return LazyOptional.of(new NonNullSupplier<T>() {
				@Override
				public T get() {
					return (T)inv;
				}
			});
		}
		return super.getCapability(cap, side);
	}
	
	public void loadRecipeFromTemporaryList() {
		if(loadRecipes) {
			if(recipes == null)
				recipes = new LinkedList<Recipe<?>>();
			recipes.clear();
			List<CraftingRecipe> recipeList = level.getRecipeManager().getAllRecipesFor(RecipeType.CRAFTING);
			for (String id : recipeIDs) {
				for(CraftingRecipe recipe : recipeList) {
					if(recipe.getId().toString().equals(id)) {
						recipes.add(recipe);
						break;
					}
				}
			}
			recipeIDs.clear();
		}
	}

	@Override
	protected void saveAdditional(CompoundTag nbt) {
		nbt.put("inv", inv.serializeNBT());
		nbt.put("output", output.serializeNBT());
		
		if(loadRecipes) {
			if(recipes == null)
				recipes = new LinkedList<Recipe<?>>();
			List<String> recipeIDs = new LinkedList<>();
			for (Recipe<?> recipe : recipes)
				recipeIDs.add(recipe.getId().toString());
			CompoundTag recipeList = Utils.putObjectArray(recipeIDs.toArray(new String[] {}));
			nbt.put("recipes", recipeList);
		}
		super.saveAdditional(nbt);
	}
	
	@Override
	public void load(CompoundTag nbt) {
		inv.deserializeNBT(nbt.getCompound("inv"));
		output.deserializeNBT(nbt.getCompound("output"));
		
		if(nbt.contains("recipes")) {
			if(!loadRecipes) {
				loadRecipes = true;
			}
			CompoundTag recipeList = nbt.getCompound("recipes");
			Object[] IDs = Utils.getObjectArray(recipeList);
			recipeIDs.clear();
			for (Object id : IDs) {
				if(id instanceof String)
					recipeIDs.add((String)id);
			}
		}
		super.load(nbt);
	}

	@Override
	public AbstractContainerMenu createMenu(int windowId, Inventory playerInv, Player player) {
		return new ContainerLaserCrafter(windowId, playerInv, this);
	}
	
	@Override
	public Component getDisplayName() {
		return new TranslatableComponent("block.lasermod.laser_crafter");
	}
	
}
