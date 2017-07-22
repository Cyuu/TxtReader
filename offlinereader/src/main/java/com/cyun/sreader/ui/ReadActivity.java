package com.cyun.sreader.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.cyun.sreader.view.BookPageFactory;
import com.cyun.sreader.view.PageWidget;
import com.cyun.sreader.R;
import com.cyun.sreader.util.VUtils;

import java.io.IOException;

public class ReadActivity extends Activity {

    private PageWidget mPageWidget;
    Bitmap mCurPageBitmap; // 上一页
    Bitmap mNextPageBitmap; // 下一页
    Canvas mCurPageCanvas; // 上一页
    Canvas mNextPageCanvas; // 下一页
    BookPageFactory pagefactory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mPageWidget = new PageWidget(this);
        setContentView(mPageWidget);

        int tWidth = VUtils.getScreenWidth(this);
        int tHeight = VUtils.getScreenHeight(this);

        mCurPageBitmap = Bitmap.createBitmap(tWidth, tHeight, Bitmap.Config.ARGB_8888);
        mNextPageBitmap = Bitmap.createBitmap(tWidth, tHeight, Bitmap.Config.ARGB_8888);

        mCurPageCanvas = new Canvas(mCurPageBitmap);
        mNextPageCanvas = new Canvas(mNextPageBitmap);
        pagefactory = new BookPageFactory(tWidth, tHeight);

        pagefactory.setBgBitmap(BitmapFactory.decodeResource(
                this.getResources(), R.drawable.bg));

        String path = null;
        try {
            Bundle bundle = getIntent().getExtras();
            path = bundle.getString("path");
        } catch (Exception e) {
            e.printStackTrace();
            path = "/sdcard/test.txt"; // Default path
        }

        try {
            pagefactory.openbook(path);
            pagefactory.onDraw(mCurPageCanvas);
        } catch (IOException e1) {
            e1.printStackTrace();
            Toast.makeText(this, "电子书不存在,请将《test.txt》放在SD卡根目录下",
                    Toast.LENGTH_LONG).show();
        }

        mPageWidget.setBitmaps(mCurPageBitmap, mCurPageBitmap);

        mPageWidget.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent e) {
                boolean ret = false;
                if (v == mPageWidget) {
                    if (e.getAction() == MotionEvent.ACTION_DOWN) {
                        mPageWidget.abortAnimation();
                        mPageWidget.calcCornerXY(e.getX(), e.getY());

                        pagefactory.onDraw(mCurPageCanvas);
                        if (mPageWidget.DragToRight()) {
                            try {
                                pagefactory.prePage();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            if (pagefactory.isfirstPage()) return false;
                            pagefactory.onDraw(mNextPageCanvas);
                        } else {
                            try {
                                pagefactory.nextPage();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            if (pagefactory.islastPage()) return false;
                            pagefactory.onDraw(mNextPageCanvas);
                        }
                        mPageWidget.setBitmaps(mCurPageBitmap, mNextPageBitmap);
                    }

                    ret = mPageWidget.doTouchEvent(e);
                    return ret;
                }
                return false;
            }

        });
    }
}