package KOWI2003.LaserMod.items.tools;

import com.google.common.collect.ImmutableSet;

import KOWI2003.LaserMod.items.ItemLaserToolBase;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ToolLaserSword extends ItemLaserToolBase {

	public ToolLaserSword(Properties properties, float speed, float damageBaseline, int maxCharge) {
		super(properties, ImmutableSet.of(), speed, damageBaseline, maxCharge);
	}
	
	@Override
	public boolean canAttackBlock(BlockState state, World world, BlockPos pos,
			PlayerEntity entity) {
		return !entity.isCreative();
	}

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		if(isExtended(stack)) {
	      if (state.is(Blocks.COBWEB)) {
	         return 15.0F;
	      } else {
	         Material material = state.getMaterial();
	         return material != Material.PLANT && material != Material.REPLACEABLE_PLANT && material != Material.CORAL && !state.is(BlockTags.LEAVES) && material != Material.VEGETABLE ? 1.0F : 1.5F;
	      }
		}
		return state.requiresCorrectToolForDrops() ? 0f : 1f;
    }
	
	public boolean isCorrectToolForDrops(BlockState state) {
	      return state.is(Blocks.COBWEB);
	}
	
}
