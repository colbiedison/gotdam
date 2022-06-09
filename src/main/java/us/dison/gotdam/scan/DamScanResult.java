package us.dison.gotdam.scan;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class DamScanResult extends AbstractScanResult<DamArea> {

    public static final DamScanResult EMPTY = notRunYet(DamArea.EMPTY);
    public static final Codec<DamScanResult> CODEC = RecordCodecBuilder.create(damScanResultInstance ->
            damScanResultInstance.group(
                    ScanStatus.CODEC.fieldOf("status").forGetter(DamScanResult::getStatus),
                    DamArea.CODEC.fieldOf("area").forGetter(DamScanResult::getArea)
            ).apply(damScanResultInstance, DamScanResult::new)
    );

    public DamScanResult(ScanStatus status, DamArea area) {
        super(status, area);
    }

    public static DamScanResult success(DamArea area) {
        return new DamScanResult(ScanStatus.SUCCESS, area);
    }

    public static DamScanResult fail(DamArea area) {
        return new DamScanResult(ScanStatus.FAIL, area);
    }

    public static DamScanResult tooBig(DamArea area) {
        return new DamScanResult(ScanStatus.TOO_BIG, area);
    }

    public static DamScanResult interrupted(DamArea area) {
        return new DamScanResult(ScanStatus.INTERRUPTED, area);
    }

    public static DamScanResult notRunYet(DamArea area) {
        return new DamScanResult(ScanStatus.NOT_RUN_YET, area);
    }


    @Override
    public Codec<DamScanResult> getCodec() {
        return CODEC;
    }
}
