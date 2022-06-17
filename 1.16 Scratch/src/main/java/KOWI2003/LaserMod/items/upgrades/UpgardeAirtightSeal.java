package KOWI2003.LaserMod.items.upgrades;

import KOWI2003.LaserMod.items.ItemUpgradeBase;
import KOWI2003.LaserMod.items.interfaces.IExtendable;
import KOWI2003.LaserMod.utils.LaserItemUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class UpgardeAirtightSeal extends ItemUpgradeBase {

	public UpgardeAirtightSeal(String name, String[] AbilityNames, float[] AbilityNameColor) {
		super(name, AbilityNames, AbilityNameColor);
		setUsefullForArmor(true, false, false, false);
	}
	
	@Override
	public void runOnArmorTick(ItemStack stack, World level, PlayerEntity player, EquipmentSlotType slot) {
		if(slot == EquipmentSlotType.HEAD) 
		{
			boolean isExtended = true;
			if(stack.getItem() instanceof IExtendable)
				isExtended = LaserItemUtils.isExtended(stack);
			if(isExtended)
				player.setAirSupply(player.getMaxAirSupply());
		}
	}
	
}
