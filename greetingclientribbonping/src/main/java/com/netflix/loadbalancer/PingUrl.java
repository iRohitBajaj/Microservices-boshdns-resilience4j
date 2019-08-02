package com.netflix.loadbalancer;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PingUrl implements IPing {
    ZoneAwareLoadBalancer lb;
    private static final Logger LOGGER = LoggerFactory.getLogger(PingUrl.class);
    String pingAppendString = "";
    boolean isSecure = false;
    String expectedContent = null;

    public PingUrl() {
    }

    public PingUrl(boolean isSecure, String pingAppendString) {
        this.isSecure = isSecure;
        this.pingAppendString = pingAppendString != null ? pingAppendString : "";
    }

    public void setPingAppendString(String pingAppendString) {
        this.pingAppendString = pingAppendString != null ? pingAppendString : "";
    }

    public String getPingAppendString() {
        return this.pingAppendString;
    }

    public boolean isSecure() {
        return this.isSecure;
    }

    public void setSecure(boolean isSecure) {
        this.isSecure = isSecure;
    }

    public String getExpectedContent() {
        return this.expectedContent;
    }

    public void setExpectedContent(String expectedContent) {
        this.expectedContent = expectedContent;
    }

    public boolean isAlive(Server server) {
        String urlStr = "";
        if (this.isSecure) {
            urlStr = "https://";
        } else {
            urlStr = "http://";
        }

        LOGGER.debug("hurrey server.getHostPort() "+server.getHostPort()+" server.getId()  "+server.getId()+" server.getHost()  "+ server.getHost()+"  server.getPort()  "+server.getPort());
        if(StringUtils.isNotEmpty(server.getHostPort())) {
            urlStr = urlStr + server.getHostPort();
        }
        else {
            urlStr = urlStr + server.getId();
        }
        urlStr = urlStr + this.getPingAppendString();
        boolean isAlive = false;
        HttpClient httpClient = new DefaultHttpClient();
        HttpUriRequest getRequest = new HttpGet(urlStr);
        String content = null;

        try {
            LOGGER.debug("urlstring to ping is : "+urlStr);
            HttpResponse response = httpClient.execute(getRequest);
            content = EntityUtils.toString(response.getEntity());
            isAlive = response.getStatusLine().getStatusCode() == 200;
            if (this.getExpectedContent() != null) {
                LOGGER.debug("content:" + content);
                if (content == null) {
                    isAlive = false;
                } else if (content.equals(this.getExpectedContent())) {
                    isAlive = true;
                } else {
                    isAlive = false;
                }
            }
        } catch (IOException var11) {
            LOGGER.error("exception is: "+var11.getCause().getMessage());
            var11.printStackTrace();
        } finally {
            getRequest.abort();
        }
        LOGGER.debug("returning from aliveness check : "+isAlive);
        return isAlive;
    }

    public static void main(String[] args) {
        PingUrl p = new PingUrl(false, "/cs/hostRunning");
        p.setExpectedContent("true");
        Server s = new Server("ec2-75-101-231-85.compute-1.amazonaws.com", 7101);
        boolean isAlive = p.isAlive(s);
        System.out.println("isAlive:" + isAlive);
    }
}
