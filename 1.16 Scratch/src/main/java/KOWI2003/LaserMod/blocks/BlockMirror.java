package KOWI2003.LaserMod.blocks;

import java.util.ArrayList;
import java.util.List;

import KOWI2003.LaserMod.tileentities.TileEntityMirror;
import KOWI2003.LaserMod.utils.Utils;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BlockMirror extends BlockHorizontal{

	public final VoxelShape V_SHAPE;
	public final VoxelShape C_SHAPE;
	
	public BlockMirror(Material materialIn) {
		super(materialIn);
		V_SHAPE = this.generateVShape();
		C_SHAPE = this.generateCShape();
	}

	@Override
	public TileEntity newBlockEntity(IBlockReader reader) {
		return new TileEntityMirror();
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return context.getPlayer().isShiftKeyDown() ? this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite().getClockWise()) : super.getStateForPlacement(context);
	}
	
	@Override
	public BlockRenderType getRenderShape(BlockState state) {
		return BlockRenderType.MODEL;
	}
	
	@Override
	public void playerWillDestroy(World world, BlockPos pos, BlockState state,
			PlayerEntity player) {
		TileEntity tileentity = world.getBlockEntity(pos);
		if(tileentity instanceof TileEntityMirror) {
			TileEntityMirror te = ((TileEntityMirror)tileentity);
			te.handleTurnOffForInteractable();
		}
		super.playerWillDestroy(world, pos, state, player);
	}

	private VoxelShape generateCShape()
	{
	    List<VoxelShape> shapes = new ArrayList<>();
	    shapes.add(VoxelShapes.box(0.25, 0, 0.25, 0.375, 0.312, 0.375)); // STAND
	    shapes.add(VoxelShapes.box(0.266, 0.312, 0.266, 0.359, 0.356, 0.359)); // STAND
	    shapes.add(VoxelShapes.box(0.244, 0.356, 0.244, 0.381, 0.5, 0.381)); // STAND
	    shapes.add(VoxelShapes.box(0.331, 0.375, 0.344, 0.438, 0.481, 0.444)); // STAND
	    shapes.add(VoxelShapes.box(0, 0, 0.938, 0.062, 1, 1)); // HIT
	    shapes.add(VoxelShapes.box(0.062, 0, 0.875, 0.125, 1, 0.938)); // HIT
	    shapes.add(VoxelShapes.box(0.125, 0, 0.812, 0.188, 1, 0.875)); // HIT
	    shapes.add(VoxelShapes.box(0.188, 0, 0.75, 0.25, 1, 0.812)); // HIT
	    shapes.add(VoxelShapes.box(0.25, 0, 0.688, 0.312, 1, 0.75)); // HIT
	    shapes.add(VoxelShapes.box(0.312, 0, 0.625, 0.375, 1, 0.688)); // HIT
	    shapes.add(VoxelShapes.box(0.375, 0, 0.438, 0.438, 1, 0.625)); // HIT
	    shapes.add(VoxelShapes.box(0.375, 0, 0.375, 0.625, 1, 0.438)); // HIT
	    shapes.add(VoxelShapes.box(0.625, 0, 0.312, 0.688, 1, 0.375)); // HIT
	    shapes.add(VoxelShapes.box(0.688, 0, 0.25, 0.75, 1, 0.312)); // HIT
	    shapes.add(VoxelShapes.box(0.75, 0, 0.188, 0.812, 1, 0.25)); // HIT
	    shapes.add(VoxelShapes.box(0.812, 0, 0.125, 0.875, 1, 0.188)); // HIT
	    shapes.add(VoxelShapes.box(0.875, 0, 0.062, 0.938, 1, 0.125)); // HIT
	    shapes.add(VoxelShapes.box(0.938, 0, 0, 1, 1, 0.062)); // HIT
	    
	    VoxelShape result = VoxelShapes.empty();
	    for(VoxelShape shape : shapes)
	    {
	        result = VoxelShapes.join(result, shape, IBooleanFunction.OR);
	    }
	    return result.optimize();
	}
	
	private VoxelShape generateVShape()
	{
	    List<VoxelShape> shapes = new ArrayList<>();
	    shapes.add(VoxelShapes.box(0.25, 0, 0.25, 0.375, 0.312, 0.375)); // STAND
	    shapes.add(VoxelShapes.box(0.266, 0.312, 0.266, 0.359, 0.356, 0.359)); // STAND
	    shapes.add(VoxelShapes.box(0.244, 0.356, 0.244, 0.381, 0.5, 0.381)); // STAND
	    shapes.add(VoxelShapes.box(0.331, 0.375, 0.344, 0.438, 0.481, 0.444)); // STAND
	    shapes.add(VoxelShapes.box(0.4, 0.375, 0.406, 0.5, 0.481, 0.506)); // STAND
	    shapes.add(VoxelShapes.box(0, 0, 0.938, 0.062, 1, 1)); // HIT
	    shapes.add(VoxelShapes.box(0.062, 0, 0.875, 0.125, 1, 0.938)); // HIT
	    shapes.add(VoxelShapes.box(0.125, 0, 0.812, 0.188, 1, 0.875)); // HIT
	    shapes.add(VoxelShapes.box(0.188, 0, 0.75, 0.25, 1, 0.812)); // HIT
	    shapes.add(VoxelShapes.box(0.25, 0, 0.688, 0.312, 1, 0.75)); // HIT
	    shapes.add(VoxelShapes.box(0.312, 0, 0.625, 0.375, 1, 0.688)); // HIT
	    shapes.add(VoxelShapes.box(0.375, 0, 0.562, 0.438, 1, 0.625)); // HIT
	    shapes.add(VoxelShapes.box(0.438, 0, 0.5, 0.5, 1, 0.562)); // HIT
	    shapes.add(VoxelShapes.box(0.5, 0, 0.438, 0.562, 1, 0.5)); // HIT
	    shapes.add(VoxelShapes.box(0.562, 0, 0.375, 0.625, 1, 0.438)); // HIT
	    shapes.add(VoxelShapes.box(0.625, 0, 0.312, 0.688, 1, 0.375)); // HIT
	    shapes.add(VoxelShapes.box(0.688, 0, 0.25, 0.75, 1, 0.312)); // HIT
	    shapes.add(VoxelShapes.box(0.75, 0, 0.188, 0.812, 1, 0.25)); // HIT
	    shapes.add(VoxelShapes.box(0.812, 0, 0.125, 0.875, 1, 0.188)); // HIT
	    shapes.add(VoxelShapes.box(0.875, 0, 0.062, 0.938, 1, 0.125)); // HIT
	    shapes.add(VoxelShapes.box(0.938, 0, 0, 1, 1, 0.062)); // HIT

	    VoxelShape result = VoxelShapes.empty();
	    for(VoxelShape shape : shapes)
	    {
	        result = VoxelShapes.join(result, shape, IBooleanFunction.OR);
	    }
	    return result.optimize();
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader reader, BlockPos pos,
			ISelectionContext context) {
		VoxelShape temp = V_SHAPE;
		temp = Utils.rotateVoxelShape(temp, state.getValue(FACING).getCounterClockWise());
	    return temp;
	}
	
	@Override
	public VoxelShape getCollisionShape(BlockState state, IBlockReader reader, BlockPos pos,
			ISelectionContext context) {
		VoxelShape temp = C_SHAPE;
		temp = Utils.rotateVoxelShape(temp, state.getValue(FACING).getCounterClockWise());
	    return temp;
	}
	
}
