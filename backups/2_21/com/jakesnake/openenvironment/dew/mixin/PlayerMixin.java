package com.jakesnake.openenvironment.dew.mixin;

import com.jakesnake.openenvironment.dew.Dew;
import com.jakesnake.openenvironment.dew.ThirstManager;
import com.jakesnake.openenvironment.dew.overlay.ThirstOverlay;
import com.mojang.authlib.GameProfile;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(PlayerEntity.class)
public abstract class PlayerMixin extends LivingEntity {

	//private ThirstOverlay thirstOverlay = new ThirstOverlay(20);

	private boolean didRun = false;



	private boolean isThirsty = false;

	@Inject(at = @At("TAIL"), method = "<init>")
	private void init(World world, BlockPos pos, float yaw, GameProfile profile, CallbackInfo ci){
		if(!world.isClient()) {

			ClientPlayNetworking.registerGlobalReceiver(Dew.getThirst_penalty(), (client, handler, buf, responseSender) -> {
				boolean thirsty = buf.readBoolean();
				this.isThirsty = true;
				client.execute(() -> {
					System.out.println(isThirsty);
					this.isThirsty = true;
				});


			});

		}
	}

	protected PlayerMixin(EntityType<? extends LivingEntity> entityType, World world) {

		super(entityType, world);


	}

	@Inject(at = @At("HEAD"), method = "tick")
	private void tick(CallbackInfo info) {


	}
	public void setIsThirsty(boolean b){

	}

	@Override
	public void setSprinting(boolean sprinting) {

		System.out.println(isThirsty + ": " + System.identityHashCode(this));
		if(!isThirsty){
			super.setSprinting(sprinting);
		}else return; //super.setSprinting(false);
	}

	public boolean isThirsty() {
		return isThirsty;
	}
}


