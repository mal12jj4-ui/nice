package com.example.myclient.mixin;

import com.example.myclient.MyClient;
import com.example.myclient.module.modules.Freecam;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Camera.class)
public abstract class CameraMixin {
    @Shadow protected abstract void setPos(double x, double y, double z);
    @Shadow protected abstract void setRotation(float yaw, float pitch);

    @Inject(method = "update", at = @At("TAIL"))
    private void myclient$update(BlockView area, Entity focusedEntity, boolean thirdPerson,
                                 boolean inverseView, float tickDelta, CallbackInfo ci) {
        Freecam fc = MyClient.modules == null ? null : MyClient.modules.get(Freecam.class);
        if (fc != null && fc.isEnabled()) {
            fc.updateCamera();
            setPos(fc.x, fc.y, fc.z);
            setRotation(fc.yaw, fc.pitch);
        }
    }

    // Makes your own player model render while in Freecam
    @Inject(method = "isThirdPerson", at = @At("HEAD"), cancellable = true)
    private void myclient$thirdPerson(CallbackInfoReturnable<Boolean> cir) {
        Freecam fc = MyClient.modules == null ? null : MyClient.modules.get(Freecam.class);
        if (fc != null && fc.isEnabled() && fc.showPlayer.get()) cir.setReturnValue(true);
    }
}
