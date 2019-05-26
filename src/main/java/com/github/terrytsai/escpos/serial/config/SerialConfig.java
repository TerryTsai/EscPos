package com.github.terrytsai.escpos.serial.config;

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
                DataBits.DATABITS_8, Parity.PARITY_NONE, StopBits.STOPBITS_1, FlowControl.FLOWCONTROL_NONE,
                Timeout.TIMEOUT_NONBLOCKING, 0, 0
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
                DataBits.DATABITS_8, Parity.PARITY_NONE, StopBits.STOPBITS_1, FlowControl.FLOWCONTROL_NONE,
                Timeout.TIMEOUT_NONBLOCKING, 0, 0
        );
    }

    private final Baud baud;
    private final DataBits dataBits;
    private final StopBits stopBits;
    private final FlowControl flowControl;
    private final Parity parity;
    private final Timeout timeout;
    private final int readTimeout;
    private final int writeTimeout;

    public SerialConfig(Baud baud,
                        DataBits dataBits,
                        Parity parity,
                        StopBits stopBits,
                        FlowControl flowControl,
                        Timeout timeout,
                        int readTimeout,
                        int writeTimeout) {
        if (baud == null || dataBits == null || parity == null || stopBits == null || flowControl == null
                || timeout == null || readTimeout < 0 || writeTimeout < 0) {
            throw new IllegalArgumentException(SerialConfig.class.getSimpleName() +
                    " does not accept null or negative values");
        }

        this.baud = baud;
        this.dataBits = dataBits;
        this.stopBits = stopBits;
        this.flowControl = flowControl;
        this.parity = parity;
        this.timeout = timeout;
        this.readTimeout = readTimeout;
        this.writeTimeout = writeTimeout;
    }

    public SerialConfig(Baud baud, DataBits dataBits, Parity parity, StopBits stopBits, FlowControl flowControl) {
        this(baud, dataBits, parity, stopBits, flowControl, Timeout.TIMEOUT_NONBLOCKING, 0, 0);
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

    public Timeout getTimeout() {
        return timeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public int getWriteTimeout() {
        return writeTimeout;
    }

}
