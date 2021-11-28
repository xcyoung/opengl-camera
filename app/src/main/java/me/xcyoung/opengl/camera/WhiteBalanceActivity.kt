package me.xcyoung.opengl.camera

import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import me.xcyoung.opengl.camera.widget.camera.GLCameraView

class WhiteBalanceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_white_balance)

        val cameraView = findViewById<GLCameraView>(R.id.cameraView)

        findViewById<SeekBar>(R.id.seekBar).setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val a = (6000f - 4000f) * progress / 100.0f + 4000f
                Log.d(WhiteBalanceActivity::class.java.simpleName, "temperature:$a")
                cameraView.setProgress(a)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })

        findViewById<SeekBar>(R.id.seekBar2).setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val a = (200f + 200f) * progress / 100.0f - 200f
                Log.d(WhiteBalanceActivity::class.java.simpleName, "tint:$a")
                cameraView.setTint(a)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
    }
}