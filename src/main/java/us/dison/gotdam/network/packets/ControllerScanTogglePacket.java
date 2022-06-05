package us.dison.gotdam.network.packets;

import io.netty.buffer.Unpooled;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import us.dison.gotdam.blockentity.ControllerBlockEntity;
import us.dison.gotdam.network.BasePacket;

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
