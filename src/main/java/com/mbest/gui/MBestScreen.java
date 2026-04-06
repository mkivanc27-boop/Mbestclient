package com.mbest.gui;

import com.mbest.MBestClient;
import com.mbest.modules.Module;
import com.mbest.modules.fps.FPSBoostModule;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.List;

public class MBestScreen extends Screen {

    // Colors
    private static final int BG           = 0xF0080808;
    private static final int CARD_BG      = 0xF0130000;
    private static final int CARD_HOV     = 0xF01E0000;
    private static final int CARD_ON      = 0xF01A0000;
    private static final int ACCENT       = 0xFFFF2020;
    private static final int ACCENT_DIM   = 0x44FF2020;
    private static final int ACCENT_GLOW  = 0x22FF2020;
    private static final int TEXT         = 0xFFFFFFFF;
    private static final int TEXT_DIM     = 0xFF999999;
    private static final int TEXT_OFF     = 0xFF555555;
    private static final int ENABLED      = 0xFFFF3333;
    private static final int TAB_SEL      = 0xFF1A0000;
    private static final int TAB_NORM     = 0xFF0D0000;
    private static final int SEPARATOR    = 0xFF1A1A1A;

    // Layout
    private static final int WIN_W  = 500;
    private static final int WIN_H  = 320;
    private static final int TAB_H  = 28;
    private static final int HDR_H  = 22;
    private static final int CARD_W = 150;
    private static final int CARD_H = 60;
    private static final int CARD_PAD = 8;

    private Module.Category selectedCategory = Module.Category.FPS;
    private final long openTime = System.currentTimeMillis();

    public MBestScreen() {
        super(Text.literal("MBest Client"));
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        renderBackground(ctx, mouseX, mouseY, delta);

        int wx = (width - WIN_W) / 2;
        int wy = (height - WIN_H) / 2;

        // Outer glow
        for (int i = 3; i > 0; i--) {
            ctx.fill(wx - i, wy - i, wx + WIN_W + i, wy + WIN_H + i,
                    0x11FF2020 / i);
        }

        // Window background
        ctx.fill(wx, wy, wx + WIN_W, wy + WIN_H, BG);

        // ── HEADER ──
        ctx.fill(wx, wy, wx + WIN_W, wy + HDR_H, 0xFF050000);
        ctx.fill(wx, wy + HDR_H, wx + WIN_W, wy + HDR_H + 1, ACCENT);

        // Logo
        ctx.drawTextWithShadow(textRenderer, "✦", wx + 8, wy + 6, ACCENT);
        ctx.drawTextWithShadow(textRenderer, "MBEST", wx + 20, wy + 7, ACCENT);
        ctx.drawTextWithShadow(textRenderer, "CLIENT", wx + 55, wy + 7, TEXT);

        // Version + FPS
        String fps = "FPS: " + MBestClient.mc().getCurrentFps();
        ctx.drawTextWithShadow(textRenderer, fps, wx + WIN_W - 80, wy + 7, ACCENT);
        ctx.drawTextWithShadow(textRenderer, "v1.0", wx + WIN_W - 30, wy + 7, TEXT_DIM);

        // ── CATEGORY TABS ──
        Module.Category[] cats = Module.Category.values();
        int tabW = WIN_W / cats.length;
        int tabY = wy + HDR_H + 1;

        for (int i = 0; i < cats.length; i++) {
            Module.Category cat = cats[i];
            int tx = wx + i * tabW;
            boolean sel = cat == selectedCategory;
            boolean hov = mouseX >= tx && mouseX < tx + tabW
                    && mouseY >= tabY && mouseY < tabY + TAB_H;

            // Tab background
            ctx.fill(tx, tabY, tx + tabW, tabY + TAB_H,
                    sel ? TAB_SEL : hov ? 0xFF110000 : TAB_NORM);

            // Bottom accent for selected
            if (sel) {
                ctx.fill(tx + 2, tabY + TAB_H - 2, tx + tabW - 2, tabY + TAB_H, ACCENT);
            }

            // Tab separator
            if (i > 0) ctx.fill(tx, tabY + 4, tx + 1, tabY + TAB_H - 4, SEPARATOR);

            // Count badge
            int count = MBestClient.moduleManager.getByCategory(cat).size();
            String label = cat.displayName.toUpperCase();
            int lx = tx + (tabW - textRenderer.getWidth(label)) / 2;
            ctx.drawTextWithShadow(textRenderer, label, lx, tabY + 10,
                    sel ? ACCENT : hov ? 0xFFFF6666 : TEXT_DIM);
        }

        // ── CARD GRID ──
        int gridY = tabY + TAB_H + 4;
        int gridX = wx + CARD_PAD;
        int cols = (WIN_W - CARD_PAD * 2) / (CARD_W + CARD_PAD);

        List<Module> mods = MBestClient.moduleManager.getByCategory(selectedCategory);

        for (int i = 0; i < mods.size(); i++) {
            Module mod = mods.get(i);
            int col = i % cols;
            int row = i / cols;

            int cx = gridX + col * (CARD_W + CARD_PAD);
            int cy = gridY + row * (CARD_H + CARD_PAD);

            if (cy + CARD_H > wy + WIN_H - 20) break;

            boolean hov = mouseX >= cx && mouseX < cx + CARD_W
                    && mouseY >= cy && mouseY < cy + CARD_H;
            boolean on = mod.isEnabled();

            // Card shadow
            ctx.fill(cx + 2, cy + 2, cx + CARD_W + 2, cy + CARD_H + 2, 0x33000000);

            // Card background
            ctx.fill(cx, cy, cx + CARD_W, cy + CARD_H,
                    on ? CARD_ON : hov ? CARD_HOV : CARD_BG);

            // Card border
            if (on) {
                ctx.fill(cx, cy, cx + CARD_W, cy + 1, ACCENT);
                ctx.fill(cx, cy + CARD_H - 1, cx + CARD_W, cy + CARD_H, ACCENT_DIM);
                ctx.fill(cx, cy, cx + 1, cy + CARD_H, ACCENT_DIM);
                ctx.fill(cx + CARD_W - 1, cy, cx + CARD_W, cy + CARD_H, ACCENT_DIM);
            } else {
                ctx.fill(cx, cy, cx + CARD_W, cy + 1, SEPARATOR);
                ctx.fill(cx, cy + CARD_H - 1, cx + CARD_W, cy + CARD_H, SEPARATOR);
                ctx.fill(cx, cy, cx + 1, cy + CARD_H, SEPARATOR);
                ctx.fill(cx + CARD_W - 1, cy, cx + CARD_W, cy + CARD_H, SEPARATOR);
            }

            // Module name
            ctx.drawTextWithShadow(textRenderer, mod.getName(),
                    cx + 8, cy + 8, on ? TEXT : TEXT_DIM);

            // Description (truncate if too long)
            String desc = mod.getDescription();
            if (textRenderer.getWidth(desc) > CARD_W - 16) {
                desc = textRenderer.trimToWidth(desc, CARD_W - 20) + "...";
            }
            ctx.drawTextWithShadow(textRenderer, desc, cx + 8, cy + 20, TEXT_OFF);

            // Status pill
            int pillX = cx + CARD_W - 30;
            int pillY = cy + CARD_H - 16;
            ctx.fill(pillX, pillY, pillX + 24, pillY + 10,
                    on ? 0xFF330000 : 0xFF1A1A1A);
            ctx.fill(pillX, pillY, pillX + 24, pillY + 1,
                    on ? ACCENT : 0xFF333333);
            ctx.drawTextWithShadow(textRenderer,
                    on ? "ON" : "OFF",
                    pillX + (on ? 4 : 2), pillY + 1,
                    on ? ACCENT : TEXT_OFF);

            // FPS Level for FPS Boost card
            if (mod instanceof FPSBoostModule fps && on) {
                String lvl = fps.getLevel().name();
                int lvlColor = switch (fps.getLevel()) {
                    case LOW    -> 0xFF00FF88;
                    case MEDIUM -> 0xFFFFAA00;
                    case HIGH   -> 0xFFFF6600;
                    case ULTRA  -> ACCENT;
                };
                ctx.drawTextWithShadow(textRenderer, "◆ " + lvl, cx + 8, cy + 34, lvlColor);
            }
        }

        // ── BOTTOM BAR ──
        int barY = wy + WIN_H - 16;
        ctx.fill(wx, barY, wx + WIN_W, wy + WIN_H, 0xFF050000);
        ctx.fill(wx, barY, wx + WIN_W, barY + 1, ACCENT_DIM);
        ctx.drawTextWithShadow(textRenderer,
                "CLICK: toggle  |  SCROLL on FPS Boost: change level  |  RSHIFT: close",
                wx + 8, barY + 4, TEXT_OFF);

        super.render(ctx, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int wx = (width - WIN_W) / 2;
        int wy = (height - WIN_H) / 2;

        // Tab clicks
        Module.Category[] cats = Module.Category.values();
        int tabW = WIN_W / cats.length;
        int tabY = wy + HDR_H + 1;
        for (int i = 0; i < cats.length; i++) {
            int tx = wx + i * tabW;
            if (mouseX >= tx && mouseX < tx + tabW
                    && mouseY >= tabY && mouseY < tabY + TAB_H) {
                selectedCategory = cats[i];
                return true;
            }
        }

        // Card clicks
        int gridY = tabY + TAB_H + 4;
        int gridX = wx + CARD_PAD;
        int cols = (WIN_W - CARD_PAD * 2) / (CARD_W + CARD_PAD);
        List<Module> mods = MBestClient.moduleManager.getByCategory(selectedCategory);
        for (int i = 0; i < mods.size(); i++) {
            int col = i % cols;
            int row = i / cols;
            int cx = gridX + col * (CARD_W + CARD_PAD);
            int cy = gridY + row * (CARD_H + CARD_PAD);
            if (mouseX >= cx && mouseX < cx + CARD_W
                    && mouseY >= cy && mouseY < cy + CARD_H) {
                mods.get(i).toggle();
                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double hAmount, double vAmount) {
        int wx = (width - WIN_W) / 2;
        int wy = (height - WIN_H) / 2;
        int tabY = wy + HDR_H + 1;
        int gridY = tabY + TAB_H + 4;
        int gridX = wx + CARD_PAD;
        int cols = (WIN_W - CARD_PAD * 2) / (CARD_W + CARD_PAD);

        List<Module> mods = MBestClient.moduleManager.getByCategory(selectedCategory);
        for (int i = 0; i < mods.size(); i++) {
            int col = i % cols;
            int row = i / cols;
            int cx = gridX + col * (CARD_W + CARD_PAD);
            int cy = gridY + row * (CARD_H + CARD_PAD);
            if (mouseX >= cx && mouseX < cx + CARD_W
                    && mouseY >= cy && mouseY < cy + CARD_H) {
                if (mods.get(i) instanceof FPSBoostModule fps) {
                    FPSBoostModule.BoostLevel[] levels = FPSBoostModule.BoostLevel.values();
                    int cur = fps.getLevel().ordinal();
                    int next = Math.max(0, Math.min(levels.length - 1,
                            cur + (vAmount > 0 ? 1 : -1)));
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
