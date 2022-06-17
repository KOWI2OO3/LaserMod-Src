package KOWI2003.LaserMod.utils;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityUtils {

	public static void markBlockForUpdate(World world, BlockPos pos)
	 {
		world.sendBlockUpdated(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
	 }
	
	public static void syncToClient(TileEntity tileEntity)
    {
        World world = tileEntity.getLevel();
        if(world != null)
        {
            BlockPos pos = tileEntity.getBlockPos();
            world.sendBlockUpdated(pos, world.getBlockState(pos), world.getBlockState(pos), 4);
        }
    }
	
	public static String StringCommands(PlayerEntity player, String s) {
		if(s.contains("{Player}")) {
        	s = s.replace("{Player}", player.getName().getString());
        }
        
        if(s.contains("{HP}")) {
        	s = s.replace("{HP}", (int)player.getHealth() + "");
        }
        
        if(s.contains("{Hunger}")) {
        	s = s.replace("{Hunger}", player.getFoodData().getFoodLevel() + "");
        }
        return s;
	}
}
