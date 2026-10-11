package com.example.myclient.module.modules;

import com.example.myclient.module.Category;
import com.example.myclient.module.Module;
import com.example.myclient.setting.ModeSetting;
import com.example.myclient.setting.NumberSetting;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

/** Needs an elytra already equipped and firework rockets in your hotbar. */
public class AutoElytraFly extends Module {
    public final NumberSetting altitude = add(new NumberSetting("Altitude", "Y level to cruise at", 150, -64, 320, 1));
    public final ModeSetting heading = add(new ModeSetting("Heading", "Direction to fly", "Current", "Current", "North", "South", "East", "West"));
    public final NumberSetting minSpeed = add(new NumberSetting("Min Speed", "Use a firework when slower than this (blocks/s)", 30, 10, 60, 1));
    public final NumberSetting boostInterval = add(new NumberSetting("Boost Interval", "Minimum ticks between fireworks", 40, 10, 200, 5));

    private float yawLock;
    private int boostTimer;
    private boolean warnedNoFireworks;

    public AutoElytraFly() {
        super("Auto Elytra Fly", "Automatically flies your elytra to search for bases", Category.DONUT);
    }

    @Override
    protected void onEnable() {
        warnedNoFireworks = false;
        boostTimer = 0;
        if (mc.player == null) return;
        if (!mc.player.getEquippedStack(EquipmentSlot.CHEST).isOf(Items.ELYTRA)) {
            say("Equip an elytra first");
            setEnabled(false);
            return;
        }
        yawLock = switch (heading.get()) {
            case "North" -> 180f;
            case "South" -> 0f;
            case "East" -> -90f;
            case "West" -> 90f;
            default -> mc.player.getYaw();
        };
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null) { setEnabled(false); return; }

        ItemStack chest = mc.player.getEquippedStack(EquipmentSlot.CHEST);
        if (!chest.isOf(Items.ELYTRA)) { say("Elytra no longer equipped"); setEnabled(false); return; }
        if (chest.getMaxDamage() - chest.getDamage() < 15) { say("Elytra almost broken, stopping"); setEnabled(false); return; }

        // Take off
        if (!mc.player.isGliding()) {
            if (mc.player.isOnGround()) {
                mc.player.jump();
            } else if (mc.player.getVelocity().y < 0) {
                mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player,
                        ClientCommandC2SPacket.Mode.START_FALL_FLYING));
            }
            return;
        }

        // Hold heading and altitude
        mc.player.setYaw(yawLock);
        double dy = altitude.get() - mc.player.getY();
        float desired = (float) MathHelper.clamp(-dy * 0.6, -45.0, 25.0);
        mc.player.setPitch(MathHelper.lerp(0.15f, mc.player.getPitch(), desired));

        // Firework boost when slow
        boostTimer++;
        Vec3d v = mc.player.getVelocity();
        double speedPerSecond = Math.hypot(v.x, v.z) * 20.0;
        if (boostTimer >= boostInterval.intValue() && speedPerSecond < minSpeed.get()) {
            if (useFirework()) boostTimer = 0;
        }
    }

    private boolean useFirework() {
        int slot = -1;
        for (int i = 0; i < 9; i++) {
            if (mc.player.getInventory().getStack(i).isOf(Items.FIREWORK_ROCKET)) { slot = i; break; }
        }
        if (slot < 0) {
            if (!warnedNoFireworks) { say("No fireworks in your hotbar"); warnedNoFireworks = true; }
            return false;
        }
        int previous = mc.player.getInventory().selectedSlot;
        mc.player.getInventory().selectedSlot = slot;
        mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
        mc.player.getInventory().selectedSlot = previous;
        return true;
    }

    private void say(String msg) {
        if (mc.player != null) mc.player.sendMessage(Text.literal("\u00a76[Auto Elytra Fly] \u00a7f" + msg), false);
    }
}
