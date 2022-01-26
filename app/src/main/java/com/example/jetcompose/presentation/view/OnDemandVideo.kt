package com.example.jetcompose.presentation.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.jetcompose.BuildConfig
import com.example.jetcompose.R
import com.example.jetcompose.databinding.MainActivityBinding
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream


@AndroidEntryPoint
class OnDemandVideo : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
       // startDownload()
    }

//    var mySessionId = 0
//    private fun startDownload() {
//        val splitInstallManager = SplitInstallManagerFactory.create(this)
//        val request =
//            SplitInstallRequest
//                .newBuilder()
//                .addModule("dynamicfeature")
//                .build()
//        splitInstallManager
//            .startInstall(request)
//            .addOnSuccessListener { sessionId -> mySessionId = sessionId }
//            .addOnFailureListener { exception -> }
//        splitInstallManager.registerListener {
//            if (it.sessionId() == mySessionId) {
//                Log.d("TAG", "startDownload: moduleNames " + it.moduleNames()[0])
//                when (it.status()) {
//                    SplitInstallSessionStatus.DOWNLOADING -> {
//                        val progress = (it.bytesDownloaded() / it.totalBytesToDownload()) * 100
//                        Log.d("TAG", "startDownload: progress $progress")
//                        Toast.makeText(this, "Downloading", Toast.LENGTH_SHORT).show()
//                    }
//                    SplitInstallSessionStatus.DOWNLOADED -> {
//                        Log.d("TAG", "startDownload: status " + "Downloaded")
//                        Toast.makeText(this, "Downloaded", Toast.LENGTH_SHORT).show()
//                    }
//                    SplitInstallSessionStatus.INSTALLED -> {
//                        Log.d("TAG", "startDownload: status " + "Installed")
//                        Toast.makeText(this, "Installed", Toast.LENGTH_SHORT).show()
//                        initAssetDelivery()
//                    }
//
//                }
//            }
//        }
//    }
//
//    private fun initAssetDelivery() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            requestPermissions(
//                arrayOf(
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                    Manifest.permission.READ_EXTERNAL_STORAGE
//                ), 1
//            )
//        }
//
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            val context: Context = createPackageContext(packageName, 0)
//            val assetManager: AssetManager = context.assets
//            val filePath = getExternalFilesDir("").toString() + File.separator + "test"
//            val file = File(filePath)
//            val stream: InputStream = assetManager.open("sample_video.mp4")
//            Log.d("TAG", "onRequestPermissionsResult: " + assetManager.locales)
//            try {
//                if (!file.exists()) {
//                    file.createNewFile()
//                }
//                val outputStream = FileOutputStream(file, false)
//                var read: Int
//                val bytes = ByteArray(8192)
//                if (stream != null) {
//                    while (stream.read(bytes).also { read = it } != -1) {
//                        outputStream.write(bytes, 0, read)
//                    }
//                }
//                outputStream.close()
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//            val video: Uri =
//                Uri.parse(file.absolutePath)
//            binding.videoView.setVideoURI(video)
//            binding.videoView.setOnPreparedListener(MediaPlayer.OnPreparedListener { mp ->
//                mp.isLooping = true
//                binding.videoView.start()
//            })
//        }
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//    }

}

