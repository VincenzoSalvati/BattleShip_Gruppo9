package battleship.server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@SuppressWarnings("DuplicatedCode")
public class Server extends Thread {
    private final static int PORT = 33333;
    private final ArrayList<Players> listPlayers = new ArrayList<>();
    private List<User> clients;
    private DatagramSocket socket;

    public Server(int port) {
        try {
            // Define some variables
            this.socket = new DatagramSocket(port);
            System.out.println("Server ready!\n");
            clients = Collections.synchronizedList(new ArrayList<>());
            this.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Server(PORT);
    }

    public void run() {
        try {
            // Wait for each clients' packets
            //noinspection InfiniteLoopStatement
            while (true) {
                byte[] mess = new byte[1000];
                DatagramPacket packet = new DatagramPacket(mess, mess.length);
                socket.receive(packet);
                // Fetch message
                String s = new String(mess, 0, packet.getLength());
                String operation;
                String msgText;
                // Split user_input in operation (default string of 3 characters) and msgText (body of message)
                // If the input is just 3 characters msgTex is set to empty string
                if (s.length() == 3) {
                    operation = s.substring(0, 3);
                    msgText = "";
                } else {
                    operation = s.substring(0, 3);
                    msgText = s.substring(3);
                }
                // Create new connected Client
                User newClient = new User(msgText, packet.getAddress(), packet.getPort());
                // User login
                boolean login = true;
                if (operation.equalsIgnoreCase("LIN")) {
                    // Check the duplicate client
                    for (User cl : clients) {
                        if (cl.getUsername().equalsIgnoreCase(msgText)) {
                            // Notify the wrong connection to the Client and close the connection
                            packet = new DatagramPacket("NO".getBytes(), "NO".getBytes().length, packet.getAddress(), packet.getPort());
                            socket.send(packet);
                            login = false;
                            break;
                        }
                    }
                    // Add new user and notify the right connection to the Client
                    if (login) {
                        clients.add(newClient);
                        packet = new DatagramPacket("OK".getBytes(), "OK".getBytes().length, packet.getAddress(), packet.getPort());
                        socket.send(packet);
                        // Find Challenger
                        boolean find_challenger = false;
                        for (Players p : listPlayers) {
                            // Challenger found
                            if (p.getAddr_player2() == null) {
                                p.setUsr_player2(msgText);
                                p.setAddr_player2(newClient.getAddr());
                                p.setPORT_player2(newClient.getPort());
                                String messageToSend = "FND" + p.getUsr_player2();
                                packet = new DatagramPacket(messageToSend.getBytes(), messageToSend.getBytes().length, p.getAddr_player1(), p.getPORT_player1());
                                socket.send(packet);
                                messageToSend = "FND" + p.getUsr_player1();
                                packet = new DatagramPacket(messageToSend.getBytes(), messageToSend.getBytes().length, p.getAddr_player2(), p.getPORT_player2());
                                socket.send(packet);
                                find_challenger = true;
                                break;
                            }
                        }
                        // Challenger not found
                        if (!find_challenger) {
                            packet = new DatagramPacket("WAI".getBytes(), "WAI".getBytes().length, newClient.getAddr(), newClient.getPort());
                            socket.send(packet);
                            listPlayers.add(new Players(msgText, newClient.getAddr(), newClient.getPort()));
                        }
                    }
                }
                // Successful login
                if (login) {
                    // Switch case depending on operation string obtained from the Client
                    switch (operation) {
                        // User logout
                        case ("LOT"):
                            // Find client to remove
                            int i = 0;
                            for (User u : clients) {
                                if (u.getAddr().equals(packet.getAddress()) && (u.getPort() == packet.getPort())) {
                                    break;
                                }
                                i++;
                            }
                            // Communicate the disconnected user to the challenger
                            int j = 0;
                            InetAddress challengerAddr = null;
                            int challengerPort = 0;
                            for (Players p : listPlayers) {
                                if ((p.getAddr_player1().equals(packet.getAddress())) && (p.getPORT_player1() == packet.getPort())) {
                                    DatagramPacket packets = new DatagramPacket("LOT".getBytes(), "LOT".getBytes().length, p.getAddr_player1(), p.getPORT_player1());
                                    socket.send(packets);
                                    if (p.getAddr_player2() != null) {
                                        packets = new DatagramPacket("LOT".getBytes(), "LOT".getBytes().length, p.getAddr_player2(), p.getPORT_player2());
                                        socket.send(packets);
                                        challengerAddr = p.getAddr_player2();
                                        challengerPort = p.getPORT_player2();
                                    }
                                    break;
                                } else if (p.getAddr_player2() != null) {
                                    if (((p.getAddr_player2().equals(packet.getAddress()))) && (p.getPORT_player2() == packet.getPort())) {
                                        DatagramPacket packets = new DatagramPacket("LOT".getBytes(), "LOT".getBytes().length, p.getAddr_player1(), p.getPORT_player1());
                                        socket.send(packets);
                                        packets = new DatagramPacket("LOT".getBytes(), "LOT".getBytes().length, p.getAddr_player2(), p.getPORT_player2());
                                        socket.send(packets);
                                        challengerAddr = p.getAddr_player1();
                                        challengerPort = p.getPORT_player1();
                                        break;
                                    }
                                }
                                j++;
                            }
                            // Remove Client from the lists
                            clients.remove(i);
                            listPlayers.remove(j);
                            // Find challenger (if exist) and remove it
                            i = 0;
                            if (challengerAddr != null) {
                                for (User u : clients) {
                                    if (u.getAddr().equals(challengerAddr) && u.getPort() == challengerPort)
                                        break;
                                    i++;
                                }
                                clients.remove(i);
                            }
                            break;
                        // Start fight
                        case ("FNS"):
                            Players challenger = null;
                            // Check who has positioned all his ships
                            for (Players p : listPlayers) {
                                if (p.getAddr_player1().equals(packet.getAddress()) && p.getPORT_player1() == packet.getPort()) {
                                    p.setReady_player1(true);
                                    challenger = p;
                                    break;
                                } else if (p.getAddr_player2() != null) {
                                    if (p.getAddr_player2().equals(packet.getAddress()) && p.getPORT_player2() == packet.getPort()) {
                                        p.setReady_player2(true);
                                        challenger = p;
                                        break;
                                    }
                                }
                            }
                            // Start the fight only if the two challengers have positioned all their ships
                            if (challenger != null)
                                if (challenger.isReady_player1() && challenger.isReady_player2()) {
                                    Random rn = new Random();
                                    int choice = rn.nextInt(2);
                                    String messageToSend = "FGT" + choice + challenger.getUsr_player2();
                                    DatagramPacket packetToSend = new DatagramPacket(messageToSend.getBytes(), messageToSend.getBytes().length, challenger.getAddr_player1(), challenger.getPORT_player1());
                                    socket.send(packetToSend);
                                    if (choice == 0)
                                        choice = 1;
                                    else
                                        choice = 0;
                                    messageToSend = "FGT" + choice + challenger.getUsr_player1();
                                    packetToSend = new DatagramPacket(messageToSend.getBytes(), messageToSend.getBytes().length, challenger.getAddr_player2(), challenger.getPORT_player2());
                                    socket.send(packetToSend);
                                }
                            break;
                        // Send coordinates of the attack
                        case ("CRD"):
                            String coordinatesToSend = "ATK" + msgText;
                            for (Players p : listPlayers) {
                                if (p.getAddr_player1() != null) {
                                    if (((p.getAddr_player1().equals(packet.getAddress())) && (p.getPORT_player1() == packet.getPort()))) {
                                        DatagramPacket packets = new DatagramPacket(coordinatesToSend.getBytes(), coordinatesToSend.getBytes().length, p.getAddr_player2(), p.getPORT_player2());
                                        socket.send(packets);
                                        break;
                                    } else if (p.getAddr_player2() != null)
                                        if ((((p.getAddr_player2().equals(packet.getAddress()))) && (p.getPORT_player2() == packet.getPort()))) {
                                            DatagramPacket packets = new DatagramPacket(coordinatesToSend.getBytes(), coordinatesToSend.getBytes().length, p.getAddr_player1(), p.getPORT_player1());
                                            socket.send(packets);
                                            break;
                                        }
                                }
                            }
                            break;
                        // Send table to the challenger
                        case ("TAB"):
                            String tableToSend = "UPD" + msgText;
                            for (Players p : listPlayers) {
                                if (((p.getAddr_player1().equals(packet.getAddress())) && (p.getPORT_player1() == packet.getPort()))) {
                                    DatagramPacket packets = new DatagramPacket(tableToSend.getBytes(), tableToSend.getBytes().length, p.getAddr_player2(), p.getPORT_player2());
                                    socket.send(packets);
                                } else if (p.getAddr_player2() != null)
                                    if ((((p.getAddr_player2().equals(packet.getAddress()))) && (p.getPORT_player2() == packet.getPort()))) {
                                        DatagramPacket packets = new DatagramPacket(tableToSend.getBytes(), tableToSend.getBytes().length, p.getAddr_player1(), p.getPORT_player1());
                                        socket.send(packets);
                                        break;
                                    }
                            }
                            break;
                        // Communicate the winner
                        case ("LOS"):
                            StringBuilder winner = new StringBuilder("WIN");
                            for (Players p : listPlayers) {
                                if (p.getAddr_player1() != null) {
                                    if (((p.getAddr_player1().equals(packet.getAddress())) && (p.getPORT_player1() == packet.getPort()))) {
                                        winner.append(p.getUsr_player2());
                                        DatagramPacket packets = new DatagramPacket(winner.toString().getBytes(), winner.toString().getBytes().length, p.getAddr_player2(), p.getPORT_player2());
                                        socket.send(packets);
                                        packets = new DatagramPacket(winner.toString().getBytes(), winner.toString().getBytes().length, p.getAddr_player1(), p.getPORT_player1());
                                        socket.send(packets);
                                        break;
                                    } else if (p.getAddr_player2() != null) {
                                        if ((((p.getAddr_player2().equals(packet.getAddress()))) && (p.getPORT_player2() == packet.getPort()))) {
                                            winner.append(p.getUsr_player1());
                                            DatagramPacket packets = new DatagramPacket(winner.toString().getBytes(), winner.toString().getBytes().length, p.getAddr_player2(), p.getPORT_player2());
                                            socket.send(packets);
                                            packets = new DatagramPacket(winner.toString().getBytes(), winner.toString().getBytes().length, p.getAddr_player1(), p.getPORT_player1());
                                            socket.send(packets);
                                            break;
                                        }
                                    }
                                }
                            }
                            break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}