package com.mushroom.automatia.block;

import java.util.Random;

import javax.annotation.Nullable;

import com.mushroom.automatia.block.entity.ArtificialVentBE;
import com.mushroom.automatia.block.entity.EnergyCubeBE;
import com.mushroom.automatia.block.entity.GeyserVentBE;
import com.mushroom.automatia.init.BlockInit;
import com.mushroom.automatia.init.ModBlockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class GeyserVent extends BaseEntityBlock {
	public static final BooleanProperty OPEN = BlockStateProperties.OPEN;

	public GeyserVent(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(OPEN, Boolean.valueOf(false)));
	}
	@Override
	public void createBlockStateDefinition(Builder<Block,BlockState> builder) {
		builder.add(OPEN);
	}
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		if(state.is(BlockInit.SULFURSTONE_VENT.get())) {
			return new GeyserVentBE(ModBlockEntities.GEYSER_VENT_ENTITY.get(), pos, state);
		}
		return new ArtificialVentBE(ModBlockEntities.ARTIFICIAL_VENT_ENTITY.get(), pos, state);
	}
	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}
	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type){
		if(level.isClientSide()) {return null;}
		return (lvl, pos, blockState, t) -> {
			if (t instanceof GeyserVentBE tile) {
				tile.tickServer(level, state);
			}
		};
	}
}
