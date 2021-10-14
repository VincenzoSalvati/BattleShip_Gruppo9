package battleship.client;

public class Ship {
    public static final int UP = 3;
    public static final int RIGHT = 0;
    public static final int DOWN = 1;
    public static final int LEFT = 2;
    public static final int SET = -1;
    private final int length;
    private int row;
    private int col;
    private int direction;

    public Ship(int length) {
        this.row = -1;
        this.col = -1;
        this.length = length;
        this.direction = SET;
    }

    public void setLocation(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getLength() {
        return length;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        if (direction != SET && direction != UP && direction != RIGHT && direction != DOWN && direction != LEFT)
            throw new IllegalArgumentException("Invalid direction. It must be -1, 0, or 1");
        this.direction = direction;
    }

    public String toString() {
        return "Ship: " + getRow() + ", " + getCol() + " with length " + getLength();
    }
}
