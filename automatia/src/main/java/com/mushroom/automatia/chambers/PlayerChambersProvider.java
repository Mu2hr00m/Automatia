package com.mushroom.automatia.chambers;

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

public class PlayerChambersProvider implements ICapabilityProvider, INBTSerializable<CompoundTag>{
	public static Capability<PlayerChambers> PLAYER_CHAMBERS = CapabilityManager.get(new CapabilityToken<>() {});
	
	private PlayerChambers playerChambers = null;
	private final LazyOptional<PlayerChambers> opt = LazyOptional.of(this::createPlayerChambers);
	
	private PlayerChambers createPlayerChambers() {
		if(playerChambers==null) {
			playerChambers = new PlayerChambers();
		}
		return playerChambers;
	}
	
	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap){
		if (cap==PLAYER_CHAMBERS) {
			return opt.cast();
		}
		return LazyOptional.empty();
	}
	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side){
		return getCapability(cap);
	}
	@Override
	public CompoundTag serializeNBT() {
		CompoundTag nbt = new CompoundTag();
		createPlayerChambers().saveNBTData(nbt);
		return nbt;
	}
	@Override
	public void deserializeNBT(CompoundTag nbt) {
		createPlayerChambers().loadNBTData(nbt);
	}

}
