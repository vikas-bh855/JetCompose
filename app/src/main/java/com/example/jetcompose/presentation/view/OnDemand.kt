package com.example.jetcompose.presentation.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class OnDemand : AppCompatActivity() {

    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column(
                Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                var visible by remember { mutableStateOf(true) }
                AnimatedVisibility(visible = visible) {
                    FilledTonalButton(onClick = {
                        visible = !visible
                        startDownload()
                    }) {
                        Text(text = "Start On Demand Feature")
                    }
                }
            }
        }
    }

    var mySessionId = 0

    private fun startDownload() {
        val splitInstallManager = SplitInstallManagerFactory.create(this)
        val request =
            SplitInstallRequest
                .newBuilder()
                .addModule("dynamicfeature")
                .build()
        splitInstallManager
            .startInstall(request)
            .addOnSuccessListener { sessionId -> mySessionId = sessionId }
            .addOnFailureListener { exception -> }
        splitInstallManager.registerListener {
            if (it.sessionId() == mySessionId) {
                Log.d("TAG", "startDownload: moduleNames " + it.moduleNames()[0])
                when (it.status()) {
                    SplitInstallSessionStatus.DOWNLOADING -> {
                        val progress = (it.bytesDownloaded() / it.totalBytesToDownload()) * 100
                        Log.d("TAG", "startDownload: progress $progress")
                    }
                    SplitInstallSessionStatus.DOWNLOADED -> {
                        Log.d("TAG", "startDownload: status " + "Downloaded")
                    }
                    SplitInstallSessionStatus.INSTALLED -> {
                        Log.d("TAG", "startDownload: status " + "Installed")
                        startActivity(
                            Intent()
                                .setClassName(
                                    BuildConfig.APPLICATION_ID,
                                    "com.demo.dynamicfeature.OnDemand"
                                )
                        )
                    }

                }
            }
        }
    }


//    fun startDownload() {
//        CoroutineScope(Dispatchers.Main).launch {
//            val assetPackName = "asset_pack_on_demand"
//            val assetManager = AssetPackManagerFactory.getInstance(this@OnDemand)
//            Log.d(
//                "TAG",
//                "startDownloadPackLocation: " + assetManager.getPackLocation(assetPackName)
//            )
//            val assets = assetManager.requestPackStates(listOf(assetPackName))
//            if (assets != null) {
//                Log.d(
//                    "TAG",
//                    "startDownload: " + assets.packStates()[assetPackName]?.bytesDownloaded()
//                        .toString() +
//                            assets.packStates()[assetPackName]?.errorCode().toString() + "--" +
//                            assets.packStates()[assetPackName]?.name().toString() + "--" +
//                            assets.packStates()[assetPackName]?.status().toString() + "--" +
//                            assets.packStates()[assetPackName]?.transferProgressPercentage()
//                                .toString() + "--" +
//                            assets.packStates()[assetPackName]?.totalBytesToDownload().toString()
//                )
//            }
//
//        }
//    }
}

