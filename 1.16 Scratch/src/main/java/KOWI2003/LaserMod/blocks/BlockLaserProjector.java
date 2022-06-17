package KOWI2003.LaserMod.blocks;

import java.util.ArrayList;
import java.util.List;

import KOWI2003.LaserMod.tileentities.TileEntityLaserProjector;
import KOWI2003.LaserMod.utils.Utils;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class BlockLaserProjector extends BlockHorizontal {

	public BlockLaserProjector(Material materialIn) {
		super(materialIn);
	}

	@Override
	public ActionResultType use(BlockState state, World world, BlockPos pos,
			PlayerEntity player, Hand hand, BlockRayTraceResult raytraceResult) {
		if(!world.isClientSide) {
			TileEntity te = world.getBlockEntity(pos);
			if(te instanceof TileEntityLaserProjector) {
				NetworkHooks.openGui((ServerPlayerEntity)player, (TileEntityLaserProjector) te, pos);
				return ActionResultType.SUCCESS;
			}
		}
		return ActionResultType.SUCCESS;
	}
	
	@Override
	public TileEntity newBlockEntity(IBlockReader reader) {
		return new TileEntityLaserProjector();
	}
	
	@Override
	public BlockRenderType getRenderShape(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	public VoxelShape getVisualShape(BlockState state, IBlockReader world, BlockPos pos,
			ISelectionContext context) {
		return getShape(state, world, pos, context);
	}
	
	@Override
	public VoxelShape getCollisionShape(BlockState state, IBlockReader world, BlockPos pos,
			ISelectionContext context) {
		return getShape(state, world, pos, context);
	}
	
	@Override
	public void playerWillDestroy(World world, BlockPos pos, BlockState state,
			PlayerEntity player) {
		TileEntity tileentity = world.getBlockEntity(pos);
		if(tileentity instanceof TileEntityLaserProjector) {
			TileEntityLaserProjector te = ((TileEntityLaserProjector)tileentity);
			ItemStack stack = te.handler.getStackInSlot(0);
			if(!stack.isEmpty())
				popResource((World) world, pos, stack);
		}
		super.playerWillDestroy(world, pos, state, player);
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos,
			ISelectionContext context) {
		List<AxisAlignedBB> aabbs = new ArrayList<AxisAlignedBB>();
		float height = 0.532f;
		aabbs.add(new AxisAlignedBB(0.25, 0, 0, 0.938, height, 1));
		aabbs.add(new AxisAlignedBB(0, 0, 0.062, 1, height, 0.938));
		aabbs.add(new AxisAlignedBB(0.062, 0, 0.938, 0.5, height, 1));
		aabbs.add(new AxisAlignedBB(0.062, 0, 0, 0.312, height, 0.062));
		return Utils.getShapeFromAABB(aabbs);
	}
	
}
