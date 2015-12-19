# EscPos
EscPos Library for Java

Basic Usage :
```java
    // Build the EscPos data
    EscPosBuilder escPos = new EscPosBuilder();
    byte[] data = escPos.initialize()
            .text("HELLO WORLD")
            .feed(5)
            .cut(Cut.PART)
            .getBytes();

    // Open SerialPort and write
    port = ComUtils.connectSerialPort("COM18", 2000, SerialConfig.CONFIG_8N1(Baud.BAUD_19200));
    port.getOutputStream().write(data);
```
