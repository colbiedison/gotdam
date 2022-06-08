package us.dison.gotdam.scan;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractArea implements ICodecProvider {

    protected final BlockPos controllerPos;
    protected final List<Long> innerBlocks;

    public AbstractArea(BlockPos controllerPos) {
        this(controllerPos, new ArrayList<>());
    }

    public AbstractArea(BlockPos controllerPos, List<Long> blocks) {
        this.controllerPos = controllerPos;
        this.innerBlocks = blocks;
    }

    public BlockPos getControllerPos() {
        return controllerPos;
    }

    public List<Long> getInnerBlocks() {
        return innerBlocks;
    }

    @Override
    public abstract Codec<?> getCodec();

    public enum AreaType implements StringIdentifiable {
        DAM("dam");

        private final String name;

        AreaType(String name) {
            this.name = name;
        }

        @Override
        public String asString() {
            return name;
        }

        public static AreaType fromName(String name) {
            for (AreaType value : values()) {
                if (value.name.equals(name))
                    return value;
            }
            return null;
        }
    }
}
