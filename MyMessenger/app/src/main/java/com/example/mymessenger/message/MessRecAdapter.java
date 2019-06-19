package com.example.mymessenger.message;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.mymassanger.R;
import com.example.mymessenger.users.UserListFr;
import java.util.ArrayList;


public class MessRecAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<MessengerItem> list;

    MessRecAdapter(ArrayList<MessengerItem> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater layoutInflater = (LayoutInflater) viewGroup.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (0 == i) {
            assert layoutInflater != null;
            view= layoutInflater.inflate(R.layout.send_message,viewGroup,false);
            return new SendMessage(view);
        }
        assert layoutInflater != null;
        view= layoutInflater.inflate(R.layout.receive_message,viewGroup,false);
        return new ReceiveMessage(view);
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position).getName() != null && list.get(position).getName().equals(UserListFr.name)) {
            return 0;
        }

        return 1;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (list.get(i).getName() != null && list.get(i).getName().equals(UserListFr.name)) {
            ( (SendMessage) viewHolder).name.setText(list.get(i).getName());
            ( (SendMessage) viewHolder).message.setText(list.get(i).getMessage());

        }else {
            ((ReceiveMessage) viewHolder).nameReceive.setText(list.get(i).getName());
            ((ReceiveMessage) viewHolder).massageReceive.setText(list.get(i).getMessage());
        }

    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }


    public class SendMessage extends RecyclerView.ViewHolder {
        TextView name;
        TextView message;

        SendMessage(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.send_name);
            message = itemView.findViewById(R.id.send_mess);

        }
    }

    public class ReceiveMessage extends RecyclerView.ViewHolder {
        TextView nameReceive;
        TextView massageReceive;

        ReceiveMessage(@NonNull View itemView) {

            super(itemView);
            nameReceive = itemView.findViewById(R.id.name_mess);
            massageReceive = itemView.findViewById(R.id.mass_txt);

        }
    }


}
