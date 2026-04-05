package com.mbest.modules;

import net.minecraft.client.MinecraftClient;

public abstract class Module {
    protected final String name;
    protected final String description;
    protected final Category category;
    protected boolean enabled;

    public enum Category {
        FPS("FPS"),
        PVP("PvP"),
        VISUAL("Visual"),
        UTILITY("Utility");

        public final String displayName;
        Category(String displayName) { this.displayName = displayName; }
    }

    public Module(String name, String description, Category category) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.enabled = false;
    }

    public void onEnable() {}
    public void onDisable() {}
    public void onTick(MinecraftClient client) {}

    public void toggle() {
        enabled = !enabled;
        if (enabled) onEnable();
        else onDisable();
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public Category getCategory() { return category; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) {
        if (this.enabled != enabled) toggle();
    }
}
