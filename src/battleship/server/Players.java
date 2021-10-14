package battleship.server;

import java.net.InetAddress;

public class Players {
    private final String usr_player1;
    private final InetAddress addr_player1;
    private final int PORT_player1;
    private boolean ready_player1;
    private String usr_player2 = "";
    private InetAddress addr_player2 = null;
    private int PORT_player2 = 0;
    private boolean ready_player2;

    public Players(String usr_player1, InetAddress addr_player1, int PORT_player1) {
        this.usr_player1 = usr_player1;
        this.addr_player1 = addr_player1;
        this.PORT_player1 = PORT_player1;
        this.ready_player2 = false;
    }

    public boolean isReady_player1() {
        return ready_player1;
    }

    public void setReady_player1(boolean ready_player1) {
        this.ready_player1 = ready_player1;
    }

    public boolean isReady_player2() {
        return ready_player2;
    }

    public void setReady_player2(boolean ready_player2) {
        this.ready_player2 = ready_player2;
    }

    public String getUsr_player1() {
        return usr_player1;
    }

    public InetAddress getAddr_player1() {
        return addr_player1;
    }

    public int getPORT_player1() {
        return PORT_player1;
    }

    public String getUsr_player2() {
        return usr_player2;
    }

    public void setUsr_player2(String usr_player2) {
        this.usr_player2 = usr_player2;
    }

    public InetAddress getAddr_player2() {
        return addr_player2;
    }

    public void setAddr_player2(InetAddress addr_player2) {
        this.addr_player2 = addr_player2;
    }

    public int getPORT_player2() {
        return PORT_player2;
    }

    public void setPORT_player2(int PORT_player2) {
        this.PORT_player2 = PORT_player2;
    }
}
