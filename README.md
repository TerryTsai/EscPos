# EscPos
EscPos Library for Java

Basic Usage :
```java
    // Build the EscPos data
    EscPosBuilder escPos = new EscPosBuilder();
    byte[] data = escPos.initialize()
            .font(Font.EMPHASIZED)
            .align(Align.CENTER)
            .text("HELLO WORLD")
            .feed(5)
            .cut(Cut.PART)
            .getBytes();

    // Open SerialPort and write
    int timeout = 2000;
    SerialConfig config = SerialConfig.CONFIG_8N1(Baud.BAUD_19200);
    port = ComUtils.connectSerialPort("COM18", timeout, config);
    port.getOutputStream().write(data);
```
