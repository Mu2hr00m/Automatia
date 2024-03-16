package com.mushroom.automatia.block.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.util.concurrent.Service.State;
import com.mushroom.automatia.energy.CustomEnergyStorage;
import com.mushroom.automatia.energy.FakeEnergyStorage;
import com.mushroom.automatia.init.BlockInit;
import com.mushroom.automatia.init.ModBlockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.ticks.ScheduledTick;
import net.minecraft.world.ticks.TickPriority;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.Tags;

public class EnergyLaserEmitterBE extends BlockEntity{
	private int MAX_BEAM_TRAVEL = 16;
	public static int maxPower = 1000;
	public boolean hasScheduledTick = false;
	public final CustomEnergyStorage energyStorage = createEnergy();
	private final LazyOptional<IEnergyStorage> energy = LazyOptional.of(() -> energyStorage);
	public final CustomEnergyStorage inputStorage = createInputEnergy();
	private final LazyOptional<IEnergyStorage> input = LazyOptional.of(() -> inputStorage);
	public EnergyLaserEmitterBE(BlockPos pos, BlockState state) {
		super(ModBlockEntities.ENERGY_LASER_EMITTER_ENTITY.get(), pos, state);
	}
	private CustomEnergyStorage createEnergy() {
		return new CustomEnergyStorage(maxPower,maxPower,maxPower) {
			@Override
			protected void onEnergyChanged() {setChanged();}
		};
	}
	private CustomEnergyStorage createInputEnergy() {
		return new CustomEnergyStorage(maxPower,maxPower,0) {
			@Override
			protected void onEnergyChanged() {setChanged();}
			@Override
			public void addEnergy(int energy) {
				super.addEnergy(energy);
				hasScheduledTick = true;
			}
			@Override
			public int receiveEnergy(int energy, boolean simulate) {
				hasScheduledTick = true;
				return super.receiveEnergy(energy, simulate);
			}
		};
	}
	public void tickServer(Level level, BlockState state) {
		if(energyStorage.getEnergyStored()!=0) {
			pushPower(level, state);
		}
		if(this.hasScheduledTick) {
			level.scheduleTick(this.worldPosition, BlockInit.ENERGY_LASER_EMITTER.get(), 1, TickPriority.HIGH);
		}
		this.inputStorage.setEnergyMaximum(EnergyLaserEmitterBE.maxPower-this.energyStorage.getEnergyStored());
	}
	private void pushPower(Level level, BlockState state) {
		int d = 1;
		while(d<=MAX_BEAM_TRAVEL) {
			BlockState dstate = level.getBlockState(worldPosition.relative(state.getValue(BlockStateProperties.FACING), d));
			BlockEntity be = level.getBlockEntity(this.worldPosition.relative(state.getValue(BlockStateProperties.FACING), d));
			if(be!=null) {
				if(be.getCapability(CapabilityEnergy.ENERGY, state.getValue(BlockStateProperties.FACING).getOpposite()).isPresent()) {
					be.getCapability(CapabilityEnergy.ENERGY, state.getValue(BlockStateProperties.FACING).getOpposite()).map(handler -> {
						if(handler.canReceive()) {
							int received = handler.receiveEnergy(Math.min(energyStorage.getEnergyStored(), 1000), false);
							energyStorage.removeEnergy(received);
							setChanged();
							return energyStorage.getEnergyStored()>0;
						}
						return true;
					}).orElse(true);
					return;
				}
			}
			if(!(dstate.is(Tags.Blocks.GLASS)||dstate.is(Tags.Blocks.GLASS_PANES)||dstate.isAir())) {return;}
			d++;
		}
	}
	@Override
	public void load(CompoundTag tag) {
		if(tag.contains("energy")) {energyStorage.deserializeNBT(tag.get("energy"));}
		super.load(tag);
	}
	@Override
	public void saveAdditional(CompoundTag tag) {
		tag.put("energy", energyStorage.serializeNBT());
	}
	@Override
	public void saveToItem(ItemStack stack) {
		CompoundTag tag = this.saveWithoutMetadata();
		tag.put("energy", energyStorage.serializeNBT());
		BlockItem.setBlockEntityData(stack, this.getType(), tag);
	}
	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side){
		if(cap==CapabilityEnergy.ENERGY&&side==null) {
			return energy.cast();
		}
		if(cap==CapabilityEnergy.ENERGY&&side!=null) {
			return input.cast();
		}
		return super.getCapability(cap,side);
	}
	@Override
	public void setRemoved() {
		super.setRemoved();
		energy.invalidate();
		input.invalidate();
	}
}
