package com.mbest.modules.visual;

import com.mbest.modules.Module;
import net.minecraft.client.MinecraftClient;

public class FullbrightModule extends Module {

    public FullbrightModule() {
        super("Fullbright", "Makes everything fully bright, no dark areas", Category.VISUAL);
    }

    @Override
    public void onEnable() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.options != null) {
            mc.options.getGamma().setValue(16.0);
            mc.options.write();
        }
    }

    @Override
    public void onDisable() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.options != null) {
            mc.options.getGamma().setValue(1.0);
            mc.options.write();
        }
    }
}
