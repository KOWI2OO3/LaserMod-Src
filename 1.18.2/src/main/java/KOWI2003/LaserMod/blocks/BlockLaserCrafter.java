package KOWI2003.LaserMod.blocks;

import KOWI2003.LaserMod.tileentities.TileEntityLaserCrafter;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;

public class BlockLaserCrafter extends BlockHorizontal {

	public BlockLaserCrafter(Material materialIn) {
		super(materialIn);
	}
	
	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand,
			BlockHitResult raytraceResult) {
		if(!world.isClientSide) {
			BlockEntity te = world.getBlockEntity(pos);
			if(te instanceof TileEntityLaserCrafter) {
				NetworkHooks.openGui((ServerPlayer)player, (TileEntityLaserCrafter)te, pos);
				return InteractionResult.SUCCESS;
			}
		}
		return InteractionResult.FAIL;
	}
	
	@Override
	public RenderShape getRenderShape(BlockState p_49232_) {
		return RenderShape.MODEL;
	}
	
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntityLaserCrafter(pos, state);
	}
	

}
