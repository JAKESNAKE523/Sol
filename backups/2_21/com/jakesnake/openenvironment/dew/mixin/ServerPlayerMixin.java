package com.jakesnake.openenvironment.dew.mixin;

import com.jakesnake.openenvironment.dew.overlay.ThirstOverlay;
import com.mojang.authlib.GameProfile;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.jakesnake.openenvironment.dew.ThirstDamage;
import com.jakesnake.openenvironment.dew.ThirstManager;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

import java.util.Random;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerMixin extends PlayerEntity{

	private ThirstManager thirstManager = new ThirstManager(this);

	public ServerPlayerMixin(World world, BlockPos pos, float yaw, GameProfile profile) {
		super(world, pos, yaw, profile);
	}


	/*public ServerPlayerMixin(World world, BlockPos pos, float yaw, GameProfile profile) {
		super(world, pos, yaw, profile);

	}*/


	@Inject(at = @At("HEAD"), method = "tick")
	private void tick(CallbackInfo info) {
		thirstManager.tick();
		thirstManager.getThirsty();
		//if(thirstManager.needsDamage())
			//this.damage(new ThirstDamage(), 1);
		//this.getHungerManager().addExhaustion(1);
	}
	@Inject(at = @At("HEAD"), method = "writeCustomDataToTag")
	public void writeCustomDataToTag(CompoundTag tag, CallbackInfo ci) {

		thirstManager.toTag(tag);
	}

	@Inject(at = @At("HEAD"), method = "readCustomDataFromTag")
	public void readCustomDataFromTag(CompoundTag tag, CallbackInfo ci) {
		thirstManager = new ThirstManager(this);
		thirstManager.fromTag(tag);
	}

	@Override
	public void consumeItem() {
		Hand hand = this.getActiveHand();
		thirstManager.drink(this.getStackInHand(hand).getItem().getTranslationKey());
		if(this.getActiveItem().getItem() == Items.POTION && this.getStackInHand(hand).getOrCreateTag().get("Potion").equals("minecraft:water")){
			Random rand = new Random();
			switch(rand.nextInt(20)){
				case 1:
					world.getPlayerByUuid(this.uuid).addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 5));
					break;
				case 2:
					world.getPlayerByUuid(this.uuid).addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 5));
					break;
				case 3:
					world.getPlayerByUuid(this.uuid).addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 5));
					break;
				case 4:
					world.getPlayerByUuid(this.uuid).addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 5));
					break;
				case 5:
					world.getPlayerByUuid(this.uuid).addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 5));
					break;
				case 6:
					world.getPlayerByUuid(this.uuid).addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 5));
					break;
				case 7:
					world.getPlayerByUuid(this.uuid).addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 5));
					break;
				default:
					break;
			}

		}
		super.consumeItem();
	}

	/*@Override
	public void setSprinting(boolean sprinting) {
		thirstManager.setSprinting(sprinting);
	}*/

}


