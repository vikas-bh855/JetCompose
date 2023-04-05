package com.example.jetcompose.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.example.jetcompose.MainActivity
import com.example.jetcompose.R
import com.example.jetcompose.theme.colorButtonGreen
import com.example.jetcompose.theme.colorLightGrey
import com.example.jetcompose.theme.colorTextFieldBG
import com.example.jetcompose.theme.colorWhite
import com.example.jetcompose.utils.Constants
import com.example.jetcompose.utils.fontFamilyPR
import dagger.hilt.android.AndroidEntryPoint

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "login")

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {

    private val loginViewModel by viewModels<LoginViewModel>()
    private val preferenceSession = stringPreferencesKey(Constants.SESSION_ID)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ComposeView(this).apply {
            setContent {
                val isLoggedIn = remember { mutableStateOf(false) }
                lifecycleScope.launchWhenCreated {
                    dataStore.data.collect {
                        val sessionId = it[preferenceSession]
                        if (sessionId.isNullOrBlank())
                            isLoggedIn.value = true
                        else startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    }
                }
                if (isLoggedIn.value)
                    Login()
            }
        })
        lifecycleScope.launchWhenStarted {
            loginViewModel.loginState.collect { sessionId ->
                if (sessionId.isNotBlank()) {
                    this@LoginActivity.dataStore.edit {
                        it[preferenceSession] = sessionId
                    }
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun Login() {
        val userId = remember { mutableStateOf("") }
        val password = remember { mutableStateOf("") }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 30.dp, end = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(painter = painterResource(id = R.drawable.app_icon), contentDescription = "App Icon")
            Spacer(modifier = Modifier.padding(20.dp))
            Text(
                text = "Login",
                fontFamily = fontFamilyPR,
                color = colorWhite,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.padding(top = 40.dp))
            EditText("User Id", userId.value) { userId.value = it }
            Spacer(modifier = Modifier.padding(top = 20.dp))
            EditText("Password", password.value) { password.value = it }
            Spacer(modifier = Modifier.padding(top = 40.dp))
            Button(
                onClick = { loginViewModel.login(userId.value, password.value) },
                colors = ButtonDefaults.buttonColors(colorButtonGreen),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Login",
                    color = colorTextFieldBG,
                    fontSize = 18.sp,
                    fontFamily = fontFamilyPR,
                    fontWeight = FontWeight.ExtraBold
                )

            }
        }
    }

    @Composable
    fun EditText(label: String, value: String, state: (String) -> Unit) {
        BasicTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = state,
            decorationBox = { innerTextField ->
                Box(
                    Modifier
                        .border(1.dp, colorButtonGreen, RoundedCornerShape(10.dp))
                        .background(colorTextFieldBG, RoundedCornerShape(10.dp))
                        .padding(15.dp)
                ) {
                    if (value.isEmpty()) {
                        Text(label, color = colorLightGrey)
                    }
                    innerTextField()  //<-- Add this
                }
            },
        )
    }
}