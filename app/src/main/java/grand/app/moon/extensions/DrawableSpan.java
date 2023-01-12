package grand.app.moon.extensions;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.style.ReplacementSpan;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DrawableSpan extends ReplacementSpan {

    @NonNull private final Drawable drawable;

    public DrawableSpan(@NonNull Drawable drawable) {
        this.drawable = drawable;
    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, @Nullable Paint.FontMetricsInt fm) {
        return width();
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
        var xInt = (int) x;
        drawable.setBounds(xInt, top, xInt + width(), bottom);
        drawable.draw(canvas);
    }

    private int width() {
        return drawable.getIntrinsicWidth();
    }

}
