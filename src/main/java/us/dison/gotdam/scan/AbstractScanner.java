package us.dison.gotdam.scan;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;


public abstract class AbstractScanner {

    protected final ServerWorld world;
    protected final BlockPos startPos;

    public AbstractScanner(ServerWorld world, BlockEntity controller, BlockPos startPos) {
        this.world = world;
        this.startPos = startPos;
    }

    public abstract AbstractScanResult<? extends AbstractArea> scan();


    public ServerWorld getWorld() {
        return world;
    }

    public abstract BlockEntity getController();

    public BlockPos getStartPos() {
        return startPos;
    }


}
