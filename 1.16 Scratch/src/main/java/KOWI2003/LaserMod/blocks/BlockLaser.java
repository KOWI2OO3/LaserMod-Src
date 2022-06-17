package KOWI2003.LaserMod.blocks;

import java.util.List;

import KOWI2003.LaserMod.items.ItemUpgradeBase;
import KOWI2003.LaserMod.tileentities.TileEntityLaser;
import KOWI2003.LaserMod.utils.Utils;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class BlockLaser extends BlockRotatable {
	
	protected static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(0.3D, 0.3D, 0.85D, 0.7D, 0.7D, 1.0D);
	protected static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(0.0D, 0.3D, 0.30D, 0.15D, 0.7D, 0.7D);
	protected static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0.3D, 0.3D, 0.0D, 0.7D, 0.7D, 0.15D);
	protected static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(0.85D, 0.3D, 0.30D, 1.0D, 0.7D, 0.7D);

	protected static final AxisAlignedBB UP_AABB = new AxisAlignedBB(0.3D, 0.0D, 0.3D, 0.7D, 0.15D, 0.7D);
	protected static final AxisAlignedBB DOWN_AABB = new AxisAlignedBB(0.3D, 0.85D, 0.3D, 0.7D, 1.0D, 0.7D);
	
	public int tickCounter;
	
	public BlockLaser(Material materialIn) {
		super(materialIn);
		this.setModelPlacement(true);
	}
	
	public BlockLaser(Properties properties) {
		super(properties);
		this.setModelPlacement(true);
	}
	
	@Override
	public ActionResultType use(BlockState state, World world, BlockPos pos,
			PlayerEntity player, Hand hand, BlockRayTraceResult raytraceResult) {
		if(!world.isClientSide) {
			TileEntity te = world.getBlockEntity(pos);
			if(te instanceof TileEntityLaser) {
				NetworkHooks.openGui((ServerPlayerEntity)player, (TileEntityLaser) te, pos);
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
	public VoxelShape getVisualShape(BlockState p_230322_1_, IBlockReader p_230322_2_, BlockPos p_230322_3_,
			ISelectionContext p_230322_4_) {
		return getShape(p_230322_1_, p_230322_2_, p_230322_3_, p_230322_4_);
	}
	
	@Override
	public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos,
				ISelectionContext context) {
		switch((Direction)state.getValue(FACING)) {
			case NORTH:
				return Utils.getShapeFromAABB(NORTH_AABB);
			case EAST:
				return Utils.getShapeFromAABB(EAST_AABB);
			case SOUTH:
				return Utils.getShapeFromAABB(SOUTH_AABB);
			case WEST:
				return Utils.getShapeFromAABB(WEST_AABB);
			case UP:
				return Utils.getShapeFromAABB(UP_AABB);
			case DOWN:
				return Utils.getShapeFromAABB(DOWN_AABB);
			default:
				return VoxelShapes.create(UP_AABB);
		}
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		switch((Direction)state.getValue(FACING)) {
			case NORTH:
				return Utils.getShapeFromAABB(NORTH_AABB);
			case EAST:
				return Utils.getShapeFromAABB(EAST_AABB);
			case SOUTH:
				return Utils.getShapeFromAABB(SOUTH_AABB);
			case WEST:
				return Utils.getShapeFromAABB(WEST_AABB);
			case UP:
				return Utils.getShapeFromAABB(UP_AABB);
			case DOWN:
				return Utils.getShapeFromAABB(DOWN_AABB);
			default:
				return VoxelShapes.create(UP_AABB);
		}
	}

	@Override
	public void playerWillDestroy(World world, BlockPos pos, BlockState state,
			PlayerEntity player) {
		TileEntity tileentity = world.getBlockEntity(pos);
		if(tileentity instanceof TileEntityLaser) {
			TileEntityLaser te = ((TileEntityLaser)tileentity);
			te.handleTurnOffForInteractable();
			List<ItemUpgradeBase> items = te.getProperties().getUpgrades();
			if(items.size() > 0)
				for (ItemUpgradeBase item : items) {
					if(item != null)
						popResource((World) world, pos, new ItemStack(item));
				}
		}
		super.playerWillDestroy(world, pos, state, player);
	}
	
	@Override
	public TileEntity newBlockEntity(IBlockReader p_196283_1_) {
		return new TileEntityLaser();
	}

}
