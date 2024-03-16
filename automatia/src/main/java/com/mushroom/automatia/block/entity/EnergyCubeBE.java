package com.mushroom.automatia.block.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.mushroom.automatia.energy.CustomEnergyStorage;
import com.mushroom.automatia.energy.FakeEnergyStorage;
import com.mushroom.automatia.init.ModBlockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class EnergyCubeBE extends BlockEntity{
	//public static final short f = 0b1001010101010;
	public static int maxPower = 800000;
	public static int maxTransfer = 5000;
	private List<Direction> inputs = new ArrayList<Direction>(6);
	private List<Direction> outputs = new ArrayList<Direction>(6);
	private final CustomEnergyStorage energyStorage = createEnergy();
	private final LazyOptional<IEnergyStorage> energy = LazyOptional.of(() -> energyStorage);
	private final CustomEnergyStorage inputStorage = createInputEnergy();
	private final LazyOptional<IEnergyStorage> input = LazyOptional.of(() -> inputStorage);
	private final CustomEnergyStorage outputStorage = createOutputEnergy();
	private final LazyOptional<IEnergyStorage> output = LazyOptional.of(() -> outputStorage);
	public EnergyCubeBE(BlockPos pos, BlockState state) {
		super(ModBlockEntities.ENERGY_CUBE_ENTITY.get(), pos, state);
	}
	private CustomEnergyStorage createEnergy() {
		return new CustomEnergyStorage(maxPower,maxTransfer,maxTransfer) {
			@Override
			protected void onEnergyChanged() {setChanged();}
		};
	}
	private CustomEnergyStorage createInputEnergy() {
		return new CustomEnergyStorage(maxPower,maxTransfer,0) {
			@Override
			protected void onEnergyChanged() {energyStorage.addEnergy(inputStorage.getEnergyStored());inputStorage.setEnergy(0,false);}
		};
	}
	private CustomEnergyStorage createOutputEnergy() {
		return new CustomEnergyStorage(maxPower,0,maxTransfer) {
			@Override
			public int extractEnergy(int maxExtract, boolean simulate) {
				int rc = energyStorage.extractEnergy(maxExtract, simulate);
				if(rc>0&&!simulate) {
					onEnergyChanged();
				}
				return rc;
			}
			@Override
			protected void onEnergyChanged() {setChanged();}
		};
	}
	public void tickServer(Level level, BlockState state) {
		if(outputs.size()!=0) {
			pushPower();
		}
	}
	private void pushPower() {
		AtomicInteger capacity = new AtomicInteger(energyStorage.getEnergyStored());
		Direction d;
		for(int i=0;i<outputs.size();i++) {
			d = outputs.get(i);
			if(capacity.get()>0) {
				BlockEntity be = level.getBlockEntity(worldPosition.relative(d));
				if(be!=null) {
					be.getCapability(CapabilityEnergy.ENERGY, d.getOpposite()).map(handler -> {
						if(handler.canReceive()) {
							int received = handler.receiveEnergy(Math.min(capacity.get(), maxTransfer), false);
							energyStorage.removeEnergy(received);
							capacity.addAndGet(-received);
							setChanged();
							return energyStorage.getEnergyStored()>0;
						}
						return true;
					}).orElse(true);
				}
			}
		}
	}
	@Override
	public void load(CompoundTag tag) {
		if(tag.contains("energy")) {energyStorage.deserializeNBT(tag.get("energy"));}
		if(tag.contains("transfers")) {
			decompileTransfers(tag.getShort("transfers"));
		}
		else {
			decompileTransfers((short)2389);
		}
		super.load(tag);
	}
	@Override
	public void saveAdditional(CompoundTag tag) {
		tag.put("energy", energyStorage.serializeNBT());
		tag.putShort("transfers", compileTransfers());
	}
	@Override
	public void saveToItem(ItemStack stack) {
		CompoundTag tag = this.saveWithoutMetadata();
		tag.put("energy", energyStorage.serializeNBT());
		tag.putShort("tansfers", compileTransfers());
		BlockItem.setBlockEntityData(stack, this.getType(), tag);
	}
	public short compileTransfers() {
		short data = 0;
		if(inputs.contains(Direction.UP))     {data|=0b01;}
		if(outputs.contains(Direction.UP))    {data|=0b10;}
		if(inputs.contains(Direction.NORTH))  {data|=0b0100;}
		if(outputs.contains(Direction.NORTH)) {data|=0b1000;}
		if(inputs.contains(Direction.EAST))   {data|=0b010000;}
		if(outputs.contains(Direction.EAST))  {data|=0b100000;}
		if(inputs.contains(Direction.SOUTH))  {data|=0b01000000;}
		if(outputs.contains(Direction.SOUTH)) {data|=0b10000000;}
		if(inputs.contains(Direction.WEST))   {data|=0b0100000000;}
		if(outputs.contains(Direction.WEST))  {data|=0b1000000000;}
		if(inputs.contains(Direction.DOWN))   {data|=0b010000000000;}
		if(outputs.contains(Direction.DOWN))  {data|=0b100000000000;}
		return data;
	}
	public void decompileTransfers(short data) {
		inputs.clear();
		outputs.clear();
		//if(data==0) {decompileTransfers((short)4778);}
		if((data&0b1)==1) {inputs.add(Direction.UP);}
		if((data&0b10)>>1>=1) {outputs.add(Direction.UP);}
		if((data&0b100)>>2>=1) {inputs.add(Direction.NORTH);}
		if((data&0b1000)>>3>=1) {outputs.add(Direction.NORTH);}
		if((data&0b10000)>>4>=1) {inputs.add(Direction.EAST);}
		if((data&0b100000)>>5>=1) {outputs.add(Direction.EAST);}
		if((data&0b1000000)>>6>=1) {inputs.add(Direction.SOUTH);}
		if((data&0b10000000)>>7>=1) {outputs.add(Direction.SOUTH);}
		if((data&0b100000000)>>8>=1) {inputs.add(Direction.WEST);}
		if((data&0b1000000000)>>9>=1) {outputs.add(Direction.WEST);}
		if((data&0b10000000000)>>10>=1) {inputs.add(Direction.DOWN);}
		if((data&0b100000000000)>>11>=1) {outputs.add(Direction.DOWN);}
	}
	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side){
		if(cap==CapabilityEnergy.ENERGY&&side==null) {
			return energy.cast();
		}
		if(side==null) {
			return super.getCapability(cap, side);
		}
		if(cap==CapabilityEnergy.ENERGY&&outputs.contains(side)&&inputs.contains(side)) {
			return energy.cast();
		}
		if(cap==CapabilityEnergy.ENERGY&&outputs.contains(side)) {
			return output.cast();
		}
		if(cap==CapabilityEnergy.ENERGY&&inputs.contains(side)) {
			return input.cast();
		}
		return super.getCapability(cap,side);
	}
	@Override
	public void setRemoved() {
		super.setRemoved();
		energy.invalidate();
		input.invalidate();
		output.invalidate();
	}
}
