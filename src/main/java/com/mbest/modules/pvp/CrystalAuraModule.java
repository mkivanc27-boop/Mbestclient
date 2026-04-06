package com.mbest.modules.pvp;

import com.mbest.modules.Module;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class CrystalAuraModule extends Module {

    private long lastPlace = 0;
    private long lastBreak = 0;
    private static final long PLACE_DELAY = 100;
    private static final long BREAK_DELAY = 80;

    public CrystalAuraModule() {
        super("Crystal Aura", "Hold right click to auto place and break crystals", Category.PVP);
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (!isEnabled() || client.player == null || client.world == null) return;

        // Elde crystal var mı?
        boolean hasInMain = client.player.getMainHandStack().getItem() == Items.END_CRYSTAL;
        boolean hasInOff = client.player.getOffHandStack().getItem() == Items.END_CRYSTAL;
        if (!hasInMain && !hasInOff) return;

        // Sağ tık basılı mı?
        boolean rightClick = GLFW.glfwGetKey(
            client.getWindow().getHandle(),
            GLFW.GLFW_MOUSE_BUTTON_RIGHT
        ) == GLFW.GLFW_PRESS;
        if (!rightClick) return;

        long now = System.currentTimeMillis();

        // PLACE
        if (now - lastPlace >= PLACE_DELAY) {
            HitResult hit = client.crosshairTarget;
            if (hit != null && hit.getType() == HitResult.Type.BLOCK) {
                BlockHitResult blockHit = (BlockHitResult) hit;
                BlockPos pos = blockHit.getBlockPos();
                boolean isObsidian = client.world.getBlockState(pos).getBlock() == Blocks.OBSIDIAN;
                boolean isBedrock = client.world.getBlockState(pos).getBlock() == Blocks.BEDROCK;

                if (isObsidian || isBedrock) {
                    BlockPos above = pos.up();
                    if (client.world.getBlockState(above).isAir()) {
                        Hand hand = hasInMain ? Hand.MAIN_HAND : Hand.OFF_HAND;
                        client.interactionManager.interactBlock(
                            client.player,
                            hand,
                            blockHit
                        );
                        lastPlace = now;
                    }
                }
            }
        }

        // BREAK - en yakın crystalı kır
        if (now - lastBreak >= BREAK_DELAY) {
            Box searchBox = client.player.getBoundingBox().expand(6.0);
            List<EndCrystalEntity> crystals = client.world.getEntitiesByClass(
                EndCrystalEntity.class,
                searchBox,
                e -> true
            );

            EndCrystalEntity closest = null;
            double closestDist = Double.MAX_VALUE;

            for (EndCrystalEntity crystal : crystals) {
                double dist = client.player.distanceTo(crystal);
                if (dist < closestDist) {
                    closestDist = dist;
                    closest = crystal;
                }
            }

            if (closest != null) {
                client.interactionManager.attackEntity(client.player, closest);
                client.player.swingHand(Hand.MAIN_HAND);
                lastBreak = now;
            }
        }
    }
}
