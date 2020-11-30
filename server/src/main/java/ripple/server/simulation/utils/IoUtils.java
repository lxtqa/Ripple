package ripple.server.simulation.utils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

/**
 * @author qingzhou.sjq
 */
public class IoUtils {
    public static byte[] tryDecompress(InputStream raw) throws Exception {

        try {
            GZIPInputStream gis
                    = new GZIPInputStream(raw);
            ByteArrayOutputStream out
                    = new ByteArrayOutputStream();

            IOUtils.copy(gis, out);

            return out.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    static private BufferedReader toBufferedReader(Reader reader) {
        return reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(
                reader);
    }

    public static void writeStringToFile(File file, String data, String encoding)
            throws IOException {
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            os.write(data.getBytes(encoding));
            os.flush();
        } finally {
            if (null != os) {
                os.close();
            }
        }
    }

    static public List<String> readLines(Reader input) throws IOException {
        BufferedReader reader = toBufferedReader(input);
        List<String> list = new ArrayList<String>();
        String line = null;
        for (; ; ) {
            line = reader.readLine();
            if (null != line) {
                if (StringUtils.isNotEmpty(line)) {
                    list.add(line.trim());
                }
            } else {
                break;
            }
        }
        return list;
    }

}
