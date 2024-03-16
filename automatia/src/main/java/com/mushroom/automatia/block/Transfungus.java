package com.mushroom.automatia.block;

import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class Transfungus extends Block {

	public Transfungus(Properties properties) {
		super(properties);
	}
	@Override
	public RenderShape getRenderShape(BlockState p_49232_) {
		return RenderShape.MODEL;
	}
	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		return Block.box(5, 0, 5, 11, 6, 11);
	}
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos otherPos, boolean something) {
		if (!level.isClientSide) {
			if (!level.getBlockState(pos.below()).isFaceSturdy(level, pos.below(), Direction.UP)) {
				dropResources(state, level, pos);
				level.removeBlock(pos, false);
			}
		}
	}
	public static void updateOrDestroy(BlockState state, BlockState other, LevelAccessor level, BlockPos pos, int updateContext, int ne) {
		if (!level.isClientSide()) {
			if (!level.getBlockState(pos.below()).isFaceSturdy(level, pos.below(), Direction.UP)||level.getBlockState(pos.below())==Blocks.AIR.defaultBlockState()) {
				level.destroyBlock(pos, true);
			}
	    }
	}
}
