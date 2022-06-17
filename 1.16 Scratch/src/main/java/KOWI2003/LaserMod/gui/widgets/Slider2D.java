package KOWI2003.LaserMod.gui.widgets;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import KOWI2003.LaserMod.Reference;
import KOWI2003.LaserMod.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.text.StringTextComponent;

public class Slider2D extends Button {

	public static final ResourceLocation WIDGETS_LOCATION = new ResourceLocation(Reference.MODID,"textures/gui/mod_widgets.png");
	
	//Widget
	public float wX, wY = 0;
	public float wSize = 10;
	
	public String internalName = "Slider";
	
	public float r, g, b = 1f;
	
	public Slider2D(int x, int y, int widht, int height,
			IPressable action) {
		super(x, y, widht, height, new StringTextComponent(""), action);
	}
	
	public Slider2D(int x, int y, int widht, int height, String internalName,
			IPressable action) {
		super(x, y, widht, height, new StringTextComponent(""), action);
		this.internalName = internalName;
	}
	
	public void setSliderStartValues(float x, float y, float size) {
		wSize = size;
		setValueX(x);
		setValueY(y);
	}
	
	boolean wasHovered = false;
	
	public Vector2f getValue() {
		return new Vector2f(getValueX(), getValueY());
	}
	
	public float getValueX() {
		return wX/(width - wSize);
	}
	
	public float getValueY() {
		return wY/(height - wSize);
	}
	
	public void setValueX(float x) {
		x = Math.min(1, Math.max(0, x));
		wX = x * (width - wSize);
	}
	
	public void setValueY(float y) {
		y = Math.min(1, Math.max(0, y));
		wY = y * (height - wSize);
	}
	
	public void setValue(Vector2f vec) {
		setValue(vec.x, vec.y);
	}
	
	public void setValue(float x, float y) {
		setValueX(x);
		setValueY(y);
	}
	
	@Override
	public void setWidth(int width) {
		float sX = getValueX();
		super.setWidth(width);
		setValueX(sX);
	}
	
	@Override
	public void setHeight(int height) {
		float sY = getValueY();
		super.setHeight(height);
		setValueY(sY);
	}
	
	@Override
	public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
		if (this.visible) {
	         this.isHovered = p_230430_2_ >= this.x && p_230430_3_ >= this.y && p_230430_2_ < this.x + this.width && p_230430_3_ < this.y + this.height;
	         if (this.wasHovered != this.isHovered()) {
	            if (this.isHovered()) {
	               if (this.isFocused()) {
	                  this.queueNarration(200);
	               } else {
	                  this.queueNarration(750);
	               }
	            } else {
	               this.nextNarration = Long.MAX_VALUE;
	            }
	         }

	         if (this.visible) {
	            this.onRender(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
	         }

	         this.narrate();
	         this.wasHovered = this.isHovered();
	      }
	}
	
	public void onRender(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
		RenderSystem.color4f(r, g, b, this.alpha);
		renderButton(stack, mouseX, mouseY, partialTicks);
		
		//Render Slider
		Minecraft minecraft = Minecraft.getInstance();
		minecraft.getTextureManager().bind(WIDGETS_LOCATION);
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.alpha);
		RenderUtils.Gui.drawQuad(stack, x + wX, y + wY, wSize, wSize, 20f / 256f + 0.0001f, 108f / 256f, 11f / 256f, -11f / 256f);
	}
	
	@Override
	public void renderButton(MatrixStack p_230431_1_, int p_230431_2_, int p_230431_3_, float p_230431_4_) {
		 Minecraft minecraft = Minecraft.getInstance();
		 FontRenderer fontrenderer = minecraft.font;
		 minecraft.getTextureManager().bind(WIDGETS_LOCATION);
		 //RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.alpha);
		 int i = this.getYImage(this.isHovered());
		 RenderSystem.enableBlend();
		 RenderSystem.defaultBlendFunc();
		 RenderSystem.enableDepthTest();
		 
		 int uvX = 41;
		 int uvY = 106; //46 + i * 20
		 
		 this.blit(p_230431_1_, this.x, this.y, uvX, uvY, this.width / 2, this.height/2); //matrix, guiX, guiY, uvX, uvY, width, height
		 this.blit(p_230431_1_, this.x + this.width / 2, this.y, (256 - this.width / 2), uvY, this.width / 2, this.height/2);
		 
		 this.blit(p_230431_1_, this.x, this.y + this.height / 2, uvX, (206 - this.height / 2), this.width / 2, this.height/2); //matrix, guiX, guiY, uvX, uvY, width, height
		 this.blit(p_230431_1_, this.x + this.width / 2, this.y + this.height / 2, (256 - this.width / 2), (206 - this.height / 2), this.width / 2, this.height/2);
		 
		 this.renderBg(p_230431_1_, minecraft, p_230431_2_, p_230431_3_);
		 //int j = this.getFGColor();
		 //drawCenteredString(p_230431_1_, fontrenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
		if (this.isHovered()) {
			this.renderToolTip(p_230431_1_, p_230431_2_, p_230431_3_);
		}
	}
	
	protected void narrate() {
      if (this.active && this.isHovered() && Util.getMillis() > this.nextNarration) {
         String s = this.createNarrationMessage().getString();
         if (!s.isEmpty()) {
            NarratorChatListener.INSTANCE.sayNow(internalName);
            this.nextNarration = Long.MAX_VALUE;
         }
      }

   }
	
	@Override
	protected void onDrag(double mouseX, double mouseY, double deltaX, double deltaY) {
		if(isDragging) {
			wX = (float)mouseX - x - wSize/2f;
			wX = Math.min(width, Math.max(0, wX) + wSize) - wSize;
			wY = (float)mouseY - y - wSize/2f;
			wY = Math.min(height, Math.max(0, wY) + wSize) - wSize;
		}
		super.onDrag(mouseX, mouseY, deltaX, deltaY);
	}
	
	public boolean isDragging = false;
	
	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX,
			double deltaY) {
		if (this.isValidClickButton(button)) {
			if(mouseX >= wX + x && mouseX <= wX + wSize + x && mouseY >= wY + y && mouseY <= wY + y + wSize) {
				isDragging = true;
			}
		}
		return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
	}
	
	@Override
	public void mouseMoved(double deltaX, double deltaY) {
		// TODO Auto-generated method stub
		super.mouseMoved(deltaX, deltaY);
	}
	
	@Override
	public void onPress() {
		super.onPress();
	}
	
	@Override
	public void onRelease(double p_231000_1_, double p_231000_3_) {
		if(isDragging)
			isDragging = false;
		super.onRelease(p_231000_1_, p_231000_3_);
	}
}
