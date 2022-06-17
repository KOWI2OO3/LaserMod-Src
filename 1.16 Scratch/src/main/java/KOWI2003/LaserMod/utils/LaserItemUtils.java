package KOWI2003.LaserMod.utils;

import KOWI2003.LaserMod.LaserProperties;
import KOWI2003.LaserMod.items.interfaces.IChargable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class LaserItemUtils {

	public static ItemStack setProperties(ItemStack stack, LaserProperties properties) {
		CompoundNBT nbt = stack.getOrCreateTag();
		properties.save(nbt);
		stack.setTag(nbt);
		return stack;
	}
	
	public static LaserProperties getProperties(ItemStack stack) {
		CompoundNBT nbt = stack.getTag();
		LaserProperties props = new LaserProperties();
		if(nbt != null)
			props.load(nbt);
		return props;
	}
	
	public static int getCharge(ItemStack stack) {
		CompoundNBT nbt = stack.getTag();
		if(nbt == null)
			return 0;
		if(nbt.contains("charge"))
			return nbt.getInt("charge");
		return 0;
	}
	
	public static ItemStack setCharge(ItemStack stack, int value) {
		CompoundNBT nbt = stack.getOrCreateTag();
		nbt.putInt("charge", value);
		stack.setTag(nbt);
		return stack;
	}
	
	public static float[] getColor(ItemStack stack) {
		CompoundNBT nbt = stack.getTag();
		if(nbt == null)
			return new float[] {1, 1, 1};
		float red = 1f;
		if(nbt.contains("Red"))
			red = nbt.getFloat("Red");
		float green = 1f;
		if(nbt.contains("Green"))
			green= nbt.getFloat("Green");
		float blue = 1f;
		if(nbt.contains("Blue"))
			blue = nbt.getFloat("Blue");
		return new float[] {red, green, blue};
	}
	
	public static ItemStack setColor(ItemStack stack, float red, float green, float blue) {
		CompoundNBT nbt = stack.getOrCreateTag();
		nbt.putFloat("Red", red);
		nbt.putFloat("Green", green);
		nbt.putFloat("Blue", blue);
		stack.setTag(nbt);
		return stack;
	}
	
	public static float getDurabilityMul(ItemStack stack) {
		LaserProperties prop = getProperties(stack);
		if(prop.hasProperty(LaserProperties.Properties.DURABILITY))
			return prop.getProperty(LaserProperties.Properties.DURABILITY);
		return 1.0f;
	}
	
	public static boolean isExtended(ItemStack stack) {
		CompoundNBT nbt = stack.getTag();
		if(nbt == null)
			return false;
		if(nbt.contains("extended"))
			return nbt.getBoolean("extended");
		return false;
	}
	
	public static ItemStack setExtended(ItemStack stack, boolean value) {
		boolean canExtend = true;
		if(stack.getItem() instanceof IChargable)
			canExtend = getCharge(stack) > 0;
		CompoundNBT nbt = stack.getOrCreateTag();
		nbt.putBoolean("extended", canExtend ? value : false);
		stack.setTag(nbt);
		return stack;
	}
	
	public static double getDurabilityForDisplay(ItemStack stack) {
		if(stack.getItem() instanceof IChargable)
			return 1d - (double)getCharge(stack)/(double)((IChargable)stack.getItem()).getMaxCharge();
		return 0.0d;
	}
	
}
