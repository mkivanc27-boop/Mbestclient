package com.mbest.modules.fps;

import com.mbest.modules.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.ParticlesMode;

public class NoParticlesModule extends Module {

    public NoParticlesModule() {
        super("No Particles", "Removes all particles for better FPS", Category.FPS);
    }

    @Override
    public void onEnable() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.options != null) {
            mc.options.getParticles().setValue(ParticlesMode.MINIMAL);
            mc.options.write();
        }
    }

    @Override
    public void onDisable() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.options != null) {
            mc.options.getParticles().setValue(ParticlesMode.ALL);
            mc.options.write();
        }
    }
}
