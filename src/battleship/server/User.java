package battleship.server;

import java.net.InetAddress;

public class User {
    private final String username;
    private final InetAddress addr;
    private final int port;

    public User(String username, InetAddress addr, int port) {
        this.username = username;
        this.addr = addr;
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public InetAddress getAddr() {
        return addr;
    }

    public int getPort() {
        return port;
    }
}