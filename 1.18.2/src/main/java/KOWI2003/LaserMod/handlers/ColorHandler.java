package KOWI2003.LaserMod.handlers;

import KOWI2003.LaserMod.init.ModBlocks;
import KOWI2003.LaserMod.items.interfaces.IItemColorable;
import KOWI2003.LaserMod.tileentities.IColorable;
import KOWI2003.LaserMod.utils.Utils;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ColorHandler {

	public static class Block implements BlockColor {
		@Override
		public int getColor(BlockState state, BlockAndTintGetter displayReader, BlockPos pos, int tintindex) {
			BlockEntity te = displayReader.getBlockEntity(pos);
			if(te instanceof IColorable) {
				float[] RGB = ((IColorable)te).getColor(tintindex);
				if(state.getBlock() == ModBlocks.LaserCatcher.get())
					System.out.println(RGB[0] + ", " + RGB[1] + ", " + RGB[2]);
				return Utils.getHexIntFromRGB(RGB[0], RGB[1], RGB[2]);
			}
			return Utils.getHexIntFromRGB(1f, 1f, 1f);
		}
	}
	
	public static class Item implements ItemColor {

		@Override
		public int getColor(ItemStack stack, int tintindex) {
			if(stack.getItem() instanceof IItemColorable) {
				float[] RGB = ((IItemColorable)stack.getItem()).getRGB(stack, tintindex);
				return Utils.getHexIntFromRGB(RGB[0], RGB[1], RGB[2]);
			}
			return Utils.getHexIntFromRGB(1f, 1f, 1f);
		}
	}
}
