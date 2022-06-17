package KOWI2003.LaserMod.handlers;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

public class OreDictionaryHandler {

	public static List<Item> getStacksFrom(ITag<Item> tag) {
		return tag.getValues();
	}
	
	public static boolean isInTag(ITag<Item> tag, Item item) {
		return item.is(tag);
	}
	
	public static boolean isInTag(ITag<Item> tag, ItemStack stack) {
		return stack.getItem().is(tag);
	}
	
	public static boolean isEmpty(ITag<Item> tag) {
		return tag.getValues().size() <= 0;
	}
	
	public static boolean isEmpty(ResourceLocation id) {
		return createOrGetTag(id).getValues().size() <= 0;
	}
	
	public static boolean doesTagExist(ResourceLocation id) {
		return ItemTags.getAllTags().getAllTags().containsKey(id);
	}
	
	@Nullable
	public static ITag<Item> getTag(ResourceLocation id) {
		Map<ResourceLocation, ITag<Item>> tags = ItemTags.getAllTags().getAllTags();
		if(tags.containsKey(id)) {
			return tags.get(id);
		}
		return null;
	}
	
	public static ITag<Item> createOrGetTag(ResourceLocation id) {
		return ItemTags.createOptional(id);
	}
	
	public static ITag<Item> createOrGetTag(ResourceLocation id, Set<Supplier<Item>> items) {
		return ItemTags.createOptional(id, items);
	}
	
}
