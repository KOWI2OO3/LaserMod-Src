package KOWI2003.LaserMod.items.render.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import KOWI2003.LaserMod.LaserProperties;
import KOWI2003.LaserMod.Reference;
import KOWI2003.LaserMod.utils.LaserItemUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

// Made with Blockbench 4.2.5
// Exported for Minecraft version 1.15 - 1.16 with Mojang mappings
// Paste this class into your mod and generate all required imports


public class LaserArmorModel<T extends LivingEntity> extends BipedModel<T> {
	public final ModelRenderer HelmetBand;
	private final ModelRenderer Rotation;
	private final ModelRenderer Cube_r1;
	private final ModelRenderer Cube_r2;
	private final ModelRenderer HelmetGem;
	private final ModelRenderer Cube_r3;
	public final ModelRenderer HelmetActive;
	public final ModelRenderer HelmetActiveAirtight;

	public boolean HelmetVisible = false;
	
	ItemStack stack;
	
	public LaserArmorModel(ItemStack stack) {
		super(0.128f);
		this.stack = stack;
		texWidth = 64;
		texHeight = 64;

		HelmetBand = new ModelRenderer(this);
		HelmetBand.setPos(0.0F, 24.0F, 0.0F);
		HelmetBand.texOffs(0, 0).addBox(-4.25F, -7.0F, -4.24F, 8.5F, 1.0F, 0.7F, 0.0F, false);
		HelmetBand.texOffs(0, 0).addBox(-4.25F, -7.0F, -3.99F, 0.7F, 1.0F, 8.5F, 0.0F, false);
		HelmetBand.texOffs(0, 0).addBox(3.55F, -7.0F, -3.99F, 0.7F, 1.0F, 8.5F, 0.0F, false);
		HelmetBand.texOffs(0, 0).addBox(-4.25F, -7.0F, 3.86F, 8.5F, 1.0F, 0.7F, 0.0F, false);
		
		head.addChild(HelmetBand);
		
		Rotation = new ModelRenderer(this);
		Rotation.setPos(0.0F, 0.0F, 0.0F);
		HelmetBand.addChild(Rotation);
		setRotationAngle(Rotation, 0.0F, 1.5708F, 0.0F);
		Rotation.texOffs(0, 0).addBox(4.05F, -7.02F, -0.5F, 0.39F, 1.02F, 1.0F, 0.0F, false);

		Cube_r1 = new ModelRenderer(this);
		Cube_r1.setPos(-3.94F, -7.5F, 0.09F);
		Rotation.addChild(Cube_r1);
		setRotationAngle(Cube_r1, 0.0F, 0.3927F, 0.0F);
		Cube_r1.texOffs(0, 0).addBox(7.581F, 0.5F, 1.6457F, 0.4F, 1.0F, 1.0F, 0.0F, false);

		Cube_r2 = new ModelRenderer(this);
		Cube_r2.setPos(-2.75F, -7.5F, 0.14F);
		Rotation.addChild(Cube_r2);
		setRotationAngle(Cube_r2, 0.0F, -0.3927F, 0.0F);
		Cube_r2.texOffs(0, 0).addBox(6.431F, 0.5F, -2.4057F, 0.36F, 1.0F, 1.0F, 0.0F, false);

		HelmetGem = new ModelRenderer(this);
		HelmetGem.setPos(0.0F, 24.0F, 0.0F);

		head.addChild(HelmetGem);

		Cube_r3 = new ModelRenderer(this);
		Cube_r3.setPos(0.0F, 0.0F, 0.0F);
		HelmetGem.addChild(Cube_r3);
		setRotationAngle(Cube_r3, 0.0F, 1.5708F, 0.0F);
		Cube_r3.texOffs(40, 4).addBox(4.05F, -7.0F, -0.5F, 0.4F, 1.0F, 1.0F, 0.0F, false);

		HelmetActive = new ModelRenderer(this);
		HelmetActive.setPos(0.0F, 30.5F, 0.0F);
		HelmetActive.texOffs(0, 0).addBox(-4F, -8F, -4F, 8.0F, 8.0F, 8.0F, 0.9f, false);
		
		head.addChild(HelmetActive);
		
		HelmetActiveAirtight = new ModelRenderer(this);
		HelmetActiveAirtight.setPos(0.0F, 30.5F, 0.0F);
		HelmetActiveAirtight.texOffs(0, 0).addBox(-4F, -8F, -4F, 8.0F, 8.0F, 8.0F, 0.7f, false);

		head.addChild(HelmetActiveAirtight);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
	}

	@Override
	public void renderToBuffer(MatrixStack matrix, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		float[] color = LaserItemUtils.getColor(stack);
		float cred = color.length > 0 ? color[0] : 1.0f;
		float cgreen = color.length > 1 ? color[1] : 1.0f;
		float cblue = color.length > 2 ? color[2] : 1.0f;
		float calpha = color.length > 3 ? color[3] : 1.0f;
		
		if(HelmetVisible)
			renderHelmet(matrix, packedLight, packedOverlay, red, green, blue, alpha, cred, cgreen, cblue, calpha);
	}
	
	public void renderHelmet(MatrixStack matrix, int packedLight, int packedOverlay, float red, float green, float blue, float alpha, float cred, float cgreen, float cblue, float calpha) {
		ResourceLocation texture = new ResourceLocation(Reference.MODID, "textures/models/armor/laser_armor.png");
		RenderType renderType = RenderType.entityTranslucent(texture);
		IVertexBuilder buffer = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(renderType);
		applyAnimation(HelmetBand, head);
		applyAnimation(HelmetGem, head);
		applyAnimation(HelmetActive, head);
		
		HelmetBand.render(matrix, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		HelmetGem.render(matrix, buffer, packedLight, packedOverlay, cred, cgreen, cblue, calpha);
		
		texture = new ResourceLocation(Reference.MODID, "textures/models/armor/laser_armor_overlay.png");
		renderType = RenderType.entityTranslucent(texture);
		buffer = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(renderType);
		
		HelmetActive.render(matrix, buffer, packedLight, packedOverlay, cred, cgreen, cblue, .6f);

		HelmetActiveAirtight.visible = HelmetActive.visible;
		
		if(hasAirtightSeal()) {
			applyAnimation(HelmetActiveAirtight, head);
			texture = new ResourceLocation(Reference.MODID, "textures/models/armor/laser_armor_overlay_airtight.png");
			renderType = RenderType.entityTranslucent(texture);
			buffer = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(renderType);
			HelmetActiveAirtight.render(matrix, buffer, packedLight, packedOverlay, cred, cgreen, cblue, .4f);
		}
	}
	
	boolean hasAirtightSeal() {
		if(stack.getEquipmentSlot() != EquipmentSlotType.HEAD)
			return false;
		LaserProperties properties = LaserItemUtils.getProperties(stack);
		return properties.hasUpgarde("airtight_seal");
	}
	
	public void applyAnimation(ModelRenderer armorPart, ModelRenderer bodyPart)
	{
		armorPart.setPos(bodyPart.x, bodyPart.y, bodyPart.z);
		armorPart.xRot = bodyPart.xRot;
		armorPart.yRot = bodyPart.yRot;
		armorPart.zRot = bodyPart.zRot;
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}