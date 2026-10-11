package com.example.myclient.mixin;

import com.example.myclient.MyClient;
import com.example.myclient.module.modules.FarmFpsOptimizer;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ParticleManager.class)
public class ParticleManagerMixin {
    @Inject(method = "addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)Lnet/minecraft/client/particle/Particle;",
            at = @At("HEAD"), cancellable = true, require = 0)
    private void myclient$addParticle(ParticleEffect parameters, double x, double y, double z,
                                      double vx, double vy, double vz, CallbackInfoReturnable<Particle> cir) {
        FarmFpsOptimizer f = MyClient.modules == null ? null : MyClient.modules.get(FarmFpsOptimizer.class);
        if (f == null || !f.isEnabled() || !f.explosions.get()) return;
        if (parameters.getType() == ParticleTypes.EXPLOSION || parameters.getType() == ParticleTypes.EXPLOSION_EMITTER) {
            cir.setReturnValue(null);
        }
    }
}
