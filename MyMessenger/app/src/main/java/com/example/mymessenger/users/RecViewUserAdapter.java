package com.example.mymessenger.users;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.mymassanger.R;
import com.example.mymessenger.message.MessengerActivity;

import java.util.ArrayList;

public class RecViewUserAdapter extends RecyclerView.Adapter<RecViewUserAdapter.ViewHolder> {

    private ArrayList<User> list;


    RecViewUserAdapter(ArrayList<User> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rec_view_user, viewGroup, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {

        viewHolder.name.setText(list.get(i).getName());
        viewHolder.surNAme.setText(" "+list.get(i).getSurName());

    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView name;
        TextView surNAme;
        LinearLayout ll;

        ViewHolder(@NonNull final View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img_user);
            name = itemView.findViewById(R.id.name_user);
            surNAme = itemView.findViewById(R.id.surname_user);
            ll=itemView.findViewById(R.id.llmess);
            ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), MessengerActivity.class);
                    intent.putExtra("key",list.get(getAdapterPosition()).getKey());
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }

}
