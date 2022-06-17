package KOWI2003.LaserMod.tileentities;

import KOWI2003.LaserMod.LaserProperties;
import KOWI2003.LaserMod.LaserProperties.Properties;
import KOWI2003.LaserMod.container.ContainerPrecisionAssembler;
import KOWI2003.LaserMod.init.ModTileTypes;
import KOWI2003.LaserMod.items.ItemUpgradeBase;
import KOWI2003.LaserMod.recipes.precisionAssembler.IPrecisionAssemblerRecipe;
import KOWI2003.LaserMod.recipes.precisionAssembler.PrecisionAssemblerRecipeHandler;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityPrecisionAssembler extends TileEntity implements ITickableTileEntity, INamedContainerProvider {

	IPrecisionAssemblerRecipe currentRecipe = null;
	public ItemStackHandler handler;

	public LaserProperties properties;
	
	private float maxTick = 120;
	private float tick;
	
	public TileEntityPrecisionAssembler() {
		super(ModTileTypes.PRECISION_ASSEMBLER);
		handler = new ItemStackHandler(5);
		properties = new LaserProperties();
		properties.setProperty(Properties.SPEED, 1f);
	}

	@Override
	public CompoundNBT save(CompoundNBT nbt) {
		nbt.put("inv", handler.serializeNBT());
		properties.save(nbt);
		return super.save(nbt);
	}
	
	@Override
	public void load(BlockState state, CompoundNBT nbt) {
		if(nbt.contains("inv"))
			handler.deserializeNBT(nbt.getCompound("inv"));
		properties.load(nbt);
		super.load(state, nbt);
	}
	
	@Override
	public void tick() {
		if(currentRecipe == null || !currentRecipe.isRecipeValid(this.getHandler())) {
			IPrecisionAssemblerRecipe recipe = PrecisionAssemblerRecipeHandler.getRecipe(this);
			if(recipe != currentRecipe) {
				tick = maxTick;
				currentRecipe = recipe;
			}
		}
		
		if(currentRecipe != null) {
			tick -= currentRecipe.getRecipeSpeed() * properties.getProperty(Properties.SPEED);
			
			if(tick <= 0) {
				PrecisionAssemblerRecipeHandler.handleRecipeEnd(currentRecipe, this);
				tick = maxTick;
				currentRecipe = null;
			}
		}
	}
	
	public float getProgress() {
		return tick/maxTick;
	}
	
	public ItemStackHandler getHandler() {
		return handler;
	}
	
	@Override
	public Container createMenu(int windowId, PlayerInventory playerInv, PlayerEntity player) {
		return new ContainerPrecisionAssembler(windowId, playerInv, this);
	}

	@Override
	public ITextComponent getDisplayName() {
		return new TranslationTextComponent("container.lasermod.precision_assembler");
	}
	
	public ItemStackHandler getUpgradeInv() {
		return properties.createHandler(1);
	}
	
	public boolean acceptsItem(ItemUpgradeBase upgrade, boolean simulate) {
		return upgrade.isUsefullForMachine(getBlockState().getBlock()) && (simulate ? properties.doesAllow(upgrade) : properties.addUpgrade(upgrade));
	}
	
	public boolean remove(ItemUpgradeBase upgarde, boolean simulate) {
		return simulate ? properties.hasUpgarde(upgarde) : properties.removeUpgrade(upgarde) != null;
	}
}
