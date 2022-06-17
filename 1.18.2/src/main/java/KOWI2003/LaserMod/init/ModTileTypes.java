package KOWI2003.LaserMod.init;

import java.util.ArrayList;
import java.util.List;

import KOWI2003.LaserMod.Reference;
import KOWI2003.LaserMod.tileentities.TileEntityAdvancedLaser;
import KOWI2003.LaserMod.tileentities.TileEntityDeviceHub;
import KOWI2003.LaserMod.tileentities.TileEntityInfuser;
import KOWI2003.LaserMod.tileentities.TileEntityLaser;
import KOWI2003.LaserMod.tileentities.TileEntityLaserCatcher;
import KOWI2003.LaserMod.tileentities.TileEntityLaserController;
import KOWI2003.LaserMod.tileentities.TileEntityLaserCrafter;
import KOWI2003.LaserMod.tileentities.TileEntityLaserProjector;
import KOWI2003.LaserMod.tileentities.TileEntityMirror;
import KOWI2003.LaserMod.tileentities.TileEntityModStation;
import KOWI2003.LaserMod.tileentities.TileEntityPrecisionAssembler;
import KOWI2003.LaserMod.tileentities.TileEntityRemoteCC;
import KOWI2003.LaserMod.utils.ModChecker;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Reference.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModTileTypes {

    @SuppressWarnings("rawtypes")
	private static final List<BlockEntityType> TILE_ENTITY_TYPES = new ArrayList<>();
	
    public static BlockEntityType<TileEntityLaser> LASER;
    public static BlockEntityType<TileEntityLaserCatcher> LASER_CATCHER;
    public static BlockEntityType<TileEntityInfuser> INFUSER;
    public static BlockEntityType<TileEntityModStation> MOD_STATION;
    public static BlockEntityType<TileEntityLaserProjector> LASER_PROJECTOR;
    public static BlockEntityType<TileEntityLaserController> LASER_CONTROLLER;
    public static BlockEntityType<TileEntityMirror> MIRROR;
    public static BlockEntityType<TileEntityAdvancedLaser> ADVANCED_LASER;
    public static BlockEntityType<TileEntityPrecisionAssembler> PRECISION_ASSEMBLER;
    public static BlockEntityType<TileEntityLaserCrafter> LASER_CRAFTER;
    public static BlockEntityType<TileEntityRemoteCC> LASER_CONTROLLER_CC;
    public static BlockEntityType<TileEntityDeviceHub> DEVICE_HUB;
    
    public static void optionalRegistry() {
    	ModChecker.check();
    	if(ModChecker.isComputercraftLoaded) LASER_CONTROLLER_CC = buildType("laser_controller_cc", BlockEntityType.Builder.of(TileEntityRemoteCC::new, ModBlocks.LaserController.get()));
    	if(ModChecker.isComputercraftLoaded) DEVICE_HUB = buildType("device_hub", BlockEntityType.Builder.of(TileEntityDeviceHub::new, ModBlocks.DeviceHub.get()));
    }
    
    public static void register() {
    	LASER = buildType("laser", BlockEntityType.Builder.of(TileEntityLaser::new, ModBlocks.Laser.get()));
    	LASER_CATCHER = buildType("laser_catcher", BlockEntityType.Builder.of(TileEntityLaserCatcher::new, ModBlocks.LaserCatcher.get()));
    	INFUSER = buildType("infuser", BlockEntityType.Builder.of(TileEntityInfuser::new, ModBlocks.Infuser.get()));
    	MOD_STATION = buildType("mod_station", BlockEntityType.Builder.of(TileEntityModStation::new, ModBlocks.ModStation.get()));
    	LASER_PROJECTOR = buildType("laser_projector", BlockEntityType.Builder.of(TileEntityLaserProjector::new, ModBlocks.LaserProjector.get()));
    	LASER_CONTROLLER = buildType("laser_controller", BlockEntityType.Builder.of(TileEntityLaserController::new, ModBlocks.LaserController.get()));
    	MIRROR = buildType("mirror", BlockEntityType.Builder.of(TileEntityMirror::new, ModBlocks.Mirror.get()));
    	ADVANCED_LASER = buildType("advanced_laser", BlockEntityType.Builder.of(TileEntityAdvancedLaser::new, ModBlocks.AdvancedLaser.get()));
    	PRECISION_ASSEMBLER = buildType("precision_assembler", BlockEntityType.Builder.of(TileEntityPrecisionAssembler::new, ModBlocks.PrecisionAssembler.get()));
    	LASER_CRAFTER = buildType("laser_crafter", BlockEntityType.Builder.of(TileEntityLaserCrafter::new, ModBlocks.LaserCrafter.get()));
    	optionalRegistry();
    }
    
	private static <T extends BlockEntity> BlockEntityType<T> buildType(String id, BlockEntityType.Builder<T> builder) {
		BlockEntityType<T> type = builder.build(null);
		type.setRegistryName(id);
		TILE_ENTITY_TYPES.add(type);
		return type;
	}
	
	@SubscribeEvent
	public static void registerType(final RegistryEvent.Register<BlockEntityType<?>> event) {
    	ModTileTypes.register();
		TILE_ENTITY_TYPES.forEach(type -> event.getRegistry().register(type));
		TILE_ENTITY_TYPES.clear();
	}
}
