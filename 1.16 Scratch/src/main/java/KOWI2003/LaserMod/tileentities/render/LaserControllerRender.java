package KOWI2003.LaserMod.tileentities.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import KOWI2003.LaserMod.Reference;
import KOWI2003.LaserMod.blocks.BlockHorizontal;
import KOWI2003.LaserMod.tileentities.TileEntityLaser;
import KOWI2003.LaserMod.tileentities.TileEntityLaser.MODE;
import KOWI2003.LaserMod.tileentities.TileEntityLaserController;
import KOWI2003.LaserMod.tileentities.TileEntityLaserProjector;
import KOWI2003.LaserMod.tileentities.TileEntityLaserProjector.PROJECTOR_MODES;
import KOWI2003.LaserMod.tileentities.TileEntityLaserProjector.ProjectorProperties.PROJECTOR_PROPERTY;
import KOWI2003.LaserMod.tileentities.render.models.LaserControllerLevers;
import KOWI2003.LaserMod.utils.RenderUtils;
import KOWI2003.LaserMod.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;

public class LaserControllerRender extends TileEntityRenderer<TileEntityLaserController> {

	public LaserControllerRender(TileEntityRendererDispatcher renderDispatcher) {
		super(renderDispatcher);
	}

	@Override
	public void render(TileEntityLaserController te, float partialTicks, MatrixStack matrix,
			IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
		ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/render/levers_and_switches.png");
		LaserControllerLevers levers = new LaserControllerLevers();
		Minecraft.getInstance().getTextureManager().bind(TEXTURE);
		RenderUtils.rotateMatrixForBlock(matrix, te.getBlockState().getValue(BlockHorizontal.FACING));
		float rot = 1.6f;
		if(te.isConnected()) {
			TileEntity tile = te.getControlTileEntity();
			boolean isActive = false;
			if(tile instanceof TileEntityLaser) {
				isActive = ((TileEntityLaser)tile).active;
				TileEntityLaser laser = ((TileEntityLaser)tile);
				levers.setValues(laser.red, laser.green, laser.blue, laser.mode.ordinal(), MODE.values().length-1);
			}
			if(tile instanceof TileEntityLaserProjector) {
				isActive = ((TileEntityLaserProjector)tile).isActive;
				TileEntityLaserProjector laser = ((TileEntityLaserProjector)tile);
				levers.setValues(laser.properties.getProperty(PROJECTOR_PROPERTY.HEIGHT),
						laser.properties.getProperty(PROJECTOR_PROPERTY.SCALE), 0.0f, laser.mode.ordinal(), PROJECTOR_MODES.values().length-1);
			}
			rot = isActive ? 0 : rot;
		}else levers.setValues(0.0f, 0.0f, 0.0f, 0.0f, 1.0f);
		levers.setRotationAngle(levers.MainLever, rot, 0, 0);
		levers.renderToBuffer(matrix, bufferIn.getBuffer(RenderType.entityTranslucent(TEXTURE)), combinedLightIn, combinedOverlayIn, 1.0f, 1.0f, 1.0f, 1.0f);	
		
		RenderSystem.enableDepthTest();
		//Screens
		matrix.pushPose();
		{	
			matrix.mulPose(Vector3f.ZN.rotationDegrees(180f));
			matrix.translate(-0.695, -0.72, 0);
			
			String mode = "NULL";

			if(te.isConnected()) {
				TileEntity tile = te.getControlTileEntity();
				if(tile instanceof TileEntityLaser) {
					mode = ((TileEntityLaser)tile).mode.getFormalName();
				}
				if(tile instanceof TileEntityLaserProjector) {
					mode = Utils.getFormalText(((TileEntityLaserProjector)tile).mode.name());
				}
			}
			
			RenderUtils.renderString(matrix, "MODE: " + mode, 0, 0, 0, 0.0033f);
		}
		matrix.popPose();
		
		if(te.isConnected()) {
			TileEntity tile = te.getControlTileEntity();
			if(tile instanceof TileEntityLaser) {
				matrix.pushPose();
				
				float scale = 0.195f;
				RenderSystem.enableDepthTest();
				RenderUtils.renderQuad(matrix, 0.211f, 0.32f, 0.01f, scale, scale, ((TileEntityLaser)tile).red, ((TileEntityLaser)tile).green, ((TileEntityLaser)tile).blue);
				
				matrix.popPose();
			}
		}
	}
}
