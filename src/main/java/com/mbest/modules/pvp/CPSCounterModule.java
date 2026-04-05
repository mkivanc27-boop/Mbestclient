package com.mbest.modules.pvp;

import com.mbest.modules.Module;
import net.minecraft.client.MinecraftClient;

public class CPSCounterModule extends Module {
    private int cps = 0;
    private int clickBuffer = 0;
    private long lastSecond = System.currentTimeMillis();

    public CPSCounterModule() {
        super("CPS Counter", "Shows your clicks per second", Category.PVP);
    }

    public void registerClick() {
        if (!isEnabled()) return;
        clickBuffer++;
    }

    @Override
    public void onTick(MinecraftClient client) {
        long now = System.currentTimeMillis();
        if (now - lastSecond >= 1000) {
            cps = clickBuffer;
            clickBuffer = 0;
            lastSecond = now;
        }
    }

    public int getCPS() { return cps; }
}
