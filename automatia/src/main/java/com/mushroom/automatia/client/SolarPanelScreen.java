package com.mushroom.automatia.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mushroom.automatia.automatia;
import com.mushroom.automatia.block.entity.SolarPanelBE;
import com.mushroom.automatia.energy.CustomEnergyStorage;
import com.mushroom.automatia.energy.SolarPanelMenu;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class SolarPanelScreen extends AbstractContainerScreen<SolarPanelMenu>{
	private static final ResourceLocation GUI = new ResourceLocation(automatia.MOD_ID,"textures/gui/solar_panel_gui.png");
	private static final ResourceLocation ENERGY_BAR = new ResourceLocation(automatia.MOD_ID,"textures/gui/power_bar.png");
	private static final ResourceLocation SUN = new ResourceLocation(automatia.MOD_ID,"textures/gui/sun_strength.png");
	
	public SolarPanelScreen(SolarPanelMenu menu, Inventory inv, Component name) {super(menu,inv,name);}
	
	@Override
	public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		this.renderTooltip(matrixStack, mouseX, mouseY);
	}
	@Override
	protected void renderLabels(PoseStack matrixStack, int mouseX, int mouseY) {
		drawString(matrixStack, Minecraft.getInstance().font,menu.getEnergy() + " " + new TranslatableComponent("gui.text.energy").getString(),48,19,0xffffff);
		drawString(matrixStack, Minecraft.getInstance().font,new TranslatableComponent("block.automatia.solar_panel").getString(),58,6,0xffffff);
		drawString(matrixStack, Minecraft.getInstance().font,menu.getEnergyGeneration() + " " + new TranslatableComponent("gui.text.energy_generation").getString(),48,30,0xffffff);
	}
	@Override
	protected void renderBg(PoseStack matrixStack, float partialTicks, int mouseX, int mouseY) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.setShaderTexture(0, GUI);
		this.blit(matrixStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
		RenderSystem.setShaderTexture(0, ENERGY_BAR);
		//this.blit(matrixStack, this.leftPos+181, this.topPos+13, 2, CustomEnergyStorage.PowerBarIndex(menu.getEnergy(), SolarPanelBE.maxPower), 0, 8, 48,376,48); //generic energy bar placement
		this.blit(matrixStack, this.leftPos+152, this.topPos+13, 2, CustomEnergyStorage.PowerBarIndex(menu.getEnergy(), SolarPanelBE.maxPower), 0, 8, 48,376,48); //compact energy bar placement
		RenderSystem.setShaderTexture(0, SUN);
		this.blit(matrixStack, this.leftPos+20, this.topPos+29, 2, menu.getEnergyGeneration()*8, 0, 8, 8,176,8); //sun placement
		if(menu.getEnergyGeneration()==1&&(menu.getDayTime()<100||menu.getDayTime()>11900)) {
			this.blit(matrixStack, this.leftPos+20, this.topPos+29, 2, 168, 0, 8, 8,176,8);
		}
	}
	@Override
	protected void init() {
		this.imageWidth = 192;
		super.init();
	}
}
