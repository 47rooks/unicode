/**
 * Copyright Daniel Semler 2015
 */
package ds.debug;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * A DebugLogger is a simple <code>java.util.logging.Logger</code> with a
 * FileHandler configured for rotation.
 * 
 * @author dsemler
 *
 */
public class DebugLogger extends Logger {

    private static final int MAX_FILE_SIZE = 1024*1024;
    private static final int FILE_COUNT = 10;
    private static final boolean APPEND = true;
    private static final String DEBUG_LOG_LEVEL = "ds.debug.logging.level";
    private static final String DEFAULT_LOG_LEVEL = "INFO";
    
    public DebugLogger(String name, String destFile)
    throws IOException
    {
        super(name, null);
        Level level = null;
        try {
            level = Level.parse(System.getProperty(DEBUG_LOG_LEVEL,
                                                   DEFAULT_LOG_LEVEL));
        } catch (IllegalArgumentException iae) {
            level = Level.parse(DEFAULT_LOG_LEVEL);
        }

        setLevel(level);
        FileHandler fh = new FileHandler(destFile,
                                   MAX_FILE_SIZE,
                                   FILE_COUNT,
                                   APPEND);
        // TODO write my own one line formatter
        fh.setFormatter(new DebugFormatter());
        addHandler(fh);
    }
}
