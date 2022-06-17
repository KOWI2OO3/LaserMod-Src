package KOWI2003.LaserMod.items.render.model;

import KOWI2003.LaserMod.items.ItemLaserArmorBase;
import KOWI2003.LaserMod.utils.LaserItemUtils;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;

public class LaserArmorModelHandler {

	public static <T extends LivingEntity> BipedModel<T> getModel(ItemStack stack, BipedModel<T> _default) {
		if(!(stack.getItem() instanceof ItemLaserArmorBase))
			return _default;
		ItemLaserArmorBase armorpiece = (ItemLaserArmorBase)stack.getItem();
		boolean isActive = LaserItemUtils.isExtended(stack);
		EquipmentSlotType slot = armorpiece.getEquipmentSlot(stack);
		
		LaserArmorModel<T> model = new LaserArmorModel<T>(stack);
		model.HelmetVisible = slot == EquipmentSlotType.HEAD;
		model.HelmetActive.visible = slot == EquipmentSlotType.HEAD && isActive;
		
		_default.copyPropertiesTo(model);
		return model;
	}
}
