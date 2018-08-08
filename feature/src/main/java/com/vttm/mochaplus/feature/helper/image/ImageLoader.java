package com.vttm.mochaplus.feature.helper.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.NotificationTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.vttm.mochaplus.feature.R;
import com.vttm.mochaplus.feature.utils.ViewUtils;

import java.util.Random;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Created by HaiKE on 14/07/16.
 */
public class ImageLoader {

    private static final String TAG = ImageLoader.class.getSimpleName();
    public static int FADE_TIME = 1000;

    public static void setImage(Context context, String url, final ImageView imageView) {
        Random random = new Random();
        int index = random.nextInt(20);
        GlideApp.with(context)
                .load(url)
                .placeholder(context.getResources().getDrawable(R.color.color_animation))
                .error(context.getResources().getDrawable(R.color.color_animation))
                .dontAnimate()
                .transition(withCrossFade(R.anim.fade_in, FADE_TIME))
                .into(imageView);
    }

    public static void setImage(Context context, String url, String imageSmall, final ImageView imageView) {
        Random random = new Random();
        int index = random.nextInt(20);
        GlideApp.with(context)
                .load(url)
//                .placeholder(context.getResources().getDrawable(R.color.color_animation))
                .thumbnail(GlideApp.with(context).load(imageSmall))
                .error(context.getResources().getDrawable(R.color.color_animation))
                .dontAnimate()
                .transition(withCrossFade(R.anim.fade_in, FADE_TIME))
                .into(imageView);
    }

    public static void setImage(Context context, int drawable, final ImageView imageView) {
        GlideApp.with(context)
                .load(drawable)
                .placeholder(context.getResources().getDrawable(R.color.color_animation))
                .error(context.getResources().getDrawable(R.color.color_animation))
                .dontAnimate()
                .transition(withCrossFade(R.anim.fade_in, FADE_TIME))
                .into(imageView);
    }

    public static void setImageSquare(Context context, String url, final ImageView imageView) {
        Random random = new Random();
        int index = random.nextInt(20);
        GlideApp.with(context)
                .load(url)
                .placeholder(context.getResources().getDrawable(R.color.color_animation))
                .error(context.getResources().getDrawable(R.color.color_animation))
                .dontAnimate()
                .transition(withCrossFade(R.anim.fade_in, 1000))
                .into(imageView);
    }

    public static void setImageNoAnimation(Context context, String url, final ImageView imageView) {
        GlideApp.with(context)
                .load(url)
                .placeholder(context.getResources().getDrawable(R.color.color_animation))
                .error(context.getResources().getDrawable(R.color.color_animation))
                .dontAnimate()
//                .crossFade(FADE_TIME)//Cirle image ko dung cai nay
                .into(imageView);
    }

    public static void setImageBorder(Context context, String url, final ImageView imageView) {
        GlideApp.with(context)
                .load(url)
                .dontAnimate()
                .placeholder(context.getResources().getDrawable(R.color.color_animation))
                .error(context.getResources().getDrawable(R.color.color_animation))
                .transform(new RoundedCornersTransformation(context, ViewUtils.dpToPx(5), 0))
                .centerCrop()
                .into(imageView);
    }

    public static void setImage(Context context, String url, final ImageLoaderCallback callback) {
        GlideApp.with(context)
                .asBitmap()
                .load(url)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        callback.onCompleted(resource);
                    }

                });
    }

    public static void setImageBlurBuilder(Context context, String url, final ImageView image, final int radius) {
        try {
            GlideApp.with(context)
                    .load(url)
//                    .placeholder(R.drawable.bg_player1)
//                    .error(R.drawable.bg_player1)
                    .transform(new BlurTransformation(context, radius))
                    .into(image);
        } catch (OutOfMemoryError e) {
            Log.e(TAG, e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public static void setImageCircle(Context context, String str, final ImageView imageView) {
        Glide.with(context)
                .load(str)
                .apply(RequestOptions.circleCropTransform())
                .into(imageView);
    }

    public static void setNotification(Context context, String url, int placeholder, int error, NotificationTarget target) {
        try {
            GlideApp.with(context)
                    .asBitmap()
                    .load(url)
                    .placeholder(placeholder)
                    .apply(new RequestOptions()
                            .error(error)
                            .placeholder(placeholder)
                            .priority(Priority.IMMEDIATE)
                            .dontTransform()
                    )
                    .into(target);
        } catch (OutOfMemoryError e) {
            Log.e(TAG, e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }
}
