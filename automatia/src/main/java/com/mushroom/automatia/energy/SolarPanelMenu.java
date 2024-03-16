package com.mushroom.automatia.energy;

import com.mushroom.automatia.block.entity.SolarPanelBE;
import com.mushroom.automatia.init.BlockInit;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class SolarPanelMenu extends AbstractContainerMenu{
	private BlockEntity be;
	private Player player;
	private IItemHandler playerInv;
	
	public SolarPanelMenu(int windowId,BlockPos pos, Inventory playerInventory,Player player) {
		super(BlockInit.SOLAR_PANEL_MENU.get(),windowId);
		be = player.getCommandSenderWorld().getBlockEntity(pos);
		this.player = player;
		this.playerInv = new InvWrapper(playerInventory);
		layoutPlayerInventorySlots(10,70);
		trackPower();
	}
	private void trackPower() {
		addDataSlot(new DataSlot() {
			@Override
			public int get() {return getEnergy()&0xffff;}
			@Override
			public void set(int value) {
				be.getCapability(CapabilityEnergy.ENERGY).ifPresent(h -> {
					int energyStored = h.getEnergyStored() &0xffff0000;
					((CustomEnergyStorage)h).setEnergy(energyStored+(value&0xffff));
				});
			}
		});
		addDataSlot(new DataSlot() {
			@Override
			public int get() {return (getEnergy()>>16)&0xffff;}
			@Override
			public void set(int value) {
				be.getCapability(CapabilityEnergy.ENERGY).ifPresent(h -> {
					int energyStored = h.getEnergyStored() &0x0000ffff;
					((CustomEnergyStorage)h).setEnergy(energyStored|(value<<16));
				});
			}
		});
		addDataSlot(new DataSlot() {
			@Override
			public int get() {return getEnergyGeneration();}
			@Override
			public void set(int value) {
				((SolarPanelBE)be).energy_generation = value;
			}
		});
	}
	public int getEnergy() {
		return be.getCapability(CapabilityEnergy.ENERGY).map(IEnergyStorage::getEnergyStored).orElse(0);
	}
	public int getEnergyGeneration() {
		return ((SolarPanelBE)be).energy_generation;
	}
	public int getDayTime() {
		return (int)be.getLevel().dayTime()%24000;
	}
	@Override
	public boolean stillValid(Player player) {
		return stillValid(ContainerLevelAccess.create(be.getLevel(),be.getBlockPos()),player,BlockInit.SOLAR_PANEL.get());
	}
	private int addSlotRange(IItemHandler handler, int index, int x, int y, int w, int dx) {
		for(int j = 0;j<w;j++) {
			addSlot(new SlotItemHandler(handler,index,x,y));
			x+=dx;
			index++;
		}
		return index;
	}
	private int addSlotBox(IItemHandler handler, int index, int x, int y, int w, int dx, int h, int dy) {
		for(int j = 0;j<h;j++) {
			index = addSlotRange(handler,index,x,y,w,dx);
			y+=dy;
		}
		return index;
	}
	private void layoutPlayerInventorySlots(int left, int top) {
		addSlotBox(playerInv,9,left,top,9,18,3,18);
		top+=58;
		addSlotRange(playerInv,0,left,top,9,18);
	}
}
