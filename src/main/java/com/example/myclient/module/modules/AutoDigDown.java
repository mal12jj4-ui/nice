package com.example.myclient.module.modules;

import com.example.myclient.module.Category;
import com.example.myclient.module.Module;
import com.example.myclient.setting.NumberSetting;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class AutoDigDown extends Module {
    public final NumberSetting targetY = add(new NumberSetting("Target Y", "Stops digging at this Y level", -50, -64, 320, 1));

    public AutoDigDown() {
        super("Auto Dig Down", "Automatically digs down to a set Y level", Category.DONUT);
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null || mc.interactionManager == null) { setEnabled(false); return; }
        if (mc.currentScreen != null) return;

        if (mc.player.getY() <= targetY.get() + 0.5) {
            say("Reached Y " + targetY.intValue());
            setEnabled(false);
            return;
        }

        BlockPos feet = mc.player.getBlockPos();
        // Safety: never dig toward lava
        for (int i = 1; i <= 3; i++) {
            if (mc.world.getFluidState(feet.down(i)).isIn(FluidTags.LAVA)) {
                say("Lava below, stopped");
                setEnabled(false);
                return;
            }
        }

        mc.player.setPitch(90f);
        BlockPos below = feet.down();
        if (!mc.world.getBlockState(below).isAir() && mc.world.getFluidState(below).isEmpty()) {
            mc.interactionManager.updateBlockBreakingProgress(below, Direction.UP);
            mc.player.swingHand(Hand.MAIN_HAND);
        }
    }

    private void say(String msg) {
        if (mc.player != null) mc.player.sendMessage(Text.literal("\u00a76[Auto Dig Down] \u00a7f" + msg), false);
    }
}
