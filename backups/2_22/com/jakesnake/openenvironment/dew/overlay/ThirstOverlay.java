package com.jakesnake.openenvironment.dew.overlay;

import com.jakesnake.openenvironment.dew.Dew;
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


public class ThirstOverlay {
    private MinecraftClient client;

    private int level;
    
    public ThirstOverlay(int startLevel) {

        client = MinecraftClient.getInstance();
        HudRenderCallback.EVENT.register((__, ___) -> {
            this.render();
        });

        ClientPlayNetworking.registerGlobalReceiver(Dew.getThirst_update(), (client, handler, buf, responseSender) -> {
            int thirstLevel = buf.readInt();
            client.execute(() ->{
                setLevel(thirstLevel);

            });
        });

        this.level = startLevel;
    }
    
    public void setLevel(int i) {
    	this.level = i;
    }

    private void render() {
        final PlayerEntity player = client.player;
        final MatrixStack matrixStack = new MatrixStack();

        if (player == null) return;

        int offsetx = (client.getWindow().getScaledWidth() / 2) + 10;
        int offsety = client.getWindow().getScaledHeight() - 48;

        if (!player.isCreative() && !player.isSpectator()) {
            RenderSystem.enableBlend();

            for(int i = 0; i<10-(level/2); i++) {
                client.getTextureManager().bindTexture(new Identifier("dew:textures/hud/thirst_empty.png"));
                DrawableHelper.drawTexture(matrixStack, offsetx+8*i, offsety, 0, 0, 8, 8, 8, 8);
            }
        	for(int i = 10-(level/2); i<10; i++) {
                client.getTextureManager().bindTexture(new Identifier("dew:textures/hud/thirst.png"));
        		DrawableHelper.drawTexture(matrixStack, offsetx+8*i, offsety, 0, 0, 8, 8, 8, 8);
        	}

        	if(level%2 == 1) {
                client.getTextureManager().bindTexture(new Identifier("dew:textures/hud/thirst.png"));
        		DrawableHelper.drawTexture(matrixStack, offsetx+8*(10-(level/2))-4, offsety, 4, 0, 4, 8, 8, 8);
        	}

        }
    }
}