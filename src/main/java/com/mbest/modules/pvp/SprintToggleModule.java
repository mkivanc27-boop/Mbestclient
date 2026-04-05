src/main/java/com/mbest/modules/pvp/SprintToggleModule.java
İçine:
package com.mbest.modules.pvp;

import com.mbest.modules.Module;
import net.minecraft.client.MinecraftClient;

public class SprintToggleModule extends Module {

    public SprintToggleModule() {
        super("Sprint Toggle", "Always sprint without holding sprint key", Category.PVP);
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (!isEnabled()) return;
        if (client.player == null) return;

        if (client.options.forwardKey.isPressed()) {
            client.player.setSprinting(true);
        }
    }
}
