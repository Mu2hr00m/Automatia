package com.mushroom.automatia.chambers;

import com.mushroom.automatia.automatia;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class PlayerChambersEvents {
	
	public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
		if(event.getObject() instanceof Player) {
			if(!event.getObject().getCapability(PlayerChambersProvider.PLAYER_CHAMBERS).isPresent()) {
				event.addCapability(new ResourceLocation(automatia.MOD_ID, "playerchambers"), new PlayerChambersProvider());
			}
		}
	}
	
	public static void onPlayerCloned(PlayerEvent.Clone event) {
		if(event.isWasDeath()) {
			event.getOriginal().getCapability(PlayerChambersProvider.PLAYER_CHAMBERS).ifPresent(oldStore -> {event.getPlayer().getCapability(PlayerChambersProvider.PLAYER_CHAMBERS).ifPresent(newStore -> {newStore.copyFrom(oldStore);});});
		}
	}
	
	public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {event.register(PlayerChambers.class);}
	
	public static void onWorldTick(TickEvent.WorldTickEvent event) {
		return;
	}
}
