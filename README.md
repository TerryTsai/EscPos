# EscPos
EscPos Library for Java

Basic Usage :
```java
SerialPort port = null;
OutputStream out = null;
EscPosBuilder escPos = new EscPosBuilder(42);

try {
    port = ComUtils.connectSerialPort("COM18", 2000, SerialConfig.CONFIG_8N1(Baud.BAUD_19200));
    out = port.getOutputStream();
    out.write(escPos.initialize()
                    .text("HELLO WORLD")
                    .feed(5)
                    .cut(Cut.PART)
                    .getBytes()
    );

} catch (NoSuchPortException | PortInUseException | UnsupportedCommOperationException | IOException e) {
    e.printStackTrace();

} finally {
    { // @formatter:off
    try {   out.close(); } catch (Exception e) {}
    try {  port.close(); } catch (Exception e) {}
    try {escPos.close(); } catch (Exception e) {}
    } // @formatter:on
}
```
