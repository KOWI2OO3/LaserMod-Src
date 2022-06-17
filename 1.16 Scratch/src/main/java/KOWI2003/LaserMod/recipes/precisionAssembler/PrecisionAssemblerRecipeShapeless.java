package KOWI2003.LaserMod.recipes.precisionAssembler;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ITag;
import net.minecraftforge.items.ItemStackHandler;

public class PrecisionAssemblerRecipeShapeless implements IPrecisionAssemblerRecipe {

	ItemStack output;
	Object[] inputs;
	Object inputBase;
	float speed;
	
	public PrecisionAssemblerRecipeShapeless(ItemStack output, float speed, Object inputBase, Object... inputs) {
		this.inputs = inputs;
		this.output = output;
		this.speed = speed;
		this.inputBase = inputBase;
	}
	
	@Override
	public Object[] getInputs() {
		return inputs;
	}

	@Override
	public Object getInputBase() {
		return inputBase;
	}

	@Override
	public ItemStack getOutput() {
		return output;
	}

	@Override
	public float getRecipeSpeed() {
		return speed;
	}
	
	@Override
	public boolean isRecipeValid(ItemStackHandler handler) {
		if(handler != null) {
			boolean condition = true;
			Object[] inputs = getInputs();
			List<Integer> slots = new ArrayList<Integer>();
			for (int i = 0; i < inputs.length; i++) {
				Object obj = inputs[i];
				if(obj instanceof ItemStack) {
					ItemStack stack = (ItemStack)obj;
					boolean con = false;
					for(int j = 0; j < handler.getSlots() - 2; j++) {
						ItemStack slot = handler.getStackInSlot(j).copy();
						if(condition && stack.isEmpty() ? slot.isEmpty() : (stack.getItem() == slot.getItem() && stack.getCount() <= slot.getCount())) {
							con = true;
							break;
						}else
							slots.add(j);
					}
					if(!con)
						return false;
				}else if(obj instanceof ITag<?>) {
					ITag<Item> tag = (ITag<Item>)obj;
					boolean con = false;
					for(int j = 0; j < handler.getSlots() - 2; j++) {
						ItemStack slot = handler.getStackInSlot(j).copy();
						if(condition && slot.getItem().is(tag) && slot.getCount() > 0) {
							con = true;
							break;
						}else
							slots.add(j);
					}
					if(!con)
						return false;
				}
			}
			for (int index : slots) {
				if(!handler.getStackInSlot(index).isEmpty())
					return false;
			}
			Object obj = getInputBase();
			if(obj instanceof ItemStack) {
				ItemStack inputBase = (ItemStack) obj;
				if(handler.getSlots() <= 0)
					return false;
				ItemStack slot = handler.getStackInSlot(3).copy();
				if(inputBase.isEmpty())
					return false;
				condition = condition && inputBase.isEmpty() ? slot.isEmpty() : (inputBase.getItem() == slot.getItem() && inputBase.getCount() <= slot.getCount());
				if(!condition)
					return false;
			}else if(obj instanceof ITag<?>) {
				ITag<Item> tag = (ITag<Item>)obj;
				ItemStack slot = handler.getStackInSlot(3).copy();
				condition = condition && slot.getItem().is(tag) && slot.getCount() > 0;
				if(!condition)
					return false;
			}else
				return false;
			
			ItemStack output = getOutput();
			if(handler.getSlots() <= 0)
				return false;
			ItemStack lastSlot = handler.getStackInSlot(4).copy();
			if(output.isEmpty())
				return false;
			condition = condition && lastSlot.isEmpty() ? true : (lastSlot.getItem() == output.getItem() && lastSlot.getCount() + output.getCount() <= output.getItem().getItemStackLimit(output));
			return condition;
		}
		return false;
	}
	
	@Override
	public String toString() {
		String inputs = "[";
		for (Object input : getInputs()) {
			inputs += input.toString() + ", ";
		}
		if(inputs.length() > 2) {
			inputs = inputs.substring(0, inputs.length() - 2);
		}
		inputs += "] + " + getInputBase().toString() + " -> " + getOutput();
		return inputs;
	}
}
