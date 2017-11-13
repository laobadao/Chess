package com.combanc.comgobang.surshot;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

import com.combanc.comgobang.game.GameView;

public class ViewThread extends Thread {
    private GameView mPanel;
    private SurfaceHolder mHolder;
    private boolean mRun = false;

    public ViewThread(GameView panel) {
        mPanel = panel;
        this.mHolder =mPanel.getHolder();
    }
    
    public void setRunning(boolean run) {
        mRun = run;
    }
    
    @Override
    public void run() {
        Canvas canvas = null;
        while (mRun) {
            canvas = mHolder.lockCanvas();
            if (canvas != null) {
                mPanel.doDraw(canvas);
                mHolder.unlockCanvasAndPost(canvas);
            }
        }
    }
}