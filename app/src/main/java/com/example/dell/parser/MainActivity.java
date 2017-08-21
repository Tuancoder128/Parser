package com.example.dell.parser;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    InputStream inputStream;
    List<BaiViet> dsBaiViet;

    BaiViet baiViet;
    String noidung;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listTinTuc);

        dsBaiViet = new ArrayList<BaiViet>();

        LayDuLieuXML();
        AdapterTinTuc adapterTinTuc =  new AdapterTinTuc(this,R.layout.customlistview_layout,dsBaiViet);
        listView.setAdapter(adapterTinTuc);




    }

    private void LayDuLieuXML() {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // mở đường dẫn kết nối Internet
                    URL url = new URL("http://vietnamnet.vn/rss/xa-hoi.rss");
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.connect();
                    // lấy luồng từ RSS , cầm áp dụng  InputStreamReader để đọc file
                    inputStream = httpURLConnection.getInputStream();
                    // kiểm tra xem file đó nhận vào có đúng hay không
//                    InputStreamReader reader = new InputStreamReader(inputStream);
//                    BufferedReader bufferedReader = new BufferedReader(reader);
//                    StringBuilder builder = new StringBuilder();
//                    String docline ="";
//                    while ((docline = bufferedReader.readLine()) !=null) {
//                        builder.append(docline);
//
//                    }
//                    Log.d("du lieu", builder.toString());
                    try {
                        // tạo ra parser
                        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                        XmlPullParser xmlPullParser = factory.newPullParser();
                        xmlPullParser.setInput(inputStream, null);

                        int events = xmlPullParser.getEventType();
                        while (events != XmlPullParser.END_DOCUMENT) {
                            switch (events) {
                                // tạo thẻ mở
                                case XmlPullParser.START_TAG:
                                    String themo = xmlPullParser.getName();
                                    // nếu là thẻ mở ta sẽ gọi ra class chứa các đối tượng
                                    if (themo.equals("item")) {
                                        baiViet = new BaiViet();
                                    }
                                    //Log.d("The Mo" , "ma events = " + events + "ten the =  " + xmlPullParser.getName());
                                    ;
                                    break;
                                // tạo thẻ nội dung (Text)
                                case XmlPullParser.TEXT:
                                    noidung = xmlPullParser.getText();

                                    // Log.d("Doan Text" , "ma events = " + events + "ten the =  " + xmlPullParser.getText());
                                    ;
                                    break;
                                //taho thẻ đóng
                                case XmlPullParser.END_TAG:
                                    String thedong = xmlPullParser.getName();

                                    if (thedong.equals("title") && baiViet != null) {
                                        baiViet.setTittle(noidung);

                                    } else if (thedong.equals("description") && baiViet != null) {
                                        baiViet.setDescription(noidung);

                                    } else if (thedong.equals("link") && baiViet != null) {
                                        baiViet.setLink(noidung);

                                    } else if (thedong.equals("pubDate") && baiViet != null) {
                                        baiViet.setPubdate(noidung);

                                    } else if (thedong.equals("image") && baiViet != null) {
                                        baiViet.setImage(noidung);

                                    } else if (thedong.equals("item") && baiViet != null) {
                                        dsBaiViet.add(baiViet);
                                    }
                                    //Log.d("The Dong" , "ma events = " + events + "ten the =  " + xmlPullParser.getName());
                                    ;
                                    break;
                            }
                            events = xmlPullParser.next();

                        }
                        for (int i = 0; i < dsBaiViet.size(); i++) {
                            Log.d("dulieu", dsBaiViet.get(i).getTittle());
                        }


                    } catch (XmlPullParserException e) {
                        e.printStackTrace();
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}




