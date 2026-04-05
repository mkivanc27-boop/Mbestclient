package com.mbest.mixin;

import com.mbest.MBestClient;
import com.mbest.modules.pvp.ArmorHUDModule;
import com.mbest.modules.pvp.CPSCounterModule;
import com.mbest.modules.pvp.ReachDisplayModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class HUDMixin {

    @Inject(method = "render", at = @At("TAIL"))
    private void onRender(DrawContext ctx, RenderTickCounter tickCounter, CallbackInfo ci) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.currentScreen != null) return;
        if (MBestClient.moduleManager == null) return;

        int x = 4;
        int y = 4;
        int lineH = 10;

        if (MBestClient.moduleManager.fpsBoost.isEnabled()) {
            String level = MBestClient.moduleManager.fpsBoost.getLevel().name();
            ctx.drawTextWithShadow(mc.textRenderer, "FPS: " + mc.getCurrentFps() + " [" + level + "]", x, y, 0xFF00D4FF);
            y += lineH;
        }

        CPSCounterModule cps = MBestClient.moduleManager.cpsCounter;
        if (cps.isEnabled()) {
            ctx.drawTextWithShadow(mc.textRenderer, "CPS: " + cps.getCPS(), x, y, 0xFF00FF99);
            y += lineH;
        }

        ReachDisplayModule reach = MBestClient.moduleManager.reachDisplay;
        if (reach.isEnabled()) {
            ctx.drawTextWithShadow(mc.textRenderer, String.format("Reach: %.2f", reach.getLastReach()), x, y, 0xFFFFD700);
            y += lineH;
        }

        ArmorHUDModule armor = MBestClient.moduleManager.armorHUD;
        if (armor.isEnabled()) {
            for (String line : ArmorHUDModule.getArmorInfo(mc).split("\n")) {
                if (!line.isEmpty()) {
                    ctx.drawTextWithShadow(mc.textRenderer, line, x, y, 0xFFFF6B6B);
                    y += lineH;
                }
            }
        }

        if (MBestClient.moduleManager.sprintToggle.isEnabled()) {
            ctx.drawTextWithShadow(mc.textRenderer, "⚡ Sprint", x, y, 0xFFFFAA00);
        }
    }
}
