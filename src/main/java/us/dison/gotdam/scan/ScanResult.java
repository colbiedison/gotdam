package us.dison.gotdam.scan;

public enum ScanResult {
    SUCCESS,
    FAIL,
    TOO_BIG,
    INTERRUPTED
    ;

    public boolean isSuccessful() {
        return this == SUCCESS;
    }
}
