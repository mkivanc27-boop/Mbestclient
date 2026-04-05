package com.mbest.modules;

import com.mbest.modules.fps.FPSBoostModule;
import com.mbest.modules.fps.NoParticlesModule;
import com.mbest.modules.pvp.*;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    private final List<Module> modules = new ArrayList<>();

    public final FPSBoostModule fpsBoost;
    public final NoParticlesModule noParticles;
    public final CPSCounterModule cpsCounter;
    public final SprintToggleModule sprintToggle;
    public final ArmorHUDModule armorHUD;
    public final ReachDisplayModule reachDisplay;

    public ModuleManager() {
        fpsBoost     = register(new FPSBoostModule());
        noParticles  = register(new NoParticlesModule());
        cpsCounter   = register(new CPSCounterModule());
        sprintToggle = register(new SprintToggleModule());
        armorHUD     = register(new ArmorHUDModule());
        reachDisplay = register(new ReachDisplayModule());
    }

    private <T extends Module> T register(T module) {
        modules.add(module);
        return module;
    }

    public void onTick(MinecraftClient client) {
        for (Module m : modules) {
            if (m.isEnabled()) m.onTick(client);
        }
    }

    public List<Module> getModules() { return modules; }

    public List<Module> getByCategory(Module.Category category) {
        List<Module> result = new ArrayList<>();
        for (Module m : modules) {
            if (m.getCategory() == category) result.add(m);
        }
        return result;
    }
}
