package com.mushroom.automatia.minimus;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public class PlayerChamberHistory {
	private BlockPos exit_pos = new BlockPos(0,100,0);//the place the player was standing at when they used the chambers
	private int Index = -1;//the index of the chambers they entered
	private ResourceKey<Level> dimension = Level.OVERWORLD;//the dimension they were in, can only be vanilla dimensions
	
	public BlockPos getExit() {return this.exit_pos;}
	public int getIndex() {return this.Index;}
	public ResourceKey<Level> getDimension() {return this.dimension;}
	public void setDimension(ResourceKey<Level> dim) {this.dimension=dim;}
	public void setIndex(int index) {this.Index = index;}
	public void setExit(BlockPos pos) {this.exit_pos = pos;}
	
	public void copyFrom(PlayerChamberHistory pos) {this.exit_pos=pos.getExit();this.dimension=pos.getDimension();this.Index=pos.getIndex();}
	
	public void saveNBTData(CompoundTag tag) {
		tag.putInt("exit_x",this.exit_pos.getX());
		tag.putInt("exit_y", this.exit_pos.getY());
		tag.putInt("exit_z", this.exit_pos.getZ());
		tag.putInt("index", this.Index);
		if(this.dimension==Level.NETHER) {tag.putInt("d", 1);}
		else if(this.dimension==Level.END) {tag.putInt("d", 2);}
		else {tag.putInt("d", 0);}
	}
	public void loadNBTData(CompoundTag tag) {
		this.exit_pos = new BlockPos(tag.getInt("exit_x"),tag.getInt("exit_y"),tag.getInt("exit_z"));
		this.Index = tag.getInt("index");
		int dim = tag.getInt("d");
		if(dim==0) {this.dimension=Level.OVERWORLD;}
		else if(dim==1) {this.dimension=Level.NETHER;}
		else if(dim==2) {this.dimension=Level.END;}
		else {throw new RuntimeException("d tag should be 0, 1, or 2!");}
	}
}
