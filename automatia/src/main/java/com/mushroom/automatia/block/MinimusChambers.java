package com.mushroom.automatia.block;

import java.util.Set;
import java.util.UUID;

import javax.annotation.Nullable;

import com.mushroom.automatia.block.entity.MinimusChambersEntity;
import com.mushroom.automatia.chambers.PlayerChambers;
import com.mushroom.automatia.chambers.PlayerChambersProvider;
import com.mushroom.automatia.minimus.ChambersData;
import com.mushroom.automatia.init.BlockInit;
import com.mushroom.automatia.minimus.ChambersSD;
import com.mushroom.automatia.world.dimension.ModDimensions;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MinimusChambers extends BaseEntityBlock{
	
	private Set<String> nbt_tags = Set.of("owner","index");
	private ChambersData chamber = new ChambersData(-1,UUID.randomUUID(),new BlockPos(-512,5,-512));
	
	@Override
	public RenderShape getRenderShape(BlockState p_49232_) {
		return RenderShape.MODEL;
	}
	@Override
	public VoxelShape getVisualShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		return Shapes.empty();
	}
	public MinimusChambers(Properties properties) {
		super(properties);
	}
	@Override
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
		if(!world.isClientSide()) {
			ItemStack item = new ItemStack(BlockInit.MINIMUS_CHAMBERS.get(), 1);
			BlockEntity blockentity = world.getBlockEntity(pos);
			if (!(blockentity instanceof MinimusChambersEntity)) {throw new IllegalStateException("Minimus Chambers missing its BlockEntity!");}
			MinimusChambersEntity chamberentity = (MinimusChambersEntity)blockentity;
			chamberentity.saveToItem(item);
			ItemEntity itementity = new ItemEntity(world, pos.getX()+0.5, pos.getY()+0.25, pos.getZ()+0.5, item);
			itementity.setDefaultPickUpDelay();
			world.addFreshEntity(itementity);
		}
	}
	@Nullable
	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack item) {
		if(!world.isClientSide()) {
			CompoundTag tag = item.getOrCreateTag().getCompound("BlockEntityData");
			if(entity instanceof Player) {
				PlayerChambers playerchamber = entity.getCapability(PlayerChambersProvider.PLAYER_CHAMBERS).resolve().get();
				if(playerchamber.getIndex()==-1&&!tag.contains("index")) {
					playerchamber.setIndex(ChambersSD.get(world.getServer().getLevel(Level.OVERWORLD)).newIndex());
					ChambersSD.get(world.getServer().getLevel(Level.OVERWORLD)).setDirty();
					tag.putInt("index", playerchamber.getIndex());
				}
				else if(!tag.contains("index")) {tag.putInt("index", playerchamber.getIndex());}
			}
			chamber = ChambersSD.get(world.getServer().getLevel(Level.OVERWORLD)).getChamber(tag.getInt("index"), (Player) entity, world);
			getChambersEntity(world,pos).setBlockData(chamber.index(),chamber.owner());
			return;
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player,
			InteractionHand hand, BlockHitResult hit_result) {
		if(!world.isClientSide) {
			if(world.dimension().equals(Level.OVERWORLD)||world.dimension().equals(Level.NETHER)||world.dimension().equals(Level.END)) {
				//player.level.getProfiler().push("minimus_chambers");
				ChambersSD.get(world.getServer().getLevel(Level.OVERWORLD)).TPPlayerIntoChamber(world, player, this.getChambersEntity(world, pos).index);
				//player.level.getProfiler().pop();
				return InteractionResult.SUCCESS;
			}
			return InteractionResult.FAIL;
		}
		return super.use(state, world, pos, player, hand, hit_result);
	}
	
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		MinimusChambersEntity entity = new MinimusChambersEntity(pos, state);
		
		return entity;
	};
	@SuppressWarnings("unused")
	private MinimusChambersEntity getChambersEntity(Level world, BlockPos pos) {
		BlockEntity blockentity = world.getBlockEntity(pos);
		if (!(blockentity instanceof MinimusChambersEntity)) {throw new IllegalStateException("Minimus Chambers missing its BlockEntity!");}
		return (MinimusChambersEntity)blockentity;
	}
}
