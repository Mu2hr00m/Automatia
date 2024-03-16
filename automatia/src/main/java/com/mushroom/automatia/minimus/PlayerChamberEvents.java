package com.mushroom.automatia.minimus;

import com.mushroom.automatia.automatia;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class PlayerChamberEvents {
	public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
		if(event.getObject() instanceof Player) {
			if(!event.getObject().getCapability(PlayerChamberHistProvider.PLAYER_CHAMBER_HIST).isPresent()) {
				event.addCapability(new ResourceLocation(automatia.MOD_ID,"player_chamber_history"), new PlayerChamberHistProvider());
			}
		}
	}
	public static void onPlayerCloned(PlayerEvent.Clone event) {
		if(event.isWasDeath()) {
			event.getOriginal().getCapability(PlayerChamberHistProvider.PLAYER_CHAMBER_HIST).ifPresent(old -> {
				event.getPlayer().getCapability(PlayerChamberHistProvider.PLAYER_CHAMBER_HIST).ifPresent(new_ -> {
					new_.copyFrom(old);
				});
			});
		}
	}
	public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {event.register(PlayerChamberHistProvider.class);}
	
}
