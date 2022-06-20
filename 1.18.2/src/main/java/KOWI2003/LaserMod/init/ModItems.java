package KOWI2003.LaserMod.init;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

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
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = Reference.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModItems {

	public static List<RegistryObject<Item>> hidenItems = new ArrayList<>();
	public static final List<RegistryObject<Item>> tabStacks = new LinkedList<RegistryObject<Item>>();
//	private static final List<Item> ITEMS = new ArrayList<>();
	
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Reference.MODID);
	
	public static final RegistryObject<Item> LaserCrystal = register("laser_crystal", () -> new ItemDefault());
	public static final RegistryObject<Item> LaserToolShell = register("laser_tool_base", () -> new ItemLaserTool());
	
	public static final RegistryObject<Item> LaserSword = register("laser_sword", () -> new ToolLaserSword(new Item.Properties().tab(MainMod.blocks), 8f, 6f, 1000));
	public static final RegistryObject<Item> LaserPickaxe = register("laser_pickaxe", () -> new ToolLaserPickaxe(new Item.Properties().tab(MainMod.blocks), 8f, 3f, 1000, 2));
	public static final RegistryObject<Item> LaserAxe = register("laser_axe", () -> new ToolLaserAxe(new Item.Properties().tab(MainMod.blocks), 8f, 3f, 1000));
	public static final RegistryObject<Item> LaserShovel = register("laser_shovel", () -> new ToolLaserShovel(new Item.Properties().tab(MainMod.blocks), 8f, 3f, 1000));
	public static final RegistryObject<Item> LaserHoe = register("laser_hoe", () -> new ToolLaserHoe(new Item.Properties().tab(MainMod.blocks), 8f, 6f, 1000));
	
	public static final RegistryObject<Item> LaserHelmet = register("laser_helmet", () -> new ItemLaserArmorBase(new LaserArmorMaterial(), EquipmentSlot.HEAD, new Item.Properties().tab(MainMod.blocks)));
	public static final RegistryObject<Item> LaserChestplate = register("laser_chestplate", () -> new ItemLaserArmorBase(new LaserArmorMaterial(), EquipmentSlot.CHEST, new Item.Properties().tab(MainMod.blocks)));
	public static final RegistryObject<Item> LaserLeggings = register("laser_leggings", () -> new ItemLaserArmorBase(new LaserArmorMaterial(), EquipmentSlot.LEGS, new Item.Properties().tab(MainMod.blocks)));
	
	public static final RegistryObject<Item> IR_Glasses = register("ir_glasses", () -> new ItemIRGlasses());
	public static final RegistryObject<Item> Linker = register("linker", () -> new ItemLinker());
	public static final RegistryObject<Item> LaserDirector = registerHidden("laser_director", () -> new ItemLaserDirector());
    
	public static final RegistryObject<Item> Silicon = register("silicon", () -> new ItemDefault());
	public static final RegistryObject<Item> SiliconBase = register("silicon_plate", () -> new ItemDefault());
	public static final RegistryObject<Item> CircuitBoard = register("printed_circuit_board", () -> new ItemDefault());
	
	public static final RegistryObject<Item> LaserToolOpened = registerHidden("hi1-lto", () -> new ItemLaserToolOpend()); //Hidden Item 1 - Laser Tool Opened
	public static final RegistryObject<Item> LinkerModel = registerHidden("hi2-l", () -> new ItemDefault(new Item.Properties())); //Hidden Item 2 - Linker Model
	
	private static RegistryObject<Item> register(String name, final Supplier<? extends Item> item) {
		RegistryObject<Item> toReturn = ITEMS.register(name, item);
		tabStacks.add(toReturn);
		return toReturn;
	}
	
	private static RegistryObject<Item> registerHidden(String name, final Supplier<? extends Item> item) {
		RegistryObject<Item> i = ITEMS.register(name, item);
		hidenItems.add(i);
		return i;
	}
	
	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void registerPropertyOverrides(FMLClientSetupEvent event) {
		event.enqueueWork(() ->
  	  	{
  	  		ItemProperties.register(ModItems.LaserPickaxe.get(), 
  	  				new ResourceLocation(Reference.MODID, "active"), (stack, world, living, id) -> {
			        return LaserItemUtils.isExtended(stack) ? 0.0F : 1.0F;
			      });
	  	  	ItemProperties.register(ModItems.LaserSword.get(), 
	  	  			new ResourceLocation(Reference.MODID, "active"), (stack, world, living, id) -> {
			        return LaserItemUtils.isExtended(stack) ? 0.0F : 1.0F;
			      });
	  	  	ItemProperties.register(ModItems.LaserHoe.get(), 
	  				new ResourceLocation(Reference.MODID, "active"), (stack, world, living, id) -> {
			        return LaserItemUtils.isExtended(stack) ? 0.0F : 1.0F;
			      });
	  	  	ItemProperties.register(ModItems.LaserAxe.get(), 
					new ResourceLocation(Reference.MODID, "active"), (stack, world, living, id) -> {
					return LaserItemUtils.isExtended(stack) ? 0.0F : 1.0F;
				});
	  	  	ItemProperties.register(ModItems.LaserShovel.get(), 
					new ResourceLocation(Reference.MODID, "active"), (stack, world, living, id) -> {
					return LaserItemUtils.isExtended(stack) ? 0.0F : 1.0F;
				});
	  	  	ItemProperties.register(ModItems.LaserHelmet.get(), 
					new ResourceLocation(Reference.MODID, "active"), (stack, world, living, id) -> {
					return LaserItemUtils.isExtended(stack) ? 1.0F : 0.0F;
				});
	  	  	ItemProperties.register(ModItems.LaserChestplate.get(), 
					new ResourceLocation(Reference.MODID, "active"), (stack, world, living, id) -> {
					return LaserItemUtils.isExtended(stack) ? 1.0F : 0.0F;
				});
		  	ItemProperties.register(ModItems.LaserLeggings.get(), 
					new ResourceLocation(Reference.MODID, "active"), (stack, world, living, id) -> {
					return LaserItemUtils.isExtended(stack) ? 1.0F : 0.0F;
				});
  	  	});
	}
	
	public static void register(IEventBus bus) {
		ITEMS.register(bus);
	}
	
	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void registerBlockColors(ColorHandlerEvent.Item event){
		event.getItemColors().register(new ColorHandler.Item(), 
				ModItems.LaserPickaxe.get(), ModItems.LaserSword.get(), ModItems.LaserHoe.get(), ModItems.LaserAxe.get(), ModItems.LaserShovel.get(),
				ModItems.LaserToolOpened.get(),
				ModItems.LaserToolShell.get(),
				ModItems.LaserHelmet.get(), ModItems.LaserChestplate.get(), ModItems.LaserLeggings.get());
	}
}
