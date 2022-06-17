package KOWI2003.LaserMod.gui;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public abstract class BetterAbstractContainerScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {

	public List<AbstractWidget> buttons;
	
	public BetterAbstractContainerScreen(T container, Inventory inv, Component title) {
		super(container, inv, title);
		buttons = new LinkedList<AbstractWidget>();
	}
	
	@Override
	protected void clearWidgets() {
		buttons.clear();
		super.clearWidgets();
	}
	
	@Override
	protected <T extends GuiEventListener & Widget & NarratableEntry> T addRenderableWidget(T widget) {
		if(widget instanceof AbstractWidget) buttons.add((AbstractWidget)widget);
		return super.addRenderableWidget(widget);
	}

}
