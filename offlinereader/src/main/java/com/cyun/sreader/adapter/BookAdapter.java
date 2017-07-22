package com.cyun.sreader.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cyun.sreader.bean.BookLocal;
import com.cyun.sreader.R;

import java.util.List;

/**
 * 本地图书列表适配器
 */

public class BookAdapter extends BaseAdapter {
    private Context context;

    private List<BookLocal> books;

    public BookAdapter() {

    }

    public BookAdapter(Context context) {
        this.context = context;
    }

    public BookAdapter(Context context, List<BookLocal> books) {
        this.context = context;
        this.books = books;
    }

    public List<BookLocal> getBooks() {
        return books;
    }

    public void setBooks(List<BookLocal> books) {
        this.books = books;
    }

    @Override
    public int getCount() {
        return books == null ? 0 : books.size();
    }

    @Override
    public Object getItem(int i) {
        return books.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_booklocal, null);
            holder = new ViewHolder();
            holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
            holder.tv_path = (TextView) view.findViewById(R.id.tv_path);
            holder.tv_rate = (TextView) view.findViewById(R.id.tv_rate);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        final BookLocal bean = books.get(position);

        holder.tv_name.setText(bean.getName());
        holder.tv_path.setText(bean.getPath());
        holder.tv_rate.setText(bean.getRate());

        return view;
    }

    public class ViewHolder {
        TextView tv_name; // name of local disk book
        TextView tv_path; // path of local disk book
        TextView tv_rate; // schedule of have read
    }
}
