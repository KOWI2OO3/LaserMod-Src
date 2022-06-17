package KOWI2003.LaserMod.items.render.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import KOWI2003.LaserMod.utils.LaserItemUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class ArmorModel<T extends LivingEntity> extends HumanoidModel<T> {

	public ModelPart Chest;
	public ModelPart Head;
	
	public ItemStack stack;
	
	public ArmorModel(ModelPart root) {
		super(root);
		init(root);
	}
	
	public void init(ModelPart root)
	{
		this.Chest = root.getChild("Chest");
		this.Head = root.getChild("Head");
	}
	
	public ArmorModel(ItemStack armorStack) {
		super(HumanoidModel.createMesh(CubeDeformation.NONE, 0).getRoot().bake(16, 16));
		init(createBodyLayer().bakeRoot());
		stack = armorStack;
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Chest = partdefinition.addOrReplaceChild("Chest", CubeListBuilder.create()
				.addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F)
				, PartPose.offset(0, 0, 0));
		PartDefinition Head = partdefinition.addOrReplaceChild("Head", CubeListBuilder.create()
				.addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F)
				, PartPose.offset(0, 0, 0));
		
		return LayerDefinition.create(meshdefinition, 16, 16);
	}
	
	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
//		super.renderToBuffer(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		
		float[] color = LaserItemUtils.getColor(stack);
		red = color.length > 0 ? color[0] : 1.0f;
		green = color.length > 1 ? color[1] : 1.0f;
		blue = color.length > 2 ? color[2] : 1.0f;
		alpha = color.length > 3 ? color[3] : 1.0f;
		
//		RenderUtils.bindTexture();
		var texture = new ResourceLocation("textures/block/stone.png");
		VertexConsumer buffer = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(renderType(texture));
		Head.loadPose(head.storePose());
		Chest.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		Head.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		
	}
}
