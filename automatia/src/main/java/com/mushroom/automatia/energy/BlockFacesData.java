package com.mushroom.automatia.energy;

import com.mushroom.automatia.block.entity.EnergyCubeBE;

import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.level.block.entity.BlockEntity;

public class BlockFacesData extends SimpleContainerData{
	private final EnergyCubeBE cube;

	public BlockFacesData(EnergyCubeBE be, int count) {
		super(count);
		this.cube = be;
	}

	@Override
	public int get(int key) {
		return this.cube.compileTransfers();
	}

	@Override
	public void set(int key, int value) {
		this.cube.decompileTransfers((short)value);
		this.cube.setChanged();
	}
}
