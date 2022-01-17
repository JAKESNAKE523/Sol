package com.jakesnake.openenvironment.dew;

import com.jakesnake.openenvironment.dew.overlay.ThirstOverlay;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

@Environment(EnvType.CLIENT)
public class ThirstManager {

	private int thirstLevel;
	
	private boolean thirst;
	
	private int thirstRate;
	
	private int ticksElapsed = 0;
	
	private int timeThirsty = 0;
	
	private boolean needsDamage = false;

	private PlayerEntity player;

	public boolean needsDamage() {
		return needsDamage;
	}

	private DrinkRegistry reg;

	private int modifier = 1;


	public ThirstManager(LivingEntity mixinEntityPlayer) {
		thirst = false;
		thirstLevel = 20;
		thirstRate = 1500;
		reg = Dew.getInstance().getDrinkRegistry();
		player = (PlayerEntity) mixinEntityPlayer;
	}
	
	public boolean getThirsty() {
		return thirst;
	}
	
	public int getThirstLevel() {
		return thirstLevel;
	}
	
	public void setThirsty(boolean isThirsty) {
		thirst = isThirsty;
	}
	public void setThirstLevel(int level) {
		thirstLevel = level;
	}

	public void tick() {

		if(player.isCreative() || player.isSpectator() ) return;

		if(thirstLevel == 0) {
			if(timeThirsty == 120) {
				player.damage(new ThirstDamage(), 1);
				timeThirsty = 0;
			} else timeThirsty++;
			requestClientUpdate();
			return;
		}
		ticksElapsed+=modifier;
		if(ticksElapsed >= thirstRate ) {
			timeThirsty = 0;
			lowerThirstLevel(1);
			ticksElapsed = 0;


		}
		requestClientUpdate();
		updateThirst();
	}

	private void requestClientUpdate() {
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeInt(thirstLevel);
		ServerPlayNetworking.send((ServerPlayerEntity) player, Dew.THIRST_UPDATE, buf);



	}

	private void updateThirst() {
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeBoolean(thirst);
		ServerPlayNetworking.send((ServerPlayerEntity) player, Dew.getThirst_penalty(), buf);


	}

	public void drink (Item item){
		int additionalDrink = reg.getNutrients(item);
		if(thirstLevel + additionalDrink > 20) {
			thirstLevel = 20;
			setThirsty(false);
		}else
			increaseThirstLevel(reg.getNutrients(item));



		requestClientUpdate();
		//tOverlay.setLevel(thirstLevel);
	}

	public void lowerThirstLevel(int val) {
		thirstLevel -= val;
		if(thirstLevel >= 8){
			setThirsty(false);
		} else setThirsty(true);
	}

	public void increaseThirstLevel(int val) {
		thirstLevel += val;
		if(thirstLevel >= 8){
			setThirsty(false);
		} else setThirsty(true);
	}

	public void fromTag(CompoundTag tag) {


		if (tag.contains("thirstLevel", 99)) {
			thirstLevel = tag.getInt("thirstLevel");
			ticksElapsed = tag.getInt("ticksElapsed");
			timeThirsty = tag.getInt("timeThirsty");
			needsDamage = tag.getBoolean("needsDamage");
		}
		//requestClientUpdate();
		//tOverlay.setLevel(thirstLevel);
	}

	public void toTag(CompoundTag tag) {


		tag.putInt("thirstLevel", this.thirstLevel);
		tag.putInt("ticksElapsed", this.ticksElapsed);
		tag.putInt("timeThirsty", this.timeThirsty);
		tag.putBoolean("needsDamage", this.needsDamage);
	}

	public void setSprinting(boolean b) {
		if(!b && modifier > 1){
			modifier -= 1;
		} else if (b){
			modifier += 1;
		}
	}
}
