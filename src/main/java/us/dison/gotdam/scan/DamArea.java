package us.dison.gotdam.scan;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class DamArea extends AbstractArea {

    public static final DamArea EMPTY = new DamArea(ServerWorld.OVERWORLD.getValue(), BlockPos.ORIGIN, new ArrayList<>(), 0);
    public static final Codec<DamArea> CODEC = RecordCodecBuilder.create(damAreaInstance ->
            damAreaInstance.group(
                    Identifier.CODEC.fieldOf("world").forGetter(DamArea::getWorldID),
                    BlockPos.CODEC.fieldOf("controllerPos").forGetter(DamArea::getControllerPos),
                    Codec.list(Codec.LONG).fieldOf("blocks").forGetter(DamArea::getInnerBlocks),
                    Codec.INT.fieldOf("topLevel").forGetter(DamArea::getTopLevel)
            ).apply(damAreaInstance, DamArea::new)
    );

    private int topLevel;

    public DamArea(Identifier world, BlockPos controllerPos) {
        this(world, controllerPos, new ArrayList<>(), 0);
    }

    public DamArea(Identifier world, BlockPos controllerPos, List<Long> blocks, int topLevel) {
        super(world, controllerPos, blocks);
        this.topLevel = topLevel;
    }

    public int getTopLevel() {
        return topLevel;
    }

    public void setTopLevel(int topLevel) {
        this.topLevel = topLevel;
    }

    @Override
    public Codec<DamArea> getCodec() {
        return CODEC;
    }
}
