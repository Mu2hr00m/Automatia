package com.mushroom.automatia.init;

import com.mushroom.automatia.automatia;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;

public class ModTags {
	public static class Blocks{
		public static final TagKey<Block> SULFURSTONE = tag("sulfurstone");
		public static final TagKey<Block> GEYSER_SOURCE = tag("geyser_source");
		public static final TagKey<Block> IGNEOUS_FORMER_SOURCE = tag("igneous_former_source");
		
		private static TagKey<Block> tag(String name){
			return BlockTags.create(new ResourceLocation(automatia.MOD_ID,name));
		}
		private static TagKey<Block> forgeTag(String name){
			return BlockTags.create(new ResourceLocation("forge",name));
		}
	}
}
