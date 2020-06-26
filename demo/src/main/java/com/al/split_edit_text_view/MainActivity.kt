package com.al.split_edit_text_view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.al.open.SplitEditTextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnChangeStyle.setOnClickListener {
            when (splitEdit1.inputBoxStyle) {
                SplitEditTextView.INPUT_BOX_STYLE_SINGLE -> {
                    splitEdit1.inputBoxStyle = SplitEditTextView.INPUT_BOX_STYLE_CONNECT
                }
                SplitEditTextView.INPUT_BOX_STYLE_CONNECT -> {
                    splitEdit1.inputBoxStyle = SplitEditTextView.INPUT_BOX_STYLE_UNDERLINE
                }
                SplitEditTextView.INPUT_BOX_STYLE_UNDERLINE -> {
                    splitEdit1.inputBoxStyle = SplitEditTextView.INPUT_BOX_STYLE_SINGLE
                }
            }

            when (splitEdit2.inputBoxStyle) {
                SplitEditTextView.INPUT_BOX_STYLE_SINGLE -> {
                    splitEdit2.inputBoxStyle = SplitEditTextView.INPUT_BOX_STYLE_CONNECT
                }
                SplitEditTextView.INPUT_BOX_STYLE_CONNECT -> {
                    splitEdit2.inputBoxStyle = SplitEditTextView.INPUT_BOX_STYLE_UNDERLINE
                }
                SplitEditTextView.INPUT_BOX_STYLE_UNDERLINE -> {
                    splitEdit2.inputBoxStyle = SplitEditTextView.INPUT_BOX_STYLE_SINGLE
                }
            }

            when (splitEdit3.inputBoxStyle) {
                SplitEditTextView.INPUT_BOX_STYLE_SINGLE -> {
                    splitEdit3.inputBoxStyle = SplitEditTextView.INPUT_BOX_STYLE_CONNECT
                }
                SplitEditTextView.INPUT_BOX_STYLE_CONNECT -> {
                    splitEdit3.inputBoxStyle = SplitEditTextView.INPUT_BOX_STYLE_UNDERLINE
                }
                SplitEditTextView.INPUT_BOX_STYLE_UNDERLINE -> {
                    splitEdit3.inputBoxStyle = SplitEditTextView.INPUT_BOX_STYLE_SINGLE
                }
            }

        }

        btnChangeMode.setOnClickListener {
            if (splitEdit1.contentShowMode == SplitEditTextView.CONTENT_SHOW_MODE_PASSWORD) {
                splitEdit1.contentShowMode = SplitEditTextView.CONTENT_SHOW_MODE_TEXT
            } else {
                splitEdit1.contentShowMode = SplitEditTextView.CONTENT_SHOW_MODE_PASSWORD
            }
            if (splitEdit2.contentShowMode == SplitEditTextView.CONTENT_SHOW_MODE_PASSWORD) {
                splitEdit2.contentShowMode = SplitEditTextView.CONTENT_SHOW_MODE_TEXT
            } else {
                splitEdit2.contentShowMode = SplitEditTextView.CONTENT_SHOW_MODE_PASSWORD
            }
            if (splitEdit3.contentShowMode == SplitEditTextView.CONTENT_SHOW_MODE_PASSWORD) {
                splitEdit3.contentShowMode = SplitEditTextView.CONTENT_SHOW_MODE_TEXT
            } else {
                splitEdit3.contentShowMode = SplitEditTextView.CONTENT_SHOW_MODE_PASSWORD
            }
        }
    }
}