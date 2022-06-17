package KOWI2003.LaserMod.utils.compat.jei.infuser;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;

import KOWI2003.LaserMod.init.ModBlocks;
import KOWI2003.LaserMod.recipes.infuser.IInfuserRecipe;
import KOWI2003.LaserMod.utils.compat.jei.RecipeCategories;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

public class InfuserRecipeCategory extends AbstractInfuserRecipeCategory<IInfuserRecipe> {

	private final IDrawable background;
	private final IDrawable icon;
	
	public InfuserRecipeCategory(IGuiHelper helper) {
		super(helper);
		background = helper.createDrawable(TEXTURE, 4, 11, 148, 62);
		icon = helper.createDrawableIngredient(new ItemStack(ModBlocks.Infuser));
	}
	
	@Override
	public void draw(IInfuserRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
		animatedPorgress.draw(matrixStack, 60, 49);
		super.draw(recipe, matrixStack, mouseX, mouseY);
	}
	
	@Override
	public ResourceLocation getUid() {
		return RecipeCategories.INFUSER;
	}
	
	@Override
	public Class<? extends IInfuserRecipe> getRecipeClass() {
		return IInfuserRecipe.class;
	}
	
	@Override
	public String getTitle() {
		return new TranslationTextComponent("container.lasermod.infuser").getString();
	}

	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public IDrawable getIcon() {
		return icon;
	}

	@Override
	public void setIngredients(IInfuserRecipe recipe, IIngredients ingredients) {
		List<Ingredient> stacks = new ArrayList<>();
		for (Object obj: recipe.getInputs()) {
			if(obj instanceof ItemStack)
				stacks.add(Ingredient.of((ItemStack)obj));
			else if(obj instanceof ITag)
				stacks.add(Ingredient.of((ITag<Item>)obj));
			else 
				stacks.add(Ingredient.EMPTY);
		}
		ingredients.setInputIngredients(stacks);
		ingredients.setOutput(VanillaTypes.ITEM, recipe.getOutput());
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, IInfuserRecipe recipe, IIngredients ingredients) {
		IGuiItemStackGroup stacks = recipeLayout.getItemStacks();
		stacks.init(input1, true, 8, 0);
		stacks.init(input2, true, 36, 44);
		stacks.init(output, false, 111, 44);
		stacks.set(ingredients);
	}

}
