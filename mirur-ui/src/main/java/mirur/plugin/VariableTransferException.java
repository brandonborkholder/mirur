package mirur.plugin;

@SuppressWarnings("serial")
public class VariableTransferException extends RuntimeException {
    public static final String ERR_Invalid_Jvm_Version = "Target JVM needs to be at least 1.5 to install Mirur agent";
    public static final String ERR_Could_Not_Write_Agent_Classes = "Could not write Mirur agent classes";
    public static final String ERR_Could_Not_Load_Agent_In_Classloader = "Could load Mirur agent class in an isolated ClassLoader";
    public static final String ERR_Exception_in_Agent_Install = "Unexpected exception in installing Mirur agent class";
    public static final String ERR_URLClassLoader_Not_Found = "java.net.URLClassLoader not found on remote vm";

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
