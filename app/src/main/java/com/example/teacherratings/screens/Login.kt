package com.example.teacherratings.screens

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.teacherratings.ui.theme.TeacherRatingsTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.teacherratings.database.Database
import com.example.teacherratings.database.Student
import com.example.teacherratings.database.Teacher

class Login : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TeacherRatingsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var userId by remember {
                        mutableStateOf("")
                    }
                    var password by remember {
                        mutableStateOf("")
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = Color.White)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 18.dp)
                                .padding(bottom = 100.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            OutlinedTextField(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                value = userId,
                                onValueChange = { userId = it },
                                textStyle = TextStyle(fontSize = 18.sp),
                                label = { Text(text = "User ID") },
                                placeholder = { Text(text = "User ID") }
                            )

                            OutlinedTextField(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                value = password,
                                onValueChange = { password = it },
                                textStyle = TextStyle(fontSize = 18.sp),
                                label = { Text(text = "Password") },
                                placeholder = { Text(text = "Password") }
                            )
                            Button(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 20.dp),
                                onClick = {
                                    Database.checkStudent(Student(userId, password)) {
                                        if (it == "Logged in Successfully") {
                                            Toast.makeText(this@Login, it, Toast.LENGTH_SHORT)
                                                .show()
                                            val intent = Intent(this@Login, MainScreen::class.java)
                                            intent.putExtra("StudentId",userId)
                                            startActivity(intent)
                                        } else {
                                            Toast.makeText(this@Login, "error", Toast.LENGTH_SHORT)
                                                .show()
                                        }
                                    }
                                }) {
                                Text("Next")
                            }
                        }
                    }
                }
            }
        }
    }
}