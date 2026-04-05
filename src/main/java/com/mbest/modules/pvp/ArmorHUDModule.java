package com.mbest.modules.pvp;

import com.mbest.modules.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;

public class ArmorHUDModule extends Module {

    public ArmorHUDModule() {
        super("Armor HUD", "Shows armor durability on screen", Category.PVP);
    }

    public static String getArmorInfo(MinecraftClient client) {
        if (client.player == null) return "";

        StringBuilder sb = new StringBuilder();
        for (ItemStack stack : client.player.getArmorItems()) {
            if (stack.isEmpty()) continue;
            if (stack.getItem() instanceof ArmorItem) {
                int durability = stack.getMaxDamage() - stack.getDamage();
                int max = stack.getMaxDamage();
                int percent = (int) ((durability / (float) max) * 100);
                sb.append(stack.getItem().getName().getString())
                  .append(": ").append(percent).append("%\n");
            }
        }
        return sb.toString();
    }
}
