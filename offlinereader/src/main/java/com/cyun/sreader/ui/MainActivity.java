package com.cyun.sreader.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.cyun.sreader.R;
import com.cyun.sreader.adapter.BookAdapter;
import com.cyun.sreader.bean.BookLocal;
import com.cyun.sreader.util.FileUtils;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;


/**
 * 1 展示txt文件列表
 * 2 浏览本地文件，返回路径
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String CODE_BOOKS = "CODE_BOOKS";

    private static final int FILE_SELECT_CODE = 0;// 选择文件的code
    private static String TAG = ScanFileActivity.class.getSimpleName();

    private Context mContext = this;

    private BookAdapter mBookAdapter;
    private List<BookLocal> bookList;

    ListView lv_books;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab_main = (FloatingActionButton) findViewById(R.id.fab_main);
        fab_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });

        initView();
        readSelectedBooks();

    }


    private void initView() {
        lv_books = (ListView) findViewById(R.id.lv_books);
        mBookAdapter = new BookAdapter(mContext);
        lv_books.setAdapter(mBookAdapter);
        bookList = new ArrayList<BookLocal>();


        lv_books.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BookLocal book = bookList.get(position);
                String path = book.getPath();

                Intent intent = new Intent(mContext, ReadActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putString("path", path);
                intent.putExtras(mBundle);
                startActivity(intent);

            }
        });

    }


    /**
     * 启动系统文件选择器
     */
    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("text/plain");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 文件选择结果处理
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    try {
                        Uri uri = data.getData();
                        Log.i(TAG, "File Uri: " + uri.toString());
                        // Get the path
                        String path = FileUtils.getPath(this, uri);

                        if (!TextUtils.isEmpty(path)) {
                            Log.i(TAG, "File Path: " + path);
                            addNewBook(path);
                        }

                        // Get the file instance
                        // File file = new File(path);
                        // Initiate the upload

                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 根据本地图书的路径生成图书类，并放入listview
     */
    private void addNewBook(String path) {
        BookLocal book = new BookLocal();
        book.setPath(path);
        book.setRate("0%");
        try {
            String[] names = path.split("/");
            String name = names[names.length - 1];
            book.setName(name);
        } catch (Exception e) {
            e.printStackTrace();
            book.setName("noname");
        }
        if (!bookList.contains(book)) {
            bookList.add(book);
            mBookAdapter.setBooks(bookList);
            mBookAdapter.notifyDataSetChanged();
            saveSelectedBooks();
        }
    }

    private void readSelectedBooks() {
        SharedPreferences sp = getPreferences(Context.MODE_PRIVATE);
        String bookStr = sp.getString(CODE_BOOKS, "");
        if (!TextUtils.isEmpty(bookStr)) {
            bookList = JSONArray.parseArray(bookStr, BookLocal.class);
            mBookAdapter.setBooks(bookList);
            mBookAdapter.notifyDataSetChanged();
        } else {
            showToast("尚无图书，请添加");
        }
    }

    private void saveSelectedBooks() {
        SharedPreferences sp = getPreferences(Context.MODE_PRIVATE);
        if (bookList != null && !bookList.isEmpty()) {
            String bookStr = JSONArray.toJSONString(bookList);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(CODE_BOOKS, bookStr);
            editor.commit();
        } else {
            showToast("尚无图书，请添加");
        }
    }

    private void showToast(String tip) {
        Toast.makeText(getApplicationContext(), tip, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            default:
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
