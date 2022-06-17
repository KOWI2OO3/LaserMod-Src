package KOWI2003.LaserMod.init;

import KOWI2003.LaserMod.Reference;
import KOWI2003.LaserMod.container.ContainerInfuser;
import KOWI2003.LaserMod.container.ContainerLaser;
import KOWI2003.LaserMod.container.ContainerLaserCrafter;
import KOWI2003.LaserMod.container.ContainerLaserProjector;
import KOWI2003.LaserMod.container.ContainerModStation;
import KOWI2003.LaserMod.container.ContainerPrecisionAssembler;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModContainerTypes {

	public static final DeferredRegister<ContainerType<?>> CONTAINER_TYPES = DeferredRegister
			.create(ForgeRegistries.CONTAINERS, Reference.MODID);
	
	public static final RegistryObject<ContainerType<ContainerLaser>> LASER_CONTAINER_TYPE = CONTAINER_TYPES
			.register("laser", () -> IForgeContainerType.create(ContainerLaser::new));
	public static final RegistryObject<ContainerType<ContainerInfuser>> INFUSER_CONTAINER_TYPE = CONTAINER_TYPES
			.register("infuser", () -> IForgeContainerType.create(ContainerInfuser::new));
	public static final RegistryObject<ContainerType<ContainerModStation>> MOD_STATION_CONTAINER_TYPE = CONTAINER_TYPES
			.register("mod_station", () -> IForgeContainerType.create(ContainerModStation::new));
	public static final RegistryObject<ContainerType<ContainerLaserProjector>> LASER_PROJECTOR_TYPE = CONTAINER_TYPES
			.register("laser_projector", () -> IForgeContainerType.create(ContainerLaserProjector::new));
	public static final RegistryObject<ContainerType<ContainerPrecisionAssembler>> PRECISION_ASSEMBLER_TYPE = CONTAINER_TYPES
			.register("precision_assembler", () -> IForgeContainerType.create(ContainerPrecisionAssembler::new));
	public static final RegistryObject<ContainerType<ContainerLaserCrafter>> LASER_CRAFTER_TYPE = CONTAINER_TYPES
			.register("laser_crafter", () -> IForgeContainerType.create(ContainerLaserCrafter::new));
}
