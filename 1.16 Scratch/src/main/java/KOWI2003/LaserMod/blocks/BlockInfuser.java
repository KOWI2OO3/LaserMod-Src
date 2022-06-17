package KOWI2003.LaserMod.blocks;

import java.util.ArrayList;
import java.util.List;

import KOWI2003.LaserMod.tileentities.TileEntityInfuser;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class BlockInfuser extends BlockHorizontal {

	public final VoxelShape SHAPE;
	
	public BlockInfuser(Material materialIn) {
		super(materialIn);
		SHAPE = this.generateShape();
	}

	@Override
	public TileEntity newBlockEntity(IBlockReader p_196283_1_) {
		return new TileEntityInfuser();
	}
	
	@Override
	public ActionResultType use(BlockState state, World world, BlockPos pos,
			PlayerEntity player, Hand hand, BlockRayTraceResult raytraceResult) {
		if(!world.isClientSide) {
			TileEntity te = world.getBlockEntity(pos);
			if(te instanceof TileEntityInfuser) {
				NetworkHooks.openGui((ServerPlayerEntity)player, (TileEntityInfuser) te, pos);
				return ActionResultType.SUCCESS;
			}
		}
		return ActionResultType.SUCCESS;
	}
	
	@Override
	public BlockRenderType getRenderShape(BlockState state) {
		return BlockRenderType.MODEL;
	}
	
	@Override
	public void playerWillDestroy(World world, BlockPos pos, BlockState state,
			PlayerEntity player) {
		TileEntity tileentity = world.getBlockEntity(pos);
		if(tileentity instanceof TileEntityInfuser) {
			TileEntityInfuser te = ((TileEntityInfuser)tileentity);
			for (int i = 0; i < te.handler.getSlots(); i++) {
				ItemStack stack = te.handler.getStackInSlot(i);
				if(!stack.isEmpty())
					popResource((World) world, pos, stack);
			}
		}
		super.playerWillDestroy(world, pos, state, player);
	}
	
	private VoxelShape generateShape()
	{
	    List<VoxelShape> shapes = new ArrayList<>();
	    shapes.add(VoxelShapes.box(0.125, 0, 0.125, 0.875, 0.019, 0.875)); 	 // CUBE
	    shapes.add(VoxelShapes.box(0.062, 0.019, 0.062, 0.938, 0.225, 0.938)); // CUBE
	    shapes.add(VoxelShapes.box(0.656, 0.181, 0.156, 0.906, 0.525, 0.406)); // CUBE

	    VoxelShape result = VoxelShapes.empty();
	    for(VoxelShape shape : shapes)
	    {
	        result = VoxelShapes.joinUnoptimized(result, shape, IBooleanFunction.OR);
	    }
	    return result.optimize();
	}

	@Override
	public VoxelShape getVisualShape(BlockState state, IBlockReader world, BlockPos pos,
			ISelectionContext context) {
		return getShape(state, world, pos, context);
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader reader, BlockPos pos,
			ISelectionContext context) {
		return Utils.rotateVoxelShape(SHAPE, state.getValue(FACING).getCounterClockWise());
	}
	
	@Override
	public VoxelShape getCollisionShape(BlockState state, IBlockReader reader, BlockPos pos,
			ISelectionContext context)
	{
		return getShape(state, reader, pos, context);
	}
}
