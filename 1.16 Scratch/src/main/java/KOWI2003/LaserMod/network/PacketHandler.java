package KOWI2003.LaserMod.network;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandler {

	public static SimpleChannel INSTANCE;

	private static int ID = 0;

	private static final String PROTOCOL_VERSION = "1";
	
	private static int nextID() {
		return ID++;
	}

	/**
	 * Register all of our network messages on their appropriate side
	 * 
	 * @param channelName
	 *            The name of the network channel
	 */
	public static void registerMessages(String channelName) {
		INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(channelName), 
				() -> PROTOCOL_VERSION,
			    PROTOCOL_VERSION::equals,
			    PROTOCOL_VERSION::equals);

		INSTANCE.messageBuilder(PacketLaser.class, nextID())
			.encoder(PacketLaser::toBytes)
			.decoder(PacketLaser::new)
			.consumer(PacketLaser::handle)
			.add();
		
		INSTANCE.messageBuilder(PacketLaserMode.class, nextID())
		.encoder(PacketLaserMode::toBytes)
		.decoder(PacketLaserMode::new)
		.consumer(PacketLaserMode::handle)
		.add();
		
		INSTANCE.messageBuilder(PacketModStation.class, nextID())
		.encoder(PacketModStation::toBytes)
		.decoder(PacketModStation::new)
		.consumer(PacketModStation::handle)
		.add();
		
		INSTANCE.messageBuilder(PacketProjectorProperty.class, nextID())
		.encoder(PacketProjectorProperty::toBytes)
		.decoder(PacketProjectorProperty::new)
		.consumer(PacketProjectorProperty::handle)
		.add();
		
		INSTANCE.messageBuilder(PacketLaserProjector.class, nextID())
		.encoder(PacketLaserProjector::toBytes)
		.decoder(PacketLaserProjector::new)
		.consumer(PacketLaserProjector::handle)
		.add();
		
		INSTANCE.messageBuilder(PacketComputerToMainThread.class, nextID())
		.encoder(PacketComputerToMainThread::toBytes)
		.decoder(PacketComputerToMainThread::new)
		.consumer(PacketComputerToMainThread::handle)
		.add();
		
		INSTANCE.messageBuilder(PacketHandleRecipeChange.class, nextID())
		.encoder(PacketHandleRecipeChange::toBytes)
		.decoder(PacketHandleRecipeChange::new)
		.consumer(PacketHandleRecipeChange::handle)
		.add();
		
		INSTANCE.messageBuilder(PacketLaserDirection.class, nextID())
		.encoder(PacketLaserDirection::toBytes)
		.decoder(PacketLaserDirection::new)
		.consumer(PacketLaserDirection::handle)
		.add();

		INSTANCE.messageBuilder(PacketDeltaMovment.class, nextID())
		.encoder(PacketDeltaMovment::toBytes)
		.decoder(PacketDeltaMovment::new)
		.consumer(PacketDeltaMovment::handle)
		.add();
		
		INSTANCE.messageBuilder(PacketSyncIColor.class, nextID())
		.encoder(PacketSyncIColor::toBytes)
		.decoder(PacketSyncIColor::new)
		.consumer(PacketSyncIColor::handle)
		.add();
		
		INSTANCE.messageBuilder(PacketSyncConfig.class, nextID())
		.encoder(PacketSyncConfig::toBytes)
		.decoder(PacketSyncConfig::new)
		.consumer(PacketSyncConfig::handle)
		.add();
		
		INSTANCE.messageBuilder(PacketSyncArmor.class, nextID())
		.encoder(PacketSyncArmor::toBytes)
		.decoder(PacketSyncArmor::new)
		.consumer(PacketSyncArmor::handle)
		.add();
		
	}
	
	public static void sendToClient(Object packet, ServerPlayerEntity player) {
    	if(INSTANCE != null && packet != null)
    		INSTANCE.sendTo(packet, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static void sendToServer(Object packet) {
    	if(INSTANCE != null && packet != null)
    		INSTANCE.sendToServer(packet);
    }
	
}
