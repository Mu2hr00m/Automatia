package com.mushroom.automatia.init;

import com.mushroom.automatia.automatia;
import com.mushroom.automatia.block.entity.ArtificialVentBE;
import com.mushroom.automatia.block.entity.EnergyCubeBE;
import com.mushroom.automatia.block.entity.EnergyLaserEmitterBE;
import com.mushroom.automatia.block.entity.GeyserVentBE;
import com.mushroom.automatia.block.entity.MinimusChambersEntity;
import com.mushroom.automatia.block.entity.RandomGeneratorBE;
import com.mushroom.automatia.block.entity.SolarPanelBE;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities{
	
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, automatia.MOD_ID);
	
	public static final RegistryObject<BlockEntityType<MinimusChambersEntity>> MINIMUS_CHAMBERS_ENTITY = BLOCK_ENTITIES.register("minimus_chambers_entity",() -> BlockEntityType.Builder.of(MinimusChambersEntity::new, BlockInit.MINIMUS_CHAMBERS.get()).build(null));
	public static final RegistryObject<BlockEntityType<SolarPanelBE>> SOLAR_PANEL_ENTITY = BLOCK_ENTITIES.register("solar_panel",() -> BlockEntityType.Builder.of(SolarPanelBE::new, BlockInit.SOLAR_PANEL.get()).build(null));
	public static final RegistryObject<BlockEntityType<EnergyCubeBE>> ENERGY_CUBE_ENTITY = BLOCK_ENTITIES.register("energy_cube",() -> BlockEntityType.Builder.of(EnergyCubeBE::new, BlockInit.ENERGY_CUBE.get()).build(null));
	public static final RegistryObject<BlockEntityType<GeyserVentBE>> GEYSER_VENT_ENTITY = BLOCK_ENTITIES.register("sulfurstone_vent",() -> BlockEntityType.Builder.of(GeyserVentBE::new, BlockInit.SULFURSTONE_VENT.get()).build(null));
	public static final RegistryObject<BlockEntityType<ArtificialVentBE>> ARTIFICIAL_VENT_ENTITY = BLOCK_ENTITIES.register("artificial_vent",() -> BlockEntityType.Builder.of(ArtificialVentBE::new, BlockInit.ARTIFICIAL_VENT.get()).build(null));
	public static final RegistryObject<BlockEntityType<EnergyLaserEmitterBE>> ENERGY_LASER_EMITTER_ENTITY = BLOCK_ENTITIES.register("energy_laser_emitter",() -> BlockEntityType.Builder.of(EnergyLaserEmitterBE::new, BlockInit.ENERGY_LASER_EMITTER.get()).build(null));
	public static final RegistryObject<BlockEntityType<RandomGeneratorBE>> RANDOM_GENERATOR_ENTITY = BLOCK_ENTITIES.register("random_generator",() -> BlockEntityType.Builder.of(RandomGeneratorBE::new, BlockInit.RANDOM_GENERATOR.get()).build(null));

	
	public static void register(IEventBus eventBus) {
		BLOCK_ENTITIES.register(eventBus);
	}
}
