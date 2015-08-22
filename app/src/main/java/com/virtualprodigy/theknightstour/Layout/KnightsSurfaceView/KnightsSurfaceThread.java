package com.virtualprodigy.theknightstour.Layout.KnightsSurfaceView;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

import java.util.logging.Handler;

/**
 * Created by virtualprodigyllc on 8/21/15.
 * /reusing this code from another one of my project and removing somethings I
 * don't see a need for at the moment.
 */
public class KnightsSurfaceThread extends Thread {
    private long lastStatusStore = 0;

    private final String TAG = this.getClass().getSimpleName();
    //these values are in ms
    private final static int STAT_INTERVAL = 1000;
    //max number of frame that can be skipped
    private final static int MAX_FRAME_SKIPS = 50;
    //The total frames persecond allowed
    private final static int MAX_FPS = 50;
    //the amount of time a frame should last within a second.
    private static int FRAME_PERIOD = 1000 / MAX_FPS;
    // the number of history fps used to calculate the average fps
    private final static int FPS_HISTORY_NR = 10;

    //number of frames skipped
    private long framesSkippedPerStatCycle = 0l;
    private long totalFramesSkipped = 0l;
    private long totalFrameCount = 0l;
    //how often to read the stats
    private long statusIntervalTimer = 0l;
    //number of frames rendered
    private int frameCountPerStatCycle = 0;
    //average number of frames since the start of the thread
    private double averageFps = 0.0;
    // the last FPS values
    private double fpsStore[];
    // the number of times the stat has been read
    private long statsCount = 0;

    private SurfaceHolder surfaceHolder;
    private KnightsSurfaceView knightsSurfaceView;

    private boolean isRunning;

    public KnightsSurfaceThread(SurfaceHolder surfaceHolder, KnightsSurfaceView knightsSurfaceView) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.knightsSurfaceView = knightsSurfaceView;
    }

    public void serRunning(boolean running) {
        this.isRunning = running;
    }

    @Override
    public void run() {
        Canvas canvas;

        initTimingElements();

        long startTime, timeDelta;
        int sleep = 0, framesSkip;

        //keeps the thread alive
        while (isRunning) {
            canvas = null;
            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    startTime = System.currentTimeMillis();
                    framesSkip = 0;
                    //update and draw the next frame
                    this.knightsSurfaceView.update();
                    this.knightsSurfaceView.render(canvas);
                    //gets the delta(difference) in the time it took to calculate and draw to the canvas
                    //then it puts the thread to sleep for the reminder of the frame if possible to
                    //preserve the frame rate
                    timeDelta = System.currentTimeMillis() - startTime;
                    sleep = (int) (FRAME_PERIOD - timeDelta);

                    if (sleep > 0) {
                        try {
                            Thread.sleep(sleep);
                        } catch (InterruptedException e) {
                            Log.e(TAG, "KnightsSurfaceThread failed to sleep thread", e);
                        }

                    }
                    //prevents the app from skipping to many frames because the
                    //update and renders are taking a long time
                    while (sleep < 0 && framesSkip < MAX_FRAME_SKIPS) {
                        this.knightsSurfaceView.update();
                        sleep += FRAME_PERIOD;
                        framesSkip++;
                    }
                    framesSkippedPerStatCycle += framesSkip;
                    storeStatics();
                }

            } finally {
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }

    }

    /**
     * Initials the fpsStore array with zero values.
     * The fpsStore will be used for calculating the
     * average frame rates
     */
    private void initTimingElements() {
        fpsStore = new double[FPS_HISTORY_NR];
        for (int i = 0; i < FPS_HISTORY_NR; i++) {
            fpsStore[i] = 0.0;
        }

    }

    /**
     * stores stats related to the FPS
     */
    private void storeStatics() {
        frameCountPerStatCycle++;
        totalFrameCount++;

        statusIntervalTimer += (System.currentTimeMillis() - statusIntervalTimer);

        if (statusIntervalTimer >= lastStatusStore + STAT_INTERVAL) {

            double actualFps = (frameCountPerStatCycle / (STAT_INTERVAL / 1000));

            fpsStore[(int) statsCount % FPS_HISTORY_NR] = actualFps;

            statsCount++;

            double totalFps = 0.0;

            for (int i = 0; i < FPS_HISTORY_NR; i++) {
                totalFps += fpsStore[i];
            }

            if (statsCount < FPS_HISTORY_NR) {

                averageFps = totalFps / statsCount;
            } else {
                averageFps = totalFps / FPS_HISTORY_NR;
            }

            totalFramesSkipped += framesSkippedPerStatCycle;

            framesSkippedPerStatCycle = 0;
            statusIntervalTimer = 0;
            frameCountPerStatCycle = 0;

            statusIntervalTimer = System.currentTimeMillis();
            lastStatusStore = statusIntervalTimer;

        }
    }

    public void setRunning(boolean running) {
        this.isRunning = running;
    }
}
