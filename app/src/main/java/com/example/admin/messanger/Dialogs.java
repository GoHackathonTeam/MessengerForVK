package com.example.admin.messanger;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Dialogs extends Activity{

    ArrayList<String> inList = new ArrayList<>();
    ArrayList<String> outList = new ArrayList<>();
    int id = 0;

    EditText text;
    ImageButton send;
    ListView listView;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.dialogs);

        inList = getIntent().getStringArrayListExtra("in");
        id = getIntent().getIntExtra("id", 0);
        Arrays.sort(inList.toArray(), Collections.reverseOrder());

        send = findViewById(R.id.sendMess);
        text = findViewById(R.id.takeMess);
        listView = findViewById(R.id.dialogsMes);
        listView.setAdapter(new CustomAdapter(this, inList, outList));


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VKRequest request = new VKRequest("messages.send", VKParameters.from(VKApiConst.USER_ID,
                        id, VKApiConst.MESSAGE, text.getText().toString()));
                request.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        super.onComplete(response);
                        text.setText("");
                        Toast.makeText(Dialogs.this, "Complete", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}