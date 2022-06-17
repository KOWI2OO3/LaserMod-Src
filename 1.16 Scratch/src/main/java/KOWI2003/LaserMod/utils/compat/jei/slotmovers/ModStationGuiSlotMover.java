package KOWI2003.LaserMod.utils.compat.jei.slotmovers;

import java.util.ArrayList;
import java.util.List;

import KOWI2003.LaserMod.gui.GuiModStation;
import KOWI2003.LaserMod.utils.compat.jei.SlotMover;
import net.minecraft.client.renderer.Rectangle2d;

public class ModStationGuiSlotMover extends SlotMover<GuiModStation> {

	public ModStationGuiSlotMover() {
		super();
	}
	
	@Override
	public List<Rectangle2d> getGuiExtraAreas(GuiModStation gui) {
		List<Rectangle2d> tabBoxes = new ArrayList<>();
		
		if(!gui.te.handler.getStackInSlot(0).isEmpty()) 
			tabBoxes.add(new Rectangle2d(gui.getGuiLeft() + 176, gui.getGuiTop(), 80, gui.getYSize()));
		return tabBoxes;
	}
	
}
