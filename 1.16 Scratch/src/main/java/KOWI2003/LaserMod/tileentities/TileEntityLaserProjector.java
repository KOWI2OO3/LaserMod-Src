package KOWI2003.LaserMod.tileentities;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.mojang.authlib.GameProfile;

import KOWI2003.LaserMod.container.ContainerLaserProjector;
import KOWI2003.LaserMod.init.ModTileTypes;
import KOWI2003.LaserMod.tileentities.TileEntityLaserProjector.ProjectorProperties.PROJECTOR_PROPERTY;
import KOWI2003.LaserMod.utils.TileEntityUtils;
import KOWI2003.LaserMod.utils.Utils;
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
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityLaserProjector extends TileEntity implements ITickableTileEntity, INamedContainerProvider {

	public PROJECTOR_MODES mode = PROJECTOR_MODES.TEXT;
	public ProjectorProperties properties;
	public ItemStackHandler handler;
	
	public boolean isActive;
	public boolean isRemoteControlled;
	
	public float rotation = 0;
	
	//Other Variables
	public String text = "";
	public boolean doesRotate = true;
	public boolean liveModel = false;
	public boolean isChild = false;
	
	public PlayerEntity playerToRender;
	public GameProfile profile;
	
	public TileEntityLaserProjector() {
		super(ModTileTypes.LASER_PROJECTOR);
		handler = new ItemStackHandler(3);
		properties = new ProjectorProperties();
	}
	
	@Override
	public void tick() {
		if(isActive) {
			if(doesRotate) {
				rotation += properties.getProperty(PROJECTOR_PROPERTY.SPEED)*2f;
				if(rotation >= 360)
					rotation = 0;
			}
			if(mode == PROJECTOR_MODES.PLAYER) {
				if(profile == null && !liveModel)
					profile = Utils.getProfile(text);
				if(playerToRender == null && liveModel){
					playerToRender = Utils.getPlayer(level, text);
				}
			}
		}
		
		if(!isRemoteControlled) {
			if(isActive != Utils.isBlockPowered(getBlockPos(), getLevel())) {
				isActive = Utils.isBlockPowered(getBlockPos(), getLevel());
			}
		}
	}

	@Override
	public double getViewDistance()
	{
		return 16384;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public AxisAlignedBB getRenderBoundingBox()
	{
		return INFINITE_EXTENT_AABB;
	}
	
	@Override
	public CompoundNBT save(CompoundNBT nbt) {
		nbt.putString("Mode", mode.name());
		properties.save(nbt);
		nbt.putString("Text", text);
		nbt.putBoolean("doesRotate", doesRotate);
		nbt.putBoolean("liveModel", liveModel);
		nbt.putBoolean("isChild", isChild);
		nbt.put("inv", handler.serializeNBT());
		nbt.putBoolean("isRemoteControlled", isRemoteControlled);
		nbt.putBoolean("isActive", isActive);
		return super.save(nbt);
	}
	
	@Override
	public void load(BlockState state, CompoundNBT nbt) {
		if(nbt.contains("inv"))
			handler.deserializeNBT(nbt.getCompound("inv"));
		if(nbt.contains("Mode"))
			mode = PROJECTOR_MODES.valueOf(nbt.getString("Mode"));
		else 
			mode = PROJECTOR_MODES.TEXT;
		if(nbt.contains("Text"))
			text = nbt.getString("Text");
		if(nbt.contains("doesRotate"))
			doesRotate = nbt.getBoolean("doesRotate");
		if(nbt.contains("liveModel"))
			liveModel = nbt.getBoolean("liveModel");
		if(nbt.contains("isChild"))
			isChild = nbt.getBoolean("isChild");
		if(nbt.contains("isRemoteControlled"))
			isRemoteControlled = nbt.getBoolean("isRemoteControlled");
		if(nbt.contains("isActive"))
			isActive = nbt.getBoolean("isActive");
		properties.load(nbt);
		super.load(state, nbt);
	}
	
	public void setActive(boolean value) {
		isActive = value;
		sync();
	}
	
	public void setText(String text) {
		this.text = text;
		sync();
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
		return new ContainerLaserProjector(windowId, playerInv, this);
	}
	
	
	@Override
	public ITextComponent getDisplayName() {
		return new TranslationTextComponent("block.lasermod.laser_projector");
	}

public static enum PROJECTOR_MODES {
	TEXT, ITEM, PLAYER
//	, SHAPE
}

public static class ProjectorProperties {
	Map<PROJECTOR_PROPERTY, Float> properties;
	
	public ProjectorProperties() {
		properties = new HashMap<PROJECTOR_PROPERTY, Float>();
		resetAll();
	}
	
	public Set<PROJECTOR_PROPERTY> getProperties() {
		return properties.keySet();
	}
	
	public float getProperty(PROJECTOR_PROPERTY property) {
		if(hasProperty(property))
			return properties.get(property);
		return 0.0f;
	}
	
	public boolean hasProperty(PROJECTOR_PROPERTY property) {
		return properties.keySet().contains(property);
	}
	
	public void setProperty(PROJECTOR_PROPERTY property, float value) {
		properties.put(property, value);
	}
	
	public CompoundNBT save(CompoundNBT nbt) {
		CompoundNBT propNBT = new CompoundNBT();
		for (PROJECTOR_PROPERTY key : properties.keySet()) {
			propNBT.putFloat(key.name(), properties.get(key));
		}
		nbt.put("Properties", propNBT);
		return nbt;
	}
	
	public void load(CompoundNBT nbt) {
		if(nbt.contains("Properties")) {
			CompoundNBT propNBT = nbt.getCompound("Properties");
			for (String key : propNBT.getAllKeys()) {
				try {
				properties.put(PROJECTOR_PROPERTY.valueOf(key), propNBT.getFloat(key));
				}catch(Exception e) {}
			}
		}
	}
	
	public void resetAll() {
		for (PROJECTOR_PROPERTY property: PROJECTOR_PROPERTY.values()) {
			properties.put(property, property.getDefaultValue());
		}
	}
	
	public static enum PROJECTOR_PROPERTY {
		SCALE(0.1f, 2.0f), HEIGHT(0.11f), ROTATION(0.0f), SPEED(0.25f), SOLID(1.0f);
		
		float defaultValue;
		float min, max;
		private PROJECTOR_PROPERTY(float min, float max, float defaultValue) {
			this.min = min;
			this.max = max;
			this.defaultValue = defaultValue;
		}
		private PROJECTOR_PROPERTY(float min, float max) {
			this(min, max, 1);
		}
		private PROJECTOR_PROPERTY(float defaultValue) {
			this();
			this.defaultValue = defaultValue;
		}
		private PROJECTOR_PROPERTY() { this(0, 1); }
		public float getMin() { return min; } 
		public float getMax() { return max; }
		public float getDefaultValue() { return defaultValue; }
	}
}
}
