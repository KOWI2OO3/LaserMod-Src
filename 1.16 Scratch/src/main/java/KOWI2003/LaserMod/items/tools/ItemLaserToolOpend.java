package KOWI2003.LaserMod.items.tools;

import KOWI2003.LaserMod.items.ItemDefault;
import KOWI2003.LaserMod.items.interfaces.IItemColorable;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class ItemLaserToolOpend extends ItemDefault implements IItemColorable {

	public ItemLaserToolOpend() {
		super(new Item.Properties());
	}

	public ItemStack getItemWithColor(float[] RGB) {
		ItemStack stack = new ItemStack(this);
		CompoundNBT nbt = new CompoundNBT();
		nbt.putFloat("Red", RGB[0]);
		nbt.putFloat("Green", RGB[1]);
		nbt.putFloat("Blue", RGB[2]);
		stack.setTag(nbt);
		return stack;
	}
	
	@Override
	public float[] getRGB(ItemStack stack, int tintindex) {
		CompoundNBT nbt = stack.getOrCreateTag();
		float[] RGB = new float[] {1.0f, 1.0f, 1.0f};
		if(nbt.contains("Red"))
			RGB[0] = nbt.getFloat("Red");
		if(nbt.contains("Green"))
			RGB[1] = nbt.getFloat("Green");
		if(nbt.contains("Blue"))
			RGB[2] = nbt.getFloat("Blue");
		return RGB;
	}
	
}
