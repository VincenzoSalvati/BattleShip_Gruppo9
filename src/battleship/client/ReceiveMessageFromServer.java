package battleship.client;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

@SuppressWarnings({"EnhancedSwitchMigration", "DuplicatedCode"})
public class ReceiveMessageFromServer extends Thread {
    private final DatagramSocket socket;
    private final Frame frame;
    private final JButton Logout_Button;
    private final JTextField Username_TextField;
    private final JTextField Column_TextField;
    private final JTextField Row_TextField;
    private final JButton Ok_Button;
    private final JButton Up_Button;
    private final JButton Right_Button;
    private final JButton Down_Button;
    private final JButton Left_Button;
    private final JTextArea Table_TextArea;
    private final JButton Aircraft_Carrier_Button;
    private final JButton Battleship_Button;
    private final JButton Destroyer_Button;
    private final JButton Submarine_Button;
    private final JButton Boat_Button;
    private final Player player;
    private final String username;
    private Enemy enemy;

    public ReceiveMessageFromServer(DatagramSocket socket, Frame frame, JButton Logout_Button, JTextField Column_TextField, JTextField Row_TextField, JButton Ok_Button, JButton Up_Button,
                                    JButton Right_Button, JButton Down_Button, JButton Left_Button, JTextArea Table_TextArea, JButton Aircraft_Carrier_Button, JButton Battleship_Button, JButton Destroyer_Button,
                                    JButton Submarine_Button, JButton Boat_Button, JTextField Username_TextField, Player player, String username) {
        this.frame = frame;
        this.socket = socket;
        this.Logout_Button = Logout_Button;
        this.Column_TextField = Column_TextField;
        this.Row_TextField = Row_TextField;
        this.Ok_Button = Ok_Button;
        this.Up_Button = Up_Button;
        this.Right_Button = Right_Button;
        this.Down_Button = Down_Button;
        this.Left_Button = Left_Button;
        this.Table_TextArea = Table_TextArea;
        this.Aircraft_Carrier_Button = Aircraft_Carrier_Button;
        this.Battleship_Button = Battleship_Button;
        this.Destroyer_Button = Destroyer_Button;
        this.Submarine_Button = Submarine_Button;
        this.Boat_Button = Boat_Button;
        this.Username_TextField = Username_TextField;
        this.player = player;
        this.username = username;
        this.start();
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

    // Convert String table into a matrix in order to correctly print it on the Text Area
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

    public void run() {
        // Receive messages received from Server
        if (socket != null) {
            //noinspection InfiniteLoopStatement
            while (true) {
                try {
                    // Init variables
                    boolean disable_Logout_Button = false;
                    boolean disable_Column_TextField = false;
                    boolean disable_Row_TextField = false;
                    boolean disable_Ok_Button = false;
                    boolean disable_Up_Button = false;
                    boolean disable_Right_Button = false;
                    boolean disable_Down_Button = false;
                    boolean disable_Left_Button = false;
                    boolean disable_Aircraft_Carrier_Button = false;
                    boolean disable_Battleship_Button = false;
                    boolean disable_Destroyer_Button = false;
                    boolean disable_Submarine_Button = false;
                    boolean disable_Boat_Button = false;
                    boolean disable_Username_TextField = false;
                    byte[] mess = new byte[1000];
                    // Read packet
                    DatagramPacket receivedPacket = new DatagramPacket(mess, mess.length);
                    socket.receive(receivedPacket);
                    // Evaluate received packet
                    String user_Input = new String(mess, 0, receivedPacket.getLength());
                    if (user_Input.length() > 0) {
                        String operation;
                        String msgText;
                        // Split user_input in operation (default string of 3 characters) and msgText (body of message)
                        // If the input is just 3 characters msgTex is set to empty string
                        if (user_Input.length() == 3) {
                            operation = user_Input.substring(0, 3);
                            msgText = "";
                        } else {
                            operation = user_Input.substring(0, 3);
                            msgText = user_Input.substring(3);
                        }
                        // Switch case depending on operation string obtained from the Server
                        switch (operation) {
                            // Challenger logout
                            case ("LOT"):
                                // Set GUI components
                                disable_Logout_Button = true;
                                disable_Column_TextField = true;
                                disable_Row_TextField = true;
                                disable_Ok_Button = true;
                                disable_Up_Button = true;
                                disable_Right_Button = true;
                                disable_Down_Button = true;
                                disable_Left_Button = true;
                                disable_Aircraft_Carrier_Button = true;
                                disable_Battleship_Button = true;
                                disable_Destroyer_Button = true;
                                disable_Submarine_Button = true;
                                disable_Boat_Button = true;
                                disable_Username_TextField = true;
                                if (enemy!=null){
                                    enemy.getColumn_TextField().setEnabled(false);
                                    enemy.getRow_TextField().setEnabled(false);
                                    enemy.getAttack_Button().setEnabled(false);
                                }
                                frame.setTitle("Challenger disconnected - End Game");
                                JOptionPane.showMessageDialog(null, "Challenger disconnected - End Game");
                                break;
                            // Wait the challenger
                            case ("WAI"):
                                // Set GUI components
                                disable_Column_TextField = true;
                                disable_Row_TextField = true;
                                disable_Ok_Button = true;
                                disable_Up_Button = true;
                                disable_Right_Button = true;
                                disable_Down_Button = true;
                                disable_Left_Button = true;
                                disable_Aircraft_Carrier_Button = true;
                                disable_Battleship_Button = true;
                                disable_Destroyer_Button = true;
                                disable_Submarine_Button = true;
                                disable_Boat_Button = true;
                                disable_Username_TextField = true;
                                JOptionPane.showMessageDialog(null, "We is looking for a new challenger...");
                                break;
                            // Challenger found
                            case ("FND"):
                                // Set GUI components
                                disable_Column_TextField = true;
                                disable_Row_TextField = true;
                                disable_Ok_Button = true;
                                disable_Up_Button = true;
                                disable_Right_Button = true;
                                disable_Down_Button = true;
                                disable_Left_Button = true;
                                disable_Username_TextField = true;
                                frame.setTitle(username + " (Connected) - Set your ships");
                                JOptionPane.showMessageDialog(null, "Fight against " + msgText);
                                break;
                            // Fight against challenger
                            case ("FGT"):
                                String start = msgText.substring(0, 1);
                                String challenger_Username = msgText.substring(1);
                                frame.setTitle(username + " (Connected) - Fight against " + challenger_Username);
                                enemy = new Enemy(challenger_Username + "'s table", socket, receivedPacket.getAddress(), receivedPacket.getPort(), Integer.parseInt(start) == 1, player.grid.printStatus().toString());
                                break;
                            // Attack from challenger
                            case ("ATK"):
                                // Set GUI components
                                disable_Column_TextField = true;
                                disable_Row_TextField = true;
                                disable_Ok_Button = true;
                                disable_Up_Button = true;
                                disable_Right_Button = true;
                                disable_Down_Button = true;
                                disable_Left_Button = true;
                                disable_Username_TextField = true;
                                int column = convertLetterToInt(msgText.charAt(0) + "");
                                int row = Integer.parseInt(msgText.charAt(1) + "");
                                if (player.grid.hasShip(row, column)) player.grid.markHIT(row, column);
                                else player.grid.markMISS(row, column);
                                String table = player.grid.printCombined().toString();
                                printGridToMatrix(Table_TextArea, table);
                                enemy.getColumn_TextField().setEnabled(true);
                                enemy.getRow_TextField().setEnabled(true);
                                enemy.getAttack_Button().setEnabled(true);
                                String messageToSend = "TAB" + player.grid.printStatus().toString();
                                DatagramPacket packetToSend = new DatagramPacket(messageToSend.getBytes(), messageToSend.getBytes().length, receivedPacket.getAddress(), receivedPacket.getPort());
                                socket.send(packetToSend);
                                if (player.grid.looser()) {
                                    messageToSend = "LOS" + player.grid.printStatus().toString();
                                    packetToSend = new DatagramPacket(messageToSend.getBytes(), messageToSend.getBytes().length, receivedPacket.getAddress(), receivedPacket.getPort());
                                    socket.send(packetToSend);
                                }
                                break;
                            // Update enemy's table
                            case ("UPD"):
                                // Set GUI components
                                disable_Column_TextField = true;
                                disable_Ok_Button = true;
                                disable_Row_TextField = true;
                                disable_Up_Button = true;
                                disable_Right_Button = true;
                                disable_Down_Button = true;
                                disable_Left_Button = true;
                                disable_Username_TextField = true;
                                enemy.receivedAttack(msgText);
                                break;
                            // Communication of the winner
                            case ("WIN"):
                                disable_Column_TextField = true;
                                disable_Row_TextField = true;
                                disable_Ok_Button = true;
                                disable_Up_Button = true;
                                disable_Right_Button = true;
                                disable_Down_Button = true;
                                disable_Left_Button = true;
                                disable_Username_TextField = true;
                                enemy.getColumn_TextField().setEnabled(false);
                                enemy.getRow_TextField().setEnabled(false);
                                enemy.getAttack_Button().setEnabled(false);
                                enemy.getFrame().setTitle("The winner is: " + msgText);
                                JOptionPane.showMessageDialog(null, "The winner is: " + msgText);
                                break;
                        }
                        // Disable or Enable buttons
                        Logout_Button.setEnabled(!disable_Logout_Button);
                        Column_TextField.setEnabled(!disable_Column_TextField);
                        Row_TextField.setEnabled(!disable_Row_TextField);
                        Ok_Button.setEnabled(!disable_Ok_Button);
                        Up_Button.setEnabled(!disable_Up_Button);
                        Right_Button.setEnabled(!disable_Right_Button);
                        Down_Button.setEnabled(!disable_Down_Button);
                        Left_Button.setEnabled(!disable_Left_Button);
                        Aircraft_Carrier_Button.setEnabled(!disable_Aircraft_Carrier_Button);
                        Battleship_Button.setEnabled(!disable_Battleship_Button);
                        Destroyer_Button.setEnabled(!disable_Destroyer_Button);
                        Submarine_Button.setEnabled(!disable_Submarine_Button);
                        Boat_Button.setEnabled(!disable_Boat_Button);
                        Username_TextField.setEnabled(!disable_Username_TextField);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
