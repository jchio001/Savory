package com.savory.utils;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.TextView;

import com.savory.R;

public class AnimationUtils {

    public static void animateLikeToggle(final TextView likeToggle, final boolean isLiked) {
        Context context = likeToggle.getContext();
        final int animLength = context.getResources().getInteger(R.integer.shorter_anim_length);
        final int lightRed = context.getResources().getColor(R.color.light_red);
        final int darkGray = context.getResources().getColor(R.color.dark_gray);

        if (likeToggle.getAnimation() == null || likeToggle.getAnimation().hasEnded()) {
            ObjectAnimator animX = ObjectAnimator.ofFloat(likeToggle, "scaleX", 0.75f);
            ObjectAnimator animY = ObjectAnimator.ofFloat(likeToggle, "scaleY", 0.75f);
            AnimatorSet shrink = new AnimatorSet();
            shrink.playTogether(animX, animY);
            shrink.setDuration(animLength);
            shrink.setInterpolator(new AccelerateInterpolator());
            shrink.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {}

                @Override
                public void onAnimationEnd(Animator animation) {
                    likeToggle.setText(isLiked ? R.string.heart_filled_icon : R.string.heart_empty_icon);
                    likeToggle.setTextColor(isLiked ? lightRed : darkGray);

                    ObjectAnimator animX = ObjectAnimator.ofFloat(likeToggle, "scaleX", 1.0f);
                    ObjectAnimator animY = ObjectAnimator.ofFloat(likeToggle, "scaleY", 1.0f);
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
