package KOWI2003.LaserMod.init;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.settings.KeyBinding;


public class ModKeybindings {

public static final List<KeyBinding> mappings = new ArrayList<>();
	
	public static final KeyBinding ArmorToggle = register(new KeyBinding("lasermod.armor_toggle", GLFW.GLFW_KEY_V, "key.categories.lasermod"));

	public static KeyBinding register(KeyBinding keyBinding)
	{
		mappings.add(keyBinding);
		return keyBinding;
	}
	
}
