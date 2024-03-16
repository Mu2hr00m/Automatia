package com.mushroom.automatia.block.entity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.mushroom.automatia.energy.CustomEnergyStorage;
import com.mushroom.automatia.init.ModBlockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class SolarPanelBE extends BlockEntity{
	public static int maxPower = 5000;
	public int energy_generation = 0;
	private final CustomEnergyStorage energyStorage = createEnergy();
	private final LazyOptional<IEnergyStorage> energy = LazyOptional.of(() -> energyStorage);
	public SolarPanelBE(BlockPos pos, BlockState state) {
		super(ModBlockEntities.SOLAR_PANEL_ENTITY.get(), pos, state);
	}
	private CustomEnergyStorage createEnergy() {
		return new CustomEnergyStorage(maxPower,0,1000) {
			@Override
			protected void onEnergyChanged() {setChanged();}
		};
	}
	public void tickServer(Level level, BlockState state) {
		energy_generation = 0;
		int day = (int)level.dayTime()%24000;
		if(day>12000) {day=-1;}
		if(day>6000) {day=day*-1+12000;}
		if(day>=100) {
			float light = level.getBrightness(LightLayer.SKY, worldPosition);
			float multiplier = 2;
			if(day<1100) {multiplier = (float)(day-100)/500f;}
			if(light>=5) {energy_generation+=(int)((light-5)*multiplier);}
			energyStorage.addEnergy(energy_generation);
			setChanged();
		}
		else if(level.getMoonPhase()==0) {
			energy_generation=1;
			energyStorage.addEnergy(energy_generation);
			setChanged();
		}
		pushPower();
	}
	private void pushPower() {
		int capacity = energyStorage.getEnergyStored();
		if(capacity>0) {
			BlockEntity be = level.getBlockEntity(worldPosition.relative(Direction.DOWN));
			if(be!=null) {
				be.getCapability(CapabilityEnergy.ENERGY, Direction.UP).map(handler -> {
					if(handler.canReceive()) {
						int received = handler.receiveEnergy(Math.min(capacity, 1000), false);
						energyStorage.removeEnergy(received);
						setChanged();
						return energyStorage.getEnergyStored()>0;
					}
					return true;
				}).orElse(true);
			}
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
	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side){
		if(cap==CapabilityEnergy.ENERGY&&side==Direction.DOWN) {
			return energy.cast();
		}
		if(cap==CapabilityEnergy.ENERGY&&side==null) {
			return energy.cast();
		}
		return super.getCapability(cap,side);
	}
	@Override
	public void setRemoved() {
		super.setRemoved();
		energy.invalidate();
	}
}
