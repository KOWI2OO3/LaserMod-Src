package KOWI2003.LaserMod.tileentities;

import KOWI2003.LaserMod.LaserProperties;
import KOWI2003.LaserMod.LaserProperties.Properties;
import KOWI2003.LaserMod.container.ContainerInfuser;
import KOWI2003.LaserMod.init.ModTileTypes;
import KOWI2003.LaserMod.items.ItemUpgradeBase;
import KOWI2003.LaserMod.recipes.infuser.IInfuserRecipe;
import KOWI2003.LaserMod.recipes.infuser.InfuserRecipeHandler;
import KOWI2003.LaserMod.utils.TileEntityUtils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityInfuser extends TileEntity implements ITickableTileEntity, INamedContainerProvider {

	public ItemStackHandler handler;
	LaserProperties properties;
	private IInfuserRecipe currentRecipe = null;
	
	private float maxTick = 120;
	private float tick;
	
	public TileEntityInfuser() {
		super(ModTileTypes.INFUSER);
		handler = new ItemStackHandler(3);
		tick = maxTick;
		properties = new LaserProperties();
		properties.setProperty(Properties.SPEED, 1f);
	}
	
	@Override
	public void tick() {
		if(currentRecipe == null || !currentRecipe.isRecipeValid(this)) {
			IInfuserRecipe recipe = InfuserRecipeHandler.getRecipe(this);
			if(recipe != currentRecipe) {
				tick = maxTick;
				currentRecipe = recipe;
			}
		}
		
		if(currentRecipe != null) {
			tick -= currentRecipe.getRecipeSpeed() * properties.getProperty(Properties.SPEED);
			
			if(tick <= 0) {
				InfuserRecipeHandler.handleRecipeEnd(currentRecipe, this);
				tick = maxTick;
				currentRecipe = null;
			}
		}
	}
	
	@Override
	public CompoundNBT save(CompoundNBT nbt) {
		nbt.put("inv", handler.serializeNBT());
		nbt.putFloat("tick", tick);
		properties.save(nbt);
		return super.save(nbt);
	}
	
	@Override
	public void load(BlockState state, CompoundNBT nbt) {
		handler.deserializeNBT(nbt.getCompound("inv"));
		tick = nbt.getFloat("tick");
		properties.load(nbt);
		super.load(state, nbt);
	}

	public float getProgress() {
		return tick/maxTick;
	}
	
	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		return new SUpdateTileEntityPacket(getBlockPos(), 0, getUpdateTag());
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		this.load(getLevel().getBlockState(pkt.getPos()), pkt.getTag());
	}
	
	@Override
	public CompoundNBT getUpdateTag() {
		return save(new CompoundNBT());
	}
	
	@Override
	public void handleUpdateTag(BlockState state, CompoundNBT tag) {
		this.load(state, tag);
	}
	
	@Override
	public CompoundNBT getTileData() {
		return this.serializeNBT();
	}
	
	public void sync()
    {
        TileEntityUtils.syncToClient(this);
    }
	
	@Override
	public Container createMenu(int windowId, PlayerInventory playerInv, PlayerEntity player) {
		return new ContainerInfuser(windowId, playerInv, this);
	}

	@Override
	public ITextComponent getDisplayName() {
		return new TranslationTextComponent("container.lasermod.infuser");
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
