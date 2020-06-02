package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {
    private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);
    private String urlString;
    private String response;

    public static String getApiResponse(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        logger.debug(" Connecting url is "+ urlString);
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/ld+json");
        conn.setRequestProperty("Content-Type", "application/ld+json");
        try(InputStream inputStream = new BufferedInputStream(conn.getInputStream());) {
            return org.apache.commons.io.IOUtils.toString(inputStream, "UTF-8");
        }

    }
}