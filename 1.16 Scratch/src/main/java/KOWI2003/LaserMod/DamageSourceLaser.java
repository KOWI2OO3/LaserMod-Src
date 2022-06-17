package KOWI2003.LaserMod;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class DamageSourceLaser extends DamageSource {
	
	boolean isFire = false;
	
	public DamageSourceLaser(String damageTypeIn, boolean isFire) {
		super(damageTypeIn);
		this.isFire = isFire;
	}
	
	@Override
	public boolean isFire() {
		return isFire;
	}
	
	@Override
	public ITextComponent getLocalizedDeathMessage(LivingEntity entityLivingBaseIn) {
		return new TranslationTextComponent("death.block.laser", entityLivingBaseIn.getDisplayName());
	}
	
	public static class DamageSourceLaserArmor extends DamageSource {
		boolean isFire = false;
		LivingEntity defender;
		
		public DamageSourceLaserArmor(String damageTypeIn, boolean isFire) {
			super(damageTypeIn);
			this.isFire = isFire;
		}
		
		public DamageSourceLaserArmor(String damageTypeIn, boolean isFire, LivingEntity defender) {
			super(damageTypeIn);
			this.isFire = isFire;
			this.defender = defender;
		}
		
		@Override
		public boolean isFire() {
			return isFire;
		}
		
		
		@Override
		public ITextComponent getLocalizedDeathMessage(LivingEntity entityLivingBaseIn) {
			return new TranslationTextComponent("death.armor.laser", entityLivingBaseIn.getDisplayName(), getEntityName());
		}
		
		ITextComponent getEntityName() { 
			return defender != null ? defender.getDisplayName() : new StringTextComponent("someone");
		}
	}
}
