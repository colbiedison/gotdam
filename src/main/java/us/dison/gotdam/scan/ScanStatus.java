package us.dison.gotdam.scan;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public enum ScanStatus implements ICodecProvider {
    SUCCESS("Success"),
    FAIL("Failed"),
    TOO_BIG("Too big"),
    INTERRUPTED("Interrupted"),
    NOT_RUN_YET("Not run yet")
    ;

    public static final Codec<ScanStatus> CODEC = RecordCodecBuilder.create(scanStatusInstance ->
            scanStatusInstance.group(
                    Codec.INT.fieldOf("ordinal").forGetter(ScanStatus::ordinal)
            ).apply(scanStatusInstance, ScanStatus::fromOrdinal)
    );

    private String message;

    ScanStatus(String message) {
        this.message = message;
    }

    public static ScanStatus fromOrdinal(int o) {
        return ScanStatus.values()[o];
    }

    public boolean isSuccessful() {
        return this == SUCCESS;
    }


    public String getMessage() {
        return message;
    }

    @Override
    public Codec<?> getCodec() {
        return CODEC;
    }
}
