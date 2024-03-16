package com.mushroom.automatia.block;

import java.util.ArrayList;
import java.util.List;

import com.mushroom.automatia.init.ModTags;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class IgneousFormer extends Block{

	public IgneousFormer(Properties properties) {
		super(properties);
	}
	public void makeProduct(Level level, BlockPos pos) {
		if(level.getBlockState(pos.north()).is(Blocks.WATER)||
				level.getBlockState(pos.east()).is(Blocks.WATER)||
				level.getBlockState(pos.south()).is(Blocks.WATER)||
				level.getBlockState(pos.west()).is(Blocks.WATER)||
				level.getBlockState(pos.north()).getOptionalValue(BlockStateProperties.WATERLOGGED).orElse(false).booleanValue()||
				level.getBlockState(pos.east()).getOptionalValue(BlockStateProperties.WATERLOGGED).orElse(false).booleanValue()||
				level.getBlockState(pos.south()).getOptionalValue(BlockStateProperties.WATERLOGGED).orElse(false).booleanValue()||
				level.getBlockState(pos.west()).getOptionalValue(BlockStateProperties.WATERLOGGED).orElse(false).booleanValue()) {
			List<BlockState> states = new ArrayList<BlockState>(4);
			if(level.getBlockState(pos.north()).is(ModTags.Blocks.IGNEOUS_FORMER_SOURCE)) {states.add(level.getBlockState(pos.north()));}
			if(level.getBlockState(pos.east()).is(ModTags.Blocks.IGNEOUS_FORMER_SOURCE)) {states.add(level.getBlockState(pos.east()));}
			if(level.getBlockState(pos.south()).is(ModTags.Blocks.IGNEOUS_FORMER_SOURCE)) {states.add(level.getBlockState(pos.south()));}
			if(level.getBlockState(pos.west()).is(ModTags.Blocks.IGNEOUS_FORMER_SOURCE)) {states.add(level.getBlockState(pos.west()));}
			if(states.size()>0&&level.getBlockState(pos.above()).isAir()) {
				level.setBlock(pos.above(), states.get(RANDOM.nextInt(states.size())).getBlock().defaultBlockState(), UPDATE_ALL);
			}
		}
	}
}
