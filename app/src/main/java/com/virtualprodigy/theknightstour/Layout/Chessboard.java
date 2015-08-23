package com.virtualprodigy.theknightstour.Layout;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.virtualprodigy.theknightstour.R;

/**
 * Created by virtualprodigyllc on 8/22/15.
 * This class will be used to draw the chessboard
 */
public class Chessboard {
    private Context context;
    private Resources res;
    private float boardMargins;
    private int numRowsColumns;
    private Paint paint;
    private Rect boardSquareRect;

    public Chessboard(Context context, Resources res, int numRowsColumns) {
        this.context = context;
        this.res = res;
        this.boardMargins = res.getDimension(R.dimen.chessboard_margins);
        //currently assuming the chessboard is a square
        this.numRowsColumns = numRowsColumns;
        this.paint = new Paint();
        paint.setAntiAlias(true);
        //I believe the first square on a chessboard top left is always white
        paint.setColor(Color.WHITE);
    }

    /**
     * This method takes the height and width of the surface view
     * and calculates the size(Px) of a square that will fit in the
     * dimensions for a chessboard of the specified rows and columns
     * @param surfViewHeight
     * @param surfViewWidth
     */
    private void calcaulteBoardSquareSize(int surfViewHeight, int surfViewWidth){
        //take the dimension that is smallest, subtract the
        //padding from it and then divide it by numRowsColumns
        int squareSize = (surfViewWidth < surfViewHeight) ? surfViewWidth : surfViewHeight;
        squareSize = (int) Math.ceil((squareSize - (boardMargins * 2)) / numRowsColumns);
        calculateFirstBoardRect(squareSize, surfViewHeight, surfViewWidth);
    }

    /**
     * This method determines the location of the first board
     * square as well as ensure the board is
     * centered in the surface view
     * @param squreSize
     * @param surfViewHeight
     * @param surfViewWidth
     */
    private void calculateFirstBoardRect(int squreSize, int surfViewHeight, int surfViewWidth){
        //first find the location for the top left square.
        //then check if drawing from this point allow the board to be
        //center, if not move the square
        int top;
        int left;

        //center the board within it's margins. The margins don't need to be expressed in
        //this equation because they're already factored into the square's size
        //I believe the top & left will always be greater or equal to the defined margin
        int boardEdgeLength = squreSize * numRowsColumns;
        top = (surfViewHeight - boardEdgeLength) / 2;
        left = (surfViewWidth - boardEdgeLength) / 2;

        //int left, int top, int right, int bottom
        boardSquareRect = new Rect(left, top, left + squreSize, top + squreSize);
    }

}
