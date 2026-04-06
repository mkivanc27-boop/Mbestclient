package com.mbest.gui;

import com.mbest.MBestClient;
import com.mbest.modules.Module;
import com.mbest.modules.fps.FPSBoostModule;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.List;

public class MBestScreen extends Screen {

    private static final int BG          = 0xF0050505;
    private static final int PANEL       = 0xF0100000;
    private static final int ACCENT      = 0xFFFF1A1A;
    private static final int ACCENT_DIM  = 0x55FF1A1A;
    private static final int ENABLED     = 0xFFFF3333;
    private static final int TEXT        = 0xFFFFFFFF;
    private static final int TEXT_DIM    = 0xFF888888;
    private static final int HOVER       = 0xFF1A0000;
    private static final int GOLD        = 0xFFFFAA00;

    private static final int WIN_W = 440;
    private static final int WIN_H = 300;
    private static final int TAB_H = 26;
    private static final int ROW_H = 36;

    private Module.Category selectedCategory = Module.Category.FPS;
    private long openTime = 0;

    public MBestScreen() {
        super(Text.literal("MBest Client"));
        openTime = System.currentTimeMillis();
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        renderBackground(ctx, mouseX, mouseY, delta);

        int x = (width - WIN_W) / 2;
        int y = (height - WIN_H) / 2;

        // Outer glow effect
        ctx.fill(x - 1, y - 1, x + WIN_W + 1, y + WIN_H + 1, ACCENT_DIM);

        // Main background
        ctx.fill(x, y, x + WIN_W, y + WIN_H, BG);

        // Top bar
        ctx.fill(x, y, x + WIN_W, y + 18, 0xFF0A0000);
        ctx.fill(x, y + 18, x + WIN_W, y + 19, ACCENT);

        // Animated title flicker
        long elapsed = System.currentTimeMillis() - openTime;
        int titleColor = (elapsed % 1000 < 500) ? ACCENT : 0xFFFF6666;
        ctx.drawTextWithShadow(textRenderer, "⚔ MBEST CLIENT", x + 8, y + 5, titleColor);
        ctx.drawTextWithShadow(textRenderer, "v1.0 | 1.20.2", x + WIN_W - 75, y + 5, TEXT_DIM);

        // Category tabs
        Module.Category[] cats = Module.Category.values();
        int tabW = WIN_W / cats.length;
        for (int i = 0; i < cats.length; i++) {
            Module.Category cat = cats[i];
            int tx = x + i * tabW;
            int ty = y + 19;
            boolean sel = cat == selectedCategory;
            boolean hov = mouseX >= tx && mouseX < tx + tabW && mouseY >= ty && mouseY < ty + TAB_H;

            ctx.fill(tx, ty, tx + tabW, ty + TAB_H, sel ? 0xFF1A0000 : hov ? 0xFF0D0000 : 0xFF080000);
            if (sel) {
                ctx.fill(tx, ty, tx + tabW, ty + 2, ACCENT);
                ctx.fill(tx, ty + TAB_H - 2, tx + tabW, ty + TAB_H, ACCENT);
            }
            ctx.fill(tx + tabW - 1, ty, tx + tabW, ty + TAB_H, 0xFF1A0000);

            String label = cat.displayName.toUpperCase();
            int lx = tx + (tabW - textRenderer.getWidth(label)) / 2;
            ctx.drawTextWithShadow(textRenderer, label, lx, ty + 8, sel ? ACCENT : hov ? 0xFFFF6666 : TEXT_DIM);
        }

        // Module list
        int listY = y + 19 + TAB_H;
        int listH = WIN_H - 19 - TAB_H - 16;
        ctx.fill(x, listY, x + WIN_W, listY + listH, PANEL);

        List<Module> mods = MBestClient.moduleManager.getByCategory(selectedCategory);
        for (int i = 0; i < mods.size(); i++) {
            Module mod = mods.get(i);
            int ry = listY + i * ROW_H;
            if (ry + ROW_H > listY + listH) break;

            boolean hov = mouseX >= x && mouseX < x + WIN_W && mouseY >= ry && mouseY < ry + ROW_H;

            // Row background
            ctx.fill(x + 3, ry + 2, x + WIN_W - 3, ry + ROW_H - 2, hov ? HOVER : 0xFF080000);

            // Left bar
            ctx.fill(x + 3, ry + 2, x + 6, ry + ROW_H - 2, mod.isEnabled() ? ACCENT : 0xFF330000);

            // Module name
            ctx.drawTextWithShadow(textRenderer, mod.getName(), x + 12, ry + 6, mod.isEnabled() ? ENABLED : TEXT);

            // Description
            ctx.drawTextWithShadow(textRenderer, mod.getDescription(), x + 12, ry + 18, TEXT_DIM);

            // Toggle button
            int btnX = x + WIN_W - 42;
            int btnY = ry + 10;
            ctx.fill(btnX, btnY, btnX + 36, btnY + 14, mod.isEnabled() ? 0xFF330000 : 0xFF1A1A1A);
            ctx.fill(btnX, btnY, btnX + 36, btnY + 1, mod.isEnabled() ? ACCENT : 0xFF333333);
            ctx.fill(btnX, btnY + 13, btnX + 36, btnY + 14, mod.isEnabled() ? ACCENT : 0xFF333333);
            ctx.drawTextWithShadow(textRenderer,
                mod.isEnabled() ? "ON" : "OFF",
                btnX + (mod.isEnabled() ? 9 : 7), btnY + 3,
                mod.isEnabled() ? ACCENT : TEXT_DIM);

            // FPS level indicator
            if (mod instanceof FPSBoostModule fps && mod.isEnabled()) {
                String lvl = fps.getLevel().name();
                int lvlColor = switch (fps.getLevel()) {
                    case LOW -> 0xFF00FF00;
                    case MEDIUM -> GOLD;
                    case HIGH -> 0xFFFF6600;
                    case ULTRA -> ACCENT;
                };
                ctx.drawTextWithShadow(textRenderer, "◆ " + lvl, x + WIN_W - 120, ry + 6, lvlColor);
            }
        }

        // Bottom bar
        int barY = y + WIN_H - 16;
        ctx.fill(x, barY, x + WIN_W, barY + 16, 0xFF0A0000);
        ctx.fill(x, barY, x + WIN_W, barY + 1, ACCENT_DIM);
        ctx.drawTextWithShadow(textRenderer, "CLICK: toggle  |  SCROLL on FPS: change level  |  RSHIFT: close", x + 6, barY + 4, TEXT_DIM);

        super.render(ctx, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int x = (width - WIN_W) / 2;
        int y = (height - WIN_H) / 2;

        Module.Category[] cats = Module.Category.values();
        int tabW = WIN_W / cats.length;
        for (int i = 0; i < cats.length; i++) {
            int tx = x + i * tabW;
            int ty = y + 19;
            if (mouseX >= tx && mouseX < tx + tabW && mouseY >= ty && mouseY < ty + TAB_H) {
                selectedCategory = cats[i];
                return true;
            }
        }

        int listY = y + 19 + TAB_H;
        List<Module> mods = MBestClient.moduleManager.getByCategory(selectedCategory);
        for (int i = 0; i < mods.size(); i++) {
            int ry = listY + i * ROW_H;
            if (mouseX >= x && mouseX < x + WIN_W && mouseY >= ry && mouseY < ry + ROW_H) {
                mods.get(i).toggle();
                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double hAmount, double vAmount) {
        int x = (width - WIN_W) / 2;
        int y = (height - WIN_H) / 2;
        int listY = y + 19 + TAB_H;

        List<Module> mods = MBestClient.moduleManager.getByCategory(selectedCategory);
        for (int i = 0; i < mods.size(); i++) {
            int ry = listY + i * ROW_H;
            if (mouseX >= x && mouseX < x + WIN_W && mouseY >= ry && mouseY < ry + ROW_H) {
                if (mods.get(i) instanceof FPSBoostModule fps) {
                    FPSBoostModule.BoostLevel[] levels = FPSBoostModule.BoostLevel.values();
                    int cur = fps.getLevel().ordinal();
                    int next = Math.max(0, Math.min(levels.length - 1, cur + (vAmount > 0 ? 1 : -1)));
                    fps.setLevel(levels[next]);
                    return true;
                }
            }
        }
        return super.mouseScrolled(mouseX, mouseY, hAmount, vAmount);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT_SHIFT) {
            this.close();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean shouldPause() { return false; }
                    }
