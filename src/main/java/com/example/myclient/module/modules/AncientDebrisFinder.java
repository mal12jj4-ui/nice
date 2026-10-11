package com.example.myclient.module.modules;

import com.example.myclient.module.Category;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;

/** Block ESP preset that only highlights ancient debris. */
public class AncientDebrisFinder extends BlockEsp {
    public AncientDebrisFinder() {
        super("Ancient Debris Finder", "Highlights ancient debris through walls", Category.DONUT);
        getSettings().removeIf(s -> s != range && s != fill); // keep only Range and Fill
    }

    @Override
    protected int colorOf(BlockState s) {
        return s.isOf(Blocks.ANCIENT_DEBRIS) ? 0xFFB5651D : 0;
    }
}
