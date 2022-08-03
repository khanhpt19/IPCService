package com.example.ipcservice.screen

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import com.example.ipcservice.IMusicService
import com.example.ipcservice.R
import com.example.ipcservice.service.MusicService
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var mService: IMusicService? = null
    private var mIsServiceConnected = false
    private var mTextCurrentDuration: TextView? = null
    private var mTextTotalDuration: TextView? = null
    private var mTextSongName: TextView? = null
    private var mHandler: Handler? = null
    private val mServiceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName, iBinder: IBinder) {
            mService = IMusicService.Stub.asInterface(iBinder)
            mIsServiceConnected = true
        }

        override fun onServiceDisconnected(componentName: ComponentName) {
            mIsServiceConnected = false
        }
    }

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bindService()
        initViews()
        updateCurrentProgress()
    }

    private fun initViews() {
        findViewById<ImageView>(R.id.image_play).setOnClickListener(this)
        findViewById<ImageView>(R.id.image_pause).setOnClickListener(this)
        mTextCurrentDuration = findViewById(R.id.text_current_duration)
        mTextTotalDuration = findViewById(R.id.text_total_duration)
        mTextSongName = findViewById(R.id.text_name)
    }

    private fun updateCurrentProgress() {
        if (mHandler == null) {
            mHandler = Handler(Looper.getMainLooper())
        }
        mHandler!!.postDelayed({
            try {
                mTextCurrentDuration?.text = convertLongToTime(mService?.currentDuration?.toLong())

                mTextTotalDuration?.text = convertLongToTime(mService?.totalDuration?.toLong())
                mTextSongName?.text = mService?.songName
            } catch (e: RemoteException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            updateCurrentProgress()
        }, DELAY_INTERVAL)
    }

    private fun convertLongToTime(time: Long?): String {
        val date = time?.let { Date(it) }
        val format = SimpleDateFormat("mm:ss", Locale.US)
        return format.format(date)
    }

    private fun bindService() {
        val intent = Intent(this, MusicService::class.java)
        bindService(intent, mServiceConnection, BIND_AUTO_CREATE)
        startService(intent)
    }

    override fun onClick(view: View) {
        if (!mIsServiceConnected) {
            return
        }
        when (view.id) {
            R.id.image_play -> try {
                mService?.play()
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
            R.id.image_pause -> try {
                mService?.pause()
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
        }
    }

    override fun onDestroy() {
        unbindService(mServiceConnection)
        super.onDestroy()
    }

    companion object {
        private const val DELAY_INTERVAL: Long = 1000
    }
}
