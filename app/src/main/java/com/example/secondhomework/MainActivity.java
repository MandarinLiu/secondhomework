package com.example.secondhomework;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.constraintlayout.widget.ConstraintLayout;



public class MainActivity extends Activity {

    private List<Item> mitemList;
    private ListView mitemListView;
    private ItemAdapter ItemAdapter;
    String mdate;
    String mitem;
    int dex;
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mitemList = new ArrayList<>();
        ItemAdapter = new ItemAdapter(MainActivity.this, mitemList);
        mitemListView = (ListView) findViewById(R.id.listview);
        mitemListView.setAdapter(ItemAdapter);


        findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ItemCreate.class);
                startActivityForResult(intent,1);
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent1) {
        super.onActivityResult(requestCode, resultCode, intent1);
        //此处可以根据两个Code进行判断，本页面和结果页面跳过来的值
        if (requestCode == 1 && resultCode == 2) {
            if (intent1 != null) {
                String mitem = intent1.getStringExtra("item");
                String mdate = intent1.getStringExtra("date");
                mitemList.add(new Item(mitem, mdate,0));
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
            }
        }
        else if (requestCode == 3 && resultCode ==4) {
            if (intent1 != null) {
                String mpro = intent1.getStringExtra("process_status");
//                ConstraintLayout layoutm = new ConstraintLayout(this);
//                View i = layoutm.findViewById(R.id.listview);
//                View i =(View)ItemAdapter.getItem(pos);
                Item i = mitemList.get(dex);
                int y =Integer.parseInt(mpro);
                if(! mpro.equals("")){
                    i.setItem_pro(y);
                    Toast.makeText(mContext, "已完成"+y+"%", Toast.LENGTH_SHORT).show();
                    ItemAdapter.notifyDataSetChanged();
                }
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
           return mItem.get(position);
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
            ProgressBar item_process;
            Button finish_Button;
            Button delete_Button;
            Button edit_Button;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            //返回一个视图
            currentPosition = position;
            final ViewHolder viewHolder;
            if (convertView == null) {
                convertView = mLay.inflate(R.layout.activity_list, null);
                viewHolder = new ViewHolder();
                convertView.setTag(viewHolder);
                // 获取控件
                viewHolder.item_todoTextView = (TextView) convertView.findViewById(R.id.item_todo);
                viewHolder.item_dateTextView = (TextView) convertView.findViewById(R.id.item_date);
                viewHolder.finish_Button = (Button) convertView.findViewById(R.id.button_finish);
                viewHolder.delete_Button = (Button) convertView.findViewById(R.id.button_delete);
                viewHolder.edit_Button = (Button) convertView.findViewById(R.id.process_edit);
                viewHolder.item_process = (ProgressBar) convertView.findViewById(R.id.process_list);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
                viewHolder.edit_Button.setTag(currentPosition);
                viewHolder.delete_Button.setTag(currentPosition);
                viewHolder.finish_Button.setTag(currentPosition);
                System.out.println(viewHolder);
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
                        mItem.remove(currentPosition);
                        Toast.makeText(mContext, "已完成", Toast.LENGTH_SHORT).show();
                        ItemAdapter.this.notifyDataSetChanged();
                    }
                });

                viewHolder.delete_Button.setOnClickListener(new View.OnClickListener() {  //为button绑定事件
                    @Override
                    public void onClick(View v) {
                        currentPosition=(int)viewHolder.delete_Button.getTag();
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

                // 和数据之间绑定
                viewHolder.item_todoTextView.setText(mItem.get(position).getItem_todo());
                viewHolder.item_dateTextView.setText(mItem.get(position).getItem_date());
                viewHolder.item_process.setProgress(mItem.get(position).getItem_pro());

                if (mItem.get(position).getItem_pro() == 100) {
                    mItem.remove(currentPosition);
                    Toast.makeText(mContext, "已完成", Toast.LENGTH_SHORT).show();
                    ItemAdapter.this.notifyDataSetChanged();
                }
                return convertView;
            }



    }
}



