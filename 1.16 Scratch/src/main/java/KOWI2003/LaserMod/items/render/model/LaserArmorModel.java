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
	
	private final ModelRenderer ChestBase;
	private final ModelRenderer ChestRightArm;
	private final ModelRenderer ChestLeftArm;
	private final ModelRenderer ChestGem;
	public final ModelRenderer ChestActive;
	private  final ModelRenderer ChestActiveRightArm;
	private final ModelRenderer ChestActiveLeftArm;

	public boolean HelmetVisible = false;
	public boolean ChestplateVisible = false;
	
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
		
		
		//Start Chestplate
		ChestBase = new ModelRenderer(this);
		ChestBase.setPos(0.0F, 22.525F, 0.225F);
		
		ModelRenderer Fillin = new ModelRenderer(this);
		ChestBase.addChild(Fillin);
		Fillin.texOffs(0, 0).addBox(-0.75F, -0.025F, -2.75F, 1.0F, 5.05F, 0.575F, 0.0f);
		Fillin.texOffs(0, 0).addBox(-0.75F, 0, 1.55F, 1.0F, 4.05F, 0.575F, 0.0f);
		Fillin.texOffs(40, 40).addBox(-1.5F, 0, -2.5F, 2.5F, 4.275F, 0.3F, 0.0f);
		Fillin.texOffs(40, 40).addBox(2.25F, .025f, 1.75F, 1.1F, 2.6F, 0.3F, 0.0f);
		Fillin.texOffs(40, 40).addBox(-3.85F, .025f, 1.75F, 1.1F, 2.475F, 0.3F, 0.0f);
		Fillin.texOffs(40, 40).addBox(1.0F, .025f, 1.75F, 1.25F, 3.0F, 0.3F, 0.0f);
		Fillin.texOffs(40, 40).addBox(-2.75F, .025f, 1.75F, 1.25F, 3.0F, 0.3F, 0.0f);
		Fillin.texOffs(40, 40).addBox(-1.5F, .025f, 1.75F, 2.5F, 3.275F, 0.3F, 0.0f);
		Fillin.texOffs(40, 40).addBox(-2.75F, 0, -2.5F, 1.25F, 4.0F, 0.3F, 0.0f);
		Fillin.texOffs(40, 40).addBox(1.0F, 0, -2.5F, 1.25F, 4.0F, 0.3F, 0.0f);
		Fillin.texOffs(40, 40).addBox(-3.85F, 0, -2.5F, 1.1F, 3.475F, 0.3F, 0.0f);
		Fillin.texOffs(40, 40).addBox(2.25F, 0, -2.5F, 1.1F, 3.6F, 0.3F, 0.0f);
		Fillin.setPos(0.25F, 0.0F, 0.0F);
		
		ModelRenderer Outline = new ModelRenderer(this);
		Outline.setPos(0.0F, -21.25F, 0.0F);
		Outline.texOffs(0, 0).addBox(3.275F, 21.225F, -2.675F, 0.725F, 3.775F, 0.5F, 0.0F);
		Outline.texOffs(0, 0).addBox(-4.0F, 21.235f, -2.675F, 0.725F, 3.75F, 0.5F, 0.0F);
		ChestBase.addChild(Outline);
		
		ModelRenderer cube_r4 = new ModelRenderer(this);
		Outline.addChild(cube_r4);
		cube_r4.texOffs(0, 0).addBox(4.15F, -2.525F, -2.7F, 0.75F, 3.725F, 0.525F, 0.0F);
		cube_r4.setPos(-1.2F, -1.725F+22.525F, 0.0F);
		setRotationAngle(cube_r4, 0.0F, 0.0F, 1.9199F);
		
		ModelRenderer cube_r5 = new ModelRenderer(this);
		Outline.addChild(cube_r5);
		cube_r5.texOffs(0, 0).addBox(4.425F, -2.375F, -2.7F, 0.75F, 3.725F, 0.525F, 0.0F);
		cube_r5.setPos(0.0F, -1.575F+22.525F, 0.0F);
		setRotationAngle(cube_r5, 0.0F, 0.0F, 1.2217F);
		
		ModelRenderer Outline2 = new ModelRenderer(this);
		Outline.addChild(Outline2);
		Outline2.texOffs(0, 0).addBox(3.275F, -1.275F+22.525F, 1.6F, 0.725F, 2.775F, 0.5F, 0.0F);
		Outline2.texOffs(0, 0).addBox(-4.0F, -1.265F+22.525F, 1.6F, 0.725F, 2.75F, 0.5F, 0.0F);
		Outline2.setPos(0.0F, 0.0F, 0.0F);
		
		ModelRenderer cube_r6 = new ModelRenderer(this);
		Outline2.addChild(cube_r6);
		cube_r6.texOffs(0, 0).addBox(4.15F, -2.525F, -2.7F, 0.75F, 3.725F, 0.525F, 0.0F);
		cube_r6.setPos(-1.2F, -2.7F+22.525F, 4.275F);
		setRotationAngle(cube_r6, 0.0F, 0.0F, 1.9199F);
		
		ModelRenderer cube_r7 = new ModelRenderer(this);
		Outline2.addChild(cube_r7);
		cube_r7.texOffs(0, 0).addBox(4.425F, -2.375F, -2.7F, 0.75F, 3.725F, 0.525F, 0.0F);
		cube_r7.setPos(0.0F, -2.55F+22.525F, 4.275F);
		setRotationAngle(cube_r7, 0.0F, 0.0F, 1.2217F);
		
		ModelRenderer Support = new ModelRenderer(this);
		Outline.addChild(Support);
		Support.texOffs(0, 0).addBox(3.275F, -1.775F+22.525F, -2.675F, 0.725F, 0.6F, 4.775F, 0.0F);
		Support.texOffs(0, 0).addBox(-4.0F, -1.775F+22.525F, -2.675F, 0.725F, 0.6F, 4.775F, 0.0F);
		Support.texOffs(40, 40).addBox(-3.35F, -1.5F+22.525F, -2.5F, 0.85F, 0.225F, 4.55F, 0.0F);
		Support.texOffs(40, 40).addBox(2.4F, -1.5F+22.525F, -2.5F, 0.85F, 0.225F, 4.55F, 0.0F);
		Support.setPos(0.0F, 0.0F, 0.0F);
		
		ModelRenderer Details = new ModelRenderer(this);
		ChestBase.addChild(Details);
		Details.texOffs(0, 0).addBox(-0.75F, -20.975F+22.525F, -3.0F, 1.0F, 1.0F, 0.575F, 0.0f);
		Details.texOffs(0, 0).addBox(-3.25F, -20.225F+22.525F, -2.675F, 1.0F, 0.25F, 0.25F, 0.0f);
		Details.texOffs(0, 0).addBox(-3.25F, -21.725F+22.525F, -2.675F, 1.0F, 0.25F, 0.25F, 0.0f);
		Details.texOffs(0, 0).addBox(-3.2F, -21.475F+22.525F, -2.625F, 0.125F, 1.25F, 0.2F, 0.0f);
		Details.texOffs(0, 0).addBox(-2.45F, -21.475F+22.525F, -2.625F, 0.125F, 1.25F, 0.2F, 0.0f);
		Details.texOffs(9, 53).addBox(-1.5F, -19.975F+22.525F, -2.625F, 0.75F, 0.25F, 0.2F, 0.0f);
		Details.texOffs(9, 53).addBox(0.015F, -19.47F+22.525F, -2.625F, 0.75F, 0.25F, 0.2F, 0.0f);
		Details.texOffs(9, 53).addBox(-1.325F, -21.3F+22.525F, -2.625F, 0.575F, 0.25F, 0.2F, 0.0f);
		Details.texOffs(9, 53).addBox(0.175F, -21.3F+22.525F, -2.625F, 1.225F, 0.25F, 0.2F, 0.0f);
		Details.texOffs(9, 53).addBox(1.675F, -20.3F+22.525F, -2.625F, 0.825F, 0.25F, 0.2F, 0.0f);
		Details.texOffs(9, 53).addBox(-2.575F, -22.175F+22.525F, -2.625F, 1.5F, 0.25F, 0.2F, 0.0f);
		Details.texOffs(9, 53).addBox(1.825F, -22.425F+22.525F, -2.625F, 1.175F, 0.25F, 0.2F, 0.0f);
		
		Details.texOffs(9, 53).addBox(-3.5F, -22.45F+22.525F, -2.625F, 0.75F, 0.25F, 0.2F, 0.0f);
		Details.texOffs(9, 53).addBox(-1.875F, -20.725F+22.525F, -2.625F, 0.375F, 0.25F, 0.2F, 0.0f);
		Details.texOffs(9, 53).addBox(-1.75F, -20.475F+22.525F, -2.625F, 0.25F, 0.75F, 0.2F, 0.0f);
		Details.texOffs(9, 53).addBox(-2.775F, -19.975F+22.525F, -2.625F, 0.25F, 0.725F, 0.2F, 0.0f);
		
		Details.texOffs(9, 53).addBox(2.25F, -20.05F+22.525F, -2.625F, 0.25F, 1.1F, 0.2F, 0.0f);
		Details.texOffs(9, 53).addBox(-1.325F, -21.925F+22.525F, -2.625F, 0.25F, 0.625F, 0.2F, 0.0f);
		Details.texOffs(9, 53).addBox(1.15F, -21.05F+22.525F, -2.625F, 0.25F, 0.525F, 0.2F, 0.0f);
		Details.texOffs(9, 53).addBox(1.575F, -22.425F+22.525F, -2.625F, 0.25F, 1.9F, 0.2F, 0.0f);
		Details.texOffs(9, 53).addBox(-2.575F, -21.925F+22.525F, -2.625F, 0.25F, 0.25F, 0.2F, 0.0f);
		Details.texOffs(9, 53).addBox(-3.0F, -22.2F+22.525F, -2.625F, 0.25F, 0.475F, 0.2F, 0.0f);
		Details.texOffs(52, 49).addBox(-0.65F, -20.875F+22.525F, -3.1F, 0.8F, 0.8F, 0.575F, 0.0f);
		Details.texOffs(21, 25).addBox(1.1F, -20.525F+22.525F, -2.8F, 0.8F, 0.8F, 0.275F, 0.0f);
		Details.texOffs(52, 49).addBox(-3.15F, -21.5F+22.525F, -2.55F, 0.8F, 1.425F, 0.025F, 0.0f);;
		Details.setPos(0.25F, 0.0F, 0.0F);
		
		ModelRenderer cube_r8 = new ModelRenderer(this);
		Details.addChild(cube_r8);
		cube_r8.texOffs(9, 53).addBox(-2.025F, -21.625F, -2.6F, 0.775F, 0.25F, 0.2F, 0.0f);
		cube_r8.setPos(0.0F, 0.9F+22.525F, -0.25f);
		setRotationAngle(cube_r8, 0.0F, 0.1309F, 0.0F);
		
		ModelRenderer cube_r9 = new ModelRenderer(this);
		Details.addChild(cube_r9);
		cube_r9.texOffs(9, 53).addBox(-17.225F, -11.075F, -2.375F, 0.925F, 0.25F, 0.2F, 0.0f);
		cube_r9.setPos(0.0F, 0.9F+22.525F, -0.25F);
		setRotationAngle(cube_r9, 0.0F, 0.0F, 0.8727F);
		
		ModelRenderer cube_r10 = new ModelRenderer(this);
		Details.addChild(cube_r10);
		cube_r10.texOffs(9, 53).addBox(13.625F, -15.375F, -2.375F, 0.475F, 0.25F, 0.2F, 0.0F);
		cube_r10.setPos(0.05F, 1.125F+22.525F, -0.25F);
		setRotationAngle(cube_r10, 0.0F, 0.0F, -0.6981F);
		
		ModelRenderer cube_r11 = new ModelRenderer(this);
		Details.addChild(cube_r11);
		cube_r11.texOffs(0, 0).addBox(-0.75F, -21.525F, 1.5F, 1.0F, 1.525F, 0.45F, 0.0f);
		cube_r11.setPos(0.0F, 1.475F+22.525F, -0.725F);
		setRotationAngle(cube_r11, 0.1745F, 0.0F, 0.0F);
		
		ModelRenderer cube_r12 = new ModelRenderer(this);
		Details.addChild(cube_r12);
		cube_r12.texOffs(0, 0).addBox(-0.75F, -20.775F, -7.75F, 1.0F, 1.475F, 0.525F, 0.0f);
		cube_r12.setPos(0.0F, -0.675F+22.525F, 1.3F);
		setRotationAngle(cube_r12, -0.1745F, 0.0F, 0.0F);
		
		ChestRightArm = new ModelRenderer(this);
		ChestRightArm.setPos(0.0F, 24.0F, 0.0F);
		
		ModelRenderer Outline3 = new ModelRenderer(this);
		ChestRightArm.addChild(Outline3);
		Outline3.texOffs(0, 0).addBox(-0.65F+5F, 3.81F+22.24F, 0.875F, 3.9F, 0.4F, 0.5F, 0.0f);
		Outline3.texOffs(0, 0).addBox(-1.215F+5F, 3.81F+22.24F, 2.25F, 0.54F, 0.4F, 1.8F, 0.0f);
		Outline3.texOffs(0, 0).addBox(-0.165F+5F, 3.735F+22.24F, 2.7F, 3.415F, 0.475F, 0.8F, 0.0f);
		Outline3.texOffs(0, 0).addBox(-0.65F+5F, 3.81F+22.24F, 4.925F, 3.9F, 0.4F, 0.5F, 0.0f);
		Outline3.setPos(-7.275F, -28.225F, -3.15F);
		
		ModelRenderer cube_r13 = new ModelRenderer(this);
		Outline3.addChild(cube_r13);
		cube_r13.texOffs(0, 0).addBox(-4.775F, 3.835F, -2.675F, 1.485F, 0.375F, 0.5F, 0.0f);
		cube_r13.setPos(5F, 22.24F, 0.0F);
		setRotationAngle(cube_r13, 0.0F, 1.9635F, 0.0F);
		
		ModelRenderer cube_r14 = new ModelRenderer(this);
		Outline3.addChild(cube_r14);
		cube_r14.texOffs(0, 0).addBox(-4.76F, 3.835F, -2.675F, 1.5F, 0.375F, 0.5F, 0.0f);
		cube_r14.setPos(3.075F+5F, 22.24F, -1.125F);
		setRotationAngle(cube_r14, 0.0F, 1.1781F, 0.0F);
		
		ModelRenderer Fill = new ModelRenderer(this);
		ChestRightArm.addChild(Fill);
		Fill.texOffs(40, 40).addBox(-8.0F+5F, -24.29F+22.24F, -1.775F, 3.975F, 0.3F, 3.55F, 0.0f);
		Fill.setPos(0.0f, 0.0f, 0.0f);
		
		ModelRenderer Detail = new ModelRenderer(this);
		ChestRightArm.addChild(Detail);
		Detail.texOffs(9, 53).addBox(-5.075F+5F, -24.35F+22.24F, -1.4F, 1.05F, 0.15F, 0.2F, 0.0f);
		Detail.texOffs(9, 53).addBox(-6.275F+5F, -24.35F+22.24F, -1.4F, 0.2F, 0.15F, 0.975F, 0.0f);
		Detail.texOffs(9, 53).addBox(-6.075F+5F, -24.35F+22.24F, -1.4F, 0.8F, 0.15F, 0.2F, 0.0f);
		Detail.texOffs(9, 53).addBox(-5.075F+5F, -24.35F+22.24F, 1.125F, 1.05F, 0.15F, 0.2F, 0.0f);
		Detail.texOffs(9, 53).addBox(-6.075F+5F, -24.35F+22.24F, 1.125F, 0.8F, 0.15F, 0.2F, 0.0f);
		Detail.texOffs(9, 53).addBox(-5.275F+5F, -24.35F+22.24F, -1.4F, 0.2F, 0.15F, 0.975F, 0.0f);
		Detail.texOffs(9, 53).addBox(-5.275F+5F, -24.35F+22.24F, 0.35F, 0.2F, 0.15F, 0.975F, 0.0f);
		Detail.texOffs(9, 53).addBox(-6.275F+5F, -24.35F+22.24F, 0.35F, 0.2F, 0.15F, 0.975F, 0.0f);
		Detail.setPos(0.0f, 0.0f, 0.0f);
		
		ChestLeftArm = new ModelRenderer(this);
		ChestLeftArm.setPos(0.0F, 24.0F, 0.0F);
		setRotationAngle(ChestLeftArm,  0.0F, 3.1416F, 0.0F);
		
		ModelRenderer Outline5 = new ModelRenderer(this);
		ChestLeftArm.addChild(Outline5);
		Outline5.texOffs(0, 0).addBox(3.28F-5F, 20.31F+22.24F, 13.175F, 3.9F, 0.4F, 0.5F, 0.0f);
		Outline5.texOffs(0, 0).addBox(7.205F-5F, 20.31F+22.24F, 10.5F, 0.54F, 0.4F, 1.8F, 0.0f);
		Outline5.texOffs(0, 0).addBox(3.28F-5F, 20.235F+22.24F, 11.05F, 3.415F, 0.475F, 0.8F, 0.0f);
		Outline5.texOffs(0, 0).addBox(3.28F-5F, 20.31F+22.24F, 9.125F, 3.9F, 0.4F, 0.5F, 0.0f);
		Outline5.setPos(0.75F, -44.725F, -11.4F);
		
		ModelRenderer cube_r15 = new ModelRenderer(this);
		Outline5.addChild(cube_r15);
		cube_r15.texOffs(0, 0).addBox(-3.906F, 16.3175F, 5.0454F, 0.5F, 0.375F, 1.485F, 0.0f);
		cube_r15.setPos(8.39F-5F, 4.0175F+22.24F, 3.15F);
		setRotationAngle(cube_r15, 0.0F, 0.3927F, 0.0F);
		
		ModelRenderer cube_r16 = new ModelRenderer(this);
		Outline5.addChild(cube_r16);
		cube_r16.texOffs(0, 0).addBox(2.4088F, 16.3175F, 8.7001F, 0.5F, 0.375F, 1.5F, 0.0f);
		cube_r16.setPos(8.39F-5F, 4.0175F+22.24F, 3.15F);
		setRotationAngle(cube_r16, 0.0F, -0.3927F, 0.0F);
		
		ModelRenderer Fill3 = new ModelRenderer(this);
		ChestLeftArm.addChild(Fill3);
		Fill3.texOffs(40, 40).addBox(-3.995F-5F, -7.79F+22.24F, 6.475F, 3.975F, 0.3F, 3.55F, 0.0f);
		Fill3.setPos(8.025F, -16.5F, -8.25F);
		
		ModelRenderer Detail3 = new ModelRenderer(this);
		ChestLeftArm.addChild(Detail3);
		Detail3.texOffs(9, 53).addBox(-3.995F-5F, -7.85F+22.24F, 9.45F, 1.05F, 0.15F, 0.2F, 0.0f);
		Detail3.texOffs(9, 53).addBox(-1.945F-5F, -7.85F+22.24F, 8.675F, 0.2F, 0.15F, 0.975F, 0.0f);
		Detail3.texOffs(9, 53).addBox(-2.745F-5F, -7.85F+22.24F, 9.45F, 0.8F, 0.15F, 0.2F, 0.0f);
		Detail3.texOffs(9, 53).addBox(-3.995F-5F, -7.85F+22.24F, 6.925F, 1.05F, 0.15F, 0.2F, 0.0f);
		Detail3.texOffs(9, 53).addBox(-2.745F-5F, -7.85F+22.24F, 6.925F, 0.8F, 0.15F, 0.2F, 0.0f);
		Detail3.texOffs(9, 53).addBox(-2.945F-5F, -7.85F+22.24F, 8.675F, 0.2F, 0.15F, 0.975F, 0.0f);
		Detail3.texOffs(9, 53).addBox(-2.945F-5F, -7.85F+22.24F, 6.925F, 0.2F, 0.15F, 0.975F, 0.0f);
		Detail3.texOffs(9, 53).addBox(-1.945F-5F, -7.85F+22.24F, 6.925F, 0.2F, 0.15F, 0.975F, 0.0f);
		Detail3.setPos(8.025F, -16.5F, -8.25F);
		
		ChestGem = new ModelRenderer(this);
		ChestGem.texOffs(48, 19).addBox(-0.325F, -21.55F+ 23.275F, -3.15F, 0.65F, 0.65F, 0.525F, 0.0f);
		ChestGem.texOffs(48, 19).addBox(-2.82F, -22.25F+ 23.275F, -2.6f, 0.7F, 1.35F, 0.025F, 0.0f);
		ChestGem.setPos(0.0F, 23.275F, 0.0F);
		
		ChestActive = new ModelRenderer(this);
		ChestActive.texOffs(16, 16).addBox(-4, 0, -2, 8, 12, 4, 0.9f);
		ChestActive.setPos(0.0f, 0.0f, 0.0f);
		
		ChestActiveRightArm = new ModelRenderer(this);
		ChestActiveRightArm.texOffs(40, 16).addBox(4-7F, -2, -2, 4, 12, 4, 0.9f);
		ChestActiveRightArm.setPos(0.0f, 0.0f, 0.0f);
		
		ChestActiveLeftArm = new ModelRenderer(this);
		ChestActiveLeftArm.texOffs(40, 16).addBox(4-5F, -2, -2, 4, 12, 4, 0.9f);
		ChestActiveLeftArm.setPos(0.0f, 0.0f, 0.0f);
		
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
		if(ChestplateVisible) 
			renderChestplate(matrix, packedLight, packedOverlay, red, green, blue, alpha, cred, cgreen, cblue, calpha);
		
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
	
	public void renderChestplate(MatrixStack matrix, int packedLight, int packedOverlay, float red, float green, float blue, float alpha, float cred, float cgreen, float cblue, float calpha) {
		ResourceLocation texture = new ResourceLocation(Reference.MODID, "textures/models/armor/laser_armor.png");
		RenderType renderType = RenderType.entityTranslucent(texture);
		IVertexBuilder buffer = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(renderType);
		applyAnimation(ChestBase, body);
		applyAnimation(ChestGem, body);
		applyAnimation(ChestActive, body);
		
		applyAnimation(ChestRightArm, rightArm);
		applyAnimation(ChestActiveRightArm, rightArm);
		applyAnimation(ChestLeftArm, leftArm);
		applyAnimation(ChestActiveLeftArm, leftArm);
		
		ChestBase.render(matrix, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		ChestRightArm.render(matrix, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		ChestLeftArm.render(matrix, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		ChestGem.render(matrix, buffer, packedLight, packedOverlay, cred, cgreen, cblue, calpha);
		
		if(ChestActive.visible) {
			texture = new ResourceLocation(Reference.MODID, "textures/models/armor/laser_armor_overlay.png");
			renderType = RenderType.entityTranslucent(texture);
			buffer = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(renderType);

			ChestActive.render(matrix, buffer, packedLight, packedOverlay, cred, cgreen, cblue, .6f);
			ChestActiveRightArm.render(matrix, buffer, packedLight, packedOverlay, cred, cgreen, cblue, .6f);
			ChestActiveLeftArm.render(matrix, buffer, packedLight, packedOverlay, cred, cgreen, cblue, .6f);
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