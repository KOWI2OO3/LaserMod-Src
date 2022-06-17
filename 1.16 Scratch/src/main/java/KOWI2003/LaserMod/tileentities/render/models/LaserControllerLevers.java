package KOWI2003.LaserMod.tileentities.render.models;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class LaserControllerLevers extends EntityModel<Entity> {
	public final ModelRenderer MainLever;
	public final ModelRenderer top_r1;
	public final ModelRenderer ModeSlider;
	public final ModelRenderer Blue;
	public final ModelRenderer Green;
	public final ModelRenderer Red;

	public LaserControllerLevers() {
		super(RenderType::entityTranslucent);
		texWidth = 16;
		texHeight = 16;

		MainLever = new ModelRenderer(this);
		MainLever.setPos(7.65F, 10.4F, 0.5F);
		

		top_r1 = new ModelRenderer(this);
		top_r1.setPos(-7.0F, 2.425F, 8F);
		MainLever.addChild(top_r1);
		setRotationAngle(top_r1, 0.7854F, 0.0F, 0.0F);
		top_r1.texOffs(10, 3).addBox(2.6F, -9.6F, -4.0F, 2.0F, 0.0F, 0.0F, 0.3F, false);
		top_r1.texOffs(0, 0).addBox(2.7F, -9.3F, -4.0F, 0.0F, 2.0F, 0.0F, 0.25F, false);
		top_r1.texOffs(0, 0).addBox(4.4F, -9.3F, -4.0F, 0.0F, 2.0F, 0.0F, 0.25F, false);

		ModeSlider = new ModelRenderer(this);
		ModeSlider.setPos(16.95F, 15.1F, 7.9F);
		ModeSlider.texOffs(11, 1).addBox(-4.6F, -3.7F, -8.3F, 0.0F, 0.0F, 0.0F, 0.3F, false);
		ModeSlider.texOffs(0, 1).addBox(-4.6F, -3.7F, -8.1F, 0.0F, 0.0F, 1.0F, 0.25F, false);

		Blue = new ModelRenderer(this);
		Blue.setPos(17.1F, 2.35F, 7.9F);
		Blue.texOffs(12, 1).addBox(-4.7F, 2.7F, -8.3F, 0.0F, 0.0F, 0.0F, 0.3F, false);
		Blue.texOffs(0, 1).addBox(-4.7F, 2.7F, -8.1F, 0.0F, 0.0F, 1.0F, 0.25F, false);

		Green = new ModelRenderer(this);
		Green.setPos(17.1F, 4.9F, 7.9F);
		Green.texOffs(11, 4).addBox(-4.7F, 1.4F, -8.3F, 0.0F, 0.0F, 0.0F, 0.3F, false);
		Green.texOffs(0, 1).addBox(-4.7F, 1.4F, -8.1F, 0.0F, 0.0F, 1.0F, 0.25F, false);

		Red = new ModelRenderer(this);
		Red.setPos(17.1F, 7.55F, 7.9F);
		Red.texOffs(11, 3).addBox(-4.7F, 0.1F, -8.3F, 0.0F, 0.0F, 0.0F, 0.3F, false);
		Red.texOffs(0, 1).addBox(-4.7F, 0.1F, -8.1F, 0.0F, 0.0F, 1.0F, 0.25F, false);
	}

	@Override
	public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		MainLever.render(matrixStack, buffer, packedLight, packedOverlay);
		ModeSlider.render(matrixStack, buffer, packedLight, packedOverlay);
		Blue.render(matrixStack, buffer, packedLight, packedOverlay);
		Green.render(matrixStack, buffer, packedLight, packedOverlay);
		Red.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
	
	public void setValues(float red, float green, float blue, float mode, float modeMax) {
		ModeSlider.setPos(16.95F, 15.1F - (1.7f * (mode/modeMax)) , 7.9F);
		Red.setPos(17.1F - (3.1f * red), 7.55F, 7.9F);
		Green.setPos(17.1F - (3.1f * green), 4.9F, 7.9F);
		Blue.setPos(17.1F - (3.1f * blue), 2.35F, 7.9F);
	}
	
	@Override
	public void setupAnim(Entity p_225597_1_, float p_225597_2_, float p_225597_3_, float p_225597_4_,
			float p_225597_5_, float p_225597_6_) {
		
	}
}