package KOWI2003.LaserMod.items;

import KOWI2003.LaserMod.MainMod;
import KOWI2003.LaserMod.init.ModBlocks;
import KOWI2003.LaserMod.items.render.RenderLinker;
import KOWI2003.LaserMod.tileentities.TileEntityDeviceHub;
import KOWI2003.LaserMod.tileentities.TileEntityLaserController;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;

public class ItemLinker extends ItemDefault {
	
	public ItemLinker() {
		super(new Item.Properties().tab(MainMod.blocks).setISTER(() -> RenderLinker::new));
	}
	
	@Override
	public ActionResultType useOn(ItemUseContext context) {
		PlayerEntity player = context.getPlayer();
		ItemStack stack = context.getItemInHand();
		BlockPos pos = context.getClickedPos();
		Block block = context.getLevel().getBlockState(pos).getBlock();
		if(block == ModBlocks.Laser || block == ModBlocks.LaserProjector || block == ModBlocks.AdvancedLaser) {
			stack = setPos(stack, pos);
			player.setItemInHand(context.getHand(), stack);
			return ActionResultType.SUCCESS;
		}else if(block == ModBlocks.LaserController) {
			BlockPos laserPos = getPos(stack);
			if(laserPos != null) {
				TileEntity tile = context.getLevel().getBlockEntity(pos);
				if(tile instanceof TileEntityLaserController) {
					((TileEntityLaserController)tile).link(laserPos);
				
					stack = setPos(stack, null);
					player.setItemInHand(context.getHand(), stack);
					
					return ActionResultType.SUCCESS;
				}
			}
		}else if(block == ModBlocks.DeviceHub)
		{
			BlockPos devicePos = getPos(stack);
			if(devicePos != null) {
				TileEntity tile = context.getLevel().getBlockEntity(pos);
				if(tile instanceof TileEntityDeviceHub) {
					((TileEntityDeviceHub)tile).link(devicePos);
				
					stack = setPos(stack, null);
					player.setItemInHand(context.getHand(), stack);
					
					return ActionResultType.SUCCESS;
				}
			}
		}else if(hasPos(stack) && player.isShiftKeyDown()) {
			stack = setPos(stack, null);
			player.setItemInHand(context.getHand(), stack);
			return ActionResultType.SUCCESS;
		}
		
		
		return super.useOn(context);
	}
	
	public static ItemStack setPos(ItemStack stack, BlockPos pos) {
		CompoundNBT nbt = stack.getOrCreateTag();
		if(pos == null) {
			if(nbt.contains("pos"))
				nbt.remove("pos");
			return stack;
		}
		CompoundNBT posNbt = new CompoundNBT();
		posNbt.putInt("x", pos.getX());
		posNbt.putInt("y", pos.getY());
		posNbt.putInt("z", pos.getZ());
		nbt.put("pos", posNbt);
		stack.setTag(nbt);
		return stack;
	}
	
	public static BlockPos getPos(ItemStack stack) {
		CompoundNBT nbt = stack.getOrCreateTag();
		if(nbt.contains("pos")) {
			CompoundNBT posNbt = nbt.getCompound("pos");
			if(posNbt.contains("x") && posNbt.contains("y") && posNbt.contains("z")) {
				return new BlockPos(posNbt.getInt("x"), posNbt.getInt("y"), posNbt.getInt("z"));
			}
		}
		return null;
	}
	
	public static boolean hasPos(ItemStack stack) {
		return getPos(stack) != null;
	}
	
}
