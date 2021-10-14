package battleship.client;

import javax.swing.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

@SuppressWarnings({"EnhancedSwitchMigration", "DuplicatedCode"})
public class Client extends JFrame {
    private final static int PORT = 33333;
    static InetAddress addr;
    static DatagramSocket socket;
    private static Player player;
    private static int sum_positioned_ships;
    private static DatagramPacket packet;
    private String username;
    private JPanel Attack_Panel;
    private JButton Logout_Button;
    private JButton Login_Button;
    private JButton Up_Button;
    private JButton Right_Button;
    private JButton Down_Button;
    private JButton Left_Button;
    private int ship;
    private JButton Aircraft_Carrier_Button;
    private JButton Battleship_Button;
    private JButton Destroyer_Button;
    private JButton Submarine_Button;
    private JButton Boat_Button;
    private JTextArea Table_TextArea;
    private JTextField Username_TextField;
    private JLabel Username_Label;
    private JTextField Column_TextField;
    private JTextField Row_TextField;
    private JButton Ok_Button;
    private String c;
    private int r;
    private boolean selected_submarine = false;

    public Client() {
        initComponents();
        initButtonListeners();
    }

    public static void printGridToMatrix(JTextArea textArea, String table) {
        textArea.setText("");
        int i = 0;
        textArea.append("\n");
        int R = 11;
        int C = 11;
        char[][] matrix = new char[R][C];
        for (int r = 0; r < R; r++) {
            for (int c = 0; c < C; c++) {
                matrix[r][c] = table.charAt(i);
                i++;
            }
        }
        for (char[] chars : matrix) {
            for (int c = 0; c < matrix[0].length; c++) {
                textArea.append(chars[c] + "\t");
            }
            textArea.append("\n");
            textArea.append("\n");
        }
    }

    public static void main(String[] args) {
        // Create frame
        Client frame = new Client();
        frame.setVisible(true);
        // Create new player
        player = new Player();
        sum_positioned_ships = 0;
        try {
            // Determine socket
            socket = new DatagramSocket();
            addr = InetAddress.getByName("localhost");
            // Get button reference
            JButton Logout_Button = frame.getLogout_Button();
            JButton Login_Button = frame.getLogin_Button();
            JLabel Username_Label = frame.getUsername_Label();
            JTextField Username_TextField = frame.getUsername_TextField();
            JTextField Coloumn_TextField = frame.getColumn_TextField();
            JTextField Row_TextField = frame.getY_TextField();
            JButton Up_Button = frame.getUp_Button();
            JButton Right_Button = frame.getRight_Button();
            JButton Down_Button = frame.getDown_Button();
            JButton Left_Button = frame.getLeft_Button();
            JTextArea Table_TextArea = frame.getTable_TextArea();
            JButton Aircraft_Carrier_Button = frame.getAircraft_Carrier_Button();
            JButton Battleship_Button = frame.getBattleship_Button();
            JButton Destroyer_Button = frame.getDestroyer_Button();
            JButton Submarine_Button = frame.getSubmarine_Button();
            JButton Boat_Button = frame.getBoat_Button();
            JButton Ok_Button = frame.getOk_Button();
            // Print initial table
            String table = player.grid.printShips().toString();
            printGridToMatrix(Table_TextArea, table);
            // Define packet
            byte[] mess = new byte[2];
            packet = new DatagramPacket(mess, mess.length);
            // Check unused username
            do {
                socket.receive(packet);
                // Alert message to indicate the username is already used
                if (new String(mess, 0, packet.getLength()).equals("NO")) {
                    JOptionPane.showMessageDialog(null, "Username already used, use another username");
                }
                // Set GUI components
                Logout_Button.setEnabled(false);
                Login_Button.setEnabled(true);
                Coloumn_TextField.setEnabled(false);
                Row_TextField.setEnabled(false);
                Ok_Button.setEnabled(false);
                Up_Button.setEnabled(false);
                Right_Button.setEnabled(false);
                Down_Button.setEnabled(false);
                Left_Button.setEnabled(false);
                Aircraft_Carrier_Button.setEnabled(false);
                Battleship_Button.setEnabled(false);
                Destroyer_Button.setEnabled(false);
                Submarine_Button.setEnabled(false);
                Boat_Button.setEnabled(false);
                frame.setTitle("Login to fight");
                Username_TextField.setText("");
            } while (!new String(mess, 0, packet.getLength()).equals("OK"));
            // Fetch accepted username
            String username = frame.getUsername();
            // Set GUI components
            Logout_Button.setEnabled(true);
            Login_Button.setVisible(false);
            Username_TextField.setVisible(false);
            Username_Label.setText("Username: " + frame.getUsername());
            Coloumn_TextField.setEnabled(false);
            Row_TextField.setEnabled(false);
            Ok_Button.setEnabled(false);
            Up_Button.setEnabled(false);
            Right_Button.setEnabled(false);
            Down_Button.setEnabled(false);
            Left_Button.setEnabled(false);
            Aircraft_Carrier_Button.setEnabled(false);
            Battleship_Button.setEnabled(false);
            Destroyer_Button.setEnabled(false);
            Submarine_Button.setEnabled(false);
            Boat_Button.setEnabled(false);
            Username_TextField.setText("");
            Username_TextField.setEnabled(false);
            frame.setTitle(username + " (Connected)");
            // Receive messages from Server
            new ReceiveMessageFromServer(socket, frame, Logout_Button, Coloumn_TextField, Row_TextField, Ok_Button,
                    Up_Button, Right_Button, Down_Button, Left_Button, Table_TextArea, Aircraft_Carrier_Button, Battleship_Button, Destroyer_Button,
                    Submarine_Button, Boat_Button, Username_TextField, player, username);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Convert table's letter into an integer
    private static int convertLetterToInt(String val) {
        int toReturn;
        switch (val) {
            case "A":
                toReturn = 0;
                break;
            case "B":
                toReturn = 1;
                break;
            case "C":
                toReturn = 2;
                break;
            case "D":
                toReturn = 3;
                break;
            case "E":
                toReturn = 4;
                break;
            case "F":
                toReturn = 5;
                break;
            case "G":
                toReturn = 6;
                break;
            case "H":
                toReturn = 7;
                break;
            case "I":
                toReturn = 8;
                break;
            case "J":
                toReturn = 9;
                break;
            default:
                toReturn = -1;
                break;
        }
        return toReturn;
    }

    // Initialized frame components
    private void initComponents() {
        this.setBounds(100, 100, 1300, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(Attack_Panel);
        // Set GUI components
        Column_TextField.setEnabled(false);
        Row_TextField.setEnabled(false);
        Ok_Button.setEnabled(false);
        Logout_Button.setEnabled(false);
        Up_Button.setEnabled(false);
        Right_Button.setEnabled(false);
        Down_Button.setEnabled(false);
        Left_Button.setEnabled(false);
        Aircraft_Carrier_Button.setEnabled(false);
        Battleship_Button.setEnabled(false);
        Destroyer_Button.setEnabled(false);
        Submarine_Button.setEnabled(false);
        Boat_Button.setEnabled(false);
        this.setTitle("Login to fight");
    }

    // Perform action listener
    private void initButtonListeners() {
        Login_Button.addActionListener(e -> {
            try {
                if (!Username_TextField.getText().equals("")) {
                    String msgToSend = "LIN" + Username_TextField.getText().trim();
                    username = msgToSend.substring(3);
                    DatagramPacket packet = new DatagramPacket(msgToSend.getBytes(), msgToSend.getBytes().length, addr, PORT);
                    socket.send(packet);
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });
        Logout_Button.addActionListener(e -> {
            try {
                String msgToSend = "LOT";
                username = msgToSend.substring(3);
                DatagramPacket packet = new DatagramPacket(msgToSend.getBytes(), msgToSend.getBytes().length, addr, PORT);
                socket.send(packet);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });
        Ok_Button.addActionListener(e -> {
            // Check set value of the column
            c = Column_TextField.getText().toUpperCase();
            if (!(c.charAt(0) >= 65 && c.charAt(0) <= 75)) {
                JOptionPane.showMessageDialog(null, "The field column must be a character");
                Column_TextField.setText("");
                return;
            }
            // Check set value of the row
            try {
                r = Integer.parseInt(Row_TextField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "The field row must be a number");
                Row_TextField.setText("");
                return;
            }
            try {
                // Check range of coordinates
                if ((c.length() == 1) && (r >= 0 && r <= 9)) {
                    // Set GUI compontents
                    Left_Button.setEnabled(true);
                    Up_Button.setEnabled(true);
                    Right_Button.setEnabled(true);
                    Down_Button.setEnabled(true);
                    // Verify other ships already placed
                    if (player.grid.hasShip(r, convertLetterToInt(c))) {
                        JOptionPane.showMessageDialog(null, "You can not place the ship here!");
                        Column_TextField.setText("");
                        Row_TextField.setText("");
                        Left_Button.setEnabled(false);
                        Up_Button.setEnabled(false);
                        Right_Button.setEnabled(false);
                        Down_Button.setEnabled(false);
                        return;
                    }
                    // Verify the position out of range
                    if (convertLetterToInt(c) < ship - 1) Left_Button.setEnabled(false);
                    if (r < ship - 1) Up_Button.setEnabled(false);
                    if (convertLetterToInt(c) + ship - 1 > 9) Right_Button.setEnabled(false);
                    if (r + ship - 1 > 9) Down_Button.setEnabled(false);
                    // Find other ships already placed
                    if (Left_Button.isEnabled())
                        for (int i = 0; i < ship; i++) {
                            if (player.grid.hasShip(r, convertLetterToInt(c) - i)) {
                                Left_Button.setEnabled(false);
                                break;
                            }
                        }
                    if (Up_Button.isEnabled())
                        for (int i = 0; i < ship; i++) {
                            if (player.grid.hasShip(r - i, convertLetterToInt(c))) {
                                Up_Button.setEnabled(false);
                                break;
                            }
                        }
                    if (Right_Button.isEnabled())
                        for (int i = 0; i < ship; i++) {
                            if (player.grid.hasShip(r, convertLetterToInt(c) + i)) {
                                Right_Button.setEnabled(false);
                                break;
                            }
                        }
                    if (Down_Button.isEnabled())
                        for (int i = 0; i < ship; i++) {
                            if (player.grid.hasShip(r + i, convertLetterToInt(c))) {
                                Down_Button.setEnabled(false);
                                break;
                            }
                        }
                } else {
                    JOptionPane.showMessageDialog(null, "Be careful to the range of the coordinates!!");
                    Left_Button.setEnabled(false);
                    Up_Button.setEnabled(false);
                    Right_Button.setEnabled(false);
                    Down_Button.setEnabled(false);
                    Column_TextField.setText("");
                    Row_TextField.setText("");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        Aircraft_Carrier_Button.addActionListener(e -> {
            Column_TextField.setEnabled(true);
            Row_TextField.setEnabled(true);
            Ok_Button.setEnabled(true);
            Up_Button.setEnabled(false);
            Right_Button.setEnabled(false);
            Down_Button.setEnabled(false);
            Left_Button.setEnabled(false);
            Aircraft_Carrier_Button.setEnabled(false);
            Battleship_Button.setEnabled(true);
            Destroyer_Button.setEnabled(true);
            Submarine_Button.setEnabled(true);
            Boat_Button.setEnabled(true);
            ship = TypeShips.getSize(TypeShips.AIRCRAFT);
            selected_submarine = false;
        });
        Battleship_Button.addActionListener(e -> {
            Column_TextField.setEnabled(true);
            Row_TextField.setEnabled(true);
            Ok_Button.setEnabled(true);
            Up_Button.setEnabled(false);
            Right_Button.setEnabled(false);
            Down_Button.setEnabled(false);
            Left_Button.setEnabled(false);
            Aircraft_Carrier_Button.setEnabled(true);
            Battleship_Button.setEnabled(false);
            Destroyer_Button.setEnabled(true);
            Submarine_Button.setEnabled(true);
            Boat_Button.setEnabled(true);
            ship = TypeShips.getSize(TypeShips.BATTLESHIP);
            selected_submarine = false;
        });
        Destroyer_Button.addActionListener(e -> {
            Column_TextField.setEnabled(true);
            Row_TextField.setEnabled(true);
            Ok_Button.setEnabled(true);
            Up_Button.setEnabled(false);
            Right_Button.setEnabled(false);
            Down_Button.setEnabled(false);
            Left_Button.setEnabled(false);
            Aircraft_Carrier_Button.setEnabled(true);
            Battleship_Button.setEnabled(true);
            Destroyer_Button.setEnabled(false);
            Submarine_Button.setEnabled(true);
            Boat_Button.setEnabled(true);
            ship = TypeShips.getSize(TypeShips.DESTROYER);
            selected_submarine = false;
        });
        Submarine_Button.addActionListener(e -> {
            Column_TextField.setEnabled(true);
            Row_TextField.setEnabled(true);
            Ok_Button.setEnabled(true);
            Up_Button.setEnabled(false);
            Right_Button.setEnabled(false);
            Down_Button.setEnabled(false);
            Left_Button.setEnabled(false);
            Aircraft_Carrier_Button.setEnabled(true);
            Battleship_Button.setEnabled(true);
            Destroyer_Button.setEnabled(true);
            Submarine_Button.setEnabled(false);
            Boat_Button.setEnabled(true);
            ship = TypeShips.getSize(TypeShips.SUBMARINE);
            selected_submarine = true;
        });
        Boat_Button.addActionListener(e -> {
            Column_TextField.setEnabled(true);
            Row_TextField.setEnabled(true);
            Ok_Button.setEnabled(true);
            Up_Button.setEnabled(false);
            Right_Button.setEnabled(false);
            Down_Button.setEnabled(false);
            Left_Button.setEnabled(false);
            Aircraft_Carrier_Button.setEnabled(true);
            Battleship_Button.setEnabled(true);
            Destroyer_Button.setEnabled(true);
            Submarine_Button.setEnabled(true);
            Boat_Button.setEnabled(false);
            ship = TypeShips.getSize(TypeShips.BOAT);
            selected_submarine = false;
        });
        Left_Button.addActionListener(e -> {
            Column_TextField.setEnabled(false);
            Row_TextField.setEnabled(false);
            Ok_Button.setEnabled(false);
            Up_Button.setEnabled(false);
            Right_Button.setEnabled(false);
            Down_Button.setEnabled(false);
            Left_Button.setEnabled(false);
            Column_TextField.setText("");
            Row_TextField.setText("");
            if (ship == TypeShips.getSize(TypeShips.AIRCRAFT)) {
                Aircraft_Carrier_Button.setVisible(false);
                player.ships[4].setLocation(r, convertLetterToInt(c));
                player.ships[4].setDirection(directionToInt("LEFT"));
                player.grid.addShip(player.ships[4]);
            } else if (ship == TypeShips.getSize(TypeShips.BATTLESHIP)) {
                Battleship_Button.setVisible(false);
                player.ships[3].setLocation(r, convertLetterToInt(c));
                player.ships[3].setDirection(directionToInt("LEFT"));
                player.grid.addShip(player.ships[3]);
            } else if (ship == TypeShips.getSize(TypeShips.DESTROYER) && !selected_submarine) {
                Destroyer_Button.setVisible(false);
                player.ships[2].setLocation(r, convertLetterToInt(c));
                player.ships[2].setDirection(directionToInt("LEFT"));
                player.grid.addShip(player.ships[2]);
            } else if (selected_submarine) {
                Submarine_Button.setVisible(false);
                player.ships[1].setLocation(r, convertLetterToInt(c));
                player.ships[1].setDirection(directionToInt("LEFT"));
                player.grid.addShip(player.ships[1]);
            } else {
                Boat_Button.setVisible(false);
                player.ships[0].setLocation(r, convertLetterToInt(c));
                player.ships[0].setDirection(directionToInt("LEFT"));
                player.grid.addShip(player.ships[0]);
            }
            String table = player.grid.printShips().toString();
            printGridToMatrix(Table_TextArea, table);
            sum_positioned_ships++;
            if (sum_positioned_ships == 5) {
                Logout_Button.setEnabled(true);
                Column_TextField.setVisible(false);
                Row_TextField.setVisible(false);
                Ok_Button.setVisible(false);
                Up_Button.setVisible(false);
                Right_Button.setVisible(false);
                Down_Button.setVisible(false);
                Left_Button.setVisible(false);
                Aircraft_Carrier_Button.setVisible(false);
                Battleship_Button.setVisible(false);
                Destroyer_Button.setVisible(false);
                Submarine_Button.setVisible(false);
                Boat_Button.setVisible(false);
                DatagramPacket packetToSend = new DatagramPacket("FNS".getBytes(), "FNS".getBytes().length, packet.getAddress(), packet.getPort());
                try {
                    socket.send(packetToSend);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        Up_Button.addActionListener(e -> {
            Column_TextField.setEnabled(false);
            Row_TextField.setEnabled(false);
            Ok_Button.setEnabled(false);
            Up_Button.setEnabled(false);
            Right_Button.setEnabled(false);
            Down_Button.setEnabled(false);
            Left_Button.setEnabled(false);
            Column_TextField.setText("");
            Row_TextField.setText("");
            if (ship == TypeShips.getSize(TypeShips.AIRCRAFT)) {
                Aircraft_Carrier_Button.setVisible(false);
                player.ships[4].setLocation(r, convertLetterToInt(c));
                player.ships[4].setDirection(directionToInt("UP"));
                player.grid.addShip(player.ships[4]);
            } else if (ship == TypeShips.getSize(TypeShips.BATTLESHIP)) {
                Battleship_Button.setVisible(false);
                player.ships[3].setLocation(r, convertLetterToInt(c));
                player.ships[3].setDirection(directionToInt("UP"));
                player.grid.addShip(player.ships[3]);
            } else if (ship == TypeShips.getSize(TypeShips.DESTROYER) && !selected_submarine) {
                Destroyer_Button.setVisible(false);
                player.ships[2].setLocation(r, convertLetterToInt(c));
                player.ships[2].setDirection(directionToInt("UP"));
                player.grid.addShip(player.ships[2]);
            } else if (selected_submarine) {
                Submarine_Button.setVisible(false);
                player.ships[1].setLocation(r, convertLetterToInt(c));
                player.ships[1].setDirection(directionToInt("UP"));
                player.grid.addShip(player.ships[1]);
            } else {
                Boat_Button.setVisible(false);
                player.ships[0].setLocation(r, convertLetterToInt(c));
                player.ships[0].setDirection(directionToInt("UP"));
                player.grid.addShip(player.ships[0]);
            }
            String table = player.grid.printShips().toString();
            printGridToMatrix(Table_TextArea, table);
            sum_positioned_ships++;
            if (sum_positioned_ships == 5) {
                Logout_Button.setEnabled(true);
                Column_TextField.setVisible(false);
                Row_TextField.setVisible(false);
                Ok_Button.setVisible(false);
                Up_Button.setVisible(false);
                Right_Button.setVisible(false);
                Down_Button.setVisible(false);
                Left_Button.setVisible(false);
                Aircraft_Carrier_Button.setVisible(false);
                Battleship_Button.setVisible(false);
                Destroyer_Button.setVisible(false);
                Submarine_Button.setVisible(false);
                Boat_Button.setVisible(false);
                DatagramPacket packetToSend = new DatagramPacket("FNS".getBytes(), "FNS".getBytes().length, packet.getAddress(), packet.getPort());
                try {
                    socket.send(packetToSend);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        Right_Button.addActionListener(e -> {
            Column_TextField.setEnabled(false);
            Row_TextField.setEnabled(false);
            Ok_Button.setEnabled(false);
            Up_Button.setEnabled(false);
            Right_Button.setEnabled(false);
            Down_Button.setEnabled(false);
            Left_Button.setEnabled(false);
            Column_TextField.setText("");
            Row_TextField.setText("");
            if (ship == TypeShips.getSize(TypeShips.AIRCRAFT)) {
                Aircraft_Carrier_Button.setVisible(false);
                player.ships[4].setLocation(r, convertLetterToInt(c));
                player.ships[4].setDirection(directionToInt("RIGHT"));
                player.grid.addShip(player.ships[4]);
            } else if (ship == TypeShips.getSize(TypeShips.BATTLESHIP)) {
                Battleship_Button.setVisible(false);
                player.ships[3].setLocation(r, convertLetterToInt(c));
                player.ships[3].setDirection(directionToInt("RIGHT"));
                player.grid.addShip(player.ships[3]);
            } else if (ship == TypeShips.getSize(TypeShips.DESTROYER) && !selected_submarine) {
                Destroyer_Button.setVisible(false);
                player.ships[2].setLocation(r, convertLetterToInt(c));
                player.ships[2].setDirection(directionToInt("RIGHT"));
                player.grid.addShip(player.ships[2]);
            } else if (selected_submarine) {
                Submarine_Button.setVisible(false);
                player.ships[1].setLocation(r, convertLetterToInt(c));
                player.ships[1].setDirection(directionToInt("RIGHT"));
                player.grid.addShip(player.ships[1]);
            } else {
                Boat_Button.setVisible(false);
                player.ships[0].setLocation(r, convertLetterToInt(c));
                player.ships[0].setDirection(directionToInt("RIGHT"));
                player.grid.addShip(player.ships[0]);
            }
            String table = player.grid.printShips().toString();
            printGridToMatrix(Table_TextArea, table);
            sum_positioned_ships++;
            if (sum_positioned_ships == 5) {
                Logout_Button.setEnabled(true);
                Column_TextField.setVisible(false);
                Row_TextField.setVisible(false);
                Ok_Button.setVisible(false);
                Up_Button.setVisible(false);
                Right_Button.setVisible(false);
                Down_Button.setVisible(false);
                Left_Button.setVisible(false);
                Aircraft_Carrier_Button.setVisible(false);
                Battleship_Button.setVisible(false);
                Destroyer_Button.setVisible(false);
                Submarine_Button.setVisible(false);
                Boat_Button.setVisible(false);
                DatagramPacket packetToSend = new DatagramPacket("FNS".getBytes(), "FNS".getBytes().length, packet.getAddress(), packet.getPort());
                try {
                    socket.send(packetToSend);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        Down_Button.addActionListener(e -> {
            Column_TextField.setEnabled(false);
            Row_TextField.setEnabled(false);
            Ok_Button.setEnabled(false);
            Up_Button.setEnabled(false);
            Right_Button.setEnabled(false);
            Down_Button.setEnabled(false);
            Left_Button.setEnabled(false);
            Column_TextField.setText("");
            Row_TextField.setText("");
            if (ship == TypeShips.getSize(TypeShips.AIRCRAFT)) {
                Aircraft_Carrier_Button.setVisible(false);
                player.ships[4].setLocation(r, convertLetterToInt(c));
                player.ships[4].setDirection(directionToInt("DOWN"));
                player.grid.addShip(player.ships[4]);
            } else if (ship == TypeShips.getSize(TypeShips.BATTLESHIP)) {
                Battleship_Button.setVisible(false);
                player.ships[3].setLocation(r, convertLetterToInt(c));
                player.ships[3].setDirection(directionToInt("DOWN"));
                player.grid.addShip(player.ships[3]);
            } else if (ship == TypeShips.getSize(TypeShips.DESTROYER) && !selected_submarine) {
                Destroyer_Button.setVisible(false);
                player.ships[2].setLocation(r, convertLetterToInt(c));
                player.ships[2].setDirection(directionToInt("DOWN"));
                player.grid.addShip(player.ships[2]);
            } else if (selected_submarine) {
                Submarine_Button.setVisible(false);
                player.ships[1].setLocation(r, convertLetterToInt(c));
                player.ships[1].setDirection(directionToInt("DOWN"));
                player.grid.addShip(player.ships[1]);
            } else {
                Boat_Button.setVisible(false);
                player.ships[0].setLocation(r, convertLetterToInt(c));
                player.ships[0].setDirection(directionToInt("DOWN"));
                player.grid.addShip(player.ships[0]);
            }
            String table = player.grid.printShips().toString();
            printGridToMatrix(Table_TextArea, table);
            sum_positioned_ships++;
            if (sum_positioned_ships == 5) {
                Logout_Button.setEnabled(true);
                Column_TextField.setVisible(false);
                Row_TextField.setVisible(false);
                Ok_Button.setVisible(false);
                Up_Button.setVisible(false);
                Right_Button.setVisible(false);
                Down_Button.setVisible(false);
                Left_Button.setVisible(false);
                Aircraft_Carrier_Button.setVisible(false);
                Battleship_Button.setVisible(false);
                Destroyer_Button.setVisible(false);
                Submarine_Button.setVisible(false);
                Boat_Button.setVisible(false);
                DatagramPacket packetToSend = new DatagramPacket("FNS".getBytes(), "FNS".getBytes().length, packet.getAddress(), packet.getPort());
                try {
                    socket.send(packetToSend);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    // Get methods
    public JButton getLogout_Button() {
        return Logout_Button;
    }

    public JButton getLogin_Button() {
        return Login_Button;
    }

    public JButton getUp_Button() {
        return Up_Button;
    }

    public JButton getRight_Button() {
        return Right_Button;
    }

    public JButton getDown_Button() {
        return Down_Button;
    }

    public JButton getLeft_Button() {
        return Left_Button;
    }

    public JButton getAircraft_Carrier_Button() {
        return Aircraft_Carrier_Button;
    }

    public JButton getBattleship_Button() {
        return Battleship_Button;
    }

    public JButton getDestroyer_Button() {
        return Destroyer_Button;
    }

    public JButton getSubmarine_Button() {
        return Submarine_Button;
    }

    public JButton getBoat_Button() {
        return Boat_Button;
    }

    public JTextArea getTable_TextArea() {
        return Table_TextArea;
    }

    public JTextField getUsername_TextField() {
        return Username_TextField;
    }

    public JLabel getUsername_Label() {
        return Username_Label;
    }

    public JTextField getColumn_TextField() {
        return Column_TextField;
    }

    public JTextField getY_TextField() {
        return Row_TextField;
    }

    public String getUsername() {
        return username;
    }

    public JButton getOk_Button() {
        return Ok_Button;
    }

    private int directionToInt(String string) {
        switch (string) {
            case ("UP"):
                return 3;
            case ("DOWN"):
                return 1;
            case ("RIGHT"):
                return 0;
            default:
                return 2;
        }
    }

    public enum TypeShips {
        AIRCRAFT(5),
        BATTLESHIP(4),
        DESTROYER(3),
        SUBMARINE(3),
        BOAT(2);
        private final int size;

        TypeShips(int size) {
            this.size = size;
        }

        public static int getSize(TypeShips s) {
            return s.size;
        }
    }
}
