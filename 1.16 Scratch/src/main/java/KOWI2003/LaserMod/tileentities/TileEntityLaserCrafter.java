package KOWI2003.LaserMod.tileentities;

import java.util.LinkedList;
import java.util.List;

import KOWI2003.LaserMod.blocks.BlockHorizontal;
import KOWI2003.LaserMod.container.ContainerLaserCrafter;
import KOWI2003.LaserMod.init.ModTileTypes;
import KOWI2003.LaserMod.utils.TileEntityUtils;
import KOWI2003.LaserMod.utils.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullSupplier;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityLaserCrafter extends TileEntity implements ITickableTileEntity, INamedContainerProvider {

	public ItemStackHandler inv;
	public ItemStackHandler output;
	
	public List<IRecipe<?>> recipes;
	public boolean loadRecipes = false;
	
	public List<String> recipeIDs;
	
	public TileEntityLaserCrafter() {
		super(ModTileTypes.LASER_CRAFTER);
		recipes = new LinkedList<IRecipe<?>>();
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
	public void tick() {
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
			}else if(level.getBlockState(dropPos).hasTileEntity()) {
				TileEntity te = level.getBlockEntity(dropPos);
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
		
		for (IRecipe<?> recipe : recipes) {
			if(isRecipeValid(recipe)) {
				output.insertItem(0, recipe.getResultItem().copy(), false);
				break;
			}
		}
	}
	
	//Furnace Upgrade??? use fire Upgrade
	//Standard Crafting Recipes
	//TODO: Worker, Laser Requirements
	public boolean isRecipeValid(IRecipe<?> recipe) {
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
				recipes = new LinkedList<IRecipe<?>>();
			recipes.clear();
			List<ICraftingRecipe> recipeList = level.getRecipeManager().getAllRecipesFor(IRecipeType.CRAFTING);
			for (String id : recipeIDs) {
				for(ICraftingRecipe recipe : recipeList) {
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
	public CompoundNBT save(CompoundNBT nbt) {
		nbt.put("inv", inv.serializeNBT());
		nbt.put("output", output.serializeNBT());
		
		if(loadRecipes) {
			if(recipes == null)
				recipes = new LinkedList<IRecipe<?>>();
			List<String> recipeIDs = new LinkedList<>();
			for (IRecipe<?> recipe : recipes)
				recipeIDs.add(recipe.getId().toString());
			CompoundNBT recipeList = Utils.putObjectArray(recipeIDs.toArray(new String[] {}));
			nbt.put("recipes", recipeList);
		}
		return super.save(nbt);
	}
	
	@Override
	public void load(BlockState state, CompoundNBT nbt) {
		inv.deserializeNBT(nbt.getCompound("inv"));
		output.deserializeNBT(nbt.getCompound("output"));
		
		if(nbt.contains("recipes")) {
			if(!loadRecipes) {
				loadRecipes = true;
			}
			CompoundNBT recipeList = nbt.getCompound("recipes");
			Object[] IDs = Utils.getObjectArray(recipeList);
			recipeIDs.clear();
			for (Object id : IDs) {
				if(id instanceof String)
					recipeIDs.add((String)id);
			}
		}
		super.load(state, nbt);
	}

	@Override
	public Container createMenu(int windowId, PlayerInventory playerInv, PlayerEntity player) {
		return new ContainerLaserCrafter(windowId, playerInv, this);
	}
	
	@Override
	public ITextComponent getDisplayName() {
		return new TranslationTextComponent("block.lasermod.laser_crafter");
	}

	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		return new SUpdateTileEntityPacket(getBlockPos(), 0, getUpdateTag());
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		this.load(getLevel().getBlockState(pkt.getPos()), pkt.getTag());
	}
	
	@Override
	public CompoundNBT getUpdateTag() {
		return save(new CompoundNBT());
	}
	
	@Override
	public void handleUpdateTag(BlockState state, CompoundNBT tag) {
		this.load(state, tag);
	}
	
	@Override
	public CompoundNBT getTileData() {
		return getUpdateTag();
	}
	
	public void sync()
    {
        TileEntityUtils.syncToClient(this);
    }
	
}
