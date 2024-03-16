package com.mushroom.automatia.block.entity;

import java.util.UUID;

import javax.annotation.Nullable;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import com.mushroom.automatia.init.ModBlockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class MinimusChambersEntity extends BlockEntity{
	//private static final Logger LOGGER = LogUtils.getLogger();
	public int index;
	public UUID owner;
	
	public MinimusChambersEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.MINIMUS_CHAMBERS_ENTITY.get(),pos,state);
	}
	@Override
	public void saveToItem(ItemStack stack) {
		CompoundTag tag = this.saveWithoutMetadata();
		tag.putUUID("owner", this.getTileData().getUUID("owner"));
		tag.putInt("index", this.getTileData().getInt("index"));
		BlockItem.setBlockEntityData(stack, this.getType(), tag);
	}
	@Override
	public void load(CompoundTag compound) {
		super.load(compound);
		this.index = this.getTileData().getInt("index");
		this.owner = this.getTileData().getUUID("owner");
		this.setChanged();
		if(level!=null&&!level.isClientSide()) {
			this.level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
		}	
	}
	public void setBlockData(int index, UUID owner) {//this is only accessed by MinimusChambers
		CompoundTag tag = this.getTileData();
		tag.putUUID("owner", owner);
		tag.putInt("index", index);
		this.owner = owner;
		this.index = index;
		this.setChanged();
		this.level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
	}
}
