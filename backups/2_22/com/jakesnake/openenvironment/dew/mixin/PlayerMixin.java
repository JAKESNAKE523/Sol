package com.jakesnake.openenvironment.dew.mixin;

import com.jakesnake.openenvironment.dew.Dew;
import com.jakesnake.openenvironment.dew.ThirstManager;
import com.jakesnake.openenvironment.dew.overlay.ThirstOverlay;
import com.mojang.authlib.GameProfile;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.recipebook.ClientRecipeBook;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.StatHandler;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(ClientPlayerEntity.class)
public abstract class PlayerMixin extends LivingEntity {

	//private ThirstOverlay thirstOverlay = new ThirstOverlay(20);

	@Shadow public abstract boolean isMainPlayer();


	private boolean isThirsty = false;

	@Inject(at = @At("TAIL"), method = "<init>")
	private void init(MinecraftClient client, ClientWorld world, ClientPlayNetworkHandler networkHandler, StatHandler stats, ClientRecipeBook recipeBook, boolean lastSneaking, boolean lastSprinting, CallbackInfo ci){

			ClientPlayNetworking.registerReceiver(Dew.THIRST_PENALTY, (aclient, handler, buf, responseSender) -> {
				boolean thirsty = buf.readBoolean();
				aclient.execute(() -> {
					this.isThirsty = thirsty;
					if(isSprinting() && isThirsty){
						setSprinting(false);
					}
				});
			});

	}

	protected PlayerMixin(EntityType<? extends LivingEntity> entityType, World world) {

		super(entityType, world);


	}

	@Inject(at = @At("HEAD"), method = "tick")
	private void tick(CallbackInfo info) {


	}


	@Override
	public void setSprinting(boolean sprinting) {

		if(!isThirsty){
			super.setSprinting(sprinting);
		}else {
			EntityAttributeInstance entityAttributeInstance = this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
			entityAttributeInstance.removeModifier(UUID.fromString("662A6B8D-DA3E-4C1C-8813-96EA6097278D"));
			return;
		}//super.setSprinting(false);
	}

	public boolean isThirsty() {
		return isThirsty;
	}
}


