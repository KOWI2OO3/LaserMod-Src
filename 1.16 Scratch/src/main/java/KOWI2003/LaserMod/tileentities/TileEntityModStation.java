package KOWI2003.LaserMod.tileentities;

import KOWI2003.LaserMod.LaserProperties;
import KOWI2003.LaserMod.container.ContainerModStation;
import KOWI2003.LaserMod.init.ModTileTypes;
import KOWI2003.LaserMod.items.ItemUpgradeBase;
import KOWI2003.LaserMod.items.interfaces.ILaserUpgradable;
import KOWI2003.LaserMod.utils.LaserItemUtils;
import KOWI2003.LaserMod.utils.TileEntityUtils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityModStation extends TileEntity implements INamedContainerProvider {

	public ItemStackHandler handler;
	
	public TileEntityModStation() {
		super(ModTileTypes.MOD_STATION);
		handler = new ItemStackHandler(2);
	}
	
	@Override
	public void load(BlockState state, CompoundNBT nbt) {
		if(nbt.contains("inv"))
			handler.deserializeNBT(nbt.getCompound("inv"));
		super.load(state, nbt);
	}
	
	@Override
	public CompoundNBT save(CompoundNBT nbt) {
		nbt.put("inv", handler.serializeNBT());
		return super.save(nbt);
	}

	public void addUpgrade() {
		ItemStack stack = handler.getStackInSlot(0);
		if(!stack.isEmpty() && stack.getItem() instanceof ILaserUpgradable) {
			ItemStack upgradeStack = handler.getStackInSlot(1);
			LaserProperties properties = LaserItemUtils.getProperties(stack);
			if(!upgradeStack.isEmpty() && upgradeStack.getItem() instanceof ItemUpgradeBase) {
				if(!((ILaserUpgradable)stack.getItem()).canBeUsed(((ItemUpgradeBase) upgradeStack.getItem())))
					return;
				if(properties.addUpgrade((ItemUpgradeBase) upgradeStack.getItem())) {
					handler.extractItem(1, 1, false);
					stack = LaserItemUtils.setProperties(stack, properties);
					handler.setStackInSlot(0, stack);
				}
			}
		}
		TileEntityUtils.syncToClient(this);
	}
	
	public void removeUpgrade(String upgradeName) {
		ItemStack stack = handler.getStackInSlot(0);
		if(!stack.isEmpty() && stack.getItem() instanceof ILaserUpgradable) {
			LaserProperties properties = LaserItemUtils.getProperties(stack);
			ItemUpgradeBase item = properties.removeUpgrade(upgradeName);
			if(item != null) {
				if(handler.getStackInSlot(1).getItem() == item || handler.getStackInSlot(1).isEmpty()) {
					ItemStack returnStack = handler.insertItem(1, new ItemStack(item), false);
					if(!returnStack.isEmpty())
						return;
					stack = LaserItemUtils.setProperties(stack, properties);
					handler.setStackInSlot(0, stack);
				}
			}
		}
		TileEntityUtils.syncToClient(this);
	}
	
	public float[] getColor() {
		ItemStack stack = handler.getStackInSlot(0);
		if(!stack.isEmpty() && stack.getItem() instanceof ILaserUpgradable) {
			LaserProperties properties = LaserItemUtils.getProperties(stack);
			if(properties.hasUpgarde("color")) {
				return LaserItemUtils.getColor(stack);
			}
		}
		return new float[] {1.0f, 0.0f, 0.0f};
	}
	
	public void setColor(float red, float green, float blue) {
		ItemStack stack = handler.getStackInSlot(0);
		if(!stack.isEmpty() && stack.getItem() instanceof ILaserUpgradable) {
			LaserProperties properties = LaserItemUtils.getProperties(stack);
			if(properties.hasUpgarde("color")) {
				stack = LaserItemUtils.setColor(stack, red, green, blue);
			}else
				stack = LaserItemUtils.setColor(stack, 1.0f, 0.0f, 0.0f);
			handler.setStackInSlot(0, stack);
		}
		TileEntityUtils.syncToClient(this);
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
		return new ContainerModStation(windowId, playerInv, this);
	}

	@Override
	public ITextComponent getDisplayName() {
		return new TranslationTextComponent("block.lasermod.mod_station");
	}

}
