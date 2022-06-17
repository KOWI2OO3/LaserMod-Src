package KOWI2003.LaserMod.tileentities.render.models;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class AdvancedLaserModel extends EntityModel<Entity> {
	private final ModelRenderer gimbal;
	public final ModelRenderer Roll;
	private final ModelRenderer gimbalBase_r1;
	public final ModelRenderer Pitch;
	private final ModelRenderer gimbalBaseServo_r1;
	private final ModelRenderer gimbalBase_r2;
	private final ModelRenderer gimbalBase_r3;
	public final ModelRenderer Yaw;
	private final ModelRenderer gimbalBase_r4;
	private final ModelRenderer gimbalBase_r5;
	private final ModelRenderer gimbalBase_r6;
	private final ModelRenderer Attachment;
	private final ModelRenderer gimbalBase_r7;
	private final ModelRenderer gimbalBase_r8;
	private final ModelRenderer Glass_r1;
	private final ModelRenderer gimbalBase_r9;

	public AdvancedLaserModel(float scale) {
		super(RenderType::entityTranslucent);

		this.gimbalBase_r9 = new ModelRenderer(this);
		this.gimbalBase_r8 = new ModelRenderer(this);
		this.gimbalBase_r7 = new ModelRenderer(this);
		this.gimbalBase_r6 = new ModelRenderer(this);
		this.gimbalBase_r5 = new ModelRenderer(this);
		this.gimbalBase_r4 = new ModelRenderer(this);
		this.gimbalBaseServo_r1 = new ModelRenderer(this);
		this.Glass_r1 = new ModelRenderer(this);
		
		texWidth = 16;
		texHeight= 16;

		gimbal = new ModelRenderer(this);
		gimbal.setPos(-8.0F, 16.0F, 8.0F);
		

		Roll = new ModelRenderer(this);
		Roll.setPos(0.0F, 4.0F, 0.0F);
		gimbal.addChild(Roll);
		Roll.texOffs(0, 3).addBox(-3.25F, 3.25F, -1.75F, 1.0F, 0.0F, 3.0F, scale, false);
		Roll.texOffs(0, 3).addBox(-3.975F, -1.425F, -1.75F, 0.0F, 4.0F, 3.0F, scale, false);
		Roll.texOffs(0, 4).addBox(-3.725F, -1.175F, -1.125F, 0.0F, 2.0F, 2.0F, scale, false);

		gimbalBase_r1 = new ModelRenderer(this);
		gimbalBase_r1.setPos(4.325F, 0.5F, 0.0F);
		Roll.addChild(gimbalBase_r1);
		setRotationAngle(gimbalBase_r1, 0.0F, 0.0F, 0.7854F);
		gimbalBase_r1.texOffs(0, 3).addBox(-4.255F, 7.38F, -1.75F, 0.71F, 0.0F, 3.0F, scale, false);

		Pitch = new ModelRenderer(this);
		Pitch.setPos(0.0F, 0.0F, 0.0F);
		Roll.addChild(Pitch);
		Pitch.texOffs(0, 4).addBox(-3.625F, -1.25F, -1.5F, 0.0F, 2.2F, 4.0F, scale, false);
		Pitch.texOffs(0, 4).addBox(-2.765F, -1.25F, 3.425F, 4.0F, 2.2F, 0.0F, scale, false);
		Pitch.texOffs(0, 2).addBox(-1.05F, -1.0F, 3.3F, 1.8F, 1.8F, 0.0F, scale, false);

		gimbalBase_r2 = new ModelRenderer(this);
		gimbalBase_r2.setPos(-1.1F, -6.25F, 2.975F);
		Pitch.addChild(gimbalBase_r2);
		setRotationAngle(gimbalBase_r2, 0.0F, 0.75F, 0.0F);
		gimbalBase_r2.texOffs(0, 1).addBox(-1.575F, 5.0F, -1.93F, 0.0F, 2.2F, 1.0F, scale, false);

		Yaw = new ModelRenderer(this);
		Yaw.setPos(0.1F, 0.0F, 0.0F);
		Pitch.addChild(Yaw);
		Yaw.texOffs(2, 2).addBox(-1.25F, -2F, 3.1F, 2.0F, 3.0F, 0.0F, scale, false);
		Yaw.texOffs(2, 4).addBox(-1.25F, -2.47F, 1.65F - 0.94f, 2.0F, 0.0F, 1.94F, scale, false);

		gimbalBase_r3 = new ModelRenderer(this);
		gimbalBase_r3.setPos(6.25F, -3.925F, 3.45F);
		Yaw.addChild(gimbalBase_r3);
		setRotationAngle(gimbalBase_r3, 0.75F, 0.0F, 0.0F);
		gimbalBase_r3.texOffs(2, 2).addBox(-7.5F, 0.64F, -1.52F, 2.0F, 0.4F, 0.0F, scale, false);

		Attachment = new ModelRenderer(this);
		Attachment.setPos(0.1F, 0.0F, 0.0F);
		Yaw.addChild(Attachment);
		Attachment.texOffs(2, 0).addBox(0.65F, -2.47F, -0.825F - 0.2f, 0.0F, 0.0F, 1.5F, scale, false);
//		Attachment.texOffs(0, 16).addBox(-1.125F, -2.275F, -0.825F, 2.0F, 0.0F, 1.0F, scale, false);
		Attachment.texOffs(2, 0).addBox(-1.35F, -2.47F, -0.825F - 0.2f, 0.0F, 0.0F, 1.5F, scale, false);
		Attachment.texOffs(2, 2).addBox(-1.35F, -2.47F, -1.075F - 0.2f, 2.0F, 0.0F, 0.0F, scale, false);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}

	@Override
	public void setupAnim(Entity p_225597_1_, float p_225597_2_, float p_225597_3_, float p_225597_4_,
			float p_225597_5_, float p_225597_6_) {
	}

	@Override
	public void renderToBuffer(MatrixStack matrix, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		gimbal.render(matrix, buffer, packedLight, packedOverlay);
	}
}