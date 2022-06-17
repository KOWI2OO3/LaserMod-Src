package KOWI2003.LaserMod.tileentities.render;

import java.util.OptionalDouble;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import KOWI2003.LaserMod.Reference;
import KOWI2003.LaserMod.init.ModItems;
import KOWI2003.LaserMod.tileentities.TileEntityLaser;
import KOWI2003.LaserMod.tileentities.TileEntityLaser.MODE;
import KOWI2003.LaserMod.tileentities.TileEntityMirror;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;

public class MirrorRender extends TileEntityRenderer<TileEntityMirror> {

	private float uMin, vMin = 0;
	
	public MirrorRender(TileEntityRendererDispatcher dispatcher) {
		super(dispatcher);
	}
	
	@Override
	public boolean shouldRenderOffScreen(TileEntityMirror te) {
		return te.isActive() && (te.getDirection() == Direction.NORTH || te.getDirection() == Direction.WEST || te.distance > 10);
	}

	@Override
	public void render(TileEntityMirror mirror, float partialTicks, MatrixStack matrix,
			IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
		RenderSystem.enableDepthTest();
		RenderSystem.color3f(1.0f, 1.0f, 1.0f);
		if(!mirror.hasLaser())
			return;
		TileEntityLaser laser = mirror.getLaser();
		float start = 0.5f;
		if(mirror.isActive()) {
			RenderSystem.enableDepthTest();
			RenderSystem.color3f(1.0f, 1.0f, 1.0f);
				double distance = mirror.distance;
				float r = laser.red;
				float g = laser.green;
				float b = laser.blue;
				float a = 0.4f;
				
				float thickness = 0.05f;
				
				float ll = 0.5f - thickness/2f;
		        float lr = 0.5f + thickness/2f;
		        
		        Direction facing = mirror.getDirection();
		        
		        if(laser.mode == MODE.NORMAL || (laser.mode == MODE.INVISIBLE && canBeSeen())) {
			        matrix.pushPose();
			        
					IVertexBuilder buffer = bufferIn.getBuffer(LaserRenderType.LASER_RENDER);
					Matrix4f matrix2 = matrix.last().pose();
						
						matrix.translate(.5f, .5f, .5f);
					
						matrix.mulPose(Vector3f.XP.rotationDegrees(facing.step().y() * 90f - 90f));
						matrix.mulPose(Vector3f.ZP.rotationDegrees((Math.abs(facing.step().y())-1) * (facing.toYRot() + 180f)));
						
						matrix.translate(-.5, -.5, 0);
						
						buffer.vertex(matrix2, ll, start, 0F).color(r,g, b, a).uv(0, 0).endVertex();
		    			buffer.vertex(matrix2, ll, (float) distance, 0F).color(r, g, b, a).uv(0, 0).endVertex();
		    			buffer.vertex(matrix2, lr, (float) distance, 0F).color(r, g, b, a).uv(0, 0).endVertex();
		    			buffer.vertex(matrix2, lr, start, 0F).color(r, g, b, a).uv(0, 0).endVertex();
		    			
				        buffer = bufferIn.getBuffer(LaserRenderType.LASER_RENDER);
		    			
				        matrix.translate(.5, 0, -0.5f);
				        
		    			buffer.vertex(matrix2, 0F, start, ll).color(r, g, b, a).uv(0, 0).endVertex();
		    			buffer.vertex(matrix2, 0f, (float) distance, ll).color(r, g, b, a).uv(0, 0).endVertex();
		    			buffer.vertex(matrix2, 0f, (float) distance, lr).color(r, g, b, a).uv(0, 0).endVertex();
		    			buffer.vertex(matrix2, 0f, start, lr).color(r, g, b, a).uv(0, 0).endVertex();
		    			
			        matrix.popPose();
		        }else if(laser.mode == MODE.POWER) {
		        	matrix.pushPose();
		        	 
						IVertexBuilder buffer = bufferIn.getBuffer(LaserRenderType.LASER_RENDER_POWER);
						Matrix4f matrix2 = matrix.last().pose();
		
							matrix.translate(.5f, .5f, .5f);
						
							matrix.mulPose(Vector3f.XP.rotationDegrees(facing.step().y() * 90f - 90f));
							matrix.mulPose(Vector3f.ZP.rotationDegrees((Math.abs(facing.step().y())-1) * (facing.toYRot() + 180f)));

							matrix.translate(-.5, -1, -.5);

							distance += .5d;
							
							buffer.vertex(matrix2, 0, start + 0.1f, 0.5F).color(r,g, b, a).uv(0, 0).endVertex();
							buffer.vertex(matrix2, 0, (float)distance, 0.5F).color(r,g, b, a).uv(1, 0).endVertex();
							buffer.vertex(matrix2, 1F, (float)distance, 0.5F).color(r,g, b, a).uv(1, 1).endVertex();
							buffer.vertex(matrix2, 1F, start + 0.1f, 0.5F).color(r,g, b, a).uv(0, 1).endVertex();
			    			
					        buffer = bufferIn.getBuffer(LaserRenderType.LASER_RENDER_POWER);
			    			
					        matrix.translate(0, 0, 0f);

							buffer.vertex(matrix2, 0.5F, start + 0.1F, 0).color(r,g, b, a).uv(0, 0).endVertex();
							buffer.vertex(matrix2, 0.5F, (float)distance, 0).color(r,g, b, a).uv(1, 0).endVertex();
							buffer.vertex(matrix2, 0.5F, (float)distance, 0 + 1F).color(r,g, b, a).uv(1, 1).endVertex();
							buffer.vertex(matrix2, 0.5F, start + 0.1F, 0 + 1F).color(r,g, b, a).uv(0, 1).endVertex();
			    			
							buffer = bufferIn.getBuffer(LaserRenderType.LASER_RENDER_BEAM);

							buffer.vertex(matrix2, 0, start + 0.1F, 0.5F).uv(0, 0).endVertex();
							buffer.vertex(matrix2, 0, (float) distance, 0.5F).uv(1, 0).endVertex();
							buffer.vertex(matrix2, 1F, (float) distance, 0.5F).uv(1, 1).endVertex();
							buffer.vertex(matrix2, 1F, start + 0.1F, 0.5F).uv(0, 1).endVertex();
							

							buffer.vertex(matrix2, 0.5F, start + 0.1F, 0).uv(0, 0).color(r, g, b, 0.5F).endVertex();
							buffer.vertex(matrix2, 0.5F, (float) distance, 0).uv(1, 0).color(r, g, b, 0.5F).endVertex();
							buffer.vertex(matrix2, 0.5F, (float) distance, 1F).uv(1, 1).color(r, g, b, 0.5F).endVertex();
							buffer.vertex(matrix2, 0.5F, start + 0.1F, 1F).uv(0, 1).color(r, g, b, 0.5F).endVertex();
							
				        matrix.popPose();
		        	
		        }
		        else if(laser.mode == MODE.BEAM) {
		        	 matrix.pushPose();
				        
		        	 ll = 0.3f;
		        	 lr = 0.7f;
		        	 
						IVertexBuilder buffer = bufferIn.getBuffer(LaserRenderType.LASER_RENDER_POWER_NEW);
						Matrix4f matrix2 = matrix.last().pose();
						
						matrix.translate(.5f, .5f, .5f);
						
						matrix.mulPose(Vector3f.XP.rotationDegrees(facing.step().y() * 90f - 90f));
						matrix.mulPose(Vector3f.ZP.rotationDegrees((Math.abs(facing.step().y())-1) * (facing.toYRot() + 180f)));
						
						rotationLogic(matrix, mirror);
						
						matrix.translate(-.5, -.5, 0);
						
						uMin -= 0.01f;
						if((uMin + 0.01f) == 1) {
							uMin = 0;
						}
						
						a += 0.25;
						
						buffer.vertex(matrix2, ll, start, 0f).color(r, g, b, a).uv(uMin, 0).endVertex();
						buffer.vertex(matrix2, ll, (float) distance, 0f).color(r, g, b, a).uv(uMin + 10f, 0).endVertex();
						buffer.vertex(matrix2, lr, (float) distance, 0f).color(r, g, b, a).uv(uMin + 10f, 1).endVertex();
						buffer.vertex(matrix2, lr, start, 0f).color(r, g, b, a).uv(uMin, 1).endVertex();
						
					matrix.popPose();
		        }else if(laser.mode == MODE.NEW_POWER) { 
		        	 matrix.pushPose();
		        	 
						IVertexBuilder buffer = bufferIn.getBuffer(LaserRenderType.LASER_RENDER_BEAM);
						Matrix4f matrix2 = matrix.last().pose();
							
						thickness *= 2f;
						
						ll = 0.5f - thickness/2f;
				        lr = 0.5f + thickness/2f;
						
						float centerThickness = (thickness/2f)/5f;
					
						matrix.translate(.5f, .5f, .5f);
					
						matrix.mulPose(Vector3f.XP.rotationDegrees(facing.step().y() * 90f - 90f));
						matrix.mulPose(Vector3f.ZP.rotationDegrees((Math.abs(facing.step().y())-1) * (facing.toYRot() + 180f)));
						
						rotationLogic(matrix, mirror);
						
						matrix.translate(-.5, -.5, 0);
						
		    			buffer.vertex(matrix2, (lr+ll)/2f-centerThickness, start, 0F).color(1f, 1f, 1f, 1f).uv(0, 0.48f).endVertex();
		    			buffer.vertex(matrix2, (lr+ll)/2f-centerThickness, (float) distance, 0F).color(1f, 1f, 1f, 1f).uv(1, 0.48f).endVertex();
		    			buffer.vertex(matrix2, (lr+ll)/2f+centerThickness, (float) distance, 0F).color(1f, 1f, 1f, 1f).uv(1, 0.52f).endVertex();
		    			buffer.vertex(matrix2, (lr+ll)/2f+centerThickness, start, 0F).color(1f, 1f, 1f, 1f).uv(0, 0.52f).endVertex();
		    			
		    			buffer = bufferIn.getBuffer(LaserRenderType.LASER_RENDER);

						buffer.vertex(matrix2, ll, start, 0F).color(r,g, b, 0f).uv(0, 0).endVertex();
		    			buffer.vertex(matrix2, ll, (float) distance, 0F).color(r, g, b, 0f).uv(0, 0).endVertex();
		    			buffer.vertex(matrix2, (lr+ll)/2f-centerThickness, (float) distance, 0F).color(r, g, b, 1f).uv(0, 0).endVertex();
		    			buffer.vertex(matrix2, (lr+ll)/2f-centerThickness, start, 0F).color(r, g, b, 1f).uv(0, 0).endVertex();
		    			
		    			buffer.vertex(matrix2, (lr+ll)/2f+centerThickness, start, 0F).color(r, g, b, 1f).uv(0, 0).endVertex();
		    			buffer.vertex(matrix2, (lr+ll)/2f+centerThickness, (float) distance, 0F).color(r, g, b, 1f).uv(0, 0).endVertex();
		    			buffer.vertex(matrix2, lr, (float) distance, 0F).color(r, g, b, 0f).uv(0, 0).endVertex();
		    			buffer.vertex(matrix2, lr, start, 0F).color(r, g, b, 0f).uv(0, 0).endVertex();

			        matrix.popPose();
		        }
		        RenderSystem.enableDepthTest();
		}
	}
	
	public boolean canBeSeen() {
		boolean condition = false;
		for(ItemStack stack : Minecraft.getInstance().player.getArmorSlots()) {
			if(stack.getItem() == ModItems.IR_Glasses) {
				condition = true;
				break;
			}
		}
		return condition && Minecraft.getInstance().options.getCameraType().isFirstPerson();
	}
	
	public void rotationLogic(MatrixStack matrix, TileEntityMirror te) {
		Direction facing = te.getDirection();
		Vector3d playerPos = Minecraft.getInstance().player.position()
				.add(new Vector3d(0, Minecraft.getInstance().player.getEyeHeight(), 0));
		
		Vector3f pos = new Vector3f(te.getBlockPos().getX() + 0.5f, te.getBlockPos().getY() + 0.5f, te.getBlockPos().getZ() + 0.5f);
		Vector3d toPlayer = playerPos.subtract(pos.x(), pos.y(), pos.z());
		Vector3f right = new Vector3f();
		Vector3f normal = new Vector3f();
		
		if(facing.getAxis().isHorizontal()) {
			right = new Vector3f(-facing.getStepZ(), facing.getStepY(), facing.getStepX());
			normal = right.copy();
			normal.cross(facing.step());
			normal = new Vector3f(Math.abs(normal.x()), Math.abs(normal.y()), Math.abs(normal.z()));
			toPlayer = toPlayer.multiply(new Vector3d(Math.abs(right.x() + normal.x()), Math.abs(right.y() + normal.y()), Math.abs(right.z() + normal.z())));
		}else {
			right = new Vector3f(-facing.getStepY(), facing.getStepX(), facing.getStepZ());
			normal = right.copy();
			normal.cross(facing.step());
			toPlayer = toPlayer.multiply(new Vector3d(Math.abs(right.x() + normal.x()), Math.abs(right.y() + normal.y()), Math.abs(right.z() + normal.z())));
		}
		
		double rad = Math.acos(((toPlayer.x()*normal.x() + toPlayer.y()*normal.y() + toPlayer.z()*normal.z())/
				(Math.sqrt(toPlayer.x * toPlayer.x + toPlayer.y * toPlayer.y + toPlayer.z * toPlayer.z) * Math.sqrt(normal.x() * normal.x() + normal.y() * normal.y() + normal.z() * normal.z()))));
		
		float angle = (float) Math.toDegrees(rad);
		
		if(facing.getAxis().isHorizontal()) {
			if(facing == Direction.NORTH || facing == Direction.EAST) {
				if(toPlayer.z + toPlayer.x < 0)
					angle = -angle;
			}else
			if(toPlayer.z + toPlayer.x > 0)
				angle = -angle;
		}else {
			if(facing == Direction.UP) {
				if(toPlayer.x > 0)
				angle = -angle;
			}else {
				if(toPlayer.x < 0)
					angle = -angle;
			}
		}
		
		matrix.mulPose(Vector3f.YP.rotationDegrees(angle));
	}
	
public static class LaserRenderType extends RenderType {

	public LaserRenderType(String p_i225992_1_, VertexFormat p_i225992_2_, int p_i225992_3_, int p_i225992_4_,
			boolean p_i225992_5_, boolean p_i225992_6_, Runnable p_i225992_7_, Runnable p_i225992_8_) {
		super(p_i225992_1_, p_i225992_2_, p_i225992_3_, p_i225992_4_, p_i225992_5_, p_i225992_6_, p_i225992_7_, p_i225992_8_);
	}
	
	public static final RenderType LASER_RENDER = create("laser_render",
			DefaultVertexFormats.POSITION_COLOR_TEX, 7, 256,
			 false, true,
			 RenderType.State.builder()
			 .setWriteMaskState(COLOR_WRITE)
			 .setCullState(CullState.NO_CULL)
			 .setTransparencyState(TransparencyState.TRANSLUCENT_TRANSPARENCY)
			 .setTextureState(NO_TEXTURE)
			 .setShadeModelState(ShadeModelState.SMOOTH_SHADE)
			 .setLayeringState(LayerState.POLYGON_OFFSET_LAYERING)
			 .setOverlayState(OverlayState.OVERLAY)
			 .createCompositeState(false));
	
	private static final TextureState TexStatePOWER = new TextureState(new ResourceLocation(Reference.MODID, "textures/render/laser_power_old_double.png"), false, false);
	private static final TextureState TexStatePOWER_NEW = new TextureState(new ResourceLocation(Reference.MODID, "textures/render/laser_power.png"), false, false);
	private static final TextureState TexStateBEAM = new TextureState(new ResourceLocation(Reference.MODID, "textures/render/laser_power_old_beam.png"), false, true);
	
	public static final RenderType LASER_RENDER_POWER_NEW = create("laser_power_new",
			DefaultVertexFormats.POSITION_COLOR_TEX, 7, 256,
			 false, true,
			 RenderType.State.builder()
			 .setWriteMaskState(COLOR_WRITE)
			 .setTextureState(TexStatePOWER_NEW)
			 .setCullState(NO_CULL)
			 .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
			 .setShadeModelState(SMOOTH_SHADE)
			 .createCompositeState(false));
	
	public static final RenderType LASER_RENDER_POWER = create("laser_power",
			DefaultVertexFormats.POSITION_COLOR_TEX, 7, 256,
			 false, true,
			 RenderType.State.builder()
			 .setWriteMaskState(COLOR_WRITE)
			 .setTextureState(TexStatePOWER)
			 .setCullState(NO_CULL)
			 .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
			 .setShadeModelState(SMOOTH_SHADE)
			 .createCompositeState(false));
	
	public static final RenderType LASER_RENDER_BEAM = create("laser_beam",
			DefaultVertexFormats.POSITION_TEX, 7, 256,
			 false, true,
			 RenderType.State.builder()
			 .setTextureState(TexStateBEAM)
			 .setCullState(NO_CULL)
			 .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
			 .setShadeModelState(SMOOTH_SHADE)
			 .createCompositeState(false));
	
	private static final TextureState TexStateTriangle = new TextureState(new ResourceLocation("minecraft", "textures/block/gray_concrete.png"), false, true);
	
	public static final RenderType TOP_RENDER = create("side_triangle",
			DefaultVertexFormats.POSITION_TEX, GL11.GL_TRIANGLES, 256,
			 false, true,
			 RenderType.State.builder()
			 .setTextureState(TexStateTriangle)
			 .setCullState(NO_CULL)
			 .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
			 .setShadeModelState(SMOOTH_SHADE)
			 .createCompositeState(false));
	   
}

public static class LineRenderType extends RenderType {

	public LineRenderType(String p_i225992_1_, VertexFormat p_i225992_2_, int p_i225992_3_, int p_i225992_4_,
			boolean p_i225992_5_, boolean p_i225992_6_, Runnable p_i225992_7_, Runnable p_i225992_8_) {
		super(p_i225992_1_, p_i225992_2_, p_i225992_3_, p_i225992_4_, p_i225992_5_, p_i225992_6_, p_i225992_7_, p_i225992_8_);
	}
	
	private static final LineState THICK_LINE = new LineState(OptionalDouble.of(3.0D));
	
	public static final RenderType OVERLAY_LINES = create("overlay_lines",
			DefaultVertexFormats.POSITION_COLOR, GL11.GL_LINES, 256,
			RenderType.State.builder().setLineState(THICK_LINE)
			.setTransparencyState(TRANSLUCENT_TRANSPARENCY)
			.setTextureState(NO_TEXTURE)
			.setCullState(NO_CULL)
			.setLightmapState(LightmapState.NO_LIGHTMAP)
			.setWriteMaskState(COLOR_WRITE)
			.createCompositeState(false));
	
}
}
