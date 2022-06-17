package KOWI2003.LaserMod.items;

import java.util.Set;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.Multimap;

import KOWI2003.LaserMod.LaserProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemLaserToolBase extends LaserItem {
	
	protected final Set<Block> blocks;
	private Multimap<Attribute, AttributeModifier> defaultModifiers;
	
	public ItemLaserToolBase(Properties properties, Set<Block> blocks, float speed, float damageBaseline, int maxCharge) {
		super(properties);
		this.blocks = blocks;
		this.defaultProperties.setProperty(LaserProperties.Properties.DAMAGE, damageBaseline);
		this.defaultProperties.setProperty(LaserProperties.Properties.SPEED, speed);
		Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", (double)damageBaseline, AttributeModifier.Operation.ADDITION));
		builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", (double)speed, AttributeModifier.Operation.ADDITION));
		this.defaultModifiers = builder.build();
		this.maxCharge = maxCharge;
	}
	
	@Override
	public ActionResult<ItemStack> use(World world, PlayerEntity entity, Hand hand) {
		ItemStack stack = entity.getItemInHand(hand);
		if(getCharge(stack) > 0)
			stack = setExtended(stack, !isExtended(stack));
		else
			return super.use(world, entity, hand);
		entity.setItemInHand(hand, stack);
		return ActionResult.pass(entity.getItemInHand(hand));
	}
	
	@Override
	public boolean canBeUsed(ItemUpgradeBase upgrade) {
		return upgrade.isUsefullForLaserTool();
	}
	
	@Override
	public int getItemStackLimit(ItemStack stack) {
		return 1;
	}

	@Override
	public String[] getAbilityNames(ItemUpgradeBase upgrade) {
		return upgrade.getToolAbilityNames();
	}
	
	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
		if(isExtended(stack) && slot == EquipmentSlotType.MAINHAND) {
			Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
			builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", (double)getProperties(stack).getProperty(KOWI2003.LaserMod.LaserProperties.Properties.DAMAGE), AttributeModifier.Operation.ADDITION));
			builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", (double)getProperties(stack).getProperty(KOWI2003.LaserMod.LaserProperties.Properties.SPEED), AttributeModifier.Operation.ADDITION));
			this.defaultModifiers = builder.build();
			return defaultModifiers;
		}
		return super.getAttributeModifiers(slot, stack);
	}
	
	@Override
	public boolean mineBlock(ItemStack stack, World world, BlockState state, BlockPos pos,
			LivingEntity entity) {
		if(isExtended(stack)) {
			if (!world.isClientSide && state.getDestroySpeed(world, pos) != 0.0F) {
				LaserProperties properties = getProperties(stack);
				properties.getUpgrades().forEach((upgrade) -> {
					upgrade.runLaserToolBlockBreak(stack, pos, state, entity);
				});
				damage(stack, 1, entity, (p_220039_0_) -> {
					  p_220039_0_.broadcastBreakEvent(EquipmentSlotType.MAINHAND); });
		      }
		    return true;
		}
		return false;
	}
	
	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity enemy, LivingEntity player) {
		if(isExtended(stack)) {
			LaserProperties properties = getProperties(stack);
			properties.getUpgrades().forEach((upgrade) -> {
				upgrade.runLaserToolHitEnemy(stack, enemy, player);
			});
			
			damage(stack, 2, player, (entity) -> {
			  entity.broadcastBreakEvent(EquipmentSlotType.MAINHAND); });
		    return true;
		}
		return false;
	}

	public float getDestroySpeed(ItemStack stack, BlockState state) {
	  if(isExtended(stack)) {
	      if (getToolTypes(stack).stream().anyMatch(e -> state.isToolEffective(e))) return getProperties(stack).getProperty(KOWI2003.LaserMod.LaserProperties.Properties.SPEED) * 4;
	      return this.blocks.contains(state.getBlock()) ? getProperties(stack).getProperty(KOWI2003.LaserMod.LaserProperties.Properties.SPEED) * 4 : 1.0F;
	  }
	  return state.requiresCorrectToolForDrops() ? 0f : 1f;
	}
	
	@Override
	public boolean canHarvestBlock(ItemStack stack, BlockState state) {
		if(isExtended(stack)) {
			if(blocks.contains(state.getBlock()))
				return super.canHarvestBlock(stack, state);
			return super.canHarvestBlock(stack, state);
		}
		return super.canHarvestBlock(stack, state);
	}
	
	@Override
	public float[] getRGB(ItemStack stack, int tintindex) {
		return getColor(stack);
	}
}
