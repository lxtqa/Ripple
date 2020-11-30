package ripple.server.simulation.utils;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

/**
 * @author qingzhou.sjq
 */
public class Util {

    public static String buildURL(String ip, String api) {
        return "http://" + ip + api;
    }

    public static String buildAddress(String ip, int port) {
        return ip + ":" + port;
    }

    public static String getAcceptEncoding(HttpServletRequest req) {
        String encode = StringUtils.defaultIfEmpty(req.getHeader("Accept-Charset"), "UTF-8");
        encode = encode.contains(",") ? encode.substring(0, encode.indexOf(",")) : encode;
        return encode.contains(";") ? encode.substring(0, encode.indexOf(";")) : encode;
    }

    public static String required(HttpServletRequest req, String key) {
        String value = req.getParameter(key);
        if (StringUtils.isEmpty(value)) {
            throw new IllegalArgumentException("Param '" + key + "' is required.");
        }

        String encoding = req.getParameter("encoding");
        if (!StringUtils.isEmpty(encoding)) {
            try {
                value = new String(value.getBytes("UTF-8"), encoding);
            } catch (UnsupportedEncodingException ignore) {
            }
        }

        return value.trim();
    }
}
