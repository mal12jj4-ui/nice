package com.example.myclient.mixin;

import com.example.myclient.MyClient;
import com.example.myclient.module.modules.Freecam;
import net.minecraft.client.input.Input;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.util.PlayerInput;
import net.minecraft.util.math.Vec2f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public class KeyboardInputMixin extends Input {
    // Stops your real player from walking around while the camera is detached
    @Inject(method = "tick", at = @At("TAIL"))
    private void myclient$tick(CallbackInfo ci) {
        Freecam fc = MyClient.modules == null ? null : MyClient.modules.get(Freecam.class);
        if (fc != null && fc.isEnabled()) {
            this.playerInput = PlayerInput.DEFAULT;
            this.movementVector = Vec2f.ZERO;
        }
    }
}
