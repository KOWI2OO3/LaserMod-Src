package KOWI2003.LaserMod.utils;

import java.awt.Color;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;

import KOWI2003.LaserMod.Reference;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.SkullTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;

public class Utils {
	
	private static Logger logger;
	private static Lang lang;
	
	public static Logger getLogger() {
		if(logger == null) {
			logger = LogManager.getFormatterLogger(Reference.MODID);
			
		}
		return logger;
	}
	
	public static Lang getlang() {
		if(lang == null) {
			lang = new Lang(Reference.MODID);
		}
		
		return lang;
	}
	
	public static int calculateRedstone(IItemHandler handler) {
		int i = 0;
		float f = 0.0F;
		for (int j = 0; j < handler.getSlots(); j++) {
			ItemStack stack = handler.getStackInSlot(j);
			if (!stack.isEmpty()) {
				f += (float) stack.getCount() / (float) Math.min(handler.getSlotLimit(j), stack.getMaxStackSize());
				i++;
			}
		}
		f = f / (float) handler.getSlots();
		return MathHelper.floor(f * 14.0F) + (i > 0 ? 1 : 0);
	}
	
	public static ItemStack addStackToInventory(IItemHandler handler, ItemStack stack, boolean simulate) {
		ItemStack remainder = stack;
		for (int slot = 0; slot < handler.getSlots(); slot++) {
			remainder = handler.insertItem(slot, stack, simulate);
			if (remainder == ItemStack.EMPTY)
				break;
		}
		return remainder;
	}
	
	public static ItemStack addStackToInventory(IItemHandler handler, int maxSlot, ItemStack stack, boolean simulate) {
		ItemStack remainder = stack;
		for (int slot = 0; slot < maxSlot; slot++) {
			remainder = handler.insertItem(slot, stack, simulate);
			if (remainder == ItemStack.EMPTY)
				break;
		}
		return remainder;
	}
	
	public static boolean isInventoryFull(IItemHandler handler) {
		int filledSlots = 0;
		for (int slot = 0; slot < handler.getSlots(); slot++) {
			if (handler.getStackInSlot(slot).getCount() == handler.getSlotLimit(slot))
				filledSlots++;
		}
		return filledSlots == handler.getSlots();
	}
	
	public static boolean isInventoryFull(IItemHandler handler, int maxSlot) {
		int filledSlots = 0;
		for (int slot = 0; slot < maxSlot; slot++) {
			if (handler.getStackInSlot(slot).getCount() == handler.getSlotLimit(slot))
				filledSlots++;
		}
		return filledSlots == maxSlot;
	}
	
	public static int getValueOf(boolean bool) {
		if(bool) 
			return 1;
		else if(!bool)
			return 0;
		else
			return 0;
	}
	
	public static boolean getBooleanOf(int value) {
		if(value == 1) 
			return true;
		else if(value == 0)
			return false;
		else
			return false;
	}
	
	public static String getHexStringFromRGB(float Red, float Green, float Blue) {
		try {
			Color hC = new Color(Red, Green, Blue);
		    String hex = Integer.toHexString(hC.getRGB() & 0xffffff);
		    return hex;
		}catch (Exception e) {
			return "FFFFFF";
		}
	}
	
	public static String getHexStringFromRGBA(float Red, float Green, float Blue, float Alpha) {
		Color hC = new Color(Red, Green, Blue);
		String alphaHex = Integer.toHexString(Math.round(Alpha*255) & 0xff);
	    String hex = Integer.toHexString(hC.getRGB() & 0xffffff);
	    return alphaHex + hex;
	}
	
	public static String getHexStringFromRGB(int[] RGB) {
		Color hC = new Color(RGB[0], RGB[1], RGB[2]);
	    String hex = Integer.toHexString(hC.getRGB() & 0xffffff);
	    return hex;
	}
	
	public static String getHexStringFromRGBA(int[] RGBA) {
		Color hC = new Color(RGBA[0], RGBA[1], RGBA[2]);
		String alphaHex = Integer.toHexString(Math.round(RGBA[3]*255) & 0xff);
	    String hex = Integer.toHexString(hC.getRGB() & 0xffffff);
	    return alphaHex + hex;
	}
	
	public static int getHexIntFromRGB(float Red, float Green, float Blue) {
		String hex = getHexStringFromRGB(Red, Green, Blue);
		return Integer.parseInt(hex, 16);
	}
	
	public static int getHexIntFromRGBA(float Red, float Green, float Blue, float Alpha) {
		return Math.round(Red*255f) << 16 | Math.round(Green*255f) << 8 | Math.round(Blue*255f) | Math.round(Alpha*255f) << 24;
	}
	
	public static int getHexIntFromRGBA(float[] RGBA) {
		return getHexIntFromRGBA(
				RGBA.length > 0 ? RGBA[0] : 1.0f, 
				RGBA.length > 1 ? RGBA[1] : 1.0f, 
				RGBA.length > 2 ? RGBA[2] : 1.0f, 
				RGBA.length > 3 ? RGBA[3] : 1.0f);
	}
	
	public static float[] getFloatRGBAFromHexInt(int hex) {
		return new float[] {
				(float)(hex >> 16 & 255) / 255.0F,
				(float)(hex >> 8 & 255) / 255.0F,
				(float)(hex & 255) / 255.0F,
				(float)(hex >> 24 & 255) / 255.0F};
	}
	
	public static int[] getIntRGBAFromHexInt(int hex) {
		return new int[] {
				(hex >> 16 & 255),
				(hex >> 8 & 255),
				(hex & 255),
				(hex >> 24 & 255)};
	}
	
	public static int getHexFromInt(int value) {
		return Integer.parseInt(Integer.toHexString(Math.round(value*255) & 0xff), 16);
	}
//	public static EnumDyeColor getColourFromDye(ItemStack stack) {
//		for(int id : OreDictionary.getOreIDs(stack)) {
//			if(id == OreDictionary.getOreID("dyeBlack")) return EnumDyeColor.BLACK;
//			if(id == OreDictionary.getOreID("dyeRed")) return EnumDyeColor.RED;
//			if(id == OreDictionary.getOreID("dyeGreen")) return EnumDyeColor.GREEN;
//			if(id == OreDictionary.getOreID("dyeBrown")) return EnumDyeColor.BROWN;
//			if(id == OreDictionary.getOreID("dyeBlue")) return EnumDyeColor.BLUE;
//			if(id == OreDictionary.getOreID("dyePurple")) return EnumDyeColor.PURPLE;
//			if(id == OreDictionary.getOreID("dyeCyan")) return EnumDyeColor.CYAN;
//			if(id == OreDictionary.getOreID("dyeLightGray")) return EnumDyeColor.SILVER;
//			if(id == OreDictionary.getOreID("dyeGray")) return EnumDyeColor.GRAY;
//			if(id == OreDictionary.getOreID("dyePink")) return EnumDyeColor.PINK;
//			if(id == OreDictionary.getOreID("dyeLime")) return EnumDyeColor.LIME;
//			if(id == OreDictionary.getOreID("dyeYellow")) return EnumDyeColor.YELLOW;
//			if(id == OreDictionary.getOreID("dyeLightBlue")) return EnumDyeColor.LIGHT_BLUE;
//			if(id == OreDictionary.getOreID("dyeMagenta")) return EnumDyeColor.MAGENTA;
//			if(id == OreDictionary.getOreID("dyeOrange")) return EnumDyeColor.ORANGE;
//			if(id == OreDictionary.getOreID("dyeWhite")) return EnumDyeColor.WHITE;
//		}
//		return EnumDyeColor.WHITE;
//	}
	
	/**
	 * gets the latest Version of the mod from the github update json
	 * 
	 * @return String
	 */
	public static String getLatestVersion() {
		try {
			JsonObject jObj = WebUtils.getJsonObj(Reference.UPDATE_URL);
			JsonObject versions = JsonUtils.getJsonObject(jObj, "version");
			String s = JsonUtils.getValue(versions, Reference.MCVERSION + "-latest");
			return s;
		}catch (NullPointerException e) {
			return Reference.VESRION;
		}
	}
	
	/**
	 * gets the recommended Version of the mod from the github update json
	 * 
	 * @return String
	 */

	public static String getRecommendedVersion() {
		try {
			JsonObject jObj = WebUtils.getJsonObj(Reference.UPDATE_URL);
			JsonObject versions = JsonUtils.getJsonObject(jObj, "version");
			String s = JsonUtils.getValue(versions, Reference.MCVERSION + "-recommended");
			return s;
		}catch (Exception e) {
			return Reference.VESRION;
		}
	}
	
	/**
	 * tells you wether or not the mod is up to date
	 * @return boolean
	 */
	public static boolean isNewestVersion() {
		String s = getLatestVersion();
		return s.equals(Reference.VESRION);
	}
	
	/**
	 * tells you wether or not the mod is up to date To the recommended version
	 * @return boolean
	 */
	public static boolean isRecommendedVersion(boolean checkRecommended) {
		String s = getLatestVersion();
		if(checkRecommended) {
			String sr = getRecommendedVersion();
			if(s.equals(Reference.VESRION)) {
				return true;
			}else
				return sr.equals(Reference.VESRION);
		}else
			return s.equals(Reference.VESRION);
	}
	
//	/**
//	 * this methode is meant to get a message from the update file but it is unstable and out of use for now
//	 * for a example of how this should work look at the update json on the desktop
//	 * 
//	 * @param jObj
//	 * @return TextComponentString[]
//	 */
//	@Deprecated
//	public static TextComponentString[] getTextComponent(@Nonnull JsonObject jObj) {
//		JsonObject jObj2 = JsonUtils.getJsonObject(jObj, "message");
//		JsonArray jsonArray = JsonUtils.getJsonElement(jObj2, Reference.MCVERSION).getAsJsonArray();
//		if(jsonArray.size() == 0)
//			return new TextComponentString[] {};
//		TextComponentString[] strings = new TextComponentString[jsonArray.size()];
//		for(int i = 0; i < jsonArray.size(); i++) {
//			JsonObject JObject = jsonArray.get(i).getAsJsonObject();
//			if(JObject.has("text")) {
//				String text = JsonUtils.getValue(JObject, "text");
//				text = text.replaceAll("ยง", "ง");
//				strings[i] = new TextComponentString(text);
//			}else
//				strings[i] = new TextComponentString("");
//			if(JObject.has("url")) {
//				String url = JsonUtils.getValue(JObject, "url");
//				strings[i].getStyle().setClickEvent(new ClickEvent(Action.OPEN_URL, url));
//			}
//			if(JObject.has("hover")) {
//				String hover = JsonUtils.getValue(JObject, "hover");
//				strings[i].getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString(hover)));
//			}
//		}
//		return strings;
//	}
	
	/**
	 * this methode rotates the aabb 180 degree Horizontaly
	 * 
	 * @param AABB
	 * @return
	 */
	public static AxisAlignedBB oppositeAABB(AxisAlignedBB AABB) {
		double x1,x2,y1,y2,z1,z2;
		x1 = AABB.minX * -1d;
		x2 = AABB.maxX * -1d;
		z1 = AABB.minZ * -1d;
		z2 = AABB.maxZ * -1d;
		y1 = AABB.minY;
		y2 = AABB.maxY;
		return new AxisAlignedBB(x2, y1, z2, x1, y2, z1).move(1, 0, 1);
	}
	
	public static AxisAlignedBB rotateAABB(AxisAlignedBB AABB, Direction facing) {
		double  x1,x2,z1,z2;
		x1 = AABB.minX;
		x2 = AABB.maxX;
		z1 = AABB.minZ;
		z2 = AABB.maxZ;
		double[] bounds = fixRotation(facing, x1, z1, x2, z2);
        return new AxisAlignedBB(bounds[0], AABB.minY, bounds[1], bounds[2], AABB.maxY, bounds[3]);
	}
	
	public static List<AxisAlignedBB> rotateAABB(List<AxisAlignedBB> AABB, Direction facing) {
		List<AxisAlignedBB> boxes = new ArrayList<AxisAlignedBB>();
		for(AxisAlignedBB box: AABB) {
			boxes.add(rotateAABB(box, facing));
		}
		return boxes;
	}
	
	/**
	 * this methode rotates All of the AABBs in a List 180 degree Horizontaly
	 * 
	 * @param AABB
	 * @return
	 */
	public static List<AxisAlignedBB> oppositeAABB(List<AxisAlignedBB> AABB) {
		List<AxisAlignedBB> boxes = new ArrayList<AxisAlignedBB>();
		for(AxisAlignedBB box: AABB) {
			boxes.add(oppositeAABB(box));
		}
		return boxes;
	}
	
	private static double[] fixRotation(Direction facing, double var1, double var2, double var3, double var4)
    {
        switch(facing)
        {
            case WEST:
                double var_temp_1 = var1;
                var1 = 1.0F - var3;
                double var_temp_2 = var2;
                var2 = 1.0F - var4;
                var3 = 1.0F - var_temp_1;
                var4 = 1.0F - var_temp_2;
                break;
            case NORTH:
                double var_temp_3 = var1;
                var1 = var2;
                var2 = 1.0F - var3;
                var3 = var4;
                var4 = 1.0F - var_temp_3;
                break;
            case SOUTH:
                double var_temp_4 = var1;
                var1 = 1.0F - var4;
                double var_temp_5 = var2;
                var2 = var_temp_4;
                double var_temp_6 = var3;
                var3 = 1.0F - var_temp_5;
                var4 = var_temp_6;
                break;
            default:
                break;
        }
        return new double[]{var1, var2, var3, var4};
    }
	
	public static VoxelShape getShapeFromAABB(List<AxisAlignedBB> aabbs) {
		List<VoxelShape> shapes = new ArrayList<>();
		for(AxisAlignedBB aabb : aabbs) {
			shapes.add(VoxelShapes.create(aabb));
		}
		VoxelShape result = VoxelShapes.empty();
	    for(VoxelShape shape : shapes)
	    {
	        result = VoxelShapes.joinUnoptimized(result, shape, IBooleanFunction.OR);
	    }
	    return result.optimize();
	}
	
	public static VoxelShape rotateVoxelShape(VoxelShape shape, Direction facing) {
		List<AxisAlignedBB> aabbs = shape.toAabbs();
		List<AxisAlignedBB> newAabbs = Utils.rotateAABB(aabbs, facing);
		return getShapeFromAABB(newAabbs);
	}
	
	public static VoxelShape getShapeFromAABB(AxisAlignedBB AABB) {
		return VoxelShapes.create(AABB);
	}
	
	public static Object getPrivateMember(Object object, String fieldName) {
		
		try {
			Field[] fields = object.getClass().getDeclaredFields();
			for(Field field: fields) {
				if(field.getName() == fieldName) {
					field.setAccessible(true);
						return field.get(object);
				}
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return object;
	}
	
	public static void setPrivateMember(Object object, String fieldName, Object value) {
		try {
			Field[] fields = object.getClass().getDeclaredFields();
			for(Field field: fields) {
				if(field.getName() == fieldName) {
					field.setAccessible(true);
					field.set(object, value);
				}
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	public static void usePrivateMethod(Object object, String methodName, Object... args) {
		try {
			Method[] methods = object.getClass().getDeclaredMethods();
			for(Method method: methods) {
				method.setAccessible(true);
				if(method.getName() == methodName) {
					method.invoke(object, args);
				}
			}
		}catch(Exception e) {}
	}
	
	public static int getOpposite(int value) {
		if(value == 1)
			return 0;
		if(value == 0)
			return 1;
		return value;
	}
	
	public static BlockPos offset(BlockPos pos, Direction facing, float distance) {
		if(facing == null)
			return pos;
//		Vector3f vec = facing.step();
//		vec.mul(distance);
		return pos.offset(facing.getStepX() * distance, facing.getStepY() * distance, facing.getStepZ() * distance);
	}
	
	public static boolean isBlockPowered(BlockPos pos, World world) { 
		return world.hasNeighborSignal(pos);
	}
	
	public static String getFormalText(String input) {
		if(input.contains("_")) {
			String[] ss = input.split("_");
			if(ss.length > 0) {
				String s = "";
				for (int i = 0; i < ss.length; i++) {
					s += makeFirstCaps(ss[i]) + " ";
				}
				if(s.length() > 0)
					s = s.substring(0, s.length()-1);
				return s;
			}
			return makeFirstCaps(input);
		}
		return makeFirstCaps(input);
	}
	
	public static String fromFormalText(String input) {
		String s = input.replace(" ", "_");
		return s.toUpperCase();
	}
	
	public static String makeFirstCaps(String input) {
		if(input.length() <= 0)
			return input;
		String s = input.toLowerCase();
		s = s.replaceFirst(s.substring(0, 1), s.substring(0, 1).toUpperCase());
		return s;
	}
	
	public static GameProfile getProfile(String username) {
		if(username.isEmpty())
			return null;
		GameProfile gameprofile = new GameProfile((UUID)null, username);
        gameprofile = SkullTileEntity.updateGameprofile(gameprofile);
        return gameprofile;
	}
	
	public static PlayerEntity getPlayer(World world, String username)  {
		PlayerEntity player = null;
		for (PlayerEntity pl : world.players()) {
			if(pl.getName().getString().equals(username)) {
				player = pl;
				break;
			}
		}
		return player;
	}
	
	public static CompoundNBT putObjectArray(Object[] array) {
		return putObjectArray(new CompoundNBT(), array);
	}
	
	public static CompoundNBT putObjectArray(CompoundNBT nbt, Object[] array) {
		for (int i = 0; i < array.length; i ++) {
			Object ob = array[i];
			if(ob instanceof Boolean)
				nbt.putBoolean(i + "",(boolean)ob);
			else if(ob instanceof String)
				nbt.putString(i + "", (String)ob);
			else if(ob instanceof Integer)
				nbt.putInt(i + "", (int)ob);
			else if(ob instanceof Float)
				nbt.putFloat(i + "", (float)ob);
			else if(ob instanceof Double)
				nbt.putDouble(i + "", (double)ob);
			else if(ob instanceof int[])
				nbt.putIntArray(i +"", (int[])ob);
			else if(ob instanceof INBTSerializable<?>)
				nbt.put(i + "", ((INBTSerializable<?>)ob).serializeNBT());
		}
		return nbt;
	}
	
	public static Object[] getObjectArray(CompoundNBT nbt) {
		List<Object> objs = new ArrayList<Object>();
		
		for (String key : nbt.getAllKeys()){
			String type = nbt.get(key).getType().getName();
			if(type.equals("STRING")) {
				objs.add(nbt.getString(key));
			}else if(type.equals("BYTE")) {
				objs.add(nbt.getBoolean(key));
			}else if(type.equals("DOUBLE")) {
				objs.add(nbt.getDouble(key));
			}else if(type.equals("FLOAT")) {
				objs.add(nbt.getFloat(key));
			}else if(type.equals("INT")) {
				objs.add(nbt.getInt(key));
			}else if(type.equals("INT[]")) {
				objs.add(nbt.getIntArray(key));
			}else if(type.equals("COMPOUND"))
				objs.add(nbt.getCompound(key));
		}
		return objs.toArray();
	}
}
