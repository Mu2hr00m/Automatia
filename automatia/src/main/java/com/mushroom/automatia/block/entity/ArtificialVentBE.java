package com.mushroom.automatia.block.entity;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import com.mushroom.automatia.init.BlockInit;
import com.mushroom.automatia.init.ModBlockEntities;
import com.mushroom.automatia.init.ModTags;
import com.mushroom.automatia.block.IgneousFormer;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

public class ArtificialVentBE extends BlockEntity{
	private final BlockPos pos;
	public Block blockToCreate = Blocks.AIR;
	private static final int SPEED = 140;
	private static final float IGNEOUS_FORMER_SPEED = 0.8f;
	private static final float GEOTHERM_SPEED = 1.5f;
	public float ticks = 0;

	public ArtificialVentBE(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		this.pos = pos;
	}
	public ArtificialVentBE(BlockPos pos, BlockState state) {
		super(ModBlockEntities.ARTIFICIAL_VENT_ENTITY.get(), pos, state);
		this.pos = pos;
	}
	public void tickServer(Level level, BlockState state) {
		this.ticks++;
		if(level.getBlockState(pos.above()).is(BlockInit.IGNEOUS_FORMER.get())&&blockToCreate.equals(Blocks.LAVA)) {this.ticks+=IGNEOUS_FORMER_SPEED;}
		if(this.ticks>=SPEED) {
			this.ticks=0;
			validateGeyser(level, state);
			makeProduct(level, state);
		}
	}
	private void makeProduct(Level level, BlockState state) {
		if(level.getBlockState(this.pos.below()).is(ModTags.Blocks.GEYSER_SOURCE)) {
			this.blockToCreate = level.getBlockState(this.pos.below()).getBlock();
		}
		if(state.getValue(BlockStateProperties.OPEN)&&level.getBlockState(this.pos.above()).isAir()) {
			level.setBlockAndUpdate(this.pos.above(), blockToCreate.defaultBlockState());
		}
		if(level.getBlockState(pos.above()).is(BlockInit.IGNEOUS_FORMER.get())&&blockToCreate.equals(Blocks.LAVA)) {
			((IgneousFormer) BlockInit.IGNEOUS_FORMER.get()).makeProduct(level,pos.above());
		}
	}
	private void validateGeyser(Level level, BlockState state) {
		AtomicInteger count = new AtomicInteger(0);
		Stream<BlockPos> blocks = BlockPos.betweenClosedStream(pos.north().east(), pos.south().west().below());
		blocks.map(BlockPos::immutable).forEach(d -> {
			if(level.getBlockState(d).is(ModTags.Blocks.SULFURSTONE)) {
				count.incrementAndGet();
			}
		});
		if(count.intValue()>=16) {
			state = state.setValue(BlockStateProperties.OPEN, true);
		}
		else {
			state = state.setValue(BlockStateProperties.OPEN, false);
		}
		level.setBlockAndUpdate(pos, state);
	}
	@Override
	public void load(CompoundTag tag) {
		if(tag.contains("ticks")) {this.ticks = tag.getInt("ticks");}
		super.load(tag);
	}
	@Override
	public void saveAdditional(CompoundTag tag) {
		tag.putFloat("ticks", this.ticks);
	}
}
