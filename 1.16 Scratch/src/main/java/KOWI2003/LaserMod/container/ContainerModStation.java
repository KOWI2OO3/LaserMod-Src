package KOWI2003.LaserMod.container;

import java.util.Objects;

import KOWI2003.LaserMod.init.ModContainerTypes;
import KOWI2003.LaserMod.items.ItemUpgradeBase;
import KOWI2003.LaserMod.items.interfaces.ILaserUpgradable;
import KOWI2003.LaserMod.tileentities.TileEntityModStation;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerModStation extends Container {

	private TileEntityModStation te;
	
	public ContainerModStation(int windowId, PlayerInventory playerInv, PacketBuffer data) {
		this(windowId, playerInv, getTileEntity(playerInv, data));
	}
	
	public ContainerModStation(int windowId, PlayerInventory playerInv, TileEntityModStation te) {
		super(ModContainerTypes.MOD_STATION_CONTAINER_TYPE.get(), windowId);
		this.te = te;
		
		//(Block) Inventory Slots
		this.addSlot(new SlotItemHandler(te.handler, 0, 134, 16) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return stack.getItem() instanceof ILaserUpgradable;
			}
		});
		this.addSlot(new SlotItemHandler(te.handler, 1, 134, 54) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return stack.getItem() instanceof ItemUpgradeBase;
			}
		});
	    
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
	
	private static TileEntityModStation getTileEntity(final PlayerInventory playerInv, final PacketBuffer data) {
		Objects.requireNonNull(playerInv, "Player Inventory cannot be null!");
		Objects.requireNonNull(data, "Packet Buffer cannot be null!");
		final TileEntity te = playerInv.player.level.getBlockEntity(data.readBlockPos());
		if(te instanceof TileEntityModStation)
			return (TileEntityModStation) te;
		throw new IllegalStateException("Tile Entity is not a instance of a Modification Station Tile Entity");
	}
	
	public TileEntityModStation getTileEntity() {
		return te;
	}
	
	public boolean stillValid(PlayerEntity player) {
		return true;
	}
}
