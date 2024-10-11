package com.example.fontcolortimer;

import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private int currentColorIndex = 0;
    private final int[] colors = {
            Color.BLUE,
            Color.CYAN,
            Color.GREEN,
            Color.MAGENTA,
            Color.RED,
            Color.YELLOW,
            Color.GRAY,
    };
    private final Handler handler = new Handler();
    private final int CHANGE_COLOR_INTERVAL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startColorChange();
    }

    private void startColorChange() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                changeWallpaperColor();
                // Повторяем вызов через 10 секунд
                handler.postDelayed(this, CHANGE_COLOR_INTERVAL);
            }
        }, CHANGE_COLOR_INTERVAL);
    }

    private void changeWallpaperColor() {
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());

        // Получаем размеры экрана
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        // Создаем изображение с использованием ARGB_8888
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(colors[currentColorIndex]);

        // Логируем выбранный цвет
        Log.d("WallpaperColor", "Changing to color: " + colors[currentColorIndex]);

        // Рисуем прямоугольник с выбранным цветом
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), paint);

        try {
            wallpaperManager.setBitmap(bitmap);
            // Переходим к следующему цвету
            currentColorIndex = (currentColorIndex + 1) % colors.length;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Останавливаем изменение цвета при уничтожении активности
        handler.removeCallbacksAndMessages(null);
    }
}