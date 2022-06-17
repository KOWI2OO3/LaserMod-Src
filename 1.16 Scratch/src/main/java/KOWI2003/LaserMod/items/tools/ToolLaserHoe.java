package KOWI2003.LaserMod.items.tools;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;

import KOWI2003.LaserMod.items.ItemLaserToolBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ToolLaserHoe extends ItemLaserToolBase {
	private static final Set<Block> DIGGABLES = ImmutableSet.of(Blocks.NETHER_WART_BLOCK, Blocks.WARPED_WART_BLOCK, Blocks.HAY_BLOCK, Blocks.DRIED_KELP_BLOCK, Blocks.TARGET, Blocks.SHROOMLIGHT, Blocks.SPONGE, Blocks.WET_SPONGE, Blocks.JUNGLE_LEAVES, Blocks.OAK_LEAVES, Blocks.SPRUCE_LEAVES, Blocks.DARK_OAK_LEAVES, Blocks.ACACIA_LEAVES, Blocks.BIRCH_LEAVES);
	protected static final Map<Block, BlockState> TILLABLES = Maps.newHashMap(ImmutableMap.of(Blocks.GRASS_BLOCK, Blocks.FARMLAND.defaultBlockState(), Blocks.GRASS_PATH, Blocks.FARMLAND.defaultBlockState(), Blocks.DIRT, Blocks.FARMLAND.defaultBlockState(), Blocks.COARSE_DIRT, Blocks.DIRT.defaultBlockState()));

	
	public ToolLaserHoe(Properties properties, float speed, float damageBaseline, int maxCharge) {
		super(properties, DIGGABLES, speed, damageBaseline, maxCharge);
	}
	
	public ActionResultType useOn(ItemUseContext context) {
		if(isExtended(context.getPlayer().getItemInHand(context.getHand()))) {
		      World world = context.getLevel();
		      BlockPos blockpos = context.getClickedPos();
		      int hook = net.minecraftforge.event.ForgeEventFactory.onHoeUse(context);
		      if (hook != 0) return hook > 0 ? ActionResultType.SUCCESS : ActionResultType.FAIL;
		      if (context.getClickedFace() != Direction.DOWN && world.isEmptyBlock(blockpos.above())) {
		         BlockState blockstate = world.getBlockState(blockpos).getToolModifiedState(world, blockpos, context.getPlayer(), context.getItemInHand(), net.minecraftforge.common.ToolType.HOE);
		         if (blockstate != null) {
		            PlayerEntity playerentity = context.getPlayer();
		            world.playSound(playerentity, blockpos, SoundEvents.HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
		            if (!world.isClientSide) {
		               world.setBlock(blockpos, blockstate, 11);
		               if (playerentity != null) {
		            	   context.getItemInHand().hurtAndBreak(1, playerentity, (p_220043_1_) -> {
		                     p_220043_1_.broadcastBreakEvent(context.getHand());
		                  });
		               }
		            }
	
		            return ActionResultType.sidedSuccess(world.isClientSide);
		         }
		      }
		  }

	      return ActionResultType.PASS;
	   }

	   @javax.annotation.Nullable
	   public static BlockState getHoeTillingState(BlockState originalState) {
	      return TILLABLES.get(originalState.getBlock());
	   }
}
