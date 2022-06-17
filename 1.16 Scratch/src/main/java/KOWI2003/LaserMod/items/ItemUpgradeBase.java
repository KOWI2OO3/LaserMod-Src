package KOWI2003.LaserMod.items;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import KOWI2003.LaserMod.LaserProperties;
import KOWI2003.LaserMod.MainMod;
import KOWI2003.LaserMod.Reference;
import KOWI2003.LaserMod.init.ModBlocks;
import KOWI2003.LaserMod.tileentities.ILaserAccess;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class ItemUpgradeBase extends  ItemDefault implements SubItems {

	private String UpgradeName;
	protected boolean stackable = false;
	private String resLoc = StringUtils.EMPTY;
	
	protected String[] AbilityNames = new String[] {};
	protected float[] abilityNameColor = new float[] {0.35f, 0.35f, 0.35f};
	
	protected List<Block> USEFULL_MACHINES = new ArrayList<Block>();
	
	private boolean CanBeUsedForLaser = false;
	private boolean CanBeUsedForLaserTools = false;
	private boolean[] CanBeUsedForLaserArmor = {false, false, false, false};
	
	private int tier = -1;
	
	public ItemUpgradeBase(boolean usefullForTools, Block... blocks) {
		this(blocks);
		setCanBeUsedForLaserTools(usefullForTools);
	}
	
	public ItemUpgradeBase(boolean usefullForTools, boolean[] usefullForArmor, String name, String[] AbilityNames, float[] AbilityNameColor) {
		this(AbilityNames, AbilityNameColor);
		this.setName(name);
		CanBeUsedForLaserArmor = usefullForArmor;
		setCanBeUsedForLaserTools(usefullForTools);
	}
	
	public ItemUpgradeBase(boolean usefullForTools, String[] AbilityNames, float[] AbilityNameColor, Block... blocks) {
		this(AbilityNames, AbilityNameColor, blocks);
		setCanBeUsedForLaserTools(usefullForTools);
	}
	
	public ItemUpgradeBase(Block... blocks) {
		super(new Item.Properties().tab(MainMod.upgrades));
		for (Block block : blocks) {
			if(block == ModBlocks.AdvancedLaser || block == ModBlocks.Laser) {
				setCanBeUsedForLaser(true);
			}else 
				USEFULL_MACHINES.add(block);
		}
	}
	
	public ItemUpgradeBase(String[] AbilityNames, float[] AbilityNameColor, Block... blocks) {
		this(new Item.Properties().tab(MainMod.upgrades), AbilityNames, AbilityNameColor);
		for (Block block : blocks) {
			if(block == ModBlocks.AdvancedLaser || block == ModBlocks.Laser) {
				setCanBeUsedForLaser(true);
			}else 
				USEFULL_MACHINES.add(block);
		}
	}
	
	public ItemUpgradeBase(Properties properties) {
		super(properties);
	}
	
	public ItemUpgradeBase(Properties properties, String[] AbilityNames, float[] AbilityNameColor) {
		super(properties);
		this.AbilityNames = AbilityNames;
		this.abilityNameColor = AbilityNameColor;
	}
	
	public String[] getArmorAbilityNames() {
		return getAbilityNames();
	}
	
	public String[] getToolAbilityNames() {
		return getAbilityNames();
	}
	
	@Override
	public int getItemStackLimit(ItemStack stack) {
		return 1;
	}
	
	public ItemUpgradeBase(String name) {
		this();
		this.setName(name);
	}
	
	public ItemUpgradeBase(String name, String[] AbilityNames, float[] AbilityNameColor) {
		this(AbilityNames, AbilityNameColor);
		this.setName(name);
	}
	
	public ItemUpgradeBase(String name, int tier) {
		this();
		this.tier = tier;
		this.setName(name + "_" + tier);
	}
	
	public ItemUpgradeBase(String name, int tier, String[] AbilityNames, float[] AbilityNameColor) {
		this(AbilityNames, AbilityNameColor);
		this.tier = tier;
		this.setName(name + "_" + tier);
	}
	
	public ItemUpgradeBase(String name, int tier, boolean stackable) {
		this(name, tier);
		this.stackable = stackable;
	}
	
	public ItemUpgradeBase(String name, int tier, boolean stackable, String[] AbilityNames, float[] AbilityNameColor) {
		this(name, tier, AbilityNames, AbilityNameColor);
		this.stackable = stackable;
	}
	
	public ItemUpgradeBase(int tier) {
		this();
		this.tier = tier;
	}
	
	public ItemUpgradeBase(int tier, String[] AbilityNames, float[] AbilityNameColor) {
		this(AbilityNames, AbilityNameColor);
		this.tier = tier;
	}
	
	public ItemUpgradeBase(int tier, boolean stackable) {
		this(tier);
		this.stackable = stackable;
	}
	
	public ItemUpgradeBase(int tier, boolean stackable, String[] AbilityNames, float[] AbilityNameColor) {
		this(tier, AbilityNames, AbilityNameColor);
		this.stackable = stackable;
	}
	
	public boolean hasTier() {
		return tier >= 0;
	}
	
	public boolean isUsefullForLaser() {
		return CanBeUsedForLaser;
	}
	
	public boolean isUsefullForLaserTool() {
		return CanBeUsedForLaserTools;
	}
	
	public boolean isUsefullForLaserArmor(int index) {
		return (index >= 0 && index <= 3) ? CanBeUsedForLaserArmor[index] : false;
	}
	
	public boolean isUsefullForMachine(Block Machine) {
		return USEFULL_MACHINES.contains(Machine);
	}
	
	protected void setResourceLocation(String id) {
		this.resLoc = id;
	}
	
	protected void clearResourceLocation() {
		this.resLoc = StringUtils.EMPTY;
	}
	
	public String getUpgradeName() {
		return UpgradeName;
	}
	
	public void setName(String upgradeName) {
		UpgradeName = upgradeName;
	}
	
	public String getUpgradeBaseName() {
		if(UpgradeName.contains("_")) {
			String[] ss = UpgradeName.split("_");
			if(ss.length > 0)
				return ss[0];
		}
		return UpgradeName;
	}
	
	@Override
	public ITextComponent getName(ItemStack stack) {
		if(hasTier())
				return new TranslationTextComponent("item." + Reference.MODID + ".upgrade_" + getUpgradeBaseName());
		return super.getName(stack);
	}
	
	public int getTier() {
		return tier;
	}
	
	public int getTierOr(int other) {
		if(hasTier())
			return getTier();
		return other;
	}
	
	protected CompoundNBT readFromNBT(ItemStack stack) {
		return stack.getTag();
	}
	
	public void writeToNBT(CompoundNBT nbt, ItemStack stack) {
		stack.save(nbt);
	}
	
	public float getMultiplier(LaserProperties.Properties property) {
		return 1;
	}
	
	public void runLaserBlock(ILaserAccess te, BlockPos pos) {}
	
	public void runLaserToolBlockBreak(ItemStack item, BlockPos pos, BlockState state, LivingEntity player) {}
	public void runLaserToolHitEnemy(ItemStack item, LivingEntity enemy, LivingEntity player) {}

	public void runOnEntityHitArmor(ItemStack item, LivingEntity attacker, LivingEntity player, float damageAmount) {}
	public void runOnHurtWithArmor(ItemStack item, LivingEntity attacker, LivingEntity player) {}
	public void runOnArmorTick(ItemStack stack, World level, PlayerEntity player, EquipmentSlotType slot) {}
	
	@Override
	public void appendHoverText(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		ITextComponent text = new TranslationTextComponent("r");
		Style st = text.getStyle().applyFormat(TextFormatting.GRAY);
		
		if(hasTier()) {
			tooltip.add(new TranslationTextComponent("item.upgrade.tooltip.tier", tier).plainCopy().setStyle(st));
		}
		
		tooltip.add(new TranslationTextComponent("item.upgrade.tooltip").plainCopy().setStyle(st));
		if(this.isUsefullForLaser())
			tooltip.add(new TranslationTextComponent("block.lasermod.laser").plainCopy().setStyle(st));
		if(this.isUsefullForLaserTool())
			tooltip.add(new TranslationTextComponent("item.upgrade.tooltip.laser_tool").plainCopy().setStyle(st));
		
		if(canBeUsedForAnyArmor()) {
			if(canBeUsedOnAllArmor()) {
				tooltip.add(new TranslationTextComponent("item.upgrade.tooltip.laser_armor").plainCopy().setStyle(st));
			}else {
				if(this.isUsefullForLaserArmor(0))
					tooltip.add(new TranslationTextComponent("item.upgrade.tooltip.laser_helmet").plainCopy().setStyle(st));
				if(this.isUsefullForLaserArmor(1))
					tooltip.add(new TranslationTextComponent("item.upgrade.tooltip.laser_chestplate").plainCopy().setStyle(st));
				if(this.isUsefullForLaserArmor(2))
					tooltip.add(new TranslationTextComponent("item.upgrade.tooltip.laser_leggings").plainCopy().setStyle(st));
				if(this.isUsefullForLaserArmor(3))
					tooltip.add(new TranslationTextComponent("item.upgrade.tooltip.laser_boots").plainCopy().setStyle(st));
			}
		}
		
		if(!USEFULL_MACHINES.isEmpty()) {
			for(int i = 0; i < USEFULL_MACHINES.size(); i++) {
				tooltip.add(USEFULL_MACHINES.get(i).getName().plainCopy().setStyle(st));
			}
		}
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
	}
	
	public void getMachineUse(Block Machine) {
		
	}
	
	public final String[] getAbilityNames() {
		return AbilityNames;
	}
	
	public final float[] getAbilityNameColor() {
		return abilityNameColor;
	}
	
	@Override
	public NonNullList<ResourceLocation> getModels() {
		NonNullList<ResourceLocation> modelLocations = NonNullList.create();
		
		if(this.resLoc == StringUtils.EMPTY)
			modelLocations.add(new ResourceLocation(Reference.MODID, "chip"));
		else
			modelLocations.add(new ResourceLocation(Reference.MODID, "upgrade_" + resLoc));
		
		return modelLocations;
	}
	
	
	final static String[] tierLevelDisplay = new String[] {"", "I", "II", "III", "IV", "V", "VI", "VII", "IIX", "IX", "X" };
	
	public String getTierLevelForAbilityName() {
		if(!hasTier())
			return "";
		if(getTier() >= tierLevelDisplay.length)
			return "" + getTier();
		return tierLevelDisplay[getTier()];
	}

	public boolean canBeUsedForAnyArmor() {
		return isUsefullForLaserArmor(0) || isUsefullForLaserArmor(1) || isUsefullForLaserArmor(2) || isUsefullForLaserArmor(3);
	}
	
	public boolean canBeUsedOnAllArmor() {
		for(int i = 0; i < 4; i++) 
			if(!isUsefullForLaserArmor(i))
				return false;
		return true;
	}

	public void setUsefullForArmor(int index, boolean canbeUsed) { if(index >= 0 && index <= 3) CanBeUsedForLaserArmor[index] = canbeUsed; }
	public void setUsefullForArmor(boolean canbeUsedForHelmet, boolean canbeUsedForChestplate, boolean canbeUsedForLeggings, boolean canbeUsedForBoots) 
	{ CanBeUsedForLaserArmor = new boolean[] {canbeUsedForHelmet, canbeUsedForChestplate, canbeUsedForLeggings, canbeUsedForBoots}; }
	public void setCanBeUsedForLaserArmor(boolean canBeUsedForLaserArmor) { CanBeUsedForLaserArmor = new boolean[] {canBeUsedForLaserArmor, canBeUsedForLaserArmor, canBeUsedForLaserArmor, canBeUsedForLaserArmor}; }
	
	
	public void setCanBeUsedForLaser(boolean canBeUsedForLaser) { CanBeUsedForLaser = canBeUsedForLaser; }
	public void setCanBeUsedForLaserTools(boolean canBeUsedForLaserTools) { CanBeUsedForLaserTools = canBeUsedForLaserTools; }
	
}
