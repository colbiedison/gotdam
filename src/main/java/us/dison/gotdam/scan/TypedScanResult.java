package us.dison.gotdam.scan;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class TypedScanResult<T extends AbstractArea> {

    public final Codec<TypedScanResult<T>> CODEC = null;

    private final ScanStatus status;
    private final T data;

    public TypedScanResult(ScanStatus status, T data) {
        this.status = status;
        this.data = data;
//        CODEC = RecordCodecBuilder.create(typedScanResultInstance ->
//                typedScanResultInstance.group(
//                        ScanStatus.CODEC.fieldOf("status").forGetter(o -> o.getStatus()),
//                        data.getCodec().fieldOf("area").forGetter(o -> o.getData())
//                ).apply(typedScanResultInstance, TypedScanResult::new)
//        );
    }


    public static <T extends AbstractArea> TypedScanResult<T> success(T data) {
        return new TypedScanResult<>(ScanStatus.SUCCESS, data);
    }

    public static <T extends AbstractArea> TypedScanResult<T> fail(T data) {
        return new TypedScanResult<>(ScanStatus.FAIL, data);
    }

    public static <T extends AbstractArea> TypedScanResult<T> tooBig(T data) {
        return new TypedScanResult<>(ScanStatus.TOO_BIG, data);
    }

    public static <T extends AbstractArea> TypedScanResult<T> interrupted(T data) {
        return new TypedScanResult<>(ScanStatus.INTERRUPTED, data);
    }

    public static <T extends AbstractArea> TypedScanResult<T> notRunYet(T data) {
        return new TypedScanResult<>(ScanStatus.NOT_RUN_YET, data);
    }


    public ScanStatus getStatus() {
        return status;
    }

    public T getData() {
        return data;
    }
}
