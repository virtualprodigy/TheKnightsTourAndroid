package com.virtualprodigy.theknightstour.Layout;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.virtualprodigy.theknightstour.R;

import java.util.ArrayList;

/**
 * Created by virtualprodigyllc on 8/23/15.
 * This helper class will draw the knight
 * on the chess board at each location
 */
public class Knight {

    private Context context;
    private Resources res;
    private Bitmap knightBitmap;
    //the current move to draw
    private int currentMove = 1;
    //the top & left to draw the knight at
    private int knightTop;
    private int knightLeft;
    private int[] knightsPath;
    private ArrayList<Rect> boardSquaresArray;
    private Paint paint;
    private final long drawNextInterval = 1300;
    private long lastDrawTime = -1;

    public Knight(Context context, int[] knightsPath, ArrayList<Rect> boardSquaresArray) {
        this.context = context;
        this.res = context.getResources();
        this.knightsPath = knightsPath;
        this.boardSquaresArray = boardSquaresArray;
        this.paint = new Paint();
        paint.setAntiAlias(true);
    }

    /**
     * This method takes the size of a chessboard
     * square and creates a bitmap of the knight that
     * is square 10dp smaller
     *
     * @param squareSize
     */
    public void createKnightBitmap(int squareSize) {
        int knightSize = (int) (res.getDimension(R.dimen.ten_dp) - squareSize);

        Bitmap original = BitmapFactory.decodeResource(res, R.drawable.knight, null);
        knightBitmap = Bitmap.createScaledBitmap(original, knightSize, knightSize, true);

    }


    /**
     * This mehtod will handle updating the knight
     * to draw at the next location
     */
    public void update() {
        if (knightBitmap != null) {
            //get the rect for the current position and calculate the
            //drawing coordinates for the knight
            long currentTime = System.currentTimeMillis();
            if (lastDrawTime == -1) {
                lastDrawTime = currentTime;
            } else if ((currentTime - lastDrawTime) >= drawNextInterval) {

                lastDrawTime = currentTime;
                if (currentMove == boardSquaresArray.size()) {
                    currentMove = 1;
                } else {
                    currentMove += 1;
                }
            }

            Rect currentMoveRect = null;
            for (int i = 0; i < knightsPath.length; i++) {
                if (knightsPath[i] == currentMove) {
                    currentMoveRect = boardSquaresArray.get(i);
                    break;
                }
            }

            int centerX = currentMoveRect.left + ((currentMoveRect.right - currentMoveRect.left) / 2);
            int centerY = currentMoveRect.top + ((currentMoveRect.bottom - currentMoveRect.top) / 2);

            knightLeft = centerX - knightBitmap.getWidth() / 2;
            knightTop = centerY - knightBitmap.getHeight() / 2;
        }
    }

    /**
     * This method draws the knight in the center of the current move square
     *
     * @param canvas
     */
    public void draw(Canvas canvas) {
        if (knightBitmap != null) {
            canvas.drawBitmap(knightBitmap, knightLeft, knightTop, paint);
        }
    }
}
