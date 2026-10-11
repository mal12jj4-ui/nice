package com.example.myclient.mixin;

import com.example.myclient.MyClient;
import com.example.myclient.module.modules.Freecam;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {
    // Redirect mouse look to the freecam instead of the player
    @Inject(method = "changeLookDirection", at = @At("HEAD"), cancellable = true)
    private void myclient$look(double cursorDeltaX, double cursorDeltaY, CallbackInfo ci) {
        if ((Object) this != MinecraftClient.getInstance().player) return;
        Freecam fc = MyClient.modules == null ? null : MyClient.modules.get(Freecam.class);
        if (fc != null && fc.isEnabled()) {
            fc.look(cursorDeltaX, cursorDeltaY);
            ci.cancel();
        }
    }
}
