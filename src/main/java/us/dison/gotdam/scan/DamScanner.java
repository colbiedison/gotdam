package us.dison.gotdam.scan;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import us.dison.gotdam.GotDam;
import us.dison.gotdam.blockentity.ControllerBlockEntity;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DamScanner extends AbstractScanner {

    public static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();
    private static volatile boolean shouldStop = false;

    private final ControllerBlockEntity controller;

    public DamScanner(ServerWorld world, ControllerBlockEntity controller, BlockPos startPos) {
        super(world, controller, startPos);
        this.controller = controller;
    }

    public static void stop() {
        shouldStop = true;
    }

    private boolean isLevelValid(int y) {
        BlockPos pos = new BlockPos(startPos.getX(), y, startPos.getZ());
        ArrayList<Long> blocks = new ArrayList<>();
        if (!world.getBlockState(pos).isAir()) return false;
        ArrayList<BlockPos> queue = new ArrayList<>();
        queue.add(pos);
        while (!queue.isEmpty()) {
            if (shouldStop) return false;
            BlockPos p = queue.remove(0);
            if (!world.getBlockState(p).isAir() || blocks.contains(p.asLong()))
                continue;
            blocks.add(p.asLong());
            queue.add(p.add( 0,  0,  1));
            queue.add(p.add( 0,  0, -1));
            queue.add(p.add( 1,  0,  0));
            queue.add(p.add(-1,  0,  0));

            if (blocks.size() > 64*64) return false;
        }

        return true;
    }

    private int findTopLevel(int y) {
        while (isLevelValid(y)) y++;
        return y-1;
    }

    @Override
    public TypedScanResult<DamArea> scan() {
        if (world.isClient) throw new UnsupportedOperationException("Scanning is not allowed on the client.");
        shouldStop = false;
        DamArea area = new DamArea(controller.getPos());

        if (!world.getBlockState(startPos).isAir()) return TypedScanResult.fail(DamArea.EMPTY);
        int topLevel = findTopLevel(startPos.getY());
        GotDam.LOGGER.info("Top level: "+topLevel);
        ArrayList<BlockPos> queue = new ArrayList<>();
        queue.add(new BlockPos(startPos.getX(), topLevel, startPos.getZ()));
        while (!queue.isEmpty()) {
            if (shouldStop) return TypedScanResult.interrupted(DamArea.EMPTY);
            BlockPos p = queue.remove(0);
            if (!world.getBlockState(p).isAir() || area.innerBlocks.contains(p.asLong()))
                continue;
            area.innerBlocks.add(p.asLong());
            queue.add(p.add( 0,  0,  1));
            queue.add(p.add( 0,  0, -1));
            queue.add(p.add( 1,  0,  0));
            queue.add(p.add(-1,  0,  0));
            queue.add(p.add( 0, -1,  0));

            if (area.innerBlocks.size() > 64*64*32) return TypedScanResult.tooBig(DamArea.EMPTY);
        }

        return TypedScanResult.success(area);
    }

    @Override
    public ControllerBlockEntity getController() {
        return this.controller;
    }
}
