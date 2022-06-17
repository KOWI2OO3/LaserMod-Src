package KOWI2003.LaserMod.items.upgrades;

import KOWI2003.LaserMod.init.ModBlocks;
import KOWI2003.LaserMod.items.ItemUpgradeBase;
import net.minecraft.block.Block;

public class UpgradeSpeed extends ItemUpgradeBase {

	public UpgradeSpeed(String name) {
		super(name);
		setCanBeUsedForLaserTools(true);
		USEFULL_MACHINES.add(ModBlocks.Infuser);
		USEFULL_MACHINES.add(ModBlocks.PrecisionAssembler);
		AbilityNames = new String[] {"Efficiency"};
		abilityNameColor = new float[] {0.1f, .9f, .8f};
	}
	
	@Override
	public void getMachineUse(Block Machine) {
		
		super.getMachineUse(Machine);
	}
	
	@Override
	public float getMultiplier(KOWI2003.LaserMod.LaserProperties.Properties property) {
		if(property == KOWI2003.LaserMod.LaserProperties.Properties.SPEED)
			return 2f;
		return super.getMultiplier(property);
	}
	
}
