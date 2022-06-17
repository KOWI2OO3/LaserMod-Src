package KOWI2003.LaserMod.items.upgrades;

import java.util.List;

import KOWI2003.LaserMod.items.ItemUpgradeBase;
import KOWI2003.LaserMod.tileentities.ILaserAccess;
import KOWI2003.LaserMod.utils.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class UpgradeFire extends ItemUpgradeBase {

	public UpgradeFire(String name) {
		super(name);
		setCanBeUsedForLaser(true);
		setCanBeUsedForLaserTools(true);
		setCanBeUsedForLaserArmor(true);
		AbilityNames = new String[] {"Fire Aspect", "Auto Smelt"};
		abilityNameColor = new float[] {1f,  0.1f, 0.1f};
	}
	
	@Override
	public void runLaserBlock(ILaserAccess te, BlockPos pos) {
		BlockState state = te.getTileEntity().getLevel().getBlockState(pos);
		if(state.isFlammable(te.getTileEntity().getLevel(), pos, te.getDirection().getOpposite())) {
			te.getTileEntity().getLevel().setBlock(Utils.offset(pos, te.getDirection().getOpposite(), 1), Blocks.FIRE.defaultBlockState(), 0);
		}
		Direction dire = te.getDirection();
		List<LivingEntity> entities = te.getEntitiesInLaser(LivingEntity.class);
		
		for (LivingEntity entity : entities) {
			entity.setSecondsOnFire(5);
		}
	}
	
	@Override
	public String[] getArmorAbilityNames() {
		return new String[] {"Fire Armor"};
	}
	
	@Override
	public void runLaserToolHitEnemy(ItemStack item, LivingEntity enemy, LivingEntity player) {
		enemy.setSecondsOnFire(5);
	}	
	
	@Override
	public void runOnEntityHitArmor(ItemStack item, LivingEntity attacker, LivingEntity player, float damageAmount) {
		attacker.setSecondsOnFire(5);
	}
}
