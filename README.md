[![CircleCI](https://circleci.com/gh/TerryTsai/EscPos.svg?style=shield)](https://circleci.com/gh/TerryTsai/EscPos)
[![Version](https://img.shields.io/github/tag/TerryTsai/EscPos.svg?label=version&color=success)](https://github.com/TerryTsai/EscPos/tags)
[![JitPack](https://jitpack.io/v/TerryTsai/EscPos.svg)](https://jitpack.io/#TerryTsai/EscPos)
[![License](https://img.shields.io/badge/License-MIT-success.svg)](https://opensource.org/licenses/MIT)


EscPos Library for Java
=
A basic Java ESC/POS implementation for receipt printers.

 - `EscPosWriter` provides a fluent style api for preparing ESC/POS data. Supported commands depend on printer model.
 - `SerialFactory` is a type-safe solution for obtaining SerialPort connections on any platform.

SerialFactory Usage
=
```java
SerialPort port = SerialFactory.com(3, SerialConfig.CONFIG_9600_8N1());

port.openPort();
OutputStream out = port.getOutputStream();
// Use OutputStream
port.closePort();
```

EscPosWriter Usage
=
```java
EscPosWriter escPos = new EscPosWriter(out)
        .initialize()
        .setCharacterCodeTable(CharacterCodeTable.PC437)
        .setJustification(Justification.CENTER)
        .setCharacterSize(Width.X3, Height.X3)
        .setEmphasize(true)
        .text("HELLO WORLD")
        .printAndFeedLines(5)
        .cut(CutA.PARTIAL)
        .sendRealTimeRequestPulse(Pin.TWO, PulseTime.FOUR);
```

EscPosWriter Methods
=

Write Commands
* `text(String text)`
* `bytes(byte[] bytes)`

Print Commands
* `printAndFeedLine()`
* `printAndReturnToStandardMode()`
* `printAndCarriageReturn()`
* `printInPageMode()`
* `printAndFeedPaper(int n)`
* `printAndReverseFeed(int n)`
* `printAndFeedLines(int n)`
* `printAndReverseFeedLines(int n)`

Line Spacing Commands
* `setDefaultLineSpacing()`
* `setLineSpacing(int n)`

Character Commands
* `cancelPrintInPageMode()`
* `setPrintMode(boolean altFont, boolean emphasized, boolean underlined, boolean doubleHeight, boolean doubleWidth)`
* `setRightSideCharacterSpacing(int n)`
* `setUnderline(Underline underline)`
* `setEmphasize(boolean enabled)`
* `setDoubleStrike(boolean enabled)`
* `setFont(Font font)`
* `setCharacterSet(CharacterSet characterSet)`
* `setRotation(Rotation rotation)`
* `setColor(Color color)`
* `setCharacterCodeTable(CharacterCodeTable characterCodeTable)`
* `setUpsideDownPrint(boolean enabled)`
* `setCharacterSize(Width width, Height height)`
* `setWhiteBlackReverse(boolean enabled)`
* `setSmoothing(boolean enabled)`

Print Position Commands
* `horizontalTab()`
* `setPrintDirection(Direction direction)`
* `setJustification(Justification justification)`
* `setLeftMargin(int nL, int nH)`
* `setPrintPositionStart(DataAction action)`
* `setPrintAreaWidth(int nL, int nH)`
* `setAbsolutePosition(int nL, int nH)`
* `setRelativePosition(int nL, int nH)`
* `setAbsoluteVerticalPosition(int nL, int nH)`
* `setRelativeVerticalPosition(int nL, int nH)`

Mechanism Control Commands
* `returnHome()`
* `setUnidirectionalPrint(boolean enabled)`
* `cut(CutA cut)`
* `cutWithFeed(CutB cut, int n)`
* `cutPosition(CutC cut, int n)`
* `cutWithFeedAndReturnStart(CutD cut, int n)`

Panel Button Commands
* `setPanelButtons(boolean enabled)`

Kanji Commands
* `setKanjiPrintMode(boolean underlined, boolean doubleWidth, boolean doubleHeight)`
* `setKanjiCharacterMode()`
* `setKanjiUnderline(Underline underline)`
* `cancelKanjiCharacterMode()`
* `setKanjiCodeSystem(Kanji kanji)`
* `setKanjiCharacterSpacing(int n1, int n2)`
* `setKanjiQuadrupleSize(boolean enabled)`

Miscellaneous Commands
* `initialize()`
* `sendRealTimeRequest(RealTimeRequest realTimeRequest)`
* `sendRealTimeRequestPulse(Pin pin, PulseTime pulseTime)`
* `sendRealTimeRequestPowerOff()`
* `sendRealTimeRequestBuzzer(int a, int n, int r, int t1, int t2)`
* `sendRealTimeRequestStatus(Status status)`
* `sendRealTimeRequestClearBuffers()`
* `setPeripheralDevice(int n)`
* `setPageMode()`
* `setStandardMode()`
* `generatePulse(Pin pin, int t1, int t2)`
* `setMotionUnits(int x, int y)`
