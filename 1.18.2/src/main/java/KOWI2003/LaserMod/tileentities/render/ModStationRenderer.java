package KOWI2003.LaserMod.tileentities.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import KOWI2003.LaserMod.blocks.BlockHorizontal;
import KOWI2003.LaserMod.init.ModItems;
import KOWI2003.LaserMod.items.ItemLaserToolBase;
import KOWI2003.LaserMod.items.tools.ItemLaserToolOpend;
import KOWI2003.LaserMod.tileentities.TileEntityModStation;
import KOWI2003.LaserMod.utils.LaserItemUtils;
import KOWI2003.LaserMod.utils.RenderUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemStack;

public class ModStationRenderer implements BlockEntityRenderer<TileEntityModStation> {
	private final BlockEntityRendererProvider.Context context;
	
	public ModStationRenderer(BlockEntityRendererProvider.Context context) {
		this.context = context;
	}

	@Override
	public void render(TileEntityModStation te, float partialTicks, PoseStack matrix, MultiBufferSource bufferIn,
			int combinedLightIn, int combinedOverlayIn) {
		RenderSystem.enableDepthTest();
		matrix.pushPose();
		matrix.translate(0.5, 0, 0.5f);
		RenderUtils.rotateMatrix(matrix, te.getBlockState().getValue(BlockHorizontal.FACING).getClockWise());
		
		if(te.handler.getStackInSlot(0).getItem() instanceof ItemLaserToolBase) {
			ItemStack stack = ((ItemLaserToolOpend)ModItems.LaserToolOpened.get()).getItemWithColor(LaserItemUtils.getColor(te.handler.getStackInSlot(0)));
			RenderUtils.renderItem(matrix, stack, -0.17f, 0.1f, -0.1f, 0.5f, bufferIn, combinedLightIn, combinedOverlayIn);
		}
		
		matrix.popPose();
	}
}
