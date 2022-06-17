package KOWI2003.LaserMod.proxy;

import java.util.function.Predicate;

import KOWI2003.LaserMod.container.ContainerInfuser;
import KOWI2003.LaserMod.container.ContainerLaser;
import KOWI2003.LaserMod.container.ContainerLaserCrafter;
import KOWI2003.LaserMod.container.ContainerLaserProjector;
import KOWI2003.LaserMod.container.ContainerModStation;
import KOWI2003.LaserMod.container.ContainerPrecisionAssembler;
import KOWI2003.LaserMod.gui.GuiInfuser;
import KOWI2003.LaserMod.gui.GuiLaser;
import KOWI2003.LaserMod.gui.GuiLaserCrafter;
import KOWI2003.LaserMod.gui.GuiLaserProjector;
import KOWI2003.LaserMod.gui.GuiModStation;
import KOWI2003.LaserMod.gui.GuiPrecisionAssembler;
import KOWI2003.LaserMod.init.ModBlocks;
import KOWI2003.LaserMod.init.ModContainerTypes;
import KOWI2003.LaserMod.init.ModKeybindings;
import KOWI2003.LaserMod.init.ModTileTypes;
import KOWI2003.LaserMod.tileentities.render.InfuserRenderer;
import KOWI2003.LaserMod.tileentities.render.LaserControllerRender;
import KOWI2003.LaserMod.tileentities.render.LaserProjectorRenderer;
import KOWI2003.LaserMod.tileentities.render.LaserRender;
import KOWI2003.LaserMod.tileentities.render.MirrorRender;
import KOWI2003.LaserMod.tileentities.render.ModStationRenderer;
import KOWI2003.LaserMod.tileentities.render.PrecisionAssemblerRenderer;
import KOWI2003.LaserMod.tileentities.render.RenderAdvancedLaser;
import KOWI2003.LaserMod.utils.ModChecker;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.gui.ScreenManager.IScreenFactory;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy {

	@Override
	@OnlyIn(Dist.CLIENT)
	public void onSetupClient() {
		ClientRegistry.bindTileEntityRenderer(ModTileTypes.LASER, LaserRender::new);
		ClientRegistry.bindTileEntityRenderer(ModTileTypes.INFUSER, InfuserRenderer::new);
		ClientRegistry.bindTileEntityRenderer(ModTileTypes.MOD_STATION, ModStationRenderer::new);
		ClientRegistry.bindTileEntityRenderer(ModTileTypes.LASER_PROJECTOR, LaserProjectorRenderer::new);
		ClientRegistry.bindTileEntityRenderer(ModTileTypes.LASER_CONTROLLER, LaserControllerRender::new);
		ClientRegistry.bindTileEntityRenderer(ModTileTypes.MIRROR, MirrorRender::new);
		ClientRegistry.bindTileEntityRenderer(ModTileTypes.ADVANCED_LASER, RenderAdvancedLaser::new);
		ClientRegistry.bindTileEntityRenderer(ModTileTypes.PRECISION_ASSEMBLER, PrecisionAssemblerRenderer::new);

		ModChecker.check();
		if(ModChecker.isComputercraftLoaded)
			ClientRegistry.bindTileEntityRenderer(ModTileTypes.LASER_CONTROLLER_CC, LaserControllerRender::new);
		
		ScreenManager.register(ModContainerTypes.LASER_CONTAINER_TYPE.get(), 
				(IScreenFactory<ContainerLaser, GuiLaser>) GuiLaser::new);
		ScreenManager.register(ModContainerTypes.INFUSER_CONTAINER_TYPE.get(), 
				(IScreenFactory<ContainerInfuser, GuiInfuser>) GuiInfuser::new);
		ScreenManager.register(ModContainerTypes.MOD_STATION_CONTAINER_TYPE.get(), 
				(IScreenFactory<ContainerModStation, GuiModStation>) GuiModStation::new);
		ScreenManager.register(ModContainerTypes.LASER_PROJECTOR_TYPE.get(), 
				(IScreenFactory<ContainerLaserProjector, GuiLaserProjector>) GuiLaserProjector::new);
		ScreenManager.register(ModContainerTypes.PRECISION_ASSEMBLER_TYPE.get(), 
				(IScreenFactory<ContainerPrecisionAssembler, GuiPrecisionAssembler>) GuiPrecisionAssembler::new);
		ScreenManager.register(ModContainerTypes.LASER_CRAFTER_TYPE.get(), 
				(IScreenFactory<ContainerLaserCrafter, GuiLaserCrafter>) GuiLaserCrafter::new);
		
		Predicate<RenderType> cutoutPredicate = renderType -> renderType == RenderType.translucent();
		RenderTypeLookup.setRenderLayer(ModBlocks.Infuser, cutoutPredicate);
		
		for(KeyBinding key : ModKeybindings.mappings)
			ClientRegistry.registerKeyBinding(key);
	}
}
