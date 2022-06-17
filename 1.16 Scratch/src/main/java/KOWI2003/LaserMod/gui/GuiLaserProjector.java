package KOWI2003.LaserMod.gui;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import KOWI2003.LaserMod.Reference;
import KOWI2003.LaserMod.container.ContainerLaserProjector;
import KOWI2003.LaserMod.network.PacketHandler;
import KOWI2003.LaserMod.network.PacketLaserMode;
import KOWI2003.LaserMod.network.PacketLaserProjector;
import KOWI2003.LaserMod.network.PacketProjectorProperty;
import KOWI2003.LaserMod.tileentities.TileEntityLaserProjector;
import KOWI2003.LaserMod.tileentities.TileEntityLaserProjector.PROJECTOR_MODES;
import KOWI2003.LaserMod.tileentities.TileEntityLaserProjector.ProjectorProperties.PROJECTOR_PROPERTY;
import KOWI2003.LaserMod.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.CheckboxButton;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.client.gui.GuiUtils;
import net.minecraftforge.fml.client.gui.widget.Slider;

public class GuiLaserProjector extends ContainerScreen<ContainerLaserProjector> {

	public static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID,
			"textures/gui/container/laser_projector.png");
	
	public TileEntityLaserProjector te;
	public PlayerEntity player;
	public PlayerInventory playerInv;
	
	//WIDGETS
	public Button modeNext;
	public Button modePrev;
	
	public List<Slider> propertySliders;
	public Button resetProperties;
	
	public TextFieldWidget text;
	public CheckboxButton doesRotate;
	public CheckboxButton liveModel;
	public CheckboxButton isChild;
	
	boolean lastDoesRotate, lastLiveModel, lastIsChild = false;
	
	public GuiLaserProjector(ContainerLaserProjector container, PlayerInventory playerInv, ITextComponent titleIn) {
		super(container, playerInv, titleIn);
		this.te = container.getTileEntity();
		this.playerInv = playerInv;
		this.player = playerInv.player;
		
		
		modePrev = new Button(0, 0, 20, 20, new StringTextComponent("<"), (button) -> {
			PacketHandler.sendToServer(new PacketLaserMode(te.getBlockPos(), false));
		});
		modeNext = new Button(modePrev.x + modePrev.getWidth() + 60, modePrev.y, modePrev.getWidth(), modePrev.getHeight(), new StringTextComponent(">"), (button) -> {
			PacketHandler.sendToServer(new PacketLaserMode(te.getBlockPos(), true));
		});
		
		text = new TextFieldWidget(Minecraft.getInstance().font, 1, 1, 80, 20, new StringTextComponent("UNKNOWN"));
		text.setValue(te.text);
		
		doesRotate = new CheckboxButton(0, 0, 10, 10, new StringTextComponent("Enable Rotation"), te.doesRotate);
		lastDoesRotate = te.doesRotate;
		
		liveModel = new CheckboxButton(0, 0, 10, 10, new StringTextComponent("Live Model"), te.liveModel);
		lastLiveModel = liveModel.selected();
		
		isChild = new CheckboxButton(0, 0, 10, 10, new StringTextComponent("Child"), te.isChild);
		lastIsChild = isChild.selected();
		
		resetProperties = new Button(0, 0, 60, 20, new StringTextComponent("Reset"), (button) -> {
			PacketHandler.sendToServer(new PacketProjectorProperty(te.getBlockPos(), -1, -1.0f));
			for (Slider slider : propertySliders) {
				PROJECTOR_PROPERTY property = PROJECTOR_PROPERTY.valueOf(Utils.fromFormalText(slider.getMessage().getString()));
				slider.setValue(property.getDefaultValue());
			}
		});
		
		propertySliders = new ArrayList<Slider>();
		for (PROJECTOR_PROPERTY property : PROJECTOR_PROPERTY.values()) {
			Slider slider = new Slider(0, 0, new StringTextComponent(Utils.getFormalText(property.name())), property.getMin(), property.getMax(), te.properties.getProperty(property), (button) -> {}, (button) -> {
				PacketHandler.sendToServer(new PacketProjectorProperty(te.getBlockPos(), property.ordinal(), (float)button.getValue()));
				button.setMessage(new StringTextComponent(Utils.getFormalText(property.name())));
			});
			slider.setMessage(new StringTextComponent(Utils.getFormalText(property.name())));
			propertySliders.add(slider);
		}
	}
	
	public GuiLaserProjector(PlayerInventory playerInv, TileEntityLaserProjector te) {
		this(new ContainerLaserProjector(3, playerInv, te), playerInv, te.getDisplayName());
	}
	
	protected void init() {
      super.init();
      this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
      ChangeSizeButtonLocationUpdate();
      
	  buttons.clear();
	  
	  buttons.add(modeNext);
	  buttons.add(modePrev);
	  buttons.add(resetProperties);
	  buttons.add(text);
	  buttons.add(doesRotate);
	  buttons.add(liveModel);
	  buttons.add(isChild);
	  for (Slider slider: propertySliders) {
			buttons.add(slider);
	  }
	}
	
	public void ChangeSizeButtonLocationUpdate() {
		int posx = width / 2;
		int posy = height / 2;
		
		this.modePrev.x = posx + 91;
		this.modePrev.y = posy - 44;
		
		this.modeNext.x = modePrev.x + modePrev.getWidth() + 30;
		this.modeNext.y = modePrev.y;
		
		text.x = posx - 40;
		text.y = posy - 50;
		
		doesRotate.visible = true;
		doesRotate.x = posx - 24;
		doesRotate.y = posy - 22;
		
		liveModel.visible = isChild.visible = te.mode == PROJECTOR_MODES.PLAYER;
		liveModel.x = posx - 74;
		liveModel.y = posy - 22;
		
		isChild.x = posx + 38;
		isChild.y = posy - 22;
		
		//Properties
		resetProperties.x = posx + 96;
		resetProperties.y = posy - 21;
		int x = posx + 89;
		int y = posy;
		int width = 75;
		int height = 15;
		
		for (int i = 0; i < propertySliders.size(); i++) {
			Slider slider = propertySliders.get(i);
			propertySliders.get(i).setWidth(width);
			propertySliders.get(i).setHeight(height);
			propertySliders.get(i).y = y + height * i;
			propertySliders.get(i).x = x;
		}
	}

	public void render(MatrixStack matrix, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
		this.renderBackground(matrix);
		try {
			super.render(matrix, p_230430_2_, p_230430_3_, p_230430_4_);
			ChangeSizeButtonLocationUpdate();
		}catch(Exception e) { e.printStackTrace(); }
		this.renderTooltip(matrix, p_230430_2_, p_230430_3_);
	}
	
	protected void renderBg(MatrixStack matrix, float partialTicks, int mouseX, int mouseY) {
		doesRotate.visible = true;
		this.minecraft.getTextureManager().bind(TEXTURE);
		int i = (this.width - this.imageWidth) / 2;
		int j = (this.height - this.imageHeight) / 2;
		this.blit(matrix, i, j, 0, 0, this.imageWidth, this.imageHeight);
		
			this.blit(matrix, i + 175, j, 175, 0, 81, imageHeight);
			GuiUtils.drawContinuousTexturedBox(matrix, Widget.WIDGETS_LOCATION, modePrev.x, modePrev.y - modePrev.getHeight(), 0, 46, 
					modeNext.x + modeNext.getWidth() - modePrev.x,modePrev.getHeight(), 200, 20, 2, 3, 2, 2, this.getBlitOffset());
			
			ITextComponent modeText = new StringTextComponent(Utils.getFormalText(te.mode.name())).copy().setStyle(title.getStyle());
			drawCenteredString(matrix, font, modeText, modePrev.x + 
					(modeNext.x + modeNext.getWidth() - modePrev.x)/2, modePrev.y - modePrev.getHeight()/2 - font.lineHeight/2, Utils.getHexIntFromRGB(1f, 1f, 1f));
			ITextComponent text = new StringTextComponent("Projection:").copy().setStyle(title.getStyle());
			font.draw(matrix, text, getGuiLeft() + 186, getGuiTop() + 10, Utils.getHexIntFromRGB(0.3f, 0.3f, 0.3f));
			
		
		if(lastDoesRotate != doesRotate.selected()) {
			lastDoesRotate = doesRotate.selected();
			PacketHandler.sendToServer(new PacketLaserProjector(te.getBlockPos(), "doesRotate", doesRotate.selected()));
		}
		if(lastLiveModel != liveModel.selected()) {
			lastLiveModel = liveModel.selected();
			if(liveModel.selected())
				if(te.mode == PROJECTOR_MODES.PLAYER) {
					if(te.liveModel)
						te.playerToRender = Utils.getPlayer(te.getLevel(), this.text.getValue());
					else
						te.profile = Utils.getProfile(te.text);
				}
			PacketHandler.sendToServer(new PacketLaserProjector(te.getBlockPos(), "liveModel", lastLiveModel));
		}
		if(lastIsChild != isChild.selected()) {
			lastIsChild = isChild.selected();
			PacketHandler.sendToServer(new PacketLaserProjector(te.getBlockPos(), "isChild", lastIsChild));
		}
		renderCheckBox(matrix, doesRotate);
		if(te.mode == PROJECTOR_MODES.PLAYER) {
			renderCheckBox(matrix, liveModel);
			renderCheckBox(matrix, isChild);
		}
		renderFG(matrix, mouseX, mouseY);
	}
	
	public void renderCheckBox(MatrixStack matrix, CheckboxButton checkbox) {
		int posx = checkbox.x;
		int posy = checkbox.y;
		checkbox.x = 0;
		checkbox.y = 0;
		RenderSystem.pushMatrix();
		RenderSystem.translated(posx, posy, 0);
		double scale = 0.5;
		RenderSystem.scaled(scale, scale, scale);
		checkbox.setWidth(20);
		checkbox.setHeight(20);
		checkbox.renderButton(matrix, 0, 0, 0);
		checkbox.setWidth(10);
		checkbox.setHeight(10);
		RenderSystem.popMatrix();
		checkbox.visible = false;
	}
	
	protected void renderFG(MatrixStack matrix, int mouseX, int mouseY) {
		text.visible = te.mode == PROJECTOR_MODES.TEXT || te.mode == PROJECTOR_MODES.PLAYER;
		
		switch (te.mode) {
		case ITEM:
			this.minecraft.getTextureManager().bind(TEXTURE);
			this.blit(matrix, 80 + getGuiLeft() - 1, 30 + getGuiTop() - 1, 177, 167, 18, 18);
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onClose() {
		if(!te.text.equals(text.getValue()))
		if(te.mode == PROJECTOR_MODES.PLAYER) {
			if(te.liveModel)
				te.playerToRender = Utils.getPlayer(te.getLevel(), text.getValue());
			else
				te.profile = Utils.getProfile(text.getValue());
		}
		PacketHandler.sendToServer(new PacketLaserProjector(te.getBlockPos(), "text", text.getValue()));
		super.onClose();
	}
	
	@Override
	public boolean mouseClicked(double p_231044_1_, double p_231044_3_, int p_231044_5_) {
		for (Widget widget : buttons) {
			widget.mouseClicked(p_231044_1_, p_231044_3_, p_231044_5_);
		}
		return super.mouseClicked(p_231044_1_, p_231044_3_, p_231044_5_);
	}
	
	@Override
	public boolean mouseReleased(double p_231048_1_, double p_231048_3_, int p_231048_5_) {
		for (Widget widget : buttons) {
			widget.mouseReleased(p_231048_1_, p_231048_3_, p_231048_5_);
		}
		return super.mouseReleased(p_231048_1_, p_231048_3_, p_231048_5_);
	}
	
	@Override
	public boolean mouseDragged(double p_231045_1_, double p_231045_3_, int p_231045_5_, double p_231045_6_,
			double p_231045_8_) {
		for (Widget widget : buttons) {
			widget.mouseDragged(p_231045_1_, p_231045_3_, p_231045_5_, p_231045_6_, p_231045_8_);
		}
		return super.mouseDragged(p_231045_1_, p_231045_3_, p_231045_5_, p_231045_6_, p_231045_8_);
	}
	
	@Override
	public void mouseMoved(double p_212927_1_, double p_212927_3_) {
		for (Widget widget : buttons) {
			widget.mouseMoved(p_212927_1_, p_212927_3_);
		}
		super.mouseMoved(p_212927_1_, p_212927_3_);
	}
	
	@Override
	public boolean charTyped(char p_231042_1_, int p_231042_2_) {
		for (Widget widget : buttons) {
			if(!(widget instanceof Button))
			if(widget.charTyped(p_231042_1_, p_231042_2_))
				return true;
		}
		return super.charTyped(p_231042_1_, p_231042_2_);
	}
	
	@Override
	public boolean keyPressed(int p_231046_1_, int p_231046_2_, int p_231046_3_) {
		if(InputMappings.getKey(p_231046_1_, p_231046_2_).getName().equals("key.keyboard.escape"))
			return super.keyPressed(p_231046_1_, p_231046_2_, p_231046_3_);
		for (Widget widget : buttons) {
			if(!(widget instanceof Button) && !(widget instanceof CheckboxButton))
			if(widget.keyPressed(p_231046_1_, p_231046_2_, p_231046_3_))
				return true;
		}
		if(text.isVisible() && text.isFocused())
			return true;
		return super.keyPressed(p_231046_1_, p_231046_2_, p_231046_3_);
	}
	
	@Override
	public boolean keyReleased(int p_223281_1_, int p_223281_2_, int p_223281_3_) {
		for (Widget widget : buttons) {
			if(!(widget instanceof Button))
			if (widget.keyReleased(p_223281_1_, p_223281_2_, p_223281_3_))
				return true;
		}
		if(text.isVisible() && text.isFocused())
			return true;
		return super.keyReleased(p_223281_1_, p_223281_2_, p_223281_3_);
	}
}
