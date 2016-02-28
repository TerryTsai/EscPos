# EscPos
EscPos Library for Java
=======================
A simple, minimal ESC/Pos implmentation for receipt printers in Java.

Basic Usage
===========
```java
    EscPosBuilder escPos = new EscPosBuilder();
    byte[] data = escPos.initialize()
            .font(Font.EMPHASIZED)
            .align(Align.CENTER)
            .text("HELLO WORLD")
            .feed(5)
            .cut(Cut.PART)
            .getBytes();
```
Although still a WIP, there is a utility class for serial port printers.
```java
    int timeout = 2000;
    SerialConfig config = SerialConfig.CONFIG_8N1(Baud.BAUD_19200);
    SerialPort port = ComUtils.connectSerialPort("COM18", timeout, config);
    port.getOutputStream().write(data);
```

EscPosBuilder Methods
===================

####**initialize();**
Printer initialization command (0x1B40)

####**reset();**
Resets the buffer

####**close();**
Closes the buffer

####**getBytes();**
Returns the current buffer as a byte[] 

####**raw(val);**
Print a raw int, byte or byte[] to the buffer

####**text(String text);**
Print text to the buffer (will not print until **feed(int lines)** is called)

####**feed(int lines);**
Prints the preceding text and feeds the number of lines

###**font(Font font);**
Toggle one of the following fonts:

 - Font.REGULAR
 - Font.DH
 - Font.DW
 - Font.DWDH
 - Font.EMPHASIZED
 - Font.DH_EMPHASIZED
 - Font.DW_EMPHASIZED
 - Font.DWDH_EMPHASIZED

###**align(Align align);**
Toggle text alignment:

 - Align.LEFT
 - Align.CENTER
 - Align.RIGHT

###**cut(Cut cut);**
Cuts the paper:

 - Cut.FULL
 - Cut.PART

ComUtils
=======
ComUtils is a WIP utilty class for connecting to serial printers in a more type-safe manner.
