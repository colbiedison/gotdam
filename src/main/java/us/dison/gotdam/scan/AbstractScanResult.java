package us.dison.gotdam.scan;

import com.mojang.serialization.Codec;

public abstract class AbstractScanResult<T extends AbstractArea> implements ICodecProvider {

    private final ScanStatus status;
    private final T data;

    public AbstractScanResult(ScanStatus status, T data) {
        this.status = status;
        this.data = data;
    }


    public ScanStatus getStatus() {
        return status;
    }

    public T getArea() {
        return data;
    }

    @Override
    public abstract Codec<? extends AbstractScanResult<T>> getCodec();
}
