package KOWI2003.LaserMod.items.upgrades;

import KOWI2003.LaserMod.init.ModBlocks;
import KOWI2003.LaserMod.items.ItemUpgradeBase;
import KOWI2003.LaserMod.tileentities.ILaserAccess;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;

public class UpgradeMining extends ItemUpgradeBase {

	public UpgradeMining(String name) {
		super(name);
		setCanBeUsedForLaser(true);
	}
	
	@SuppressWarnings("static-access")
	@Override
	public void runLaserBlock(ILaserAccess te, BlockPos pos) {
		BlockState state = te.getTileEntity().getLevel().getBlockState(pos);
		if(state.getDestroySpeed(te.getTileEntity().getLevel(), pos) >= 0)
			if(state.getMaterial() == Material.STONE) {
				if(state.getBlock() == ModBlocks.LaserCatcher)
					return;
				te.getTileEntity().getLevel().removeBlock(pos, true);
				state.getBlock().dropResources(state, te.getTileEntity().getLevel(), pos);
			}
	}
	
}
