package model;

import java.io.Serializable;

public class ClientDetails implements Serializable {
    private String ip;
    private int port;
    private String clientId;

    public ClientDetails(String s, int i) {
        ip = s;
        port = i;
        clientId = "";
    }

    public ClientDetails(String s, int i, String cId) {
        ip=s;
        port=i;
        clientId = cId;
    }

    public String getIp() {
        return this.ip;
    }

    public String getClientId() {
        return this.clientId;
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
        return getIp() + ":" + getPort() + ":" + clientId;
    }

    @Override
    public boolean equals(Object o) {

        if (o instanceof ClientDetails) {
            ClientDetails serverInfo = (ClientDetails) o;
            return serverInfo.toString().equals(this.toString());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }
}
