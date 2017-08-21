package com.example.dell.parser;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by dell on 2017-08-19.
 */

public class AdapterTinTuc extends BaseAdapter {
    Context context;
    int layout;
    List<BaiViet> dsbaiviet;

    public AdapterTinTuc(Context context, int layout, List<BaiViet> dsDanhSach) {
        this.context = context;
        this.layout = layout;
        this.dsbaiviet = dsDanhSach;
    }

    public class ViewHolder{
        TextView txtTieuDe,txtNgayDang,txtNoiDung;
        ImageView hinhdownload;
    }

    @Override
    public int getCount() {
        return dsbaiviet.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowItem = convertView;
        if(rowItem == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowItem = inflater.inflate(layout, parent, false);

            ViewHolder viewHolder =  new ViewHolder();
            viewHolder.txtTieuDe = (TextView) rowItem.findViewById(R.id.txtTieuDe);
            viewHolder.txtNgayDang = (TextView) rowItem.findViewById(R.id.txtNgayDang);
            viewHolder.txtNoiDung= (TextView) rowItem.findViewById(R.id.txtNoiDung);
            viewHolder.hinhdownload = (ImageView) rowItem.findViewById(R.id.imgHinhDaiDien);

            rowItem.setTag(viewHolder);
        }

            ViewHolder holder = (ViewHolder) rowItem.getTag();

        holder.txtTieuDe.setText(dsbaiviet.get(position).getTittle());
        holder.txtNgayDang.setText(dsbaiviet.get(position).getPubdate());
        holder.txtNoiDung.setText(dsbaiviet.get(position).getDescription());

        String duongdan = dsbaiviet.get(position).getImage();
        DownLoadHinhAnh downLoadHinhAnh =  new DownLoadHinhAnh();
        downLoadHinhAnh.execute(duongdan);
        try {
            holder.hinhdownload.setImageBitmap(downLoadHinhAnh.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        return rowItem;
    }

    public class DownLoadHinhAnh extends AsyncTask<String, Void, Bitmap> {
        Bitmap hinhanhdownload;

        @Override
        protected Bitmap doInBackground(String... params) {

            try {
                URL url = new URL(params[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();
                // lấy ra luồng trên Internet
                InputStream inputStream = httpURLConnection.getInputStream();
                hinhanhdownload = BitmapFactory.decodeStream(inputStream);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return hinhanhdownload;
        }
    }
}
