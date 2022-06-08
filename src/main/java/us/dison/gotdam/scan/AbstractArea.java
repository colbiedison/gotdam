package us.dison.gotdam.scan;

import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

public abstract class AbstractArea {

    protected final BlockPos controllerPos;
    protected final ArrayList<Long> innerBlocks;

    public AbstractArea(BlockPos controllerPos) {
        this(controllerPos, new ArrayList<>());
    }

    public AbstractArea(BlockPos controllerPos, ArrayList<Long> blocks) {
        this.controllerPos = controllerPos;
        this.innerBlocks = blocks;
    }

    public BlockPos getControllerPos() {
        return controllerPos;
    }

    public ArrayList<Long> getInnerBlocks() {
        return innerBlocks;
    }
}
