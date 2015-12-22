package uc2ool.model;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ds.debug.DebugLogger;
import uc2ool.model.Calculator;
import uc2ool.model.Calculator.InputType;

public class Uc2oolUnitTest {

    private Calculator m_calc;
    
    @Before
    public void setUp() throws Exception {
        m_calc = new Calculator(new DebugLogger("%t/CalculatorUnitTest%g.log",
                                                "FINEST"));
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
     * 0x10000 - 0x1fffff
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
        testHCPUTF8Conversions("10385", InputType.HEXCODEPOINT, "F0 90 8E 85");
    }
    
    @Test
    public void testHCPUTF84BytesHigh() {
        // NOT ASSIGNED
        testHCPUTF8Conversions("0x1fffff", InputType.HEXCODEPOINT, "F7 BF BF BF");
    }
    
    @Test
    public void testHCPUTF85Bytes() {
        testHCPUTF8Conversions("41", InputType.HEXCODEPOINT, "1");
    }
    
    @Test
    public void testHCPUTF86Bytes() {
        testHCPUTF8Conversions("41", InputType.HEXCODEPOINT, "1");
    }

    /*
     * Core test method for all UTF-8 conversions and boundary conditions
     * @param i the input string to convert
     * @param type the InputType
     * @param result the expected result from <code>getUTFEncoding()</code> call
     */
    private void testHCPUTF8Conversions(String i,
                                        InputType type, 
                                        String result) {
        m_calc.setInput(i, type);
        System.out.println("*" + m_calc.getUTF8Encoding() + "*");
        assertTrue(m_calc.getUTF8Encoding(),
                   m_calc.getUTF8Encoding().equals(result));
    }
    
    // Test exception cases
    @Test
    public void testHCPUBelow0() {
        try {
            m_calc.setInput("-12", InputType.HEXCODEPOINT);
        } catch (IllegalArgumentException iae) {
            // Expected
            // FIXME Validate the exception message
        }
        fail("Excepted exception not thrown");
    }
    

}
