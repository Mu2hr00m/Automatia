package com.mushroom.automatia.energy;

import com.mushroom.automatia.block.entity.EnergyCubeBE;
import com.mushroom.automatia.init.BlockInit;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import network.MachineFaceSettingsServerbound;
import network.PacketInit;

public class EnergyCubeMenu extends AbstractContainerMenu{
	private BlockEntity be;
	private Player player;
	private BlockPos pos;
	private IItemHandler playerInv;
	public ContainerData data;
	
	public EnergyCubeMenu(int windowId,BlockPos pos,Inventory playerInv,Player player) {
		this(windowId,pos,playerInv,player,new BlockFacesData((EnergyCubeBE)player.getLevel().getBlockEntity(pos),1));
	}
	
	public EnergyCubeMenu(int windowId,BlockPos pos, Inventory playerInventory,Player player,ContainerData data) {
		super(BlockInit.ENERGY_CUBE_MENU.get(),windowId);
		be = player.getCommandSenderWorld().getBlockEntity(pos);
		this.pos = pos;
		this.player = player;
		this.playerInv = new InvWrapper(playerInventory);
		this.data = data;
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
			public int get() {return ((EnergyCubeBE)be).compileTransfers();}
			@Override
			public void set(int value) {
				((EnergyCubeBE)be).decompileTransfers((short)value);
			}
		});
		addDataSlots(data);
	}
	public int getEnergy() {
		return be.getCapability(CapabilityEnergy.ENERGY).map(IEnergyStorage::getEnergyStored).orElse(0);
	}
	public short getSides() {
		return ((EnergyCubeBE)be).compileTransfers();
	}
	public byte getSide(Direction side) {
		short sides = getSides();
		byte data = 0;
		if(side==Direction.UP)    {data = (byte) (sides&0b0000000000000011);}
		if(side==Direction.NORTH) {data = (byte)((sides&0b0000000000001100)>>2);}
		if(side==Direction.EAST)  {data = (byte)((sides&0b0000000000110000)>>4);}
		if(side==Direction.SOUTH) {data = (byte)((sides&0b0000000011000000)>>6);}
		if(side==Direction.WEST)  {data = (byte)((sides&0b0000001100000000)>>8);}
		if(side==Direction.DOWN)  {data = (byte)((sides&0b0000110000000000)>>10);}
		return data;
	}
	public void setSides(short sides) {
		((EnergyCubeBE)be).decompileTransfers(sides);
		PacketInit.MAIN.sendToServer(new MachineFaceSettingsServerbound(pos,sides));
		//player.displayClientMessage(new TextComponent((sides&1)+""+(sides&2>>1)+""+(sides&4>>2)+""+(sides&8>>3)+""+(sides&16>>4)+""+(sides&32>>5)+""+(sides&64>>6)+""+(sides&128>>7)+""+(sides&256>>8)+""+(sides&512>>9)+""+(sides&1024>>10)+""+(sides&2048>>11)+""), false);
		
	}
	public void setSide(Direction side, byte value) {
		short sides = getSides();
		if(side==Direction.UP)    {sides&=0b1111111111111100;sides|=(value&0b0000000000000011);}
		if(side==Direction.NORTH) {sides&=0b1111111111110011;sides|=(value&0b0000000000000011)<<2;}
		if(side==Direction.EAST)  {sides&=0b1111111111001111;sides|=(value&0b0000000000000011)<<4;}
		if(side==Direction.SOUTH) {sides&=0b1111111100111111;sides|=(value&0b0000000000000011)<<6;}
		if(side==Direction.WEST)  {sides&=0b1111110011111111;sides|=(value&0b0000000000000011)<<8;}
		if(side==Direction.DOWN)  {sides&=0b1111001111111111;sides|=(value&0b0000000000000011)<<10;}
		setSides(sides);
	}
	@Override
	public boolean stillValid(Player player) {
		return stillValid(ContainerLevelAccess.create(be.getLevel(),be.getBlockPos()),player,BlockInit.ENERGY_CUBE.get());
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
