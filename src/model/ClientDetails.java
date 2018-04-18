package model;

import java.io.Serializable;

public class ClientDetails implements Serializable {
    private String ip;
    private int port;

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return getIp() + ":" + getPort();
    }
}
