package com.jakesnake.openenvironment.sol;

import com.jakesnake.openenvironment.sol.overlay.TemperatureOverlay;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Sol implements ModInitializer {


	public static Sol sol;

	public static final Identifier TEMPERATURE_UPDATE = new Identifier("sol","update");


	private TemperatureOverlay temperatureOverlay;

	public static Identifier getTemperatureUpdate() {
		return TEMPERATURE_UPDATE;
	}

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		temperatureOverlay = new TemperatureOverlay(70);

		sol = this;
	}

	public static Sol getInstance() {
		return sol;
	}

}
