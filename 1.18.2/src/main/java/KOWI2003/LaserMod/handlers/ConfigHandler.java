package KOWI2003.LaserMod.handlers;

import KOWI2003.LaserMod.Reference;
import KOWI2003.LaserMod.config.ModConfig;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Reference.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ConfigHandler {

	public static ModConfig config;
	
	@SubscribeEvent
	public static void register(RegistryEvent.Register<Block> event) {
//		config = new ModConfig();
	}
	
}
