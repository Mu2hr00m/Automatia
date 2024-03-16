package com.mushroom.automatia.minimus;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.mushroom.automatia.block.MinimusChambersExit;
import com.mushroom.automatia.world.dimension.ModDimensions;

import net.minecraft.commands.arguments.EntityAnchorArgument.Anchor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.ITeleporter;

public class ChambersSD extends net.minecraft.world.level.saveddata.SavedData{
	
	private final Map<ChunkPos,ChambersData> ChambersMap = new HashMap<>();
	
	@Nonnull
	private ChambersData getInternal(int index, Player player, Level level) {
		ChunkPos chunk = new ChunkPos(fetchIndex(index));
		return ChambersMap.computeIfAbsent(chunk, cp -> new ChambersData(index,player.getUUID(),level));
	}
	public ChambersData getChamber(int index, Player player, Level level) {
		return getInternal(index, player, level.getServer().getLevel(Level.OVERWORLD));
	}
	public BlockPos fetchIndex(int index) {
		return new BlockPos((int)(index%2000)*512,10,(int)(index/20000)*512);
	}
	public int newIndex() {
		return ChambersMap.size()+1;
	}
	public void setEntrance(BlockPos pos, Player player, int index, Level level) {
		ChambersData chamber = getInternal(index, player, level.getServer().getLevel(Level.OVERWORLD));
		chamber.setEntrance(pos);
		chamber.setDirection(level.getBlockState(pos).getValue(MinimusChambersExit.FACING));
	}
	public void TPPlayerIntoChamber(Level world, Player player, int index) {
		PlayerChamberHistory chamber_hist = player.getCapability(PlayerChamberHistProvider.PLAYER_CHAMBER_HIST).resolve().get();
		chamber_hist.setExit(player.getOnPos());
		chamber_hist.setDimension(player.level.dimension());
		chamber_hist.setIndex(index);
		ChambersData chamber = getInternal(index,player,world.getServer().getLevel(Level.OVERWORLD));
		player.changeDimension(world.getServer().getLevel(ModDimensions.MINIMUS_CHAMBERS_KEY), new ITeleporter() {
			@Override
			public Entity placeEntity(Entity entity, ServerLevel cur_world, ServerLevel dest_world, float yaw, Function<Boolean,Entity> repositionEntity) {
				entity = repositionEntity.apply(false);
				BlockPos pos = chamber.entrance();
				entity.teleportTo(pos.getX()+0.5,pos.getY()+0.126,pos.getZ()+0.5);
				if(chamber.Direction()==Direction.EAST) {entity.lookAt(Anchor.EYES, new Vec3(pos.getX()+3, pos.getY()+1.6, pos.getZ()+0.5));}
				else if(chamber.Direction()==Direction.SOUTH) {entity.lookAt(Anchor.EYES, new Vec3(pos.getX()+0.5, pos.getY()+1.6, pos.getZ()+3));}
				else if(chamber.Direction()==Direction.WEST) {entity.lookAt(Anchor.EYES, new Vec3(pos.getX()-3, pos.getY()+1.6, pos.getZ()+0.5));}
				else {entity.lookAt(Anchor.EYES, new Vec3(pos.getX()+0.5, pos.getY()+1.6, pos.getZ()-3));}
				return entity;
			}
		});
		chamber.generateWalls(world);
	}
	public void TPPlayerOutChamber(Level world, Player player) {
		PlayerChamberHistory chamber_hist = player.getCapability(PlayerChamberHistProvider.PLAYER_CHAMBER_HIST).resolve().get();
		player.changeDimension(world.getServer().getLevel(chamber_hist.getDimension()), new ITeleporter() {
			@Override
			public Entity placeEntity(Entity entity, ServerLevel cur_world, ServerLevel dest_world, float yaw, Function<Boolean,Entity> repositionEntity) {
				entity = repositionEntity.apply(false);
				BlockPos pos = chamber_hist.getExit();
				entity.teleportTo(pos.getX()+0.5,pos.getY()+1,pos.getZ()+0.5);
				return entity;
			}
		});
	}
	public ChambersSD() {
	}
	public ChambersSD(CompoundTag tag) {
		ListTag list = tag.getList("chambers", Tag.TAG_COMPOUND);
		for (Tag t:list) {
			CompoundTag chamberTag = (CompoundTag)t;
			ChambersData chamber = new ChambersData(chamberTag.getInt("index"),chamberTag.getUUID("owner"),new BlockPos(chamberTag.getInt("ex"),chamberTag.getInt("ey"),chamberTag.getInt("ez")));
			chamber.setSize(chamberTag.getInt("size"));
			int intDirection = chamberTag.getInt("f");
			if(intDirection==1) {chamber.setDirection(Direction.EAST);}
			else if(intDirection==2) {chamber.setDirection(Direction.SOUTH);}
			else if(intDirection==3) {chamber.setDirection(Direction.WEST);}
			else {chamber.setDirection(Direction.NORTH);}
			ChunkPos pos = new ChunkPos(chamberTag.getInt("x"),chamberTag.getInt("z"));
			ChambersMap.put(pos, chamber);
		}
	}
	@Nonnull
	public static ChambersSD get(Level level) {
		if (level.isClientSide) {throw new RuntimeException("ChambersSD shouldn't be accessed from clientside!");}
		DimensionDataStorage storage = level.getServer().getLevel(ServerLevel.OVERWORLD).getDataStorage();
		return storage.computeIfAbsent(ChambersSD::new, ChambersSD::new, "chambers_sd");
	}
		
	public CompoundTag save(CompoundTag tag) {
		ListTag list = new ListTag();
		ChambersMap.forEach((key, value) -> {
			CompoundTag chamber = new CompoundTag();
			chamber.putInt("x", key.x);
			chamber.putInt("z", key.z);
			chamber.putInt("index", value.index());
			chamber.putUUID("owner", value.owner());
			chamber.putInt("ex", value.entrance().getX());
			chamber.putInt("ey", value.entrance().getY());
			chamber.putInt("ez", value.entrance().getZ());
			chamber.putInt("size", value.size());
			Direction g = value.Direction();
			if(g==Direction.EAST) {chamber.putInt("f", 1);}
			if(g==Direction.SOUTH) {chamber.putInt("f", 2);}
			if(g==Direction.WEST) {chamber.putInt("f", 3);}
			else {chamber.putInt("f", 0);}
			list.add(chamber);
		});
		tag.put("chambers", list);
		return tag;
	}
}
