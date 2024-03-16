package com.mushroom.automatia.init;

import com.google.common.base.Supplier;
import com.mushroom.automatia.automatia;
import com.mushroom.automatia.item.MaterialiteArmor;
import com.mushroom.automatia.item.ProbabilityData;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemInit {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, automatia.MOD_ID);
	
	public static final RegistryObject<Item> MATERIALITE_INGOT = register("materialite_ingot",() -> new Item(new Item.Properties().tab(automatia.AUTOMATIA_GENERAL)));
	public static final RegistryObject<Item> CIRCUIT = register("circuit",() -> new Item(new Item.Properties().tab(automatia.AUTOMATIA_GENERAL)));
	public static final RegistryObject<Item> MICROCONTROLLER = register("microcontroller",() -> new Item(new Item.Properties().tab(automatia.AUTOMATIA_GENERAL)));
	public static final RegistryObject<Item> COMPUTER = register("computer",() -> new Item(new Item.Properties().tab(automatia.AUTOMATIA_GENERAL)));
	public static final RegistryObject<Item> GRAPHENE = register("graphene",() -> new Item(new Item.Properties().tab(automatia.AUTOMATIA_GENERAL)));
	public static final RegistryObject<ProbabilityData> PROBABILITY_DATA = register("probability_data",() -> new ProbabilityData(new Item.Properties().tab(automatia.AUTOMATIA_GENERAL)));
	
	public static final RegistryObject<MaterialiteArmor> MATERILAITE_HELMET = register("materialite_helmet", () -> new MaterialiteArmor(EquipmentSlot.HEAD, new Item.Properties().tab(automatia.AUTOMATIA_GENERAL)));
	public static final RegistryObject<MaterialiteArmor> MATERILAITE_CHESTPLATE = register("materialite_chestplate", () -> new MaterialiteArmor(EquipmentSlot.CHEST, new Item.Properties().tab(automatia.AUTOMATIA_GENERAL)));
	public static final RegistryObject<MaterialiteArmor> MATERILAITE_LEGGINGS = register("materialite_leggings", () -> new MaterialiteArmor(EquipmentSlot.LEGS, new Item.Properties().tab(automatia.AUTOMATIA_GENERAL)));
	public static final RegistryObject<MaterialiteArmor> MATERILAITE_BOOTS = register("materialite_boots", () -> new MaterialiteArmor(EquipmentSlot.FEET, new Item.Properties().tab(automatia.AUTOMATIA_GENERAL)));
	
	private static <T extends Item> RegistryObject<T> register(final String name, final Supplier<T> item){
		return ITEMS.register(name,item);
	}
}
