package KOWI2003.LaserMod.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import KOWI2003.LaserMod.Reference;
import KOWI2003.LaserMod.container.ContainerLaser;
import KOWI2003.LaserMod.gui.widgets.Slider2D;
import KOWI2003.LaserMod.network.PacketHandler;
import KOWI2003.LaserMod.network.PacketLaser;
import KOWI2003.LaserMod.network.PacketLaserDirection;
import KOWI2003.LaserMod.network.PacketLaserMode;
import KOWI2003.LaserMod.tileentities.TileEntityAdvancedLaser;
import KOWI2003.LaserMod.tileentities.TileEntityLaser;
import KOWI2003.LaserMod.utils.MathUtils;
import KOWI2003.LaserMod.utils.Utils;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.client.gui.GuiUtils;
import net.minecraftforge.fml.client.gui.widget.Slider;

public class GuiLaser extends ContainerScreen<ContainerLaser> {

	public static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID,
			"textures/gui/container/laser.png");
	
	public TileEntityLaser te;
	public PlayerEntity player;
	public PlayerInventory playerInv;
	
	public Button options;
	public boolean menuOpen = false;
	
	public Slider Red;
	public Slider Green;
	public Slider Blue;
	
	public Button modeNext;
	public Button modePrev;
	
	public Slider2D gimbalSlider;
	
	public GuiLaser(ContainerLaser container, PlayerInventory playerInv, ITextComponent titleIn) {
		super(container, playerInv, titleIn);
		this.te = container.getTileEntity();
		this.playerInv = playerInv;
		this.player = playerInv.player;
		
		int guiLeft = (this.width - this.imageWidth) / 2;
		int guiTop = (this.height - this.imageHeight) / 2;

		options = new Button(width + getGuiLeft() + 260 - 30, getGuiTop() + 100, 50, 20, new TranslationTextComponent("container.lasermod.laser.button.options"), (button) -> 
			toggleMenu());
		
		Red = new Slider(0, 0, new TranslationTextComponent("container.lasermod.laser.red"), 0f, 1f, te.red, (button) -> {}, (button) -> {
			PacketHandler.sendToServer(new PacketLaser(te.getBlockPos(), (float)Red.getValue(), (float)Green.getValue(), (float)Blue.getValue()));
			Red.setMessage(new TranslationTextComponent("container.lasermod.laser.red"));
		});
		Green = new Slider(0, 40, new StringTextComponent("container.lasermod.laser.green"), 0f, 1f, te.green, (button) -> {}, (button) -> {
			PacketHandler.sendToServer(new PacketLaser(te.getBlockPos(), (float)Red.getValue(), (float)Green.getValue(), (float)Blue.getValue()));
			Green.setMessage(new TranslationTextComponent("container.lasermod.laser.green"));
		});
		Blue = new Slider(0, 80, new StringTextComponent("container.lasermod.laser.blue"), 0f, 1f, te.blue, (button) -> {}, (button) -> {
			PacketHandler.sendToServer(new PacketLaser(te.getBlockPos(), (float)Red.getValue(), (float)Green.getValue(), (float)Blue.getValue()));
			Blue.setMessage(new TranslationTextComponent("container.lasermod.laser.blue"));
		});
		
		Red.setMessage(new TranslationTextComponent("container.lasermod.laser.red"));
		Green.setMessage(new TranslationTextComponent("container.lasermod.laser.green"));
		Blue.setMessage(new TranslationTextComponent("container.lasermod.laser.blue"));
		
		modePrev = new Button(0, 0, 20, 20, new StringTextComponent("<"), (button) -> {
			PacketHandler.sendToServer(new PacketLaserMode(te.getBlockPos(), false));
		});
		
		modeNext = new Button(modePrev.x + modePrev.getWidth() + 60, modePrev.y, modePrev.getWidth(), modePrev.getHeight(), new StringTextComponent(">"), (button) -> {
			PacketHandler.sendToServer(new PacketLaserMode(te.getBlockPos(), true));
		});
		
		gimbalSlider = new Slider2D(0, 0, 100, 100, (o) -> {});
		gimbalSlider.setSliderStartValues(0.5f, 0.5f, 15f);
		if(te instanceof TileEntityAdvancedLaser) {
			float absAngle = Math.abs(TileEntityAdvancedLaser.minAngle);
			Vector2f rotation = ((TileEntityAdvancedLaser)te).getEularRotation();
			gimbalSlider.setSliderStartValues(Math.round(((rotation.y/absAngle)/2f + 0.5f) * 100f) / 100f, Math.round(((-rotation.x/absAngle)/2f + 0.5f) * 100f) / 100f, gimbalSlider.wSize);
		}
	}
	
	public void toggleMenu() {
		menuOpen = !menuOpen;
	}
	
	public GuiLaser(PlayerInventory playerInv, TileEntityLaser te) {
		this(new ContainerLaser(9, playerInv, te), playerInv, te.getDisplayName());
	}
	
	protected void init() {
	  super.init();
	  this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
	
	  ChangeSizeButtonLocationUpdate();
	  options.active = options.visible = hasSideMenu();
	  
	  buttons.clear();
	  if(te instanceof TileEntityAdvancedLaser)
		  buttons.add(gimbalSlider);
	  buttons.add(options);
	  buttons.add(Red);
	  buttons.add(Green);
	  buttons.add(Blue);
	  
	  buttons.add(modeNext);
	  buttons.add(modePrev);
	  
	}
	
	public void ChangeSizeButtonLocationUpdate() {
		int posx = width / 2;
		int posy = height / 2;
		
		this.options.x = posx + 30;
		this.options.y = posy - 25;
		
		this.modePrev.x = posx + 91;
		this.modePrev.y = posy - 40;
		
		this.modeNext.x = modePrev.x + modePrev.getWidth() + 30;
		this.modeNext.y = modePrev.y;
		
		int x = 89;
		int y = 10;
		
		this.Red.x = posx + x;
		this.Red.y = posy + 1 - y;
		this.Green.x = posx + x;
		this.Green.y = posy + 21 - y;
		this.Blue.x = posx + x;
		this.Blue.y = posy + 41 - y;

		Red.setWidth(75);
		Green.setWidth(75);
		Blue.setWidth(75);
		
		if(te instanceof TileEntityAdvancedLaser) {
			gimbalSlider.x = posx-81;
			gimbalSlider.y = posy-65;
			gimbalSlider.setWidth(50);
			gimbalSlider.setHeight(50);
		}
	}

	public void render(MatrixStack matrix, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
		this.renderBackground(matrix);
		super.render(matrix, p_230430_2_, p_230430_3_, p_230430_4_);
		this.renderTooltip(matrix, p_230430_2_, p_230430_3_);
	}
	
	protected void renderBg(MatrixStack matrix, float partialTicks, int mouseX, int mouseY) {
		if(te != null) {
			RenderSystem.color4f(te.red, te.green, te.blue, 1.0f);
			if(te instanceof TileEntityAdvancedLaser) {
				gimbalSlider.r = te.red;
				gimbalSlider.g = te.green;
				gimbalSlider.b = te.blue;
			}
		}else
			RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0F);
		this.minecraft.getTextureManager().bind(TEXTURE);
		int i = (this.width - this.imageWidth) / 2;
		int j = (this.height - this.imageHeight) / 2;
		this.blit(matrix, i, j, 0, 0, this.imageWidth, this.imageHeight);
		
		if(hasChanged())
			onUpgradeAddOrRemove();
		
		Red.active = Red.visible =
			Blue.active = Blue.visible =
			Green.active = Green.visible = te.getProperties().hasUpgarde("color") && menuOpen;
		
		modePrev.active = modePrev.visible =
				modeNext.active = modeNext.visible = te.getProperties().hasUpgarde("mode") && menuOpen;
		
		if(hasSideMenu() && menuOpen) {
			this.minecraft.getTextureManager().bind(TEXTURE);
			this.blit(matrix, i + 175, j, 175, 0, 81, imageHeight);
			
			if(te.getProperties().hasUpgarde("mode")) {
				GuiUtils.drawContinuousTexturedBox(matrix, Widget.WIDGETS_LOCATION, modePrev.x, modePrev.y - modePrev.getHeight(), 0, 46, 
						modeNext.x + modeNext.getWidth() - modePrev.x,modePrev.getHeight(), 200, 20, 2, 3, 2, 2, this.getBlitOffset());
				
				ITextComponent modeText = new StringTextComponent(te.mode.getFormalName()).copy().setStyle(title.getStyle());
				drawCenteredString(matrix, font, modeText, modePrev.x + 
						(modeNext.x + modeNext.getWidth() - modePrev.x)/2, modePrev.y - modePrev.getHeight()/2 - font.lineHeight/2, Utils.getHexIntFromRGB(1f, 1f, 1f));
				ITextComponent text = new StringTextComponent("Laser Mode:").copy().setStyle(title.getStyle());
				font.draw(matrix, text, getGuiLeft() + 185, getGuiTop() + 10, Utils.getHexIntFromRGB(0.3f, 0.3f, 0.3f));
			}
		}
		
		renderFG(mouseX, mouseY);
	}
	
	protected void renderFG(int mouseX, int mouseY) {
		int actualMouseX = mouseX - ((this.width - this.imageWidth) / 2);
		int actualMouseY = mouseY - ((this.height - this.imageHeight) / 2);
		
		if(te instanceof TileEntityAdvancedLaser) {
			Vector2f rotation = gimbalSlider.getValue();
			rotation = new Vector2f(-(rotation.y - 0.5f) * 2f, (rotation.x - 0.5f) * 2f);
			rotation = MathUtils.mulVector(rotation, Math.abs(TileEntityAdvancedLaser.minAngle));
			rotation = new Vector2f(Math.round(rotation.x * 100f) / 100f, Math.round(rotation.y * 100f) / 100f);
			PacketHandler.INSTANCE.sendToServer(new PacketLaserDirection(te.getBlockPos(), rotation));
		}
	}
	
	private void onUpgradeAddOrRemove() {
		if(menuOpen && !hasSideMenu())
			menuOpen = false;
		options.active = options.visible = hasSideMenu();
		if(!te.getProperties().hasUpgarde("color")) {
			Red.setValue(1.0f);
			Green.setValue(0.0f);
			Blue.setValue(0.0f);
			PacketHandler.sendToServer(new PacketLaser(te.getBlockPos(), (float)Red.getValue(), (float)Green.getValue(), (float)Blue.getValue()));
		}
		if(!te.getProperties().hasUpgarde("mode")) {
			PacketHandler.sendToServer(new PacketLaserMode(te.getBlockPos(), 0));
		}
	}
	
	public boolean hasSideMenu() {
		return te.getProperties().hasUpgarde("color") || te.getProperties().hasUpgarde("mode");
	}
	
	boolean Ucol, UMode = false;
	public boolean hasChanged() {
		if(te.getProperties().hasUpgarde("color") != Ucol || te.getProperties().hasUpgarde("mode") != UMode) {
			Ucol = te.getProperties().hasUpgarde("color");
			UMode = te.getProperties().hasUpgarde("mode");
			return true;
		}
		return false;
	}
	
	@Override
	public boolean mouseClicked(double p_231044_1_, double p_231044_3_, int p_231044_5_) {
		for (Widget widget : buttons) {
			widget.mouseClicked(p_231044_1_, p_231044_3_, p_231044_5_);
		}
		return super.mouseClicked(p_231044_1_, p_231044_3_, p_231044_5_);
	}
	
	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int p_231045_5_, double p_231045_6_,
			double p_231045_8_) {
		double actualMouseX = mouseX - ((this.width - this.imageWidth) / 2);
		double actualMouseY = mouseY - ((this.height - this.imageHeight) / 2);
		Red.mouseDragged(mouseX, mouseY, p_231045_5_, p_231045_6_, p_231045_8_);
		Green.mouseDragged(mouseX, mouseY, p_231045_5_, p_231045_6_, p_231045_8_);
		Blue.mouseDragged(mouseX, mouseY, p_231045_5_, p_231045_6_, p_231045_8_);
		if(te instanceof TileEntityAdvancedLaser)
			gimbalSlider.mouseDragged(mouseX, mouseY, p_231045_5_, p_231045_6_, p_231045_8_);
		return super.mouseDragged(mouseX, mouseY, p_231045_5_, p_231045_6_, p_231045_8_);
	}
	
	@Override
	public void mouseMoved(double deltaX, double deltaY) {
		Red.mouseMoved(deltaX, deltaY);
		Green.mouseMoved(deltaX, deltaY);
		Blue.mouseMoved(deltaX, deltaY);
		if(te instanceof TileEntityAdvancedLaser)
			gimbalSlider.mouseMoved(deltaX, deltaY);
		super.mouseMoved(deltaX, deltaY);
	}
	
	@Override
	public boolean mouseReleased(double p_231048_1_, double p_231048_3_, int p_231048_5_) {
		Red.mouseReleased(p_231048_1_, p_231048_3_, p_231048_5_);
		Green.mouseReleased(p_231048_1_, p_231048_3_, p_231048_5_);
		Blue.mouseReleased(p_231048_1_, p_231048_3_, p_231048_5_);
		if(te instanceof TileEntityAdvancedLaser)
			gimbalSlider.mouseReleased(p_231048_1_, p_231048_3_, p_231048_5_);
		return super.mouseReleased(p_231048_1_, p_231048_3_, p_231048_5_);
	}

}
