package KOWI2003.LaserMod.items.tools;

import KOWI2003.LaserMod.LaserProperties;
import KOWI2003.LaserMod.items.ItemLaserToolBase;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.TierSortingRegistry;

public class ToolLaserPickaxe extends ItemLaserToolBase {
	int miningLevel = 0;
	
	public ToolLaserPickaxe(Properties properties, float speed, float damageBaseline,
			int maxCharge, int miningLevel) {
		super(properties, BlockTags.MINEABLE_WITH_PICKAXE, speed, damageBaseline, maxCharge);
		this.miningLevel = miningLevel;
	}
	
	@Override
	public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
	      int i = miningLevel;
	      return isExtended(stack) ? TierSortingRegistry.isCorrectTierForDrops(new Tier() {
				
				@Override
				public int getUses() {
					return 10;
				}
				
				@Override
				public float getSpeed() {
					return getDestroySpeed(stack, state);
				}
				
				@Override
				public Ingredient getRepairIngredient() {
					return null;
				}
				
				@Override
				public int getLevel() {
					return i;
				}
				
				@Override
				public int getEnchantmentValue() {
					return 0;
				}
				
				@Override
				public float getAttackDamageBonus() {
					return 0;
				}
			}, state) : false;
//	      Material material = state.getMaterial();
//	      return material == Material.STONE || material == Material.METAL || material == Material.HEAVY_METAL;
	}
	
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		if(isExtended(stack)) {
			Material material = state.getMaterial();
			return state.is(BlockTags.MINEABLE_WITH_PICKAXE) ? getProperties(stack).getProperty(LaserProperties.Properties.SPEED) : super.getDestroySpeed(stack, state);
		}
		return state.requiresCorrectToolForDrops() ? 0f : 1f;
	}
}
