package com.mushroom.automatia.minimus;

import java.util.UUID;
import java.util.stream.Stream;

import com.mushroom.automatia.init.BlockInit;
import com.mushroom.automatia.world.dimension.ModDimensions;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

public class ChambersData {
	private UUID owner;
	private int Index = -1;
	private int size = 3;
	private Direction facing = Direction.NORTH;
	private BlockPos Entrance = new BlockPos(0,2,0);
	
	public ChambersData(int index, UUID owner, Level level) {
		this.owner = owner;
		this.Index = index;
		this.Entrance = ChambersSD.get(level).fetchIndex(index);
	}
	public ChambersData(int index, UUID owner, BlockPos pos) {
		this.owner = owner;
		this.Index = index;
		this.Entrance = pos;
	}
	
	public ChambersData(int index) {this.Index = index;}
	public int index() {return this.Index;}
	public void setSize(int size) {this.size = size;}
	public int size() {return this.size;}
	public void setIndex(int index) {this.Index = index;}
	public UUID owner() {return this.owner;}
	public void setOwner(UUID owner) {this.owner = owner;}
	public BlockPos entrance() {return this.Entrance;}
	public void setEntrance(BlockPos pos) {this.Entrance = pos;}
	public Direction Direction() {return this.facing;}
	public void setDirection(Direction direction) {this.facing = direction;}
	
	public void generateWalls(Level level2) {
		Level level = level2.getServer().getLevel(ModDimensions.MINIMUS_CHAMBERS_KEY);
		BlockPos origin = ChambersSD.get(level.getServer().getLevel(Level.OVERWORLD)).fetchIndex(this.Index);
		BlockState Wall = BlockInit.MINIMUS_INDESTRUCTIBLE_WALL.get().defaultBlockState();
		origin = origin.offset(-3,-3,-3);
		//if(level.isLoaded(origin.offset(255, 255, 255))) {
			//Iterable<BlockPos> interior = BlockPos.betweenClosed(origin.offset(1,1,1), origin.offset(this.size, this.size, this.size));
			BoundingBox interior2 = new BoundingBox(origin.getX()+1, origin.getY()+1, origin.getZ()+1, origin.getX()+this.size, origin.getY()+this.size, origin.getZ()+this.size);
			Stream<BlockPos> box3 = BlockPos.betweenClosedStream(origin.offset(-2,-2,-2), origin.offset(this.size+3, this.size+3, this.size+3));
			box3.filter(level::isEmptyBlock).map(BlockPos::immutable).forEach(b -> {if(!interior2.isInside(new Vec3i(b.getX(), b.getY(), b.getZ()))){level.setBlock(b, BlockInit.MINIMUS_INDESTRUCTIBLE_WALL.get().defaultBlockState(), 7);}});
		//}
		int d = 0;
		int e = 0;
		while(d<=this.size) {
			e = 0;
			while(e<=this.size) {
				if(level.isEmptyBlock(origin.offset(d,0,e))) {
					level.setBlock(origin.offset(d,0,e), Wall, 0);
				}
				e++;
			}
			d++;
		}
	}
}
