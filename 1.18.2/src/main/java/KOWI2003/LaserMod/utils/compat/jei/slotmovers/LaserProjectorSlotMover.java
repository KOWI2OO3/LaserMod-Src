package KOWI2003.LaserMod.utils.compat.jei.slotmovers;

import java.util.ArrayList;
import java.util.List;

import KOWI2003.LaserMod.gui.GuiLaserProjector;
import KOWI2003.LaserMod.utils.compat.jei.SlotMover;
import net.minecraft.client.renderer.Rect2i;

public class LaserProjectorSlotMover extends SlotMover<GuiLaserProjector> {

	public LaserProjectorSlotMover() {
		super();
	}
	
	@Override
	public List<Rect2i> getGuiExtraAreas(GuiLaserProjector gui) {
		List<Rect2i> tabBoxes = new ArrayList<>();
		
		tabBoxes.add(new Rect2i(gui.getGuiLeft() + 176, gui.getGuiTop(), 80, gui.getYSize()));
		return tabBoxes;
	}
	
}
