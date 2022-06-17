package KOWI2003.LaserMod.handlers;

import KOWI2003.LaserMod.items.interfaces.IItemColorable;
import KOWI2003.LaserMod.tileentities.IColorable;
import KOWI2003.LaserMod.utils.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;

public class ColorHandler {

	public static class BlockEntity implements IBlockColor {
		@Override
		public int getColor(BlockState state, IBlockDisplayReader displayReader, BlockPos pos,
				int tintindex) {
			TileEntity te = displayReader.getBlockEntity(pos);
			if(te instanceof IColorable) {
				float[] RGB = ((IColorable)te).getColor(tintindex);
				return Utils.getHexIntFromRGB(RGB[0], RGB[1], RGB[2]);
			}
			return Utils.getHexIntFromRGB(1f, 1f, 1f);
		}
	}
	
	public static class Item implements IItemColor {

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
