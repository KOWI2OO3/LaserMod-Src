package KOWI2003.LaserMod.container;

import java.util.Objects;

import KOWI2003.LaserMod.init.ModContainerTypes;
import KOWI2003.LaserMod.items.ItemUpgradeBase;
import KOWI2003.LaserMod.tileentities.TileEntityLaser;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerLaser extends Container {

	private final ItemStackHandler inventory;
	private TileEntityLaser te;
	
	public ContainerLaser(int windowId, PlayerInventory playerInv, PacketBuffer data) {
		this(windowId, playerInv, getTileEntity(playerInv, data));
	}
	
	public ContainerLaser(int windowId, PlayerInventory playerInv, final TileEntityLaser te) {
		this(windowId, playerInv, te.createHandler(), te);
		this.te = te;
	}
	
	public ContainerLaser(int windowId, PlayerInventory playerInv, ItemStackHandler inventory, TileEntityLaser te) {
		super(ModContainerTypes.LASER_CONTAINER_TYPE.get(), windowId);
		this.inventory = inventory;
		this.te = te;
		
		//(Block) Inventory Slots
	    for(int i = 0; i < 3; ++i) {
	    	for(int j = 0; j < 3; ++j) {
	    		this.addSlot(new SlotItemHandler(inventory, j + i * 3, 62 + j * 18, 17 + i * 18) {
	    			@Override
	    			public ItemStack onTake(PlayerEntity player, ItemStack stack) {
	    				if(stack.getItem() instanceof ItemUpgradeBase) {
	    					boolean removed =  te.remove((ItemUpgradeBase)stack.getItem(), false);
	    					return removed ? stack : ItemStack.EMPTY;
	    				}
	    				return super.onTake(player, stack);
	    			}
	    			
	    			@Override
	    			public boolean mayPlace(ItemStack stack) {
	    				if(stack.getItem() instanceof ItemUpgradeBase)
	    					return te.acceptsItem((ItemUpgradeBase)stack.getItem(), true);
	    				return false;
	    			}
	    			
	    			@Override
	    			public int getMaxStackSize() {
	    				return getMaxStackSize(null);
	    			}
	    			
	    			@Override
	    			public int getMaxStackSize(ItemStack stack) {
	    				return 1;
	    			}
	    		});
	    	}
	    }
	
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
	
	private static TileEntityLaser getTileEntity(final PlayerInventory playerInv, final PacketBuffer data) {
		Objects.requireNonNull(playerInv, "Player Inventory cannot be null!");
		Objects.requireNonNull(data, "Packet Buffer cannot be null!");
		final TileEntity te = playerInv.player.level.getBlockEntity(data.readBlockPos());
		if(te instanceof TileEntityLaser)
			return (TileEntityLaser) te;
		throw new IllegalStateException("Tile Entity is not a instance of a Laser Tile Entity");
	}
	
	public TileEntityLaser getTileEntity() {
		return te;
	}
	
	public boolean stillValid(PlayerEntity player) {
		return true;
		//return this.inventory.stillValid(player);
	}
	
}
