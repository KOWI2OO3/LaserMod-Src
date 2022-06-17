package KOWI2003.LaserMod.utils.compat.jei;

import java.util.ArrayList;
import java.util.List;

import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.Rectangle2d;

public class SlotMover<T extends ContainerScreen<?>> implements IGuiContainerHandler<T> {

	private static List<Rectangle2d> list;
	
	public SlotMover() {
		list = new ArrayList<Rectangle2d>();
	}
	
	public void addRectangle(int x, int y, int width, int height) {
		list.add(new Rectangle2d(x, y, width, height));
	}
	
	public void removeRectangle(int x, int y, int width, int height) {
		list.remove(new Rectangle2d(x, y, width, height));
	}
	
	public void addRectangle(Rectangle2d rect) {
		list.add(rect);
	}
	
	public void removeRectangle(Rectangle2d rect) {
		list.remove(rect);
	}
	
	@Override
	public List<Rectangle2d> getGuiExtraAreas(T containerScreen) {
		List<Rectangle2d> tabBoxes = new ArrayList<>();

		for(Rectangle2d rect : list) {
			if(rect != null)
				tabBoxes.add(rect);
		}
		
		return tabBoxes;
	}
	
	@Override
	public Object getIngredientUnderMouse(T containerScreen, double mouseX, double mouseY) {
		return null;
	}
	
}
