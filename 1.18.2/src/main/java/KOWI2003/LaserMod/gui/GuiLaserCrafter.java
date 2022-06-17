package KOWI2003.LaserMod.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import KOWI2003.LaserMod.Reference;
import KOWI2003.LaserMod.container.ContainerLaserCrafter;
import KOWI2003.LaserMod.gui.widgets.ButtonList;
import KOWI2003.LaserMod.gui.widgets.RecipeButton;
import KOWI2003.LaserMod.network.PacketHandleRecipeChange;
import KOWI2003.LaserMod.network.PacketHandler;
import KOWI2003.LaserMod.tileentities.TileEntityLaserCrafter;
import KOWI2003.LaserMod.utils.RenderUtils;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.Recipe;

public class GuiLaserCrafter extends BetterAbstractContainerScreen<ContainerLaserCrafter> {

	public static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID,
			"textures/gui/container/laser.png");
	
	public ContainerLaserCrafter container;
	public TileEntityLaserCrafter te;
	public Player player;
	public Inventory playerInv;
	
	public Button Save, NextRecipe;
	public ButtonList recipes;
	
	public GuiLaserCrafter(ContainerLaserCrafter container, Inventory playerInv, Component titleIn) {
		super(container, playerInv, titleIn);
		this.container = container;
		this.te = container.getTileEntity();
		this.playerInv = playerInv;
		this.player = playerInv.player;
		
		int guiLeft = (this.width - this.imageWidth) / 2;
		int guiTop = (this.height - this.imageHeight) / 2;
		
		Save = new Button(0, 0, 20, 20, new TextComponent("Save"), (button) -> {
				//Save Recipe
				SaveSelectedRecipe();
		});
		
		NextRecipe = new Button(0, 0, 20, 20, new TextComponent(">"), (button) -> {
				nextPossibleRecipe();
			});
		
		recipes = new ButtonList(0, 0, 20, 20, new TextComponent("Null"));
	}
	
	public void removeRecipe(Recipe<?> recipe) {
		PacketHandler.INSTANCE.sendToServer(new PacketHandleRecipeChange(te.getBlockPos(), recipe.getId().toString(), PacketHandleRecipeChange.REMOVE));
		te.recipes.remove(recipe);
		reloadRecipeButtonList();
		
	}
	
	public void reloadRecipeButtonList() {
		recipes.clearList();
		for (Recipe<?> recipe: te.recipes) {
			recipes.addButton(new RecipeButton(0, 0, 50, 10, recipe, (button, r) -> {removeRecipe(r);}));
		}
	}
	
	public void SaveSelectedRecipe() {
		if(container.selectedRecipe != null) {
			if(!te.recipes.contains(container.selectedRecipe)) {
//				System.out.println("Saving Recipe: " + container.selectedRecipe.getId().toString());
				te.recipes.add(container.selectedRecipe);
				PacketHandler.INSTANCE.sendToServer(new PacketHandleRecipeChange(te.getBlockPos(), container.selectedRecipe.getId().toString(), PacketHandleRecipeChange.ADD));
				reloadRecipeButtonList();
			}
		}
	}
	
	public void nextPossibleRecipe() {
		if(container.possibleRecipes.size() <= 1)
			return;
		int currentIndex = container.possibleRecipes.indexOf(container.selectedRecipe);
		int next = currentIndex + 1;
		if(next >= container.possibleRecipes.size())
			next = 0;
		container.selectedRecipe = container.possibleRecipes.get(next);
		container.fakeOutput.set(container.selectedRecipe.getResultItem());
	}
	
	protected void init() {
	  super.init();
	  this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
	
	  ChangeSizeButtonLocationUpdate();
	  
	  buttons.clear();
	  buttons.add(Save);
	  buttons.add(NextRecipe);
	  buttons.add(recipes);
	}
	
	public void ChangeSizeButtonLocationUpdate() {
		int posx = width / 2;
		int posy = height / 2;
		
		Save.setWidth(30);
		Save.setHeight(20);
		Save.x = posx + 43;
		Save.y = posy - 30;
		
		NextRecipe.setWidth(10);
		NextRecipe.setHeight(20);
		NextRecipe.x = posx + 30;
		NextRecipe.y = posy - 30;
		
		recipes.setWidth(54);
		recipes.setHeight(55);
		recipes.x = getGuiLeft() + 5;
		recipes.y = posy - 67;
		
		reloadRecipeButtonList();
	}

	public void render(PoseStack matrix, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrix);
		super.render(matrix, mouseX, mouseY, partialTicks);
		this.renderTooltip(matrix, mouseX, mouseY);
	}
	
	protected void renderBg(PoseStack matrix, float partialTicks, int mouseX, int mouseY) {
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0F);
		RenderUtils.bindTexture(TEXTURE);
		int i = (this.width - this.imageWidth) / 2;
		int j = (this.height - this.imageHeight) / 2;
		this.blit(matrix, i, j, 0, 0, this.imageWidth, this.imageHeight);
		
		NextRecipe.active = container.possibleRecipes.size() > 1;
		Save.active = container.selectedRecipe != null;
		
		renderFG(mouseX, mouseY);
	}
	
	protected void renderFG(int mouseX, int mouseY) {
		int actualMouseX = mouseX - ((this.width - this.imageWidth) / 2);
		int actualMouseY = mouseY - ((this.height - this.imageHeight) / 2);
	}
	
	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double deltaScroll) {
		for (AbstractWidget widget : buttons)
			if(widget.isMouseOver(mouseX, mouseY))
				widget.mouseScrolled(mouseX, mouseY, deltaScroll);
		return super.mouseScrolled(mouseX, mouseY, deltaScroll);
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		for (AbstractWidget widget : buttons)
			if(widget.isMouseOver(mouseX, mouseY))
				widget.mouseClicked(mouseX, mouseY, button);
		return super.mouseClicked(mouseX, mouseY, button);
	}
}
