package us.dison.gotdam.scan;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.Unpooled;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.PacketByteBuf;

public class Dam {

    public static final Dam EMPTY = new Dam(-1, DamScanResult.EMPTY);
    public static final Codec<Dam> CODEC = RecordCodecBuilder.create(damInstance ->
            damInstance.group(
                    Codec.INT.fieldOf("id").forGetter(Dam::getID),
                    DamScanResult.CODEC.fieldOf("scan").forGetter(Dam::getScan)
            ).apply(damInstance, Dam::new)
    );

    private final int id;
    private final DamScanResult scan;

    public Dam(int id, DamScanResult scanResult) {
        this.id = id;
        this.scan = scanResult;
    }

    public static Dam fromPacket(PacketByteBuf p) {
        return CODEC.parse(NbtOps.INSTANCE, p.readNbt())
                .result()
                .orElseThrow();
    }

    public int getID() {
        return id;
    }

    public DamScanResult getScan() {
        return scan;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Dam dam)
            if (dam.getID() == getID())
                return true;

        return false;
    }
}
