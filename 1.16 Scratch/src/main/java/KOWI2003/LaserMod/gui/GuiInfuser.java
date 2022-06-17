package KOWI2003.LaserMod.gui;

import com.mojang.blaze3d.matrix.MatrixStack;

import KOWI2003.LaserMod.Reference;
import KOWI2003.LaserMod.container.ContainerInfuser;
import KOWI2003.LaserMod.tileentities.TileEntityInfuser;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class GuiInfuser extends ContainerScreen<ContainerInfuser> {

	public static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID,
			"textures/gui/container/infuser.png");
	
	public TileEntityInfuser te;
	public PlayerEntity player;
	public PlayerInventory playerInv;
	
	public GuiInfuser(ContainerInfuser container, PlayerInventory playerInv, ITextComponent titleIn) {
		super(container, playerInv, titleIn);
		this.te = container.getTileEntity();
		this.playerInv = playerInv;
		this.player = playerInv.player;
	}
	
	public GuiInfuser(PlayerInventory playerInv, TileEntityInfuser te) {
		super(new ContainerInfuser(3, playerInv, te), playerInv, te.getDisplayName());
		this.te = te;
		this.player = playerInv.player;
		this.playerInv = playerInv;
	}
	
	protected void init() {
      super.init();
      this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
	}

	public void render(MatrixStack matrix, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
		this.renderBackground(matrix);
		super.render(matrix, p_230430_2_, p_230430_3_, p_230430_4_);
		this.renderTooltip(matrix, p_230430_2_, p_230430_3_);
	}
	
	protected void renderBg(MatrixStack matrix, float partialTicks, int mouseX, int mouseY) {
		this.minecraft.getTextureManager().bind(TEXTURE);
		int i = (this.width - this.imageWidth) / 2;
		int j = (this.height - this.imageHeight) / 2;
		this.blit(matrix, i, j, 0, 0, this.imageWidth, this.imageHeight);
		
		if(te.getProgress() < 1 && te.getProgress() != 0)
			this.blit(matrix, i + 64, j + 60, 176, 0, Math.round(Math.abs(45 * (te.getProgress() - 1))), 9);
		
		renderFG(mouseX, mouseY);
	}
	
	protected void renderFG(int mouseX, int mouseY) {
		
	}
}
