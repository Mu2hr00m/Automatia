package com.mushroom.automatia.item;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class MinimusChambersItem extends BlockItem{
	public MinimusChambersItem(Block block,Properties p_41383_) {
		super(block, p_41383_);
	}
	@Override
	public Component getName(ItemStack stack) {
		return new TranslatableComponent(this.getDescriptionId(stack)).withStyle(ChatFormatting.LIGHT_PURPLE);
	}
	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
		if(level!=null) {
			if(stack.getOrCreateTag().getCompound("BlockEntityTag").hasUUID("owner")) {
				components.add(new TranslatableComponent("block.automatia.minimus_chambers.owner").append(level.getPlayerByUUID(stack.getOrCreateTag().getCompound("BlockEntityTag").getUUID("owner")).getName()).withStyle(ChatFormatting.RESET).withStyle(ChatFormatting.BLUE));
			} else {
				components.add(new TranslatableComponent("block.automatia.minimus_chambers.unbound").withStyle(ChatFormatting.RESET).withStyle(ChatFormatting.BLUE));
			}
		}
	}
}
