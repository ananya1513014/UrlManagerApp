package com.leapfin.assignment.util;

import com.leapfin.assignment.bo.UrlBo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class UrlProcessor {
    public static UrlBo processUrl(String url) throws IOException {
        UrlBo urlObj = new UrlBo(url);
        URL oracle = new URL(url);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(oracle.openStream()));
        URLConnection conn = oracle.openConnection();
        conn.connect();
        urlObj.setUseCaches(conn.getDefaultUseCaches());
        urlObj.setDoInput(conn.getDoInput());
        urlObj.setDoOutput(conn.getDoOutput());
        urlObj.setAllowUserIteraction(conn.getAllowUserInteraction());
        urlObj.setExpiration(conn.getExpiration());
        urlObj.setHeader(conn.getHeaderFields().toString().substring(0, 50));
        urlObj.setLastModified(conn.getLastModified());
        urlObj.setContent(conn.getContent().toString());
        urlObj.setEncoding(conn.getContentEncoding() );
        urlObj.setContentLength(conn.getContentLength());
        urlObj.setContentType(conn.getContentType());
        return urlObj;
    }
}
