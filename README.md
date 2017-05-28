EscPos Library for Java
=
A basic Java ESC/POS implementation for receipt printers.

 - `EscPosBuilder` provides a fluent style api for preparing ESC/POS data.
 - `SerialFactory` is a type-safe solution for obtaining SerialPort connections
on any platform.


EscPosBuilder Usage
=
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

SerialFactory Usage
=
```java
SerialPort port = SerialFactory.jSerialPort("COM3", SerialConfig.CONFIG_9600_8N1());

port.openPort();
port.getOutputStream().write(data);
port.closePort();
```

EscPosBuilder Methods
=
**initialize();**
Printer initialization command (0x1B40)

**reset();**
Resets the buffer

**close();**
Closes the buffer

**getBytes();**
Returns the current buffer as a byte[] 

**raw(val);**
Print a raw int, byte or byte[] to the buffer

**text(String text);**
Print text to the buffer (will not print until **feed(int lines)** is called)

**feed(int lines);**
Prints the preceding text and feeds the number of lines

**font(Font font);**
Toggle one of the following fonts:
 - Font.REGULAR
 - Font.DH
 - Font.DW
 - Font.DWDH
 - Font.EMPHASIZED
 - Font.DH_EMPHASIZED
 - Font.DW_EMPHASIZED
 - Font.DWDH_EMPHASIZED

**align(Align align);**
Toggle text alignment:
 - Align.LEFT
 - Align.CENTER
 - Align.RIGHT

**cut(Cut cut, int lines);**
Feeds the number of lines and cuts the paper:
 - Cut.FULL
 - Cut.PART

**kick(DrawerKick kick, int t1Pulse, int t2Pulse);**
Send Drawer Kick pulse to the following pin:
 - DrawerKick.PIN2
 - DrawerKick.PIN5