package com.mushroom.automatia;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import network.PacketInit;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;

import com.mushroom.automatia.init.ItemInit;
import com.mushroom.automatia.init.ModBlockEntities;
import com.mushroom.automatia.item.MaterialiteArmor;
import com.mushroom.automatia.minimus.PlayerChamberEvents;
import com.mushroom.automatia.world.dimension.ModDimensions;
import com.mushroom.automatia.block.TransfungusMycelium;
import com.mushroom.automatia.chambers.PlayerChambers;
import com.mushroom.automatia.chambers.PlayerChambersEvents;
import com.mushroom.automatia.client.EnergyCubeScreen;
import com.mushroom.automatia.client.SolarPanelScreen;
import com.mushroom.automatia.init.BlockInit;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

@Mod("automatia")
public class automatia {
	public static final String MOD_ID = "automatia";
	public static final CreativeModeTab AUTOMATIA_GENERAL = new CreativeModeTab(MOD_ID) {
		@Override
		@OnlyIn(Dist.CLIENT)
		public ItemStack makeIcon() {
			return new ItemStack(ItemInit.MATERIALITE_INGOT.get());
		}
	};
	public void setup() {
		IEventBus bus = MinecraftForge.EVENT_BUS;
		bus.addGenericListener(Entity.class, PlayerChamberEvents::onAttachCapabilitiesPlayer);// these 3 for the chunk data
		bus.addListener(PlayerChamberEvents::onPlayerCloned);
		bus.addListener(PlayerChamberEvents::onRegisterCapabilities);
		bus.addGenericListener(Entity.class, PlayerChambersEvents::onAttachCapabilitiesPlayer);//these other 3 are for the player memory of their index
		bus.addListener(PlayerChambersEvents::onPlayerCloned);
		bus.addListener(PlayerChambersEvents::onRegisterCapabilities);
		bus.addGenericListener(ItemStack.class, MaterialiteArmor::attachCapabilities);
		bus.addListener(MaterialiteArmor::onRegisterCapabilities);
		//bus.addListener(TransfungusMycelium::tickEvent);
	}
	public void init(FMLCommonSetupEvent event) {
		event.enqueueWork(PacketInit::init);
	}
	public void clientSetup(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			MenuScreens.register(BlockInit.SOLAR_PANEL_MENU.get(), SolarPanelScreen::new);
			MenuScreens.register(BlockInit.ENERGY_CUBE_MENU.get(), EnergyCubeScreen::new);
			ItemBlockRenderTypes.setRenderLayer(BlockInit.MINIMUS_CHAMBERS.get(), RenderType.cutout());
			ItemBlockRenderTypes.setRenderLayer(BlockInit.DIAMOND_TRANSFUNGUS.get(), RenderType.cutout());
			ItemBlockRenderTypes.setRenderLayer(BlockInit.EMERALD_TRANSFUNGUS.get(), RenderType.cutout());
			ItemBlockRenderTypes.setRenderLayer(BlockInit.REDSTONE_TRANSFUNGUS.get(), RenderType.cutout());
			ItemBlockRenderTypes.setRenderLayer(BlockInit.LAPIS_TRANSFUNGUS.get(), RenderType.cutout());
		});
	}
	public automatia() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		
		bus.addListener(this::init);
		ItemInit.ITEMS.register(bus);
		BlockInit.BLOCKS.register(bus);
		ModBlockEntities.BLOCK_ENTITIES.register(bus);
		BlockInit.CONTAINERS.register(bus);
		ModDimensions.register();
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> bus.addListener(this::clientSetup));
		setup();
		MinecraftForge.EVENT_BUS.register(this);
	}
}
