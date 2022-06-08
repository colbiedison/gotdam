package us.dison.gotdam.scan;

import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

public class DamArea extends AbstractArea {

    public static final DamArea EMPTY = new DamArea(BlockPos.ORIGIN, new ArrayList<>());

    public DamArea(BlockPos controllerPos) {
        this(controllerPos, new ArrayList<>());
    }

    public DamArea(BlockPos controllerPos, ArrayList<Long> blocks) {
        super(controllerPos, blocks);
    }
}
