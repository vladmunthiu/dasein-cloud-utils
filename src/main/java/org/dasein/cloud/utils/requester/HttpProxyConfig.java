package org.dasein.cloud.utils.requester;

/**
 * Created by vmunthiu on 11/26/2015.
 */

public class HttpProxyConfig
{
    private String host;
    private Integer port;

    public HttpProxyConfig(String host, Integer port)
    {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public Integer getPort() {
        return port;
    }
}
