package KOWI2003.LaserMod.tileentities;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.mojang.authlib.GameProfile;

import KOWI2003.LaserMod.container.ContainerLaserProjector;
import KOWI2003.LaserMod.init.ModTileTypes;
import KOWI2003.LaserMod.tileentities.TileEntityLaserProjector.ProjectorProperties.PROJECTOR_PROPERTY;
import KOWI2003.LaserMod.utils.Utils;
import KOWI2003.LaserMod.utils.Utils.GenericConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityLaserProjector extends SyncableBlockEntity implements BlockEntityTicker<TileEntityLaserProjector>, MenuProvider {

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
	
	public boolean isProfileChecked = false;
	
	public Player playerToRender;
	public GenericConsumer<GameProfile> profile;
	
	public TileEntityLaserProjector(BlockPos pos, BlockState state) {
		super(ModTileTypes.LASER_PROJECTOR, pos, state);
		handler = new ItemStackHandler(3);
		properties = new ProjectorProperties();
		profile = new GenericConsumer<>();
	}
	
	
	@Override
	public void tick(Level level, BlockPos pos, BlockState state, TileEntityLaserProjector tile) {
		if(isActive) {
			if(doesRotate) {
				rotation += properties.getProperty(PROJECTOR_PROPERTY.SPEED)*2f;
				if(rotation >= 360)
					rotation = 0;
			}
			if(mode == PROJECTOR_MODES.PLAYER) {
				if(profile == null)
					profile = new GenericConsumer<>();
				if(profile.getStored() == null && !liveModel && !isProfileChecked) {
					profile = Utils.updateProfileConsumer(text, profile);
					isProfileChecked = true;
				}
//				if(playerToRender == null && liveModel)
//					playerToRender = Utils.getPlayer(level, text);
			}
			sync();
		}
		
		if(!isRemoteControlled) {
			if(isActive != Utils.isBlockPowered(getBlockPos(), getLevel())) {
				isActive = Utils.isBlockPowered(getBlockPos(), getLevel());
			}
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public AABB getRenderBoundingBox()
	{
		return INFINITE_EXTENT_AABB;
	}
	
	@Override
	protected void saveAdditional(CompoundTag nbt) {
		nbt.putString("Mode", mode.name());
		properties.save(nbt);
		nbt.putString("Text", text);
		nbt.putBoolean("doesRotate", doesRotate);
		nbt.putBoolean("liveModel", liveModel);
		nbt.putBoolean("isChild", isChild);
		nbt.put("inv", handler.serializeNBT());
		nbt.putBoolean("isRemoteControlled", isRemoteControlled);
		nbt.putBoolean("isActive", isActive);
		nbt.putFloat("Rotation", rotation);
		try {
			if(profile != null && profile.getStored() != null) {
				CompoundTag tag = new CompoundTag();
				NbtUtils.writeGameProfile(tag, profile.getStored());
				nbt.put("profile", tag);
			}
		}catch(Exception e) {}
		super.saveAdditional(nbt);
	}
	
	@Override
	public void load(CompoundTag nbt) {
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
		if(nbt.contains("Rotation"))
			rotation = nbt.getFloat("Rotation");
		try {
			if(nbt.contains("profile")) {
				CompoundTag tag = nbt.getCompound("profile");
				GameProfile pr = NbtUtils.readGameProfile(tag);
				if(profile == null)
					profile = new GenericConsumer<>();
				if(pr != null)
					profile.accept(pr);
				profile = Utils.updateProfileConsumer(text, profile);
			}
		}catch(Exception ex) {}
		properties.load(nbt);
		super.load(nbt);
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
	public AbstractContainerMenu createMenu(int windowId, Inventory playerInv, Player player) {
		return new ContainerLaserProjector(windowId, playerInv, this);
	}
	
	@Override
	public Component getDisplayName() {
		return new TranslatableComponent("block.lasermod.laser_projector");
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
	
	public CompoundTag save(CompoundTag nbt) {
		CompoundTag propNBT = new CompoundTag();
		for (PROJECTOR_PROPERTY key : properties.keySet()) {
			propNBT.putFloat(key.name(), properties.get(key));
		}
		nbt.put("Properties", propNBT);
		return nbt;
	}
	
	public void load(CompoundTag nbt) {
		if(nbt.contains("Properties")) {
			CompoundTag propNBT = nbt.getCompound("Properties");
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
