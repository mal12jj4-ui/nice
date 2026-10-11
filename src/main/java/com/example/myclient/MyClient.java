package com.example.myclient;

import com.example.myclient.config.Config;
import com.example.myclient.gui.ClickGuiScreen;
import com.example.myclient.module.ModuleManager;
import com.example.myclient.render.Render3D;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;

public class MyClient implements ClientModInitializer {
    public static ModuleManager modules;
    private static KeyBinding guiKey;

    @Override
    public void onInitializeClient() {
        modules = new ModuleManager();
        modules.init();
        Config.load();
        ClientLifecycleEvents.CLIENT_STOPPING.register(client -> Config.save());

        guiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.myclient.gui", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_SHIFT, "category.myclient"));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            modules.tick(client);
            while (guiKey.wasPressed()) {
                client.setScreen(new ClickGuiScreen());
            }
        });

        // World-space ESP boxes
        WorldRenderEvents.LAST.register(ctx -> {
            if (ctx.matrixStack() == null) return;
            Matrix4f matrix = ctx.matrixStack().peek().getPositionMatrix();
            Vec3d cam = ctx.camera().getPos();
            Render3D.begin();
            modules.render3D(matrix, cam);
            Render3D.end();
        });

        // 2D HUD panels
        HudRenderCallback.EVENT.register((ctx, tickCounter) -> modules.renderHud(ctx));
    }
}
