package KOWI2003.LaserMod.tileentities.render.models;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class AdvancedLaser extends EntityModel<Entity> {

    //fields
    public ModelRenderer e1;
    public ModelRenderer e2;
    public ModelRenderer e3;
    public ModelRenderer e4;
    public ModelRenderer e5;
    public ModelRenderer e6;
    public ModelRenderer e7;
    public ModelRenderer e8;
    public ModelRenderer e9;
    public ModelRenderer e10;
    public ModelRenderer e11;
    public ModelRenderer e12;
    public ModelRenderer e13;
    public ModelRenderer e14;
    public ModelRenderer e15;
    public ModelRenderer e16;
    public ModelRenderer e17;
    public ModelRenderer e18;
    public ModelRenderer e19;

    public AdvancedLaser()
    {
        texWidth = 64;
        texHeight = 64;

        e1 = new ModelRenderer(this, 0, 26);
        e1.setPos(-2.5F, 24F, -2.5F);
        e1.addBox(0F, -0.5F, 0F, 5, 1, 5);
        e1.texOffs(64, 64);
        e1.mirror = false;
        setRotation(e1, 0F, 0F, 0F);
        e2 = new ModelRenderer(this, 0, 26);
        e2.setPos(-2.5F, 24.75F, -2.5F);
        e2.addBox(0F, -0.5F, 0F, 5, 1, 5);
        e2.texOffs(64, 64);
        e2.mirror = false;
        setRotation(e2, 0F, 0F, 0F);
        e3 = new ModelRenderer(this, 12, 21);
        e3.setPos(-2F, 24.75F, -2F);
        e3.addBox(0F, -1F, 0F, 4, 1, 4);
        e3.texOffs(64, 64);
        e3.mirror = false;
        setRotation(e3, 0F, 0F, 0F);
        e4 = new ModelRenderer(this, 12, 16);
        e4.setPos(-2F, 24.25F, -2F);
        e4.addBox(0F, -0.75F, 0F, 4, 1, 4);
        e4.texOffs(64, 64);
        e4.mirror = false;
        setRotation(e4, 0F, 0F, 0F);
        e5 = new ModelRenderer(this, 20, 27);
        e5.setPos(0F, 17F, 0F);
        e5.addBox(1.5F, 7.25F, -1.75F, 2, 1, 4);
        e5.texOffs(64, 64);
        e5.mirror = false;
        setRotation(e5, 0F, 0F, 0F);
        e6 = new ModelRenderer(this, 0, 9);
        e6.setPos(2.225F, 17.675F, 0F);
        e6.addBox(1.499999F, 3.25F, -1.75F, 1, 5, 4);
        e6.texOffs(64, 64);
        e6.mirror = false;
        setRotation(e6, 0F, 0F, 0F);
        e7 = new ModelRenderer(this, 12, 10);
        e7.setPos(1.975F, 19.15F, -0.1750002F);
        e7.addBox(1.624999F, 5.425F, -0.9499998F, 1, 3, 3);
        e7.texOffs(64, 64);
        e7.mirror = false;
        setRotation(e7, 0F, 0F, 0F);
        e8 = new ModelRenderer(this, 0, 4);
        e8.setPos(-4.325F, 12.5F, 0F);
        e8.addBox(3.25F, 7.25F, -1.75F, 1, 1, 4);
        e8.texOffs(64, 64);
        e8.mirror = false;
        setRotation(e8, 0F, 0F, 0.7853982F);
        e9 = new ModelRenderer(this, 0, 18);
        e9.setPos(1.85F, 19.25F, 0.25F);
        e9.addBox(1.525F, 5F, -1.75F, 1, 3, 5);
        e9.texOffs(64, 64);
        e9.mirror = false;
        setRotation(e9, 0F, 0F, 0F);
        e10 = new ModelRenderer(this, 28, 22);
        e10.setPos(1.1F, 19.25F, 2.975F);
        e10.addBox(1.525F, 5F, -1.750001F, 1, 3, 1);
        e10.texOffs(64, 64);
        e10.mirror = false;
        setRotation(e10, 0F, 0.7853982F, 0F);
        e11 = new ModelRenderer(this, 0, 0);
        e11.setPos(0.1000004F, 19.25F, 5.2F);
        e11.addBox(-1.375F, 5F, -1.775001F, 5, 3, 1);
        e11.texOffs(64, 64);
        e11.mirror = false;
        setRotation(e11, 0F, 0F, 0F);
        e12 = new ModelRenderer(this, 12, 2);
        e12.setPos(0.75F, 19.5F, 4.95F);
        e12.addBox(-1.75F, 5.5F, -1.65F, 2, 2, 1);
        e12.texOffs(64, 64);
        e12.mirror = false;
        setRotation(e12, 0F, 0F, 0F);
        e13 = new ModelRenderer(this, 12, 5);
        e13.setPos(-6.35F, 25.375F, 4.875F);
        e13.addBox(5F, -1.75F, -1.775001F, 3, 4, 1);
        e13.texOffs(64, 64);
        e13.mirror = false;
        setRotation(e13, 0F, 0F, 0F);
        e14 = new ModelRenderer(this, 20, 13);
        e14.setPos(-6.35F, 28.275F, 1.55F);
        e14.addBox(5F, 1.525F, -0.5F, 3, 1, 2);
        e14.texOffs(64, 64);
        e14.mirror = false;
        setRotation(e14, 0F, 0F, 0F);
        e15 = new ModelRenderer(this, 12, 0);
        e15.setPos(-6.35F, 29.175F, 3.45F);
        e15.addBox(5F, 0.7500005F, -1.775F, 3, 1, 1);
        e15.texOffs(64, 64);
        e15.mirror = false;
        setRotation(e15, 0.7853982F, 0F, 0F);
        e16 = new ModelRenderer(this, 20, 7);
        e16.setPos(-6.35F, 28.275F, -0.3249998F);
        e16.addBox(5F, 1.525F, -0.5000005F, 1, 1, 2);
        e16.texOffs(64, 64);
        e16.mirror = false;
        setRotation(e16, 0F, 0F, 0F);
        e17 = new ModelRenderer(this, 20, 10);
        e17.setPos(-6.125F, 28.325F, -0.3249998F);
        e17.addBox(5F, 1.55F, -0.5000005F, 3, 1, 2);
        e17.texOffs(64, 64);
        e17.mirror = false;
        setRotation(e17, 0F, 0F, 0F);
        e18 = new ModelRenderer(this, 20, 7);
        e18.setPos(-4.1F, 28.275F, -0.3249998F);
        e18.addBox(5F, 1.525F, -0.5000005F, 1, 1, 2);
        e18.texOffs(64, 64);
        e18.mirror = false;
        setRotation(e18, 0F, 0F, 0F);
        e19 = new ModelRenderer(this, 12, 0);
        e19.setPos(-4.1F, 28.275F, -2.2F);
        e19.addBox(2.75F, 1.525F, 1.125F, 3, 1, 1);
        e19.texOffs(64, 64);
        e19.mirror = false;
        setRotation(e19, 0F, 0F, 0F);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z)
    {
        model.xRot = x;
        model.yRot = y;
        model.zRot = z;
    }

	@Override
	public void setupAnim(Entity p_225597_1_, float p_225597_2_, float p_225597_3_, float p_225597_4_,
			float p_225597_5_, float p_225597_6_) {
		
	}

	@Override
	public void renderToBuffer(MatrixStack matrix, IVertexBuilder buffer, int combinedLight, int combinedLightOverlay,
			float red, float green, float blue, float alpha) {
		e1.render(matrix, buffer, combinedLight, combinedLightOverlay, red, green, blue, alpha);
		e2.render(matrix, buffer, combinedLight, combinedLightOverlay, red, green, blue, alpha);
		e3.render(matrix, buffer, combinedLight, combinedLightOverlay, red, green, blue, alpha);
		e4.render(matrix, buffer, combinedLight, combinedLightOverlay, red, green, blue, alpha);
		e5.render(matrix, buffer, combinedLight, combinedLightOverlay, red, green, blue, alpha);
		e6.render(matrix, buffer, combinedLight, combinedLightOverlay, red, green, blue, alpha);
		e7.render(matrix, buffer, combinedLight, combinedLightOverlay, red, green, blue, alpha);
		e8.render(matrix, buffer, combinedLight, combinedLightOverlay, red, green, blue, alpha);
		e9.render(matrix, buffer, combinedLight, combinedLightOverlay, red, green, blue, alpha);
		e10.render(matrix, buffer, combinedLight, combinedLightOverlay, red, green, blue, alpha);
		e11.render(matrix, buffer, combinedLight, combinedLightOverlay, red, green, blue, alpha);
		e12.render(matrix, buffer, combinedLight, combinedLightOverlay, red, green, blue, alpha);
		e13.render(matrix, buffer, combinedLight, combinedLightOverlay, red, green, blue, alpha);
		e14.render(matrix, buffer, combinedLight, combinedLightOverlay, red, green, blue, alpha);
		e15.render(matrix, buffer, combinedLight, combinedLightOverlay, red, green, blue, alpha);
		e16.render(matrix, buffer, combinedLight, combinedLightOverlay, red, green, blue, alpha);
		e17.render(matrix, buffer, combinedLight, combinedLightOverlay, red, green, blue, alpha);
		e18.render(matrix, buffer, combinedLight, combinedLightOverlay, red, green, blue, alpha);
		e19.render(matrix, buffer, combinedLight, combinedLightOverlay, red, green, blue, alpha);
	}
}