package KOWI2003.LaserMod.tileentities.render.models;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import KOWI2003.LaserMod.Reference;
import KOWI2003.LaserMod.utils.MathUtils;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3f;

public class AdvancedLaserTop extends EntityModel<Entity> {

    //fields
    public ModelRenderer Rotatingbase;
    public ModelRenderer Piston1base;
    public ModelRenderer Piston1baseattach;
    public ModelRenderer Piston1;
    public ModelRenderer Piston1attach;
    public ModelRenderer Piston2attach;
    public ModelRenderer Piston2baseattach;
    public ModelRenderer Piston2base;
    public ModelRenderer Piston2;
    public ModelRenderer Piston3attach;
    public ModelRenderer Piston3;
    public ModelRenderer Piston3base;
    public ModelRenderer Piston3baseattach;
    public ModelRenderer Piston4baseattach;
    public ModelRenderer Piston4attach;
    public ModelRenderer Piston4base;
    public ModelRenderer Piston4;
    public ModelRenderer Rotatingbase2;
    public ModelRenderer Laserhead;

    public AdvancedLaserTop()
    {
    	float scale = 1f;
        texWidth = 64;
        texHeight = 64;
        
        Rotatingbase = new ModelRenderer(this, 0, 11);
        Rotatingbase.setPos(-8F, 23.35F, -8F);
        Rotatingbase.addBox(-2F, 0F, -2F, 4, 0.5f, 4);
        Rotatingbase.texOffs(64, 64);
        Rotatingbase.mirror = false;
        setRotation(Rotatingbase, 0F, 0F, 0F);
        Piston1base = new ModelRenderer(this, 24, 13);
        Piston1base.setPos(-8F, 22.15F, -6.3F);
        Piston1base.addBox(2F, -0.2499998F, -1.8F, 0.5f, 0.5f, 1.65f);
        Piston1base.texOffs(64, 64);
        Piston1base.mirror = false;
        setRotation(Piston1base, -5.890486F, 0F, 0F);
        Piston1baseattach = new ModelRenderer(this, 8, 0);
        Piston1baseattach.setPos(-6F, 22.35F, -6.6F);
        Piston1baseattach.addBox(0F, -0.5000001F, 0F, 0.6f, 0.6f, 0.6f);
        Piston1baseattach.texOffs(64, 64);
        Piston1baseattach.mirror = false;
        setRotation(Piston1baseattach, 0F, 0F, 0F);
        Piston1 = new ModelRenderer(this, 16, 12);
        Piston1.setPos(-8F, 22.15F, -6.3F);
//        Piston1.addBox(2.05F, -0.2F, -3.61F, 0.4f, 0.4f, 2.3f);
        Piston1.texOffs(64, 64);
        Piston1.mirror = false;
        setRotation(Piston1, -5.890486F, 0F, 0F);
        Piston1attach = new ModelRenderer(this, 12, 4);
        Piston1attach.setPos(-8F, 23.35F, -8F);
        Piston1attach.addBox(2F, 0F, -2F, 0.5f, 0.5f, 0.5f);
        Piston1attach.texOffs(64, 64);
        Piston1attach.mirror = false;
        setRotation(Piston1attach, 0F, 0F, 0F);
        Piston2attach = new ModelRenderer(this, 12, 4);
        Piston2attach.setPos(-8F, 23.35F, -8F);
        Piston2attach.addBox(-2F, 0F, -2.5F, 0.5f, 0.5f, 0.5f);
        Piston2attach.texOffs(64, 64);
        Piston2attach.mirror = false;
        setRotation(Piston2attach, 0F, 0F, 0F);
        Piston2baseattach = new ModelRenderer(this, 8, 0);
        Piston2baseattach.setPos(-6.6F, 22.35F, -10.6F);
        Piston2baseattach.addBox(0F, -0.5000001F, 0F, 0.6f, 0.6f, 0.6f);
        Piston2baseattach.texOffs(64, 64);
        Piston2baseattach.mirror = false;
        setRotation(Piston2baseattach, 0F, 0F, 0F);
        Piston2base = new ModelRenderer(this, 16, 10);
        Piston2base.setPos(-6.3F, 22.15F, -10.3F);
        Piston2base.addBox(-1.8F, -0.2499998F, -0.1999998F, 1.65f, 0.5f, 0.5f);
        Piston2base.texOffs(64, 64);
        Piston2base.mirror = false;
        setRotation(Piston2base, 0F, 0F, 5.890486F);
        Piston2 = new ModelRenderer(this, 0, 0);
        Piston2.setPos(-6.3F, 22.15F, -10.3F);
//        Piston2.addBox(-3.65F, -0.2F, -0.1500001F, 2.3f, 0.4f, 0.4f);
        Piston2.texOffs(64, 64);
        Piston2.mirror = false;
        setRotation(Piston2, 0F, 0F, 5.890486F);
        Piston3attach = new ModelRenderer(this, 12, 4);
        Piston3attach.setPos(-8F, 23.35F, -8F);
        Piston3attach.addBox(1.5F, 0F, 2F, 0.5f, 0.5f, 0.5f);
        Piston3attach.texOffs(64, 64);
        Piston3attach.mirror = false;
        setRotation(Piston3attach, 0F, 0F, 0F);
        Piston3 = new ModelRenderer(this, 0, 0);
        Piston3.setPos(-9.7F, 22.15F, -5.7F);
//        Piston3.addBox(1.45F, -0.2F, -0.25F, 2.3f, 0.4f, 0.4f);
        Piston3.texOffs(64, 64);
        Piston3.mirror = false;
        setRotation(Piston3, 0F, 0F, 0.3926991F);
        Piston3base = new ModelRenderer(this, 16, 10);
        Piston3base.setPos(-9.7F, 22.15F, -5.7F);
        Piston3base.addBox(0.1999998F, -0.2499998F, -0.3000002F, 1.65f, 0.5f, 0.5f);
        Piston3base.texOffs(64, 64);
        Piston3base.mirror = false;
        setRotation(Piston3base, 0F, 0F, 0.3926991F);
        Piston3baseattach = new ModelRenderer(this, 8, 0);
        Piston3baseattach.setPos(-10F, 22.35F, -6F);
        Piston3baseattach.addBox(0F, -0.5000001F, 0F, 0.6f, 0.6f, 0.6f);
        Piston3baseattach.texOffs(64, 64);
        Piston3baseattach.mirror = false;
        setRotation(Piston3baseattach, 0F, 0F, 0F);
        Piston4baseattach = new ModelRenderer(this, 8, 0);
        Piston4baseattach.setPos(-10.6F, 22.35F, -10F);
        Piston4baseattach.addBox(0F, -0.5000001F, 0F, 0.6f, 0.6f, 0.6f);
        Piston4baseattach.texOffs(64, 64);
        Piston4baseattach.mirror = false;
        setRotation(Piston4baseattach, 0F, 0F, 0F);
        Piston4attach = new ModelRenderer(this, 12, 4);
        Piston4attach.setPos(-8F, 23.35F, -8F);
        Piston4attach.addBox(-2.5F, 0F, 1.5F, 0.5f, 0.5f, 0.5f);
        Piston4attach.texOffs(64, 64);
        Piston4attach.mirror = false;
        setRotation(Piston4attach, 0F, 0F, 0F);
        Piston4base = new ModelRenderer(this, 24, 13);
        Piston4base.setPos(-10.3F, 22.15F, -9.7F);
        Piston4base.addBox(-0.1999998F, -0.2499998F, 0.09999943F, 0.5f, 0.5f, 1.65f);
        Piston4base.texOffs(64, 64);
        Piston4base.mirror = false;
        setRotation(Piston4base, -0.3926991F, 0F, 0F);
        Piston4 = new ModelRenderer(this, 16, 12);
        Piston4.setPos(-10.3F, 22.15F, -9.7F);
//        Piston4.addBox(-0.1500003F, -0.2F, 1.49F, 0.4f, 0.4f, 2.3f);
        Piston4.texOffs(64, 64);
        Piston4.mirror = false;
        setRotation(Piston4, -0.3926991F, 0F, 0F);
        Rotatingbase2 = new ModelRenderer(this, 0, 6);
        Rotatingbase2.setPos(-8F, 23.35F, -8F);
        Rotatingbase2.addBox(-1.75F, 0.5F, -1.75F, 3.5f, 0.3f, 3.5f);
        Rotatingbase2.texOffs(64, 64);
        Rotatingbase2.mirror = false;
        setRotation(Rotatingbase2, 0F, 0F, 0F);
        Laserhead = new ModelRenderer(this, 0, 2);
        Laserhead.setPos(-8F, 23.35F, -8F);
        Laserhead.addBox(-1.5F, 0.7F, -1.5F, 3, 0.25f, 3);
        Laserhead.texOffs(16, 16);
        Laserhead.mirror = false;
        setRotation(Laserhead, 0F, 0F, 0F);
    }

    public void setRotation(ModelRenderer model, float x, float y, float z)
    {
        model.xRot = x;
        model.yRot = y;
        model.zRot = z;
    }

	@Override
	public void setupAnim(Entity p_225597_1_, float p_225597_2_, float p_225597_3_, float p_225597_4_,
			float p_225597_5_, float p_225597_6_) {
	}

	public void setTranslationForPlatform(float x, float y, float z) {
        Rotatingbase.setPos(-8F + x, 23.35F + y, -8F + z);
        Rotatingbase2.setPos(-8F + x, 23.35F + y, -8F + z);
        Laserhead.setPos(-8F + x, 23.35F + y, -8F + z);
        Piston1attach.setPos(-8F + x, 23.35F + y, -8F + z);
        Piston2attach.setPos(-8F + x, 23.35F + y, -8F + z);
        Piston3attach.setPos(-8F + x, 23.35F + y, -8F + z);
        Piston4attach.setPos(-8F + x, 23.35F + y, -8F + z);
	}
	
	public void setRotationPlatform(float x, float y) {
		setRotation(Rotatingbase, x, 0, y);
		setRotation(Rotatingbase2, x, 0, y);
		setRotation(Laserhead, x, 0, y);
		setRotation(Piston1attach, x, 0, y);
		
		setRotation(Piston2attach, x, 0, y);
		setRotation(Piston3attach, x, 0, y);
		setRotation(Piston4attach, x, 0, y);
		
		//PISTON 1
		float angle = getPistonAngle(Piston1attach, Piston1baseattach, new Vector3f(0, 0, -1), "ZY");
		float angleX = getPistonAngle(Piston1attach, Piston1baseattach, new Vector3f(-1, 0, 0), "XY");
		setRotation(Piston1, angle, 0, 0);
		setRotation(Piston1base, angle, 0, 0);
		
		double lenght = getPistonLenght(Piston1attach, Piston1baseattach, Piston1, "ZY");
		Piston1.addBox(2.05F, -0.2F, -4.61F + 2.3f*2f - (float)lenght, 0.4f, 0.4f, (float)lenght /*2.3f*/);
		
		//PISTON 2
		angle = getPistonAngle(Piston2attach, Piston2baseattach, new Vector3f(-1, 0, 0), "XY");
		setRotation(Piston2, 0, 0, -angle);
		setRotation(Piston2base, 0, 0, -angle);
		
		lenght = getPistonLenght(Piston2attach, Piston2baseattach, Piston2, "XY");
		Piston2.addBox(-4.65F + 2.3f*2f - (float)lenght, -0.2F, -0.1500001F, (float)lenght, 0.4f, 0.4f);

		//PISTON 3
		angle = getPistonAngle(Piston3attach, Piston3baseattach, new Vector3f(1, 0, 0), "XY");
		setRotation(Piston3, 0, 0, angle);
		setRotation(Piston3base, 0, 0, angle);

		lenght = getPistonLenght(Piston3attach, Piston3baseattach, Piston3, "XY");
        Piston3.addBox(0.0F, -0.2F, -0.25F, (float)lenght, 0.4f, 0.4f);
//		Piston2.addBox(-4.65F + 2.3f*2f - (float)lenght, -0.2F, -0.1500001F, (float)lenght, 0.4f, 0.4f);
		
		//PISTON 4
		angle = getPistonAngle(Piston4attach, Piston4baseattach, new Vector3f(0, 0, 1), "ZY");
		setRotation(Piston4, -angle, 0, 0);
		setRotation(Piston4base, -angle, 0, 0);
		
		lenght = getPistonLenght(Piston4attach, Piston4baseattach, Piston4, "ZY");
        Piston4.addBox(-0.1500003F, -0.2F, 0.0F, 0.4f, 0.4f, (float)lenght);
	        
	}
	
	public float getPistonAngle(ModelRenderer attach, ModelRenderer baseAttach, Vector3f direction, String interperter) {
		Vector3f pos = MathUtils.getPosWithRotation(attach);
		Vector3f pos2 =MathUtils.getPosWithRotation(baseAttach);
		
		//PISTON 1
		Vector3f dir = new Vector3f(pos.x() - pos2.x(), 
				pos.y() - pos2.y(), 
				pos.z() - pos2.z());
		Vector2f vec = MathUtils.getVec2f(dir, interperter);
		Vector2f vec2 = MathUtils.getVec2f(direction, interperter);
		vec = MathUtils.normalize(vec);
		return (float) MathUtils.getAngle(vec, vec2);
	}
	
	public double getPistonLenght(ModelRenderer attach, ModelRenderer baseAttach, ModelRenderer piston, String interperter) {
		Vector3f pos = MathUtils.getPosWithRotation(attach);
		Vector3f pos2 =MathUtils.getPosWithRotation(baseAttach);
		Vector3f dir = new Vector3f(pos.x() - pos2.x(), 
				pos.y() - pos2.y(), 
				pos.z() - pos2.z());
		return MathUtils.getLenght(MathUtils.getVec2f(dir, interperter));
	}
	
	@Override
	public void renderToBuffer(MatrixStack p_225598_1_, IVertexBuilder p_225598_2_, int p_225598_3_, int p_225598_4_,
			float p_225598_5_, float p_225598_6_, float p_225598_7_, float p_225598_8_) {
	}
	
	public void renderToBuffer(MatrixStack matrix, IRenderTypeBuffer buffer, int combinedLight, int combinedLightOverlay,
			float red, float green, float blue, float alpha) {

		ResourceLocation GRAY = new ResourceLocation("minecraft", "textures/block/gray_concrete.png");
		ResourceLocation LIGHT_GRAY = new ResourceLocation("minecraft", "textures/block/light_gray_concrete.png");
		ResourceLocation REDSTONE = new ResourceLocation(Reference.MODID, "textures/blocks/redstone_block_bw.png");
		
		Rotatingbase.render(matrix, buffer.getBuffer(RenderType.entityTranslucent(GRAY)), combinedLight, combinedLightOverlay, 1.0f, 1.0f, 1.0f, 1.0f);
	    Piston1base.render(matrix, buffer.getBuffer(RenderType.entityTranslucent(GRAY)), combinedLight, combinedLightOverlay, 1.0f, 1.0f, 1.0f, 1.0f);
	    Piston1baseattach.render(matrix, buffer.getBuffer(RenderType.entityTranslucent(GRAY)), combinedLight, combinedLightOverlay, 1.0f, 1.0f, 1.0f, 1.0f);
	    Piston1.render(matrix, buffer.getBuffer(RenderType.entityTranslucent(LIGHT_GRAY)), combinedLight, combinedLightOverlay, 1.0f, 1.0f, 1.0f, 1.0f);
	    Piston1attach.render(matrix, buffer.getBuffer(RenderType.entityTranslucent(LIGHT_GRAY)), combinedLight, combinedLightOverlay, 1.0f, 1.0f, 1.0f, 1.0f);
	    Piston2attach.render(matrix, buffer.getBuffer(RenderType.entityTranslucent(LIGHT_GRAY)), combinedLight, combinedLightOverlay, 1.0f, 1.0f, 1.0f, 1.0f);
	    Piston2baseattach.render(matrix, buffer.getBuffer(RenderType.entityTranslucent(GRAY)), combinedLight, combinedLightOverlay, 1.0f, 1.0f, 1.0f, 1.0f);
	    Piston2base.render(matrix, buffer.getBuffer(RenderType.entityTranslucent(GRAY)), combinedLight, combinedLightOverlay, 1.0f, 1.0f, 1.0f, 1.0f);
	    Piston2.render(matrix, buffer.getBuffer(RenderType.entityTranslucent(LIGHT_GRAY)), combinedLight, combinedLightOverlay, 1.0f, 1.0f, 1.0f, 1.0f);
	    Piston3attach.render(matrix, buffer.getBuffer(RenderType.entityTranslucent(LIGHT_GRAY)), combinedLight, combinedLightOverlay, 1.0f, 1.0f, 1.0f, 1.0f);
	    Piston3.render(matrix, buffer.getBuffer(RenderType.entityTranslucent(LIGHT_GRAY)), combinedLight, combinedLightOverlay, 1.0f, 1.0f, 1.0f, 1.0f);
	    Piston3base.render(matrix, buffer.getBuffer(RenderType.entityTranslucent(GRAY)), combinedLight, combinedLightOverlay, 1.0f, 1.0f, 1.0f, 1.0f);
	    Piston3baseattach.render(matrix, buffer.getBuffer(RenderType.entityTranslucent(GRAY)), combinedLight, combinedLightOverlay, 1.0f, 1.0f, 1.0f, 1.0f);
	    Piston4baseattach.render(matrix, buffer.getBuffer(RenderType.entityTranslucent(GRAY)), combinedLight, combinedLightOverlay, 1.0f, 1.0f, 1.0f, 1.0f);
	    Piston4attach.render(matrix, buffer.getBuffer(RenderType.entityTranslucent(LIGHT_GRAY)), combinedLight, combinedLightOverlay, 1.0f, 1.0f, 1.0f, 1.0f);
	    Piston4base.render(matrix, buffer.getBuffer(RenderType.entityTranslucent(GRAY)), combinedLight, combinedLightOverlay, 1.0f, 1.0f, 1.0f, 1.0f);
	    Piston4.render(matrix, buffer.getBuffer(RenderType.entityTranslucent(LIGHT_GRAY)), combinedLight, combinedLightOverlay, 1.0f, 1.0f, 1.0f, 1.0f);
	    Rotatingbase2.render(matrix, buffer.getBuffer(RenderType.entityTranslucent(GRAY)), combinedLight, combinedLightOverlay, 1.0f, 1.0f, 1.0f, 1.0f);
	    Laserhead.render(matrix, buffer.getBuffer(RenderType.entityTranslucent(REDSTONE)), combinedLight, combinedLightOverlay, red, green, blue, alpha);
	}
 
}