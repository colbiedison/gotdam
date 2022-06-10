package us.dison.gotdam.network.packets;

import io.netty.buffer.Unpooled;
import net.minecraft.network.NetworkSide;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import us.dison.gotdam.blockentity.ControllerBlockEntity;
import us.dison.gotdam.data.DamManager;
import us.dison.gotdam.network.BasePacket;
import us.dison.gotdam.scan.Dam;
import us.dison.gotdam.scan.DamScanResult;

public class ControllerPreviewTogglePacket extends BasePacket {
    public final BlockPos pos;
    public final boolean state;

    public ControllerPreviewTogglePacket(PacketByteBuf buf) {
        this.pos = buf.readBlockPos();
        this.state = buf.readBoolean();
    }

    public ControllerPreviewTogglePacket(BlockPos pos, boolean state) {
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
        if (player.world instanceof ServerWorld serverWorld) {
            if (serverWorld.getBlockEntity(pos) instanceof ControllerBlockEntity controller) {
                Dam dam = DamManager.ofWorld(serverWorld).get(controller.getID());
                if (state) {
                    if (dam.getScan().getStatus().isSuccessful())
                        player.networkHandler.sendPacket(new DamPreviewPacket(dam).toPacket(NetworkSide.CLIENTBOUND));
                } else {
                    player.networkHandler.sendPacket(new DamPreviewPacket(new Dam(dam.getID(), DamScanResult.EMPTY)).toPacket(NetworkSide.CLIENTBOUND));
                }
            }
        }
    }
}
