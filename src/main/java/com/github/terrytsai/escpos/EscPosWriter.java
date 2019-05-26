package com.github.terrytsai.escpos;

import com.github.terrytsai.escpos.enums.*;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;

/**
 * Fluent style api for ESC/POS
 */
@SuppressWarnings({"unused", "SameParameterValue"})
public class EscPosWriter {

    private static final int ENQ = 5;
    private static final int HT = 9;
    private static final int LF = 10;
    private static final int FF = 12;
    private static final int CR = 13;
    private static final int DLE = 16;
    private static final int DC4 = 20;
    private static final int CAN = 24;
    private static final int ESC = 27;
    private static final int FS = 28;
    private static final int GS = 29;
    private static final int SP = 32;

    private final OutputStream out;

    /**
     * Constructor
     *
     * @param out OutputStream
     */
    public EscPosWriter(OutputStream out) {
        this.out = out;
    }

    ///////////////////////////////////////////////
    //
    // Write Commands
    //
    ///////////////////////////////////////////////

    /**
     * Sends a string to the printer.
     *
     * @param text string
     * @return {@link EscPosWriter}
     */
    public EscPosWriter text(String text) {
        return bytes(text.getBytes());
    }

    /**
     * Sends a raw byte array to the printer.
     *
     * @param bytes byte array
     * @return {@link EscPosWriter}
     */
    public EscPosWriter bytes(byte[] bytes) {
        try {
            for (byte v : bytes) {
                out.write(v);
            }
            return this;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }


    ///////////////////////////////////////////////
    //
    // Print Commands
    //
    ///////////////////////////////////////////////

    /**
     * Prints the data in the print buffer and feeds one line, based on the current line spacing.
     * <p>
     * [Notes]
     * - The amount of paper fed per line is based on the value set using the line spacing command (ESC 2 or ESC 3).
     * - After printing, the print position is moved to left side of the printable area. Also, the printer is in the status "beginning of the line".
     * - When this command is processed in Page mode, only the print position moves, and the printer does not perform actual printing
     *
     * @return {@link EscPosWriter}
     */
    public EscPosWriter printAndFeedLine() {
        return write(LF);
    }

    /**
     * In Page mode, prints the data in the print buffer collectively.
     * <p>
     * [Notes]
     * - This command is enabled only in Page mode. Page mode can be selected by ESC L.
     * - After printing, the printer does not clear the buffered data, the print position, or values set by other commands.
     * - The printer returns to Standard mode with FF, ESC S, and ESC @. When it returns to Standard mode by ESC @, all settings are canceled.
     * - This command is used when the data in Page mode is printed repeatedly.
     *
     * @return {@link EscPosWriter}
     */
    public EscPosWriter printAndReturnToStandardMode() {
        return write(FF);
    }

    /**
     * Executes one of the following operations (Alignment, Auto Line Feed):
     * - Horizontal, Enabled - Executes printing and one line feed as LF
     * - Horizontal, Disabled - This command is ignored.
     * - Vertical, Enabled - Executes printing and one line feed as LF
     * - Vertical, Disabled - In Standard mode, prints the data in the print buffer and moves the print position to the
     * beginning of the print line. In Page mode, moves the print position to the beginning of the print line.
     * <p>
     * [Notes]
     * - With a serial interface, the command performs as if auto line feed is disabled.
     * - Enabling or disabling the auto line feed can be selected by the DIP switch or the memory switch. Memory switch can be changed with GS ( E <Function 3>;.
     * - After printing, the print position is moved to left side of the printable area. Also, the printer is in the status "beginning of the line".
     * - When this command is processed in Page mode, only the print position moves, and the printer does not perform actual printing.
     *
     * @return {@link EscPosWriter}
     */
    public EscPosWriter printAndCarriageReturn() {
        return write(CR);
    }

    /**
     * In Page mode, prints all the data in the print buffer collectively and switches from Page mode to Standard mode.
     * <p>
     * [Notes]
     * - This command is enabled only in Page mode. See FF (in Standard mode) to use this command in Standard mode. Page mode can be selected by ESC L.
     * - The data is deleted in the print area after being printed.
     * - This command returns the values set by ESC W to the default values.
     * - The value set by ESC T is maintained.
     * - After printing, the printer returns to Standard mode and moves the print position to left side of the printable area. Also, the printer is in the status "beginning of the line".
     *
     * @return {@link EscPosWriter}
     */
    public EscPosWriter printInPageMode() {
        return write(ESC, FF);
    }

    /**
     * Prints the data in the print buffer and feeds the paper [n × (vertical or horizontal motion unit)].
     * <p>
     * [Notes]
     * - When Standard mode is selected, the vertical motion unit is used.
     * - When Page mode is selected, the vertical or horizontal motion unit is used for the print direction set by ESC T.
     * - When the starting position is set to the upper left or lower right of the print area using ESC T, the vertical motion unit is used.
     * - When the starting position is set to the upper right or lower left of the print area using ESC T, the horizontal motion unit is used.
     * - The maximum paper feed amount depends on the printer model. If specified over the maximum amount, the maximum paper feed is executed.
     * - After printing, the print postion is moved to left side of the printable area. Also, the printer is in the status "beginning of the line".
     * - When this command is processed in Page mode, only the print position moves; the printer does not perform actual printing.
     * - This command is used to temporarily feed a specific length without changing the line spacing set by other commands.
     *
     * @param n vertical or horizontal motion units (0-255)
     * @return {@link EscPosWriter}
     */
    public EscPosWriter printAndFeedPaper(int n) {
        return write(ESC, 'J', n);
    }

    /**
     * Prints the data in the print buffer and feeds the paper n × (vertical or horizontal motion unit) in the reverse direction.
     * <p>
     * [Notes]
     * - When Standard mode is selected, the vertical motion unit is used.
     * - when Page mode is selected, the vertical or horizontal motion unit is used for the print direction set by ESC T.
     * - When the starting position is set to the upper left or lower right of the print area using ESC T, the vertical motion unit is used.
     * - When the starting position is set to the upper right or lower left of the print area using ESC T, the horizontal motion unit is used.
     * - When this command is processed in Page mode, only the print position moves; the printer does not perform actual printing.
     * - After printing, the print postion is moved to left side of the printable area. Also, the printer is in the status "beginning of the line".
     * - The maximum paper feed amount depends on the printer model. If specified over the maximum amount, the reverse feed is not executed although the print is executed.
     * - This command is used to temporarily feed a specific length without changing the line spacing set by other commands.
     * - Some printers execute a little forward paper feed after reverse feed, from a restriction of the printer mechanism.
     *
     * @param n vertical or horizontal motion units (0-255)
     * @return {@link EscPosWriter}
     */
    public EscPosWriter printAndReverseFeed(int n) {
        return write(ESC, 'K', n);
    }

    /**
     * Prints the data in the print buffer and feeds n lines.
     * <p>
     * [Notes]
     * - The amount of paper fed per line is based on the value set using the line spacing command (ESC 2 or ESC 3).
     * - The maximum paper feed amount depends on the printer model. If specified over the maximum amount, the maximum paper feed is executed.
     * - After printing, the print postion is moved to left side of the printable area. Also, the printer is in the status "beginning of the line".
     * - When this command is processed in Page mode, only the print position moves, and the printer does not perform actual printing.
     * - This command is used to temporarily feed a specific line without changing the line spacing set by other commands.
     *
     * @param n lines (0-255)
     * @return {@link EscPosWriter}
     */
    public EscPosWriter printAndFeedLines(int n) {
        return write(ESC, 'd', n);
    }

    /**
     * Prints the data in the print buffer and feeds n lines in the reverse direction.
     * <p>
     * [Notes]
     * - The amount of paper fed per line is based on the value set using the line spacing command (ESC 2 or ESC 3).
     * - The maximum paper feed amount depends on the printer model. If specified over the maximum amount, the reverse feed is not executed although the print is executed.
     * - After printing, the print postion is moved to left side of the printable area. Also, the printer is in the status "beginning of the line".
     * - When this command is processed in Page mode, only the print position moves, and the printer does not perform actual printing.
     * - This command is used to temporarily feed a specific line without changing the line spacing set by other commands.
     * - Some printers execute a little forward paper feed after reverse feed, from a restriction of the printer mechanism.
     *
     * @param n lines (0-255)
     * @return {@link EscPosWriter}
     */
    public EscPosWriter printAndReverseFeedLines(int n) {
        return write(ESC, 'e', n);
    }

    ///////////////////////////////////////////////
    //
    // Line Spacing Commands
    //
    ///////////////////////////////////////////////

    /**
     * Sets the line spacing to the "default line spacing".
     * <p>
     * [Notes]
     * - The line spacing can be set independently in Standard mode and in Page mode.
     * - In Standard mode this command sets the line spacing of Standard mode.
     * - In Page mode this command sets the line spacing of page mode.
     * - Selected line spacing is effective until ESC 3 is executed, ESC @ is executed, the printer is reset, or the power is turned off.
     *
     * @return {@link EscPosWriter}
     */
    public EscPosWriter setDefaultLineSpacing() {
        return write(ESC, '2');
    }

    /**
     * Sets the line spacing to n × (vertical or horizontal motion unit).
     * <p>
     * [Notes]
     * - The maximum line spacing is 1016 mm {40 inches}. However, it may be smaller depending on models.
     * - If the specified amount exceeds the maximum line spacing, the line spacing is automatically set to the maximum.
     * - When Standard mode is selected, the vertical motion unit is used.
     * - when Page mode is selected, the vertical or horizontal motion unit is used for the print direction set by ESC T.
     * - When the starting position is set to the upper left or lower right of the print area using ESC T, the vertical motion unit is used.
     * - When the starting position is set to the upper right or lower left of the print area using ESC T, the horizontal motion unit is used.
     * - The line spacing can be set independently in Standard mode and in Page mode.
     * - In Standard mode this command sets the line spacing of Standard mode.
     * - in Page mode this command sets the line spacing of page mode.
     * - When the motion unit is changed after the line spacing is set, the line spacing setting does not change.
     * - Selected line spacing is effective until ESC 2 is executed, ESC @ is executed, the printer is reset, or the power is turned off.
     *
     * @param n vertical or horizontal motion units (0-255)
     * @return {@link EscPosWriter}
     */
    public EscPosWriter setLineSpacing(int n) {
        return write(ESC, '3', n);
    }

    ///////////////////////////////////////////////
    //
    // Character Commands
    //
    ///////////////////////////////////////////////

    /**
     * Selects the character font and styles (emphasized, double-height, double-width, and underline).
     * <p>
     * [Notes]
     * - Configurations of Font 1 and Font 2 are different, depending on the printer model. If the desired font type cannot be selected with this command, use ESC M.
     * - The settings of font (Bit 0), double-height (Bit 4), double-width (Bit 5) and underline (Bit 7) are effective for 1-byte code characters. On some models, the settings of double-height (Bit 4), double-width (Bit 5) and underline (Bit 7) are effective also for Korean characters.
     * - The emphasized print modes set by this command (Bit 3) is effective for both 1-byte code characters and multi-byte code characters.
     * - Settings of this command are effective until ESC @ is executed, the printer is reset, the power is turned off, or one of the following commands is executed:
     * - Bit 0 (character font): ESC M
     * - Bit 3 (Emphasized mode): ESC E
     * - Bit 4, 5 (character size): GS !
     * - Bit 7 (underline mode): ESC -
     * - When some characters in a line are double-height, all characters on the line are aligned at the baseline.
     * - When double-width mode is turned on, the characters are enlarged to the right, based on the left side of the character.
     * - When both double-height and double-width modes are turned on, quadruple size characters are printed.
     * - In Standard mode, the character is enlarged in the paper feed direction when double-height mode is selected, and it is enlarged perpendicular to the paper feed direction when double-width mode is selected. However, when character orientation changes in 90° clockwise rotation mode, the relationship between double-height and double-width is reversed.
     * - in Page mode, double-height and double-width are on the character orientation.
     * - The underline thickness is that specified by ESC -, regardless of the character size. The underline is the same color as the printed character. The printed character's color is specified by GS ( N <Function 48>.
     * - The following are not underlined.
     * - 90° clockwise-rotated characters
     * - white/black reverse characters
     * - space set by HT, ESC $, and ESC \
     * - On printers that have the Automatic font replacement function, the replaced font with GS ( E <Function 5> (a = 111, 112, 113) is selected by this command.
     *
     * @param altFont      true for Font 1, false for Font 2
     * @param emphasized   enable emphasized font
     * @param underlined   enable underlined font
     * @param doubleHeight enable double-height font
     * @param doubleWidth  enable double-width font
     * @return {@link EscPosWriter}
     */
    public EscPosWriter setPrintMode(boolean altFont, boolean emphasized, boolean underlined,
                                     boolean doubleHeight, boolean doubleWidth) {
        int n = (altFont ? 1 : 0)
                | (emphasized ? 8 : 0)
                | (doubleHeight ? 16 : 0)
                | (doubleWidth ? 32 : 0)
                | (underlined ? 128 : 0);
        return write(ESC, '!', n);
    }

    /**
     * In Page mode, deletes all the print data in the current print area.
     * <p>
     * [Notes]
     * - This command is enabled only in Page mode. Page mode is selected by ESC L.
     * - If data set in the previously specified print area is set in the currently specified print area, it is deleted.
     *
     * @return {@link EscPosWriter}
     */
    public EscPosWriter cancelPrintInPageMode() {
        return write(CAN);
    }

    /**
     * Sets the right-side character spacing to n × (horizontal or vertical motion unit).
     * <p>
     * [Notes]
     * - The character spacing set by this command is effective for alphanumeric, Kana, and user-defined characters.
     * - When characters are enlarged, the character spacing is n times normal value.
     * - When Standard mode is selected, the horizontal motion unit is used.
     * - when Page mode is selected, the vertical or horizontal motion unit is used for the print direction set by ESC T.
     * - When the starting position is set to the upper left or lower right of the print area using ESC T, the horizontal motion unit is used.
     * - When the starting position is set to the upper right or lower left of the print area using ESC T, the vertical motion unit is used.
     * - The character spacing can be set independently in Standard mode and in Page mode.
     * - In Standard mode this command sets the character spacing of Standard mode.
     * - in Page mode this command sets the character spacing of page mode.
     * - If the horizontal or vertical motion unit is changed after this command is executed, the character spacing is not changed.
     * - Settings of this command are effective until ESC @ is executed, the printer is reset, or the power is turned off.
     * - It is used to change the spacing between characters.
     *
     * @param n vertical or horizontal motion units (0-255)
     * @return {@link EscPosWriter}
     */
    public EscPosWriter setRightSideCharacterSpacing(int n) {
        return write(ESC, SP, n);
    }

    /**
     * Turns underline mode off or on with 1-dot/2-dot thickness
     * <p>
     * [Notes]
     * - The underline mode is effective for alphanumeric, Kana, and user-defined characters. On some models, it is effective also for Korean characters.
     * - The color of underline is the same as that of the printing character. The printing character's color is selected by GS ( N <Function 48>.
     * - Changing the character size does not affect the current underline thickness.
     * - When underline mode is turned off, the following data cannot be underlined, but the thickness is maintained.
     * - The printer does not underline 90° clockwise rotated characters, white/black reverse characters, and the space set by HT, ESC $, and ESC \.
     * - Setting of this command is effective until ESC ! is executed, ESC @ is executed, the printer is reset, or the power is turned off.
     * - Some of the printer models support the 2-dot thick underline (n = 2 or 50).
     *
     * @param underline underline mode
     * @return {@link EscPosWriter}
     */
    public EscPosWriter setUnderline(Underline underline) {
        return write(ESC, '-', underline.code);
    }

    /**
     * Turns emphasized mode on or off.
     * <p>
     * [Notes]
     * - This mode is effective for alphanumeric, Kana, multilingual, and user-defined characters.
     * - Settings of this command are effective until ESC ! is executed, ESC @ is executed, the printer is reset, or the power is turned off.
     *
     * @param enabled enable emphasized font
     * @return {@link EscPosWriter}
     */
    public EscPosWriter setEmphasize(boolean enabled) {
        return write(ESC, 'E', enabled ? 1 : 0);
    }

    /**
     * Turns double-strike mode on or off.
     * <p>
     * [Notes]
     * - This mode is effective for alphanumeric, Kana, multilingual, and user-defined characters.
     * - Settings of this command are effective until ESC ! is executed, ESC @ is executed, the printer is reset, or the power is turned off.
     *
     * @param enabled enable double strike font
     * @return {@link EscPosWriter}
     */
    public EscPosWriter setDoubleStrike(boolean enabled) {
        return write(ESC, 'G', enabled ? 1 : 0);
    }

    /**
     * Selects a character font.
     * <p>
     * [Notes]
     * - The character font set by this command is effective for alphanumeric, Kana, and user-defined characters.
     * - Configurations of Font A and Font B depend on the printer model.
     * - Settings of this command are effective until ESC ! is executed, ESC @ is executed, the printer is reset, or the power is turned off.
     * - On the printer that has the Automatic font replacement function, the replaced font with GS ( E <Function 5> (a = 111 or 112) is selected by this command.
     *
     * @param font character font
     * @return {@link EscPosWriter}
     */
    public EscPosWriter setFont(Font font) {
        return write(ESC, 'M', font.code);
    }

    /**
     * Sets an international character set.
     * <p>
     * [Notes]
     * - The selected international character set is effective until ESC @ is executed, the printer is reset, or the power is turned off.
     * - Refer to International Character Sets in Character Code Tables for TM printers for the international characters.
     *
     * @param characterSet character set
     * @return {@link EscPosWriter}
     */
    public EscPosWriter setCharacterSet(CharacterSet characterSet) {
        return write(ESC, 'R', characterSet.code);
    }

    /**
     * In Standard mode, turns 90° clockwise rotation mode on or off for characters.
     * <p>
     * [Notes]
     * - The 90° clockwise rotation mode is effective for alphanumeric, Kana, multilingual, and user-defined characters.
     * - When underline mode is turned on, the printer does not underline 90° clockwise-rotated characters.
     * - When character orientation changes in 90° clockwise rotation mode, the relationship between vertical and horizontal directions is reversed.
     * - The 90° clockwise rotation mode has no effect in Page mode.
     * - Some printer models support 90° clockwise rotation mode when n = 2 or 50.
     * - Some printer models have a font for which 90° clockwise rotation mode is not effective.
     * - Settings of this command are effective until ESC @ is executed, the printer is reset, or the power is turned off.
     *
     * @param rotation rotation
     * @return {@link EscPosWriter}
     */
    public EscPosWriter setRotation(Rotation rotation) {
        return write(ESC, 'V', rotation.code);
    }

    /**
     * Select print color.
     * <p>
     * [Notes]
     * - When Standard mode is selected, this command is enabled only when processed at the beginning of the line.
     * - when Page mode is selected, the color setting is the same for all data collectively printed by FF or ESC FF.
     * - This command is effective until ESC @ is executed, the printer is reset, or the power is turned off.
     *
     * @param color color
     * @return {@link EscPosWriter}
     */
    public EscPosWriter setColor(Color color) {
        return write(ESC, 'r', color.code);
    }

    /**
     * Selects a page n from the character code table.
     * <p>
     * [Notes
     * - The characters of each page are the same for alphanumeric parts (ASCII code: Hex = 20h – 7Fh / Decimal = 32 – 127), and different for the escape character parts (ASCII code: Hex = 80h – FFh / Decimal = 128 – 255).
     * - The selected character code table is valid until ESC @ is executed, the printer is reset, or the power is turned off.
     *
     * @param characterCodeTable character code table
     * @return {@link EscPosWriter}
     */
    public EscPosWriter setCharacterCodeTable(CharacterCodeTable characterCodeTable) {
        return write(ESC, 't', characterCodeTable.code);
    }

    /**
     * In Standard mode, turns upside-down print mode on or off.
     * <p>
     * [Notes]
     * - When Standard mode is selected, this command is enabled only when processed at the beginning of the line.
     * - The upside-down print mode is effective for all data in Standard mode except the following:
     * - The graphics from GS ( L / GS 8 L <Function 112>, GS ( L / GS 8 L <Function 113>.
     * - Raster bit image from GS v 0.
     * - Variable vertical size bit image from GS Q 0.
     * - The upside-down print mode has no effect in Page mode.
     * - Settings of this command are effective until ESC @ is executed, the printer is reset, or the power is turned off.
     * - When upside-down print mode is turned on, the printer prints 180°-rotated characters from right to left. The line printing order is not reversed; therefore, be careful of the order of the data transmitted.
     *
     * @param enabled enable upside-down print
     * @return {@link EscPosWriter}
     */
    public EscPosWriter setUpsideDownPrint(boolean enabled) {
        return write(ESC, '{', enabled ? 1 : 0);
    }

    /**
     * Selects the character height and width.
     * <p>
     * [Notes]
     * - The character size set by this command is effective for alphanumeric, Kana, multilingual, and user-defined characters.
     * - When the characters are enlarged with different heights on one line, all the characters on the line are aligned at the baseline.
     * - When the characters are enlarged widthwise, the characters are enlarged to the right, based on the left side of the character.
     * - ESC ! can also turn double-width and double-height modes on or off.
     * - In Standard mode, the character is enlarged in the paper feed direction when double-height mode is selected, and it is enlarged perpendicular to the paper feed direction when double-width mode is selected. However, when character orientation changes in 90° clockwise rotation mode, the relationship between double-height and double-width is reversed.
     * - in Page mode, double-height and double-width are on the character orientation.
     * - The setting of the character size of alphanumeric and Katakana is effective until ESC ! is executed, ESC @ is executed, the printer is reset, or the power is turned off.
     * - The setting of the character size of Kanji and multilingual characters is effective until FS ! is executed, FS W is executed, ESC @ is executed, the printer is reset, or the power is turned off.
     *
     * @param width  width
     * @param height height
     * @return {@link EscPosWriter}
     */
    public EscPosWriter setCharacterSize(Width width, Height height) {
        return write(ESC, '!', width.code | height.code);
    }

    /**
     * Turns white/black reverse print mode on or off.
     * <p>
     * [Notes]
     * - The white/black reverse print is effective for 1-byte code characters and multi-byte code characters.
     * - When white/black reverse print mode is turned on, it also affects the right-side character spacing set by ESC SP and left- and right-side spacing of multi-byte code characters set by FS S.
     * - When white/black reverse print mode is turned on, it does not affect the space between lines and the spaces skipped by HT, ESC $, or ESC \.
     * - When underline mode is turned on, the printer does not underline white/black reverse characters.
     * - This command is effective until ESC @ is executed, the printer is reset, or the power is turned off.
     * - In white/black reverse print mode, characters are printed in white on a black background.
     *
     * @param enabled enable white/black reverse
     * @return {@link EscPosWriter}
     */
    public EscPosWriter setWhiteBlackReverse(boolean enabled) {
        return write(GS, 'B', enabled ? 1 : 0);
    }

    /**
     * Turns smoothing mode on or off.
     * <p>
     * [Notes]
     * - The smoothing mode is effective for quadruple-size or larger characters [alphanumeric, Kana, multilingual, and user-defined characters.]
     * - This command is effective until ESC @ is executed, the printer is reset, or the power is turned off.
     *
     * @param enabled enable smoothing
     * @return {@link EscPosWriter}
     */
    public EscPosWriter setSmoothing(boolean enabled) {
        return write(GS, 'b', enabled ? 1 : 0);
    }

    ///////////////////////////////////////////////
    //
    // Print Position Commands
    //
    ///////////////////////////////////////////////

    /**
     * Moves the print position to the next horizontal tab position.
     * <p>
     * [Notes]
     * - This command is ignored unless the next horizontal tab position has been set.
     * - If the next horizontal tab position exceeds the print area, the printer sets the print position to [Print area width + 1].
     * - If this command is processed when the print position is at [Print area width + 1], the printer executes print buffer-full printing of the current line and horizontal tab processing from the beginning of the next line. In this case, in Page mode, the printer does not execute printing, but the print position is moved.
     * - Horizontal tab positions are set by ESC D.
     * - The printer will not be in the beginning of the line by executing this command.
     * - When underline mode is turned on, the underline will not be printed under the tab space skipped by this command.
     *
     * @return {@link EscPosWriter}
     */
    public EscPosWriter horizontalTab() {
        return write(HT);
    }

    /**
     * Moves the print position to (nL + nH × 256) × (horizontal or vertical motion unit) from the left edge of the print area.
     * <p>
     * [Notes]
     * - The printer ignores any setting that exceeds the print area.
     * - When Standard mode is selected, the horizontal motion unit is used.
     * - when Page mode is selected, the horizontal or vertical motion unit is used for the print direction set by ESC T.
     * - When the starting position is set to the upper left or lower right of the print area using ESC T, the horizontal motion unit is used.
     * - When the starting position is set to the upper right or lower left of the print area using ESC T, the vertical motion unit is used.
     * - If the horizontal or vertical motion unit is changed after this command is executed, the print position is not changed.
     * - The printer will not be in the beginning of the line by executing this command.
     * - Even if underline mode is turned on, the underline will not be printed under the space skipped by this command.
     *
     * @param nL nL
     * @param nH nH
     * @return {@link EscPosWriter}
     */
    public EscPosWriter setAbsolutePosition(int nL, int nH) {
        return write(ESC, '$', nL, nH);
    }

    /**
     * Moves the print position to (nL + nH × 256) × (horizontal or vertical motion unit) from the current position.
     * <p>
     * [Notes]
     * - The printer ignores any setting that exceeds the print area.
     * - A positive number specifies movement to the right, and a negative number specifies movement to the left.
     * - When Standard mode is selected, the horizontal motion unit is used.
     * - when Page mode is selected, the horizontal or vertical motion unit is used for the print direction set by ESC T.
     * - When the starting position is set to the upper left or lower right of the print area using ESC T, the horizontal motion unit is used.
     * - When the starting position is set to the upper right or lower left of the print area using ESC T, the vertical motion unit is used.
     * - Even if the vertical or horizontal motion unit is changed after changing the print position, the setting of the print position will not be changed.
     * - When underline mode is turned on, the underline will not be printed under the space skipped by this command.
     * - "\" corresponds to "¥" in the JIS code system.
     *
     * @param nL nL
     * @param nH nH
     * @return {@link EscPosWriter}
     */
    public EscPosWriter setRelativePosition(int nL, int nH) {
        return write(ESC, '\\', nL, nH);
    }

    /**
     * In Page mode, selects the print direction and starting position.
     * <p>
     * [Notes]
     * - The print direction set by this command is effective only in Page mode.
     * - This command setting has no effect in Standard mode.
     * - The parameters for the horizontal or vertical motion unit differ, depending on the starting position of the print area as follows:
     * - If the starting position is the upper left or lower right of the print area:
     * - These commands use horizontal motion units: ESC SP, ESC $, ESC \, FS S
     * - These commands use vertical motion units: ESC 3, ESC J, ESC K, GS $, GS \
     * - If the starting position is the upper right or lower left of the print area:
     * - These commands use horizontal motion units: ESC 3, ESC J, ESC K, GS $, GS \
     * - These commands use vertical motion units: ESC SP, ESC $, ESC \, FS S
     * - Settings of this command are effective until ESC @ is executed, the printer is reset, or the power is turned off.
     *
     * @param direction direction
     * @return {@link EscPosWriter}
     */
    public EscPosWriter setPrintDirection(Direction direction) {
        return write(ESC, 'T', direction.code);
    }

    /**
     * In Standard mode, aligns all the data in one line to the selected layout.
     * <p>
     * [Notes]
     * - When Standard mode is selected, this command is enabled only when processed at the beginning of the line in Standard mode.
     * - The justification has no effect in Page mode.
     * - This command executes justification in the print area set by GS L and GS W.
     * - This command justifies printing data (such as characters, all graphics, barcodes, and two-dimensional code) and space area set by HT, ESC $, and ESC \.
     * - Settings of this command are effective until ESC @ is executed, the printer is reset, or the power is turned off.
     *
     * @param justification justification
     * @return {@link EscPosWriter}
     */
    public EscPosWriter setJustification(Justification justification) {
        return write(ESC, 'a', justification.code);
    }

    /**
     * In Page mode, moves the vertical print position to (nL + nH × 256) × (vertical or horizontal motion unit) from the starting position set by ESC T.
     * <p>
     * [Notes]
     * - This command is enabled only in Page mode. If this command is processed in Standard mode, it is ignored.
     * - The printer ignores any setting that exceeds the print area set by ESC W.
     * - The horizontal or vertical motion unit is used for the print direction set by ESC T.
     * - When the starting position is set to the upper left or lower right of the print area using ESC T, the vertical motion unit is used.
     * - When the starting position is set to the upper right or lower left of the print area using ESC T, the horizontal motion unit is used.
     * - Even if the vertical or horizontal motion unit is changed after changing the print position, the print position will not be changed.
     *
     * @param nL nL
     * @param nH nH
     * @return {@link EscPosWriter}
     */
    public EscPosWriter setAbsoluteVerticalPosition(int nL, int nH) {
        return write(GS, '$', nL, nH);
    }

    /**
     * In Page mode, moves the vertical print position to (nL + nH × 256) × (vertical or horizontal motion unit) from the current position.
     * <p>
     * [Notes]
     * - This command is enabled only in Page mode. If this command is processed in Standard mode, it is ignored.
     * - The printer ignores any setting that exceeds the print area set by ESC W.
     * - A positive number specifies movement downward, and a negative number specifies movement upward.
     * - The horizontal or vertical motion unit is used for the print direction set by ESC T.
     * - When the starting position is set to the upper left or lower right of the print area using ESC T, the vertical motion unit is used.
     * - When the starting position is set to the upper right or lower left of the print area using ESC T, the horizontal motion unit is used.
     * - Even if vertical or horizontal motion unit is changed after changing the print position, the setting of print position will not be changed.
     * - "\" corresponds to "¥" in the JIS code set.
     *
     * @param nL nL
     * @param nH nH
     * @return {@link EscPosWriter}
     */
    public EscPosWriter setRelativeVerticalPosition(int nL, int nH) {
        return write(GS, '\\', nL, nH);
    }

    /**
     * In Standard mode, sets the left margin to (nL + nH × 256) × (horizontal motion unit) from the left edge of the printable area.
     * <p>
     * [Notes]
     * - When Standard mode is selected, this command is enabled only when processed at the beginning of the line.
     * - The left margin has no effect in Page mode. If this command is processed in Page mode, the left margin is set and it is enabled when the printer returns to Standard mode.
     * - If the setting exceeds the printable area, the left margin is automatically set to the maximum value of the printable area.
     * - If this command and GS W set the print area width to less than the width of one character, the print area width is extended to accommodate one character for the line.
     * - Horizontal motion unit is used.
     * - If horizontal motion unit is changed after changing left margin, left margin setting is not changed.
     * - Left margin setting is effective until ESC @ is executed, the printer is reset, or the power is turned off.
     * - Left margin position is left edge of the printable area. If left margin setting is changed, left edge of the printable area will move.
     *
     * @param nL nL
     * @param nH nH
     * @return {@link EscPosWriter}
     */
    public EscPosWriter setLeftMargin(int nL, int nH) {
        return write(GS, 'L', nL, nH);
    }

    /**
     * In Standard mode, moves the print position to the beginning of the print line after performing the operation specified.
     * <p>
     * [Notes]
     * - In Page mode, this command is ignored.
     * - This command is ignored if the print position is already the beginning of the line.
     * - If the print position is not set to the beginning of the line, when n = 1, 49, this command functions the same as LF.
     * - When erasure is specified (n = 0, 48), executes cancel processing for the print data currently in the print buffer and maintains other data and settings.
     * - Setting values of each command, definitions, and receive buffer content are not changed.
     * - The command execution moves the print position to left side of the printable area. Also, the printer is in the status "beginning of the line".
     * - When using commands that are enabled only at the beginning of the line, these commands are sure to be executed if this command is used immediately before using those commands.
     *
     * @param action data operation to perform
     * @return {@link EscPosWriter}
     */
    public EscPosWriter setPrintPositionToStart(DataAction action) {
        return write(GS, 'T', action.code);
    }

    /**
     * In Standard mode, sets the print area width to (nL + nH × 256) × (horizontal motion unit).
     * <p>
     * [Notes]
     * - When Standard mode is selected, this command is enabled only when processed at the beginning of the line.
     * - The print area width has no effect in Page mode. If this command is processed in Page mode, the print area width is set and it is enabled when the printer returns to Standard mode.
     * - If the [left margin + print area width] exceeds the printable area, the print area width is automatically set to [printable area - left margin].
     * - If this command and GS L set the print area width to less than the width of one character, the print area width is extended to accommodate one character for the line.
     * - Horizontal motion unit is used.
     * - If horizontal motion unit is changed after setting the printable area width, the printable area width setting will not be changed.
     * - Printable area width setting is effective until ESC @ is executed, the printer is reset, or the power is turned off.
     *
     * @param nL nL
     * @param nH nH
     * @return {@link EscPosWriter}
     */
    public EscPosWriter setPrintAreaWidth(int nL, int nH) {
        return write(GS, 'W', nL, nH);
    }

    ///////////////////////////////////////////////
    //
    // Mechanism Control Commands
    //
    ///////////////////////////////////////////////

    /**
     * Moves the print head to the standby position.
     * <p>
     * [Notes]
     * - The standby position is different, depending on the printer model.
     * - The command rechecks the standby position; therefore, the print position might be shifted before and after checking the standby position.
     *
     * @return {@link EscPosWriter}
     */
    public EscPosWriter returnHome() {
        return write(ESC, '<');
    }

    /**
     * Turns unidirectional print mode on or off.
     * <p>
     * [Notes]
     * - This mode can be set independently in Standard mode and in Page mode.
     * - When this command is used in Standard mode, the printer sets the mode for Standard mode.
     * - When this command is used in Page mode, the printer sets the mode for page mode.
     * - When unidirectional print mode is turned off, bidirectional print mode is automatically turned on.
     * - when Page mode is selected, the printer performs unidirectional printing for all data that is to be collectively printed using FF or ESC FF.
     * - Unidirectional print mode can be turned on when printing double-height characters or graphics or bit image or two-dimensional code to ensure that the top and bottom of the printing patterns are aligned.
     * - Settings of this command are effective until ESC @ is executed, the printer is reset, or the power is turned off.
     *
     * @param enabled enable unidirectional print mode
     * @return {@link EscPosWriter}
     */
    public EscPosWriter setUnidirectionalPrint(boolean enabled) {
        return write(ESC, 'U', enabled ? 1 : 0);
    }

    /**
     * Executes paper cut.
     * <p>
     * [Notes]
     * - When using this command, note that there is a gap between the cutting position and the print position.
     *
     * @param cut cut mode
     * @return {@link EscPosWriter}
     */
    public EscPosWriter cut(CutA cut) {
        return write(GS, 'V', cut.code);
    }

    /**
     * Feeds paper to [cutting position + (n × vertical motion unit)] and executes paper cut.
     * <p>
     * [Notes]
     * - Printers without an auto-cutter only feed the paper for specified amount.
     *
     * @param cut cut mode
     * @param n   vertical motion units
     * @return {@link EscPosWriter}
     */
    public EscPosWriter cutWithFeed(CutB cut, int n) {
        return write(GS, 'V', cut.code, n);
    }

    /**
     * Preset [cutting position + (n × vertical motion unit)] to the paper cutting position, and executes paper cut when it reaches the autocutter position after printing and feeding
     * <p>
     * [Notes]
     * - The preset cutting position is cleared if any of the following cases is performed.
     * - Execution of ESC @
     * - Execution of commands that perform a software reset
     * - Power off or hardware reset
     * - Paper feed by pressing paper feed button
     * - Execution of buffer clear commands
     * - The paper cut involves a stop of printing. This might affect the print quality.
     * - The function makes it possible to save paper by reducing the top margin for continuous printing.
     *
     * @param cut cut mode
     * @param n   vertical motion units
     * @return {@link EscPosWriter}
     */
    public EscPosWriter cutPosition(CutC cut, int n) {
        return write(GS, 'V', cut.code, n);
    }

    /**
     * Feeds paper to [cutting position + (n × vertical motion unit)] and executes paper cut, then moves paper to the print start position by reverse feeding.
     * <p>
     * [Notes]
     * - The function makes it possible to save paper by reducing the top margin for continuous printing.
     * - The top margin of the print start position is different depending on the printers.
     *
     * @param cut cut mode
     * @param n   vertical motion units
     * @return {@link EscPosWriter}
     */
    public EscPosWriter cutWithFeedAndReturnStart(CutD cut, int n) {
        return write(GS, 'V', cut.code, n);
    }

    ///////////////////////////////////////////////
    //
    // Panel Button Commands
    //
    ///////////////////////////////////////////////

    /**
     * Enables or disables the panel buttons.
     * <p>
     * [Notes]
     * - If panel buttons are enabled, the function of the panel button, such as feeding, will be executed when the panel button is turned on.
     * - If panel buttons are disabled, the function of the panel button will not be executed even if pressed the button. To prevent problems caused by accidentally pressing the buttons, use this command to disable the buttons.
     * - Setting of this command is effective until ESC @ is executed, the printer is reset, or the power is turned off.
     * - When the printer cover is open, there are buttons that are always enabled or disabled regardless of this command. The buttons are different, depending on the printer model.
     * - Even if the panel buttons are disabled by this command, paper feed button will be enabled temporarily while printer is waiting for the buttons to be pressed. The following are some examples of the case.
     * - When a new roll of paper is installed in the TM-U230
     * - When waiting for an online recovery for the TM-L90 with Peeler
     * - When the printer is waiting for the button to be pressed while GS ^ is executed
     *
     * @param enabled enable panel buttons
     * @return {@link EscPosWriter}
     */
    public EscPosWriter setPanelButtons(boolean enabled) {
        return write(ESC, 'c', '5', enabled ? 1 : 0);
    }

    ///////////////////////////////////////////////
    //
    // Kanji Commands
    //
    ///////////////////////////////////////////////

    /**
     * Selects the character styles (double-height, double-width, and Kanji-underlined) together for multi-byte code character.
     * <p>
     * [Notes]
     * - Settings of this command affect multilingual characters and user-defined characters.
     * - Settings of this command are effective until any of the following commands are executed, ESC @ is executed, the printer is, or the power is turned off.
     * - Character size (bits 2 and 3): FS W, GS !
     * - Underline (bit 7): FS -
     * - When a double-height mode is specified, a character is enlarged based on a baseline of the character.
     * - When a double-width mode is specified, a character is enlarged based on the left side of the character.
     * - When both double-width and double-height modes are specified, quadruple-size characters are printed.
     * - When double-height mode is selected in Standard mode, a character is enlarged in the paper feed direction and when double-width mode is selected, a character is enlarged in the direction which is perpendicular to the paper feed direction. Therefore, when 90° clockwise-rotation is selected, the relationship between directions of enlargement of double-height and double-width is opposite from normal direction.
     * - When double-height mode is selected in Page mode, height size is enlarged and when double-width mode is selected in Page mode, width size is enlarged.
     * - When Kanji underline mode is specified, the width of the underline set by FS - is added. Even if the character size is changed, the width is not changed. The underline has the same color as the characters. The color can be selected by Function 48 of GS ( N.
     * - Even if Kanji underline mode is specified, 90° clockwise-rotated characters, white/black reverse characters, and spaces skipped by HT, ESC $, or ESC \ are not underlined.
     *
     * @param underlined   enable underline
     * @param doubleWidth  enable double width
     * @param doubleHeight enable double height
     * @return {@link EscPosWriter}
     */
    public EscPosWriter setKanjiPrintMode(boolean underlined, boolean doubleWidth, boolean doubleHeight) {
        int n = (doubleHeight ? 8 : 0)
                | (doubleWidth ? 4 : 0)
                | (underlined ? 128 : 0);
        return write(FS, '!', n);
    }

    /**
     * Selects Kanji character mode.
     * <p>
     * [Notes]
     * - This command can be used only for the Japanese, Simplified Chinese, and Traditional Chinese models.
     * - Settings of this command are effective until FS . is executed, ESC @ is executed, the printer is reset, or the power is turned off.
     * <p>
     * [Notes for Japanese models]
     * - Settings of this command affect processing of a character code only when JIS code system is selected by FS C.
     * - If Kanji mode is specified when JIS code system is selected, the printer processes a character code as a 2-byte code.
     * - Kanji codes are processed in order of the first byte then the second byte.
     * - Kanji mode is canceled at default.
     * <p>
     * [Notes for Simplified Chinese models]
     * - When Kanji mode is selected, the printer processes a character code that corresponds to the first byte of Kanji code, and then processes consecutive byte(s) as the second byte ( – the fourth byte) of Kanji code. Therefore, when Kanji code is specified, an ASCII code character that corresponds to the first byte of Kanji code cannot be printed.
     * - Kanji mode is selected at default.
     * <p>
     * [Notes for Traditional Chinese models]
     * - When Kanji mode is selected, the printer processes a character code that corresponds to the first byte of Kanji code, and then processes a consecutive byte as the second byte of Kanji code. Therefore, when Kanji code is specified, an ASCII code character that corresponds to the first byte of Kanji code cannot be printed.
     * - Kanji mode is selected at default.
     *
     * @return {@link EscPosWriter}
     */
    public EscPosWriter setKanjiCharacterMode() {
        return write(FS, '&');
    }

    /**
     * Turns on or off underline mode for multi-byte code character (Kanji-underline).
     * <p>
     * [Notes]
     * - Settings of this command affect multilingual characters and user-defined characters.
     * - The underline has the same color as the characters. The color can be selected by Function 48 of GS ( N.
     * - Even if Kanji underline mode is specified, 90° clockwise-rotation characters, white/black reverse characters, and spaces skipped by HT, ESC $, or ESC \ are not underlined.
     * - When a character size is changed, an underline width is not changed.
     * - When underline mode is canceled, the following characters are not underlined; however, an underline width set right before the mode is canceled remains.
     * - Settings of this command are effective until FS ! is executed, ESC @ is executed, the printer is reset, or the power is turned off.
     * - Some printer models support Kanji underline mode with 2-dot width.
     *
     * @param underline enable underline
     * @return {@link EscPosWriter}
     */
    public EscPosWriter setKanjiUnderline(Underline underline) {
        return write(FS, '-', underline.code);
    }

    /**
     * Cancels Kanji character mode.
     * <p>
     * [Notes]
     * - This command can be used only for the Japanese, Simplified Chinese, and Traditional Chinese models.
     * - Settings of this command are effective until FS & is executed, ESC @ is executed, the printer is reset, or the power is turned off.
     * <p>
     * [Notes for Japanese models]
     * - Settings of this command affect character code processing only when JIS code system is selected by FS C.
     * - When JIS code system is selected, if Kanji mode is canceled, the printer processes a character code as a 1-byte code of alphanumeric Katakana characters.
     * - Kanji mode is canceled at default.
     * <p>
     * [Notes for Simplified Chinese models]
     * - If Kanji mode is canceled, the printer processes a character code as a 1-byte code of alphanumeric Katakana characters.
     * - Kanji mode is selected at default.
     * <p>
     * [Notes for Traditional Chinese models]
     * - If Kanji mode is canceled, the printer processes a character code as a 1-byte code of alphanumeric Katakana characters.
     * - Kanji mode is selected at default.
     *
     * @return {@link EscPosWriter}
     */
    public EscPosWriter cancelKanjiCharacterMode() {
        return write(FS, '.');
    }

    /**
     * Selects a Kanji character code system for the Japanese model.
     * <p>
     * [Notes]
     * - This command is effective only with Japanese model.
     * - When JIS code system is selected, Kanji mode should be specified by FS & to print Kanji characters.
     * - When SHIFT JIS code system is selected, if the printer processes a character code that corresponds to the first byte of the Kanji code, the printer processes a consecutive byte as the second byte of the Kanji code.
     * - Therefore, when this code system is selected, ASCII code that corresponds to the first byte of the Kanji code cannot be printed.
     * - Kanji code processes the first byte and the second byte in order.
     * - The command is effective until ESC @ is executed, the printer is reset, or the power is turned off.
     *
     * @param kanji kanji character code system
     * @return {@link EscPosWriter}
     */
    public EscPosWriter setKanjiCodeSystem(Kanji kanji) {
        return write(FS, 'C', kanji.code);
    }

    /**
     * Sets left- and right-side spacing of the multi-byte code character n1 and n2, respectively.
     * - Sets the left-side character spacing to [n1 × horizontal or vertical motion units].
     * - Sets the right-side character spacing to [n2 × horizontal or vertical motion units].
     * <p>
     * [Notes]
     * - Settings of this command affect multilingual characters and user-defined characters.
     * - When a character size is set to N times as large as a normal size, both right- and left-side character spacings are also set to N times as large as a normal size.
     * - In Standard mode, the horizontal motion unit (perpendicular to the paper feed direction) is used.
     * - in Page mode, the horizontal or vertical motion unit differs, depending on the starting position set by ESC T.
     * - When the starting position is set to the upper left or lower right, the horizontal motion unit (perpendicular to the paper feed direction) is used.
     * - When the starting position is set to the upper right or lower left, the vertical motion unit (paper feed direction) is used.
     * - Different character spacing can be set for Standard mode and page mode.
     * - When this command is set in Standard mode, character spacing for multilingual (except Thai) characters printed in Standard mode is set.
     * - When this command is set in Page mode, character spacing for multilingual (except Thai) characters printed in Page mode is set.
     * - If the horizontal or vertical motion unit is changed after setting the character spacing, the spacing between the characters is not changed.
     * - The character spacing is effective until ESC @ is executed, the printer is reset, or the power is turned off.
     * - This command is used to change spacing between characters.
     *
     * @param n1 left-side character spacing
     * @param n2 right-side character spacing
     * @return {@link EscPosWriter}
     */
    public EscPosWriter setKanjiCharacterSpacing(int n1, int n2) {
        return write(FS, 'S', n1, n2);
    }

    /**
     * Turns quadruple-size mode on or off for multi-byte code character.
     * <p>
     * [Notes]
     * - Settings of this command affect multilingual characters and user-defined characters.
     * - When a double-height mode is specified, a character is enlarged based on a baseline of the character and when a double-width mode is specified, a character is enlarged based on the left side of the character.
     * - Settings of this command are effective until FS ! is executed, GS ! is executed, ESC @ is executed, the printer is reset, or the power is turned off.
     *
     * @param enabled enable quadruple size
     * @return {@link EscPosWriter}
     */
    public EscPosWriter setKanjiQuadrupleSize(boolean enabled) {
        return write(FS, 'W', enabled ? 1 : 0);
    }

    ///////////////////////////////////////////////
    //
    // Miscellaneous Commands
    //
    ///////////////////////////////////////////////

    /**
     * Responds to a request in real time from the host computer.
     * <p>
     * [Notes]
     * - This is a Real-time command. Refer to Notes of Real-time commands for usage note.
     * - DLE ENQ 0 can be used in the following online recovery waiting status.
     * - Online recovery after roll paper insertion in TM-U230 (See GS z 0)
     * - Online recovery for the TM-L90 with Peeler (See GS z 0 (Peeler)).
     * - Status waiting for the button to be pressed, while GS ^ is executed.
     * - DLE ENQ 1 or DLE ENQ 2 is enabled only when a recoverable error occurs, with the exception of an automatically recoverable error. When a recoverable error occurs, after removing the cause of the error, the printer can recover from the error by transmitting this command without the printer being turned off. Errors recoverable by this command depend on the printer model.
     * - in Page mode, if the printer recovers from a recoverable error by using DLE ENQ 2, the printer returns to Standard mode after clearing the data in receive and print buffers and changing the values set by ESC W to the default values.
     * - After processing DLE ENQ 2, the print position is moved to left side of the printable area. Also, the printer is in the status "beginning of the line".
     *
     * @param realTimeRequest request
     * @return {@link EscPosWriter}
     */
    public EscPosWriter sendRealTimeRequest(RealTimeRequest realTimeRequest) {
        return write(DLE, ENQ, realTimeRequest.code);
    }

    /**
     * Outputs the pulse specified by t to connector pin.
     * The pulse ON time is [t × 100 ms] and the OFF time is [t × 100 ms]
     * <p>
     * [Notes]
     * - This is a Real-time command. Refer to Notes of Real-time commands for usage note.
     * - This command is ignored in the following states:
     * - In error status
     * - When a pulse is being output to the drawer kick-out connector
     * - During transmission of block data (Header – NUL)
     * - When this command is disabled by GS ( D
     * - It is not possible to output the pulses to the drawer kick-out connector pin 2 and 5 at the same time.
     *
     * @param pin       connector pin
     * @param pulseTime pulse time
     * @return {@link EscPosWriter}
     */
    public EscPosWriter sendRealTimeRequestPulse(Pin pin, PulseTime pulseTime) {
        return write(DLE, DC4, 1, pin.code, pulseTime.code);
    }

    /**
     * Executes the printer power-off sequence and transmits the power-off notice.
     * - Saving the maintenance counter values
     * - Busy controlling for interface
     * - Changing to waiting state of mechanism
     * <p>
     * [Notes]
     * - This is a Real-time command. Refer to Notes of Real-time commands for usage note.
     * - This command can be used after enabling real time command processing with GS ( D.
     * - All information and data stored in RAM will be deleted by processing this command.
     * - When the printer has executed setup of power off processing, the printer transmits the power-off notice as shown below to the host.
     * -    Power-off notice	Hex	Decimal	Number of bytes
     * -    Header	            3Bh	59	    1 byte
     * -    Identifier	        30h	48	    1 byte
     * -    NUL	                00h	0	    1 byte
     * - Maintenance counter values are maintained by executing this command; therefore the maintenance counter values will be more accurate if the user uses this command before turning off the power switch.
     * - The power-off notice can be differentiated from other transmission data according to specific data of the transmission data block. When the header transmitted from the printer is [hex = 3Bh / decimal = 59], treat NUL [hex = 00h / decimal = 0] as a data group and identify it according to the combination of the header and the identifier.
     *
     * @return {@link EscPosWriter}
     */
    public EscPosWriter sendRealTimeRequestPowerOff() {
        return write(DLE, DC4, 2, 1, 8);
    }

    /**
     * Sounds the buzzer with a sound pattern specified by a the number of times specified by r.
     * a, n, r, t1, t2: different depending on the printers.
     * <p>
     * [Notes]
     * - This is a Real-time command. Refer to Notes of Real-time commands for usage note.
     * - This command can be used after enabling real time command processing with GS ( D.
     * - The function of not sounding the buzzer affects autonomous buzzer sound (errors, paper-end) and buzzer sound with ESC ( A <Function 97>, buzzer sound during cutting, buzzer sound by generating the specified pulse.
     *
     * @param a  a
     * @param n  n
     * @param r  r
     * @param t1 t1
     * @param t2 t2
     * @return {@link EscPosWriter}
     */
    public EscPosWriter sendRealTimeRequestBuzzer(int a, int n, int r, int t1, int t2) {
        return write(DLE, DC4, 3, a, n, r, t1, t2);
    }

    /**
     * Transmits specified status in real-time as follows.
     * <p>
     * [Notes]
     * - This is a Real-time command. Refer to Notes of Real-time commands for usage note.
     * - The status or response format is the same as the format of the related command in the table above. See the description of the related command corresponding to m.
     * - Battery status differs depending on printer model.
     * - This command is not affected the setting of the corresponding ASB or response. This command transmits the ASB or response only once, even if it is disabled by the related command.
     * - This command doesn't affect the setting of the corresponding ASB or response.
     *
     * @param status status
     * @return {@link EscPosWriter}
     */
    public EscPosWriter sendRealTimeRequestStatus(Status status) {
        return write(DLE, DC4, 7, status.code);
    }

    /**
     * Clears all data stored in the receive buffer and the print buffer and transmits Clear response.
     * <p>
     * [Notes]
     * - Do not use this command in a system in which the printer is used with the OPOS driver and Java POS driver that are provided by Seiko Epson Corporation.
     * - This is a Real-time command. Refer to Notes of Real-time commands for usage note.
     * - When this command is transmitted, do not transmit data that follows until the Clear responseis received.
     * - If this command is sent while another command is processing, processing of the other command is canceled.
     * - Exmaple: If this command is sent while NV graphics is defined, the definition will be canceled.
     * - If this command is processed in Page mode, the printer returns to Standard mode. The setting of ESC W is changed to the default.
     * - This command does not change or initialize settings of other commands (except for ESC W with Page modeselected).
     * - The command execution moves the print position to left side of the printable area.
     * - If this command is executed when a recoverable error has occurred, the printer recovers from the error. This is the same function as DLE ENQ 2.
     * - When buffer clear processing is finished, the printer transmits the Clear response as shown below.
     * - Clear response  Hex  Decimal  Number of bytes
     * - Header          37h  55       1 byte
     * - Identifier      25h  37       1 byte
     * - NUL             00h  0        1 byte
     *
     * @return {@link EscPosWriter}
     */
    public EscPosWriter sendRealTimeRequestClearBuffers() {
        return write(DLE, DC4, 8, 1, 3, 20, 1, 6, 2, 8);
    }

    /**
     * Set peripheral device.
     * n is different depending on printer
     * <p>
     * [Notes]
     * - When the printer is disabled, it ignores all received data and commands with the exception of ESC = and real-time commands.
     * - If ASB is enabled when the printer is disabled by this command, the printer transmits the ASB status message whenever the status changes. See the description of GS a for ASB function.
     * - Settings of this command are effective until ESC @ is executed, the printer is reset, or the power is turned off.
     * - The default value when the power supply is turned on and when ESC @ is executed might be different.
     *
     * @param n n
     * @return {@link EscPosWriter}
     */
    public EscPosWriter setPeripheralDevice(int n) {
        return write(ESC, '=', n);
    }

    /**
     * Clears the data in the print buffer and resets the printer modes to the modes that were in effect when the power was turned on.
     * - Any macro definitions are not cleared.
     * - Offline response selection is not cleared.
     * - Contents of user NV memory are not cleared.
     * - NV graphics (NV bit image) and NV user memory are not cleared.
     * - The maintenance counter value is not affected by this command.
     * - Software setting values are not cleared.
     * <p>
     * [Notes]
     * - The DIP switch settings are not checked again.
     * - The data in the receive buffer is not cleared.
     * - When this command is processed in Page mode, the printer deletes the data in the print areas, initializes all settings, and selects Standard mode.
     * - This command can cancel all the settings, such as print mode and line feed, at the same time.
     * - The command execution moves the print position to left side of the printable area. Also, the printer is in the status "beginning of the line".
     *
     * @return {@link EscPosWriter}
     */
    public EscPosWriter initialize() {
        return write(ESC, '@');
    }

    /**
     * Switches from Standard mode to Page Mode.
     * <p>
     * [Notes]
     * - This command is enabled only when processed at the beginning of the line in Standard mode. In other cases, this command is ignored.
     * - The print position is the starting position specified by ESC T within the print area defined by ESC W.
     * - The following commands switch the settings for Page modebecause these commands can be set independently in Standard mode and in Page mode:
     * - ESC SP, ESC 2, ESC 3, ESC U, and FS S
     * - The following commands are disabled in Page mode.
     * - ESC L, FS g 1, FS q, GS ( A, GS ( C (part of functions), GS ( E, GS ( L / GS 8 L (part of functions), GS ( M (part of functions), GS ( P, GS T, and GS g 0
     * - The following commands are not effective in Page mode.
     * - ESC V, ESC a, ESC {, GS L, and GS W
     * - The printer returns to Standard mode with ESC S, FF, and ESC @. When it returns to Standard mode by ESC @, all settings are canceled.
     * - Standard mode is selected as the default.
     * - in Page mode, the printer prints the data in the print buffer for the print area specified by ESC W collectively by FF or ESC FF. When executing the print and paper feed commands, such as LF, CR, ESC J, and ESC d, only the print position moves; the printer does not perform actual printing.
     *
     * @return {@link EscPosWriter}
     */
    public EscPosWriter setPageMode() {
        return write(ESC, 'L');
    }

    /**
     * Switches from Page Mode to Standard Mode.
     * <p>
     * [Notes]
     * - This command is enabled only in Page mode. Page mode can be selected by ESC L.
     * - When this command is executed, data in all the print areas is cleared, the print area set by ESC W returns to the default value, but the value set by ESC T is maintained.
     * - The following commands switch the settings for Standard mode because these commands can be set independently in Standard mode and in Page mode:
     * - ESC SP, ESC 2, ESC 3, ESC U, FS S
     * - In Standard mode, the following commands are ignored.
     * - CAN, ESC FF, GS $, GS ( Q, GS \
     * - The settings of the following commands do not affect printing in Standard mode.
     * - ESC T, ESC W, GS ( P
     * - Standard mode is selected as the default.
     *
     * @return {@link EscPosWriter}
     */
    public EscPosWriter setStandardMode() {
        return write(ESC, 'S');
    }

    /**
     * Outputs the pulse specified by t1 and t2 to the specified connector pin.
     * The pulse for ON time is (t1 × 2 msec) and for OFF time is (t2 × 2 msec).
     * <p>
     * [Notes]
     * - If t2 < t1, the OFF time is equal to the ON time.
     * - It is not possible to output the pulses to the drawer kick-out connector pin 2 and 5 at the same time.
     *
     * @param pin connector pin
     * @param t1  t1
     * @param t2  t2
     * @return {@link EscPosWriter}
     */
    public EscPosWriter generatePulse(Pin pin, int t1, int t2) {
        return write(ESC, 'p', pin.code, t1, t2);
    }

    /**
     * Sets the horizontal and vertical motion units to approximately 25.4/x mm {1/x"} and approximately 25.4/y mm {1/y"}, respectively.
     * - When x = 0, the default value of the horizontal motion unit is used.
     * - When y = 0, the default value of the vertical motion unit is used.
     * <p>
     * [Notes]
     * - The horizontal and vertical motion units indicate the minimum pitch used for calculating the values of related commands.
     * - The horizontal direction is perpendicular to the paper feed direction and the vertical direction is the paper feed direction.
     * - In Standard mode, the following commands use x or y.
     * - Commands using x: ESC SP, ESC $, ESC \, FS S, GS ( P, GS L, and GS W
     * - Commands using y: ESC 3, ESC J, ESC K, GS ( P, and GS V
     * - in Page mode, the following commands use x or y, when the starting position is set to the upper left or lower right of the print area using ESC T.
     * - Commands using x: ESC SP, ESC $, ESC W, ESC \, GS ( Q, and FS S
     * - Commands using y: ESC 3, ESC J, ESC K, ESC W, GS $, GS V, GS ( Q, and GS \
     * - in Page mode, the following commands use x or y, when the starting position is set to the upper right or lower left of the print area using ESC T.
     * - Commands using x: ESC 3, ESC J, ESC K, ESC W, GS $, GS ( Q, and GS \
     * - Commands using y: ESC SP, ESC $, ESC W, ESC \, FS S, GS ( Q, and GS V
     * - Setting of this command is effective until ESC @ is executed, the printer is reset, or the power is turned off.
     * - The calculated result from combining this command with others is truncated to the minimum value of the mechanical pitch.
     * - This command does not affect the current setting values.
     *
     * @param x horizontal motion units
     * @param y vertical motion units
     * @return {@link EscPosWriter}
     */
    public EscPosWriter setMotionUnits(int x, int y) {
        return write(GS, 'P', x, y);
    }

    ///////////////////////////////////////////////
    //
    // TODO: Bit image commands
    //
    ///////////////////////////////////////////////

    ///////////////////////////////////////////////
    //
    // TODO: 2D code commands
    //
    ///////////////////////////////////////////////

    ///////////////////////////////////////////////
    //
    // TODO: User-defined commands
    //
    ///////////////////////////////////////////////

    ///////////////////////////////////////////////
    //
    // TODO: Macro functions
    //
    ///////////////////////////////////////////////

    ///////////////////////////////////////////////
    //
    // Raw Write Commands (to avoid var-args)
    //
    ///////////////////////////////////////////////

    private EscPosWriter write(int val1) {
        try {
            out.write(val1);
            return this;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private EscPosWriter write(int val1, int val2) {
        try {
            out.write(val1);
            out.write(val2);
            return this;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private EscPosWriter write(int val1, int val2, int val3) {
        try {
            out.write(val1);
            out.write(val2);
            out.write(val3);
            return this;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private EscPosWriter write(int val1, int val2, int val3, int val4) {
        try {
            out.write(val1);
            out.write(val2);
            out.write(val3);
            out.write(val4);
            return this;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private EscPosWriter write(int val1, int val2, int val3, int val4, int val5) {
        try {
            out.write(val1);
            out.write(val2);
            out.write(val3);
            out.write(val4);
            out.write(val5);
            return this;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private EscPosWriter write(int val1, int val2, int val3, int val4, int val5, int val6, int val7, int val8) {
        try {
            out.write(val1);
            out.write(val2);
            out.write(val3);
            out.write(val4);
            out.write(val5);
            out.write(val6);
            out.write(val7);
            out.write(val8);
            return this;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private EscPosWriter write(int val1, int val2, int val3, int val4, int val5, int val6, int val7, int val8,
                               int val9, int val10) {
        try {
            out.write(val1);
            out.write(val2);
            out.write(val3);
            out.write(val4);
            out.write(val5);
            out.write(val6);
            out.write(val7);
            out.write(val8);
            out.write(val9);
            out.write(val10);
            return this;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
