package email.com.gmail.ttsai0509.serial.config;

/**
 * Type-safe wrapper for SerialPort Parameters
 */
public class SerialConfig {

    /**
     * Convenience method for 9600/8-N-1 configuration. That is -
     * 8 data bits, no parity bit, 1 stop bit, at 9600 baud.
     *
     * @return 9600/8-N-1 configuration
     */
    public static SerialConfig CONFIG_9600_8N1() {
        return new SerialConfig(
                Baud.BAUD_9600,
                DataBits.DATABITS_8, Parity.PARITY_NONE, StopBits.STOPBITS_1, FlowControl.FLOWCONTROL_NONE
        );
    }

    /**
     * Convenience method for general 8-N-1 configuration. That is -
     * 8 data bits, no parity bit, 1 stop bit.
     *
     * @param baud bits-per-second
     * @return baud/8-N-1 configuration
     */
    public static SerialConfig CONFIG_8N1(Baud baud) {
        return new SerialConfig(
                baud,
                DataBits.DATABITS_8, Parity.PARITY_NONE, StopBits.STOPBITS_1, FlowControl.FLOWCONTROL_NONE
        );
    }



    private final Baud baud;
    private final DataBits dataBits;
    private final StopBits stopBits;
    private final FlowControl flowControl;
    private final Parity parity;

    public SerialConfig(Baud baud, DataBits dataBits, Parity parity, StopBits stopBits, FlowControl flowControl) {

        if (baud == null || dataBits == null || parity == null || stopBits == null || flowControl == null)
            throw new NullPointerException(SerialConfig.class.getSimpleName() + " does not accept null parameters.");

        this.baud = baud;
        this.dataBits = dataBits;
        this.stopBits = stopBits;
        this.flowControl = flowControl;
        this.parity = parity;
    }

    public Baud getBaud() {
        return baud;
    }

    public DataBits getDataBits() {
        return dataBits;
    }

    public StopBits getStopBits() {
        return stopBits;
    }

    public FlowControl getFlowControl() {
        return flowControl;
    }

    public Parity getParity() {
        return parity;
    }

}
