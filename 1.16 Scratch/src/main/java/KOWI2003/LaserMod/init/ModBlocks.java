package KOWI2003.LaserMod.init;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

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
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Reference.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBlocks {

	public static List<Block> BLOCKS = new ArrayList<Block>();
	public static List<Item> ITEMS = new ArrayList<Item>(); 
	
	public static Block Laser = register("laser", new BlockLaser(Material.STONE));
	public static Block LaserCatcher = register("laser_catcher", new BlockLaserCatcher());
	public static Block Infuser = register("infuser", new BlockInfuser(Material.METAL));
	public static Block ModStation = register("mod_station", new BlockModificationStation(Material.METAL));
	public static Block LaserProjector = register("laser_projector", new BlockLaserProjector(Material.METAL));
	public static Block LaserController = register("laser_controller", new BlockRemote(Material.METAL));
	public static Block Mirror = register("mirror", new BlockMirror(Material.METAL));
	public static Block PrecisionAssembler = register("precision_assembler", new BlockPrecisionAssembler(Material.METAL));
	public static Block AdvancedLaser = register("advanced_laser", new AdvancedLaserBlock(Material.METAL));
	public static Block LaserCrafter = registerHidden("laser_crafter", new BlockLaserCrafter(Material.METAL));
	public static Block DeviceHub;
	
	static {
		ModChecker.check();
		if(ModChecker.isComputercraftLoaded)
			DeviceHub = registerHidden("device_hub", new BlockDeviceHub(Material.METAL));
	}
	
	private static Block registerHidden(String name, Block block) {
    	return register(name, block, new Item.Properties());
    }
	
    private static Block register(String name, Block block) {
    	return register(name, block, new Item.Properties().tab(MainMod.blocks));
    }
    
    private static Block register(String name, Block block, Item.Properties properties) {
    	return register(name, block, new BlockItem(block, properties));
    }
    
	private static Block register(String name, Block block, BlockItem item) {
		return register(name, block, block1 -> item);
	}
    
	private static Block register(String name, Block block, Function<Block, BlockItem> function) {
		block.setRegistryName(new ResourceLocation(Reference.MODID, name));
		BLOCKS.add(block);
		if(block.getRegistryName() != null) {
			Item item = function.apply(block);
			if(item != null) {
				item.setRegistryName(new ResourceLocation(Reference.MODID, name));
				if(item.getCreativeTabs().contains(MainMod.blocks))
					ModItems.tabStacks.add(item.getDefaultInstance());
				ITEMS.add(item);
			}
		}
		return block;
	}
	
	@SubscribeEvent
	public static void registerBlocks(final RegistryEvent.Register<Block> event) {
		ModChecker.check();
		BLOCKS.forEach(block -> event.getRegistry().register(block));
		BLOCKS.clear();
	}
	
	@SubscribeEvent
	public static void registerItems(final RegistryEvent.Register<Item> event) {
		ITEMS.forEach(item -> event.getRegistry().register(item));
		ITEMS.clear();
	}
	
	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void registerBlockColors(ColorHandlerEvent.Block event){
		event.getBlockColors().register(new ColorHandler.BlockEntity(), ModBlocks.LaserCatcher, ModBlocks.Laser, ModBlocks.AdvancedLaser);
	}
}
