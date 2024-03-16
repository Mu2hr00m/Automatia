package com.mushroom.automatia.block;

import javax.annotation.Nullable;

import com.mushroom.automatia.minimus.ChambersData;
import com.mushroom.automatia.minimus.ChambersSD;
import com.mushroom.automatia.minimus.PlayerChamberHistProvider;
import com.mushroom.automatia.minimus.PlayerChamberHistory;
import com.mushroom.automatia.world.dimension.ModDimensions;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MinimusChambersExit extends Block{
	
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static VoxelShape SHAPE = Block.box(1, 0, 1, 15, 2, 15);
	
	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}
	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}
	public MinimusChambersExit(Properties props) {
		super(props);
	}
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());}
	@Override
	public BlockState rotate(BlockState state, Rotation rot) {return state.setValue(FACING, rot.rotate(state.getValue(FACING)));}
	@SuppressWarnings("deprecation")
	@Override
	public BlockState mirror(BlockState state, Mirror mir) {return state.rotate(mir.getRotation(state.getValue(FACING)));}
	protected void createBlockStateDefinition(StateDefinition.Builder<Block,BlockState> builder) {builder.add(FACING);}
	
	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack item) {
		if(!world.isClientSide()) {
			if(entity instanceof Player) {
				if(world.dimension().equals(ModDimensions.MINIMUS_CHAMBERS_KEY)) {
					Level overworld = world.getServer().getLevel(Level.OVERWORLD);
					PlayerChamberHistory chamberhist = entity.getCapability(PlayerChamberHistProvider.PLAYER_CHAMBER_HIST).resolve().get();
					if(chamberhist.getIndex()!=-1) {
						ChambersData chamber = ChambersSD.get(overworld).getChamber(chamberhist.getIndex(), (Player)entity, world);
						chamber.setEntrance(pos);
						chamber.setDirection(state.getValue(FACING));
						ChambersSD.get(overworld).setDirty();
					}
				}
			}
		}
	}
	@SuppressWarnings("deprecation")
	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player,
			InteractionHand hand, BlockHitResult hit_result) {
		if(!world.isClientSide) {
			if(world.dimension().equals(ModDimensions.MINIMUS_CHAMBERS_KEY)) {
				player.level.getProfiler().push("minimus_chambers");
				ChambersSD.get(world.getServer().getLevel(Level.OVERWORLD)).TPPlayerOutChamber(world, player);
				player.level.getProfiler().pop();
				return InteractionResult.SUCCESS;
			}
			return InteractionResult.FAIL;
		}
		return super.use(state, world, pos, player, hand, hit_result);
	}
}
