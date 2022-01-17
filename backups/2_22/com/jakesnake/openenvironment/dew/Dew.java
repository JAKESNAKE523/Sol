package com.jakesnake.openenvironment.dew;

import com.jakesnake.openenvironment.dew.blocks.WaterFilter;
import com.jakesnake.openenvironment.dew.blocks.WaterFilterEntity;
import com.jakesnake.openenvironment.dew.overlay.ThirstOverlay;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Dew implements ModInitializer {


	public static Dew dew;
	
	//public static ThirstOverlay tOverlay;

	private static DrinkRegistry drinkRegistry;
	public static final Identifier THIRST_UPDATE = new Identifier("dew","update");
	public static final Identifier THIRST_PENALTY = new Identifier("dew", "is_thirsty");

	private ThirstOverlay thirstOverlay;

	public static net.minecraft.util.Identifier getThirst_update() {
		return THIRST_UPDATE;
	}

	public static Identifier getThirst_penalty() {
		return THIRST_PENALTY;
	}

	//Blocks
	public static final Block WATER_FILTER = new WaterFilter(FabricBlockSettings.of(Material.STONE).strength(4.0f));
	public static final Item PURIFIED_WATER = new Item(new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(0).build()));
	public static BlockEntityType<WaterFilterEntity> WATER_FILTER_ENTITY;


	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		Registry.register(Registry.ITEM, new Identifier("dew", "purified_water"), PURIFIED_WATER);

		drinkRegistry = new DrinkRegistry();

		drinkRegistry.RegisterDrink(PURIFIED_WATER, 4);
		drinkRegistry.RegisterDrink(Items.POTION, 3);
		drinkRegistry.RegisterDrink(Items.MILK_BUCKET, 3);
		drinkRegistry.RegisterDrink(Items.MUSHROOM_STEW, 2);
		drinkRegistry.RegisterDrink(Items.SUSPICIOUS_STEW, 2);
		drinkRegistry.RegisterDrink(Items.RABBIT_STEW, 2);
		drinkRegistry.RegisterDrink(Items.MELON_SLICE, 1);


		for (DrinkRegisterEntryPoint entryPoint : FabricLoader.getInstance().getEntrypoints("thirstRegistry", DrinkRegisterEntryPoint.class)) {
			entryPoint.register(drinkRegistry);
		}

		Registry.register(Registry.BLOCK, new Identifier("dew", "water_filter"), WATER_FILTER);
		Registry.register(Registry.ITEM, new Identifier("dew", "water_filter"), new BlockItem(WATER_FILTER, new FabricItemSettings().group(ItemGroup.DECORATIONS)));
		WATER_FILTER_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, "dew:water_filter_entity", BlockEntityType.Builder.create(WaterFilterEntity::new, WATER_FILTER).build(null));
		thirstOverlay = new ThirstOverlay(20);

		ColorProviderRegistry.BLOCK.register((state, view, pos, tintIndex) -> 0x3495eb, WATER_FILTER);
		BlockRenderLayerMap.INSTANCE.putBlock(WATER_FILTER, RenderLayer.getCutout());

		dew = this;
	}
	/*public static ThirstOverlay getThirstOverlay() {
		return tOverlay;
	}*/
	
	public static Dew getInstance() {
		return dew;
	}

	/*public static ThirstManager getThirstManger() {
		return thirstManager;
	}*/

	public static DrinkRegistry getDrinkRegistry() {
		return drinkRegistry;
	}
}
