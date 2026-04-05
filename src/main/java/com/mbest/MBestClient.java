package com.mbest;

import com.mbest.gui.MBestScreen;
import com.mbest.modules.ModuleManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MBestClient implements ClientModInitializer {
    public static final String MOD_ID = "mbestclient";
    public static final String MOD_NAME = "MBest Client";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static MBestClient INSTANCE;
    public static ModuleManager moduleManager;

    private static KeyBinding menuKey;

    @Override
    public void onInitializeClient() {
        INSTANCE = this;
        moduleManager = new ModuleManager();

        menuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.mbestclient.menu",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                "category.mbestclient"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (menuKey.wasPressed()) {
                if (client.currentScreen == null) {
                    client.setScreen(new MBestScreen());
                }
            }
            if (moduleManager != null) {
                moduleManager.onTick(client);
            }
        });

        LOGGER.info("[MBest Client] Loaded! Press Right Shift to open menu.");
    }

    public static MinecraftClient mc() {
        return MinecraftClient.getInstance();
    }
          }
