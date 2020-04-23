package com.example.secondhomework;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;


public class ItemCreate extends AppCompatActivity {
    private ImageView imageView;
    private DatePicker datePicker;
    private DatePicker datePicker_end;
    private EditText date_input;
    private EditText date_input_end;
    private byte[] imageData;
    private DataBaseHelper dbHelper;
    private SQLiteDatabase db;
    private Item mitems;
  //  Handler mHandler = new Handler();
    private void showDatePickerDialog() {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(ItemCreate.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                if(dayOfMonth <= 9)
                    date_input.setText(year+"/"+(monthOfYear+1)+"/0"+dayOfMonth);
                else
                    date_input.setText(year+"/"+(monthOfYear+1)+"/"+dayOfMonth);
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }
    private void showDatePickerDialogend() {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(ItemCreate.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                if(dayOfMonth <= 9)
                    date_input_end.setText(year+"/"+(monthOfYear+1)+"/0"+dayOfMonth);
                else
                    date_input_end.setText(year+"/"+(monthOfYear+1)+"/"+dayOfMonth);
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }
     String  mdate ;
     EditText mitem;
     String  mdate_end;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        date_input = (EditText) findViewById(R.id.date_input);
        date_input.setInputType(InputType.TYPE_NULL);
        datePicker = findViewById(R.id.date_picker);
        datePicker_end=findViewById(R.id.date_picker_end);
        date_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        date_input_end = (EditText) findViewById(R.id.date_end);
        date_input_end.setInputType(InputType.TYPE_NULL);
        date_input_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialogend();
            }
        });

        dbHelper=new DataBaseHelper(this,"ItemList.db",null,1);
        db=dbHelper.getWritableDatabase();

        imageView=findViewById(R.id.image);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent1,9);
            }
        });
        findViewById(R.id.btn_create).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mdate = date_input.getText().toString();
                mdate_end=date_input_end.getText().toString();
                 mitem = (EditText) findViewById(R.id.edit_name);

                if(!mdate.equals("")&&!mitem.getText().toString().equals("")&&!mdate_end.equals("")&& imageData !=null){
                    mitems=new Item(mitem.getText().toString(),mdate,mdate_end,0, imageData);
                }else {
                    Toast.makeText(getApplicationContext(), "事项/日期/图片不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                ContentValues values=new ContentValues();
                values.put("item",mitems.getItem_todo());
                values.put("date",mitems.getItem_date());
                values.put("date_end",mitems.getItem_date_end());
                values.put("progress",0);
                values.put("image",mitems.getImage());
                db.insert("Item_Todo",null,values);

               Intent intent = new Intent(ItemCreate.this,MainActivity.class);
                //Intent intent=new Intent();
                intent.putExtra("item",mitem.getText().toString());
                intent.putExtra("date",mdate);
                intent.putExtra("date_end",mdate_end);
                intent.putExtra("Item", mitems);
                System.out.println("addItem="+mitems);
                setResult(2,intent);
                finish();
            }

    });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 9) {
            if (resultCode == Activity.RESULT_OK) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                imageView.setImageBitmap(bitmap);
                int size = bitmap.getWidth() * bitmap.getHeight() * 4;

                ByteArrayOutputStream back= new ByteArrayOutputStream(size);
                try {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 30, back);
                    imageData = back.toByteArray();
                }catch (Exception e){
                }finally {
                    try {
                        back.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ItemCreate.this,MainActivity.class);
        intent.putExtra("item",mitem.getText().toString());
        intent.putExtra("date",mdate);
        intent.putExtra("date_end",mdate_end);
        intent.putExtra("Item", (Parcelable) mitems);
        setResult(2,intent);
        finish();
    }
}
