package com.mushroom.automatia.block;

import javax.annotation.Nullable;

import com.mushroom.automatia.minimus.ChambersData;
import com.mushroom.automatia.minimus.ChambersSD;
import com.mushroom.automatia.minimus.PlayerChamberHistProvider;
import com.mushroom.automatia.minimus.PlayerChamberHistory;
import com.mushroom.automatia.world.dimension.ModDimensions;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class MinimusChambersReset extends Block{
	public MinimusChambersReset(Properties props) {
		super(props);
	}
	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack item) {
		if(!world.isClientSide()) {
			if(entity instanceof Player) {
				if(world.dimension().equals(ModDimensions.MINIMUS_CHAMBERS_KEY)) {
					Level overworld = world.getServer().getLevel(Level.OVERWORLD);
					PlayerChamberHistory chamberhist = entity.getCapability(PlayerChamberHistProvider.PLAYER_CHAMBER_HIST).resolve().get();
					if(chamberhist.getIndex()!=-1) {
						ChambersData chamber = ChambersSD.get(overworld).getChamber(chamberhist.getIndex(), (Player)entity, world);
						chamber.setEntrance(ChambersSD.get(overworld).fetchIndex(chamber.index()));
						chamber.setDirection(Direction.NORTH);
						chamber.setSize(3);
						ChambersSD.get(overworld).setDirty();
					}
				}
			}
		}
	}
}
