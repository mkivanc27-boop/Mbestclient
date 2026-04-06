package com.mbest.modules.fps;

import com.mbest.modules.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GraphicsMode;
import net.minecraft.client.option.ParticlesMode;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.FireworkRocketEntity;

public class FPSBoostModule extends Module {

    public enum BoostLevel {
        LOW, MEDIUM, HIGH, ULTRA
    }

    private BoostLevel level = BoostLevel.HIGH;

    public FPSBoostModule() {
        super("FPS Boost", "Maximum FPS optimization", Category.FPS);
    }

    @Override
    public void onEnable() { apply(); }

    public void setLevel(BoostLevel level) {
        this.level = level;
        if (isEnabled()) apply();
    }

    public BoostLevel getLevel() { return level; }

    @Override
    public void onTick(MinecraftClient client) {
        if (!isEnabled() || client.world == null) return;
        ClientWorld world = client.world;
        if (level == BoostLevel.HIGH || level == BoostLevel.ULTRA) {
            world.setRainGradient(0);
            world.setThunderGradient(0);
        }
        if (level == BoostLevel.ULTRA) {
            for (Entity e : world.getEntities()) {
                if (e instanceof FireworkRocketEntity) {
                    e.discard();
                }
            }
        }
    }

    private void apply() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.options == null) return;
        switch (level) {
            case LOW -> {
                mc.options.getViewDistance().setValue(6);
                mc.options.getGraphicsMode().setValue(GraphicsMode.FAST);
                mc.options.getEntityDistanceScaling().setValue(0.75);
                mc.options.getParticles().setValue(ParticlesMode.DECREASED);
                mc.options.getBiomeBlendRadius().setValue(2);
                mc.options.getMipmapLevels().setValue(2);
                mc.options.getMaxFps().setValue(260);
            }
            case MEDIUM -> {
                mc.options.getViewDistance().setValue(5);
                mc.options.getGraphicsMode().setValue(GraphicsMode.FAST);
                mc.options.getEntityDistanceScaling().setValue(0.5);
                mc.options.getParticles().setValue(ParticlesMode.MINIMAL);
                mc.options.getBiomeBlendRadius().setValue(0);
                mc.options.getMipmapLevels().setValue(1);
                mc.options.getMaxFps().setValue(260);
            }
            case HIGH -> {
                mc.options.getViewDistance().setValue(4);
                mc.options.getGraphicsMode().setValue(GraphicsMode.FAST);
                mc.options.getEntityDistanceScaling().setValue(0.25);
                mc.options.getParticles().setValue(ParticlesMode.MINIMAL);
                mc.options.getBiomeBlendRadius().setValue(0);
                mc.options.getMipmapLevels().setValue(0);
                mc.options.getMaxFps().setValue(260);
            }
            case ULTRA -> {
                mc.options.getViewDistance().setValue(2);
                mc.options.getGraphicsMode().setValue(GraphicsMode.FAST);
                mc.options.getEntityDistanceScaling().setValue(0.1);
                mc.options.getParticles().setValue(ParticlesMode.MINIMAL);
                mc.options.getBiomeBlendRadius().setValue(0);
                mc.options.getMipmapLevels().setValue(0);
                mc.options.getGamma().setValue(16.0);
                mc.options.getMaxFps().setValue(260);
            }
        }
        mc.options.write();
    }

    @Override
    public void onDisable() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.options == null) return;
        mc.options.getViewDistance().setValue(8);
        mc.options.getGraphicsMode().setValue(GraphicsMode.FANCY);
        mc.options.getEntityDistanceScaling().setValue(1.0);
        mc.options.getParticles().setValue(ParticlesMode.ALL);
        mc.options.getBiomeBlendRadius().setValue(2);
        mc.options.getMipmapLevels().setValue(4);
        mc.options.getGamma().setValue(1.0);
        mc.options.write();
    }
}
