package KOWI2003.LaserMod.init;

import java.util.ArrayList;
import java.util.List;

import KOWI2003.LaserMod.MainMod;
import KOWI2003.LaserMod.Reference;
import KOWI2003.LaserMod.handlers.ColorHandler;
import KOWI2003.LaserMod.items.ItemDefault;
import KOWI2003.LaserMod.items.ItemIRGlasses;
import KOWI2003.LaserMod.items.ItemLaserArmorBase;
import KOWI2003.LaserMod.items.ItemLaserArmorBase.LaserArmorMaterial;
import KOWI2003.LaserMod.items.ItemLaserDirector;
import KOWI2003.LaserMod.items.ItemLaserTool;
import KOWI2003.LaserMod.items.ItemLinker;
import KOWI2003.LaserMod.items.tools.ItemLaserToolOpend;
import KOWI2003.LaserMod.items.tools.ToolLaserAxe;
import KOWI2003.LaserMod.items.tools.ToolLaserHoe;
import KOWI2003.LaserMod.items.tools.ToolLaserPickaxe;
import KOWI2003.LaserMod.items.tools.ToolLaserShovel;
import KOWI2003.LaserMod.items.tools.ToolLaserSword;
import KOWI2003.LaserMod.utils.LaserItemUtils;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Reference.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModItems {

	public static List<Item> hidenItems = new ArrayList<>();
	public static final List<ItemStack> tabStacks = new ArrayList<ItemStack>();
	private static final List<Item> ITEMS = new ArrayList<>();
	
	public static final Item LaserCrystal = registerItem("laser_crystal", new ItemDefault());
	public static final Item LaserToolShell = registerItem("laser_tool_base", new ItemLaserTool());
	
	public static final Item LaserSword = registerItem("laser_sword", new ToolLaserSword(new Item.Properties().tab(MainMod.blocks), 8f, 6f, 1000));
	public static final Item LaserPickaxe = registerItem("laser_pickaxe", new ToolLaserPickaxe(new Item.Properties().tab(MainMod.blocks), 8f, 3f, 1000, 2));
	public static final Item LaserAxe = registerItem("laser_axe", new ToolLaserAxe(new Item.Properties().tab(MainMod.blocks), 8f, 3f, 1000));
	public static final Item LaserShovel = registerItem("laser_shovel", new ToolLaserShovel(new Item.Properties().tab(MainMod.blocks), 8f, 3f, 1000));
	public static final Item LaserHoe = registerItem("laser_hoe", new ToolLaserHoe(new Item.Properties().tab(MainMod.blocks), 8f, 6f, 1000));
	
	public static final Item LaserHelmet = registerItem("laser_helmet", new ItemLaserArmorBase(new LaserArmorMaterial(), EquipmentSlotType.HEAD, new Item.Properties().tab(MainMod.blocks)));
	
	public static final Item IR_Glasses = registerItem("ir_glasses", new ItemIRGlasses());
	public static final Item Linker = registerItem("linker", new ItemLinker());
	public static final Item LaserDirector = registerItemHidden("laser_director", new ItemLaserDirector());
    
	public static final Item Silicon = registerItem("silicon", new ItemDefault());
	public static final Item SiliconBase = registerItem("silicon_plate", new ItemDefault());
	public static final Item CircuitBoard = registerItem("printed_circuit_board", new ItemDefault());
	
	public static final Item LaserToolOpened = registerItemHidden("hi1-lto", new ItemLaserToolOpend()); //Hidden Item 1 - Laser Tool Opened
	public static final Item LinkerModel = registerItemHidden("hi2-l", new ItemDefault(new Item.Properties())); //Hidden Item 2 - Linker Model
	
	private static Item registerItemHidden(String name, Item item)
    {
        hidenItems.add(item);
    	return registerItem(name, item);
    }
    
    private static Item registerItem(String name, Item item)
    {
    	item.setRegistryName(new ResourceLocation(Reference.MODID, name));
        ITEMS.add(item);
        return item;
    }
    
	@SubscribeEvent
	public static void registerItems(final RegistryEvent.Register<Item> event) {
		ITEMS.forEach(item -> {
			event.getRegistry().register(item);
			if(!ModItems.hidenItems.contains(item))
				tabStacks.add(item.getDefaultInstance());
		});
		ITEMS.clear();
	}
	
	public static void registerPropertyOverrides(FMLClientSetupEvent event) {
		event.enqueueWork(() ->
  	  	{
			ItemModelsProperties.register(ModItems.LaserPickaxe, 
			      new ResourceLocation(Reference.MODID, "active"), (stack, world, living) -> {
			        return LaserItemUtils.isExtended(stack) ? 0.0F : 1.0F;
			      });
			ItemModelsProperties.register(ModItems.LaserSword, 
			      new ResourceLocation(Reference.MODID, "active"), (stack, world, living) -> {
			        return LaserItemUtils.isExtended(stack) ? 0.0F : 1.0F;
			      });
			ItemModelsProperties.register(ModItems.LaserHoe, 
			      new ResourceLocation(Reference.MODID, "active"), (stack, world, living) -> {
			        return LaserItemUtils.isExtended(stack) ? 0.0F : 1.0F;
			      });
			ItemModelsProperties.register(ModItems.LaserAxe, 
			      new ResourceLocation(Reference.MODID, "active"), (stack, world, living) -> {
			        return LaserItemUtils.isExtended(stack) ? 0.0F : 1.0F;
			      });
			ItemModelsProperties.register(ModItems.LaserShovel, 
			      new ResourceLocation(Reference.MODID, "active"), (stack, world, living) -> {
			        return LaserItemUtils.isExtended(stack) ? 0.0F : 1.0F;
			      });
			ItemModelsProperties.register(ModItems.LaserHelmet, 
				      new ResourceLocation(Reference.MODID, "active"), (stack, world, living) -> {
				        return LaserItemUtils.isExtended(stack) ? 1.0F : 0.0F;
				      });
  	  	});
	}
	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void registerBlockColors(ColorHandlerEvent.Item event){
		event.getItemColors().register(new ColorHandler.Item(), ModItems.LaserPickaxe, ModItems.LaserSword, ModItems.LaserHoe, 
				ModItems.LaserAxe, ModItems.LaserShovel, ModItems.LaserToolOpened, ModItems.LaserToolShell, ModItems.LaserHelmet);
	}
}
