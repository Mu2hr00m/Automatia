package com.mushroom.automatia.energy;

import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;

public class BundledEnergy {
	public CustomEnergyStorage storage;
	public LazyOptional<IEnergyStorage> energy;
	
	public BundledEnergy(CustomEnergyStorage storage, LazyOptional<IEnergyStorage> energy) {
		this.energy = energy;
		this.storage = storage;
	}
}
