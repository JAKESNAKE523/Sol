package com.jakesnake.openenvironment.sol.overlay;

import com.jakesnake.openenvironment.sol.Sol;
import com.mojang.blaze3d.systems.RenderSystem;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

import static net.minecraft.client.gui.DrawableHelper.*;


public class TemperatureOverlay {
    private MinecraftClient client;

    private float ticksElapsed;
    private float targetLevel;

    private float level;
    
    public TemperatureOverlay(int startLevel) {

        client = MinecraftClient.getInstance();
        HudRenderCallback.EVENT.register((__, ___) -> {
            this.render();
        });


        this.level = this.targetLevel = startLevel;
    }
    
    public void setLevel(int i) {
    	this.level = i;
    }

    private void render() {


        final PlayerEntity player = client.player;
        final MatrixStack matrixStack = new MatrixStack();

        if (player == null) return;

        //Should be 24.5f
        float temp = 25.0f + (player.getEntityWorld().getBiome(player.getBlockPos()).getTemperature()/0.1f)*5;

        if (targetLevel != temp) {targetLevel = temp; ticksElapsed = 0;};

        if(level < targetLevel){
            if(ticksElapsed >= 40) {
                level += 5;
                ticksElapsed = 0;
            }
        } else if(level > targetLevel){
            if(ticksElapsed >= 40) {
                level -= 5;
                ticksElapsed = 0;
            }
        }
        ticksElapsed++;
        System.out.println(level);

        int offsetx = (client.getWindow().getScaledWidth() / 2) - 120;
        int offsety = client.getWindow().getScaledHeight() - 34;

        if (!player.isCreative() && !player.isSpectator()) {
            RenderSystem.enableBlend();

            int thermLev = (int) Math.floor((((level)/125.0f)*25) + 6);

            client.getTextureManager().bindTexture(new Identifier("sol:textures/hud/empty_thermometer.png"));
            drawTexture(matrixStack, offsetx, offsety, 0, 0, 32, 32, 32, 32);

            client.getTextureManager().bindTexture(new Identifier("sol:textures/hud/thermometer_overlay.png"));
            drawTexture(matrixStack, offsetx, offsety+(32-(int)thermLev), 0, 32-thermLev, 32, (int) thermLev, 32, 32);

        }
    }
}