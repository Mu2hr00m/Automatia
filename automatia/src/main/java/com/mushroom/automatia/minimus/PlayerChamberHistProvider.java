package com.mushroom.automatia.minimus;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

public class PlayerChamberHistProvider implements ICapabilityProvider, INBTSerializable<CompoundTag>{
	
	public static Capability<PlayerChamberHistory> PLAYER_CHAMBER_HIST = CapabilityManager.get(new CapabilityToken<>() {});
	private PlayerChamberHistory player_chamber_history = null;
	private final LazyOptional<PlayerChamberHistory> opt = LazyOptional.of(this::createPlayerChamberHist);
	
	@Nonnull
	private PlayerChamberHistory createPlayerChamberHist() {
		if(player_chamber_history==null) {
			player_chamber_history = new PlayerChamberHistory();
		}
		return player_chamber_history;
	}
	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
		if(cap==PLAYER_CHAMBER_HIST) {return opt.cast();}
		return LazyOptional.empty();
	}
	@Nonnull
	@Override
	public <U> LazyOptional<U> getCapability(@Nonnull Capability<U> cap, @Nullable Direction side) {
		if(cap==PLAYER_CHAMBER_HIST) {return opt.cast();}
		return LazyOptional.empty();
	}
	@Override
	public CompoundTag serializeNBT() {
		CompoundTag tag = new CompoundTag();
		createPlayerChamberHist().saveNBTData(tag);
		return tag;
	}

	@Override
	public void deserializeNBT(CompoundTag tag) {
		createPlayerChamberHist().loadNBTData(tag);
	}
}
