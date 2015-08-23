package com.virtualprodigy.theknightstour.Layout.KnightsSurfaceView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.virtualprodigy.theknightstour.Layout.Chessboard;
import com.virtualprodigy.theknightstour.Layout.Knight;
import com.virtualprodigy.theknightstour.Utilities.CalculateKnightMove;

/**
 * Created by virtualprodigyllc on 8/21/15.
 */
public class KnightsSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private KnightsSurfaceThread thread;
    private Context context;
    private Chessboard chessboard;
    private Knight knight;
    private CalculateKnightMove knightMovements;

    public KnightsSurfaceView(Context context) {
        super(context);
        init(context);
    }

    public KnightsSurfaceView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    /**
     * A centralize method are all of the different constructs to intialize
     * the SurfaceView in the same way without dup code
     */
    private void init(Context context) {
        this.context = context;
        getHolder().addCallback(this);
        setFocusable(true);
        chessboard = new Chessboard(context, CalculateKnightMove.boardSpaces);
        knightMovements = new CalculateKnightMove(context);
        knightMovements.startingPoint();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        chessboard.prepareChessBoardToDraw(h, w);
        knight = new Knight(context, knightMovements.getknightsPath(), chessboard.getboardSquaresArray());
        knight.createKnightBitmap(chessboard.getSquareSize());
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        thread = new KnightsSurfaceThread(getHolder(), this);
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);

                thread.join();
                retry = false;
            } catch (InterruptedException e) {

            }
        }
    }

    KnightsSurfaceThread getThread() {
        return thread;
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

        }
        return true;
    }

    /**
     * This method is called by the thread before render(drawing to the canvas)
     * to perform any updates in the logic before drawing the next squence
     */
    public void update() {
        knight.update();
    }

    /**
     * This method is called by the thread class to fire off any
     * drawing by helper classes. Just like any canvas, the draw
     * order implies the layer as well
     *
     * @param canvas
     */
    public void render(Canvas canvas) {
        //draw the canvas background first
        canvas.drawColor(Color.BLUE);
        chessboard.draw(canvas);
        knight.draw(canvas);

    }

}
