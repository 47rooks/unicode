package uc2ool.model;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ds.debug.DebugLogger;
import uc2ool.model.Uc2oolModel;
import uc2ool.model.Uc2oolModel.InputType;

public class Uc2oolUnitTest {

    private Uc2oolModel m_calc;
    
    @Before
    public void setUp() throws Exception {
        m_calc = new Uc2oolModel(new DebugLogger("Uc2oolUnitTest",
                                                 "uc2oolTests%g.log"));
    }

    @After
    public void tearDown() throws Exception {
        m_calc = null;
    }

    /* Test Specification
     *
     * For each range in each conversion algorithm test:
     *   edges of each conversion range and one within each range
     *   invalid input cases
     *   unassigned code point
     */
    
    /* Test conversions of the following codepoints to UTF-8
     *
     * 0 - 0x7f
     * 0x80 - 0x7ff
     * 0x800 - 0xffff
     * 0x10000 - 0x1fffff - this range terminates at 0x10FFFF in Unicode 8.
     * 0x200000 - 0x3ffffff - there are currently no codepoints in this range
     * 0x4000000 - 0x7fffffff - there are currently no codepoints in this range
     */
    @Test
    public void testHCPUTF81ByteLow() {
        // NULL
        testHCPUTF8Conversions("0", InputType.HEXCODEPOINT, "00");
    }

    @Test
    public void testHCPUTF81Byte() {
        // LATIN CAPITAL LETTER A
        testHCPUTF8Conversions("41", InputType.HEXCODEPOINT, "41");
    }

    @Test
    public void testHCPUTF81ByteHigh() {
        // DELETE
        testHCPUTF8Conversions("7f", InputType.HEXCODEPOINT, "7F");
    }
    
    @Test
    public void testHCPUTF82BytesLow() {
        // <UNNAMED - seems to be Euro character
        testHCPUTF8Conversions("80", InputType.HEXCODEPOINT, "C2 80");
    }
    
    @Test
    public void testHCPUTF82Bytes() {
        // ARABIC NUMBER SIGN
        testHCPUTF8Conversions("600", InputType.HEXCODEPOINT, "D8 80");
    }
    
    @Test
    public void testHCPUTF82BytesHigh() {
        // <UNNAMED>
        testHCPUTF8Conversions("7ff", InputType.HEXCODEPOINT, "DF BF");
    }
    
    @Test
    public void testHCPUTF83BytesLow() {
        // SAMARITAN LETTER ALAF
        testHCPUTF8Conversions("800", InputType.HEXCODEPOINT, "E0 A0 80");
    }
    
    @Test
    public void testHCPUTF83Bytes() {
        // HEBREW LETTER WIDE LAMED
        testHCPUTF8Conversions("fb25", InputType.HEXCODEPOINT, "EF AC A5");
    }
    
    @Test
    public void testHCPUTF83BytesHigh() {
        // <UNNAMED>
        testHCPUTF8Conversions("ffff", InputType.HEXCODEPOINT, "EF BF BF");
    }
    
    @Test
    public void testHCPUTF84BytesLow() {
        // LINEAR B SYLLABLE B008 A
        testHCPUTF8Conversions("10000", InputType.HEXCODEPOINT, "F0 90 80 80");
    }
    
    @Test
    public void testHCPUTF84Bytes() {
        // UGARITIC LETTER HO
        testHCPUTF8Conversions("10385",
                               InputType.HEXCODEPOINT, "F0 90 8E 85");
    }
    
    @Test
    public void testHCPUTF84BytesLimit() {
        // LAST CODE POINT VALUE
        testHCPUTF8Conversions("0x01ffff", 
                               InputType.HEXCODEPOINT, "F0 9F BF BF");
    }

    @Test
    public void testHCPUTF84BytesHigh() {
        try {
            // NOT ASSIGNED
            testHCPUTF8Conversions("0x1fffff",
                                   InputType.HEXCODEPOINT, "F7 BF BF BF");
        } catch (UncheckedModelException uce) {
            // Expected
            assertTrue(uce.getLocalizedMessage().equals(
                    "Invalid hexadecimal code point 0x1fffff encountered"));
        }
    }

    /*
     * Core test method for all UTF-8 conversions and boundary conditions
     * 
     * @param i the input string to convert
     * @param type the InputType
     * @param result the expected result from <code>getUTF8Encoding()</code>
     * call
     */
    private void testHCPUTF8Conversions(String i,
                                        InputType type, 
                                        String result) {
        m_calc.setInput(i, type);
        System.out.println("*" + m_calc.getUTF8Encoding() + "*");
        assertTrue(m_calc.getUTF8Encoding(),
                   m_calc.getUTF8Encoding().equals(result));
    }
    
    /*
     * 0 - 0xffff
     * 0x10000 - 0x10FFFF terminates here in Unicode 8
     */
    @Test
    public void testHCPUTF161IntLow() {
        // NULL
        testHCPUTF16Conversions("0", InputType.HEXCODEPOINT, "0000");
    }

    @Test
    public void testHCPUTF161Int() {
        // LATIN CAPITAL LETTER A
        testHCPUTF16Conversions("41", InputType.HEXCODEPOINT, "0041");
    }

    @Test
    public void testHCPUTF161IntHigh() {
        // <UNNAMED>
        testHCPUTF16Conversions("0xffff", InputType.HEXCODEPOINT, "FFFF");
    }
    
    @Test
    public void testHCPUTF162IntLow() {
        // LINEAR B SYLLABLE B008 A
        testHCPUTF16Conversions("10000", InputType.HEXCODEPOINT, "D800 DC00");
    }
    
    @Test
    public void testHCPUTF162Int() {
        // BRAHMI SIGN CANDRABINDU
        testHCPUTF16Conversions("11000", InputType.HEXCODEPOINT, "D804 DC00");
    }
    
    @Test
    public void testHCPUTF162IntHigh() {
        // <UNNAMED>
        testHCPUTF16Conversions("0x10ffff",
                                InputType.HEXCODEPOINT, "DBFF DFFF");
    }
    
    /*
     * Core test method for the UTF-16 conversions and boundary conditions
     * 
     * @param i the input string to convert
     * @param type the InputType
     * @param result the expected result from <code>getUTF16Encoding()</code>
     * call
     */
    private void testHCPUTF16Conversions(String i,
                                         InputType type,
                                         String result) {
        m_calc.setInput(i, type);
        System.out.println("*" + m_calc.getUTF16Encoding() + "*");
        assertTrue(m_calc.getUTF16Encoding(),
                   m_calc.getUTF16Encoding().equals(result));
    }
    
    // Test exception cases
    @Test
    public void testHCPUBelow0() {
        try {
            m_calc.setInput("-12", InputType.HEXCODEPOINT);
            fail("Excepted exception not thrown");
        } catch (UncheckedModelException uce) {
            // Expected
            assertTrue(uce.getLocalizedMessage().equals(
                    "Invalid hexadecimal code point -12 encountered"));
        }
    }
    

}
