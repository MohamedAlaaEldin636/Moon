package grand.app.moon.presentation.media.image.utils.animation;

import android.animation.ValueAnimator;

import grand.app.moon.presentation.media.image.utils.ImageMatrixCorrector;


/**
 * Created by Martin on 13-10-2016.
 */

public abstract class AbsCorrectorAnimatorHandler implements ValueAnimator.AnimatorUpdateListener {

    private ImageMatrixCorrector corrector;
    private float[] values;

    public AbsCorrectorAnimatorHandler(ImageMatrixCorrector corrector) {
        this.corrector = corrector;
        this.values = new float[9];
    }

    public ImageMatrixCorrector getCorrector() {
        return corrector;
    }

    protected float[] getValues() {
        return values;
    }
}
