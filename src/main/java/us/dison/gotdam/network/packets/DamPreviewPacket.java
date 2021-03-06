package us.dison.gotdam.network.packets;

import io.netty.buffer.Unpooled;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import us.dison.gotdam.client.GotDamClient;
import us.dison.gotdam.network.BasePacket;
import us.dison.gotdam.scan.Dam;

public class DamPreviewPacket extends BasePacket {
    public final Dam dam;

    public DamPreviewPacket(PacketByteBuf buf) {
        this.dam = Dam.fromPacket(buf);
    }

    public DamPreviewPacket(Dam dam) {
        this.dam = dam;

        PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
        data.writeInt(this.getPacketID());
        data.encode(Dam.CODEC, dam);
        this.configureWrite(data);
    }

    @Override
    public void handleOnClient(PlayerEntity player) {
        if (player.world instanceof ClientWorld clientWorld && clientWorld.getRegistryKey().getValue().equals(dam.getScan().getArea().getWorldID())) {
            GotDamClient.removePreviewDamIf(dam1 -> dam1.equals(dam));
            if (dam.getScan().getArea().getInnerBlocks().size() > 0) {
                GotDamClient.addPreviewDam(dam);
            }
        }
    }
}
