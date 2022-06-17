package KOWI2003.LaserMod.items;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

public class ItemCrafting extends ItemDefault {

	public ItemCrafting() {}
	
	@Override
	public void appendHoverText(ItemStack stack, World world, List<ITextComponent> tooltip,
			ITooltipFlag flag) {
		tooltip.add(new StringTextComponent("Crafting Item"));
		super.appendHoverText(stack, world, tooltip, flag);
	}
}
