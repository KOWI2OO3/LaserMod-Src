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
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Reference.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModTileTypes {

    @SuppressWarnings("rawtypes")
	private static final List<TileEntityType> TILE_ENTITY_TYPES = new ArrayList<>();
	
    public static final TileEntityType<TileEntityLaser> LASER = buildType("laser", TileEntityType.Builder.of(TileEntityLaser::new, ModBlocks.Laser));
    public static final TileEntityType<TileEntityLaserCatcher> LASER_CATCHER = buildType("laser_catcher", TileEntityType.Builder.of(TileEntityLaserCatcher::new, ModBlocks.LaserCatcher));
    public static final TileEntityType<TileEntityInfuser> INFUSER = buildType("infuser", TileEntityType.Builder.of(TileEntityInfuser::new, ModBlocks.Infuser));
    public static final TileEntityType<TileEntityModStation> MOD_STATION = buildType("mod_station", TileEntityType.Builder.of(TileEntityModStation::new, ModBlocks.ModStation));
    public static final TileEntityType<TileEntityLaserProjector> LASER_PROJECTOR = buildType("laser_projector", TileEntityType.Builder.of(TileEntityLaserProjector::new, ModBlocks.LaserProjector));
    public static final TileEntityType<TileEntityLaserController> LASER_CONTROLLER = buildType("laser_controller", TileEntityType.Builder.of(TileEntityLaserController::new, ModBlocks.LaserController));
    public static final TileEntityType<TileEntityMirror> MIRROR = buildType("mirror", TileEntityType.Builder.of(TileEntityMirror::new, ModBlocks.Mirror));
    public static final TileEntityType<TileEntityAdvancedLaser> ADVANCED_LASER = buildType("advanced_laser", TileEntityType.Builder.of(TileEntityAdvancedLaser::new, ModBlocks.AdvancedLaser));
    public static final TileEntityType<TileEntityPrecisionAssembler> PRECISION_ASSEMBLER = buildType("precision_assembler", TileEntityType.Builder.of(TileEntityPrecisionAssembler::new, ModBlocks.PrecisionAssembler));
    public static final TileEntityType<TileEntityLaserCrafter> LASER_CRAFTER = buildType("laser_crafter", TileEntityType.Builder.of(TileEntityLaserCrafter::new, ModBlocks.LaserCrafter));
    public static TileEntityType<TileEntityRemoteCC> LASER_CONTROLLER_CC;
    public static TileEntityType<TileEntityDeviceHub> DEVICE_HUB;
    
    public static void optionalRegistry() {
    	ModChecker.check();
    	if(ModChecker.isComputercraftLoaded) LASER_CONTROLLER_CC = buildType("laser_controller_cc", TileEntityType.Builder.of(TileEntityRemoteCC::new, ModBlocks.LaserController));
    	if(ModChecker.isComputercraftLoaded) DEVICE_HUB = buildType("device_hub", TileEntityType.Builder.of(TileEntityDeviceHub::new, ModBlocks.DeviceHub));
    }
    
	private static <T extends TileEntity> TileEntityType<T> buildType(String id, TileEntityType.Builder<T> builder) {
		TileEntityType<T> type = builder.build(null);
		type.setRegistryName(id);
		TILE_ENTITY_TYPES.add(type);
		return type;
	}
	
	@SubscribeEvent
	public static void registerType(final RegistryEvent.Register<TileEntityType<?>> event) {
    	ModTileTypes.optionalRegistry();
		TILE_ENTITY_TYPES.forEach(type -> event.getRegistry().register(type));
		TILE_ENTITY_TYPES.clear();
	}
}
