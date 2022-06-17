package KOWI2003.LaserMod.gui.widgets;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import KOWI2003.LaserMod.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class RecipeButton extends Button {
	
	IRecipe<?> recipeID;
	protected final IRecipePressable onPress;
	
	public RecipeButton(int x, int y, int width, int height,
			ITextComponent title, IRecipePressable action) {
		super(x, y, width, height, title, null);
		this.onPress = action;
	}
	
	public RecipeButton(int x, int y, int width, int height,
			IRecipe<?> recipe, IRecipePressable action) {
		this(x, y, width, height, new StringTextComponent("Null"), action);
		setRecipe(recipe);
	}
	
	public void setRecipe(IRecipe<?> recipe) {
		recipeID = recipe;
		setMessage(recipe == null ? new StringTextComponent("Null") : recipe.getResultItem().getHoverName());
	}
	
	@Override
	public void renderButton(MatrixStack matrix, int x, int y, float partialTicks) {
		Minecraft minecraft = Minecraft.getInstance();
		FontRenderer fontrenderer = minecraft.font;
		minecraft.getTextureManager().bind(new ResourceLocation(Reference.MODID,
				"textures/gui/mod_widgets.png"));
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.alpha);
		int i = this.getYImage(this.isHovered());
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.enableDepthTest();
		int tempWidth = getWidth();
		int tempHeight = this.height;
		float scales = (float)getHeight()/20f;
		setWidth((int) (tempWidth * 1f/scales));
		
		matrix.pushPose();
		matrix.translate(this.x, this.y, 0);
		matrix.scale(scales, scales, scales);
		this.blit(matrix, 0, 0, 0, 46 + i * 20, this.width / 2, 20);
		matrix.popPose();
		matrix.pushPose();
		matrix.translate(this.x, this.y, 0);
		matrix.scale(scales, scales, scales);
		matrix.translate(this.width*scales, 0, 0);
		this.blit(matrix, 0, 0, 200 - this.width / 2, 46 + i * 20, this.width / 2, 20);
		matrix.popPose();
		this.renderBg(matrix, minecraft, x, y);
		int j = this.getFGColor();
		setWidth(tempWidth);
		if(recipeID == null)
			return;
		
		float scale = 0.55f;
		RenderSystem.pushMatrix();
		RenderSystem.translated(this.x + 2, this.y + (height-8*scale)/2 - 4*scale, 0);
		RenderSystem.scalef(scale, scale, scale);
		Minecraft.getInstance().getItemRenderer().renderGuiItem(recipeID.getResultItem(), 0, 0);
		RenderSystem.popMatrix();
		matrix.pushPose();
		matrix.translate(this.x + this.width / 2 - 8/scale, this.y + (this.height - 8 * scale) / 2, 0);
		matrix.scale(scale, scale, scale);
		String message = getMessage().getString();
		if(message.length() > 12)
			message = message.substring(0, 12) + "...";
		drawString(matrix, fontrenderer, new StringTextComponent(message), 0, 0, j);
		matrix.popPose();
		
		if (this.isHovered()) {
			this.renderToolTip(matrix, x, y);
		}
	}
	
	@Override
	public void onPress() {
		if(onPress != null)
			this.onPress.onPress(this, recipeID);
	}
	
	public static interface IRecipePressable {
		public void onPress(Button button, IRecipe<?> recipe);
	}
}
