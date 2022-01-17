package com.jakesnake.openenvironment.sol;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.biome.Biome;

@Environment(EnvType.CLIENT)
public class TemperatureManager {

	private float biomeTemperature;
	private float playerTemperature = 98.5f;
	private int ticksPassed;
	private double heatRate = 1200;


	private boolean extremeTemperature;

	
	public Biome currentBiome;

	private int timeExtreme = 0;
	
	private boolean needsDamage = false;

	private PlayerEntity player;

	public boolean needsDamage() {
		return needsDamage;
	}


	private int modifier = 1;


	public TemperatureManager(LivingEntity mixinEntityPlayer) {
		extremeTemperature = false;
		biomeTemperature = 70;
		player = (PlayerEntity) mixinEntityPlayer;
	}

	public boolean isExtremeTemperature() {
		return extremeTemperature;
	}
	
	public float getBiomeTemperature() {
		return biomeTemperature;
	}

	public void setExtremeTemperature(boolean isExtremeTemperature) {
		extremeTemperature = isExtremeTemperature;
	}
	public void setTemperatureLevel(int level) {
		biomeTemperature = level;
	}

	public void tick() {
		if(player.isCreative() || player.isSpectator() ) return;
		if(currentBiome != player.getEntityWorld().getBiome(player.getBlockPos())) {
			currentBiome = player.getEntityWorld().getBiome(player.getBlockPos());
			biomeTemperature = 25.0f + (player.getEntityWorld().getBiome(player.getBlockPos()).getTemperature()/0.1f)*5;
			heatRate = 1200 * Math.max((float)(15/Math.abs(biomeTemperature-(double)playerTemperature)), 1.5f);
			System.out.println(heatRate);
		}
		if(playerTemperature < biomeTemperature && ticksPassed > heatRate) {
			playerTemperature++;
			ticksPassed = 0;
		} else if (playerTemperature > biomeTemperature && ticksPassed > heatRate) {
			playerTemperature--;
			ticksPassed = 0;
		}
		ticksPassed++;

	}

	public void fromTag(CompoundTag tag) {
		if (tag.contains("thirstLevel", 99)) {
			biomeTemperature = tag.getInt("biomeTemperature");
			playerTemperature = tag.getInt("playerTemperature");
			ticksPassed = tag.getInt("ticksPassed");
			timeExtreme = tag.getInt("timeExtreme");
			needsDamage = tag.getBoolean("needsDamage");
		}
	}

	public void toTag(CompoundTag tag) {


		tag.putFloat("biomeTemperature", this.biomeTemperature);
		tag.putFloat("playerTemperature", this.playerTemperature);
		tag.putInt("ticksPassed", this.ticksPassed);
		tag.putInt("timeExtreme", this.timeExtreme);
		tag.putBoolean("needsDamage", this.needsDamage);
	}

}
