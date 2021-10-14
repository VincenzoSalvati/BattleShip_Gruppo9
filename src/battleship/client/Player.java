package battleship.client;

public class Player {
    private final static int[] SHIP_LENGTHS = {2, 3, 3, 4, 5};
    private final static int NUM_OF_SHIPS = 5;
    public Ship[] ships;
    public Table grid;

    public Player() {
        if (NUM_OF_SHIPS != 5) {
            throw new IllegalArgumentException("The ships must be 5");
        }
        ships = new Ship[NUM_OF_SHIPS];
        for (int i = 0; i < NUM_OF_SHIPS; i++) {
            Ship tmpShip = new Ship(SHIP_LENGTHS[i]);
            ships[i] = tmpShip;
        }
        grid = new Table();
    }
}