package com.example.jetcompose.presentation.view

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.jetcompose.databinding.OndemandassetVideoBinding
import com.google.android.play.core.assetpacks.AssetPackLocation
import com.google.android.play.core.assetpacks.AssetPackManager
import com.google.android.play.core.assetpacks.AssetPackManagerFactory
import com.google.android.play.core.assetpacks.AssetPackStateUpdateListener
import com.google.android.play.core.assetpacks.model.AssetPackStatus
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream


class OnDemandAssetVideo : Activity() {
    private var assetPackManager: AssetPackManager? = null
    private val onDemandAssetPack = "asset_pack_on_demand"
    private val TAG = "OnDemandAssetVideo"
    private var waitForWifiConfirmationShown = false
    private var inputStream: InputStream? = null

    private val REQUEST_WRITE_PERMISSION = 1
    private lateinit var binding: OndemandassetVideoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = OndemandassetVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ), REQUEST_WRITE_PERMISSION
            )
        }
        binding.apply {
            btnDownloadVideo.setOnClickListener {
                initAssetPackManager()
                //initInstallTime()
            }
        }
    }

    private var assetManager: AssetManager? = null
    private fun initInstallTime() {
        try {
            val context: Context = createPackageContext("com.demo.jetcompose", 0)
            assetManager = context.assets
            getInputStreamFromAssetManager("BigBuckBunny.mp4");
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }

    private fun getInputStreamFromAssetManager(fileName: String) {
        var list: Array<String?>? = null
        try {
            inputStream = assetManager!!.open(fileName)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        if (inputStream != null) {
            writeDataToFile(inputStream)
        } else {
        }
    }

    private fun writeDataToFile(inputStream: InputStream?) {
        val tempFileName = "test"
        val filePath = getExternalFilesDir("").toString() + File.separator + tempFileName
        val file = File(filePath)
        try {
            if (!file.exists()) {
                file.createNewFile()
            }
            val outputStream = FileOutputStream(file, false)
            var read: Int
            val bytes = ByteArray(8192)
            if (inputStream != null) {
                while (inputStream.read(bytes).also { read = it } != -1) {
                    outputStream.write(bytes, 0, read)
                }
            }
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        playVideo(file.absolutePath)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun initOnDemand() {
        val assetsPath: String = getAbsoluteAssetPath(onDemandAssetPack)!!
        val file = File(assetsPath)
        file.list()?.forEachIndexed { index, it ->
            Log.d(TAG, "initOnDemand: $it")
            if (file.exists() && it.equals("BigBuckBunny.mp4")) {
                val videoFile = File(assetsPath + File.separator + it)
                Thread.sleep(500)
                playVideo(videoFile.absolutePath)
            }
        }
    }

    private fun playVideo(path: String) {
        val video = Uri.parse(path)
        binding.btnCircular.progress = 0
        binding.videoView.visibility = View.VISIBLE
        binding.videoView.setVideoURI(video)
        binding.videoView.setOnPreparedListener { mp ->
            mp.isLooping = true
            binding.videoView.start()
        }
    }

    private fun initAssetPackManager() {
        if (assetPackManager == null) {
            assetPackManager = AssetPackManagerFactory.getInstance(applicationContext);
        }
        registerListener()
    }

    private fun getAbsoluteAssetPath(assetPack: String): String? {
        val assetPackPath: AssetPackLocation =
            assetPackManager?.getPackLocation(assetPack) ?: return null
        return assetPackPath.assetsPath()
    }

    private fun registerListener() {
        val assetPackStateUpdateListener =
            AssetPackStateUpdateListener { state ->
                Log.d(TAG, "registerListener: " + state.status().toString())
                when (state.status()) {
                    AssetPackStatus.PENDING -> Log.i(TAG, "Pending")
                    AssetPackStatus.DOWNLOADING -> {
                        val downloaded = state.bytesDownloaded()
                        val totalSize = state.totalBytesToDownload()
                        val percent = 100.0 * downloaded / totalSize
                        Log.i(TAG, "PercentDone=" + String.format("%.2f", percent))
                        binding.btnCircular.progress = percent.toInt()
                        val downloadedT = downloaded / (1024 * 1024)
                        val totalT = totalSize / (1024 * 1024)
                        binding.tvDownloadSpace.text = "$downloadedT / $totalT"
                        Log.d(TAG, "downloaded: $downloadedT")
                        Log.d(TAG, "totalSize: $totalT")
                    }
                    AssetPackStatus.TRANSFERRING -> {
                    }
                    AssetPackStatus.COMPLETED -> {
                        Toast.makeText(this, "Downloaded", Toast.LENGTH_LONG).show()
                        initOnDemand()
                    }                    // Asset pack is ready to use. Start the Game/App.

                    AssetPackStatus.FAILED ->                     // Request failed. Notify user.
                        Log.e(TAG, state.errorCode().toString())
                    AssetPackStatus.CANCELED -> {
                    }
                    AssetPackStatus.WAITING_FOR_WIFI -> showWifiConfirmationDialog()
                    AssetPackStatus.NOT_INSTALLED -> {
                    }
                    AssetPackStatus.UNKNOWN -> {
                    }
                }
            }
        val onDemandAssetPackPath = getAbsoluteAssetPath(onDemandAssetPack)
        if (onDemandAssetPackPath == null) {
            assetPackManager!!.registerListener(assetPackStateUpdateListener)
            val assetPackList: MutableList<String> = ArrayList()
            assetPackList.add(onDemandAssetPack)
            assetPackManager!!.fetch(assetPackList)
        } else {
            initOnDemand()
        }
    }

    private fun showWifiConfirmationDialog() {
        if (!waitForWifiConfirmationShown) {
            assetPackManager!!.showCellularDataConfirmation(this)
                .addOnSuccessListener { resultCode ->
                    if (resultCode == RESULT_OK) {
                        Log.d(TAG, "Confirmation dialog has been accepted.")
                        registerListener()
                    } else if (resultCode == RESULT_CANCELED) {
                        Log.d(TAG, "Confirmation dialog has been denied by the user.")
                    }
                }
            waitForWifiConfirmationShown = true
        }
    }

}
