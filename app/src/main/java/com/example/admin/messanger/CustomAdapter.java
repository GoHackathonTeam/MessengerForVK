package com.example.admin.messanger;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiDialog;
import com.vk.sdk.api.model.VKApiMessage;
import com.vk.sdk.api.model.VKList;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {
    VKList<VKApiDialog> list;
    private ArrayList<String> users, messages;
    private Context context;

    public CustomAdapter(Context context, ArrayList<String> users, ArrayList<String> messages, VKList<VKApiDialog> list) {
        this.users = users;
        this.messages = messages;
        this.context = context;
        this.list = list;
    }

    public CustomAdapter(Context context, ArrayList<String> messages) {
        this.messages = messages;
        this.context = context;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        SetData setData = new SetData();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.user_mess, null);
        setData.user_name = view.findViewById(R.id.user1);
        setData.user_name.setText(users.get(position));

        if (list != null) {
            view = inflater.inflate(R.layout.oppps, null);
            setData.user_name = view.findViewById(R.id.user);
            setData.msg = view.findViewById(R.id.msg);

            setData.user_name.setText(users.get(position));
            setData.msg.setText(messages.get(position));
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ArrayList<String> inList = new ArrayList<>();
                    final int id = list.get(position).message.user_id;

                    dialogsHistory(inList, id);
                }
            });
        }

        return view;
    }

    public void dialogsHistory(final ArrayList<String> inList, final int id){
        VKRequest request = new VKRequest("messages.getHistory", VKParameters.from(VKApiConst.USER_ID, id));
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {

                super.onComplete(response);
                try {
                    JSONArray array = response.json.getJSONObject("response").getJSONArray("items");
                    int arrLen = array.length();
                    VKApiMessage[] msg = new VKApiMessage[arrLen];
                    for (int i = 0; i < arrLen; i++) {
                        VKApiMessage mes = new VKApiMessage(array.getJSONObject(i));
                        msg[i] = mes;
                    }
                    for (VKApiMessage mess : msg) {
                        if (mess.out)
                            inList.add("YOU: " + mess.body + "\n");
                        else
                            inList.add("FRIEND: " + mess.body +  "\n");
                    }
                    CustomAdapter.this.context.startActivity(new Intent(context, Dialogs.class).putExtra("id", id)
                            .putExtra("in", inList));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public class SetData {
        TextView user_name, msg;
    }
}
