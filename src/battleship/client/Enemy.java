package battleship.client;

import javax.swing.*;
import java.awt.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

@SuppressWarnings("DuplicatedCode")
public class Enemy extends JFrame {
    private final String title;
    private final DatagramSocket socket;
    private final InetAddress addr;
    private final int port;
    private final boolean start;
    private JPanel Attack_Enemy_Panel;
    private JTextArea Table_TextArea;
    private JTextField Column_TextField;
    private JTextField Row_TextField;
    private JButton Attack_Button;
    private String table;
    private String c;
    private int r;

    public Enemy(String title, DatagramSocket socket, InetAddress addr, int port, boolean start, String table) {
        this.title = title;
        this.socket = socket;
        this.addr = addr;
        this.port = port;
        this.start = start;
        this.table = table;
        initComponents();
        initButtonListeners();
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

    public void receivedAttack(String table) {
        this.table = table;
        printGridToMatrix(Table_TextArea, table);
    }

    // Initialized frame components
    private void initComponents() {
        this.setBounds(100, 100, 1500, 650);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(Attack_Enemy_Panel);
        Player p = new Player();
        String table = p.grid.printShips().toString();
        printGridToMatrix(Table_TextArea, table);
        // Set GUI components
        if (start) {
            Column_TextField.setEnabled(true);
            Row_TextField.setEnabled(true);
            Attack_Button.setEnabled(true);
        } else {
            Column_TextField.setEnabled(false);
            Row_TextField.setEnabled(false);
            Attack_Button.setEnabled(false);
        }
        this.setTitle(title);
        this.setVisible(true);
        JOptionPane.showMessageDialog(null, "Fight!!");
    }

    // Perform action listener
    private void initButtonListeners() {
        Attack_Button.addActionListener(e -> {
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
                if ((c.length() == 1) && (r >= 0 && r < 10)) {
                    int i = 0;
                    int R = 10;
                    int C = 11;
                    char[][] matrix = new char[R][C];
                    for (int r = 0; r < R; r++) {
                        for (int c = 0; c < C; c++) {
                            matrix[r][c] = table.charAt(i);
                            i++;
                        }
                    }
                    // Check if position has already been attacked
                    if (matrix[r + 1][convertLetterToInt(c) + 1] != '0' && matrix[r + 1][convertLetterToInt(c) + 1] != 'X') {
                        String msgToSend = "CRD" + c + r;
                        DatagramPacket packet = new DatagramPacket(msgToSend.getBytes(), msgToSend.getBytes().length, addr, port);
                        socket.send(packet);
                        Column_TextField.setEnabled(false);
                        Row_TextField.setEnabled(false);
                        Attack_Button.setEnabled(false);
                    } else {
                        JOptionPane.showMessageDialog(null, "This position has already been attacked!!");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Be careful to the range of the coordinates!!");
                }
                Column_TextField.setText("");
                Row_TextField.setText("");
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });
    }

    // Get Method
    public JTextField getColumn_TextField() {
        return Column_TextField;
    }

    public JTextField getRow_TextField() {
        return Row_TextField;
    }

    public JButton getAttack_Button() {
        return Attack_Button;
    }

    public Frame getFrame() {
        return this;
    }
}
