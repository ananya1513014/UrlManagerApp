package com.leapfin.assignment.util;

import com.leapfin.assignment.bo.UrlBo;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

public class UrlProcessor {
    public static UrlBo processUrl(String url) throws IOException {
        UrlBo urlBo = new UrlBo(url);
        URL urlObj = new URL(url);
        URLConnection conn = urlObj.openConnection();
        conn.connect();

        urlBo.setUseCaches(conn.getDefaultUseCaches());
        urlBo.setDoInput(conn.getDoInput());
        urlBo.setDoOutput(conn.getDoOutput());
        urlBo.setAllowUserIteraction(conn.getAllowUserInteraction());
        urlBo.setExpiration(conn.getExpiration());
        urlBo.setHeader(conn.getHeaderFields().toString().substring(0, 250));
        urlBo.setLastModified(conn.getLastModified());
        urlBo.setEncoding(conn.getContentEncoding() );
        urlBo.setContentLength(conn.getContentLength());
        urlBo.setContentType(conn.getContentType());
        String fileName = Long.toString(System.currentTimeMillis());
        FileUtil.writeFile(fileName, urlObj.openStream());
        urlBo.setContent(fileName);

        return urlBo;
    }
}