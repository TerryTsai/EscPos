EscPos Library for Java
=
A basic Java ESC/POS implementation for receipt printers.

 - `EscPosBuilder` provides a fluent style api for preparing ESC/POS data.
 - `SerialFactory` is a type-safe solution for obtaining SerialPort connections
on any platform.
 - `Printer` and `PrinterDispatcher` facilitates multiple print jobs to multiple printers.


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

Printer Usage
=
```java
Printer printer = new JSerialFactoryPrinter("COM3", SerialConfig.CONFIG_9600_8N1()) {
PrintJob job = new PrintJob(data);

printer.print(job);
```

PrinterDispatcher Usage
=
```java
PrinterDispatcher dispatcher = new PrinterDispatcherThreadsafe();
dispatcher.registerPrinter("Printer1", printer1);
dispatcher.registerPrinter("Printer2", printer2);
dispatcher.registerPrinter("Printer3", printer3);

dispatcher.requestPrint("Printer1", PrintJob.of(data));
dispatcher.requestPrint("Printer2", PrintJob.of(data));
dispatcher.requestPrint("Printer3", PrintJob.of(data));
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

**raw(rxtxVal);**
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

**cut(Cut cut);**
Cuts the paper:
 - Cut.FULL
 - Cut.PART

