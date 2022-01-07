package com.example.chessbuzzer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.text.format.Time
import androidx.appcompat.app.ActionBar
import com.example.chessbuzzer.TimerService
import com.example.chessbuzzer.databinding.ActivityMainBinding
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {
    lateinit var mMediaPlayer: MediaPlayer
    var player:Int = 0
    var blackFlag = false
    var whiteFlag = true
    var blackMovedCount = 0
    var whiteMovedCount = 0


    // 0 = white player
    // 1 = black player

    private lateinit var binding: ActivityMainBinding
    private var timerStarted = false
    private lateinit var serviceIntent: Intent
    private var time = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mMediaPlayer = MediaPlayer.create(this, R.raw.clicksound)

        val actionBar: ActionBar? = supportActionBar
        if (actionBar != null) actionBar.hide()
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)




        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startStopButton.setOnClickListener { startStopTimer() }
        binding.resetButton.setOnClickListener { resetTimer() }

        serviceIntent = Intent(applicationContext, TimerService::class.java)
        registerReceiver(updateTime, IntentFilter(TimerService.TIMER_UPDATED))


    }

    private fun resetTimer()
    {
        stopTimer()
        time = 0.0
        if(player==0)
            binding.whiteTimerTV.text = getTimeStringFromDouble(time)
        else if(player == 1)
            binding.blackTimerTV.text = getTimeStringFromDouble(time)

    }

    private fun startStopTimer()
    {
        if(timerStarted)
            stopTimer()
        else
            startTimer()
    }


    private fun startTimer()
    {
        serviceIntent.putExtra(TimerService.TIME_EXTRA, time)
        startService(serviceIntent)
        binding.startStopButton.text = "STOP"
        timerStarted = true
    }

    private fun stopTimer()
    {
        stopService(serviceIntent)
        timerStarted = false
    }

    private val updateTime: BroadcastReceiver = object : BroadcastReceiver()
    {
        override fun onReceive(context: Context, intent: Intent)
        {
            time = intent.getDoubleExtra(TimerService.TIME_EXTRA, 0.0)
                if(player == 0)
                    binding.whiteTimerTV.text = getTimeStringFromDouble(time)
                else if(player == 1)
                    binding.blackTimerTV.text = getTimeStringFromDouble(time)




        }
    }

    private fun getTimeStringFromDouble(time: Double): String
    {
        val resultInt = time.roundToInt()
        val minutes = resultInt % 86400 % 3600 / 60
        val seconds = resultInt % 86400 % 3600 % 60

        return makeTimeString( minutes, seconds)
    }







fun blackButton(view:View){
        player = 0
        if(blackFlag) {

            stopTimer()
            resetTimer()
            startStopTimer()
            mMediaPlayer.start()
            blackFlag = false
            whiteFlag = true


        }
    }


    fun whiteButton(view : View) {
        player = 1
        if (whiteFlag) {
            stopTimer()
            resetTimer()
            startStopTimer()
            mMediaPlayer.start()
            whiteFlag = false
            blackFlag = true


        }
    }



    private fun makeTimeString( min: Int, sec: Int): String = String.format("%02d:%02d", min, sec)

}