package KOWI2003.LaserMod.init;

import java.util.LinkedList;
import java.util.function.Supplier;

import KOWI2003.LaserMod.MainMod;
import KOWI2003.LaserMod.Reference;
import KOWI2003.LaserMod.blocks.AdvancedLaserBlock;
import KOWI2003.LaserMod.blocks.BlockDeviceHub;
import KOWI2003.LaserMod.blocks.BlockInfuser;
import KOWI2003.LaserMod.blocks.BlockLaser;
import KOWI2003.LaserMod.blocks.BlockLaserCatcher;
import KOWI2003.LaserMod.blocks.BlockLaserCrafter;
import KOWI2003.LaserMod.blocks.BlockLaserProjector;
import KOWI2003.LaserMod.blocks.BlockMirror;
import KOWI2003.LaserMod.blocks.BlockModificationStation;
import KOWI2003.LaserMod.blocks.BlockPrecisionAssembler;
import KOWI2003.LaserMod.blocks.BlockRemote;
import KOWI2003.LaserMod.handlers.ColorHandler;
import KOWI2003.LaserMod.utils.ModChecker;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = Reference.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBlocks {
	
	public static final LinkedList<RegistryObject<Block>> tabBlocks = new LinkedList<>();
	
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Reference.MODID);
//	public static final DeferredRegister<Item> BLOCK_ITEM = DeferredRegister.create(ForgeRegistries.ITEMS, Reference.MODID);
	
	public static RegistryObject<Block> Laser = register("laser", () -> new BlockLaser(Material.STONE));
	public static RegistryObject<Block> LaserCatcher = register("laser_catcher", () -> new BlockLaserCatcher());
//	public static Block LaserCatcher = register("laser_catcher", new Block(BlockBehaviour.Properties));
	public static RegistryObject<Block> Infuser = register("infuser", () -> new BlockInfuser(Material.METAL));
	public static RegistryObject<Block> ModStation = register("mod_station", () -> new BlockModificationStation(Material.METAL));
	public static RegistryObject<Block> LaserProjector = register("laser_projector", () -> new BlockLaserProjector(Material.METAL));
	public static RegistryObject<Block> LaserController = register("laser_controller", () -> new BlockRemote(Material.METAL));
	public static RegistryObject<Block> Mirror = register("mirror", () -> new BlockMirror(Material.METAL));
	public static RegistryObject<Block> PrecisionAssembler = register("precision_assembler", () -> new BlockPrecisionAssembler(Material.METAL));
	public static RegistryObject<Block> AdvancedLaser = register("advanced_laser", () -> new AdvancedLaserBlock(Material.METAL));
	public static RegistryObject<Block> LaserCrafter = registerHidden("laser_crafter", () -> new BlockLaserCrafter(Material.METAL));
	public static RegistryObject<Block> DeviceHub;
	
	static {
		ModChecker.check();
		if(ModChecker.isComputercraftLoaded)
			DeviceHub = registerHidden("device_hub", () -> new BlockDeviceHub(Material.METAL));
	}
	
	private static RegistryObject<Block> register(String name, final Supplier<? extends Block> block) {
		RegistryObject<Block> toReturn = BLOCKS.register(name, block);
		registerBlockItem(name, toReturn, MainMod.blocks);
		tabBlocks.add(toReturn);
		return toReturn;
	}
	
	private static RegistryObject<Block> registerHidden(String name, final Supplier<? extends Block> block) {
		RegistryObject<Block> toReturn = BLOCKS.register(name, block);
		registerBlockItemHidden(name, toReturn);
		return toReturn;
	}
	
	private static RegistryObject<Item> registerBlockItemHidden(String name, RegistryObject<Block> block) {
		RegistryObject<Item> i = ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
		ModItems.hidenItems.add(i);
		return i;
	}
	
	private static RegistryObject<Item> registerBlockItem(String name, RegistryObject<Block> block, CreativeModeTab tab) {
		return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
	}
	
//	private static Block registerHidden(String name, final Supplier<? extends Block> block) {
//    	
//    }
	
//	private static Block registerHidden(String name, Block block) {
//    	return register(name, block, new Item.Properties());
//    }
//	
//    private static Block register(String name, Block block) { 	
//    	return register(name, block, new Item.Properties().tab(MainMod.blocks));
//    }
//    
//    private static Block register(String name, Block block, Item.Properties properties) {
//    	return register(name, block, new BlockItem(block, properties));
//    }
//    
//	private static Block register(String name, Block block, BlockItem item) {
//		return register(name, block, block1 -> item);
//	}
//    
//	private static Block register(String name, Block block, Function<Block, BlockItem> function) {
//		block.setRegistryName(new ResourceLocation(Reference.MODID, name));
//		BLOCKS.add(block);
//		if(block.getRegistryName() != null) {
//			Item item = function.apply(block);
//			if(item != null) {
//				item.setRegistryName(new ResourceLocation(Reference.MODID, name));
//				if(item.getCreativeTabs().contains(MainMod.blocks))
//					ModItems.tabStacks.add(item.getDefaultInstance());
//				ITEMS.add(item);
//			}
//		}
//		return block;
//	}
	
	public static void register(IEventBus bus) {
		BLOCKS.register(bus);
	}
	
	@SubscribeEvent
	public static void registerBlocks(final RegistryEvent.Register<Block> event) {
//		ModChecker.check();
//		BLOCKS.forEach(block -> event.getRegistry().register(block));
//		BLOCKS.clear();
	}
//	
//	@SubscribeEvent
//	public static void registerItems(final RegistryEvent.Register<Item> event) {
//		ITEMS.forEach(item -> event.getRegistry().register(item));
//		ITEMS.clear();
//	}
//	
	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void registerBlockColors(final ColorHandlerEvent.Block event){
		event.getBlockColors().register(new ColorHandler.Block(), ModBlocks.LaserCatcher.get(), ModBlocks.Laser.get(), ModBlocks.AdvancedLaser.get());
	}
}
