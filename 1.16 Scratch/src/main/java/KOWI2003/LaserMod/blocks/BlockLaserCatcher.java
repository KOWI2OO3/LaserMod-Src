package KOWI2003.LaserMod.blocks;

import KOWI2003.LaserMod.tileentities.TileEntityLaserCatcher;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class BlockLaserCatcher extends BlockRotatable {
	
	public BlockLaserCatcher() {
		super(Material.STONE);
		setModelPlacement(false);
	}
	
	@Override
	public int getSignal(BlockState state, IBlockReader world, BlockPos pos,
			Direction direction) {
		TileEntity te = world.getBlockEntity(pos);
		if(te instanceof TileEntityLaserCatcher)
			return ((TileEntityLaserCatcher) te).isHit ? 15 : 0;
		return 0;
	}
	
	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}
	
	@Override
	public boolean isSignalSource(BlockState state) {
		return true;
	}
	
	@Override
	public TileEntity newBlockEntity(IBlockReader p_196283_1_) {
		return new TileEntityLaserCatcher();
	}
	
	@Override
	public BlockRenderType getRenderShape(BlockState state) {
		return BlockRenderType.MODEL;
	}

}
