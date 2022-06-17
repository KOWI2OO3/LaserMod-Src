package KOWI2003.LaserMod.items.render;

import com.mojang.blaze3d.matrix.MatrixStack;

import KOWI2003.LaserMod.init.ModItems;
import KOWI2003.LaserMod.items.ItemLinker;
import KOWI2003.LaserMod.utils.RenderUtils;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3f;

public class RenderLinker extends ItemStackTileEntityRenderer {

	@Override
	public void renderByItem(ItemStack stack, TransformType transformType, MatrixStack matrix,
			IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
		
		matrix.pushPose();
		
		matrix.translate(0.5, 0.5, 0.5);
		Minecraft.getInstance().getItemRenderer().renderStatic(new ItemStack(ModItems.LinkerModel), transformType, combinedLight, combinedOverlay, matrix, buffer);
		
		matrix.mulPose(Vector3f.ZN.rotationDegrees(180));
		matrix.translate(-0.085, -0.14, -0.035);
		
		BlockPos pos = ItemLinker.getPos(stack);
		String blockName = "NULL";
		float scale = 0.003f;
		if(pos != null) {
			Block block = Minecraft.getInstance().player.level.getBlockState(pos).getBlock();
			blockName = block.getName().getString();
		}
		if(blockName.length() > 5) {
			matrix.translate(0, 0.003, 0);
			scale = 0.002f;
		}
		
		RenderUtils.renderString(matrix, blockName, 0, 0, 0, scale);
		
		scale = 0.003f;
		float height = 0;
		
		if(pos != null) {
			if((pos.getX() + "").length() > 5) {
				scale = scale - 0.00039f * ((float)(pos.getX() + "").length() - 5f);
				height = (0.0025f * ((float)(pos.getX() + "").length() - 5f)) / 2f;
			}
			RenderUtils.renderString(matrix, pos.getX() + "", 0f, 0.095f + height, 0, scale);
			scale = 0.003f;
			height = 0;
			if((pos.getY() + "").length() > 5) {
				scale = scale - 0.00034f * ((float)(pos.getY() + "").length() - 5f);
				height = (0.0025f * ((float)(pos.getY() + "").length() - 5f)) / 2f;
			}
			RenderUtils.renderString(matrix, pos.getY() + "", 0f, 0.182f + height, 0, scale);
			scale = 0.003f;
			height = 0;
			if((pos.getZ() + "").length() > 5) {
				scale = scale - 0.00034f * ((float)(pos.getZ() + "").length() - 5f);
				height = (0.0025f * ((float)(pos.getZ() + "").length() - 5f)) / 2f;
			}
			RenderUtils.renderString(matrix, pos.getZ() + "", 0f, 0.269f + height, 0, scale);
		
		}
		
		
		matrix.popPose();
	}
}
