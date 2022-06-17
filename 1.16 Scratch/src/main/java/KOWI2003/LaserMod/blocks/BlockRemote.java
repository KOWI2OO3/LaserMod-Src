package KOWI2003.LaserMod.blocks;

import java.util.ArrayList;
import java.util.List;

import KOWI2003.LaserMod.tileentities.TileEntityLaser;
import KOWI2003.LaserMod.tileentities.TileEntityLaserController;
import KOWI2003.LaserMod.tileentities.TileEntityLaserProjector;
import KOWI2003.LaserMod.tileentities.TileEntityLaserProjector.PROJECTOR_MODES;
import KOWI2003.LaserMod.tileentities.TileEntityRemoteCC;
import KOWI2003.LaserMod.utils.ModChecker;
import KOWI2003.LaserMod.utils.Utils;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class BlockRemote extends BlockHorizontal {

	public final VoxelShape SHAPE;
	
	AxisAlignedBB MAIN = new AxisAlignedBB(0.175, 0.569, -0.031, 0.35, 0.75, 0.031);
	AxisAlignedBB MODE = new AxisAlignedBB(0.738, 0.569, -0.031, 0.806, 0.75, 0.031);
	AxisAlignedBB RED = new AxisAlignedBB(0.544, 0.444, -0.031, 0.812, 0.513, 0.031);
	AxisAlignedBB GREEN = new AxisAlignedBB(0.544, 0.363, -0.031, 0.812, 0.431, 0.031);
	AxisAlignedBB BLUE = new AxisAlignedBB(0.544, 0.281, -0.031, 0.812, 0.35, 0.031);
	
	public BlockRemote(Material materialIn) {
		super(materialIn);
		SHAPE = this.generateShape();
	}
	
	@Override
	public ActionResultType use(BlockState state, World world, BlockPos pos,
			PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		float x = (float) hit.getLocation().x;
		float y = (float) hit.getLocation().y;
		float z = (float) hit.getLocation().z;
		Vector3d loc = hit.getLocation().subtract(new Vector3d(pos.getX(), pos.getY(), pos.getZ()));
		TileEntityLaserController te = (TileEntityLaserController) world.getBlockEntity(pos);
		
		if(!te.isConnected())
			return super.use(state, world, pos, player, hand, hit);
			
		TileEntity tile = te.getControlTileEntity();
		
		AxisAlignedBB temp = Utils.rotateAABB(MAIN, state.getValue(FACING).getClockWise());
		if(temp.contains(loc)) {
			if(tile instanceof TileEntityLaser) {
				TileEntityLaser laser = ((TileEntityLaser)tile);
				laser.setActive(!laser.active);
			}else if(tile instanceof TileEntityLaserProjector) {
				TileEntityLaserProjector laser = ((TileEntityLaserProjector)tile);
				laser.setActive(!laser.isActive);
			}
			return ActionResultType.SUCCESS;
		}
		temp = Utils.rotateAABB(MODE, state.getValue(FACING).getClockWise());
		if(temp.contains(loc)) {
			if(tile instanceof TileEntityLaser) {
				TileEntityLaser laser = ((TileEntityLaser)tile);
				if(laser.getProperties().hasUpgarde("mode")) {
					int index = laser.mode.ordinal(); 
					if(player.isShiftKeyDown())
						index -= 1;
					else
						index += 1;
					if(index >= KOWI2003.LaserMod.tileentities.TileEntityLaser.MODE.values().length) {
						index = 0;
					}
					if(index < 0) {
						index = KOWI2003.LaserMod.tileentities.TileEntityLaser.MODE.values().length-1;
					}
					laser.mode = KOWI2003.LaserMod.tileentities.TileEntityLaser.MODE.values()[index];
				}
			}else if(tile instanceof TileEntityLaserProjector) {
				TileEntityLaserProjector laser = ((TileEntityLaserProjector)tile);
				int index = laser.mode.ordinal(); 
				if(player.isShiftKeyDown())
					index -= 1;
				else
					index += 1;
				if(index >= PROJECTOR_MODES.values().length) {
					index = 0;
				}
				if(index < 0) {
					index = PROJECTOR_MODES.values().length-1;
				}
				laser.mode = PROJECTOR_MODES.values()[index];
			}
			return ActionResultType.SUCCESS;
		}
		temp = Utils.rotateAABB(RED, state.getValue(FACING).getClockWise());
		if(temp.contains(loc)) {
			float value = 0;
			if(state.getValue(FACING) == Direction.NORTH || state.getValue(FACING) == Direction.SOUTH)
				value = (float) ((loc.x - (state.getValue(FACING) == Direction.NORTH ? temp.maxX : temp.minX)) / 
						(temp.maxX - temp.minX));
			else
				value = (float) ((loc.z - (state.getValue(FACING) == Direction.EAST ? temp.maxZ : temp.minZ)) / 
						(temp.maxZ - temp.minZ));
			value = Math.abs(value);
			if(tile instanceof TileEntityLaser) {
				TileEntityLaser laser = ((TileEntityLaser)tile);
				laser.setColor(value, laser.green, laser.blue);
				return ActionResultType.SUCCESS;
			}
			
		}
		temp = Utils.rotateAABB(GREEN, state.getValue(FACING).getClockWise());
		if(temp.contains(loc)) {
			float value = 0;
			if(state.getValue(FACING) == Direction.NORTH || state.getValue(FACING) == Direction.SOUTH)
				value = (float) ((loc.x - (state.getValue(FACING) == Direction.NORTH ? temp.maxX : temp.minX)) / 
						(temp.maxX - temp.minX));
			else
				value = (float) ((loc.z - (state.getValue(FACING) == Direction.EAST ? temp.maxZ : temp.minZ)) / 
						(temp.maxZ - temp.minZ));
			value = Math.abs(value);
			if(tile instanceof TileEntityLaser) {
				TileEntityLaser laser = ((TileEntityLaser)tile);
				laser.setColor(laser.red, value, laser.blue);
				return ActionResultType.SUCCESS;
			}
		}
		temp = Utils.rotateAABB(BLUE, state.getValue(FACING).getClockWise());
		if(temp.contains(loc)) {
			float value = 0;
			if(state.getValue(FACING) == Direction.NORTH || state.getValue(FACING) == Direction.SOUTH)
				value = (float) ((loc.x - (state.getValue(FACING) == Direction.NORTH ? temp.maxX : temp.minX)) / 
						(temp.maxX - temp.minX));
			else
				value = (float) ((loc.z - (state.getValue(FACING) == Direction.EAST ? temp.maxZ : temp.minZ)) / 
						(temp.maxZ - temp.minZ));
			value = Math.abs(value);
			if(tile instanceof TileEntityLaser) {
				TileEntityLaser laser = ((TileEntityLaser)tile);
				laser.setColor(laser.red, laser.green, value);
				return ActionResultType.SUCCESS;
			}
		}
		
		return super.use(state, world, pos, player, hand, hit);
	}
	
	@Override
	public TileEntity newBlockEntity(IBlockReader reader) {
		ModChecker.check();
		return ModChecker.isComputercraftLoaded ? new TileEntityRemoteCC() : new TileEntityLaserController();
	}
	
	@Override
	public void playerWillDestroy(World world, BlockPos pos, BlockState state,
			PlayerEntity player) {
		TileEntity tileentity = world.getBlockEntity(pos);
		if(tileentity instanceof TileEntityRemoteCC) {
			TileEntityRemoteCC te = ((TileEntityRemoteCC)tileentity);
			if(te.isConnected())
				te.Disconnect();
		}
		super.playerWillDestroy(world, pos, state, player);
	}
	
	@Override
	public void destroy(IWorld world, BlockPos pos, BlockState state) {
		TileEntity tileentity = world.getBlockEntity(pos);
		if(tileentity instanceof TileEntityRemoteCC) {
			TileEntityRemoteCC te = ((TileEntityRemoteCC)tileentity);
			if(te.isConnected())
				te.Disconnect();
		}
		super.destroy(world, pos, state);
	}
	
	@Override
	public BlockRenderType getRenderShape(BlockState state) {
		return BlockRenderType.MODEL;
	}
	
	private VoxelShape generateShape()
	{
	    List<VoxelShape> shapes = new ArrayList<>();
	    shapes.add(VoxelShapes.box(0, 0, 0, 0.125, 0.031, 0.125)); // CUBE
	    shapes.add(VoxelShapes.box(0.875, 0, 0, 1, 0.031, 0.125)); // CUBE
	    shapes.add(VoxelShapes.box(0.875, 0, 0.875, 1, 0.031, 1)); // CUBE
	    shapes.add(VoxelShapes.box(0, 0, 0.875, 0.125, 0.031, 1)); // CUBE
	    shapes.add(VoxelShapes.box(0.031, 0.031, 0.906, 0.094, 0.125, 0.969)); // CUBE
	    shapes.add(VoxelShapes.box(0.031, 0.031, 0.031, 0.094, 0.125, 0.094)); // CUBE
	    shapes.add(VoxelShapes.box(0.906, 0.031, 0.031, 0.969, 0.125, 0.094)); // CUBE
	    shapes.add(VoxelShapes.box(0.906, 0.031, 0.906, 0.969, 0.125, 0.969)); // CUBE
	    shapes.add(VoxelShapes.box(0.013, 0.125, 0.013, 0.987, 0.925, 0.987)); // CUBE
	    shapes.add(VoxelShapes.box(0, 0.112, 0.938, 0.062, 0.938, 1)); // CUBE
	    shapes.add(VoxelShapes.box(0.938, 0.112, 0.938, 1, 0.938, 1)); // CUBE
	    shapes.add(VoxelShapes.box(0, 0.112, 0, 0.062, 0.938, 0.062)); // CUBE
	    shapes.add(VoxelShapes.box(0.938, 0.112, 0, 1, 0.938, 0.062)); // CUBE
	    shapes.add(VoxelShapes.box(0.056, 0.112, 0.938, 0.944, 0.175, 1)); // CUBE
	    shapes.add(VoxelShapes.box(0.056, 0.112, 0, 0.944, 0.175, 0.062)); // CUBE
	    shapes.add(VoxelShapes.box(0.056, 0.875, 0, 0.944, 0.938, 0.062)); // CUBE
	    shapes.add(VoxelShapes.box(0.056, 0.875, 0.938, 0.944, 0.938, 1)); // CUBE
	    shapes.add(VoxelShapes.box(0, 0.875, 0.062, 0.062, 0.938, 0.938)); // CUBE
	    shapes.add(VoxelShapes.box(0.938, 0.875, 0.062, 1, 0.938, 0.938)); // CUBE
	    shapes.add(VoxelShapes.box(0.938, 0.112, 0.062, 1, 0.175, 0.938)); // CUBE
	    shapes.add(VoxelShapes.box(0, 0.112, 0.062, 0.062, 0.175, 0.938)); // CUBE

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
	    return SHAPE;
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, IBlockReader reader, BlockPos pos,
			ISelectionContext context) {
		return SHAPE;
	}

}
