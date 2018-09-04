package com.savory.utils;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.StringRes;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.TextView;

import com.savory.R;

public class AnimationUtils {

    /**
     * Animates a toggle on a feed item, like the bookmark or like button.
     * The animation consists of shrinking and then growing the item with an overshoot interpolator.
     */
    public static void animateFeedItemToggle(
            final TextView toggle,
            final boolean isToggled,
            final int toggledColor,
            final int untoggledColor,
            @StringRes final int toggledIcon,
            @StringRes final int unToggledIcon) {
        Context context = toggle.getContext();
        final int animLength = context.getResources().getInteger(R.integer.shorter_anim_length);

        if (toggle.getAnimation() == null || toggle.getAnimation().hasEnded()) {
            ObjectAnimator animX = ObjectAnimator.ofFloat(toggle, "scaleX", 0.75f);
            ObjectAnimator animY = ObjectAnimator.ofFloat(toggle, "scaleY", 0.75f);
            AnimatorSet shrink = new AnimatorSet();
            shrink.playTogether(animX, animY);
            shrink.setDuration(animLength);
            shrink.setInterpolator(new AccelerateInterpolator());
            shrink.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {}

                @Override
                public void onAnimationEnd(Animator animation) {
                    toggle.setText(isToggled ? toggledIcon : unToggledIcon);
                    toggle.setTextColor(isToggled ? toggledColor : untoggledColor);

                    ObjectAnimator animX = ObjectAnimator.ofFloat(toggle, "scaleX", 1.0f);
                    ObjectAnimator animY = ObjectAnimator.ofFloat(toggle, "scaleY", 1.0f);
                    AnimatorSet grow = new AnimatorSet();
                    grow.playTogether(animX, animY);
                    grow.setDuration(animLength);
                    grow.setInterpolator(new AnticipateOvershootInterpolator());
                    grow.start();
                }

                @Override
                public void onAnimationCancel(Animator animation) {}

                @Override
                public void onAnimationRepeat(Animator animation) {}
            });
            shrink.start();
        }
    }
}
