package com.ashokvarma.sqlitemanager.csv;

import java.io.IOException;
import java.io.Writer;

/**
 * Class description
 *
 * @author ashok
 * @version 1.0
 * @since 12/12/17
 */
public class CsvEscaper extends CharSequenceTranslator {

    private static CsvEscaper instance;

    private CsvEscaper() {
    }

    public static CsvEscaper getInstance() {
        if (instance == null) {
            instance = new CsvEscaper();
        }

        return instance;
    }

    private static final char CSV_DELIMITER = ',';
    private static final char CSV_QUOTE = '"';
    /**
     * {@code \u000a} linefeed LF ('\n').
     *
     * @see <a href="http://docs.oracle.com/javase/specs/jls/se7/html/jls-3.html#jls-3.10.6">JLF: Escape Sequences
     * for Character and String Literals</a>
     * @since 2.2
     */
    private static final char LF = '\n';

    /**
     * {@code \u000d} carriage return CR ('\r').
     *
     * @see <a href="http://docs.oracle.com/javase/specs/jls/se7/html/jls-3.html#jls-3.10.6">JLF: Escape Sequences
     * for Character and String Literals</a>
     * @since 2.2
     */
    private static final char CR = '\r';

    private static final String CSV_QUOTE_STR = String.valueOf(CSV_QUOTE);
    private static final char[] CSV_SEARCH_CHARS =
            new char[]{CSV_DELIMITER, CSV_QUOTE, CR, LF};

    @Override
    public int translate(final CharSequence input, final int index, final Writer out) throws IOException {

        if (index != 0) {
            throw new IllegalStateException("CsvEscaper should never reach the [1] index");
        }

        if (StringUtils.containsNone(input.toString(), CSV_SEARCH_CHARS)) {
            out.write(input.toString());
        } else {
            out.write(CSV_QUOTE);
            out.write(StringUtils.replace(input.toString(), CSV_QUOTE_STR, CSV_QUOTE_STR + CSV_QUOTE_STR));
            out.write(CSV_QUOTE);
        }
        return Character.codePointCount(input, 0, input.length());
    }
}