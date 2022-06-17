package KOWI2003.LaserMod.utils.compat.jei.slotmovers;

import java.util.ArrayList;
import java.util.List;

import KOWI2003.LaserMod.gui.GuiLaser;
import KOWI2003.LaserMod.utils.compat.jei.SlotMover;
import net.minecraft.client.renderer.Rectangle2d;

public class LaserGuiSlotMover extends SlotMover<GuiLaser> {

	public LaserGuiSlotMover() {
		super();
	}
	
	@Override
	public List<Rectangle2d> getGuiExtraAreas(GuiLaser gui) {
		List<Rectangle2d> tabBoxes = new ArrayList<>();
		
		if(gui.menuOpen) 
			tabBoxes.add(new Rectangle2d(gui.getGuiLeft() + 176, gui.getGuiTop(), 80, gui.getYSize()));
		return tabBoxes;
	}
	
}
