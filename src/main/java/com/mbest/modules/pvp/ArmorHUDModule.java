package com.mbest.modules.pvp;

import com.mbest.modules.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;

public class ArmorHUDModule extends Module {

    public ArmorHUDModule() {
        super("Armor HUD", "Shows armor and item durability on screen", Category.PVP);
    }

    public static void render(MinecraftClient mc, net.minecraft.client.gui.DrawContext ctx) {
        if (mc.player == null) return;

        int x = 6;
        int y = 6;
        int slotSize = 18;
        int barW = 40;
        int barH = 4;

        // Armor parçaları yukardan aşağı: helmet, chestplate, leggings, boots
        ItemStack[] armorSlots = new ItemStack[4];
        int i = 0;
        for (ItemStack stack : mc.player.getArmorItems()) {
            armorSlots[i++] = stack;
        }
        // getArmorItems boots->helmet sırasıyla gelir, tersine çevir
        ItemStack[] reversed = new ItemStack[]{
            armorSlots[3], // helmet
            armorSlots[2], // chestplate
            armorSlots[1], // leggings
            armorSlots[0]  // boots
        };

        for (int slot = 0; slot < 4; slot++) {
            ItemStack stack = reversed[slot];
            int sy = y + slot * (slotSize + 6);

            // Item icon
            ctx.drawItem(stack, x, sy);
            ctx.drawItemInSlot(mc.textRenderer, stack, x, sy);

            if (!stack.isEmpty() && stack.getItem() instanceof ArmorItem) {
                int maxDur = stack.getMaxDamage();
                int curDur = maxDur - stack.getDamage();
                int percent = (int) ((curDur / (float) maxDur) * 100);

                // Durability bar rengi
                int barColor;
                if (percent > 60) barColor = 0xFF00FF44;
                else if (percent > 30) barColor = 0xFFFFAA00;
                else barColor = 0xFFFF2020;

                // Bar background
                ctx.fill(x + slotSize + 4, sy + 6, x + slotSize + 4 + barW, sy + 6 + barH, 0xFF1A1A1A);

                // Bar fill
                int fillW = (int) (barW * (percent / 100f));
                ctx.fill(x + slotSize + 4, sy + 6, x + slotSize + 4 + fillW, sy + 6 + barH, barColor);

                // Percent text
                ctx.drawTextWithShadow(mc.textRenderer,
                    percent + "%",
                    x + slotSize + 4 + barW + 3, sy + 5,
                    barColor);
            }
        }

        // En altta elimizdeki eşya
        ItemStack held = mc.player.getMainHandStack();
        int heldY = y + 4 * (slotSize + 6) + 4;

        if (!held.isEmpty()) {
            ctx.drawItem(held, x, heldY);
            ctx.drawItemInSlot(mc.textRenderer, held, x, heldY);

            String name = held.getItem().getName().getString();
            if (name.length() > 12) name = name.substring(0, 12) + "..";
            ctx.drawTextWithShadow(mc.textRenderer, name, x + slotSize + 4, heldY + 5, 0xFFFFFFFF);

            // Eğer durability varsa göster
            if (held.getMaxDamage() > 0) {
                int maxDur = held.getMaxDamage();
                int curDur = maxDur - held.getDamage();
                int percent = (int) ((curDur / (float) maxDur) * 100);
                int barColor = percent > 60 ? 0xFF00FF44 : percent > 30 ? 0xFFFFAA00 : 0xFFFF2020;
                ctx.fill(x + slotSize + 4, heldY + 14, x + slotSize + 4 + 40, heldY + 18, 0xFF1A1A1A);
                int fillW = (int) (40 * (percent / 100f));
                ctx.fill(x + slotSize + 4, heldY + 14, x + slotSize + 4 + fillW, heldY + 18, barColor);
            }
        }
    }
}
