package com.mbest.modules.pvp;

import com.mbest.modules.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

public class ReachDisplayModule extends Module {
    private double lastReach = 0;

    public ReachDisplayModule() {
        super("Reach Display", "Shows distance to target entity", Category.PVP);
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (!isEnabled() || client.player == null) return;

        HitResult hit = client.crosshairTarget;
        if (hit != null && hit.getType() == HitResult.Type.ENTITY) {
            EntityHitResult entityHit = (EntityHitResult) hit;
            Entity target = entityHit.getEntity();
            lastReach = client.player.distanceTo(target);
        }
    }

    public double getLastReach() { return lastReach; }
}
