package us.dison.gotdam.scan;

import net.minecraft.block.FernBlock;
import net.minecraft.block.PlantBlock;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import us.dison.gotdam.GotDam;
import us.dison.gotdam.blockentity.ControllerBlockEntity;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DamScanner extends AbstractScanner {

    public static final int MAX_SIZE = 64*64*32;

    private volatile boolean shouldStop = false;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final ControllerBlockEntity controller;

    private BlockPos lastValidPos = null;

    public DamScanner(ServerWorld world, ControllerBlockEntity controller, BlockPos startPos) {
        super(world, controller, startPos);
        this.controller = controller;
    }

    public void stop() {
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
            if (!world.getBlockState(p).isAir() || world.getBlockState(p).getBlock() instanceof PlantBlock || blocks.contains(p.asLong()))
                continue;
            blocks.add(p.asLong());
            queue.add(p.offset(Direction.NORTH));
            queue.add(p.offset(Direction.EAST));
            queue.add(p.offset(Direction.SOUTH));
            queue.add(p.offset(Direction.WEST));

            randomParticle(p);

            if (blocks.size() > 64*64) return false;
        }

        return true;
    }

    private int findTopLevel(int y) {
        while (isLevelValid(y)) y++;
        return y-1;
    }

    @Override
    public DamScanResult scan() {
        if (world.isClient) throw new UnsupportedOperationException("Scanning is not allowed on the client.");
        shouldStop = false;
        DamArea area = new DamArea(world.getRegistryKey().getValue(), controller.getPos());

        if (!world.getBlockState(startPos).isAir())
            return DamScanResult.fail(DamArea.EMPTY);
        int topLevel = findTopLevel(startPos.getY());
        area.setTopLevel(topLevel);
        GotDam.LOGGER.info("Top level: "+topLevel);
        ArrayList<BlockPos> queue = new ArrayList<>();
        queue.add(new BlockPos(startPos.getX(), topLevel, startPos.getZ()));
        while (!queue.isEmpty()) {
            if (shouldStop)
                return DamScanResult.interrupted(DamArea.EMPTY);
            BlockPos p = queue.remove(0);
            if (!world.getBlockState(p).isAir() && !(world.getBlockState(p).getBlock() instanceof PlantBlock) || area.innerBlocks.contains(p.asLong())) {
                continue;
            }
            area.innerBlocks.add(p.asLong());
            queue.add(p.offset(Direction.NORTH));
            queue.add(p.offset(Direction.EAST));
            queue.add(p.offset(Direction.SOUTH));
            queue.add(p.offset(Direction.WEST));
            queue.add(p.offset(Direction.DOWN));

            randomParticle(p);

            if (area.innerBlocks.size() > MAX_SIZE)
                return DamScanResult.tooBig(DamArea.EMPTY);
            controller.setScanProgress(100d * area.innerBlocks.size() / MAX_SIZE);
        }

        return DamScanResult.success(area);
    }

    public void randomParticle(BlockPos p) {
        if (Math.random() >= 0.75d)
            world.spawnParticles(ParticleTypes.INSTANT_EFFECT, p.getX()+0.5d, p.getY()+0.5d, p.getZ()+0.5d, 1, 0.5d, 0.5d, 0.5d, 1);
    }

    @Override
    public ControllerBlockEntity getController() {
        return this.controller;
    }


    public ExecutorService getExecutor() {
        return executor;
    }
}
