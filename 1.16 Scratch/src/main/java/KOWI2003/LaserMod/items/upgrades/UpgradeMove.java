package KOWI2003.LaserMod.items.upgrades;

import java.util.List;

import KOWI2003.LaserMod.items.ItemUpgradeBase;
import KOWI2003.LaserMod.network.PacketDeltaMovment;
import KOWI2003.LaserMod.network.PacketHandler;
import KOWI2003.LaserMod.tileentities.ILaserAccess;
import KOWI2003.LaserMod.tileentities.TileEntityAdvancedLaser;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;

public class UpgradeMove extends ItemUpgradeBase {

	private boolean isPush = true;
	
	public UpgradeMove(String name, int tier, boolean isPush) {
		super(name, tier);
		this.isPush = isPush;
		setCanBeUsedForLaser(true);
		setCanBeUsedForLaserTools(true);
		AbilityNames = isPush ? new String[] {"Knockback"} : new String[] {"Attract"};
		abilityNameColor = isPush ? new float[] {0.35f, 0.5f, 0.35f} : new float[] {0.5f,  1f, 0.5f};
	}
	
	public UpgradeMove(String name, boolean isPush) {
		super(name);
		this.isPush = isPush;
		setCanBeUsedForLaser(true);
		setCanBeUsedForLaserTools(true);
		AbilityNames = isPush ? new String[] {"Knockback"} : new String[] {"Attract"};
		abilityNameColor = isPush ? new float[] {0.35f, 0.5f, 0.35f} : new float[] {0.5f,  1f, 0.5f};
	}
	
	@Override
	public void runLaserToolHitEnemy(ItemStack item, LivingEntity enemy, LivingEntity player) {
		float strenght = 0.8f;
		if(!isPush)
			enemy.knockback(strenght, player.getDirection().getStepX(), player.getDirection().getStepZ());
		else
			enemy.knockback(strenght, -player.getDirection().getStepX(), -player.getDirection().getStepZ());
		super.runLaserToolHitEnemy(item, enemy, player);
	}
	
	@Override
	public void runLaserBlock(ILaserAccess te, BlockPos pos) {
		Direction dire = te.getDirection();
		List<Entity> entities = te.getEntitiesInLaser(Entity.class);
		
		for (Entity entity : entities) {
			Vector3d entityPos = entity.position();
			
			float speed = 0.05f;
			
			if(!isPush)
				speed *= -1;
			if(hasTier())
				speed *= getTier();
			
			//entityPos = entityPos.add(new Vec3(dire.getStepX() * speed, dire.getStepY() * speed, dire.getStepZ() * speed));
			
			if(dire == Direction.UP || dire == Direction.DOWN || te.getLaser() instanceof TileEntityAdvancedLaser) {
				entity.setDeltaMovement(entity.getDeltaMovement().x, entity.getDeltaMovement().y + 0.05f, entity.getDeltaMovement().z);
			}
			
			Vector3f dir = te.getForward();
			dir.normalize();
			dir.mul(speed);
			Vector3d vec = new Vector3d(dir);
			entity.setDeltaMovement(entity.getDeltaMovement().add(vec));
			if(entity instanceof PlayerEntity)
			{
				PlayerEntity player = (PlayerEntity) entity;
				Vector3d playerPos = player.position();
				Vector3d movement = new Vector3d(dire.getStepX() * speed, dire.getStepY() * speed, dire.getStepZ() * speed);
				playerPos = playerPos.add(movement);
				
				if(player instanceof ServerPlayerEntity) {
					ServerPlayerEntity sPlayer = (ServerPlayerEntity)player;
					PacketHandler.sendToClient(new PacketDeltaMovment(movement), sPlayer);
				}
			}
		}
		super.runLaserBlock(te, pos);
	}
	
	
}
