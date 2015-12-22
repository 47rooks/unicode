package uc2ool.resources;

import java.util.ListResourceBundle;

public class Messages extends ListResourceBundle {

    @Override
    protected Object[][] getContents() {
        return m_contents;
    }

    static final Object[][] m_contents = {
            { "INV_DEC_CP", "Invalid decimal code point %1$s encountered" },
            { "INV_HEX_CP", "Invalid hexadecimal code point %1$s encountered" }
    };
}
