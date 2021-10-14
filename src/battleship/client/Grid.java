package battleship.client;

public class Grid {
    public static final int VOID = 0;
    public static final int HIT = 1;
    public static final int MISS = 2;
    private boolean hasShip;
    private int status;
    private int lengthOfShip;
    @SuppressWarnings("unused")
    private int directionOfShip;

    public Grid() {
        status = 0;
        hasShip = false;
        lengthOfShip = -1;
        directionOfShip = -1;
    }

    public boolean hasShip() {
        return hasShip;
    }

    public void setShip(boolean hasShip) {
        this.hasShip = hasShip;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getLengthOfShip() {
        return lengthOfShip;
    }

    public void setLengthOfShip(int lengthOfShip) {
        this.lengthOfShip = lengthOfShip;
    }

    public void setDirectionOfShip(int val) {
        directionOfShip = val;
    }

    // Check if the ship is hit
    public boolean checkHIT() {
        return status == HIT;
    }

    // Check if the ship is missed
    public boolean checkMISS() {
        return status == MISS;
    }

    // Check if the box is empty
    public boolean checkVOID() {
        return status == VOID;
    }

    // Set HIT
    public void markHIT() {
        setStatus(HIT);
    }

    // Set MISS
    public void markMISS() {
        setStatus(MISS);
    }
}
