package com.example.secondhomework;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.List;


public class ItemCreate extends Activity {
    private DatePicker datePicker;
    private EditText date_input;
    Handler mHandler = new Handler();
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
     String  mdate ;
     EditText mitem;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        date_input = (EditText) findViewById(R.id.date_input);
        date_input.setInputType(InputType.TYPE_NULL);
        datePicker = findViewById(R.id.date_picker);
        date_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        findViewById(R.id.btn_create).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mdate = date_input.getText().toString();
                 mitem = (EditText) findViewById(R.id.edit_name);
//               mHandler.post(new Runnable(){
//                   @Override
//                   public void run() {
//                       Intent intent = new Intent(ItemCreate.this,MainActivity.class);
//                       Item newItems = new Item(item.getText().toString(),date,0);
//                       intent.putExtra("newitem",(String)newItems);
//                       startActivity(intent);
//                   }
//               });
                Intent intent = new Intent(ItemCreate.this,MainActivity.class);
                intent.putExtra("item",mitem.getText().toString());
                intent.putExtra("date",mdate);
                setResult(2,intent);
                finish();
            }

    });
    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ItemCreate.this,MainActivity.class);
        intent.putExtra("item",mitem.getText().toString());
        intent.putExtra("date",mdate);
        setResult(2,intent);
        finish();
    }
}
