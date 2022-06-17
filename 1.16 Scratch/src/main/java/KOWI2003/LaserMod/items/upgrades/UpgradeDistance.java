package KOWI2003.LaserMod.items.upgrades;

import KOWI2003.LaserMod.items.ItemUpgradeBase;

public class UpgradeDistance extends ItemUpgradeBase {

	public UpgradeDistance(String name, int tier) {
		super(name, tier);
		setCanBeUsedForLaser(true);
	}
	
	public UpgradeDistance(String name) {
		super(name);
		setCanBeUsedForLaser(true);
	}
	
	@Override
	public float getMultiplier(KOWI2003.LaserMod.LaserProperties.Properties property) {
		if(property == KOWI2003.LaserMod.LaserProperties.Properties.MAX_DISTANCE)
			return 1 + getTierOr(1);
		return super.getMultiplier(property);
	}
	
}
