package com.virtualprodigy.theknightstour.Utilities;

/**
 * Created by virtualprodigyllc on 8/10/15.
 * In the project I'm taking an old class assignment I completed
 * years ago and I'm making it better. Original it just printed out the
 * steps of the knights tour, now I'm added a graphic side to the project
 */
public class CalculateKnightMove {
    private final int TOTAL_MOVES = 8;
    //This is the dimension(number of spaces) of the board on one side. Since the board is currently being treated
    //as a square there only needs to be one variable for x && y
    public static final int boardSpaces = 8;

    //This 2d array represents the chessboard
    private final int[][] chessboard = new int[boardSpaces][boardSpaces];
    //Knight movement types
    private final int knightMovementPattern[][] = {{2, 1}, {2, -1}, {1, 2}, {1, -2}, {-1, 2},
            {-1, -2}, {-2, 1}, {-2, -1}};


    /**
     * this method kicks off the calculations for the knights tour
     * this program should work with changing the knights initial position
     * to anywhere on the board
     */
    public void startingPoint() {

        //initial starting positions
        int initialXPos = 7;
        int initialYPos = 3;
        initializeChessboard(initialXPos, initialYPos);
        getKnightsPath(initialXPos, initialYPos);
        printOutMovementPattern();
    }

    /**
     * This method sets the initial location of the Knight
     * on the chessboard as 1 and marks all the other locations
     * as 0. These values will be set later to indicate the knights
     * path
     *
     * @param initialXPos
     * @param initialYPos
     */
    private void initializeChessboard(int initialXPos, int initialYPos) {

        for (int i = 0; i < boardSpaces; i++) {
            for (int y = 0; y < boardSpaces; y++) {
                chessboard[i][y] = 0;
            }
        }
        chessboard[initialXPos][initialYPos] = 1;
    }

    /**
     * This method gets the knights path
     * along the chessboard while moving in it's L shaped pattern
     * This is achieved by taking in the intital position 0,0 and looping around the board,
     * the initial position values are passed to find next move and the next move location is found
     * afterwards the intialPos values are updated with the new location and the method starts over again
     *
     * @param initialXPos
     * @param initialYPos
     */
    private void getKnightsPath(int initialXPos, int initialYPos) {
        //this variable is the current movement number. it starts at two
        //since the knights initial position was already set and I decide against zero index
        int currentMovement = 2;
        //This array represents the next movement pattern the knight TOTAL_MOVES from it's current position
        int[] nextMovementPattern = new int[2];

        for (int i = 1; i < boardSpaces * boardSpaces; i++) {
            nextMovementPattern[0] = initialXPos;
            nextMovementPattern[1] = initialYPos;
            findNextPosition(nextMovementPattern);
            initialXPos = nextMovementPattern[0];
            initialYPos = nextMovementPattern[1];
            chessboard[initialXPos][initialYPos] = currentMovement;
            currentMovement++;
        }
    }

    /**
     * Prints out a text based representation of the
     * movements
     */
    private void printOutMovementPattern() {
        for (int i = 0; i < boardSpaces; i++) {
            for (int y = 0; y < boardSpaces; y++) {
                System.out.print(chessboard[i][y] + "  ");
            }
            System.out.println("\n");
        }
    }

    /**
     * Prints out a text based representation of the
     * movements
     */
    public String printKnightsMovementPattern() {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < boardSpaces; i++) {
            for (int y = 0; y < boardSpaces; y++) {
                stringBuffer.append(chessboard[i][y] + "  ");
            }
            stringBuffer.append("\n");
        }
        return stringBuffer.toString();
    }

    /**
     * This method checks if x & y are within the boards bounds
     * and then it checks to see if the combined coordinate in
     * the 2d array does not have a movement position selected already
     * if all of these are true then the method returns true that
     * this move is to an empty space
     *
     * @param x
     * @param y
     * @return
     */
    private boolean isAnEmptySpaceWithinBoard(int x, int y) {
        return (x < boardSpaces
                && x >= 0
                && y < boardSpaces
                && y >= 0
                && chessboard[x][y] == 0);
    }

    /**
     * This method loops and determines the the number of empty spaces
     * the knight can move from it's current position
     *
     * @param x
     * @param y
     * @return
     */
    private int getNumberOfMoves(int x, int y) {
        int numberOfMoves = 0;
        for (int loop = 0; loop < TOTAL_MOVES; loop++)
            if (isAnEmptySpaceWithinBoard(x + knightMovementPattern[loop][0], y + knightMovementPattern[loop][1])) {
                numberOfMoves++;
            }
        return numberOfMoves;
    }

    /**
     * This method takes in the initial starting point, and using to variable to represent the x and y coordinates
     * it adds the next movement pattern to the each respective value. The program then passes these values to getNumberOfMoves
     * if the number of moves is less than the number of remaining moves, the knghts position is updated to the next location
     * The method chooses the position with the least amount of next move options to prevent the knight from running out of moves before
     * it has toured the entire board
     *
     * @param nextMovementPattern
     */
    private void findNextPosition(int nextMovementPattern[]) {
        int currentX = nextMovementPattern[0];
        int currentY = nextMovementPattern[1];
        int nextX = 0;
        int nextY = 0;
        int movesFromCurrentPosition = 0;
        int remainingMoves = TOTAL_MOVES;

        for (int loop = 0; loop < TOTAL_MOVES; loop++) {
            nextX = currentX + knightMovementPattern[loop][0];
            nextY = currentY + knightMovementPattern[loop][1];
            movesFromCurrentPosition = getNumberOfMoves(nextX, nextY);

            if (isAnEmptySpaceWithinBoard(nextX, nextY) && movesFromCurrentPosition < remainingMoves) {
                nextMovementPattern[0] = nextX;
                nextMovementPattern[1] = nextY;
                remainingMoves = movesFromCurrentPosition;
            }

        }

    }

}
