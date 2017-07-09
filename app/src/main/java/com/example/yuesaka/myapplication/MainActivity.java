package com.example.yuesaka.myapplication;

import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements RecyclerView.OnScrollChangeListener, NextQuestionListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    LinearLayoutManager layoutManager;
    MyRecyclerViewAdapter adapter;

    public static final int SCROLL_STATE_IDLE = 0;

    public static final int SCROLL_STATE_DRAGGING = 1;

    public static final int SCROLL_STATE_SETTLING = 2;

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MyRecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);

        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == SCROLL_STATE_IDLE) {
                    Log.d("yuma", "SCROLL_STATE_IDLE");
                    Log.d("yuma", "findFirstVisibleItemPosition():" + ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition());
                    Log.d("yuma", "findFirstCompletelyVisibleItemPosition():" + ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition());
                    Log.d("yuma", "findLastVisibleItemPosition():" + ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastVisibleItemPosition());
                    Log.d("yuma", "findLastCompletelyVisibleItemPosition():" + ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition());
                    showKeyboard(recyclerView);
                } else if (newState == SCROLL_STATE_DRAGGING) {
                    Log.d("yuma", "SCROLL_STATE_DRAGGING");
                } else if (newState == SCROLL_STATE_SETTLING) {
                    Log.d("yuma", "SCROLL_STATE_SETTLING");
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        recyclerView.setOnFlingListener(new RecyclerView.OnFlingListener() {
            @Override
            public boolean onFling(int velocityX, int velocityY) {
                    // This performs a singular item scroll on fling.
                    final int visible = layoutManager.findFirstVisibleItemPosition();
                    if (visible < (layoutManager.getItemCount() - 1)) {
                        if (velocityY > 0) {
                            recyclerView.smoothScrollToPosition(visible + 1);
                        } else {

                            recyclerView.smoothScrollToPosition(visible);
                        }
                    }
                return false;
            }
        });
    }

    @Override
    public void nextQuestion() {
        hideKeyboard();


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int nextPosition = layoutManager.findLastVisibleItemPosition() + 1;
                recyclerView.smoothScrollToPosition(nextPosition);
            }
        }, 100);
    }

    private void showKeyboard(RecyclerView recyclerView) {
        int completelyVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
        View view = recyclerView.getLayoutManager().findViewByPosition(completelyVisibleItemPosition);
        if (view instanceof ViewGroup && ((ViewGroup)view).getChildCount() > 1) {
            EditText editText = (EditText) ((ViewGroup) recyclerView.getLayoutManager().findViewByPosition(completelyVisibleItemPosition)).getChildAt(1);
            if (editText.getText().length() == 0) {
                editText.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
            }
        }
    }

    private void hideKeyboard() {
        int completelyVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
        View view = recyclerView.getLayoutManager().findViewByPosition(completelyVisibleItemPosition);
        if (view instanceof ViewGroup && ((ViewGroup)view).getChildCount() > 1) {
            EditText editText = (EditText) ((ViewGroup) recyclerView.getLayoutManager().findViewByPosition(completelyVisibleItemPosition)).getChildAt(1);
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    }

    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

    }
}
