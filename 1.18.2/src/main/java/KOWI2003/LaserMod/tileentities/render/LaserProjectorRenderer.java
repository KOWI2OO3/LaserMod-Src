package KOWI2003.LaserMod.tileentities.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;

import KOWI2003.LaserMod.blocks.BlockHorizontal;
import KOWI2003.LaserMod.tileentities.TileEntityLaserProjector;
import KOWI2003.LaserMod.tileentities.TileEntityLaserProjector.PROJECTOR_MODES;
import KOWI2003.LaserMod.tileentities.TileEntityLaserProjector.ProjectorProperties.PROJECTOR_PROPERTY;
import KOWI2003.LaserMod.tileentities.render.LaserRender.LaserRenderType;
import KOWI2003.LaserMod.utils.RenderUtils;
import KOWI2003.LaserMod.utils.TileEntityUtils;
import KOWI2003.LaserMod.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.entity.player.Player;

public class LaserProjectorRenderer implements BlockEntityRenderer<TileEntityLaserProjector> {
	private final BlockEntityRendererProvider.Context context;
	
	public LaserProjectorRenderer(BlockEntityRendererProvider.Context context) {
		this.context = context;
	}
	
	@Override
	public void render(TileEntityLaserProjector te, float partialTicks, PoseStack matrix, MultiBufferSource bufferIn,
			int combinedLightIn, int combinedOverlayIn) {
		if(te.isActive) {
			
			for (int i = 0; i < 4; i++) {
				matrix.pushPose();
				matrix.translate(0.5, 0, 0.5);
				RenderUtils.rotateMatrix(matrix, 90f * i);
				matrix.translate(-0.5, 0, -0.5);
				
				RenderSystem.disableTexture();
				RenderSystem.enableDepthTest();
				RenderSystem.disableCull();
				RenderSystem.enableBlend();
//				GL11.glEnable(GL11.GL_ALPHA_TEST);
//				RenderSystem.enableAlphaTest();
				RenderSystem.depthMask(true);
				
				matrix.translate(0, 0.5, 0.7);
				
//				VertexConsumer buffer = bufferIn.getBuffer(ProjectorRenderType.PROJECTION_RENDER);
				VertexConsumer buffer = bufferIn.getBuffer(LaserRenderType.LASER_RENDER);
				Matrix4f matrix2 = matrix.last().pose();
				
				float[] RGB = new float[] {1.0f, 0.0f, 0.0f};
				float peel = 0.3f;
				float height = 0.4f;
				float bottomOffset = 0.3f;
				
				buffer.vertex(matrix2, 0 - (peel-0.3f), height, peel).color(RGB[0], RGB[1], RGB[2], 0f).uv(0, 1).endVertex();
				buffer.vertex(matrix2, 1 + (peel-0.3f), height, peel).color(RGB[0], RGB[1], RGB[2], 0f).uv(1, 1).endVertex();
				buffer.vertex(matrix2, 1 - bottomOffset, 0, 0f).color(RGB[0], RGB[1], RGB[2], 1f).uv(1,01).endVertex();
				buffer.vertex(matrix2, bottomOffset, 0, 0f).color(RGB[0], RGB[1], RGB[2], 1f).uv(0, 0).endVertex();
				
				RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
				
				RenderSystem.enableTexture();
				RenderSystem.enableCull();
				RenderSystem.disableBlend();
				
				matrix.popPose();
			}
			
			matrix.pushPose();
			if(te.mode == PROJECTOR_MODES.TEXT) {
				matrix.pushPose();
				
				String text = TileEntityUtils.StringCommands(Minecraft.getInstance().player, te.text);
				
				RenderSystem.disableCull();
				RenderUtils.rotateMatrixForBlock(matrix, te.getBlockState().getValue(BlockHorizontal.FACING));
				RenderUtils.rotateMatrixForBlock(matrix, te.properties.getProperty(PROJECTOR_PROPERTY.ROTATION)*360 + (te.doesRotate ? te.rotation : 0));
				
				matrix.mulPose(Vector3f.ZP.rotationDegrees(180f));
				
				float scale = 0.025f * te.properties.getProperty(PROJECTOR_PROPERTY.SCALE);
				matrix.translate(-0.5f - text.length()/2f * scale*5f, -1.1 - (scale*5f/2f) - te.properties.getProperty(PROJECTOR_PROPERTY.HEIGHT)*2.2f, 0.5f);
				matrix.scale(scale, scale, scale);
				
				RenderUtils.renderString(matrix, text, 0, 0, 0, 1.0f, new float[] {1.0f, 1.0f, 1.0f, te.properties.getProperty(PROJECTOR_PROPERTY.SOLID)}, false);
				
				RenderSystem.enableCull();
				matrix.popPose();
			}else
			if(te.mode == PROJECTOR_MODES.ITEM) {
				matrix.pushPose();
				
				RenderUtils.rotateMatrixForBlock(matrix, te.getBlockState().getValue(BlockHorizontal.FACING));
				RenderUtils.rotateMatrixForBlock(matrix, te.properties.getProperty(PROJECTOR_PROPERTY.ROTATION)*360 + (te.doesRotate ? te.rotation: 0));

				float scale = te.properties.getProperty(PROJECTOR_PROPERTY.SCALE);
				matrix.translate(0, + 1 + te.properties.getProperty(PROJECTOR_PROPERTY.HEIGHT)*2.2f, 0);

				RenderUtils.renderItem(matrix, te.handler.getStackInSlot(0), 0, 0, 0, scale, bufferIn, combinedLightIn, combinedOverlayIn);

//				Minecraft.getInstance().textureManager.bind(new ResourceLocation("minecraft:textures/block/diamond_block.png"));
//				RenderUtils.renderCircle(matrix, 5, 100, 0, 0, 10, 10);
				
				
//				RenderSystem.enableDepthTest();
//				
//				{
//					RenderUtils.setupStencil();
//					matrix.pushPose();
//					matrix.translate(0, 0, -0.005f);
//					matrix.scale(1, 1.6f, 1);
//					RenderUtils.renderCircle(matrix, 1.6f, 100, 1, 1, 1);
//					matrix.popPose();
//					RenderUtils.setupRenderInside();
//					
//					matrix.pushPose();
//					matrix.translate(0, 0, -0.005f);
//					matrix.scale(1, 1.6f, 1);
//					float[] color = new float[] {0, .4f, 1f, 1f};
//					color = new float[] {.8f, .5f, .2f};
//					RenderUtils.renderQuad(matrix, -5, -5, 0, 10, 10, color);
//					matrix.popPose();
//					RenderUtils.disableStencil();
//				}
//				
//				{
//					RenderSystem.disableCull();
//					
//					
//					
//					RenderUtils.setupStencil();
//						float sc = 2;
//						float depth = 1;
//						matrix.pushPose();
//						matrix.translate(0, 0, -0.01f);
//						matrix.scale(1, 1.6f, 1);
//						RenderUtils.renderCircle(matrix, 1.5f, 100, 0, 0, 0);
//						matrix.popPose();
//						RenderUtils.renderCube(matrix, -1f, -1, 0.001f, sc - 0.002f, sc - 0.002f, depth - 0.002f, 0, 0, 0);
//					RenderUtils.setupRenderInside();
//					{
//						Minecraft mc = Minecraft.getInstance();
//						
//						matrix.pushPose();
//
//						matrix.translate(0, -1.4f, 0);
//						matrix.mulPose(Vector3f.XP.rotationDegrees(90f));
//						
//						RenderSystem.setShaderColor(1, 1, 1, 1);
//						RenderSystem.enableTexture();
//						RenderUtils.bindTexture(new ResourceLocation("minecraft:textures/block/stone.png"));
//						float s = 100;
//						RenderUtils.renderQuad(matrix, -s/2f, 0, 0, s, s, 0, 0, s, s);
//						RenderSystem.disableTexture();
//						matrix.popPose();
//					}
//					RenderUtils.disableStencil();
//				}
					
//				RenderSystem.enableDepthTest();
//				Minecraft mc = Minecraft.getInstance();
//				int[] fbo = RenderUtils.setupRenderToTexture();
//				
//				Entity cam = mc.getCameraEntity();
//
//				matrix.pushPose();
//				try {
//					matrix.mulPose(Vector3f.YP.rotationDegrees(180f));
//					matrix.translate(0, 2, 0);
//					mc.gameRenderer.renderLevel(partialTicks, te.getLevel().getGameTime(), matrix);
//				}catch(Exception ex) {
//					
//				}
//				matrix.popPose();
//				
//				RenderUtils.stopRenderToTexture();
//				mc.setCameraEntity(cam);
//				
//				MainWindow win = mc.getWindow();
//				float f = win.getWidth() / win.getHeight();
//				float h = 2;
//				float w = f * h;
//				RenderSystem.enableDepthTest();
//				
//				RenderSystem.enableTexture();
//				RenderUtils.bindTexture(fbo[1]);
//				RenderUtils.renderQuad(matrix, 0, 0, 0, w, h, 0, 0, 1, 1);
//
//				mc.textureManager.bind(new ResourceLocation("minecraft:textures/block/stone.png"));
//				RenderUtils.destoryFBO(fbo);
				
				matrix.popPose();
			}else if(te.mode == PROJECTOR_MODES.PLAYER) {
				matrix.pushPose();
				
				RenderUtils.rotateMatrixForBlock(matrix, te.getBlockState().getValue(BlockHorizontal.FACING));
				RenderUtils.rotateMatrixForBlock(matrix, te.properties.getProperty(PROJECTOR_PROPERTY.ROTATION)*360 + (te.doesRotate ? te.rotation: 0));
				if(te.liveModel)
					RenderUtils.rotateMatrixForBlock(matrix, 90);
				
				float scale = te.properties.getProperty(PROJECTOR_PROPERTY.SCALE);
				matrix.translate(0.5, + 1 + te.properties.getProperty(PROJECTOR_PROPERTY.HEIGHT)*2.2f, 0.5);
				scale *= 0.7f;

//				try {
				if(te.liveModel) {
					Player p;
					if(te.text.equals("{Player}"))
						p = Minecraft.getInstance().player;
					else 
						p = Utils.getPlayer(te.getLevel(), te.text);
					
					if(p == null) {
						matrix.popPose();
						matrix.pushPose();
						String text = "Unable To Find Player!";
						RenderSystem.disableCull();
						RenderUtils.rotateMatrixForBlock(matrix, te.getBlockState().getValue(BlockHorizontal.FACING));
						matrix.mulPose(Vector3f.ZP.rotationDegrees(180f));
						matrix.translate(-0.5f - text.length()/2f * 0.015f*5f, -1.1 - (0.015f*5f/2f) - .3f*2.2f, 0.5f);
						matrix.scale(0.015f, 0.015f, 0.005f);
						RenderUtils.renderString(matrix, text, 0, 0, 0, 1.0f, new float[] {.6f, 0.0f, 0.0f, 1.0f}, false);
						RenderSystem.enableCull();
					}else {
						scale *= 1.05f;
						matrix.scale(scale, scale, scale);
						RenderUtils.renderPlayer(matrix, p, 0, 0, 0, 1f, new float[] {1.0f, 1.0f, 1.0f, te.properties.getProperty(PROJECTOR_PROPERTY.SOLID)}, bufferIn, combinedLightIn, combinedOverlayIn, te.isChild);
					}
				}else
					if(te.text.equals("{Player}"))
						RenderUtils.renderPlayerGameProfile(matrix, Minecraft.getInstance().player.getGameProfile(), 0, scale*1.5f, 0, scale, new float[] {1.0f, 1.0f, 1.0f, te.properties.getProperty(PROJECTOR_PROPERTY.SOLID)},  bufferIn, combinedLightIn, combinedOverlayIn, te.isChild);
					else if(te.profile != null)
							RenderUtils.renderPlayerGameProfile(matrix, te.profile.getStored(), 0, scale*1.5f, 0, scale, new float[] {1.0f, 1.0f, 1.0f, te.properties.getProperty(PROJECTOR_PROPERTY.SOLID)},  bufferIn, combinedLightIn, combinedOverlayIn, te.isChild);
//				}catch(Exception e) {}
				
				matrix.popPose();
			}

			matrix.popPose();
		}
	}

public static class ProjectorRenderType extends RenderType {

	public ProjectorRenderType(String p_173178_, VertexFormat p_173179_, Mode p_173180_, int p_173181_,
			boolean p_173182_, boolean p_173183_, Runnable p_173184_, Runnable p_173185_) {
		super(p_173178_, p_173179_, p_173180_, p_173181_, p_173182_, p_173183_, p_173184_, p_173185_);
	}

	public static final RenderType PROJECTION_RENDER = create("projection_render",
			DefaultVertexFormat.POSITION_COLOR_TEX, Mode.QUADS, 256, false, true,
			 RenderType.CompositeState.builder()
			 .setWriteMaskState(COLOR_WRITE)
			 .setCullState(NO_CULL)
			 .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
			 .setTextureState(NO_TEXTURE)
			 .setShaderState(RENDERTYPE_ENTITY_SMOOTH_CUTOUT_SHADER)
			 .createCompositeState(false));
	}
	
}
