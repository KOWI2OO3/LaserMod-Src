package KOWI2003.LaserMod.container;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import KOWI2003.LaserMod.container.slots.MemorySlot;
import KOWI2003.LaserMod.init.ModContainerTypes;
import KOWI2003.LaserMod.tileentities.TileEntityLaserCrafter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

public class ContainerLaserCrafter extends Container {

	private final ItemStackHandler inventory;
	private TileEntityLaserCrafter te;
	
	protected List<MemorySlot> fakeInputs;
	public MemorySlot fakeOutput;
	
	public ContainerLaserCrafter(int windowId, PlayerInventory playerInv, PacketBuffer data) {
		this(windowId, playerInv, getTileEntity(playerInv, data));
	}
	
	public ContainerLaserCrafter(int windowId, PlayerInventory playerInv, final TileEntityLaserCrafter te) {
		this(windowId, playerInv, te.inv, te);
		this.te = te;
	}
	
	public ContainerLaserCrafter(int windowId, PlayerInventory playerInv, ItemStackHandler inventory, TileEntityLaserCrafter te) {
		super(ModContainerTypes.LASER_CRAFTER_TYPE.get(), windowId);
		this.inventory = inventory;
		this.te = te;
		fakeInputs = new LinkedList<>();
		
		int xOffset = 0;
		int yOffset = 0;
		
		//(Block) Inventory Slots
	    for(int i = 0; i < 3; ++i) {
	    	for(int j = 0; j < 3; ++j) {
	    		MemorySlot slot = new MemorySlot(62 + j * 18 + xOffset, 17 + i * 18 + yOffset) {
	    			@Override
	    			public boolean mayPlace(ItemStack stack) {
	    				boolean output = super.mayPlace(stack);
	    				update(te.getLevel());
	    				return output;
	    			}
	    			
	    			@Override
	    			public boolean mayPickup(PlayerEntity playerIn) {
	    				boolean output = super.mayPickup(playerIn);
	    				update(te.getLevel());
	    				return output;
	    			}
	    		};
	    		this.addSlot(slot);
	    		fakeInputs.add(slot);
	    	}
	    }
	    
	    fakeOutput = new MemorySlot(62 + 4 * 18 + xOffset, 17 + 1 * 18 + yOffset) {
	    	@Override
	    	public boolean mayPickup(PlayerEntity playerIn) {
	    		return false;
	    	}
	    };
	    this.addSlot(fakeOutput);
	
	    //Player Inventory Slots
	    for(int k = 0; k < 3; ++k) {
	    	for(int i1 = 0; i1 < 9; ++i1) {
	    		this.addSlot(new Slot(playerInv, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18));
	    	}
	    }
	      
	    //Player Hotbar Slots
	    for(int l = 0; l < 9; ++l) {
	    	this.addSlot(new Slot(playerInv, l, 8 + l * 18, 142));
	    }
	}

	@Override
	public ItemStack quickMoveStack(PlayerEntity player, int index) {
	      Slot slot = this.slots.get(index);
	      return ItemStack.EMPTY;
	}
	
	private static TileEntityLaserCrafter getTileEntity(final PlayerInventory playerInv, final PacketBuffer data) {
		Objects.requireNonNull(playerInv, "Player Inventory cannot be null!");
		Objects.requireNonNull(data, "Packet Buffer cannot be null!");
		final TileEntity te = playerInv.player.level.getBlockEntity(data.readBlockPos());
		if(te instanceof TileEntityLaserCrafter)
			return (TileEntityLaserCrafter) te;
		throw new IllegalStateException("Tile Entity is not a instance of a Laser Crafter Tile Entity");
	}
	
	public TileEntityLaserCrafter getTileEntity() {
		return te;
	}
	
	public List<ICraftingRecipe> possibleRecipes = new ArrayList<ICraftingRecipe>();
	public ICraftingRecipe selectedRecipe = null;
	
	public void update(World level) {
		possibleRecipes.clear();
		selectedRecipe = null;
		boolean findMultiple = true;
		
		List<ICraftingRecipe> recipes = level.getRecipeManager().getAllRecipesFor(IRecipeType.CRAFTING);
		try {
			CraftingInventory inv = new CraftingInventory(this, 3, 3);
			for(int i = 0; i < fakeInputs.size(); i++) {
				inv.setItem(i, fakeInputs.get(i).getItem());
			}
			
			boolean hasFoundRecipe = false;
			for (ICraftingRecipe recipe : recipes) {
				if(recipe.matches(inv, level)) {
					if(!hasFoundRecipe) {
						fakeOutput.set(recipe.getResultItem());
						selectedRecipe = recipe;
					}
					hasFoundRecipe = true;
					possibleRecipes.add(recipe);
					if(!findMultiple)
						break;
				}
			}
			if(!hasFoundRecipe)
				fakeOutput.set(ItemStack.EMPTY);
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public boolean stillValid(PlayerEntity player) {
//		update(player.level);
		
		return true;
		//return this.inventory.stillValid(player);
	}
}
