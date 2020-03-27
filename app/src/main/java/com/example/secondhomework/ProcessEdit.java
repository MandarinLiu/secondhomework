package com.example.secondhomework;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class ProcessEdit extends Activity {
    ProgressBar mprogress;
    Button finish;
    EditText edit;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_processbar);
        mprogress=findViewById(R.id.editprocess);
        finish=findViewById(R.id.processbtn_finish);
        edit = (EditText) findViewById(R.id.process_finishstaus);
        Intent intent =getIntent();
        if(intent!=null){
            String a= intent.getStringExtra("status");
            int s = Integer.parseInt(a);
            System.out.println(s);
            mprogress.setProgress(s);
        }
        finish.setOnClickListener(new View.OnClickListener() {  //为button绑定事件
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ProcessEdit.this,MainActivity.class);
                intent1.putExtra("process_status",edit.getText().toString());
               setResult(4,intent1);
               finish();
            }
        });
    }
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ProcessEdit.this,MainActivity.class);
        intent.putExtra("process_status",edit.getText());
        setResult(4,intent);
        finish();
    }
}
