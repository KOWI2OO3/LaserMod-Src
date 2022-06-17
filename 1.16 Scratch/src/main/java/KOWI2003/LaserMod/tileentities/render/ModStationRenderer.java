package KOWI2003.LaserMod.tileentities.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import KOWI2003.LaserMod.blocks.BlockHorizontal;
import KOWI2003.LaserMod.init.ModItems;
import KOWI2003.LaserMod.items.ItemLaserToolBase;
import KOWI2003.LaserMod.items.tools.ItemLaserToolOpend;
import KOWI2003.LaserMod.tileentities.TileEntityModStation;
import KOWI2003.LaserMod.utils.LaserItemUtils;
import KOWI2003.LaserMod.utils.RenderUtils;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;

public class ModStationRenderer extends TileEntityRenderer<TileEntityModStation> {

	public ModStationRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn);
	}

	@Override
	public void render(TileEntityModStation te, float partialTicks, MatrixStack matrix,
			IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
		RenderSystem.enableDepthTest();
		matrix.pushPose();
		matrix.translate(0.5, 0, 0.5f);
		RenderUtils.rotateMatrix(matrix, te.getBlockState().getValue(BlockHorizontal.FACING).getClockWise());
		
		if(te.handler.getStackInSlot(0).getItem() instanceof ItemLaserToolBase) {
			ItemStack stack = ((ItemLaserToolOpend)ModItems.LaserToolOpened).getItemWithColor(LaserItemUtils.getColor(te.handler.getStackInSlot(0)));
			RenderUtils.renderItem(matrix, stack, -0.17f, 0.1f, -0.1f, 0.5f, bufferIn, combinedLightIn, combinedOverlayIn);
		}
		
		matrix.popPose();
	}
}
