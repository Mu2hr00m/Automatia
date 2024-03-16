package com.mushroom.automatia.item;

import java.util.List;

import javax.annotation.Nullable;

import com.mushroom.automatia.block.entity.EnergyCubeBE;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class EnergyCubeItem extends BlockItem{

	public EnergyCubeItem(Block block,Properties p_41383_) {
		super(block, p_41383_);
	}
	@Override
	public Component getName(ItemStack stack) {
		return new TranslatableComponent(this.getDescriptionId(stack)).withStyle(ChatFormatting.LIGHT_PURPLE);
	}
	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
		components.add(new TranslatableComponent("gui.text.energy").append(": ").append(String.valueOf(stack.getOrCreateTag().getCompound("BlockEntityTag").getInt("energy"))).append(new TextComponent("/")).append(String.valueOf(EnergyCubeBE.maxPower)).withStyle(ChatFormatting.RESET).withStyle(ChatFormatting.BLUE));
		if(flag.isAdvanced()) {
			components.add(new TextComponent("Transfers: ").append(String.valueOf(stack.getOrCreateTag().getCompound("BlockEntityTag").getShort("transfers"))).withStyle(ChatFormatting.RESET).withStyle(ChatFormatting.DARK_GRAY));
		}
	}
}
