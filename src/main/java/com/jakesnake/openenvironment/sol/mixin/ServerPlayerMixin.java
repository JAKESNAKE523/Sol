package com.jakesnake.openenvironment.sol.mixin;

import com.jakesnake.openenvironment.sol.TemperatureManager;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

import java.util.Random;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerMixin extends PlayerEntity {

	@Shadow public abstract boolean isCreative();

	@Shadow public abstract boolean isSpectator();

	private TemperatureManager temperatureManager = new TemperatureManager(this);

	public ServerPlayerMixin(World world, BlockPos pos, float yaw, GameProfile profile) {
		super(world, pos, yaw, profile);
	}




	public TemperatureManager getTemperatureManager(){
		return temperatureManager;
	}

	@Inject(at = @At("HEAD"), method = "tick")
	private void tick(CallbackInfo info) {
		temperatureManager.tick();
		temperatureManager.getBiomeTemperature();
	}
	@Inject(at = @At("HEAD"), method = "writeCustomDataToTag")
	public void writeCustomDataToTag(CompoundTag tag, CallbackInfo ci) {

		temperatureManager.toTag(tag);
	}

	@Inject(at = @At("HEAD"), method = "readCustomDataFromTag")
	public void readCustomDataFromTag(CompoundTag tag, CallbackInfo ci) {
		temperatureManager = new TemperatureManager(this);
		temperatureManager.fromTag(tag);
	}
}


