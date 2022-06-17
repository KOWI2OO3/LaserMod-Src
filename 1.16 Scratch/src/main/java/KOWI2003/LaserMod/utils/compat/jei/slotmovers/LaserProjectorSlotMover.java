package KOWI2003.LaserMod.utils.compat.jei.slotmovers;

import java.util.ArrayList;
import java.util.List;

import KOWI2003.LaserMod.gui.GuiLaserProjector;
import KOWI2003.LaserMod.utils.compat.jei.SlotMover;
import net.minecraft.client.renderer.Rectangle2d;

public class LaserProjectorSlotMover extends SlotMover<GuiLaserProjector> {

	public LaserProjectorSlotMover() {
		super();
	}
	
	@Override
	public List<Rectangle2d> getGuiExtraAreas(GuiLaserProjector gui) {
		List<Rectangle2d> tabBoxes = new ArrayList<>();
		
		tabBoxes.add(new Rectangle2d(gui.getGuiLeft() + 176, gui.getGuiTop(), 80, gui.getYSize()));
		return tabBoxes;
	}
	
}
