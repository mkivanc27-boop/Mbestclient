package com.mbest.gui;

import com.mbest.MBestClient;
import com.mbest.modules.Module;
import com.mbest.modules.fps.FPSBoostModule;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.List;

public class MBestScreen extends Screen {

    private static final int BG_COLOR    = 0xE0050505;
    private static final int PANEL_COLOR = 0xE0111111;
    private static final int ACCENT      = 0xFF00D4FF;
    private static final int ACCENT_DIM  = 0x5500D4FF;
    private static final int TEXT_COLOR  = 0xFFFFFFFF;
    private static final int TEXT_DIM    = 0xFF888888;
    private static final int ENABLED     = 0xFF00FF99;
    private static final int HOVER_COLOR = 0xFF1A1A2E;

    private static final int WIN_W = 420;
    private static final int WIN_H = 280;
    private static final int TAB_H = 28;

    private Module.Category selectedCategory = Module.Category.FPS;

    public MBestScreen() {
        super(Text.literal("MBest Client"));
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        renderBackground(ctx, mouseX, mouseY, delta);

        int x = (width - WIN_W) / 2;
        int y = (height - WIN_H) / 2;

        ctx.fill(x, y, x + WIN_W, y + WIN_H, BG_COLOR);
        ctx.fill(x, y, x + WIN_W, y + 2, ACCENT);
        ctx.drawTextWithShadow(textRenderer, "✦ MBest Client", x + 10, y + 7, ACCENT);
        ctx.drawTextWithShadow(textRenderer, "v1.0 | 1.20.2", x + WIN_W - 70, y + 7, TEXT_DIM);

        Module.Category[] cats = Module.Category.values();
        int tabW = WIN_W / cats.length;
        for (int i = 0; i < cats.length; i++) {
            Module.Category cat = cats[i];
            int tx = x + i * tabW;
            int ty = y + 18;
            boolean selected = cat == selectedCategory;
            ctx.fill(tx, ty, tx + tabW, ty + TAB_H, selected ? ACCENT_DIM : PANEL_COLOR);
            if (selected) ctx.fill(tx, ty + TAB_H - 2, tx + tabW, ty + TAB_H, ACCENT);
            int lx = tx + (tabW - textRenderer.getWidth(cat.displayName)) / 2;
            ctx.drawTextWithShadow(textRenderer, cat.displayName, lx, ty + 9, selected ? ACCENT : TEXT_DIM);
        }

        int listY = y + 18 + TAB_H;
        int listH = WIN_H - 18 - TAB_H;
        ctx.fill(x, listY, x + WIN_W, listY + listH, PANEL_COLOR);

        List<Module> mods = MBestClient.moduleManager.getByCategory(selectedCategory);
        for (int i = 0; i < mods.size(); i++) {
            Module mod = mods.get(i);
            int my = listY + i * 38;
            boolean hovered = mouseX >= x && mouseX <= x + WIN_W && mouseY >= my && mouseY <= my + 38;

            ctx.fill(x + 4, my + 4, x + WIN_W - 4, my + 36, hovered ? HOVER_COLOR : 0xFF0A0A0A);
            ctx.fill(x + 4, my + 4, x + 7, my + 36, mod.isEnabled() ? ENABLED : 0xFF333333);
            ctx.drawTextWithShadow(textRenderer, mod.getName(), x + 14, my + 10, mod.isEnabled() ? ENABLED : TEXT_COLOR);
            ctx.drawTextWithShadow(textRenderer, mod.getDescription(), x + 14, my + 22, TEXT_DIM);
            ctx.drawTextWithShadow(textRenderer, mod.isEnabled() ? "ON" : "OFF", x + WIN_W - 30, my + 14, mod.isEnabled() ? ENABLED : 0xFF555555);

            // FPS Boost level slider
            if (mod instanceof FPSBoostModule fps && mod.isEnabled() && selectedCategory == Module.Category.FPS) {
                String lvl = "Level: " + fps.getLevel().name();
                ctx.drawTextWithShadow(textRenderer, lvl, x + WIN_W - 100, my + 22, ACCENT);
            }
        }

        int barY = y + WIN_H - 14;
        ctx.fill(x, barY, x + WIN_W, barY + 14, 0xFF000000);
        ctx.fill(x, barY, x + WIN_W, barY + 1, ACCENT_DIM);
        ctx.drawTextWithShadow(textRenderer, "Click to toggle | Scroll on FPS Boost to change level | Right Shift to close", x + 8, barY + 3, TEXT_DIM);

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
            int ty = y + 18;
            if (mouseX >= tx && mouseX <= tx + tabW && mouseY >= ty && mouseY <= ty + TAB_H) {
                selectedCategory = cats[i];
                return true;
            }
        }

        int listY = y + 18 + TAB_H;
        List<Module> mods = MBestClient.moduleManager.getByCategory(selectedCategory);
        for (int i = 0; i < mods.size(); i++) {
            int my = listY + i * 38;
            if (mouseX >= x && mouseX <= x + WIN_W && mouseY >= my && mouseY <= my + 38) {
                mods.get(i).toggle();
                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        int x = (width - WIN_W) / 2;
        int y = (height - WIN_H) / 2;
        int listY = y + 18 + TAB_H;

        List<Module> mods = MBestClient.moduleManager.getByCategory(selectedCategory);
        for (int i = 0; i < mods.size(); i++) {
            int my = listY + i * 38;
            if (mouseX >= x && mouseX <= x + WIN_W && mouseY >= my && mouseY <= my + 38) {
                if (mods.get(i) instanceof FPSBoostModule fps) {
                    FPSBoostModule.BoostLevel[] levels = FPSBoostModule.BoostLevel.values();
                    int cur = fps.getLevel().ordinal();
                    int next = Math.max(0, Math.min(levels.length - 1, cur + (verticalAmount > 0 ? 1 : -1)));
                    fps.setLevel(levels[next]);
                    return true;
                }
            }
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
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
