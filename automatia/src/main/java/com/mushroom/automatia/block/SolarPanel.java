package com.mushroom.automatia.block;

import javax.annotation.Nullable;

import com.mushroom.automatia.block.entity.SolarPanelBE;
import com.mushroom.automatia.energy.SolarPanelMenu;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;

public class SolarPanel extends BaseEntityBlock{

	public SolarPanel(Properties p_49224_) {
		super(p_49224_);
	}
	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult trace) {
		if(!level.isClientSide()) {
			BlockEntity panel = level.getBlockEntity(pos);
			if(panel instanceof SolarPanelBE) {
				MenuProvider menuProvider = new MenuProvider() {
					@Override
					public Component getDisplayName() {return new TextComponent("Solar Panel");}

					@Override
					public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player player) {
						return new SolarPanelMenu(i,pos,playerInventory,player);
					}
				};
				NetworkHooks.openGui((ServerPlayer)player, menuProvider, panel.getBlockPos());
			} else {return InteractionResult.FAIL;}
		}
		return InteractionResult.SUCCESS;
	}
	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type){
		if(level.isClientSide()) {return null;}
		return (lvl, pos, blockState, t) -> {
			if (t instanceof SolarPanelBE tile) {
				tile.tickServer(level, state);
			}
		};
	}
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new SolarPanelBE(pos,state);
	}
	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}
	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		return Block.box(0, 0, 0, 16, 2, 16);
	}
}
