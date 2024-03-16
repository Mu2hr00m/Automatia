package com.mushroom.automatia.block;

import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class TransfungusMycelium extends Block {
	public static final IntegerProperty AGE = BlockStateProperties.AGE_7;
	public final Block ABSORBER;
	public final Block TRANSFUNGUS;

	public TransfungusMycelium(Properties p_49795_, Block absorber, Block fungus) {
		super(p_49795_);
		ABSORBER = absorber;
		TRANSFUNGUS = fungus;
		this.registerDefaultState(this.stateDefinition.any().setValue(AGE, Integer.valueOf(0)));
	}
	public void randomTick(BlockState state, ServerLevel slevel, BlockPos pos, Random random) {
		if(state.getValue(AGE)==7) {
			if(slevel.getBlockState(pos.below()).is(ABSORBER)&&slevel.getBlockState(pos.above()).isAir()) {
				slevel.setBlock(pos, state.setValue(AGE, 0), UPDATE_ALL);
				slevel.destroyBlock(pos.below(),false);
				slevel.setBlock(pos.above(), this.TRANSFUNGUS.defaultBlockState(), UPDATE_ALL);
				return;
			}
			return;
		}
		if(slevel.getBlockState(pos.below()).is(ABSORBER)) {
			slevel.destroyBlock(pos.below(),false);
			slevel.setBlock(pos, state.setValue(AGE, state.getValue(AGE)+1), UPDATE_ALL);
		}
		return;
	}
	@Override
	public void createBlockStateDefinition(Builder<Block,BlockState> builder) {
		builder.add(AGE);
	}
}
