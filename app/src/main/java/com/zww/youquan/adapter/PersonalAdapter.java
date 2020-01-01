package com.zww.youquan.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zww.youquan.R;

public class PersonalAdapter extends RecyclerView.Adapter<PersonalAdapter.PersonalHolder> {


    @NonNull
    @Override
    public PersonalHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_personal, parent, false);
        return new PersonalHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonalHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 13;
    }

    static class PersonalHolder extends RecyclerView.ViewHolder {

        ImageView image, functionImage;
        TextView text, functionText;

        PersonalHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            functionImage = itemView.findViewById(R.id.functionImage);
            text = itemView.findViewById(R.id.text);
            functionText = itemView.findViewById(R.id.functionText);
        }
    }
}
