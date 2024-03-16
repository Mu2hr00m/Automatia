package com.mushroom.automatia.item;

import java.util.List;

import javax.annotation.Nullable;

import com.mushroom.automatia.block.entity.EnergyCubeBE;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class ProbabilityData extends Item{

	public ProbabilityData(Properties properties) {
		super(properties);
	}
	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
		components.add(new TranslatableComponent("gui.text.proportion").append(": ").append(String.valueOf(stack.getOrCreateTag().getDouble("proportion"))).withStyle(ChatFormatting.RESET).withStyle(ChatFormatting.GRAY));
	}
}
