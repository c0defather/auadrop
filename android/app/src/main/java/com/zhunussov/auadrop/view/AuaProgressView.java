package com.zhunussov.auadrop.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by Kuanysh Zhunussov on 10/8/18.
 * Copyright @2018 AuaDrop. All rights reserved.
 */
public class AuaProgressView extends View {

    private static final int DEFAULT_BG_COLOR = Color.parseColor("#D1C4E9");
    private static final int DEFAULT_FILL_COLOR = Color.parseColor("#6200EA");
    private static final int DEFAULT_STROKE_WIDTH = 20;

    private float progress = 0;
    private RectF[] rects;
    private Paint bgPaint;
    private Paint fillPaint;
    private int strokeWidth = DEFAULT_STROKE_WIDTH;

    public AuaProgressView(Context context) {
        super(context);
        init();
    }

    public AuaProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AuaProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        rects = new RectF[]{new RectF(), new RectF(), new RectF()};

        bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bgPaint.setColor(DEFAULT_BG_COLOR);
        bgPaint.setStyle(Paint.Style.STROKE);
        bgPaint.setStrokeWidth(DEFAULT_STROKE_WIDTH);
        bgPaint.setStrokeCap(Paint.Cap.ROUND);

        fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fillPaint.setColor(DEFAULT_FILL_COLOR);
        fillPaint.setStyle(Paint.Style.STROKE);
        fillPaint.setStrokeWidth(DEFAULT_STROKE_WIDTH);
        fillPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawArc(rects[0], 180, 180, false, bgPaint);
        canvas.drawArc(rects[1], 180, -180, false, bgPaint);
        canvas.drawArc(rects[2], 180, 180, false, bgPaint);

        float h = getHeight();
        float w = getWidth();
        canvas.drawLine(strokeWidth / 2, h / 2, strokeWidth / 2, h - strokeWidth / 2, bgPaint);
        canvas.drawLine(w - strokeWidth / 2, h / 2, w - strokeWidth / 2, h - strokeWidth / 2, bgPaint);

        if (progress > 20) {
            canvas.drawLine(strokeWidth / 2, h / 2, strokeWidth / 2, h - strokeWidth / 2, fillPaint);
        }
        if (progress > 40) {
            canvas.drawArc(rects[0], 180, 180, false, fillPaint);
            canvas.drawLine(strokeWidth, h/2, w / 3, h/2, fillPaint);
        }
        if (progress > 60) {
            canvas.drawArc(rects[1], 180, -180, false, fillPaint);
        }
        if (progress > 80) {
            canvas.drawArc(rects[2], 180, 180, false, fillPaint);
            canvas.drawLine(w * 2 / 3, h/2, w - strokeWidth, h/2, fillPaint);
        }

        if (progress >= 0 && progress <= 20) {
            canvas.drawLine(strokeWidth / 2, h / 2 + ((20 - progress) * h / 40) - strokeWidth / 2, strokeWidth / 2, h - strokeWidth / 2, fillPaint);
        } else if (progress <= 40) {
            canvas.drawArc(rects[0], 180, 180 * (progress - 20) / 20, false, fillPaint);
            canvas.drawLine(0, h/2, w / 3 * (progress - 20) / 20, h/2, fillPaint);
        } else if (progress <= 60) {
            canvas.drawArc(rects[1], 180, -180 * (progress - 40) / 20, false, fillPaint);
        } else if (progress <= 80) {
            canvas.drawArc(rects[2], 180, 180 * (progress - 60) / 20, false, fillPaint);
            canvas.drawLine(w * 2 / 3, h/2, w * 2 / 3 + (w / 3 - strokeWidth) * (progress - 60) / 20, h/2, fillPaint);
        } else {
            canvas.drawLine(w - strokeWidth / 2, h / 2, w - strokeWidth / 2, h / 2 + ((progress - 80) * h / 40) - strokeWidth / 2, fillPaint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        final int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        setMeasuredDimension(width, height);

        rects[0].set(strokeWidth / 2, strokeWidth / 2, width / 3, height - strokeWidth / 2);
        rects[1].set(width / 3, strokeWidth / 2, 2 * width / 3, height - strokeWidth / 2);
        rects[2].set(width * 2 / 3, strokeWidth / 2, width - strokeWidth / 2, height - strokeWidth / 2);
    }

    public void setProgress(float percentage) {
        if (percentage < 0) {
            progress = 0;
        } else if (percentage > 100) {
            progress = 100;
        } else {
            progress = percentage;
        }
        invalidate();
    }

    public void setProgressAnimated(int percentage) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "progress", progress, percentage);
        objectAnimator.setDuration(500);
        objectAnimator.setInterpolator(new DecelerateInterpolator());
        objectAnimator.start();
    }
}
