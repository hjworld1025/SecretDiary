package org.hans.secretdiary

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.EditText
import androidx.core.content.edit
import androidx.core.widget.addTextChangedListener

class DiaryActivity : AppCompatActivity() {

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary)

        val diaryEditText = findViewById<EditText>(R.id.diaryEditText)
        val detailPreferences = getSharedPreferences("diary", Context.MODE_PRIVATE)

        diaryEditText.setText(detailPreferences.getString("detail", ""))

        // Worker Thread 구현
        val runnable = Runnable {
            // edit의 저장방법
            // 1. commit()방식 (commit 인자가 true) : 동기처리
            // 2. apply()방식 (commit 인자가 false일 때) : 비동기처리
            getSharedPreferences("diary", Context.MODE_PRIVATE).edit {
                putString("detail", diaryEditText.text.toString())
            }
        }

        // text를 추가할때마다 실행되는 리스너
        // 설명 : text를 계속 추가하면 removeCallbacks가 계속 실행되면서
        // runnable 실행 전 pending된 post들을 계속 제거한다.
        // 만약 text의 추가가 멈추면 마지막 runnable이 0.5초 후 실행된다.
        diaryEditText.addTextChangedListener {
            handler.removeCallbacks(runnable)
            handler.postDelayed(runnable, 500)
        }
    }
}