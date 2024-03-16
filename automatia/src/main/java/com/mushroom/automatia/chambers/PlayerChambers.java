package com.mushroom.automatia.chambers;

import net.minecraft.nbt.CompoundTag;

public class PlayerChambers {
	private int index = -1;
	public int getIndex() {return this.index;}
	public void setIndex(int Index) {this.index = Index;}
	public void saveNBTData(CompoundTag compound) {compound.putInt("ThisIndex", this.index);}
	public void loadNBTData(CompoundTag compound) {this.index = compound.getInt("ThisIndex");}
	public void copyFrom(PlayerChambers source) {this.index = source.index;}
}
