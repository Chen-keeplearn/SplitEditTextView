package com.al.split_edit_text_view;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.al.open.OnInputListener;
import com.al.open.SplitEditTextView;

class TestActivity extends AppCompatActivity {
    SplitEditTextView splitEditTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        splitEditTextView = findViewById(R.id.splitTextViewTest);

        splitEditTextView.setOnInputListener(new OnInputListener() {
            @Override
            public void onInputFinished(String content) {

            }

            @Override
            public void onInputChanged(String text) {
                super.onInputChanged(text);
            }
        });
    }
}
