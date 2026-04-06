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
    private boolean applied = false;

    public FPSBoostModule() {
        super("FPS Boost", "Maximum FPS optimization", Category.FPS);
    }

    @Override
    public void onEnable() {
        applied = false;
        apply();
    }

    public void setLevel(BoostLevel level) {
        this.level = level;
        if (isEnabled()) {
            applied = false;
            apply();
        }
    }

    public BoostLevel getLevel() { return level; }

    @Override
    public void onTick(MinecraftClient client) {
        if (!isEnabled() || client.world == null) return;
        ClientWorld world = client.world;

        // No weather - sadece HIGH ve ULTRA
        if (level == BoostLevel.HIGH || level == BoostLevel.ULTRA) {
            world.setRainGradient(0);
            world.setThunderGradient(0);
        }

        // Firework temizle - sadece ULTRA
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

        // Sadece gerçekten değişmesi gereken ayarları değiştir
        switch (level) {
            case LOW -> {
                if ((int)mc.options.getViewDistance().getValue() != 6)
                    mc.options.getViewDistance().setValue(6);
                mc.options.getGraphicsMode().setValue(GraphicsMode.FAST);
                mc.options.getEntityDistanceScaling().setValue(0.75);
                mc.options.getParticles().setValue(ParticlesMode.DECREASED);
                mc.options.getBiomeBlendRadius().setValue(2);
                mc.options.getMipmapLevels().setValue(2);
                mc.options.getMaxFps().setValue(260);
            }
            case MEDIUM -> {
                if ((int)mc.options.getViewDistance().getValue() != 5)
                    mc.options.getViewDistance().setValue(5);
                mc.options.getGraphicsMode().setValue(GraphicsMode.FAST);
                mc.options.getEntityDistanceScaling().setValue(0.5);
                mc.options.getParticles().setValue(ParticlesMode.MINIMAL);
                mc.options.getBiomeBlendRadius().setValue(0);
                mc.options.getMipmapLevels().setValue(1);
                mc.options.getMaxFps().setValue(260);
            }
            case HIGH -> {
                if ((int)mc.options.getViewDistance().getValue() != 4)
                    mc.options.getViewDistance().setValue(4);
                mc.options.getGraphicsMode().setValue(GraphicsMode.FAST);
                mc.options.getEntityDistanceScaling().setValue(0.25);
                mc.options.getParticles().setValue(ParticlesMode.MINIMAL);
                mc.options.getBiomeBlendRadius().setValue(0);
                mc.options.getMipmapLevels().setValue(0);
                mc.options.getMaxFps().setValue(260);
            }
            case ULTRA -> {
                if ((int)mc.options.getViewDistance().getValue() != 2)
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
