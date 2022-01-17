package com.jakesnake.openenvironment.sol.mixin;

import net.fabricmc.fabric.api.biome.v1.OverworldBiomes;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.client.render.SkyProperties;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.tag.FluidTags;
import net.minecraft.tag.TagGroup;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Iterator;
import java.util.List;

@Mixin(GlassBottleItem.class)
public abstract class GlassBottleMixin extends Item {

    public GlassBottleMixin(Settings settings) {
        super(settings);
    }

    @Inject(at = @At("HEAD"), method = "fill")
    private void fill(ItemStack itemStack, PlayerEntity playerEntity, ItemStack itemStack2, CallbackInfoReturnable ci) {
        if(!itemStack2.getOrCreateTag().get("Potion").asString().equals("minecraft:water")) return;
        World world = playerEntity.world;
        String biomeName = world.getBiome(playerEntity.getBlockPos()).getCategory().getName().substring(0,1).toUpperCase() + world.getBiome(playerEntity.getBlockPos()).getCategory().getName().substring(1);

        world.playSound(playerEntity, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
        CompoundTag tag = itemStack2.getTag();
        CompoundTag display = new CompoundTag();
        ListTag Lore = new ListTag();
        StringTag biomeTag = StringTag.of(Text.Serializer.toJson(Text.of(Formatting.AQUA + biomeName)));
        Formatting colorTag = StringTag.AQUA;
        Lore.addTag(0, biomeTag);
        display.put("Lore", Lore);
        tag.put("display", display);
        tag.putString("biome", world.getBiome(playerEntity.getBlockPos()).getCategory().getName());
        itemStack2.setTag(tag);
    }
}
///give @s bow{display:{Lore:['{"text":"line 1","color":"dark_aqua"}','{"text":"line 2"}']}}

