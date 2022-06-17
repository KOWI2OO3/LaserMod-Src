package KOWI2003.LaserMod.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;

public abstract class ContainerBlockDefault extends ContainerBlock {
	
	protected ContainerBlockDefault(Properties builder) {
		super(builder);
	}

	public ContainerBlockDefault(Material materialIn) {
		this(Properties.of(materialIn));	//This is Changed
	}
	
	public ContainerBlockDefault() {
		this(Material.STONE);				//This is Changed
	}
	
	@Override
	public boolean isToolEffective(BlockState state, ToolType tool) {
		return tool == ToolType.PICKAXE;
	}
}
