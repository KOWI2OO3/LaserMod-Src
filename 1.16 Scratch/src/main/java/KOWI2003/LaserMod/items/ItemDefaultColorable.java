package KOWI2003.LaserMod.items;

import KOWI2003.LaserMod.MainMod;
import KOWI2003.LaserMod.items.interfaces.IItemColorable;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class ItemDefaultColorable extends ItemDefault implements IItemColorable {

	float red, green, blue = 1.0f;
	
	public ItemDefaultColorable(Properties properties) {
		super(properties);
	}
	
	public ItemDefaultColorable() {
		this(new Item.Properties().tab(MainMod.blocks));
	}
	
	public ItemDefaultColorable(Properties properties, float defaultRed, float defaultGreen, float defaultBlue) {
		this(properties);
		red = defaultRed;
		green = defaultGreen;
		blue = defaultBlue;
	}
	
	public ItemDefaultColorable(float defaultRed, float defaultGreen, float defaultBlue) {
		this(new Item.Properties().tab(MainMod.blocks), defaultRed, defaultGreen, defaultBlue);
	}

	@Override
	public ItemStack getDefaultInstance() {
		return createItemStackWithColor(red, green, blue);
	}
	
	public ItemStack createItemStackWithColor(float red, float green, float blue) {
		return setColor(new ItemStack(this), red, green, blue);
	}
	
	public ItemStack setColor(ItemStack stack, float red, float green, float blue) {
		CompoundNBT nbt = stack.getOrCreateTag();
		nbt.putFloat("Red", red);
		nbt.putFloat("Green", green);
		nbt.putFloat("Blue", blue);
		stack.setTag(nbt);
		return stack;
	}
	
	public float[] getRGBColor(ItemStack stack) {
		float[] RGB = new float[] {1.0f, 1.0f, 1.0f};
		CompoundNBT nbt = stack.getOrCreateTag();
		if(nbt.contains("Red"))
			RGB[0] = nbt.getFloat("Red");
		if(nbt.contains("Green"))
			RGB[1] = nbt.getFloat("Green");
		if(nbt.contains("Blue"))
			RGB[2] = nbt.getFloat("Blue");
		return RGB;
	}
	
	@Override
	public float[] getRGB(ItemStack stack, int tintindex) {
		return getRGBColor(stack);
	}

}
