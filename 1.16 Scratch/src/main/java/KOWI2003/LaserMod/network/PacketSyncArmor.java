package KOWI2003.LaserMod.network;

import java.util.function.Supplier;

import KOWI2003.LaserMod.items.ItemLaserArmorBase;
import KOWI2003.LaserMod.utils.LaserItemUtils;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketSyncArmor {

	public boolean active;
	public EquipmentSlotType slot;
	
	public PacketSyncArmor(PacketBuffer buf) {
		active = buf.readBoolean();
		slot = EquipmentSlotType.values()[buf.readInt()];
	}
	
	public PacketSyncArmor(boolean active, EquipmentSlotType slot) {
		this.active = active;
		this.slot = slot;
	}
	
	public void toBytes(PacketBuffer buf) {
		buf.writeBoolean(active);
		buf.writeInt(slot.ordinal());
	}
	
	public void handle(Supplier<NetworkEvent.Context> ctx) {
	    ctx.get().enqueueWork(() -> {
	        // Work that needs to be thread-safe (most work)
	    	
	        //ServerPlayer sender = ctx.get().getSender(); // the client that sent this packet
	    	
	        if(ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT) {
	        	
	        }else {
	        	ServerPlayerEntity sender = ctx.get().getSender();
	        	for (ItemStack armor: sender.getArmorSlots()) {
					if(armor.getEquipmentSlot() == slot) 
					{
						if(armor.getItem() instanceof ItemLaserArmorBase) {
							armor = LaserItemUtils.setExtended(armor, active);
						}
					}
				}
	        }
	    });
	    ctx.get().setPacketHandled(true);
	    //return true;
	}
	
}
