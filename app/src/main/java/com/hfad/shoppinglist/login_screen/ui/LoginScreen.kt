package com.hfad.shoppinglist.login_screen.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hfad.shoppinglist.R
import com.hfad.shoppinglist.login_screen.data.UserData


@Composable
fun LoginScreen(
    model: LoginViewModel = hiltViewModel(),
    onNavigate: (UserData) -> Unit
) {
    val context = LocalContext.current
    var login by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    var isLoading by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        model.loginEvents.collect { event ->
            when (event) {
                is LoginEvent.Error -> {
                    isLoading = false
                    Toast.makeText(
                        context, event.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                LoginEvent.Loading -> {
                    isLoading = true
                }

                is LoginEvent.LoginSuccess -> {
                    isLoading = false
                    onNavigate(
                        UserData(
                            event.email,
                            event.uid
                        )
                    )
                }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 30.dp, end = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.shopping_list_app),
                color = Color.Black,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedTextField(
                value = login,
                onValueChange = { text ->
                    login = text
                },
                singleLine = true,
                label = {
                    Text(stringResource(id = R.string.login))
                },
                textStyle = MaterialTheme.typography.bodyMedium,
                visualTransformation = VisualTransformation.None,
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    unfocusedIndicatorColor = Color.LightGray
                ),
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .height(56.dp)
            )
            OutlinedTextField(
                value = password,
                onValueChange = { text ->
                    password = text
                },
                singleLine = true,
                label = {
                    Text(stringResource(id = R.string.password))
                },
                textStyle = MaterialTheme.typography.bodyMedium,
                visualTransformation = VisualTransformation.None,
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    unfocusedIndicatorColor = Color.LightGray
                ),
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .height(56.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    if (!isLoading) {
                        model.signIn(login, password)
                    }
                }) {
                Text(text = stringResource(id = R.string.sign_in))
            }
            Spacer(modifier = Modifier.height(5.dp))
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    if (!isLoading) {
                        model.signUp(login, password)
                    }
                }) {
                Text(text = stringResource(id = R.string.sign_up))
            }
            Spacer(modifier = Modifier.height(15.dp))
            if (isLoading) CircularProgressIndicator()
        }
    }
}
