package KOWI2003.LaserMod.init;

import java.util.ArrayList;
import java.util.List;

import KOWI2003.LaserMod.Reference;
import KOWI2003.LaserMod.items.ItemUpgradeBase;
import KOWI2003.LaserMod.items.upgrades.UpgardeAirtightSeal;
import KOWI2003.LaserMod.items.upgrades.UpgradeDamage;
import KOWI2003.LaserMod.items.upgrades.UpgradeDistance;
import KOWI2003.LaserMod.items.upgrades.UpgradeFire;
import KOWI2003.LaserMod.items.upgrades.UpgradeMining;
import KOWI2003.LaserMod.items.upgrades.UpgradeMove;
import KOWI2003.LaserMod.items.upgrades.UpgradeSpeed;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Reference.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModUpgrades {

	private static final List<ItemUpgradeBase> UPGARDES = new ArrayList<>();
	public static final NonNullList<ItemStack> tabStacks = NonNullList.create();
	
	public static final Item Speed = registerItem(new UpgradeSpeed("speed"));
	public static final Item Mining = registerItem(new UpgradeMining("mining"));
	public static final Item Fire = registerItem(new UpgradeFire("fire"));
	public static final Item Color = registerItem(new ItemUpgradeBase("color", new String[] {"Colorable"}, new float[] {0.5f,  0.0f, 0.5f}) {
		public boolean isUsefullForLaser() {return true;};
		public boolean isUsefullForLaserTool() { return true; };
		public boolean isUsefullForLaserArmor(int index) {
			return true;
		};
	});
	public static final Item Mode = registerItem(new ItemUpgradeBase("mode") {
		public boolean isUsefullForLaser() {return true;};
	});
	
	public static final Item Damage1 = registerItem(new UpgradeDamage("damage", 1));
	public static final Item Damage2 = registerItem(new UpgradeDamage("damage", 2));
	public static final Item Damage3 = registerItem(new UpgradeDamage("damage", 3));
	public static final Item Damage4 = registerItem(new UpgradeDamage("damage", 4));
	public static final Item Damage5 = registerItem(new UpgradeDamage("damage", 5) {
		public float getMultiplier(KOWI2003.LaserMod.LaserProperties.Properties property) {
			if(property == KOWI2003.LaserMod.LaserProperties.Properties.DAMAGE)
				return 22.5f;
			return super.getMultiplier(property);
		};
	});
	
	public static final Item NoDamage = registerItem(new ItemUpgradeBase("no_damage") {
		public boolean isUsefullForLaser() { return true; };
		public float getMultiplier(KOWI2003.LaserMod.LaserProperties.Properties property) {
			if(property == KOWI2003.LaserMod.LaserProperties.Properties.DAMAGE)
				return 0;
			return super.getMultiplier(property);
		};
	});
	
	public static final Item Push = registerItem(new UpgradeMove("push", true));
	public static final Item Pull = registerItem(new UpgradeMove("pull", false));
	public static final Item Distance = registerItem(new UpgradeDistance("distance", 1));
	public static final Item Distance2 = registerItem(new UpgradeDistance("distance", 2));
	public static final Item Distance3 = registerItem(new UpgradeDistance("distance", 3));

	public static final Item AirtightSeal = registerItem(new UpgardeAirtightSeal("airtight_seal", new String[] {"Airtight Seal"}, new float[] {0.58f,  0.0f, 0.827f}));
	
	public static List<String> getAllNames() {
		List<String> list = new ArrayList<String>();
		for (ItemUpgradeBase upgrade : UPGARDES) {
			list.add(upgrade.getUpgradeName());
		}
		return list;
	}
	
	public static ItemUpgradeBase getUpgradeByName(String upgradeName) {
		for (ItemUpgradeBase upgrade : UPGARDES) {
			if(upgrade.getUpgradeName().equals(upgradeName))
				return upgrade;
		}
		return null;
	}
	
	public static ItemUpgradeBase getUpgradeByBaseName(String upgradeName) {
		for (ItemUpgradeBase upgrade : UPGARDES) {
			if(upgrade.getUpgradeBaseName().equals(upgradeName))
				return upgrade;
		}
		return null;
	}
	
	private static Item registerItemHidden(String name, ItemUpgradeBase item)
    {
        ModItems.hidenItems.add(item);
    	return registerItem(item);
    }
    
    private static Item registerItem(ItemUpgradeBase item)
    {
    	item.setRegistryName(new ResourceLocation(Reference.MODID, "upgrade_" + item.getUpgradeName()));
    	UPGARDES.add(item);
    	tabStacks.add(new ItemStack(item));
        return item;
    }
    
	@SubscribeEvent
	public static void registerItems(final RegistryEvent.Register<Item> event) {
		UPGARDES.forEach(item -> event.getRegistry().register(item));
		//UPGARDES.clear();
	}
	
	public static void registerUpgrade(ItemUpgradeBase upgrade) {
		UPGARDES.add(upgrade);
	}
	
	public static void removeUpgrade(ItemUpgradeBase item) {
		UPGARDES.remove(item);
	}
}
