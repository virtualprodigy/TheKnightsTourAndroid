package com.virtualprodigy.theknightstour.Layout;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.virtualprodigy.theknightstour.R;

import java.util.ArrayList;

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
    private ArrayList<Rect> boardSqauresArray;

    public Chessboard(Context context, Resources res, int numRowsColumns) {
        this.context = context;
        this.res = res;
        this.boardMargins = res.getDimension(R.dimen.chessboard_margins);
        //currently assuming the chessboard is a square
        this.numRowsColumns = numRowsColumns;
        this.paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        //I believe the first square on a chessboard top left is always white
        paint.setColor(Color.WHITE);
    }

    /**
     * This method takes the height and width of the surface view
     * and calculates the size(Px) of a square that will fit in the
     * dimensions for a chessboard of the specified rows and columns
     *
     * @param surfViewHeight
     * @param surfViewWidth
     */
    private void calcaulteBoardSquareSize(int surfViewHeight, int surfViewWidth) {
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
     *
     * @param squareSize
     * @param surfViewHeight
     * @param surfViewWidth
     */
    private void calculateFirstBoardRect(int squareSize, int surfViewHeight, int surfViewWidth) {
        //first find the location for the top left square.
        //then check if drawing from this point allow the board to be
        //center, if not move the square
        int top;
        int left;

        //center the board within it's margins. The margins don't need to be expressed in
        //this equation because they're already factored into the square's size
        //I believe the top & left will always be greater or equal to the defined margin
        int boardEdgeLength = squareSize * numRowsColumns;
        top = (surfViewHeight - boardEdgeLength) / 2;
        left = (surfViewWidth - boardEdgeLength) / 2;

        //int left, int top, int right, int bottom
        Rect boardSquareRect = new Rect(left, top, left + squareSize, top + squareSize);
        createBoardSquareArray(squareSize, boardSquareRect);
    }

    /**
     * This will create an array holding all of the board squares
     * this array will be help when drawing the knight in the
     * center of each tile
     */
    private void createBoardSquareArray(int squareSize, Rect boardSquareRect) {
        final int orgLeft = boardSquareRect.left;
        if (boardSqauresArray == null) {
            boardSqauresArray = new ArrayList<>();
        }
        //add the initial square to the array
        boardSqauresArray.add(boardSquareRect);

        for (int i = 1; i < numRowsColumns * numRowsColumns; i++) {
            //the row is complete move to the next one
            if (i % 8 == 0) {
                boardSquareRect.left = orgLeft;
                boardSquareRect.top = boardSquareRect.bottom;//the current bottom is the top of the next row
                boardSquareRect.right = orgLeft + squareSize;
                boardSquareRect.bottom = boardSquareRect.top + squareSize;
            } else {
                boardSquareRect.left = boardSquareRect.right + squareSize; //the current right is the left of the next square
                boardSquareRect.right =boardSquareRect.left + squareSize;
            }


        }
    }

    /**
     * This method draws the squares to the canvas
     */
    private void draw(Canvas canvas) {

        for (Rect boardSquare : boardSqauresArray) {
            canvas.drawRect(boardSquare, paint);

            //toggles the color of paint
            if (paint.getColor() == Color.WHITE) {
                paint.setColor(Color.BLACK);
            } else {
                paint.setColor(Color.WHITE);
            }
        }
    }

}
