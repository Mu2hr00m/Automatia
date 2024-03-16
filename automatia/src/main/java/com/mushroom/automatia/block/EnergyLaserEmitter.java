package com.mushroom.automatia.block;

import java.util.Random;

import javax.annotation.Nullable;

import com.mushroom.automatia.block.entity.EnergyLaserEmitterBE;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.level.block.BaseEntityBlock;

public class EnergyLaserEmitter extends BaseEntityBlock{
	
	public static final DirectionProperty FACING = BlockStateProperties.FACING;
	
	public EnergyLaserEmitter(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.UP));
	}
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite());
	}
	@Override
	public void createBlockStateDefinition(Builder<Block,BlockState> builder) {
		builder.add(FACING);
	}
	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type){
		if(level.isClientSide()) {return null;}
		return (lvl, pos, blockState, t) -> {
			if (t instanceof EnergyLaserEmitterBE tile) {
				tile.tickServer(level, state);
			}
		};
	}
	public void tick(BlockState state, ServerLevel level, BlockPos pos, Random random) {
		EnergyLaserEmitterBE be = (EnergyLaserEmitterBE)level.getBlockEntity(pos);
		be.energyStorage.addEnergy(be.inputStorage.getEnergyStored());
		be.inputStorage.setEnergy(0);
		be.setChanged();
	}
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new EnergyLaserEmitterBE(pos,state);
	}
	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}
}
