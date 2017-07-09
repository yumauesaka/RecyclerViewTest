package com.example.yuesaka.myapplication;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yuesaka on 7/9/17.
 */

public class MyViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.item)
    TextView item;

    @BindView(R.id.editText)
    EditText editText;

    @BindView(R.id.button)
    Button button;

    NextQuestionListener listener;

    public MyViewHolder(View itemView, NextQuestionListener listener) {
        super(itemView);
        this.listener = listener;
        ButterKnife.bind(this, itemView);
    }

    public void onBind(int position) {
        item.setText("Question: " + position);
        editText.setText("");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.nextQuestion();
            }
        });
    }
}
