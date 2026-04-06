package com.mbest.modules.visual;

import com.mbest.modules.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import org.lwjgl.glfw.GLFW;

public class ZoomModule extends Module {

    private double originalFov = 70.0;
    private boolean zooming = false;

    public ZoomModule() {
        super("Zoom", "Hold C to zoom in like OptiFine", Category.VISUAL);
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (!isEnabled() || client.options == null) return;

        boolean cPressed = GLFW.glfwGetKey(
            client.getWindow().getHandle(),
            GLFW.GLFW_KEY_C
        ) == GLFW.GLFW_PRESS;

        if (cPressed && !zooming) {
            zooming = true;
            originalFov = client.options.getFov().getValue();
            client.options.getFov().setValue(15);
            client.options.write();
        } else if (!cPressed && zooming) {
            zooming = false;
            client.options.getFov().setValue((int) originalFov);
            client.options.write();
        }
    }

    @Override
    public void onDisable() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.options != null && zooming) {
            mc.options.getFov().setValue((int) originalFov);
            mc.options.write();
            zooming = false;
        }
    }
}
