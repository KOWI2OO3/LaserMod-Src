package KOWI2003.LaserMod.items;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.Multimap;

import KOWI2003.LaserMod.LaserProperties;
import KOWI2003.LaserMod.init.ModKeybindings;
import KOWI2003.LaserMod.items.interfaces.IChargable;
import KOWI2003.LaserMod.items.interfaces.IExtendable;
import KOWI2003.LaserMod.items.interfaces.ILaserUpgradable;
import KOWI2003.LaserMod.items.render.model.LaserArmorModelHandler;
import KOWI2003.LaserMod.network.PacketHandler;
import KOWI2003.LaserMod.network.PacketSyncArmor;
import KOWI2003.LaserMod.utils.Utils;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponent;
import net.minecraft.world.World;

public class ItemLaserArmorBase extends ArmorItem implements ILaserUpgradable, IChargable, IExtendable {
	private static final UUID[] ARMOR_MODIFIER_UUID_PER_SLOT = new UUID[]{UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"), UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"), UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"), UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150")};
	
   	protected final EquipmentSlotType slot;
   	IArmorMaterial material;
	protected LaserProperties defaultProperties;

	protected int maxCharge = 1000;
	
	public ItemLaserArmorBase(IArmorMaterial material, EquipmentSlotType slot, Properties properties) {
		super(material, slot, properties);
		this.slot = slot;
		this.material = material;
		this.defaultProperties = new LaserProperties();
		this.defaultProperties.setProperty(LaserProperties.Properties.DEFENCE, getDefense());;
		this.defaultProperties.setProperty(LaserProperties.Properties.TOUGHNESS, getToughness());
	}
	
	static boolean isDown = false;
	
	@Override
	public void onArmorTick(ItemStack stack, World level, PlayerEntity player) {
		LaserProperties properties = getProperties(stack);
		for(ItemUpgradeBase upgrade : properties.getUpgrades())
			upgrade.runOnArmorTick(stack, level, player, slot);
		
		if(level.isClientSide)
		{
			boolean keyDown = ModKeybindings.ArmorToggle.isDown();
			if(keyDown && !isDown) {
				//Toggle Armor!
				boolean isAnyActive = false;
				boolean isAnyDeactive = false;
				for(ItemStack armor : player.getArmorSlots())
				{
					if(armor.getItem() instanceof IExtendable) {
						if(isExtended(stack)) {
							isAnyActive = true;
						}else
							isAnyDeactive = true;
					}
				}
				
//				boolean newState = isAnyActive == isAnyDeactive ? !isAnyActive : isAnyDeactive;
				boolean newState = isAnyDeactive;
				
				for(ItemStack armor : player.getArmorSlots())
				{
					if(armor.getItem() instanceof IExtendable) {
						armor = setExtended(armor, newState);
						PacketHandler.sendToServer(new PacketSyncArmor(newState, armor.getEquipmentSlot()));
					}
				}
				
				isDown = true;
			}else if(isDown && !keyDown)
				isDown = false;
		}
		
		super.onArmorTick(stack, level, player);
	}
	
	
	@Override
	public int getMaxCharge() {
		return maxCharge;
	}
	
	@Override
	public boolean canBeUsed(ItemUpgradeBase upgrade) {
		return upgrade.isUsefullForLaserArmor(3-slot.getIndex());
	}
	
	@Override
	public int getItemStackLimit(ItemStack stack) {
		return 1;
	}

	@Override
	public IArmorMaterial getMaterial() {
		return ArmorMaterial.IRON;
	}
	
	@Override
	public EquipmentSlotType getEquipmentSlot(ItemStack stack) {
		return slot;
	}
	
	@Override
	public EquipmentSlotType getSlot() {
		return slot;
	}
	
	@Override
	public int getEnchantmentValue() {
		return 0;
	}
	
	@Override
	public boolean isValidRepairItem(ItemStack stack, ItemStack repairItem) {
		return false;
	}

	public int getDefense() {
		return this.material.getDefenseForSlot(getSlot());
	}

	public float getToughness() {
		return this.material.getToughness();
	}
	
	@Nullable
	public SoundEvent getEquipSound() {
		return this.material.getEquipSound();
	}
	
	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		return false;
	}
	
	@Override
	public boolean isEnchantable(ItemStack stack) {
		return false;
	}
	
	@Override
	public ItemStack getDefaultInstance() {
		ItemStack stack = new ItemStack(this);
		stack = setExtended(stack, false);
		stack = setCharge(stack, maxCharge);
		stack = setColor(stack, 1.0f, 0f, 0f);
		stack = setProperties(stack, defaultProperties);
		return stack;
	}
	
//	@Override
//	public int getBarWidth(ItemStack stack) {
//		return (int) Math.round((1d - getDurabilityForDisplay(stack))*13);
//	}
	
	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		return 1d - (double)getCharge(stack)/(double)maxCharge;
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return getCharge(stack) < maxCharge;
	}
	
	@Override
	public int getRGBDurabilityForDisplay(ItemStack stack) {
		return MathHelper.hsvToRgb(Math.max(0.0F, (float) (1.0F - getDurabilityForDisplay(stack))) / 3.0F, 1.0F, 1.0F);
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
	public <A extends BipedModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack,
			EquipmentSlotType armorSlot, A _default) {
		return (A) LaserArmorModelHandler.getModel(itemStack, _default);
	}
	
	@Override
	public String[] getAbilityNames(ItemUpgradeBase upgrade) {
		return upgrade.getArmorAbilityNames();
	}
	
	@Override
	public void appendHoverText(ItemStack stack, World world, List<ITextComponent> tooltip,
			ITooltipFlag flags) {
		TextComponent charge = new StringTextComponent("Charge: " + getCharge(stack) + "/" + maxCharge);
		charge.setStyle(charge.getStyle().withColor(Color.fromRgb(Utils.getHexIntFromRGB(0.35f, 0.35f, 0.35f))));
		tooltip.add(charge);

		boolean debug = false;
		if(debug) {
			tooltip.add(new StringTextComponent(isExtended(stack)+""));
			float[] color = getColor(stack);
			tooltip.add(new StringTextComponent(color[0]+", " + color[1] + ", " + color[2]));
		}
		
		LaserProperties properties = getProperties(stack);
		
		if(properties.hasUpgradeWithAbilityName()) {
			tooltip.add(new StringTextComponent(""));
			tooltip.add(new StringTextComponent("Abilities: ").setStyle(new StringTextComponent("").getStyle().withColor(Color.fromRgb(Utils.getHexIntFromRGB(0.7f, 0.7f, 0.7f)))));
			for (ItemUpgradeBase upgrade : properties.getUpgrades()) {
				if(getAbilityNames(upgrade).length > 0) {
					float[] color = upgrade.getAbilityNameColor();
					for(String abilityName : getAbilityNames(upgrade)) {
						StringTextComponent comp = new StringTextComponent(abilityName);
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
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
		return slot == this.slot && isExtended(stack) ? getAttributeModifiers(getProperties(stack)) : getEmptyModifiers();
	}
	
	private Multimap<Attribute, AttributeModifier> getEmptyModifiers() {
		Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		return builder.build();
	}
	
	private Multimap<Attribute, AttributeModifier> getAttributeModifiers(LaserProperties properties) {
		//TODO add chaching for the modifiers;
		UUID uuid = ARMOR_MODIFIER_UUID_PER_SLOT[getSlot().getIndex()];
		Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(Attributes.ARMOR, new AttributeModifier(uuid, "Armor modifier", this.getDefense(), AttributeModifier.Operation.ADDITION));
		builder.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(uuid, "Armor toughness", (double)this.getToughness(), AttributeModifier.Operation.ADDITION));
		double knockbackResistance = (double)this.getMaterial().getKnockbackResistance();
		if (knockbackResistance > 0) {
			builder.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(uuid, "Armor knockback resistance", knockbackResistance, AttributeModifier.Operation.ADDITION));
		}
		return builder.build();
	}
	
	public float[] getRGB(ItemStack stack, int tintindex) {
		return tintindex == 0 ? getColor(stack) : new float[] {1.0f, 1.0f, 1.0f};
	}
	
	public static class LaserArmorMaterial implements IArmorMaterial {
		
		private static final int[] HEALTH_PER_SLOT = new int[]{13, 15, 16, 11};
		
		int durability;
		int[] defense;
		float toughness;
		float knockbackResistance;
		String name;
		SoundEvent sound;
		
		public LaserArmorMaterial() {
			this("Laser", 15, new int[] {2, 5, 6, 2}, SoundEvents.ARMOR_EQUIP_IRON, 0.0f, 0.0f);
		}
		
		public LaserArmorMaterial(String name, int durability, int[] defense, SoundEvent sound, float toughness, float knockbackResistance) {
			this.name = name;
			this.sound = sound;
			this.durability = durability;
			this.defense = defense;
			this.toughness = toughness;
			this.knockbackResistance = knockbackResistance;
		}
		
		@Override
		public int getDurabilityForSlot(EquipmentSlotType slot) { return HEALTH_PER_SLOT[slot.getIndex()] * durability; }
		@Override
		public int getDefenseForSlot(EquipmentSlotType slot) { return defense[slot.getIndex()]; }
		@Override
		public int getEnchantmentValue() { return 0; }
		@Override
		public SoundEvent getEquipSound() { return null; }
		@Override
		public Ingredient getRepairIngredient() { return null; }
		@Override
		public String getName() { return name; }
		@Override
		public float getToughness() { return toughness; }
		@Override
		public float getKnockbackResistance() { return knockbackResistance; }
		
	}
	
}