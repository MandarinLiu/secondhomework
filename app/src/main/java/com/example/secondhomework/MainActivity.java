package com.example.secondhomework;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class MainActivity extends Activity {

    private List<Item> mitemList;
    private ListView mitemListView;
    private ItemAdapter ItemAdapter;
    String mdate;
    String mitem;
    int dex;
    private Context mContext;
    String mdate_end;
    private DataBaseHelper dbHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper =new DataBaseHelper(this,"ItemList.db",null,1);
        dbHelper.getWritableDatabase();
        db=dbHelper.getWritableDatabase();


        mitemList = new ArrayList<>();
        ItemAdapter = new ItemAdapter(MainActivity.this, mitemList);
        mitemListView = (ListView) findViewById(R.id.listview);
        mitemListView.setAdapter(ItemAdapter);
        getData();

        findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ItemCreate.class);
                startActivityForResult(intent,1);
            }
        });
    }

    //读取数据库
    private void getData() {
        Cursor cursor=db.query("Item_Todo",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                String item=cursor.getString(cursor.getColumnIndex("item"));
                String date=cursor.getString(cursor.getColumnIndex("date"));
                String date_end=cursor.getString(cursor.getColumnIndex("date_end"));
                int progress=cursor.getInt(cursor.getColumnIndex("progress"));
                byte[] image=cursor.getBlob(cursor.getColumnIndex("image"));
                Item items=new Item(item,date,date_end,progress,image);
                mitemList.add(items);
            }while (cursor.moveToNext());
        }
        ItemAdapter.notifyDataSetChanged();
        cursor.close();
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent1) {
        super.onActivityResult(requestCode, resultCode, intent1);
        //此处可以根据两个Code进行判断，本页面和结果页面跳过来的值
        if (requestCode == 1 && resultCode == 2) {
            if (intent1 != null) {
//                 mitem = intent1.getStringExtra("item");
//                 mdate = intent1.getStringExtra("date");
//                mdate_end =intent1.getStringExtra("date_end");
                mitemList.add((Item) intent1.getSerializableExtra("Item"));

                //排序
                Collections.sort(mitemList, new Comparator<Object>() {
                    public int compare(Object arg0, Object arg1) {
                        Item item0 = (Item) arg0;
                        Item item1 = (Item) arg1;
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
                        int flag = item0.getItem_date().compareTo(item1.getItem_date());
                        return flag;
                    }
                });
                ItemAdapter.notifyDataSetChanged();
                // SharedPreferences sharedPreferences=MainActivity.this.getSharedPreferences("main_activity",Context.MODE_PRIVATE);
            }
        }
        else if (requestCode == 3 && resultCode ==4) {
            if (intent1 != null) {
                String mpro = intent1.getStringExtra("process_status");
                Item i = mitemList.get(dex);
                int y =Integer.parseInt(mpro);
                if(! mpro.equals("")){
                    i.setItem_pro(y);
                    Toast.makeText(mContext, "已完成"+y+"%", Toast.LENGTH_SHORT).show();
                    ItemAdapter.notifyDataSetChanged();
                }
                ContentValues values=new ContentValues();
                values.put("progress",y);
                db.update("Item_Todo",values,"item=?",new String[]{""+mitemList.get(dex).getItem_todo()});
            }
        }
    }




    //public class ItemAdapter extends BaseAdapter implements View.OnClickListener {
    class ItemAdapter extends BaseAdapter {

        private LayoutInflater mLay;
        List<Item> mItem = new ArrayList<>();
        private int currentPosition;
        private float downX;  //点下时候获取的x坐标
        private float upX;   //手指离开时候的x坐标
        private View view;


        @Override
        public int getCount() {
            return mItem.size();
        }

        @Override
        public Object getItem(int position) {
            // return mItem.get(position);
            return this.getView(position, null,(ListView) mItem);
//            View v=this.getView(position, null, (ListView)mItem);
//           return v;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public ItemAdapter(Context context, List<Item> item) {
            mItem = item;
            mContext = context;
            mLay = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void refresh(List<Item> Items) {
            mItem = Items;
            notifyDataSetChanged();
        }

        class ViewHolder {
            TextView item_todoTextView;
            TextView item_dateTextView;
            TextView item_date_endTextView;
            ProgressBar item_process;
            Button finish_Button;
            Button delete_Button;
            Button edit_Button;
            ImageView imageView;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            //返回一个视图
            currentPosition = position;
            final ViewHolder viewHolder;
            if (convertView == null) {
                convertView = mLay.inflate(R.layout.activity_list, null);
                viewHolder = new ViewHolder();
                // 获取控件
                viewHolder.item_todoTextView = (TextView) convertView.findViewById(R.id.item_todo);
                viewHolder.item_dateTextView = (TextView) convertView.findViewById(R.id.item_date);
                viewHolder.finish_Button = (Button) convertView.findViewById(R.id.button_finish);
                viewHolder.delete_Button = (Button) convertView.findViewById(R.id.button_delete);
                viewHolder.edit_Button = (Button) convertView.findViewById(R.id.process_edit);
                viewHolder.item_process = (ProgressBar) convertView.findViewById(R.id.process_list);
                viewHolder.item_date_endTextView=(TextView) convertView.findViewById(R.id.item_date_end);
                viewHolder.imageView=(ImageView) convertView.findViewById(R.id.image_contain);
                convertView.setTag(viewHolder);
                //不能删除
                viewHolder.edit_Button.setTag(currentPosition);
                viewHolder.delete_Button.setTag(currentPosition);
                viewHolder.finish_Button.setTag(currentPosition);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
                //不能删除
                viewHolder.edit_Button.setTag(currentPosition);
                viewHolder.delete_Button.setTag(currentPosition);
                viewHolder.finish_Button.setTag(currentPosition);
                //  System.out.println(viewHolder);
            }
            convertView.setOnTouchListener(new View.OnTouchListener() {  //为每个item设置setOnTouchListener事件
                public boolean onTouch(View v, MotionEvent event) {
                    final ViewHolder holder = (ViewHolder) v.getTag();  //获取滑动时候相应的ViewHolder，以便获取button按钮
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:  //手指按下
                            downX = event.getX(); //获取手指x坐标
                            if (holder.delete_Button != null) {
                                holder.delete_Button.setVisibility(View.GONE);  //影藏显示出来的button
                            }
                            break;
                        case MotionEvent.ACTION_UP:  //手指离开
                            upX = event.getX(); //获取x坐标值
                            break;
                    }

                    if (holder.delete_Button != null) {
                        if (Math.abs(downX - upX) > 35) {  //2次坐标的绝对值如果大于35，就认为是左右滑动
                            holder.delete_Button.setVisibility(View.VISIBLE);  //显示删除button
                            view = v;
                            return true; //终止事件
                        }
                        return false;  //释放事件，使onitemClick可以执行
                    }
                    return false;
                }

            });
            viewHolder.finish_Button.setOnClickListener(new View.OnClickListener() {  //为button绑定事件
                @Override
                public void onClick(View v) {
                    currentPosition=(int)viewHolder.finish_Button.getTag();
                    db.delete("Item_Todo","item=?",new String[]{mitemList.get(currentPosition).getItem_todo()});
                    db.close();
                    mItem.remove(currentPosition);
                    Toast.makeText(mContext, "已完成", Toast.LENGTH_SHORT).show();
                    ItemAdapter.this.notifyDataSetChanged();
                }
            });

            viewHolder.delete_Button.setOnClickListener(new View.OnClickListener() {  //为button绑定事件
                @Override
                public void onClick(View v) {
                    currentPosition=Integer.parseInt(viewHolder.delete_Button.getTag().toString());
                    db.delete("Item_Todo","item=?",new String[]{mitemList.get(currentPosition).getItem_todo()});
                    db.close();
                    mItem.remove(currentPosition);
                    Toast.makeText(mContext, "已删除", Toast.LENGTH_SHORT).show();
                    ItemAdapter.this.notifyDataSetChanged();
                }
            });
            viewHolder.edit_Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentPosition=(int)viewHolder.edit_Button.getTag();
                    dex = currentPosition;
                    Intent intent = new Intent(mContext, ProcessEdit.class);
                    intent.putExtra("status", mItem.get(position).getItem_pro() + "");
                    ((MainActivity) mContext).startActivityForResult(intent, 3);
                }
            });
            System.out.println("viewholder.date"+viewHolder.item_date_endTextView);
            // 和数据之间绑定
            viewHolder.item_todoTextView.setText(mItem.get(position).getItem_todo());
            viewHolder.item_dateTextView.setText("初始日期"+mItem.get(position).getItem_date());
            viewHolder.item_date_endTextView.setText("截止日期"+mItem.get(position).getItem_date_end());
            viewHolder.item_process.setProgress(mItem.get(position).getItem_pro());
            System.out.println("viewholder.image"+viewHolder.imageView);
            byte[] imgData=mItem.get(position).getImage();
            System.out.println("imgdata="+imgData);
            if (imgData!=null) {
                //将字节数组转化为位图
                Bitmap imagebitmap = BitmapFactory.decodeByteArray(imgData, 0, imgData.length);
                System.out.println("imageView"+viewHolder.imageView);
                //将位图显示为图片
                viewHolder.imageView.setImageBitmap(imagebitmap);
            }else {
                viewHolder.imageView.setBackgroundResource(android.R.drawable.menuitem_background);
            }
            //将日期转为毫秒数
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
            long mdate =0;
            try {
                mdate = simpleDateFormat.parse(mItem.get(position).getItem_date_end()).getTime();
                System.out.println("date="+mdate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(mdate<System.currentTimeMillis()){
                System.out.println(mItem.get(position).getItem_date_end());
                System.out.println(mdate);
                System.out.println(System.currentTimeMillis());
                viewHolder.item_todoTextView.setTextColor(Color.RED);
            }

            if (mItem.get(position).getItem_pro() == 100) {
                mItem.remove(position);
                Toast.makeText(mContext, "已完成", Toast.LENGTH_SHORT).show();
                ItemAdapter.this.notifyDataSetChanged();
            }
            return convertView;
        }



    }
}



