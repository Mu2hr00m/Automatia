package com.mushroom.automatia.item;

import java.util.List;
import java.util.function.Consumer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.mushroom.automatia.automatia;
import com.mushroom.automatia.block.entity.EnergyCubeBE;
import com.mushroom.automatia.chambers.PlayerChambers;
import com.mushroom.automatia.energy.BundledEnergy;
import com.mushroom.automatia.energy.CustomEnergyStorage;
import com.mushroom.automatia.energy.CustomEnergyStorageProvider;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.event.AttachCapabilitiesEvent;

public class MaterialiteArmor extends ArmorItem{
	private static final int DEFAULT_ENERGY_DRAIN_MULTIPLIER = 10;
	private static final int DEFAULT_ENERGY_MAXIMUM = 10000;
	private static final int DEFAULT_ENERGY_INPUT = 100;

	public MaterialiteArmor(EquipmentSlot slot, Properties properties) {
		super(ArmorMaterials.DIAMOND, slot, properties);
	}
	@Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
		stack.getCapability(CapabilityEnergy.ENERGY).map(e -> {
			if(e.canExtract()) {
				e.extractEnergy(amount*DEFAULT_ENERGY_DRAIN_MULTIPLIER, false);
				return e.getEnergyStored()>0;
			}
			return true;
		});
        return 0;
    }
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		stack.getCapability(CapabilityEnergy.ENERGY).map(energy -> {
			energy.receiveEnergy(100, false);
			return true;
		});
		return super.use(level, player, hand);
	}
	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
		components.add(new TranslatableComponent("gui.text.energy").append(": ").append(String.valueOf(stack.getOrCreateTag().getCompound("Capabilities").getInt("energy"))).append(new TextComponent("/")).append(String.valueOf(stack.getOrCreateTag().getCompound("Capabilities").getInt("max"))).withStyle(ChatFormatting.RESET).withStyle(ChatFormatting.BLUE));
	}
	public static void attachCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
		if(event.getObject().getItem() instanceof MaterialiteArmor) {
			if(!event.getObject().getCapability(CapabilityEnergy.ENERGY).isPresent()) {
				event.addCapability(new ResourceLocation(automatia.MOD_ID+"_energy"), new CustomEnergyStorageProvider());
			}
		}
	}
	public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {event.register(CustomEnergyStorage.class);}
}
