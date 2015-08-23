package com.virtualprodigy.theknightstour.Utilities;

import android.content.Context;

import com.virtualprodigy.theknightstour.R;


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
    //this array list holds the knights path. size the drawing of chessboard is stored in a 1d array
    //having the moves in a 1d array is beneficial, once I find the number of the next moves I can use
    //the index from this to get the location of the square to draw the knight in the other array
    private final int[] knightsPath = new int[(boardSpaces * boardSpaces)];
    //Knight movement types
    private final int knightMovementPattern[][] = {{2, 1}, {2, -1}, {1, 2}, {1, -2}, {-1, 2},
            {-1, -2}, {-2, 1}, {-2, -1}};

    private Context context;

    public CalculateKnightMove(Context context) {
        this.context = context;
    }

    /**
     * this method kicks off the calculations for the knights tour
     * this program should work with changing the knights initial position
     * to anywhere on the board
     */
    public void startingPoint() {

        //initial starting positions
        int initialRowPos = 7;
        int initialColPos = 3;
        initializeChessboard(initialRowPos, initialColPos);
        getKnightsPath(initialRowPos, initialColPos);
        printOutMovementPattern();
    }

    /**
     * This method sets the initial location of the Knight
     * on the chessboard as 1 and marks all the other locations
     * as 0. These values will be set later to indicate the knights
     * path
     *
     * @param initialRowPos
     * @param initialColPos
     */
    private void initializeChessboard(int initialRowPos, int initialColPos) {

        for (int i = 0; i < boardSpaces; i++) {
            for (int y = 0; y < boardSpaces; y++) {
                chessboard[i][y] = 0;
            }
        }
        //set the knights position in the array for the board and the move list
        //the equation for a 2d to 1 is index = column + (row * width)
        chessboard[initialRowPos][initialColPos] = 1;
        knightsPath[initialRowPos * boardSpaces + initialColPos] = 1;
    }

    /**
     * This method gets the knights path
     * along the chessboard while moving in it's L shaped pattern
     * This is achieved by taking in the intital position 0,0 and looping around the board,
     * the initial position values are passed to find next move and the next move location is found
     * afterwards the intialPos values are updated with the new location and the method starts over again
     *
     * @param initialRowPos
     * @param initialColPos
     */
    private void getKnightsPath(int initialRowPos, int initialColPos) {
        //this variable is the current movement number. it starts at two
        //since the knights initial position was already set and I decide against zero index
        int currentMovement = 2;
        //This array represents the next movement pattern the knight TOTAL_MOVES from it's current position
        int[] nextMovementPattern = new int[2];

        for (int i = 1; i < boardSpaces * boardSpaces; i++) {
            nextMovementPattern[0] = initialRowPos;
            nextMovementPattern[1] = initialColPos;
            findNextPosition(nextMovementPattern);
            initialRowPos = nextMovementPattern[0];
            initialColPos = nextMovementPattern[1];
            chessboard[initialRowPos][initialColPos] = currentMovement;
            knightsPath[(initialRowPos * boardSpaces) + initialColPos] = currentMovement;
            currentMovement++;

        }
    }

    /**
     * Prints out a text based representation of the
     * movements
     */
    private void printOutMovementPattern() {
        System.out.print(context.getString(R.string.knight_text_path_header));
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
        stringBuffer.append(context.getString(R.string.knight_text_path_header));
        for (int i = 0; i < boardSpaces; i++) {
            for (int y = 0; y < boardSpaces; y++) {
                stringBuffer.append(chessboard[i][y] + " | ");
            }
            stringBuffer.append("\n");
        }

        return stringBuffer.toString();
    }

    /**
     * This method checks if row & column are within the boards bounds
     * and then it checks to see if the combined coordinate in
     * the 2d array does not have a movement position selected already
     * if all of these are true then the method returns true that
     * this move is to an empty space
     *
     * @param row
     * @param column
     * @return
     */
    private boolean isAnEmptySpaceWithinBoard(int row, int column) {
        return (row < boardSpaces
                && row >= 0
                && column < boardSpaces
                && column >= 0
                && chessboard[row][column] == 0);
    }

    /**
     * This method loops and determines the the number of empty spaces
     * the knight can move from it's current position
     *
     * @param row
     * @param column
     * @return
     */
    private int getNumberOfMoves(int row, int column) {
        int numberOfMoves = 0;
        for (int loop = 0; loop < TOTAL_MOVES; loop++)
            if (isAnEmptySpaceWithinBoard(row + knightMovementPattern[loop][0], column + knightMovementPattern[loop][1])) {
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
        int currentRow = nextMovementPattern[0];
        int currentCol = nextMovementPattern[1];
        int nextRow = 0;
        int nextCol = 0;
        int movesFromCurrentPosition = 0;
        int remainingMoves = TOTAL_MOVES;

        for (int loop = 0; loop < TOTAL_MOVES; loop++) {
            nextRow = currentRow + knightMovementPattern[loop][0];
            nextCol = currentCol + knightMovementPattern[loop][1];
            movesFromCurrentPosition = getNumberOfMoves(nextRow, nextCol);

            if (isAnEmptySpaceWithinBoard(nextRow, nextCol) && movesFromCurrentPosition < remainingMoves) {
                nextMovementPattern[0] = nextRow;
                nextMovementPattern[1] = nextCol;
                remainingMoves = movesFromCurrentPosition;
            }

        }

    }

    /**
     * This method returns the knights path
     * 1d array
     *
     * @return
     */
    public int[] getknightsPath() {
        return knightsPath;
    }

}
