package KOWI2003.LaserMod.tileentities;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nullable;

import KOWI2003.LaserMod.init.ModBlocks;
import KOWI2003.LaserMod.init.ModTileTypes;
import KOWI2003.LaserMod.network.PacketComputerToMainThread;
import KOWI2003.LaserMod.network.PacketHandler;
import KOWI2003.LaserMod.tileentities.TileEntityLaser.MODE;
import KOWI2003.LaserMod.tileentities.TileEntityLaserProjector.PROJECTOR_MODES;
import KOWI2003.LaserMod.tileentities.TileEntityLaserProjector.ProjectorProperties.PROJECTOR_PROPERTY;
import KOWI2003.LaserMod.utils.TileEntityUtils;
import KOWI2003.LaserMod.utils.compat.cctweaked.LuaBlockPos;
import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.IDynamicLuaObject;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IDynamicPeripheral;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.shared.Capabilities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public class TileEntityDeviceHub extends TileEntity implements ITickableTileEntity {
	//TODO: add a physical clear list button, a display of the amount of devices connected
	//TODO: add gui with clear button and display a list of connected devices where you could remove any specific device at any moment (also a device count)
	
	List<Device> devices = new LinkedList<Device>();
	
	IPeripheral peripheral;
	private LazyOptional<IPeripheral> peripheralCap;
	
	public TileEntityDeviceHub() {
		super(ModTileTypes.DEVICE_HUB);
	}
	
	@Override
	public void tick() {
		validateDevices();
	}
	
	public void validateDevices() {
		List<Device> destructionQueue = new LinkedList<>();
		for (Device device : devices) {
			device.validateDevice(getLevel());
			if(device.queueDestroy)
				destructionQueue.add(device);
		}
		for(Device device : destructionQueue)
			devices.remove(device);
	}
	
	@Override
	public CompoundNBT save(CompoundNBT nbt) {
		List<Integer> coords = new LinkedList<>();
		for (Device d: devices) {
			coords.add(d.getPos().getX());
			coords.add(d.getPos().getY());
			coords.add(d.getPos().getZ());
		}
		nbt.putIntArray("Data", coords);
		return super.save(nbt);
	}
	
	@Override
	public void load(BlockState state, CompoundNBT nbt) {
		List<BlockPos> positions = new LinkedList<>();
		int[] coords = nbt.getIntArray("Data");
		for(int i = 0; i < coords.length; i += 3)
			positions.add(new BlockPos(coords[i], coords[i+1], coords[i+2]));
		devices.clear();
		for (BlockPos pos : positions)
			devices.add(new Device(pos, getLevel()));
		super.load(state, nbt);
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		sync();
		if(cap == Capabilities.CAPABILITY_PERIPHERAL) {
			if(peripheral == null) peripheral = new PeripheralInterface(this);
			if(peripheralCap == null) peripheralCap = LazyOptional.of(() -> peripheral);
			return peripheralCap.cast();
		}
		return super.getCapability(cap, side);
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
	
	public void link(BlockPos pos) {
		Device device = new Device(pos, getLevel());
		TileEntity tileentity = device.getTileEntity();
		if(tileentity instanceof TileEntityLaser) {
			((TileEntityLaser)tileentity).isRemoteControlled = true;
			((TileEntityLaser)tileentity).sync();
		}if(tileentity instanceof TileEntityLaserProjector) {
			((TileEntityLaserProjector)tileentity).isRemoteControlled = true;
			((TileEntityLaserProjector)tileentity).sync();
		}
		devices.add(device);
		sync();
	}
	
	public void callMethod(int methodIndex, Object[] args) {
		switch( methodIndex )
        {
			//connectDevice
			case 3:
			{
				if(args.length == 3)
					link(new BlockPos((int)(double)args[0], (int)(double)args[1], (int)(double)args[2]));
				break;
			}
			
			//disconnectDevice
			case 4:
			{
				if(args.length == 1) {
					int index = (int)(double)args[0];
					if(index < devices.size()) {
						devices.remove(index);
						sync();
					}
				}
				break;
			}
		
			//setActive
			case 5:
			{
				if(args.length == 1) {
					boolean value = (boolean)args[0];
					for (Device d : devices) {
						TileEntity te = d.getTileEntity();
						if(te instanceof TileEntityLaser)
							((TileEntityLaser) te).setActive(value);
						else if(te instanceof TileEntityLaserProjector)
							((TileEntityLaserProjector) te).setActive(value);
					}
				}else if(args.length == 2) {
					boolean value = (boolean)args[0];
					int index = (int)args[1];
					if(index >= devices.size())
						break;
					TileEntity te = devices.get(index).getTileEntity();
					if(te instanceof TileEntityLaser)
						((TileEntityLaser) te).setActive(value);
					else if(te instanceof TileEntityLaserProjector)
						((TileEntityLaserProjector) te).setActive(value);
				}
				break;
			}
			
			//setColor
			case 6:
			{
				if(args.length >= 3 && args.length <= 4) {
					float r = (float)((double)args[0]);
					r = r > 1.0f ? 1.0f : r < 0 ? 0 : r;
					float g = (float)((double)args[1]);
					g = g > 1.0f ? 1.0f : g < 0 ? 0 : g;
					float b = (float)((double)args[2]);
					b = b > 1.0f ? 1.0f : b < 0 ? 0 : b;
					
					if(args.length == 4) {
						int index = (int)args[1];
						if(index >= devices.size())
							break;
						TileEntity te = devices.get(index).getTileEntity();
						if(te instanceof TileEntityLaser)
							((TileEntityLaser) te).setColor(r, g, b);
					}else
						for(Device d : devices) {
							TileEntity te = d.getTileEntity();
							if(te instanceof TileEntityLaser)
								((TileEntityLaser) te).setColor(r, g, b);
						}
				}
				break;
			}
			
			//setMode
			case 7:
			{
				if(args.length >= 1 && args.length <= 2) {
					int mode = (int)(double)args[0];
					if(args.length == 2) {
						int index = (int)(double)args[1];
						Device d = devices.get(index);
						TileEntity te = d.getTileEntity();
						if(te instanceof TileEntityLaser) {
							if(mode >= MODE.values().length)
								break;
							MODE m = MODE.values()[mode];
							((TileEntityLaser) te).mode = m;
							((TileEntityLaser) te).sync();
						}else if(te instanceof TileEntityLaserProjector) {
							if(mode >= PROJECTOR_MODES.values().length)
								break;
							PROJECTOR_MODES m = PROJECTOR_MODES.values()[mode];
							((TileEntityLaserProjector) te).mode = m;
							((TileEntityLaserProjector) te).sync();
						}
					}
					else 
						for(Device d : devices) {
							TileEntity te = d.getTileEntity();
							if(te instanceof TileEntityLaser) {
								if(mode < MODE.values().length) {
									MODE m = MODE.values()[mode];
									((TileEntityLaser) te).mode = m;
									((TileEntityLaser) te).sync();
								}
							}else if(te instanceof TileEntityLaserProjector) {
								if(mode < PROJECTOR_MODES.values().length) {
									PROJECTOR_MODES m = PROJECTOR_MODES.values()[mode];
									((TileEntityLaserProjector) te).mode = m;
									((TileEntityLaserProjector) te).sync();
								}
							}
						}
				}
				break;
			}
			
			//setText
			case 9:
			{
				if(args.length >= 1 && args.length <= 2) {
					String text = (String)args[0];
					if(args.length == 2) {
						int index = (int)(double)args[1];
						Device d = devices.get(index);
						TileEntity te = d.getTileEntity();
						if(te instanceof TileEntityLaserProjector)
							((TileEntityLaserProjector) te).setText(text);
					}else 
						for(Device d : devices) {
							TileEntity te = d.getTileEntity();
							if(te instanceof TileEntityLaserProjector)
								((TileEntityLaserProjector) te).setText(text);
						}
				}
				break;
			}
			
			//setProperty
			case 11:
			{
				if(args.length >= 2 && args.length <= 3) {
					if(!(args[0] instanceof Number) || !(args[1] instanceof Number))
        				break;
					int propIndex = (int)((double)args[0]);
	        		float value = (float)((double)args[1]);
	        		if(args.length == 3) {
	        			if(!(args[2] instanceof Number))
	        				break;
	        			int index = (int)(double)args[2];
	        			Device d = devices.get(index);
	        			TileEntity te = d.getTileEntity();
	        			if(te instanceof TileEntityLaserProjector) {
	    					((TileEntityLaserProjector)te).properties.setProperty(PROJECTOR_PROPERTY.values()[propIndex], value);
	    					((TileEntityLaserProjector)te).sync();
	    				}
	        		}else
	        			for(Device d : devices) {
	        				TileEntity te = d.getTileEntity();
		        			if(te instanceof TileEntityLaserProjector) {
		    					((TileEntityLaserProjector)te).properties.setProperty(PROJECTOR_PROPERTY.values()[propIndex], value);
		    					((TileEntityLaserProjector)te).sync();
		    				}
	        			}
				}
				break;
			}
			
			// resetProperties
        	case 14:
        	{
        		if(args.length == 0) {
        			for(Device d : devices) {
        				TileEntity te = d.getTileEntity();
        				if(te instanceof TileEntityLaserProjector) {
        					for(PROJECTOR_PROPERTY prop : PROJECTOR_PROPERTY.values()) 
        					{ ((TileEntityLaserProjector)te).properties.setProperty(prop, prop.getDefaultValue()); }
        					((TileEntityLaserProjector)te).sync();
        				}
        			}
        		}else if(args.length == 1) {
        			int index = (int)(double)args[0];
        			Device d = devices.get(index);
        			TileEntity te = d.getTileEntity();
    				if(te instanceof TileEntityLaserProjector) {
    					for(PROJECTOR_PROPERTY prop : PROJECTOR_PROPERTY.values()) 
    					{ ((TileEntityLaserProjector)te).properties.setProperty(prop, prop.getDefaultValue()); }
    					((TileEntityLaserProjector)te).sync();
    				}
        		}
				break;
        	}
			
        	// resetProperty
        	case 15:
        	{
        		if(args.length == 1) {
        			int propIndex = (int)((double)args[0]);
        			for(Device d : devices) {
        				TileEntity te = d.getTileEntity();
        				if(te instanceof TileEntityLaserProjector) {
        					((TileEntityLaserProjector)te).properties.setProperty(PROJECTOR_PROPERTY.values()[propIndex], PROJECTOR_PROPERTY.values()[propIndex].getDefaultValue());
        					((TileEntityLaserProjector)te).sync();
        				}
        			}
        		}else if(args.length == 2) {
        			int propIndex = (int)((double)args[0]);
        			int index = (int)(double)args[0];
        			Device d = devices.get(index);
        			TileEntity te = d.getTileEntity();
    				if(te instanceof TileEntityLaserProjector) {
    					((TileEntityLaserProjector)te).properties.setProperty(PROJECTOR_PROPERTY.values()[propIndex], PROJECTOR_PROPERTY.values()[propIndex].getDefaultValue());
    					((TileEntityLaserProjector)te).sync();
    				}
        		}
				break;
        	}
        	
        	//setDoesRotate
        	case 16:
        	{
        		if(args.length >= 1 && args.length <= 2)
        		{
        			boolean value = (boolean)args[0];
        			if(args.length == 2) {
        				Device d = devices.get((int)(double)args[1]);
        				TileEntity tileentity = d.getTileEntity();
        				if(tileentity instanceof TileEntityLaserProjector) {
        					((TileEntityLaserProjector)tileentity).doesRotate = value;
        					((TileEntityLaserProjector)tileentity).sync();
        				}
        			}else
        				for(Device d : devices) {
            				TileEntity tileentity = d.getTileEntity();
            				if(tileentity instanceof TileEntityLaserProjector) {
            					((TileEntityLaserProjector)tileentity).doesRotate = value;
            					((TileEntityLaserProjector)tileentity).sync();
            				}
        				}
        		}
				break;
        	}
        	
        	//setRotation
        	case 17:
        	{
        		if(args.length >= 2 && args.length <= 3) {
	        		float x = (float)(double)args[0];
	        		float y = (float)(double)args[1];
	        		if(args.length == 3) {
	        			Device d = devices.get((int)(double)args[2]);
	        			TileEntity te = d.getTileEntity();
	        			if(te instanceof TileEntityAdvancedLaser)
	        				((TileEntityAdvancedLaser) te).setRotationEular(new Vector2f(x, y));
	        		}else
	        			for(Device d : devices) {
	        				TileEntity te = d.getTileEntity();
		        			if(te instanceof TileEntityAdvancedLaser)
		        				((TileEntityAdvancedLaser) te).setRotationEular(new Vector2f(x, y));
	        			}
        		}
				break;
        	}
        	
        	//setHeight
        	case 18:
        	{
        		if(args.length >= 1 && args.length <= 2) {
        			float h = (float)(double)args[0];
        			if(args.length == 2) {
        				Device d = devices.get((int)(double)args[1]);
	        			TileEntity te = d.getTileEntity();
	        			if(te instanceof TileEntityAdvancedLaser)
	        				((TileEntityAdvancedLaser) te).setHeight(h);
        			}else
        				for(Device d : devices) {
        					TileEntity te = d.getTileEntity();
    	        			if(te instanceof TileEntityAdvancedLaser)
    	        				((TileEntityAdvancedLaser) te).setHeight(h);
        				}
        		}
				break;
        	}
        	
        	//setDirection
        	case 19:
        	{
        		if(args.length >= 3 && args.length <= 4) {
	        		float x = (float)(double)args[0];
	        		float y = (float)(double)args[1];
	        		float z = (float)(double)args[2];
	        		Vector3f vec = new Vector3f(x, y, z);
	        		vec.normalize();
	        		
	        		if(args.length == 4) {
	        			Device d = devices.get((int)(double)args[3]);
	        			TileEntity te = d.getTileEntity();
	        			if(te instanceof TileEntityAdvancedLaser)
	        				((TileEntityAdvancedLaser) te).setDirectionDirect(vec);
	        		}else 
	        			for(Device d : devices)
	        			{
	        				TileEntity te = d.getTileEntity();
		        			if(te instanceof TileEntityAdvancedLaser)
		        				((TileEntityAdvancedLaser) te).setDirectionDirect(vec);
	        			}
        		}
				break;
        	}
        	
			default:
				break;
        }
	}
	
public class PeripheralInterface implements IDynamicPeripheral {

	TileEntityDeviceHub te;
	
	public PeripheralInterface(TileEntityDeviceHub te) {
		this.te = te;
	}

	@Override
	public String getType() {
		return "Device_Hub";
	}

	@Override
	public MethodResult callMethod(IComputerAccess computer, ILuaContext content, int methodIndex, IArguments args)
			throws LuaException {
		try {
		switch( methodIndex )
        {
			//getDevicesCount
			case 0:
			{
				if(args.getAll().length == 0) {
					return MethodResult.of(te.devices.size());
				}else
    				throw new LuaException("Expected 0 arguments");
			}
			
			//getMethodeUse
			case 1:
			{
				throw new LuaException("Not Yet Implemented!");
//				if(args.getAll().length == 0) {
//					return MethodResult.of(
//							"getAmount()", 
//							"getMethodUse() or getMethodUse( [methodName] )",
//							"getDevice( [deviceIndex] )",
//							"setActive( [boolean], {deviceIndex} )"
//							);
//				}else
//    				throw new LuaException("Expected 0 arguments");
			}
			
			//getDevice
			case 2:
			{
				if(args.getAll().length == 1) {
					if(!(args.getAll()[0] instanceof Number))
	    				throw new LuaException("Expected a number argument [argument 0]");
					if((int)(double)args.getAll()[0] >= te.devices.size())
						throw new LuaException("The index " + (int)(double)args.getAll()[0] + " is out of range for the devices! [argument 0]");
					return MethodResult.of(new DeviceObject(te, (int)(double)args.getAll()[0]));
				}else
    				throw new LuaException("Expected 1 number arguments");
			}
			
			//connectDevice
			case 3:
			{
				if(args.getAll().length == 3) {
					for(int i = 0; i < 3; i++) {
						if(!(args.get(i) instanceof Number))
							throw new LuaException("Expected a number! [argument " + i +"]");
					}
					BlockPos pos = new BlockPos(args.getInt(0), args.getInt(1), args.getInt(2));
					Device d = new Device(pos, te.level);
					if(d.getType() == DeviceType.NONE)
						throw new LuaException("Block at (" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + ") is not a valid Device!");
    				PacketHandler.sendToServer(new PacketComputerToMainThread(getBlockPos(), methodIndex, args.getAll()));
				}else
					throw new LuaException("Expected 3 arguments!");
				return MethodResult.of();
			}
			
			//disconnectDevice
			case 4:
			{
				if(args.getAll().length == 1) {
					if(!(args.get(0) instanceof Number))
						throw new LuaException("Expected a number! [argument 0]");
					if((int)(double)args.getAll()[0] >= te.devices.size())
						throw new LuaException("The index " + (int)(double)args.getAll()[0] + " is out of range for the devices! [argument 0]");
    				PacketHandler.sendToServer(new PacketComputerToMainThread(getBlockPos(), methodIndex, args.getAll()));
				}else
					throw new LuaException("Expected 1 arguments!");
				return MethodResult.of();
			}
			
			//setActive
			case 5:
			{
				if(args.getAll().length == 1) {
					if(!(args.getAll()[0] instanceof Boolean))
						throw new LuaException("Expected a boolean [argument 0]");
    				PacketHandler.sendToServer(new PacketComputerToMainThread(getBlockPos(), methodIndex, args.getAll()));
				}else if(args.getAll().length == 2) {
					if(!(args.getAll()[0] instanceof Boolean))
						throw new LuaException("Expected a boolean [argument 0]");
					if(!(args.getAll()[1] instanceof Number))
						throw new LuaException("Expected a number [argument 1]");
					if((int)(double)args.getAll()[1] >= te.devices.size())
						throw new LuaException("The index " + (int)(double)args.getAll()[1] + " is out of range for the devices! [argument 1]");
    				PacketHandler.sendToServer(new PacketComputerToMainThread(getBlockPos(), methodIndex, args.getAll()));
				}else
					throw new LuaException("Expected 1 or 2 arguments!");
				return MethodResult.of();
			}
			
			//setColor
			case 6:
			{
				if(args.getAll().length >= 3 && args.getAll().length <= 4) {
					for(int i = 0; i < 3; i++) {
						if(!(args.get(i) instanceof Number))
							throw new LuaException("Expected a number! [argument " + i +"]");
						if(!(args.getDouble(i) >= 0 && args.getDouble(i) <= 1))
							throw new LuaException("Number must be in range (0.0 -> 1.0)! [argument " + i +"]");
					}
					if(args.getAll().length == 4) {
						if(!(args.get(3) instanceof Number))
							throw new LuaException("Expected a number! [argument 3]");
						if((int)(double)args.getAll()[3] >= te.devices.size())
							throw new LuaException("The index " + (int)(double)args.getAll()[3] + " is out of range for the devices! [argument 3]");
					}
    				PacketHandler.sendToServer(new PacketComputerToMainThread(getBlockPos(), methodIndex, args.getAll()));
				}else
					throw new LuaException("Expected 3 or 4 arguments!");
				return MethodResult.of();
			}
			
			//setMode
			case 7:
			{
				if(args.getAll().length >= 1 && args.getAll().length <= 2) {
					if(!(args.getAll()[0] instanceof Number))
						throw new LuaException("Expected a number! [argument 0]");
					int modeIndex = (int)args.getAll()[0];
					int max = Math.max(MODE.values().length, PROJECTOR_MODES.values().length);
					if(args.getAll().length == 2) {
						if(!(args.get(1) instanceof Number))
							throw new LuaException("Expected a number! [argument 1]");
						if((int)(double)args.getAll()[1] >= te.devices.size())
							throw new LuaException("The index " + (int)(double)args.getAll()[1] + " is out of range for the devices! [argument 1]");
						
						Device d = te.devices.get((int)(double)args.getAll()[1]);
						if(d.getType() == DeviceType.LASER || d.getType() == DeviceType.ADVANCED_LASER)
							max = MODE.values().length;
						else if(d.getType() == DeviceType.LASER_PROJECTOR)
							max = PROJECTOR_MODES.values().length;
					}
					if(modeIndex >= max)
						throw new LuaException("Mode index is out of range (0 -> " + (max - 1) + ")! [argument 0]");
					PacketHandler.sendToServer(new PacketComputerToMainThread(getBlockPos(), methodIndex, args.getAll()));
    				return MethodResult.of();
				}else
					throw new LuaException("Expected 1 or 2 arguments!");
			}
			
			//canBeColored
			case 8:
			{
				if(args.getAll().length == 1) {
					if(!(args.get(0) instanceof Number))
						throw new LuaException("Expected a number! [argument 0]");
					if((int)(double)args.getAll()[0] >= te.devices.size())
						throw new LuaException("The index " + (int)(double)args.getAll()[0] + " is out of range for the devices! [argument 0]");
					TileEntity tileentity = te.devices.get((int)(double)args.getAll()[0]).getTileEntity();
					return MethodResult.of(tileentity instanceof TileEntityLaser && ((TileEntityLaser) tileentity).getProperties().hasUpgarde("color"));
				}else
    				throw new LuaException("Expected 1 argument!");
			}
			
			//setText
			case 9:
			{
				if(args.getAll().length >= 1 && args.getAll().length <= 2) {
					if(!(args.getAll()[0] instanceof String))
        				throw new LuaException("Argument is not a string!");
					if(args.getAll().length == 2) {
						Device d = getDevice(args, 1);
						if(d != null)
							if(d.getType() != DeviceType.LASER_PROJECTOR) 
								throw new LuaException("Cannot set the text for this device! (device incapatible)");
					}
					PacketHandler.sendToServer(new PacketComputerToMainThread(getBlockPos(), methodIndex, args.getAll()));
				}else
    				throw new LuaException("Expected 1 or 2 arguments!");
    			return MethodResult.of();
			}
			
			//hasText
			case 10:
			{
				if(args.getAll().length == 1) {
					Device d = getDevice(args, 0);
					if(d != null) {
						return MethodResult.of(d.getTileEntity() instanceof TileEntityLaserProjector);
					}
				}else
    				throw new LuaException("Expected 1 argument!");
				throw new LuaException("An unknown error occured!");
			}
			
			//setProperty
			case 11:
			{
				if(args.getAll().length >= 2 && args.getAll().length <= 3)
				{
					if(!(args.getAll()[0] instanceof Number))
        				throw new LuaException("Argument 0 is not a number!");
        			if(!(args.getAll()[1] instanceof Number))
        				throw new LuaException("Argument 1 is not a number!");
        			if(args.getAll().length == 3) {
        				Device d = getDevice(args, 2);
        				if(d != null) {
        					if(d.getType() != DeviceType.LASER_PROJECTOR)
        						throw new LuaException("Cannot set the property for this device! (device incapatible)");
        				}
        			}
        			int property = (int)((double)args.getAll()[0]);
        			if(property >= PROJECTOR_PROPERTY.values().length)
        				throw new LuaException("Property index should be between: 0 and " + (PROJECTOR_PROPERTY.values().length - 1));
        			PROJECTOR_PROPERTY prop = PROJECTOR_PROPERTY.values()[property];
        			float value = (float)((double)args.getAll()[1]); 
        			if(!(value >= prop.getMin() && value <= prop.getMax()))
        				throw new LuaException("Property index should be between: " + prop.getMin() + " and " + prop.getMax());
    				PacketHandler.sendToServer(new PacketComputerToMainThread(getBlockPos(), methodIndex, args.getAll()));
        			return MethodResult.of();
				}else
    				throw new LuaException("Expected 2 or 3 argument!");
			}
			
			// getPropertyMinMax
        	case 12:
        	{
        		if(args.getAll().length >= 1 && args.getAll().length <= 2) {
        			if(args.getAll().length == 2) {
	        			Device d = getDevice(args, 1);
	        			if(d.getType() != DeviceType.LASER_PROJECTOR)
	        				throw new LuaException("Cannot get the property data for this device! (device incapatible)");
        			}
        			if(!(args.getAll()[0] instanceof Number))
        				throw new LuaException("Argument 0 is not a number!");
        			int property = (int)((double)args.getAll()[0]);
        			if(property >= PROJECTOR_PROPERTY.values().length)
        				throw new LuaException("Property index should be between: 0 and " + (PROJECTOR_PROPERTY.values().length - 1) + "[argument 0]");
        			PROJECTOR_PROPERTY prop = PROJECTOR_PROPERTY.values()[property];
        			return MethodResult.of(new float[] {prop.getMin(), prop.getMax()});
        		}else
    				throw new LuaException("Expected 2 number arguments!");
        	}
        	
        	// getPropertyIndex
        	case 13:
        	{
        		if(args.getAll().length >= 1 && args.getAll().length <= 2) {
        			if(args.getAll().length == 2) {
	        			Device d = getDevice(args, 1);
	        			if(d.getType() != DeviceType.LASER_PROJECTOR)
	        				throw new LuaException("Cannot get the property data for this device! (device incapatible)");
        			}
        			if(!(args.getAll()[0] instanceof String))
        				throw new LuaException("Argument 0 is not a string!");
        			String name = ((String)args.getAll()[0]).toUpperCase();
        			PROJECTOR_PROPERTY[] props = PROJECTOR_PROPERTY.values();
        			PROJECTOR_PROPERTY prop = null;
        			String s = "[";
        			for (PROJECTOR_PROPERTY pp : props) {
        				if(prop == null)
							if(pp.name().equals(name)) {
								prop = pp;
							}
        				s += pp.name().toLowerCase() + ", ";
					}
        			if(s.length() > 2) {
        				s = s.substring(0, s.length() - 2);
        			}
        			s += "]";
        			if(prop == null)
        				throw new LuaException("Expected one of: " + s + "; got: " + name);
        			return MethodResult.of(prop.ordinal());
        		}else
    				throw new LuaException("Expected 2 arguments!");
        	}
			
        	// resetProperties
        	case 14:
        	{
        		if(args.getAll().length >= 0 || args.getAll().length <= 1) {
        			if(args.getAll().length == 1) {
        				Device d = getDevice(args, 0);
        				if(d.getType() != DeviceType.LASER_PROJECTOR)
            				throw new LuaException("Cannot reset the property for this device! (device incapatible)");
        			}
    				PacketHandler.sendToServer(new PacketComputerToMainThread(getBlockPos(), methodIndex, args.getAll()));
        			return MethodResult.of();
        		}else
    				throw new LuaException("Expected 0 or 1 argument!");
        	}
        	
        	// resetProperty
        	case 15:
        	{
        		if(args.getAll().length >= 1 || args.getAll().length <= 2) {
        			if(!(args.getAll()[0] instanceof Number)) 
        				throw new LuaException("Expected a number! [argument 0]");
        			int property = (int)((double)args.getAll()[0]);
        			if(property >= PROJECTOR_PROPERTY.values().length)
        				throw new LuaException("Property index should be between: 0 and " + (PROJECTOR_PROPERTY.values().length - 1));
        			if(args.getAll().length == 2) {
        				Device d = getDevice(args, 1);
        				if(d.getType() != DeviceType.LASER_PROJECTOR)
            				throw new LuaException("Cannot reset the property for this device! (device incapatible)");
        			}
    				PacketHandler.sendToServer(new PacketComputerToMainThread(getBlockPos(), methodIndex, args.getAll()));
        			return MethodResult.of();
        		}else
    				throw new LuaException("Expected 1 or 2 arguments!");
        	}
        	
        	//setDoesRotate
        	case 16:
        	{
        		if(args.getAll().length >= 1 || args.getAll().length <= 2) {
        			if(!(args.getAll()[0] instanceof Boolean)) 
        				throw new LuaException("Expected a boolean! [argument 0]");
        			if(args.getAll().length == 2) {
        				Device d = getDevice(args, 1);
        				if(d.getType() != DeviceType.LASER_PROJECTOR)
            				throw new LuaException("Cannot reset the property for this device! (device incapatible)");
        			}
        			PacketHandler.sendToServer(new PacketComputerToMainThread(getBlockPos(), methodIndex, args.getAll()));
        			return MethodResult.of();
        		}else
    				throw new LuaException("Expected 1 or 2 arguments!");
        	}
        	
        	//setRotation
        	case 17:
        	{
        		if(args.getAll().length >= 2 && args.getAll().length <= 3) {
        			if(args.getAll().length == 3) {
        				Device d = getDevice(args, 2);
        				if(d != null) {
        					if(d.getType() != DeviceType.ADVANCED_LASER)
        						throw new LuaException("Cannot set the rotation of this device (device incapatible, should be a advanced laser)!");
        				}
        			}
        			if(!(args.getAll()[0] instanceof Number))
        				throw new LuaException("Expected a number argument! [argument 0]");
        			if(!(args.getAll()[1] instanceof Number))
        				throw new LuaException("Expected a number argument! [argument 1]");
        			
            		if(Math.abs((float)(double)args.getAll()[0]) > Math.abs(TileEntityAdvancedLaser.minAngle))
        				throw new LuaException("Argument 0 is out of range [range: " + TileEntityAdvancedLaser.minAngle + " -> " + Math.abs(TileEntityAdvancedLaser.minAngle) + "]");
            		if(Math.abs((float)(double)args.getAll()[1]) > Math.abs(TileEntityAdvancedLaser.minAngle))
        				throw new LuaException("Argument 1 is out of range [range: " + TileEntityAdvancedLaser.minAngle + " -> " + Math.abs(TileEntityAdvancedLaser.minAngle) + "]");
            		
    				PacketHandler.sendToServer(new PacketComputerToMainThread(getBlockPos(), methodIndex, args.getAll()));
        		}else
        			throw new LuaException("Expected 2 number arguments!");
        		return MethodResult.of();
        	}
        	
        	//setHeight
        	case 18:
        	{
        		if(args.getAll().length >= 1 && args.getAll().length <= 2) {
        			if(args.getAll().length == 2) {
        				Device d = getDevice(args, 1);
        				if(d != null)
        					if(d.getType() != DeviceType.ADVANCED_LASER)
        						throw new LuaException("Cannot set the height offset of this device (device incapatible, should be a advanced laser)!");
        			}
        			if(!(args.getAll()[0] instanceof Number))
        				throw new LuaException("Expected a number argument! [argument 0]");
        			
        			double val = (double)args.getAll()[0];
        			double val2 = Math.min(TileEntityAdvancedLaser.maxHeight, Math.max(TileEntityAdvancedLaser.minHeight, val));
        			if(val != val2)
        				throw new LuaException("Argument 0 is out of range [range: " + TileEntityAdvancedLaser.minHeight +" -> " + TileEntityAdvancedLaser.maxHeight + "]");
        			
    				PacketHandler.sendToServer(new PacketComputerToMainThread(getBlockPos(), methodIndex, args.getAll()));
        		}else
        			throw new LuaException("Expected 1 or 2 arguments!");
    			return MethodResult.of();
        	}
        	
        	//setDirection
        	case 19:
        	{
        		if(args.getAll().length >= 3 && args.getAll().length <= 4) {
        			if(args.getAll().length == 4) {
        				Device d = getDevice(args, 1);
        				if(d != null)
        					if(d.getType() != DeviceType.ADVANCED_LASER)
        						throw new LuaException("Cannot set the direction of this device (device incapatible, should be a advanced laser)!");
	        		}
        			
        			if(!(args.getAll()[0] instanceof Number))
        				throw new LuaException("Expected a number argument! [argument 0]");
        			if(!(args.getAll()[1] instanceof Number))
        				throw new LuaException("Expected a number argument! [argument 1]");
        			if(!(args.getAll()[2] instanceof Number))
        				throw new LuaException("Expected a number argument! [argument 2]");
    				PacketHandler.sendToServer(new PacketComputerToMainThread(getBlockPos(), methodIndex, args.getAll()));
        		}else
        			throw new LuaException("Expected 3 or 4 arguments!");
    			return MethodResult.of();
        	}
        	
        	default:
        	{
        		throw new LuaException("Method index out of range!");
        	}
        }
		}catch(NullPointerException ex) {
			return MethodResult.of();
		}
	}

	@Override
	public String[] getMethodNames() {
		return new String[] {
				"getDevicesCount",
				"getMethodUse",	//getMethodeUse() or overload: getMethodeUse([Methode Name])
				"getDevice",
				"connectDevice",	//connectDevice(x, y, z)
				"disconnectDevice",	//disconnectDevice([index]) or disconnectDevice(x, y, z)
				"setActive",	//setActive([true/false]) or overload: setActive([true/false], [device_index])
				"setColor",
				"setMode",
				"canBeColored",
				"setText",
				"hasText",
				"setProperty",
				"getPropertyMinMax",
				"getPropertyIndex",
				"resetProperties",
				"resetProperty",
				"setDoesRotate",
				"setRotation",
				"setHeight",
				"setDirection"
		};
	}
	
	public int getIndex(IArguments args, int argumentIndex) throws LuaException {
		if(args.getAll().length == argumentIndex+1) {
			if(!(args.get(argumentIndex) instanceof Number))
				throw new LuaException("Expected a number! [argument " + argumentIndex + "]");
			if((int)(double)args.getAll()[argumentIndex] >= te.devices.size())
				throw new LuaException("The index " + (int)(double)args.getAll()[argumentIndex] + " is out of range for the devices! [argument " + argumentIndex + "]");
			return (int)(double)args.getAll()[argumentIndex];
		}
		return -1;
	}
	
	@Nullable
	public Device getDevice(IArguments args, int argumentIndex) throws LuaException {
		int index = getIndex(args, argumentIndex);
		if(index < te.devices.size() && index >= 0) {
			return te.devices.get(index);
		}
		return null;
	}
	
	@Override
	public boolean equals(IPeripheral arg0) {
		return false;
	}
}

public class DeviceObject implements IDynamicLuaObject {

	TileEntityDeviceHub te;
	int index;
	
	public DeviceObject(TileEntityDeviceHub te, int index) {
		this.te = te;
		this.index = index;
	}
	
	@Override
	public MethodResult callMethod(ILuaContext context, int methodIndex, IArguments args) throws LuaException {
		try {
			switch( methodIndex )
	        {
				case 0:
				{
					if(args.getAll().length == 0) {
						return MethodResult.of(te.devices.get(index).getType().name().toLowerCase());
					}else
	    				throw new LuaException("Expected 0 arguments");
				}
				case 1:
				{
					if(args.getAll().length == 0) {
						return MethodResult.of(te.devices.get(index).getTypeID());
					}else
	    				throw new LuaException("Expected 0 arguments");
				}
				case 2:
				{
					if(args.getAll().length == 0) {
						return MethodResult.of(new LuaBlockPos(te.devices.get(index).getPos()));
					}else
	    				throw new LuaException("Expected 0 arguments");
				}
				
	        	default:
	        	{
	        		throw new LuaException("Method index out of range!");
	        	}
	        }
			}catch(NullPointerException ex) {
				return MethodResult.of();
			}
	}

	@Override
	public String[] getMethodNames() {
		return new String[] {
				"getType",
				"getTypeID",
				"getPosition"
		};
	}
}

public static class Device {
	
	BlockPos pos;
	World level;
	
	boolean queueDestroy = false;
	
	public Device(BlockPos pos, World level) {
		this.pos = pos;
		this.level = level;
	}
	
	public TileEntity getTileEntity() {
		return level.getBlockEntity(pos);
	}
	
	public BlockState getState() {
		return level != null ? level.getBlockState(pos) : Blocks.AIR.defaultBlockState();
	}
	
	public Block getBlock() {
		return getState().getBlock();
	}
	
	public BlockPos getPos() {
		return pos;
	}
	
	public DeviceType getType() {
		Block block = getBlock();
		for (DeviceType type: DeviceType.values()) {
			if(type.getBlock() == block)
				return type;
		}
		return DeviceType.NONE;
	}
	
	public int getTypeID() {
		return getType().ordinal()-1;
	}
	
	public void validateDevice(World level) {
		if(this.level == null)
			this.level = level;
		if(getTypeID() == -1 && this.level != null)
			queueDestroy = true;
	}
}

public enum DeviceType
{
	NONE(Blocks.AIR), LASER(ModBlocks.Laser), LASER_PROJECTOR(ModBlocks.LaserProjector), ADVANCED_LASER(ModBlocks.AdvancedLaser);
	
	Block marker;
	
	private DeviceType(Block marker) {
		this.marker = marker;
	}
	
	public Block getBlock() {
		return marker;
	}
}

}
