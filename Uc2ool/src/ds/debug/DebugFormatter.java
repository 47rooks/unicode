/**
 * Copyright Daniel Semler 2015
 */
package ds.debug;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * @author dsemler
 *
 */
public class DebugFormatter extends Formatter {

    private final static SimpleDateFormat s_tmFormat =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    
    @Override
    public String format(LogRecord r) {
        StringBuilder sb = new StringBuilder();
        sb.append(s_tmFormat.format(new Date(r.getMillis()))).append(" ");
        sb.append(r.getSourceClassName()).append(".");
        sb.append(r.getSourceMethodName()).append(" ");
        sb.append(r.getLevel()).append(" ").append(r.getMessage());
        sb.append("\n");
        return sb.toString();
    }

}
