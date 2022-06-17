package KOWI2003.LaserMod.blocks;

import KOWI2003.LaserMod.tileentities.TileEntityLaserCrafter;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class BlockLaserCrafter extends BlockHorizontal {

	public BlockLaserCrafter(Material materialIn) {
		super(materialIn);
	}

	@Override
	public ActionResultType use(BlockState state, World world, BlockPos pos,
			PlayerEntity player, Hand hand, BlockRayTraceResult raytraceResult) {
		if(!world.isClientSide) {
			TileEntity te = world.getBlockEntity(pos);
			if(te instanceof TileEntityLaserCrafter) {
				NetworkHooks.openGui((ServerPlayerEntity)player, (TileEntityLaserCrafter) te, pos);
				return ActionResultType.SUCCESS;
			}
		}
		return ActionResultType.FAIL;
	}
	
	@Override
		public BlockRenderType getRenderShape(BlockState state) {
		return BlockRenderType.MODEL;
	}
	
	@Override
	public TileEntity newBlockEntity(IBlockReader reader) {
		return new TileEntityLaserCrafter();
	}
	

}
