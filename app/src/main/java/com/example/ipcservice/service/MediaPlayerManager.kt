package com.example.ipcservice.service

import android.content.Context
import android.media.MediaPlayer
import android.os.IBinder
import com.example.ipcservice.IMusicService
import com.example.ipcservice.R

class MediaPlayerManager(private val mContext: Context) : IMusicService {
    private var mMediaPlayer: MediaPlayer? = null
    private var mName: String = ""
    override fun getSongName(): String {
        return mName
    }

    override fun getCurrentDuration(): Int {
        try {
            if (mMediaPlayer != null) {
                return mMediaPlayer!!.currentPosition
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return 0
    }

    override fun getTotalDuration(): Int {
        return if (mMediaPlayer != null) {
            mMediaPlayer!!.duration
        } else 0
    }

    override fun changeMediaStatus() {
        if (mMediaPlayer == null) {
            return
        }
        if (mMediaPlayer!!.isPlaying) {
            pause()
        } else {
            play()
        }
    }

    override fun playSong() {
        if (mMediaPlayer != null && mMediaPlayer!!.isPlaying) {
            return
        }
        playNewSong()
    }

    private fun playNewSong() {
        mMediaPlayer = MediaPlayer.create(mContext, R.raw.dung_quen_ten_anh)
        mName = "Đừng quên tên anh"
        mMediaPlayer?.start()
    }

    override fun play() {
        if (mMediaPlayer == null) {
            playNewSong()
            return
        }
        if (!mMediaPlayer!!.isPlaying) {
            mMediaPlayer!!.start()
        }
    }

    override fun pause() {
        if (mMediaPlayer != null && mMediaPlayer!!.isPlaying) {
            mMediaPlayer!!.pause()
        }
    }

    override fun asBinder(): IBinder? {
        return null
    }
}
