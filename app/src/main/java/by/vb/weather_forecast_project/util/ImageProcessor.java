package by.vb.weather_forecast_project.util;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

public class ImageProcessor {
    public static void loadImage(android.content.Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);
    }
}