package us.dison.gotdam.network.packets;

import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import us.dison.gotdam.GotDam;
import us.dison.gotdam.block.ControllerBlock;
import us.dison.gotdam.blockentity.ControllerBlockEntity;
import us.dison.gotdam.network.BasePacket;
import us.dison.gotdam.scan.DamArea;
import us.dison.gotdam.scan.DamScanner;
import us.dison.gotdam.scan.TypedScanResult;

public class ControllerScanTogglePacket extends BasePacket {
    public final BlockPos pos;
    public final boolean state;

    public ControllerScanTogglePacket(PacketByteBuf buf) {
        this.pos = buf.readBlockPos();
        this.state = buf.readBoolean();
    }

    public ControllerScanTogglePacket(BlockPos pos, boolean state) {
        this.pos = pos;
        this.state = state;

        PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
        data.writeInt(this.getPacketID());
        data.writeBlockPos(pos);
        data.writeBoolean(state);
        this.configureWrite(data);
    }

    @Override
    public void handleOnServer(ServerPlayerEntity player) {
        if (player.world instanceof ServerWorld world) {
            world.getServer().execute(() -> {
                if (world.getBlockEntity(this.pos) instanceof ControllerBlockEntity controller) {
                    controller.setScanning(state);
                    if (state) {
                        DamScanner scanner = new DamScanner(
                                world,
                                controller,
                                controller.getPos().offset(world.getBlockState(pos).get(ControllerBlock.FACING))
                        );

                        DamScanner.EXECUTOR.execute(() -> {
                            TypedScanResult<DamArea> result = scanner.scan();
                            if (result.getResult().isSuccessful())
                                GotDam.LOGGER.info("Found " + result.getData().getInnerBlocks().size() + " blocks.");
                            else
                                GotDam.LOGGER.info("Scan failed.");
                        });
                    } else {
                        DamScanner.stop();
                        GotDam.LOGGER.info("Stopped scan");
                    }
                }
            });
        }
    }

//    @Override
//    public void handleOnClient(PlayerEntity player) {
//        if (player.world instanceof ClientWorld world) {
//            MinecraftClient.getInstance().execute(() -> {
//                if (world.getBlockEntity(this.pos) instanceof ControllerBlockEntity controller) {
//                    controller.setScanning(state);
//                }
//            });
//        }
//    }
}
