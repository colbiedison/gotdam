package us.dison.gotdam.scan;

public class TypedScanResult<T> {
    private final ScanResult result;
    private final T data;

    public TypedScanResult(ScanResult result, T data) {
        this.result = result;
        this.data = data;
    }

    public static <T> TypedScanResult<T> success(T data) {
        return new TypedScanResult<>(ScanResult.SUCCESS, data);
    }

    public static <T> TypedScanResult<T> fail(T data) {
        return new TypedScanResult<>(ScanResult.FAIL, data);
    }

    public static <T> TypedScanResult<T> tooBig(T data) {
        return new TypedScanResult<>(ScanResult.TOO_BIG, data);
    }

    public static <T> TypedScanResult<T> interrupted(T data) {
        return new TypedScanResult<>(ScanResult.INTERRUPTED, data);
    }


    public ScanResult getResult() {
        return result;
    }

    public T getData() {
        return data;
    }
}
