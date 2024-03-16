package com.mushroom.automatia.init;

import java.util.function.Function;

import com.google.common.base.Supplier;
import com.mushroom.automatia.automatia;
import com.mushroom.automatia.block.AnalogDiode;
import com.mushroom.automatia.block.EnergyCube;
import com.mushroom.automatia.block.EnergyLaserEmitter;
import com.mushroom.automatia.block.GeyserVent;
import com.mushroom.automatia.block.IgneousFormer;
import com.mushroom.automatia.block.MinimusChambers;
import com.mushroom.automatia.block.MinimusChambersExit;
import com.mushroom.automatia.block.MinimusChambersReset;
import com.mushroom.automatia.block.RandomGenerator;
import com.mushroom.automatia.block.SolarPanel;
import com.mushroom.automatia.block.Transfungus;
import com.mushroom.automatia.block.TransfungusMycelium;
import com.mushroom.automatia.energy.EnergyCubeMenu;
import com.mushroom.automatia.energy.SolarPanelMenu;
import com.mushroom.automatia.item.EnergyCubeItem;
import com.mushroom.automatia.item.MinimusChambersItem;

import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockInit {
	public static final DeferredRegister<Item> ITEMS = ItemInit.ITEMS;
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, automatia.MOD_ID);
	public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, automatia.MOD_ID);
	
	//specialty blocks
	public static final RegistryObject<MinimusChambers> MINIMUS_CHAMBERS = registerBlock("minimus_chambers",() -> new MinimusChambers(BlockBehaviour.Properties.of(Material.METAL,MaterialColor.COLOR_LIGHT_GRAY).strength(2.0f,1000.0f).sound(SoundType.METAL).noOcclusion()),object -> () -> new MinimusChambersItem(object.get(), new Item.Properties().tab(automatia.AUTOMATIA_GENERAL).stacksTo(1).fireResistant()));
	public static final RegistryObject<Block> MINIMUS_RESET = registerBlock("minimus_chambers_reset",() -> new MinimusChambersReset(BlockBehaviour.Properties.of(Material.METAL,MaterialColor.COLOR_LIGHT_GRAY).requiresCorrectToolForDrops().strength(2.0f).sound(SoundType.METAL)),object -> () -> new BlockItem(object.get(), new Item.Properties().tab(automatia.AUTOMATIA_GENERAL)));
	public static final RegistryObject<Block> MINIMUS_EXIT = registerBlock("minimus_chambers_exit",() -> new MinimusChambersExit(BlockBehaviour.Properties.of(Material.METAL,MaterialColor.COLOR_LIGHT_GRAY).requiresCorrectToolForDrops().strength(2.0f).sound(SoundType.METAL).noOcclusion()),object -> () -> new BlockItem(object.get(), new Item.Properties().tab(automatia.AUTOMATIA_GENERAL)));
	public static final RegistryObject<Block> SOLAR_PANEL = registerBlock("solar_panel",() -> new SolarPanel(BlockBehaviour.Properties.of(Material.METAL,MaterialColor.COLOR_CYAN).requiresCorrectToolForDrops().strength(2.0f).sound(SoundType.METAL).noOcclusion()),object -> () -> new BlockItem(object.get(), new Item.Properties().tab(automatia.AUTOMATIA_GENERAL)));
	public static final RegistryObject<MenuType<SolarPanelMenu>> SOLAR_PANEL_MENU = CONTAINERS.register("solar_panel", () -> IForgeMenuType.create((windowId, inv, data) -> new SolarPanelMenu(windowId,data.readBlockPos(),inv,inv.player)));
	public static final RegistryObject<EnergyCube> ENERGY_CUBE = registerBlock("energy_cube",() -> new EnergyCube(BlockBehaviour.Properties.of(Material.METAL,MaterialColor.COLOR_LIGHT_GRAY).requiresCorrectToolForDrops().strength(2.0f).sound(SoundType.METAL).noOcclusion()),object -> () -> new EnergyCubeItem(object.get(), new Item.Properties().tab(automatia.AUTOMATIA_GENERAL).stacksTo(1)));
	public static final RegistryObject<MenuType<EnergyCubeMenu>> ENERGY_CUBE_MENU = CONTAINERS.register("energy_cube", () -> IForgeMenuType.create((windowId, inv, data) -> new EnergyCubeMenu(windowId,data.readBlockPos(),inv,inv.player)));
	public static final RegistryObject<EnergyLaserEmitter> ENERGY_LASER_EMITTER = registerBlock("energy_laser_emitter",() -> new EnergyLaserEmitter(BlockBehaviour.Properties.of(Material.METAL,MaterialColor.COLOR_LIGHT_GRAY).requiresCorrectToolForDrops().strength(2.0f).sound(SoundType.METAL).noOcclusion()),object -> () -> new BlockItem(object.get(), new Item.Properties().tab(automatia.AUTOMATIA_GENERAL).stacksTo(1)));
	public static final RegistryObject<AnalogDiode> ANALOG_DIODE = registerBlock("analog_diode",() -> new AnalogDiode(BlockBehaviour.Properties.of(Material.STONE,MaterialColor.COLOR_LIGHT_GRAY).strength(1.0f).sound(SoundType.STONE)),object -> () -> new BlockItem(object.get(), new Item.Properties().tab(automatia.AUTOMATIA_GENERAL)));
	public static final RegistryObject<RandomGenerator> RANDOM_GENERATOR = registerBlock("random_generator",() -> new RandomGenerator(BlockBehaviour.Properties.of(Material.STONE,MaterialColor.COLOR_LIGHT_GRAY).strength(1.0f).sound(SoundType.STONE)),object -> () -> new BlockItem(object.get(), new Item.Properties().tab(automatia.AUTOMATIA_GENERAL)));
	
	//generic blocks
	public static final RegistryObject<Block> MATERIALITE_BLOCK = registerBlock("materialite_block",() -> new Block(BlockBehaviour.Properties.of(Material.METAL,MaterialColor.COLOR_LIGHT_GRAY).requiresCorrectToolForDrops().strength(10.0f).sound(SoundType.METAL)),object -> () -> new BlockItem(object.get(), new Item.Properties().tab(automatia.AUTOMATIA_GENERAL)));
	public static final RegistryObject<Block> MINIMUS_INDESTRUCTIBLE_WALL = registerBlock("minimus_chambers_indestructible_wall",() -> new Block(BlockBehaviour.Properties.of(Material.BARRIER,MaterialColor.COLOR_LIGHT_GRAY).strength(10000.0f).destroyTime(-5.0f).sound(SoundType.DEEPSLATE_BRICKS).noDrops()),object -> () -> new BlockItem(object.get(), new Item.Properties().tab(automatia.AUTOMATIA_GENERAL)));
	public static final RegistryObject<Block> MINIMUS_WALL = registerBlock("minimus_chambers_wall",() -> new Block(BlockBehaviour.Properties.of(Material.METAL,MaterialColor.COLOR_LIGHT_GRAY).requiresCorrectToolForDrops().strength(10.0f).sound(SoundType.DEEPSLATE_BRICKS)),object -> () -> new BlockItem(object.get(), new Item.Properties().tab(automatia.AUTOMATIA_GENERAL)));
	public static final RegistryObject<Block> DIAMOND_TRANSFUNGUS = registerBlock("diamond_transfungus",() -> new Transfungus(BlockBehaviour.Properties.of(Material.STONE,MaterialColor.DIAMOND).strength(1.0f).sound(SoundType.TUFF).noOcclusion().noCollission()),object -> () -> new BlockItem(object.get(), new Item.Properties().tab(automatia.AUTOMATIA_GENERAL)));
	public static final RegistryObject<Block> EMERALD_TRANSFUNGUS = registerBlock("emerald_transfungus",() -> new Transfungus(BlockBehaviour.Properties.of(Material.STONE,MaterialColor.EMERALD).strength(1.0f).sound(SoundType.TUFF).noOcclusion().noCollission()),object -> () -> new BlockItem(object.get(), new Item.Properties().tab(automatia.AUTOMATIA_GENERAL)));
	public static final RegistryObject<Block> REDSTONE_TRANSFUNGUS = registerBlock("redstone_transfungus",() -> new Transfungus(BlockBehaviour.Properties.of(Material.STONE,MaterialColor.COLOR_RED).strength(1.0f).sound(SoundType.TUFF).noOcclusion().noCollission()),object -> () -> new BlockItem(object.get(), new Item.Properties().tab(automatia.AUTOMATIA_GENERAL)));
	public static final RegistryObject<Block> LAPIS_TRANSFUNGUS = registerBlock("lapis_transfungus",() -> new Transfungus(BlockBehaviour.Properties.of(Material.STONE,MaterialColor.LAPIS).strength(1.0f).sound(SoundType.TUFF).noOcclusion().noCollission()),object -> () -> new BlockItem(object.get(), new Item.Properties().tab(automatia.AUTOMATIA_GENERAL)));
	public static final RegistryObject<Block> SULFURSTONE = registerBlock("sulfurstone",() -> new Block(BlockBehaviour.Properties.of(Material.STONE,MaterialColor.TERRACOTTA_YELLOW).requiresCorrectToolForDrops().strength(3.0f).sound(SoundType.NETHERRACK)),object -> () -> new BlockItem(object.get(), new Item.Properties().tab(automatia.AUTOMATIA_GENERAL)));
	public static final RegistryObject<Block> SULFURSTONE_STAIRS = registerBlock("sulfurstone_stairs",() -> new StairBlock(SULFURSTONE.get().defaultBlockState(),BlockBehaviour.Properties.of(Material.STONE,MaterialColor.TERRACOTTA_YELLOW).requiresCorrectToolForDrops().strength(3.0f).sound(SoundType.NETHERRACK)),object -> () -> new BlockItem(object.get(), new Item.Properties().tab(automatia.AUTOMATIA_GENERAL)));
	public static final RegistryObject<Block> SULFURSTONE_SLAB = registerBlock("sulfurstone_slab",() -> new SlabBlock(BlockBehaviour.Properties.of(Material.STONE,MaterialColor.TERRACOTTA_YELLOW).requiresCorrectToolForDrops().strength(3.0f).sound(SoundType.NETHERRACK)),object -> () -> new BlockItem(object.get(), new Item.Properties().tab(automatia.AUTOMATIA_GENERAL)));
	public static final RegistryObject<Block> SULFURSTONE_VENT = registerBlock("sulfurstone_vent",() -> new GeyserVent(BlockBehaviour.Properties.of(Material.STONE,MaterialColor.TERRACOTTA_YELLOW).requiresCorrectToolForDrops().strength(4.0f).sound(SoundType.NETHERRACK)),object -> () -> new BlockItem(object.get(), new Item.Properties().tab(automatia.AUTOMATIA_GENERAL)));
	public static final RegistryObject<Block> ARTIFICIAL_VENT = registerBlock("artificial_vent",() -> new GeyserVent(BlockBehaviour.Properties.of(Material.STONE,MaterialColor.TERRACOTTA_YELLOW).requiresCorrectToolForDrops().strength(4.0f).sound(SoundType.NETHERRACK)),object -> () -> new BlockItem(object.get(), new Item.Properties().tab(automatia.AUTOMATIA_GENERAL)));
	public static final RegistryObject<Block> IGNEOUS_FORMER = registerBlock("igneous_former",() -> new IgneousFormer(BlockBehaviour.Properties.of(Material.METAL,MaterialColor.COLOR_LIGHT_GRAY).requiresCorrectToolForDrops().strength(4.0f).sound(SoundType.DEEPSLATE_BRICKS).noOcclusion()),object -> () -> new BlockItem(object.get(), new Item.Properties().tab(automatia.AUTOMATIA_GENERAL)));
	
	
	//transfungus mycelium
	public static final RegistryObject<Block> DIAMOND_MYCELIUM = registerBlock("diamond_mycelium",() -> new TransfungusMycelium(BlockBehaviour.Properties.of(Material.STONE,MaterialColor.DIAMOND).strength(3.0f).sound(SoundType.GRASS).randomTicks(), Blocks.COAL_BLOCK, DIAMOND_TRANSFUNGUS.get()),object -> () -> new BlockItem(object.get(), new Item.Properties().tab(automatia.AUTOMATIA_GENERAL)));
	public static final RegistryObject<Block> EMERALD_MYCELIUM = registerBlock("emerald_mycelium",() -> new TransfungusMycelium(BlockBehaviour.Properties.of(Material.STONE,MaterialColor.EMERALD).strength(3.0f).sound(SoundType.GRASS).randomTicks(), Blocks.DIORITE, EMERALD_TRANSFUNGUS.get()),object -> () -> new BlockItem(object.get(), new Item.Properties().tab(automatia.AUTOMATIA_GENERAL)));
	public static final RegistryObject<Block> REDSTONE_MYCELIUM = registerBlock("redstone_mycelium",() -> new TransfungusMycelium(BlockBehaviour.Properties.of(Material.STONE,MaterialColor.COLOR_RED).strength(3.0f).sound(SoundType.GRASS).randomTicks(), Blocks.GRANITE, REDSTONE_TRANSFUNGUS.get()),object -> () -> new BlockItem(object.get(), new Item.Properties().tab(automatia.AUTOMATIA_GENERAL)));
	public static final RegistryObject<Block> LAPIS_MYCELIUM = registerBlock("lapis_mycelium",() -> new TransfungusMycelium(BlockBehaviour.Properties.of(Material.STONE,MaterialColor.LAPIS).strength(3.0f).sound(SoundType.GRASS).randomTicks(), Blocks.SMOOTH_STONE, LAPIS_TRANSFUNGUS.get()),object -> () -> new BlockItem(object.get(), new Item.Properties().tab(automatia.AUTOMATIA_GENERAL)));
	
	private static <T extends Block> RegistryObject<T> registerBlockNoItem(final String name, final Supplier<? extends T> block){
		return BLOCKS.register(name,block);
	}
	private static <T extends Block> RegistryObject<T> registerBlock(final String name, final Supplier<? extends T> block, Function<RegistryObject<T>, Supplier<? extends Item>> item){
		RegistryObject<T> obj = registerBlockNoItem(name, block);
		ITEMS.register(name,item.apply(obj));
		return obj;
	}
}
