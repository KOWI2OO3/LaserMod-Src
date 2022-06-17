package KOWI2003.LaserMod.container.slots;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class MemorySlot extends SlotItemHandler {

	private ItemStack memory = ItemStack.EMPTY;
	
	protected MemorySlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
		super(itemHandler, index, xPosition, yPosition);
	}
	
	public MemorySlot(int xPosition, int yPosition) {
		super(null, 0, xPosition, yPosition);
	}
	
	public MemorySlot(int xPosition, int yPosition, ItemStack savedStack) {
		super(null, 0, xPosition, yPosition);
		memory = savedStack;
	}
	
	public void setStack(ItemStack stack) { memory = stack; }
	public ItemStack getStack() { return memory; }
	
	@Override
	public ItemStack getItem() {
		return memory;
	}
	
	public boolean isEmpty() { return memory == ItemStack.EMPTY; }
	
	@Override
	public void set(ItemStack stack) {
		setStack(stack.copy());
		memory.setCount(1);
	}
	
	@Override
	public ItemStack remove(int amount) {
		setStack(ItemStack.EMPTY.copy());
		return ItemStack.EMPTY;
	}
	
	@Override
	public boolean mayPlace(ItemStack stack) {
		if(isEmpty())
			set(stack);
		else
			setStack(ItemStack.EMPTY.copy());
		return false;
	}
	
	@Override
	public boolean mayPickup(PlayerEntity playerIn) {
		if(!isEmpty())
			setStack(ItemStack.EMPTY.copy());
		return false;
	}
	
	@Override
	public boolean isSameInventory(Slot p_isSameInventory_1_) {
		return false;
	}
	
	@Override
	public IItemHandler getItemHandler() {
		return null;
	}
	
	@Override
	public int getMaxStackSize() {
		return 1;
	}
	
	@Override
	public int getSlotIndex() {
		return 0;
	}
	
	@Override
	public int getMaxStackSize(ItemStack stack) {
		return 1;
	}

}
