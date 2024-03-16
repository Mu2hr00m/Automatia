package com.mushroom.automatia.world.dimension;

import com.mushroom.automatia.automatia;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;

public class ModDimensions {
	public static final ResourceKey<Level> MINIMUS_CHAMBERS_KEY = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(automatia.MOD_ID, "minimus_chambers"));
	public static final ResourceKey<DimensionType> MINIMUS_CHAMBERS_TYPE = ResourceKey.create(Registry.DIMENSION_TYPE_REGISTRY, MINIMUS_CHAMBERS_KEY.getRegistryName());
	
	public static void register() {
		
	}
}
