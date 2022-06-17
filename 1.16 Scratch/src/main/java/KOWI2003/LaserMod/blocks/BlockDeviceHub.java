package KOWI2003.LaserMod.blocks;

import KOWI2003.LaserMod.tileentities.TileEntityDeviceHub;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class BlockDeviceHub extends BlockHorizontal {

	public BlockDeviceHub(Material material) {
		super(material);
	}
	
	@Override
	public TileEntity newBlockEntity(IBlockReader world) {
		return new TileEntityDeviceHub();
	}

}
