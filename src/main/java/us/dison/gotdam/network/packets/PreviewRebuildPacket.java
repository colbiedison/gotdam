package us.dison.gotdam.network.packets;

import io.netty.buffer.Unpooled;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import us.dison.gotdam.client.GotDamClient;
import us.dison.gotdam.network.BasePacket;

public class PreviewRebuildPacket extends BasePacket {

    public PreviewRebuildPacket(PacketByteBuf buf) {
    }

    public PreviewRebuildPacket() {
        PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
        data.writeInt(this.getPacketID());
        this.configureWrite(data);
    }

    @Override
    public void handleOnClient(PlayerEntity player) {
        MinecraftClient.getInstance().execute(GotDamClient::onChangeDimension);
    }
}
