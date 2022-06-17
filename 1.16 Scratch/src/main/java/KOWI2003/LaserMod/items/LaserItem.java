package KOWI2003.LaserMod.items;

import java.util.List;
import java.util.function.Consumer;

import KOWI2003.LaserMod.LaserProperties;
import KOWI2003.LaserMod.items.interfaces.IChargable;
import KOWI2003.LaserMod.items.interfaces.IExtendable;
import KOWI2003.LaserMod.items.interfaces.ILaserUpgradable;
import KOWI2003.LaserMod.utils.LaserItemUtils;
import KOWI2003.LaserMod.utils.Utils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponent;
import net.minecraft.world.World;

public class LaserItem extends ItemDefault implements ILaserUpgradable, IChargable, IExtendable {

	protected LaserProperties defaultProperties;
	
	int maxCharge = 1000;
	
	public LaserItem(Properties properties, int maxCharge) {
		this(properties);
		this.maxCharge = maxCharge;
	}
	
	public LaserItem(Properties properties) {
		super(properties);
		defaultProperties = new LaserProperties();
	}

	@Override
	public int getMaxCharge() {
		return maxCharge;
	}
	
	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		return Math.abs(1d - (double)getCharge(stack)/(double)maxCharge);
	}
	
	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return getCharge(stack) < getMaxCharge();
	}
	
	@Override
	public int getRGBDurabilityForDisplay(ItemStack stack) {
		return MathHelper.hsvToRgb(Math.max(0.0F, (float) (1.0F - LaserItemUtils.getDurabilityForDisplay(stack))) / 3.0F, 1.0F, 1.0F);
	}
	
	public <T extends LivingEntity> void damage(ItemStack stack, int amount, T entity, Consumer<T> entityConsumer) {
		if (!entity.level.isClientSide && (!(entity instanceof PlayerEntity) || !((PlayerEntity)entity).isCreative())) {
			if(getCharge(stack) > 0 && isExtended(stack)) {
				stack = setCharge(stack, getCharge(stack) - amount);
				if(getCharge(stack) <= 0)
					setExtended(stack, false);
			}
	    }
	}
	
	@Override
	public void appendHoverText(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flags) {
		TextComponent charge = new StringTextComponent("Charge: " + getCharge(stack) + "/" + getMaxCharge());
		charge.setStyle(charge.getStyle().withColor(Color.fromRgb(Utils.getHexIntFromRGB(0.35f, 0.35f, 0.35f))));
		tooltip.add(charge);
		
		LaserProperties properties = getProperties(stack);
		
		if(properties.hasUpgradeWithAbilityName()) {
			tooltip.add(new StringTextComponent(""));
			tooltip.add(new StringTextComponent("Abilities: ").setStyle(new StringTextComponent("").getStyle().withColor(Color.fromRgb(Utils.getHexIntFromRGB(0.7f, 0.7f, 0.7f)))));
			for (ItemUpgradeBase upgrade : properties.getUpgrades()) {
				if(getAbilityNames(upgrade).length > 0) {
					float[] color = upgrade.getAbilityNameColor();
					for(String abilityName : getAbilityNames(upgrade)) {
						TextComponent comp = new StringTextComponent(abilityName);
						Style style = comp.getStyle();
						style = style.withColor(Color.fromRgb(Utils.getHexIntFromRGB(
								color.length > 0 ? color[0] : 0.35f,
								color.length > 1 ? color[1] : 0.35f,
								color.length > 2 ? color[2] : 0.35f)));
						comp.setStyle(style);
						tooltip.add(comp);
					}
				}
			}
		}
		
		super.appendHoverText(stack, world, tooltip, flags);
	}
	
	@Override
	public ItemStack getDefaultInstance() {
		ItemStack stack = new ItemStack(this);
		stack = setExtended(stack, false);
		stack = setCharge(stack, getMaxCharge());
		stack = setColor(stack, 1.0f, 0f, 0f);
		stack = setProperties(stack, defaultProperties);
		return stack;
	}
	
	@Override
	public float[] getRGB(ItemStack stack, int tintindex) {
		return getColor(stack);
	}

}
