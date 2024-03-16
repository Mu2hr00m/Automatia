package com.mushroom.automatia.energy;

import net.minecraftforge.energy.EnergyStorage;

public class CustomEnergyStorage extends EnergyStorage{

	public CustomEnergyStorage(int capacity, int maxTransfer) { //symmetrical extract and insert
		super(capacity, maxTransfer, maxTransfer);
	}
	public CustomEnergyStorage(int capacity, int maxInsert, int maxExtract) { //asymmetrical extract and insert
		super(capacity, maxInsert, maxExtract);
	}
	//should be overridden with set to save function
	protected void onEnergyChanged() {
		
	}
	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		int rc = super.receiveEnergy(maxReceive, simulate);
		if(rc>0&&!simulate) {
			onEnergyChanged();
		}
		return rc;
	}
	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		int rc = super.extractEnergy(maxExtract, simulate);
		if(rc>0&&!simulate) {
			onEnergyChanged();
		}
		return rc;
	}
	public void setEnergy(int energy) {
		this.energy = energy;
		onEnergyChanged();
	}
	public void addEnergy(int energy) {
		this.energy+=energy;
		if(this.energy>this.getMaxEnergyStored()) {
			this.energy = this.getMaxEnergyStored();
		}
		onEnergyChanged();
	}
	public void removeEnergy(int energy) {
		this.energy-=energy;
		if(this.energy<0) {
			this.energy = 0;
		}
		onEnergyChanged();
	}
	public void setEnergy(int energy, boolean setChanged) {
		this.energy = energy;
		if(setChanged) {onEnergyChanged();}
	}
	public void setEnergyMaximum(int max) {
		this.capacity = max;
	}
	public void addEnergy(int energy, boolean setChanged) {
		this.energy+=energy;
		if(this.energy>this.getMaxEnergyStored()) {
			this.energy = this.getMaxEnergyStored();
		}
		if(setChanged) {onEnergyChanged();}
	}
	public void removeEnergy(int energy, boolean setChanged) {
		this.energy-=energy;
		if(this.energy<0) {
			this.energy = 0;
		}
		if(setChanged) {onEnergyChanged();}
	}
	public static int PowerBarIndex(float power, float maxPower) {
		if(power<=0) {return 0;}
		if(power>=maxPower) {return 368;}
		return (int)((power/maxPower)*47)*8;
	}
}
