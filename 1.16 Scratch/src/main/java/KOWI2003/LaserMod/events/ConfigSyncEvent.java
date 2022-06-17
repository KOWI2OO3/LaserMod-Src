package KOWI2003.LaserMod.events;

import KOWI2003.LaserMod.config.ConfigSerializer;
import KOWI2003.LaserMod.network.PacketHandler;
import KOWI2003.LaserMod.network.PacketSyncConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent.LoggedOutEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ConfigSyncEvent {

	@SubscribeEvent
	public void OnJoinEvent(PlayerLoggedInEvent event) {
		PlayerEntity player = event.getPlayer();
		World world = player.getCommandSenderWorld();
		if(!world.isClientSide)
			PacketHandler.sendToClient(new PacketSyncConfig(), (ServerPlayerEntity)player);
		else 
			PacketHandler.sendToServer(new PacketSyncConfig());
	}
	
	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void OnLeaveEvent(LoggedOutEvent event) {
		System.out.println("Desyncing Config Data From Server, Reading From File...");
		ConfigSerializer.GetInstance().updateConfigFromFile();
		System.out.println("Done Reading Confg From File...");
	}
}
