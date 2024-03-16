package com.mushroom.automatia.block.entity;

import com.mushroom.automatia.init.ModBlockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class RandomGeneratorBE extends BlockEntity{
	public BlockPos pos;
	public double proportion = 0.5;
	
	public RandomGeneratorBE(BlockPos pos, BlockState state) {
		super(ModBlockEntities.RANDOM_GENERATOR_ENTITY.get(), pos, state);
		this.pos = pos;
	}
	@Override
	public void load(CompoundTag tag) {
		if(tag.contains("proportion")) {proportion = tag.getDouble("proportion");}
		super.load(tag);
	}
	@Override
	public void saveAdditional(CompoundTag tag) {
		tag.putDouble("proportion", proportion);
	}
}
