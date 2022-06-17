package KOWI2003.LaserMod.container;

import java.util.Objects;

import KOWI2003.LaserMod.init.ModContainerTypes;
import KOWI2003.LaserMod.tileentities.TileEntityLaserProjector;
import KOWI2003.LaserMod.tileentities.TileEntityLaserProjector.PROJECTOR_MODES;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerLaserProjector extends AbstractContainerMenu {
	
	private final ItemStackHandler inventory;
	private TileEntityLaserProjector te;
	
	public ContainerLaserProjector(int windowId, Inventory playerInv, FriendlyByteBuf data) {
		this(windowId, playerInv, getTileEntity(playerInv, data));
	}
	
	public ContainerLaserProjector(int windowId, Inventory playerInv, final TileEntityLaserProjector te) {
		this(windowId, playerInv, te.handler, te);
		this.te = te;
	}
	
	public ContainerLaserProjector(int windowId, Inventory playerInv, ItemStackHandler inventory, TileEntityLaserProjector te) {
		super(ModContainerTypes.LASER_PROJECTOR_TYPE.get(), windowId);
		this.inventory = inventory;
		this.te = te;
		
		//(Block) Inventory Slots
	    this.addSlot(new SlotItemHandler(inventory, 0, 80, 30) {
	    	@Override
	    	public boolean isActive() {
	    		return te.mode == PROJECTOR_MODES.ITEM;
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
	public ItemStack quickMoveStack(Player player, int index) {
	      Slot slot = this.slots.get(index);
	      return ItemStack.EMPTY;
	}
	
	private static TileEntityLaserProjector getTileEntity(final Inventory playerInv, final FriendlyByteBuf data) {
		Objects.requireNonNull(playerInv, "Player Inventory cannot be null!");
		Objects.requireNonNull(data, "Packet Buffer cannot be null!");
		final BlockEntity te = playerInv.player.level.getBlockEntity(data.readBlockPos());
		if(te instanceof TileEntityLaserProjector)
			return (TileEntityLaserProjector) te;
		throw new IllegalStateException("Tile Entity is not a instance of a Laser Projector Tile Entity");
	}
	
	public TileEntityLaserProjector getTileEntity() {
		return te;
	}
	
	public boolean stillValid(Player player) {
		return true;
		//return this.inventory.stillValid(player);
	}
}
