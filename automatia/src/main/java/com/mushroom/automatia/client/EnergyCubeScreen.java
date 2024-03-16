package com.mushroom.automatia.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mushroom.automatia.automatia;
import com.mushroom.automatia.block.entity.EnergyCubeBE;
import com.mushroom.automatia.energy.CustomEnergyStorage;
import com.mushroom.automatia.energy.EnergyCubeMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.client.gui.widget.ExtendedButton;

public class EnergyCubeScreen extends AbstractContainerScreen<EnergyCubeMenu> {
	private static final ResourceLocation GUI = new ResourceLocation(automatia.MOD_ID,"textures/gui/energy_cube_gui.png");
	private static final ResourceLocation ENERGY_BAR = new ResourceLocation(automatia.MOD_ID,"textures/gui/power_bar.png");
	private static final ResourceLocation[] INSERTS = {new ResourceLocation(automatia.MOD_ID,"textures/gui/none.png"),new ResourceLocation(automatia.MOD_ID,"textures/gui/insert.png"),new ResourceLocation(automatia.MOD_ID,"textures/gui/extract.png"),new ResourceLocation(automatia.MOD_ID,"textures/gui/both.png")};
	
	public EnergyCubeScreen(EnergyCubeMenu menu, Inventory inv, Component name) {super(menu,inv,name);}
	
	@Override
	public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		this.renderTooltip(matrixStack, mouseX, mouseY);
	}
	@Override
	protected void renderLabels(PoseStack matrixStack, int mouseX, int mouseY) {
		drawString(matrixStack, Minecraft.getInstance().font,menu.getEnergy() + " " + new TranslatableComponent("gui.text.energy").getString(),56,20,0xffffff);
		drawString(matrixStack, Minecraft.getInstance().font,new TranslatableComponent("block.automatia.energy_cube").getString(),56,6,0xffffff);
	}
	@Override
	protected void renderBg(PoseStack matrixStack, float partialTicks, int mouseX, int mouseY) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.setShaderTexture(0, GUI);
		this.blit(matrixStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
		RenderSystem.setShaderTexture(0, ENERGY_BAR);
		//this.blit(matrixStack, this.leftPos+181, this.topPos+13, 2, CustomEnergyStorage.PowerBarIndex(menu.getEnergy(), SolarPanelBE.maxPower), 0, 8, 48,376,48); //generic energy bar placement
		this.blit(matrixStack, this.leftPos+9, this.topPos+13, 2, CustomEnergyStorage.PowerBarIndex(menu.getEnergy(), EnergyCubeBE.maxPower/4), 0, 8, 48,376,48); //compact energy bar placement
		this.blit(matrixStack, this.leftPos+19, this.topPos+13, 2, CustomEnergyStorage.PowerBarIndex(menu.getEnergy()-EnergyCubeBE.maxPower/4, EnergyCubeBE.maxPower/4), 0, 8, 48,376,48);
		this.blit(matrixStack, this.leftPos+29, this.topPos+13, 2, CustomEnergyStorage.PowerBarIndex(menu.getEnergy()-EnergyCubeBE.maxPower/2, EnergyCubeBE.maxPower/4), 0, 8, 48,376,48);
		this.blit(matrixStack, this.leftPos+39, this.topPos+13, 2, CustomEnergyStorage.PowerBarIndex((float)(menu.getEnergy()-EnergyCubeBE.maxPower*0.75), EnergyCubeBE.maxPower/4), 0, 8, 48,376,48);
		RenderSystem.setShaderTexture(0, INSERTS[menu.getSide(Direction.UP)&0b11]);
		this.blit(matrixStack, this.leftPos+148, this.topPos+24, 0, 0, 9, 9, 9, 9);
		RenderSystem.setShaderTexture(0, INSERTS[menu.getSide(Direction.NORTH)&0b11]);
		this.blit(matrixStack, this.leftPos+148, this.topPos+34, 0, 0, 9, 9, 9, 9);
		RenderSystem.setShaderTexture(0, INSERTS[menu.getSide(Direction.EAST)&0b11]);
		this.blit(matrixStack, this.leftPos+158, this.topPos+34, 0, 0, 9, 9, 9, 9);
		RenderSystem.setShaderTexture(0, INSERTS[menu.getSide(Direction.SOUTH)&0b11]);
		this.blit(matrixStack, this.leftPos+158, this.topPos+44, 0, 0, 9, 9, 9, 9);
		RenderSystem.setShaderTexture(0, INSERTS[menu.getSide(Direction.WEST)&0b11]);
		this.blit(matrixStack, this.leftPos+138, this.topPos+34, 0, 0, 9, 9, 9, 9);
		RenderSystem.setShaderTexture(0, INSERTS[menu.getSide(Direction.DOWN)&0b11]);
		this.blit(matrixStack, this.leftPos+148, this.topPos+44, 0, 0, 9, 9, 9, 9);
	}
	@Override
	protected void init() {
		this.imageWidth = 192;
		super.init();
		this.addWidget(new ExtendedButton(this.leftPos+148,this.topPos+24,9,9,new TextComponent("up"),btn -> {
			menu.setSide(Direction.UP, (byte)((menu.getSide(Direction.UP)+1)%4));
		}));
		this.addWidget(new ExtendedButton(this.leftPos+148,this.topPos+34,9,9,new TextComponent("north"),btn -> {
			menu.setSide(Direction.NORTH, (byte)((menu.getSide(Direction.NORTH)+1)%4));
		}));
		this.addWidget(new ExtendedButton(this.leftPos+158,this.topPos+34,9,9,new TextComponent("east"),btn -> {
			menu.setSide(Direction.EAST, (byte)((menu.getSide(Direction.EAST)+1)%4));
		}));
		this.addWidget(new ExtendedButton(this.leftPos+158,this.topPos+44,9,9,new TextComponent("south"),btn -> {
			menu.setSide(Direction.SOUTH, (byte)((menu.getSide(Direction.SOUTH)+1)%4));
		}));
		this.addWidget(new ExtendedButton(this.leftPos+138,this.topPos+34,9,9,new TextComponent("west"),btn -> {
			menu.setSide(Direction.WEST, (byte)((menu.getSide(Direction.WEST)+1)%4));
		}));
		this.addWidget(new ExtendedButton(this.leftPos+148,this.topPos+44,9,9,new TextComponent("down"),btn -> {
			menu.setSide(Direction.DOWN, (byte)((menu.getSide(Direction.DOWN)+1)%4));
		}));
	}
}
