package battleship.client;

@SuppressWarnings("DuplicatedCode")
public class Table {
    public static final int ROWS = 10;
    public static final int COLS = 10;
    private final Grid[][] grid;
    private int points;

    public Table() {
        grid = new Grid[ROWS][COLS];
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                Grid tempGrid = new Grid();
                grid[row][col] = tempGrid;
            }
        }
    }

    // Set HIT
    public void markHIT(int row, int col) {
        grid[row][col].markHIT();
        points++;
    }

    // Set MISS
    public void markMISS(int row, int col) {
        grid[row][col].markMISS();
    }

    // Return the existent ship
    public boolean hasShip(int row, int col) {
        return grid[row][col].hasShip();
    }

    // Print table for enemy (hide the ship position)
    public StringBuilder printStatus() {
        return printTableToString(0);
    }

    // Print table only with the positioned ship
    public StringBuilder printShips() {
        return printTableToString(1);
    }

    // Print table with the ship and hit ship
    public StringBuilder printCombined() {
        return printTableToString(2);
    }

    public boolean looser() {
        return points >= 17;
    }

    // 0 per STATUS, 1 per SHIPS, 2 per COMBINED
    private StringBuilder printTableToString(int type) {
        StringBuilder table = new StringBuilder(" ");
        // Print Columns
        int endLetterForLoop = (ROWS - 1) + 65;
        for (int i = 65; i <= endLetterForLoop; i++) {
            char theChar = (char) i;
            table.append(theChar);
        }
        // Print Rows
        for (int i = 0; i < ROWS; i++) {
            table.append(i);
            for (int j = 0; j < COLS; j++) {
                if (type == 0) {
                    if (grid[i][j].checkVOID()) {
                        table.append("-");
                    } else if (grid[i][j].checkHIT()) {
                        table.append("X");
                    } else if (grid[i][j].checkMISS()) {
                        table.append("0");
                    }
                } else if (type == 1) {
                    if (grid[i][j].hasShip()) {
                        // "X "
                        if (grid[i][j].getLengthOfShip() == 2) {
                            table.append("S");
                        } else if (grid[i][j].getLengthOfShip() == 3) {
                            table.append("S");
                        } else if (grid[i][j].getLengthOfShip() == 4) {
                            table.append("S");
                        } else if (grid[i][j].getLengthOfShip() == 5) {
                            table.append("S");
                        }
                    } else if (!(grid[i][j].hasShip())) {
                        table.append("-");
                    }
                } else {
                    if (grid[i][j].checkHIT()) {
                        table.append("X");
                    } else if (grid[i][j].checkMISS()) {
                        table.append("0");
                    } else if (grid[i][j].hasShip()) {
                        // "X "
                        if (grid[i][j].getLengthOfShip() == 2) {
                            table.append("S");
                        } else if (grid[i][j].getLengthOfShip() == 3) {
                            table.append("S");
                        } else if (grid[i][j].getLengthOfShip() == 4) {
                            table.append("S");
                        } else if (grid[i][j].getLengthOfShip() == 5) {
                            table.append("S");
                        }
                    } else if (!(grid[i][j].hasShip())) {
                        table.append("-");
                    }
                }
            }
        }
        return table;
    }

    // Add ship to the grid
    public void addShip(Ship ship) {
        int row = ship.getRow();
        int col = ship.getCol();
        int len = ship.getLength();
        int dir = ship.getDirection();
        if (dir == 0) { // Right
            for (int i = col; i < col + len; i++) {
                grid[row][i].setShip(true);
                grid[row][i].setLengthOfShip(len);
                grid[row][i].setDirectionOfShip(dir);
            }
        } else if (dir == 1) { // Down
            for (int i = row; i < row + len; i++) {
                grid[i][col].setShip(true);
                grid[i][col].setLengthOfShip(len);
                grid[i][col].setDirectionOfShip(dir);
            }
        } else if (dir == 2) { // Left
            for (int i = col; i > col - len; i--) {
                grid[row][i].setShip(true);
                grid[row][i].setLengthOfShip(len);
                grid[row][i].setDirectionOfShip(dir);
            }
        } else { // Up
            for (int i = row; i > row - len; i--) {
                grid[i][col].setShip(true);
                grid[i][col].setLengthOfShip(len);
                grid[i][col].setDirectionOfShip(dir);
            }
        }
    }
}
