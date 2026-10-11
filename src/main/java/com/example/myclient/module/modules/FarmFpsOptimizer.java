package com.example.myclient.module.modules;

import com.example.myclient.module.Category;
import com.example.myclient.module.Module;
import com.example.myclient.setting.BoolSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.FallingBlockEntity;

public class FarmFpsOptimizer extends Module {
    public final BoolSetting xpOrbs = add(new BoolSetting("XP Orbs", "Hides experience orbs", true));
    public final BoolSetting fallingBlocks = add(new BoolSetting("Falling Blocks", "Hides falling sand, gravel and concrete powder", true));
    public final BoolSetting tnt = add(new BoolSetting("TNT", "Hides activated TNT", true));
    public final BoolSetting explosions = add(new BoolSetting("Explosions", "Hides explosion particles", true));

    public FarmFpsOptimizer() {
        super("Farm FPS Optimizer", "Reduces lag around farms", Category.DONUT);
    }

    public boolean hides(Entity e) {
        return (xpOrbs.get() && e instanceof ExperienceOrbEntity)
                || (fallingBlocks.get() && e instanceof FallingBlockEntity)
                || (tnt.get() && e instanceof TntEntity);
    }
}
