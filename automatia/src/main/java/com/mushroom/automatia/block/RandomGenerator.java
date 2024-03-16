package com.mushroom.automatia.block;

import com.mushroom.automatia.block.entity.RandomGeneratorBE;
import com.mushroom.automatia.block.entity.SolarPanelBE;
import com.mushroom.automatia.energy.SolarPanelMenu;
import com.mushroom.automatia.init.ItemInit;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;

public class RandomGenerator extends BaseEntityBlock{
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	
	public RandomGenerator(Properties properties) {
		super(properties);
	}
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos otherPos, boolean something) {
		if (!level.isClientSide) {
			if(level.getBlockState(pos.above()).getBlock()==Blocks.SAND&&level.getBlockState(pos.relative(state.getValue(FACING).getClockWise()).above()).isAir()&&level.getBlockState(pos.relative(state.getValue(FACING).getCounterClockWise()).above()).isAir()) {
				RandomGeneratorBE be = (RandomGeneratorBE)level.getBlockEntity(pos);
				boolean placed = false;
				if(RANDOM.nextDouble()<=be.proportion) {
					level.setBlockAndUpdate(pos.relative(state.getValue(FACING).getCounterClockWise()).above(), level.getBlockState(pos.above()));
					placed = true;
				} else {
					level.setBlockAndUpdate(pos.relative(state.getValue(FACING).getClockWise()).above(), level.getBlockState(pos.above()));
					placed = true;
				}
				if(placed) {
					level.removeBlock(pos.above(), false);
				}
			}
		}
	}
	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult trace) {
		if(!level.isClientSide()) {
			if(player.isCrouching()) {
				ItemStack item = player.getItemInHand(hand);
				if(item.getItem()==Items.PAPER) {
					ItemStack data = ItemInit.PROBABILITY_DATA.get().getDefaultInstance();
					data.getOrCreateTag().putDouble("proportion", getSandProportion(state,level,pos));
					player.setItemInHand(hand, data);
					return InteractionResult.SUCCESS;
				}
				else if(item.getItem()==ItemInit.PROBABILITY_DATA.get()) {
					item.getOrCreateTag().putDouble("proportion", getSandProportion(state,level,pos));
					return InteractionResult.SUCCESS;
				}
			}
			else if(player.getItemInHand(hand).getItem()==ItemInit.PROBABILITY_DATA.get()) {
				RandomGeneratorBE be = (RandomGeneratorBE)level.getBlockEntity(pos);
				be.proportion = player.getItemInHand(hand).getOrCreateTag().contains("proportion") ? player.getItemInHand(hand).getTag().getDouble("proportion") : 0.5;
				return InteractionResult.SUCCESS;
			}
		}
		return InteractionResult.PASS;
	}
	public double getSandProportion(BlockState state, Level level, BlockPos pos) {
		double left = 1;
		while(true) {
			if(level.getBlockState(pos.relative(state.getValue(FACING).getCounterClockWise(), (int)left)).getBlock()==Blocks.SAND) {
				left++;
			} else {break;}
		}
		double right = 1;
		while(true) {
			if(level.getBlockState(pos.relative(state.getValue(FACING).getClockWise(), (int)right)).getBlock()==Blocks.SAND) {
				right++;
			} else {break;}
		}
		left--;
		right--;
		if(left==0&&right==0) {return 0.5;}
		return left/(left+right);
	}
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new RandomGeneratorBE(pos, state);
	}
	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());}
	@Override
	public void createBlockStateDefinition(Builder<Block,BlockState> builder) {
		builder.add(FACING);
	}
}
