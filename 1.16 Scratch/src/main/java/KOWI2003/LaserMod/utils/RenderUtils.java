package KOWI2003.LaserMod.utils;

import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_RGB;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.glFramebufferTexture2D;

import java.nio.ByteBuffer;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.NativeType;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import KOWI2003.LaserMod.blocks.BlockRotatable;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;

public class RenderUtils {

	public static class Gui {
		public static void drawQuad(MatrixStack matrix, float x, float y, float width, float height, float uvX, float uvY, float uvWidth, float uvHeight) {
			BufferBuilder bufferbuilder = Tessellator.getInstance().getBuilder();
			Matrix4f matrix2 = matrix.last().pose();
			bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
			bufferbuilder.vertex(matrix2, x		   , y + height, 0f).uv(uvX			 , uvY - uvHeight).endVertex();
			bufferbuilder.vertex(matrix2, x + width, y + height, 0f).uv(uvX + uvWidth, uvY - uvHeight).endVertex();
			bufferbuilder.vertex(matrix2, x + width, y		   , 0f).uv(uvX + uvWidth, uvY 			 ).endVertex();
			bufferbuilder.vertex(matrix2, x		   , y		   , 0f).uv(uvX			 , uvY 			 ).endVertex();
			bufferbuilder.end();
			WorldVertexBufferUploader.end(bufferbuilder);
		}
		
		public static void drawQuad(MatrixStack matrix, float x, float y, float width, float height, float uvX, float uvY) {
			drawQuad(matrix, x, y, width, height, uvX, uvY, width, height);
		}
		
		public static void drawQuad(MatrixStack matrix, float x, float y, float width, float height, float red, float green, float blue) {
			RenderSystem.disableTexture();
			RenderSystem.color3f(red, green, blue);
			drawQuad(matrix, x, y, width, height, 0, 0);
			RenderSystem.enableTexture();
		}
		
		public static void drawFontString(MatrixStack matrix, String msg, float x, float y, float red, float green, float blue) {
			Minecraft.getInstance().font.draw(matrix, msg, x, y, Utils.getHexIntFromRGB(red, green, blue));
		}
		
		public static void drawFontString(MatrixStack matrix, String msg, float x, float y, float red, float green, float blue, float scale) {
			matrix.pushPose();
			matrix.translate(x, y, 0);
			matrix.scale(scale, scale, scale);
			Minecraft.getInstance().font.draw(matrix, msg, 0, 0, Utils.getHexIntFromRGB(red, green, blue));
			matrix.popPose();
		}
		
		public static void drawCircle(MatrixStack matrix, double radius, int segments) {
			radius *= 0.5f;
			BufferBuilder bufferbuilder = Tessellator.getInstance().getBuilder();
			Matrix4f matrix2 = matrix.last().pose();
			bufferbuilder.begin(GL11.GL_POLYGON, DefaultVertexFormats.POSITION_TEX);
	        for (int i = 0; i < segments; i++) {
	        	bufferbuilder.vertex(matrix2, (float)(Math.sin((i / (double) segments) * Math.PI * 2) * radius), (float)(Math.cos((i / (double) segments) * Math.PI * 2) * radius), 0f).endVertex();
	        }
			bufferbuilder.end();
			WorldVertexBufferUploader.end(bufferbuilder);
	    }
	}
	
	public static void renderCircle(MatrixStack matrix, double radius, int segments, float u, float v, float uWidth, float vHeight) {
		radius *= 0.5f;
		BufferBuilder bufferbuilder = Tessellator.getInstance().getBuilder();
		Matrix4f matrix2 = matrix.last().pose();
		bufferbuilder.begin(GL11.GL_POLYGON, DefaultVertexFormats.POSITION_TEX);
        for (int i = 0; i < segments; i++) {
        	bufferbuilder.vertex(matrix2, (float)(Math.sin((i / (double) segments) * Math.PI * 2) * radius), (float)(Math.cos((i / (double) segments) * Math.PI * 2) * radius), 0f)
        	.uv((float)(u + 0.5f + (Math.sin((i / (double) segments) * Math.PI * 2)/2f+1f) * uWidth), (float)(v + 0.5f + (Math.cos((i / (double) segments) * Math.PI * 2)/2f + 1f) * vHeight)).endVertex();
        }
		bufferbuilder.end();
		WorldVertexBufferUploader.end(bufferbuilder);
    }
	
	public static void renderCircle(MatrixStack matrix, double radius, int segments, float[] color) {
		RenderSystem.disableTexture();
		radius *= 0.5f;
		BufferBuilder bufferbuilder = Tessellator.getInstance().getBuilder();
		Matrix4f matrix2 = matrix.last().pose();
		bufferbuilder.begin(GL11.GL_POLYGON, DefaultVertexFormats.POSITION_COLOR);
        for (int i = 0; i < segments; i++) {
        	bufferbuilder.vertex(matrix2, (float)(Math.sin((i / (double) segments) * Math.PI * 2) * radius), (float)(Math.cos((i / (double) segments) * Math.PI * 2) * radius), 0f)
        	.color(color.length > 0 ? color[0] : 1f,
        		   color.length > 1 ? color[1] : 1f, 
        		   color.length > 2 ? color[2] : 1f, 
        		   color.length > 3 ? color[3] : 1f).endVertex();
        }
		bufferbuilder.end();
		WorldVertexBufferUploader.end(bufferbuilder);
		RenderSystem.enableTexture();
    }
	
	public static void renderCircle(MatrixStack matrix, double radius, int segments, float red, float green, float blue) {
		RenderSystem.disableTexture();
		renderCircle(matrix, radius, segments, new float[] {red, green, blue});
		RenderSystem.enableTexture();
    }
	
	public static void renderCircle(MatrixStack matrix, double radius, int segments) {
		radius *= 0.5f;
		BufferBuilder bufferbuilder = Tessellator.getInstance().getBuilder();
		Matrix4f matrix2 = matrix.last().pose();
		bufferbuilder.begin(GL11.GL_POLYGON, DefaultVertexFormats.POSITION);
        for (int i = 0; i < segments; i++) {
        	bufferbuilder.vertex(matrix2, (float)(Math.sin((i / (double) segments) * Math.PI * 2) * radius), (float)(Math.cos((i / (double) segments) * Math.PI * 2) * radius), 0f).endVertex();
        }
		bufferbuilder.end();
		WorldVertexBufferUploader.end(bufferbuilder);
    }
	
	public static void renderCube(MatrixStack matrix, float x, float y, float z, float width, float height, float depth, float uvX, float uvY, float uvWidth, float uvHeight) {
		BufferBuilder bufferbuilder = Tessellator.getInstance().getBuilder();
		Matrix4f matrix2 = matrix.last().pose();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.vertex(matrix2, x		   , y + height, z).uv(uvX			, uvY + uvHeight).endVertex();
		bufferbuilder.vertex(matrix2, x + width, y + height, z).uv(uvX + uvWidth, uvY + uvHeight).endVertex();
		bufferbuilder.vertex(matrix2, x + width, y		   , z).uv(uvX + uvWidth, uvY			).endVertex();
		bufferbuilder.vertex(matrix2, x		   , y		   , z).uv(uvX			, uvY			).endVertex();
		

		bufferbuilder.vertex(matrix2, x + width, y + height, z + depth).uv(uvX + uvWidth, uvY + uvHeight).endVertex();
		bufferbuilder.vertex(matrix2, x		   , y + height, z + depth).uv(uvX			, uvY + uvHeight).endVertex();
		bufferbuilder.vertex(matrix2, x		   , y		   , z + depth).uv(uvX			, uvY			).endVertex();
		bufferbuilder.vertex(matrix2, x + width, y		   , z + depth).uv(uvX + uvWidth, uvY			).endVertex();

		bufferbuilder.vertex(matrix2, x + width, y 	   	   , z + depth).uv(uvX + uvWidth, uvY + uvHeight).endVertex();
		bufferbuilder.vertex(matrix2, x		   , y 		   , z + depth).uv(uvX			, uvY + uvHeight).endVertex();
		bufferbuilder.vertex(matrix2, x		   , y		   , z).uv(uvX			, uvY			).endVertex();
		bufferbuilder.vertex(matrix2, x + width, y		   , z).uv(uvX + uvWidth, uvY			).endVertex();
		

		bufferbuilder.vertex(matrix2, x		   , y + height, z + depth).uv(uvX			, uvY + uvHeight).endVertex();
		bufferbuilder.vertex(matrix2, x + width, y + height, z + depth).uv(uvX + uvWidth, uvY + uvHeight).endVertex();
		bufferbuilder.vertex(matrix2, x + width, y + height, z).uv(uvX + uvWidth, uvY			).endVertex();
		bufferbuilder.vertex(matrix2, x		   , y + height, z).uv(uvX			, uvY			).endVertex();
		

		bufferbuilder.vertex(matrix2, x		   , y + height, z + depth).uv(uvX + uvWidth, uvY + uvHeight).endVertex();
		bufferbuilder.vertex(matrix2, x		   , y + height, z).uv(uvX			, uvY + uvHeight).endVertex();
		bufferbuilder.vertex(matrix2, x		   , y		   , z).uv(uvX			, uvY			).endVertex();
		bufferbuilder.vertex(matrix2, x 	   , y		   , z + depth).uv(uvX + uvWidth, uvY			).endVertex();
		

		bufferbuilder.vertex(matrix2, x + width, y + height, z).uv(uvX			, uvY + uvHeight).endVertex();
		bufferbuilder.vertex(matrix2, x + width, y + height, z + depth).uv(uvX + uvWidth, uvY + uvHeight).endVertex();
		bufferbuilder.vertex(matrix2, x + width, y		   , z + depth).uv(uvX + uvWidth, uvY			).endVertex();
		bufferbuilder.vertex(matrix2, x + width, y		   , z).uv(uvX			, uvY			).endVertex();
		bufferbuilder.end();
		WorldVertexBufferUploader.end(bufferbuilder);
	}
	
	public static void renderQuad(MatrixStack matrix, float x, float y, float z, float width, float height, float uvX, float uvY, float uvWidth, float uvHeight) {
		BufferBuilder bufferbuilder = Tessellator.getInstance().getBuilder();
		Matrix4f matrix2 = matrix.last().pose();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.vertex(matrix2, x		   , y + height, z).uv(uvX			, uvY + uvHeight).endVertex();
		bufferbuilder.vertex(matrix2, x + width, y + height, z).uv(uvX + uvWidth, uvY + uvHeight).endVertex();
		bufferbuilder.vertex(matrix2, x + width, y		   , z).uv(uvX + uvWidth, uvY			).endVertex();
		bufferbuilder.vertex(matrix2, x		   , y		   , z).uv(uvX			, uvY			).endVertex();
		bufferbuilder.end();
		WorldVertexBufferUploader.end(bufferbuilder);
	}
	
	public static void renderQuad(MatrixStack matrix, float x, float y, float z, float width, float height, float uvX, float uvY) {
		renderQuad(matrix, x, y, z, width, height, uvX, uvY, width, height);
	}
	
	public static void renderQuad(MatrixStack matrix, float x, float y, float z, float width, float height, float[] color) {
		RenderSystem.disableTexture();
		RenderSystem.color4f(
			   color.length > 0 ? color[0] : 1f,
     		   color.length > 1 ? color[1] : 1f, 
     		   color.length > 2 ? color[2] : 1f, 
     		   color.length > 3 ? color[3] : 1f);
		renderQuad(matrix, x, y, z, width, height, 0, 0);
		RenderSystem.color3f(1.0f, 1.0f, 1.0f);
		RenderSystem.enableTexture();
	}

	public static void renderCube(MatrixStack matrix, float x, float y, float z, float width, float height, float depth, float uvX, float uvY) {
		renderCube(matrix, x, y, z, width, height, depth, uvX, uvY, width, height);
	}
	
	public static void renderQuad(MatrixStack matrix, float x, float y, float z, float width, float height, float red, float green, float blue) {
		RenderSystem.disableTexture();
		RenderSystem.color3f(red, green, blue);
		renderQuad(matrix, x, y, z, width, height, 0, 0);
		RenderSystem.color3f(1.0f, 1.0f, 1.0f);
		RenderSystem.enableTexture();
	}
	
	public static void renderCube(MatrixStack matrix, float x, float y, float z, float width, float height, float depth, float red, float green, float blue) {
		RenderSystem.disableTexture();
		RenderSystem.color3f(red, green, blue);
		renderCube(matrix, x, y, z, width, height, depth, 0, 0);
		RenderSystem.color3f(1.0f, 1.0f, 1.0f);
		RenderSystem.enableTexture();
	}
	
	
	public static void renderItem(MatrixStack matrix, ItemStack stack, float x, float y, float z, float height, float scale,
			IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
		matrix.pushPose();
		RenderSystem.enableDepthTest();
		{
			if(stack.getItem() instanceof BlockItem) {
				matrix.translate(x, y, z);
	          	matrix.translate(0.5, height, 0.5);
	          	matrix.translate(-0.5, -0.5, -0.5);
	          	matrix.translate(0.5, 0.685, 0.5);
			}else {
				matrix.translate(x, y, z);

				matrix.translate(0.5, height + 0.11, 0.5);
				matrix.translate(-0.5, -0.5, -0.5);
				matrix.mulPose(Vector3f.XP.rotationDegrees(90));
				matrix.translate(0.5, 0.5, -0.5 - 0.03125);
			}  	
			
			matrix.scale(scale, scale, scale);
			RenderSystem.pushLightingAttributes();
			RenderHelper.setupFor3DItems();
			RenderSystem.color3f(1.0f, 1f, 1f);
			Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemCameraTransforms.TransformType.FIXED, combinedLightIn, combinedOverlayIn, matrix, bufferIn);
			RenderHelper.turnOff();
			RenderSystem.popAttributes();
		}
		matrix.popPose();
		RenderSystem.disableDepthTest();
	}
	
	public static void renderItem(MatrixStack matrix, ItemStack stack, float x, float y, float z, float scale,
			IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
		renderItem(matrix, stack, x, y, z, 0, scale, bufferIn, combinedLightIn, combinedOverlayIn);
	}
	
	public static void rotateMatrix(MatrixStack matrix, Direction facing) {
		matrix.mulPose(Vector3f.YP.rotationDegrees((Math.abs(facing.step().y())-1) * (facing.toYRot() + 180f)));
	}
	
	public static void rotateMatrixForBlock(MatrixStack matrix, Direction facing) {
		matrix.translate(0.5f, 0.5f, 0.5f);
		matrix.mulPose(Vector3f.YP.rotationDegrees((Math.abs(facing.step().y())-1) * (facing.toYRot() + 180f)));
		matrix.translate(-0.5f, -0.5f, -0.5f);
	}
	
	public static void rotateMatrix(MatrixStack matrix, float angle) {
		matrix.mulPose(Vector3f.YP.rotationDegrees(angle));
	}
	
	public static void rotateMatrixForBlock(MatrixStack matrix, float angle) {
		matrix.translate(0.5f, 0.5f, 0.5f);
		matrix.mulPose(Vector3f.YP.rotationDegrees(angle));
		matrix.translate(-0.5f, -0.5f, -0.5f);
	}
	
	public static void rotationLogic(MatrixStack matrix, TileEntity te) {
		Direction facing = Direction.NORTH;
		if(te.getBlockState().hasProperty(BlockRotatable.FACING))
			facing = te.getBlockState().getValue(BlockRotatable.FACING);
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
	
	public static void renderString(MatrixStack matrix, String msg, float x, float y, float z, float scale, 
			float[] RGBA, float shadowTint) {
		matrix.pushPose();
		matrix.translate(x, y, z);
		matrix.scale(scale, scale, scale);
		Minecraft.getInstance().font.draw(matrix, msg, 0, 0, Utils.getHexIntFromRGBA(RGBA));
		Minecraft.getInstance().font.drawShadow(matrix, msg, 0, 0, Utils.getHexIntFromRGB(shadowTint, shadowTint, shadowTint));
		matrix.popPose();
	}
	
	public static void renderString(MatrixStack matrix, String msg, float x, float y, float z, float scale, 
			float[] RGBA, boolean drawShadow) {
		matrix.pushPose();
		matrix.translate(x, y, z);
		matrix.scale(scale, scale, scale);
		RenderSystem.color4f(1, 1, 1, 0.1f);
		Minecraft.getInstance().font.draw(matrix, msg, 0, 0, Utils.getHexIntFromRGBA(RGBA));
		if(drawShadow)
			Minecraft.getInstance().font.drawShadow(matrix, msg, 0, 0, Utils.getHexIntFromRGB(0.3f, 0.3f, 0.3f));
		RenderSystem.disableAlphaTest();
		matrix.popPose();
	}
	
	public static void renderEntity(MatrixStack matrix, Entity entity, float x, float y, float z, float scale, 
			float[] RGBA, IRenderTypeBuffer bufferIn) {
		Minecraft.getInstance().getEntityRenderDispatcher().render(entity, 0, 0, 0, 0, 0.128f, matrix, 
				bufferIn, Utils.getHexIntFromRGBA(RGBA));
		
	}
	
	public static void applyPlayerTexture(String username) {
		applyPlayerTexture(Utils.getProfile(username));
	}
	
	public static ResourceLocation getPlayerTexture(String username) {
		return getPlayerTexture(Utils.getProfile(username));
	}
	
	public static ResourceLocation getPlayerTexture(GameProfile profile) {
		ResourceLocation resourcelocation = DefaultPlayerSkin.getDefaultSkin();
		if(profile != null) {
            Map<Type, MinecraftProfileTexture> map = Minecraft.getInstance().getSkinManager().getInsecureSkinInformation(profile);

            if (map.containsKey(Type.SKIN))
            {
                resourcelocation = Minecraft.getInstance().getSkinManager().registerTexture(map.get(Type.SKIN), Type.SKIN);
            }
		}
		return resourcelocation;
	}
	
	public static void applyPlayerTexture(GameProfile profile) {
		ResourceLocation resourcelocation = DefaultPlayerSkin.getDefaultSkin();
		if(profile != null) {
            Map<Type, MinecraftProfileTexture> map = Minecraft.getInstance().getSkinManager().getInsecureSkinInformation(profile);

            if (map.containsKey(Type.SKIN))
            {
                resourcelocation = Minecraft.getInstance().getSkinManager().registerTexture(map.get(Type.SKIN), Type.SKIN);
            }
		}
        Minecraft.getInstance().getTextureManager().bind(resourcelocation);
	}
	
	public static void renderPlayerGameProfile(MatrixStack matrix, String username, float x, float y, float z, float scale, 
			float[] RGBA, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn, boolean isChild) {
		renderPlayerGameProfile(matrix, Utils.getProfile(username), x, y, z, scale, RGBA, bufferIn, combinedLightIn, combinedOverlayIn, isChild);
	}
	
	public static void renderPlayerGameProfile(MatrixStack matrix, GameProfile profile, float x, float y, float z, float scale, 
			float[] RGBA, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn , boolean isChild) {
		PlayerModel<LivingEntity> player = new PlayerModel<LivingEntity>(0.0125f, false);
		player.young = isChild;

		matrix.translate(x, y, z);
		matrix.mulPose(Vector3f.ZP.rotationDegrees(180));
		matrix.scale(scale, scale, scale);
		RenderSystem.enableTexture();
		matrix.pushPose();
		IVertexBuilder buffer = bufferIn.getBuffer(RenderType.entityTranslucentCull(getPlayerTexture(profile)));
		player.renderToBuffer(matrix, buffer, combinedLightIn, combinedOverlayIn, 
				RGBA.length > 0 ? RGBA[0] : 1.0f, RGBA.length > 1 ? RGBA[1] : 1.0f, RGBA.length > 2 ? RGBA[2] : 1.0f, RGBA.length > 3 ? RGBA[3] : 1.0f);
		matrix.popPose();
	}
	
	public static void renderPlayer(MatrixStack matrix, PlayerEntity player, float x, float y, float z, float scale, 
			float[] RGBA, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn, boolean isChild) {
		if(player == null)
			return;
		
		Minecraft.getInstance().getEntityRenderDispatcher().setRenderShadow(false);
		Minecraft.getInstance().getEntityRenderDispatcher().render(player, x, y, z, 0.0f, 0f, matrix, bufferIn, 
				combinedLightIn);
		Minecraft.getInstance().getEntityRenderDispatcher().setRenderShadow(true);
	}
	
	public static void renderString(MatrixStack matrix, String msg, float x, float y, float z, float scale, boolean drawShadow) {
		renderString(matrix, msg, x, y, z, scale, new float[] {}, drawShadow);
	}
	
	public static void renderString(MatrixStack matrix, String msg, float x, float y, float z, float scale) {
		renderString(matrix, msg, x, y, z, scale, false);
	}
	
	public static void renderString(MatrixStack matrix, String msg, float x, float y, float z, float scale,
			float red, float green, float blue, boolean drawShadow) {
		renderString(matrix, msg, x, y, z, scale, new float[] {red, green, blue}, drawShadow);
	}
	
	public static void renderString(MatrixStack matrix, String msg, float x, float y, float z, float scale, float shadowTint) {
		renderString(matrix, msg, x, y, z, scale, new float[] {}, shadowTint);
	}
	
	public static void renderString(MatrixStack matrix, String msg, float x, float y, float z, 
			float red, float green, float blue, float shadowTint) {
		renderString(matrix, msg, x, y, z, 1.0f, new float[] {red, green, blue}, shadowTint);
	}
	
	public static void renderString(MatrixStack matrix, String msg, float x, float y, float z, 
			float[] RGBA, float shadowTint) {
		renderString(matrix, msg, x, y, z, 1.0f, RGBA, shadowTint);
	}
	
	public static void renderString(MatrixStack matrix, String msg, float x, float y, float z) {
		renderString(matrix, msg, x, y, z, 1.0f);
	}
	
	public static void renderString(MatrixStack matrix, String msg, float x, float y, float z,
			float red, float green, float blue, boolean drawShadow) {
		renderString(matrix, msg, x, y, z, 1.0f, new float[] {red, green, blue}, drawShadow);
	}
	
	public static void renderString(MatrixStack matrix, String msg, float x, float y, float z,
			float[] RGBA, boolean drawShadow) {
		renderString(matrix, msg, x, y, z, 1.0f, RGBA, drawShadow);
	}
	
	public static void renderString(MatrixStack matrix, String msg, float x, float y, float z,
			float red, float green, float blue) {
		renderString(matrix, msg, x, y, z, red, green, blue, false);
	}
	
	public static void renderString(MatrixStack matrix, String msg, float x, float y, float z,
			float[] RGBA) {
		renderString(matrix, msg, x, y, z, RGBA, false);
	}
	
/*
 * 	===========================================================================================
 * 										Advanced OpenGL
 * 	===========================================================================================
 */
	
	public static float[] screenCoordToUV(float u, float v) {
		MainWindow window = Minecraft.getInstance().getWindow();
		Screen scr = Minecraft.getInstance().screen;
		float f = (float)window.getWidth()/(float)scr.width;
		return new float[] {(float)u*f/(float)window.getWidth(), (float)v*f/(float)window.getHeight()};
	}
	
	public static int createFrameBuffer() {
		int frameBuffer = GL30.glGenFramebuffers();
		return frameBuffer;
	}
	
	public static void bindFrameBuffer(int fbo) {
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, fbo);
	}
	
	/**
	 * @return any of:</br> <b>
	 * GL_FRAMEBUFFER_COMPLETE</br>
	 * GL_FRAMEBUFFER_UNDEFINED</br>
	 * GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT</br>
	 * GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT</br>
	 * GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER</br>
	 * GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER</br>
	 * GL_FRAMEBUFFER_UNSUPPORTED</br>
	 * GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE</br>
	 * GL_FRAMEBUFFER_INCOMPLETE_LAYER_TARGETS</br></b>
	 */
	public static int checkFrameBufferStatus() {
		return GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER);
	}
	
	public static void resetToDefaultFrameBuffer() {
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);   
	}
	
	public static void resetToDefaultFrameBuffer(int fbo) {
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0); 
		GL30.glDeleteFramebuffers(fbo); 
	}
	
	/**
	 * 
	 * @param target the framebuffer type we're targeting (draw, read or both).
	 * @param attachment the type of attachment we're going to attach. Right now we're attaching a color attachment. 
	 * @param textarget the type of the texture you want to attach.
	 * @param texture the actual texture to attach.
	 * @param level he mipmap level.
	 */
	public static void attachFrameBufferTexture2D(int target, int attachment, int textarget, int texture, int level) {
		GL30.glFramebufferTexture2D(target, attachment, textarget, textarget, level);
	}
	
	public static void attachTextureToFrameBuffer(int texture) {
	    glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, texture, 0);
	}
	
	public static int attachNewTextureToCurrentFrameBuffer(int width, int height) {
		int texture = createTextureAttachment(width, height);
		attachFrameBufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL30.GL_TEXTURE_2D, texture, 0);
		return texture;
	}
	
	public static void attachDepthTexToFrameBuffer(int depthBuffer) {
		attachFrameBufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_DEPTH_COMPONENT, depthBuffer, 0);
	}
	
	public static void attachStencilTexToFrameBuffer(int stencil) {
		attachFrameBufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_STENCIL_ATTACHMENT, GL30.GL_STENCIL_INDEX, stencil, 0);
	}
	
	public static void setCurrentTextureAsDepthAndStencil(int width, int height) {
		GL30.glTexImage2D(GL30.GL_TEXTURE_2D, 0, GL30.GL_DEPTH24_STENCIL8, width, height, 0, GL30.GL_DEPTH_STENCIL, GL30.GL_UNSIGNED_INT_24_8, (ByteBuffer)null);
	}
	
	public static void attachNewDepthAndStencilAttachment(int width, int height) {
		int texture = createTextureAttachment(width, height);
		setCurrentTextureAsDepthAndStencil(width, height);
		attachDepthAndStencilAttachment(texture);
	}
	
	public static void attachDepthAndStencilAttachment(int texture) {
		attachFrameBufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_STENCIL_ATTACHMENT, GL30.GL_TEXTURE_2D, texture, 0);
	}
	
	public static int createTextureAttachment() {
		return createTextureAttachment(800, 600);
	}
	
	public static int createTextureAttachment(@NativeType("GLsizei") int width, @NativeType("GLsizei") int height) {
		int texture = glGenTextures();
		glBindTexture(GL30.GL_TEXTURE_2D, texture);
		glBindTexture(GL_TEXTURE_2D, texture);
	    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, (ByteBuffer)null);
	    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
	    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		return texture;
	}
	
	public static int createRenderBufferObject() {
		return GL30.glGenRenderbuffers();
	}
	
	public static void bindRenderbuffer(int rbo) {
		GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, rbo);
	}
	
	public static int createDepthAndStencilRenderbuffer(int width, int height) {
		int rbo = createRenderBufferObject();
		bindRenderbuffer(rbo);
		setStoredRboToDepthAndStencil(width, height);
		return rbo;
	}
	
	public static int attachNewDepthAndStencilRenderBuffer(int width, int height) {
		int rbo = createDepthAndStencilRenderbuffer(width, height);
		attachRenderBuffer(rbo);
		return rbo;
	}
	
	public static void attachRenderBuffer(int rbo) {
		GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_STENCIL_ATTACHMENT, GL30.GL_RENDERBUFFER, rbo);
	}
	
	public static void setStoredRboToDepthAndStencil(int width, int height) {
		GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL30.GL_DEPTH24_STENCIL8, width, height);
	}
	
	public static void bindTexture(int texture) {
		GL30.glBindTexture(GL30.GL_TEXTURE_2D, texture);
	}
	
	public static void bindDefaultTexture() {
		bindTexture(0);
	}
	
	public static void bindDefaultFrameBuffer() {
		bindFrameBuffer(1);
	}
	
	public static int[] setupRenderToTexture() {
		MainWindow win = Minecraft.getInstance().getWindow();
		return setupRenderToTexture(win.getWidth(), win.getHeight());
	}
	
	/**
	 * 
	 * @param width
	 * @param height
	 * @return a array of ints -> int[3] {fbo, texture, rbo}
	 */
	public static int[] setupRenderToTexture(int width, int height) {
		int fbo = createFrameBuffer();
		bindFrameBuffer(fbo);
		
		int texture = createTextureAttachment(width, height);
		attachTextureToFrameBuffer(texture);
		
		int rbo = createDepthAndStencilRenderbuffer(width, height);
		attachRenderBuffer(rbo);
		
		if(checkFrameBufferStatus() != GL30.GL_FRAMEBUFFER_COMPLETE)
			System.err.println("ERROR::FRAMEBUFFER:: Framebuffer is not complete!");
		bindRenderbuffer(0);
		return new int[] {fbo, texture, rbo};
	}
	
	public static void stopRenderToTexture() {
		bindDefaultFrameBuffer();
		bindDefaultTexture();
	}
	
	public static void destoryFBO(int[] fbo) {
		if(fbo.length > 0)
			GL30.glDeleteFramebuffers(fbo[0]);
		if(fbo.length > 1)
			GL30.glDeleteTextures(fbo[1]);
		if(fbo.length > 2)
			GL30.glDeleteRenderbuffers(fbo[2]);
	}
	
	public static void destoryFBO(int fbo) {
		GL30.glDeleteFramebuffers(fbo);
	}
	
	// https://www.curseforge.com/minecraft/mc-mods/stencil-shenanigans/files/2931876
	public static void setupStencil() {		
		RenderSystem.clearStencil(0xFF);
		
		EnableStencils();
		setStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_REPLACE);
		clearStencilBuffer();
		setStencilFunc(GL11.GL_ALWAYS, 1, 0xFF); // all fragments should pass the stencil test
		setStencilMask(0xFF);
		//Draw the Things that need to be replaced!
	}
	
	// https://www.curseforge.com/minecraft/mc-mods/stencil-shenanigans/files/2931876
		public static void setupStencil(boolean clearStencil) {	
			if(clearStencil)
				RenderSystem.clearStencil(0xFF);
			
			EnableStencils();
			setStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_REPLACE);
			clearStencilBuffer();
			setStencilFunc(GL11.GL_ALWAYS, 1, 0xFF); // all fragments should pass the stencil test
			setStencilMask(0xFF);
			//Draw the Things that need to be replaced!
		}
	
	// https://www.curseforge.com/minecraft/mc-mods/stencil-shenanigans/files/2931876
		public static void setupStencilInverse() {		
			RenderSystem.clearStencil(0xFF);
			
			EnableStencils();
			setStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_REPLACE);
			clearStencilBuffer();
			setStencilFunc(GL11.GL_ALWAYS, 0, 0xFF); // all fragments should pass the stencil test
			setStencilMask(0xFF);
			//Draw the Things that need to be replaced!
		}
	
	//TODO render outside (with stencilFunc GL_NOTEQUAL)
	public static void setupRenderInside() {
		setStencilFunc(GL11.GL_EQUAL, 1, 0xFF);
		setStencilMask(0x00); // disable writing to the stencil buffer (because we only want to read the stencil buffer from this point, not write to it anymore)
		RenderSystem.disableDepthTest();
		RenderSystem.depthMask(false);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		//Render whatever is inside of the stencil/window! (portal??)
	}
	
	public static void setupRenderOutside() {
		setStencilFunc(GL11.GL_NOTEQUAL, 1, 0xFF);
		setStencilMask(0x00); // disable writing to the stencil buffer (because we only want to read the stencil buffer from this point, not write to it anymore)
		RenderSystem.disableDepthTest();
		RenderSystem.depthMask(false);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		//Render whatever is inside of the stencil/window! (portal??)
	}
	
	public static void disableStencil() {
		setStencilMask(0xFF);
		setStencilFunc(GL11.GL_ALWAYS, 0, 0xFF);  
		resetStencilFunctions();
		disableStencils();
		RenderSystem.enableDepthTest();
		RenderSystem.depthMask(true);
//		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}
	
	public static void disableStencil(boolean clearStencil) {
		setStencilMask(0xFF);
		setStencilFunc(GL11.GL_ALWAYS, 0, 0xFF);  
		resetStencilFunctions();
		disableStencils(clearStencil);
		RenderSystem.enableDepthTest();
		RenderSystem.depthMask(true);
//		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}
	
	/**
	 * Stencil Info: https://learnopengl.com/Advanced-OpenGL/Stencil-testing
	 */
	public static void EnableStencils() {
		GL20.glEnable(GL20.GL_STENCIL_TEST);
		if(!Minecraft.getInstance().getMainRenderTarget().isStencilEnabled()) {
			Minecraft.getInstance().getMainRenderTarget().enableStencil();
		}
		clearStencilBuffer();
	}
	
	/**
	 * Stencil Info: https://learnopengl.com/Advanced-OpenGL/Stencil-testing
	 */
	private static void EnableStencils(boolean clearStencil) {
		GL20.glEnable(GL20.GL_STENCIL_TEST);
		if(!Minecraft.getInstance().getMainRenderTarget().isStencilEnabled()) {
			Minecraft.getInstance().getMainRenderTarget().enableStencil();
		}
		if(clearStencil)
			clearStencilBuffer();
	}
	
	
	public static void clearStencilBuffer() {
		RenderSystem.clear(GL11.GL_STENCIL_BUFFER_BIT, false);
//		GL20.glClear(GL20.GL_STENCIL_BUFFER_BIT);
	}
	
	private static void disableStencils() {
		clearStencilBuffer();
		GL20.glDisable(GL20.GL_STENCIL_TEST);
	}
	
	private static void disableStencils(boolean clearStencil) {
		if(clearStencil)
			clearStencilBuffer();
		GL20.glDisable(GL20.GL_STENCIL_TEST);
	}
	
	/**
	 * 0xFF -> each bit is written to the stencil buffer as is,</br>
	 * 0x00 -> each bit ends up as 0 in the stencil buffer (disabling writes)
	 * @param value
	 */
	public static void setStencilMask(int value) {
		RenderSystem.stencilMask(value);
//		GL20.glStencilMask(value);
	}
	
	/**
	 * @param func	-> GL_NEVER, GL_LESS, GL_LEQUAL, GL_GREATER, GL_GEQUAL, GL_EQUAL, GL_NOTEQUAL, GL_ALWAYS
	 * @param ref	-> the value to compare to (if 1 and the func is GL_EQUAL only parts of the buffer that contain exact 1 will pass)
	 * @param mask	-> the mask to compare (also the applied stencil buffer?) (0xFF)
	 */
	public static void setStencilFunc(int func, int ref, int mask) {
		RenderSystem.stencilFunc(func, ref, mask);
//		GL20.glStencilFunc(func, ref, mask);
	}
	
	/**
	 *	'SetStencilOperation'</br></br>
	 *  contains three options of which we can specify for each option what action to take:</br><i>
	 *  -sfail: action to take if the stencil test fails.</br>
	 *  -dpfail: action to take if the stencil test passes, but the depth test fails.</br>
	 *  -dppass: action to take if both the stencil and the depth test pass.</i>
	 *  </br></br>
	 *  Then for each of the options you can take any of the following actions:</br>
	 * 
	 * <b>GL_KEEP</b>		The currently stored stencil value is kept.</br>
	 * <b>GL_ZERO</b>		The stencil value is set to 0.</br>
	 * <b>GL_REPLACE</b>	The stencil value is replaced with the reference value set with glStencilFunc.</br>
	 * <b>GL_INCR</b>		The stencil value is increased by 1 if it is lower than the maximum value.</br>
	 * <b>GL_INCR_WRAP</b>	Same as GL_INCR, but wraps it back to 0 as soon as the maximum value is exceeded.</br>
	 * <b>GL_DECR</b>	The stencil value is decreased by 1 if it is higher than the minimum value.</br>
	 * <b>GL_DECR_WRAP</b>	Same as GL_DECR, but wraps it to the maximum value if it ends up lower than 0.</br>
	 * <b>GL_INVERT</b>	Bitwise inverts the current stencil buffer value.</br>
	 * @param sfail
	 * @param dpfail
	 * @param dppass
	 */
	public static void setStencilOp(int sfail, int dpfail, int dppass) {
		RenderSystem.stencilOp(sfail, dpfail, dppass);
//		GL20.glStencilOp(sfail, dpfail, dppass);
	}
	
	public static void resetStencilFunctions() {
		setStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_KEEP);
	}
}
