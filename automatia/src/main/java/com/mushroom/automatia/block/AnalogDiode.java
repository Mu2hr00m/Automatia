package com.mushroom.automatia.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class AnalogDiode extends Block{
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final IntegerProperty POWER = BlockStateProperties.POWER;
	public static VoxelShape SHAPE = Block.box(0, 0, 0, 16, 2, 16);
	
	public AnalogDiode(Properties properties) {
		super(properties);
	}
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos otherPos, boolean something) {
		if (!level.isClientSide) {
			if (level.getBlockState(pos.below()).isFaceSturdy(level, pos.below(), Direction.UP)) {
				//if(pos.relative(state.getValue(FACING).getOpposite())!=otherPos) {return;}
				//level.players().forEach(p -> {p.displayClientMessage(new TextComponent(String.valueOf(otherPos.toString())), false);});
				BlockState powerstate = level.getBlockState(pos.relative(state.getValue(FACING).getOpposite()));//state of block powering this block
				int power = powerstate.getBlock().isSignalSource(powerstate)||!powerstate.isRedstoneConductor(level,pos.relative(state.getValue(FACING).getOpposite())) ?//is the block a signal source or is it transparent to redstone?
						powerstate.getSignal(level, pos.relative(state.getValue(FACING).getOpposite()),state.getValue(FACING).getOpposite()) : //if the block is a signal source or transparent, read its strength
						level.getBestNeighborSignal(pos.relative(state.getValue(FACING).getOpposite())); //if the block isnt a signal source but allows redstone, get best neighbor soft signal
				if(power!=state.getValue(POWER)) {
					level.setBlock(pos, state.setValue(POWER, power), 2);
					level.neighborChanged(pos.above(), this, pos);
					level.neighborChanged(pos.below(), this, pos);
					level.neighborChanged(pos.relative(state.getValue(FACING).getClockWise()), this, pos);
					level.neighborChanged(pos.relative(state.getValue(FACING).getCounterClockWise()), this, pos);
					level.neighborChanged(pos.relative(state.getValue(FACING)), this, pos);
					level.updateNeighborsAtExceptFromFacing(pos.relative(state.getValue(FACING)), this, state.getValue(FACING).getOpposite());
				}
			} else {
				dropResources(state, level, pos);
				level.removeBlock(pos, false);
			}
		}
	}
	public int getSignal(BlockState state, BlockGetter block, BlockPos pos, Direction direction) {
		return direction==state.getValue(FACING).getOpposite() ? state.getValue(POWER) : 0;
	}
	public int getDirectSignal(BlockState state, BlockGetter block, BlockPos pos, Direction direction) {
		return direction==state.getValue(FACING).getOpposite() ? state.getValue(POWER) : 0;
	}
	public boolean isSignalSource(BlockState state) {
	    return true;
	}
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection()).setValue(POWER, 0);}
	@Override
	public void createBlockStateDefinition(Builder<Block,BlockState> builder) {
		builder.add(FACING);
		builder.add(POWER);
	}
	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}
}
