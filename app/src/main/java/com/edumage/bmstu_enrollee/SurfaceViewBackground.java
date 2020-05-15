package com.edumage.bmstu_enrollee;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import androidx.annotation.NonNull;

public class SurfaceViewBackground extends SurfaceView implements SurfaceHolder.Callback {

    public SurfaceViewBackground(Context context) {
        super(context);
        getHolder().addCallback(this);
    }

    public SurfaceViewBackground(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
    }

    private boolean enabled=true;

    private DrawThread drawThread;

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    public void setAnimationEnabled(boolean enabled){
        if(drawThread!=null)
        drawThread.setRunning(enabled);

        this.enabled=enabled;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        drawThread = new DrawThread(getHolder(), getResources());
        drawThread.setRunning(enabled);
        drawThread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        drawThread.setRunning(false);
        while (retry) {
            try {
                drawThread.join();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static class DrawThread extends Thread {
        ArrayList<Dot> dots;
        int amount = 50;
        boolean runFlag;
        private final SurfaceHolder surfaceHolder;
        int w;
        int h;
        int colorDot;
        int colorLine;

        DrawThread(SurfaceHolder surfaceHolder, Resources resources) {
            this.surfaceHolder = surfaceHolder;
            colorDot = resources.getColor(R.color.colorPrimary);
            colorLine = resources.getColor(R.color.lightgray);
        }

        void setRunning(boolean runFlag) {
            this.runFlag = runFlag;
        }

        private void generateDots() {
            dots = new ArrayList<>();

            for (int i = 0; i < amount; i++) {
                Dot d = new Dot();
                d.x = (float) (Math.random() * w);
                d.y = (float) (Math.random() * h);
                d.speed = (float) (Dot.defaultSpeed + Math.random() * Dot.variantSpeed);
                d.directionAngle = (int) Math.round(Math.random() * 360);
                d.color = colorDot;
                d.radius = (float) (Dot.defaultRadius + Math.random() * Dot.variantRadius);
                d.vector = new Vector
                        ((float) (Math.cos(d.directionAngle) * d.speed),
                                (float) (Math.sin(d.directionAngle) * d.speed));
                dots.add(d);
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        public void run() {
            Canvas canvas = null;
            try {
                canvas = surfaceHolder.lockCanvas(null);
                w = canvas.getWidth();
                h = canvas.getHeight();
            } catch (NullPointerException e) {
                e.printStackTrace();
            } finally {
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
            generateDots();
            long prevTime = System.currentTimeMillis();
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            while (runFlag) {
                long now = System.currentTimeMillis();
                long elapsedTime = now - prevTime;
                if (elapsedTime > 35) {
                    prevTime = now;
                } else {
                    continue;
                }

                canvas = null;
                try {
                    canvas = surfaceHolder.lockCanvas(null);
                    w = canvas.getWidth();
                    h = canvas.getHeight();

                    synchronized (surfaceHolder) {
                        canvas.drawColor(Color.WHITE);

                        ArrayList<Dot> list = (ArrayList<Dot>) dots.clone();
                        for (Iterator<Dot> iterator = list.iterator(); iterator.hasNext(); ) {
                            linkDots(iterator.next(), list, canvas, paint);
                            iterator.remove();
                        }

                        for (Dot d : dots) {
                            d.update(w, h);
                            d.draw(canvas, paint);
                        }
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                } finally {
                    if (canvas != null) {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
            }
        }

        float checkDistance(float x1, float y1, float x2, float y2) {
            return (float) Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
        }

        void linkDots(Dot d, List<Dot> list, Canvas canvas, Paint paint) {
            paint.setColor(colorLine);
            for (Dot o : list) {

                float distance = checkDistance(d.x, d.y, o.x, o.y);
                int alpha = Math.round((1 - distance / Dot.linkRadius) * 255);

                paint.setAlpha(alpha);
                paint.setStrokeWidth(2);

                if (alpha > 0) {
                    canvas.drawLine(d.x, d.y, o.x, o.y, paint);
                }
            }
        }

        private static class Dot implements Cloneable {
            int color;

            float speed = 1;
            float x;
            float y;
            float directionAngle = 0;
            float radius;

            static float variantRadius = 4;
            static float linkRadius = 200;
            static float defaultSpeed = 1;
            static float variantSpeed = 2;
            static float defaultRadius = 4;

            Vector vector;

            Dot(int color, float speed, float x, float y, float directionAngle, float radius, Vector vector) {
                this.color = color;
                this.speed = speed;
                this.x = x;
                this.y = y;
                this.directionAngle = directionAngle;
                this.radius = radius;
                this.vector = vector;
            }

            void update(float w, float h) {
                border(w, h);
                x += vector.x;
                y += vector.y;
            }

            void border(float w, float h) {
                if (x >= w || x <= 0) {
                    vector.x *= -1;
                }
                if (y >= h || y <= 0) {
                    vector.y *= -1;
                }
                if (x > w) x = w;
                if (y > h) y = h;
                if (x < 0) x = 0;
                if (y < 0) y = 0;
            }

            void draw(Canvas canvas, Paint paint) {
                paint.setColor(color);
                paint.setAlpha(255);
                canvas.drawCircle(x, y, radius, paint);
            }

            Dot() {
            }

            @NonNull
            @Override
            protected Dot clone() throws CloneNotSupportedException {
                super.clone();
                return new Dot(color, speed, x, y, directionAngle, radius, vector.clone());
            }
        }

        static class Vector implements Cloneable {
            float x;
            float y;

            Vector(float x, float y) {
                this.x = x;
                this.y = y;
            }

            @NonNull
            @Override
            protected Vector clone() throws CloneNotSupportedException {
                super.clone();
                return new Vector(x, y);

            }
        }
    }
}
