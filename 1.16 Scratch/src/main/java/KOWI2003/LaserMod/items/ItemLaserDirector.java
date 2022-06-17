package KOWI2003.LaserMod.items;

import KOWI2003.LaserMod.init.ModBlocks;
import KOWI2003.LaserMod.tileentities.TileEntityAdvancedLaser;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3f;

public class ItemLaserDirector extends ItemDefault {

//	public ItemLaserDirector() {
//		super(new Item.Properties().tab(MainMod.blocks).setISTER(() -> RenderLinker::new));
//	}
	
	@Override
	public ActionResultType useOn(ItemUseContext context) {
		PlayerEntity player = context.getPlayer();
		ItemStack stack = context.getItemInHand();
		BlockPos pos = context.getClickedPos();
		Block block = context.getLevel().getBlockState(pos).getBlock();
		if(block == ModBlocks.AdvancedLaser && hasPos(stack)) {
			TileEntity tile = context.getLevel().getBlockEntity(pos);
			if(tile instanceof TileEntityAdvancedLaser) {
				((TileEntityAdvancedLaser) tile).setDirection(getPos(stack));
				return ActionResultType.SUCCESS;
			}
		}
		
		if(hasPos(stack) && player.isShiftKeyDown()) {
			stack = setPos(stack, pos);
			player.setItemInHand(context.getHand(), stack);
			return ActionResultType.SUCCESS;
		}
		
		stack = setPos(stack, pos);
		player.setItemInHand(context.getHand(), stack);
		return ActionResultType.SUCCESS;
		
		//return super.useOn(context);
	}
	
	public static ItemStack setPos(ItemStack stack, BlockPos pos) {
		return setPos(stack, new Vector3f(pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f));
	}
	
	public static ItemStack setPos(ItemStack stack, Vector3f pos) {
		CompoundNBT nbt = stack.getOrCreateTag();
		if(pos == null) {
			if(nbt.contains("pos"))
				nbt.remove("pos");
			return stack;
		}
		CompoundNBT posNbt = new CompoundNBT();
		posNbt.putDouble("x", pos.x());
		posNbt.putDouble("y", pos.y());
		posNbt.putDouble("z", pos.z());
		nbt.put("pos", posNbt);
		stack.setTag(nbt);
		return stack;
	}
	
	public static Vector3f getPos(ItemStack stack) {
		CompoundNBT nbt = stack.getOrCreateTag();
		if(nbt.contains("pos")) {
			CompoundNBT posNbt = nbt.getCompound("pos");
			if(posNbt.contains("x") && posNbt.contains("y") && posNbt.contains("z")) {
				return new Vector3f((float)posNbt.getDouble("x"), (float)posNbt.getDouble("y"), (float)posNbt.getDouble("z"));
			}
		}
		return null;
	}
	
	public static boolean hasPos(ItemStack stack) {
		return getPos(stack) != null;
	}
	
}
