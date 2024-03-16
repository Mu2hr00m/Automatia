package com.mushroom.automatia.energy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.mushroom.automatia.chambers.PlayerChambers;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class CustomEnergyStorageProvider implements ICapabilityProvider, INBTSerializable<CompoundTag>{
	public static Capability<CustomEnergyStorage> ENERGY = CapabilityManager.get(new CapabilityToken<>() {});
	
	private final CustomEnergyStorage energyStorage  = new CustomEnergyStorage(0,0,0);
	private final LazyOptional<IEnergyStorage> energy = LazyOptional.of(() -> energyStorage);
	
	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap){
		if (cap==CapabilityEnergy.ENERGY) {
			return energy.cast();
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
		nbt.putInt("energy", energyStorage.getEnergyStored());
		nbt.putInt("max", energyStorage.getMaxEnergyStored());
		return nbt;
	}
	@Override
	public void deserializeNBT(CompoundTag nbt) {
		energyStorage.setEnergyMaximum(nbt.getInt("max"));
		energyStorage.setEnergy(nbt.getInt("energy"));
	}

}
