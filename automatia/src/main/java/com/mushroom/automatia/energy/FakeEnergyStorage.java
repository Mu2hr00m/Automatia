package com.mushroom.automatia.energy;

import net.minecraftforge.energy.EnergyStorage;

public class FakeEnergyStorage extends EnergyStorage{
	private CustomEnergyStorage storage;
	public FakeEnergyStorage(CustomEnergyStorage storage, int capacity, int maxTransfer) { //symmetrical extract and insert
		super(capacity, maxTransfer, maxTransfer);
		this.storage = storage;
	}
	public FakeEnergyStorage(CustomEnergyStorage storage, int capacity, int maxInsert, int maxExtract) { //asymmetrical extract and insert
		super(capacity, maxInsert, maxExtract);
		this.storage = storage;
	}
	//should be overridden with set to save function
	protected void onEnergyChanged() {
		
	}
	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		int rc = storage.receiveEnergy(maxReceive, simulate);
		if(rc>0&&!simulate) {
			onEnergyChanged();
		}
		return rc;
	}
	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		int rc = storage.extractEnergy(maxExtract, simulate);
		if(rc>0&&!simulate) {
			onEnergyChanged();
		}
		return rc;
	}
	public void setEnergy(int energy) {
		storage.setEnergy(energy);
		onEnergyChanged();
	}
	public void addEnergy(int energy) {
		storage.addEnergy(energy);
		onEnergyChanged();
	}
	public void removeEnergy(int energy) {
		storage.removeEnergy(energy);
		onEnergyChanged();
	}
}
