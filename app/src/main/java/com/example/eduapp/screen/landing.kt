package com.example.eduapp.screen

import android.content.Context
import android.content.res.Configuration
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.example.eduapp.R
import com.example.eduapp.viewmodel.AppViewModel
import com.example.eduapp.ui.theme.AppFont
import helper.findActivity
import org.koin.androidx.compose.koinViewModel

@Composable
fun LandingScreen(
    navController: NavHostController, 
    modifier: Modifier = Modifier,
    isMuted: Boolean = true,
    onToggleMusic: () -> Unit = {}
) {
    val context = LocalContext.current
    val activity = context.findActivity()
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    
    val viewModel: AppViewModel = koinViewModel(
        viewModelStoreOwner = activity ?: (context as ComponentActivity)
    )

    var username by remember { mutableStateOf(viewModel.currentUsername.value) }
    val isNameValid = username.trim().length >= 3
    val logoBackgroundColor = Color(0xFFF9F9F1) 
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(logoBackgroundColor)
    ) {
        // Music Toggle on Top Right
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 16.dp, end = 16.dp)
                .size(48.dp)
                .clickable { onToggleMusic() }
                .zIndex(1f),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.speaker_icon),
                contentDescription = "Toggle Music",
                modifier = Modifier.size(36.dp),
                alpha = if (isMuted) 0.5f else 1.0f 
            )
            if (isMuted) {
                Box(modifier = Modifier.width(36.dp).height(2.dp).background(Color.Red))
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (isLandscape) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    // Logo on Left
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Mini Edu Games",
                        modifier = Modifier
                            .weight(1f)
                            .heightIn(max = 200.dp),
                        contentScale = ContentScale.Fit
                    )
                    
                    Spacer(modifier = Modifier.width(24.dp))
                    
                    // Input and Button on Right
                    Column(
                        modifier = Modifier.weight(1.2f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        UsernameInputField(username, isNameValid) { 
                            username = it
                            viewModel.currentUsername.value = it
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        PlayButton(isNameValid) { navController.navigate("setting") }
                    }
                }
            } else {
                // Portrait Layout
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Mini Edu Games",
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .aspectRatio(1f),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.height(20.dp))
                UsernameInputField(username, isNameValid) { 
                    username = it
                    viewModel.currentUsername.value = it
                }
                Spacer(modifier = Modifier.height(30.dp))
                PlayButton(isNameValid) { navController.navigate("setting") }
            }
        }
    }
}

@Composable
fun UsernameInputField(username: String, isNameValid: Boolean, onValueChange: (String) -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        shape = RoundedCornerShape(24.dp),
        shadowElevation = 4.dp,
        color = Color.White
    ) {
        OutlinedTextField(
            value = username,
            onValueChange = onValueChange,
            label = { Text("Enter Your Name", fontFamily = AppFont, color = if (isNameValid) Color(0xFF4CAF50) else Color.Gray) },
            placeholder = { Text("e.g. Hero Gamer", fontFamily = AppFont, color = Color.LightGray) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            singleLine = true,
            leadingIcon = { Icon(Icons.Default.Person, null, tint = Color(0xFF4CAF50)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF4CAF50),
                unfocusedBorderColor = Color.Transparent
            ),
            textStyle = LocalTextStyle.current.copy(fontFamily = AppFont)
        )
    }
}

@Composable
fun PlayButton(enabled: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier.fillMaxWidth().height(56.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
        shape = RoundedCornerShape(28.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
    ) {
        Text("PLAY NOW", fontFamily = AppFont, fontSize = 18.sp)
    }
}
