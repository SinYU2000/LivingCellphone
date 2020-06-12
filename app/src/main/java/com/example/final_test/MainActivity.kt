package com.example.final_test

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.os.BatteryManager
import android.widget.Button
import com.example.final_test.MainActivity.Mp_charging.mp_charging2
import com.example.final_test.MainActivity.Mp_powerlevel.mp_powerHigh_2
import com.example.final_test.MainActivity.Mp_powerlevel.mp_powerLow_2
import com.example.final_test.MainActivity.Mp_powerlevel.mp_powerMid_2


class MainActivity : AppCompatActivity() {

    private var mp_shake2: MediaPlayer? = null
    private var mp_volumeUpKey2: MediaPlayer? = null
    private var mp_volumeDownKey2: MediaPlayer? = null

    var playbtn : Button? = null

    var receiver_charging:PowerConnectionReceiver?=null
    var intentFilter_charging: IntentFilter? = null

    var receiver_batterylevel:BatteryLevelReceiver?=null
    var intentFilter_batterylevel: IntentFilter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //////////sound///////////
        mp_shake2 = MediaPlayer.create(this,R.raw.screech2)
        mp_volumeUpKey2 = MediaPlayer.create(this,R.raw.up2)
        mp_volumeDownKey2 = MediaPlayer.create(this,R.raw.down2)
        mp_charging2 = MediaPlayer.create(this,R.raw.groove)
        mp_powerHigh_2 = MediaPlayer.create(this,R.raw.charging_high2)
        mp_powerMid_2 = MediaPlayer.create(this,R.raw.charging_mid2)
        mp_powerLow_2 = MediaPlayer.create(this,R.raw.charging_low2)

        playbtn = findViewById<Button>(R.id.play_sound)
        playbtn?.setOnClickListener{
            playOrPauseMusic()
        }

        //////////Shake///////////
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(shakeSensorListener, sensor, SensorManager.SENSOR_DELAY_NORMAL)

        /////////BatteryConnect//////
        receiver_charging =PowerConnectionReceiver()
        intentFilter_charging = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        registerReceiver(receiver_charging,intentFilter_charging)

        ///////////PowerLevel//////////
        receiver_batterylevel = BatteryLevelReceiver()
        intentFilter_batterylevel = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        registerReceiver(receiver_batterylevel,intentFilter_batterylevel)

    }

    fun playOrPauseMusic(): String {
        if (mp_shake2!!.isPlaying) {
            mp_shake2?.pause()
            return "Play"
        } else {
            mp_shake2?.start()
            return "Pause"
        }
    }
///////////////////////////////
    private val shakeSensorListener = object: SensorEventListener {
        override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}

        override fun onSensorChanged(event: SensorEvent?) {
            if (event != null) {
                val xValue = Math.abs(event.values[0]) // 加速度 - X 軸方向
                val yValue = Math.abs(event.values[1]) // 加速度 - Y 軸方向
                val zValue = Math.abs(event.values[2]) // 加速度 - Z 軸方向
                if (xValue > 20 || yValue > 20 || zValue > 20) {
                    playOrPauseMusic()
                }
            }
        }
    }
/////////////////////////////////
    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        return when (keyCode) {
            KeyEvent.KEYCODE_VOLUME_UP-> {
                mp_volumeUpKey2?.start()
                true
            }
            else-> false

        }

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return when (keyCode) {
            KeyEvent.KEYCODE_VOLUME_DOWN-> {
                mp_volumeDownKey2?.start()
                true
            }
            else-> false
        }
    }

    //////////////////////////////
    object Mp_powerlevel {
        var  mp_powerHigh_2 : MediaPlayer? = null
        var  mp_powerMid_2: MediaPlayer? = null
        var  mp_powerLow_2: MediaPlayer? = null
    }
    object Mp_charging {
        var mp_charging2 : MediaPlayer? = null

    }

}
