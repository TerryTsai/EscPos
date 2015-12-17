# EscPos
EscPos Library for Java

Sample Usage :
```java
  SerialPort printer = ESCPos.connectSerialPort("COM1");
  OutputStream out = printer.getOutputStream();

  ESCPos.Commander.command(out)
                  .print(ESCPos.Command.INITIALIZE)
                  .print(ESCPos.Command.ALIGN_CENTER)
                  .print("Hello World")
                  .print(ESCPos.Command.FEED_N)
                  .print(10)
                  .print(ESCPos.Command.FULL_CUT);
  out.close();
  printer.close();
```
