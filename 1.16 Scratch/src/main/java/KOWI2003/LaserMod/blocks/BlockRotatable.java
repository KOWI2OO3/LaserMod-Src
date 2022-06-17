package KOWI2003.LaserMod.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;

public abstract class BlockRotatable extends ContainerBlockDefault {

	public static final DirectionProperty FACING = BlockStateProperties.FACING;
	
	private boolean ModelPlacement = false;
	
	public BlockRotatable(Material materialIn) {
		super(Block.Properties.of(materialIn));
	}
	
	public BlockRotatable(Properties properties) {
		super(properties);
	}
	
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}
	
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}
	
	@Override
	public boolean isToolEffective(BlockState state, ToolType tool) {
		return tool == ToolType.PICKAXE;
	}
	
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		if(!this.ModelPlacement) {
			float rot = context.getPlayer().getRotationVector().x;
			if(rot > 90 - 45.5f)
				return super.getStateForPlacement(context).setValue(FACING, Direction.DOWN);
			else if(rot < -90 + 45.5f)
					return super.getStateForPlacement(context).setValue(FACING, Direction.UP);
			return super.getStateForPlacement(context).setValue(FACING, context.getPlayer().getDirection());
		}else if(this.ModelPlacement)
			return super.getStateForPlacement(context).setValue(FACING, context.getClickedFace());
		else
			return super.getStateForPlacement(context);
	}
	
	public void setModelPlacement(boolean modelPlacement) {
		ModelPlacement = modelPlacement;
	}
	
	@Override
	public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos,
			PlayerEntity player) {
		return new ItemStack(this, 1);
	}
	
}
