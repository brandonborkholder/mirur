package mirur.plugin;

@SuppressWarnings("serial")
public class VariableTransferException extends RuntimeException {
    public static final String ERR_Invalid_Jvm_Version = "Target JVM needs to be at least 1.5 to install Mirur agent";

    public VariableTransferException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public VariableTransferException(String message, Throwable cause) {
        super(message, cause);
    }

    public VariableTransferException(String message) {
        super(message);
    }

    public VariableTransferException(Throwable cause) {
        super(cause);
    }
}
