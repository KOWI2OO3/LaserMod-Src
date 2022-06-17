package KOWI2003.LaserMod.events;

import com.google.gson.JsonObject;

import KOWI2003.LaserMod.Reference;
import KOWI2003.LaserMod.config.ModConfig;
import KOWI2003.LaserMod.utils.JsonUtils;
import KOWI2003.LaserMod.utils.MultiVersionUtils;
import KOWI2003.LaserMod.utils.Utils;
import KOWI2003.LaserMod.utils.WebUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class WorldJoinEvent {
	
    private final String PREFIX = TextFormatting.GOLD + "-> ";
    
	@SubscribeEvent
	public void onEvent(PlayerLoggedInEvent event) {
		try {
			if(ModConfig.GetConfig().updateChecker.useUpdateChecker) {
				PlayerEntity player = (PlayerEntity) event.getEntityLiving();
				
				String version = "";
				version = ModConfig.GetConfig().updateChecker.updateCheckerType.equals("latest") ? Utils.getLatestVersion() : Utils.getRecommendedVersion();

				System.out.println(Utils.getLatestVersion() + " -> " + Reference.VESRION);
				
				if(version == null)
					version = Reference.VESRION;
				
				
				String message = "";
				try {
					JsonObject messages = JsonUtils.getJsonObject(WebUtils.getJsonObj(Reference.UPDATE_URL), "message");
					message = messages.get(MultiVersionUtils.getMCVersionGroup()).getAsString();
				}catch(Exception e) {}
				
				if(!message.isEmpty()) {
					boolean useLink = message.contains("#link");
					
					if(message.contains("#check_version"))
						message = message.replace("#check_version", version);
					if(message.contains("#version"))
						message = message.replace("#version", Reference.VESRION);
					if(message.contains("#mc"))
						message = message.replace("#mc", MultiVersionUtils.getMCVersion());
					if(message.contains("#player"))
						message = message.replace("#player", player.getName().getString());
					if(useLink)
						message = message.replace("#link", "");
					
					StringTextComponent mod = new StringTextComponent(TextFormatting.DARK_AQUA  + "---Laser Mod---");
					StringTextComponent s = new StringTextComponent(PREFIX + message);
					player.sendMessage(mod, player.getUUID());
					player.sendMessage(s, player.getUUID());
					if(useLink) {
						StringTextComponent link = new StringTextComponent(PREFIX + TextFormatting.BLUE + TextFormatting.UNDERLINE + "Click Here");
						Style style = link.getStyle();
						style = link.getStyle().withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, JsonUtils.getValue(WebUtils.getJsonObj(Reference.UPDATE_URL), "homepage")));
						style = style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new StringTextComponent("go to the mod page")));
						link.setStyle(style);
						player.sendMessage(link, player.getUUID());
					}
				}else {
					if(!Reference.VESRION.equals(version)) {
						StringTextComponent mod = new StringTextComponent(TextFormatting.DARK_AQUA  + "---Laser Mod---");
						StringTextComponent s = new StringTextComponent(PREFIX + "version " + version + " is out, your client is out-dated, to update ");
						StringTextComponent link = new StringTextComponent(PREFIX + TextFormatting.BLUE + TextFormatting.UNDERLINE + "Click Here");
						Style style = link.getStyle();
						style = link.getStyle().withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, JsonUtils.getValue(WebUtils.getJsonObj(Reference.UPDATE_URL), "homepage")));
						style = style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new StringTextComponent("go to the mod page")));
						link.setStyle(style);
						player.sendMessage(mod, player.getUUID());
						player.sendMessage(s, player.getUUID());
						player.sendMessage(link, player.getUUID());
					}
				}
			}
		}catch(Exception ex) {}
	}
}
