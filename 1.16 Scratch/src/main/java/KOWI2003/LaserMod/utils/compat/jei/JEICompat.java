package KOWI2003.LaserMod.utils.compat.jei;

import KOWI2003.LaserMod.Reference;
import KOWI2003.LaserMod.gui.GuiInfuser;
import KOWI2003.LaserMod.gui.GuiLaser;
import KOWI2003.LaserMod.gui.GuiLaserProjector;
import KOWI2003.LaserMod.gui.GuiModStation;
import KOWI2003.LaserMod.gui.GuiPrecisionAssembler;
import KOWI2003.LaserMod.init.ModBlocks;
import KOWI2003.LaserMod.recipes.precisionAssembler.PrecisionAssemblerRecipeHandler;
import KOWI2003.LaserMod.utils.compat.jei.infuser.InfuserRecipeCategory;
import KOWI2003.LaserMod.utils.compat.jei.infuser.InfuserRecipeMaker;
import KOWI2003.LaserMod.utils.compat.jei.precisionAssembler.PrecisionRecipeCategory;
import KOWI2003.LaserMod.utils.compat.jei.slotmovers.LaserGuiSlotMover;
import KOWI2003.LaserMod.utils.compat.jei.slotmovers.LaserProjectorSlotMover;
import KOWI2003.LaserMod.utils.compat.jei.slotmovers.ModStationGuiSlotMover;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import mezz.jei.api.runtime.IIngredientManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

@JeiPlugin
public class JEICompat implements IModPlugin {

	@Override
	public ResourceLocation getPluginUid() {
		return new ResourceLocation(Reference.MODID);
	}
	
	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {
		final IJeiHelpers helpers = registry.getJeiHelpers();
		final IGuiHelper gui = helpers.getGuiHelper();
		
		registry.addRecipeCategories(new InfuserRecipeCategory(gui), new PrecisionRecipeCategory(gui));
		
		IModPlugin.super.registerCategories(registry);
	}
	
	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		registration.addRecipeCatalyst(new ItemStack(ModBlocks.Infuser), RecipeCategories.INFUSER);
		registration.addRecipeCatalyst(new ItemStack(ModBlocks.PrecisionAssembler), RecipeCategories.PRECISION_ASSEMBLER);
		
		IModPlugin.super.registerRecipeCatalysts(registration);
	}
	
	@Override
	public void registerRecipes(IRecipeRegistration registry) {
		final IIngredientManager ingredientRegistry = registry.getIngredientManager();
		final IJeiHelpers helpers = registry.getJeiHelpers();
		
		registry.addRecipes(InfuserRecipeMaker.getRecipes(helpers), RecipeCategories.INFUSER);
		registry.addRecipes(PrecisionAssemblerRecipeHandler.getAllRecipes(), RecipeCategories.PRECISION_ASSEMBLER);
		
		IModPlugin.super.registerRecipes(registry);
	}
	
	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration registry) {
		
		registry.addGuiContainerHandler(GuiLaser.class, new LaserGuiSlotMover());
		registry.addGuiContainerHandler(GuiLaserProjector.class, new LaserProjectorSlotMover());
		registry.addGuiContainerHandler(GuiModStation.class, new ModStationGuiSlotMover());
		
		registry.addRecipeClickArea(GuiInfuser.class, 64, 60, 45, 9, RecipeCategories.INFUSER);
		registry.addRecipeClickArea(GuiPrecisionAssembler.class, 76, 35, 45, 9, RecipeCategories.PRECISION_ASSEMBLER);
		
		IModPlugin.super.registerGuiHandlers(registry);
	}
	
	@Override
	public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
		
		IModPlugin.super.registerRecipeTransferHandlers(registration);
	}

}
