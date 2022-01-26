package com.example.jetcompose.presentation.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.media.MediaPlayer.OnPreparedListener
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.jetcompose.databinding.MainActivityBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream


@AndroidEntryPoint
class InstallInTime : AppCompatActivity() {
    private lateinit var binding: MainActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
       // initAssetDelivery()
    }

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
//
//            val context: Context = createPackageContext(packageName, 0)
//            val assetManager: AssetManager = context.assets
//            val filePath = getExternalFilesDir("").toString() + File.separator + "test"
//            val file = File(filePath)
//            val stream: InputStream = assetManager.open("sample.mp4")
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
//            binding.videoView.setOnPreparedListener(OnPreparedListener { mp ->
//                mp.isLooping = true
//                binding.videoView.start()
//            })
//        }
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//    }

}

