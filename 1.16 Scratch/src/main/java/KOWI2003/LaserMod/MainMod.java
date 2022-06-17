package KOWI2003.LaserMod;

import KOWI2003.LaserMod.config.ConfigSerializer;
import KOWI2003.LaserMod.handlers.EventHandler;
import KOWI2003.LaserMod.init.ModBlocks;
import KOWI2003.LaserMod.init.ModContainerTypes;
import KOWI2003.LaserMod.init.ModItems;
import KOWI2003.LaserMod.init.ModSounds;
import KOWI2003.LaserMod.init.ModUpgrades;
import KOWI2003.LaserMod.proxy.ClientProxy;
import KOWI2003.LaserMod.proxy.CommonProxy;
import KOWI2003.LaserMod.utils.ModChecker;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("lasermod")
public class MainMod {

	public static final ItemGroup blocks = new ItemGroup("tabLaserMod")
    {
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(ModBlocks.Laser);
		}
		
		@Override
		public void fillItemList(NonNullList<ItemStack> list) {
			list.addAll(ModItems.tabStacks);
		}
    };
    
    public static final ItemGroup upgrades = new ItemGroup("tabLaserMod.upgrades")
    {
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(ModUpgrades.Speed);
		}
		
		@Override
		public void fillItemList(NonNullList<ItemStack> list) {
			list.addAll(ModUpgrades.tabStacks);
		}
    };
	
	public static final CommonProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> CommonProxy::new);
	
	public MainMod() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onCommonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
        
        EventHandler.registerEvents();
//        new ModConfig();
    	ConfigSerializer.GetInstance();
        
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ModSounds.register(bus);
        ModContainerTypes.CONTAINER_TYPES.register(bus);
	}
    
    private void onCommonSetup(FMLCommonSetupEvent event)
    {	
    	ModChecker.check();
    	proxy.onSetupCommon();
    }
    
    private void onClientSetup(FMLClientSetupEvent event)
    {
    	proxy.onSetupClient();
    	ModItems.registerPropertyOverrides(event);
    }
	
}
