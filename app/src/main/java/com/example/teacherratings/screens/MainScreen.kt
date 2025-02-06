package com.example.teacherratings.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.teacherratings.database.Database
import com.example.teacherratings.database.Database.Companion.getRatings
import com.example.teacherratings.database.Rating
import com.example.teacherratings.database.Teacher
import com.example.teacherratings.ui.theme.TeacherRatingsTheme
import kotlin.math.ceil

class MainScreen : ComponentActivity() {
    @SuppressLint("DefaultLocale")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TeacherRatingsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var teachers by remember {
                        mutableStateOf<List<Teacher>>(emptyList())
                    }



                    Database.getTeachers { list ->
                        teachers = list
                    }


                    var searchtext by remember {
                        mutableStateOf("")
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(18.dp)
                        ) {


                            OutlinedTextField(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                value = searchtext,
                                onValueChange = {
                                    searchtext = it

                                },
                                textStyle = TextStyle(fontSize = 18.sp),
                                label = { Text(text = "Search") },
                                placeholder = { Text(text = "Search") },
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Filled.Search,
                                        contentDescription = "Search Icon"
                                    )
                                }
                            )
                            val i = intent
                            LazyColumn() {
                                items(teachers.filter {
                                    it.name!!.contains(searchtext).or(it.subject!!.contains(searchtext))
                                }) {


                                    var sum by remember {
                                        mutableDoubleStateOf(0.0)
                                    }
                                    var avg by remember {
                                        mutableDoubleStateOf(0.0)
                                    }
                                    var count by remember {
                                        mutableIntStateOf(0)
                                    }
                                    var ratings by remember {
                                        mutableStateOf<List<Rating>>(emptyList())
                                    }
                                    getRatings {
                                        ratings = it.toMutableList()
                                    }
                                    ratings.forEach { i ->
                                        if (i.to == it.teacherId) {
                                            Log.d("TG2", it.toString())
                                            sum += i.rate!!
                                            count++
                                        }
                                    }

                                    avg = sum / count
                                    val formatted = String.format("%.1f", ceil(avg * 100) / 100)


                                    i.extras?.getString("StudentId")
                                        ?.let { it1 -> TeacherItem(it, it1, formatted) }
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    @Composable
    fun TeacherItem(teacher: Teacher, id: String, rate: String) {
        Row(
            modifier = Modifier
                .background(Color.White)
                .padding(vertical = 10.dp)
                .fillMaxWidth()
                .clickable {
                    val intent = Intent(this@MainScreen, Rate::class.java)
                    intent.putExtra("teacherId", teacher.teacherId)
                    intent.putExtra("studentId", id)
                    startActivity(intent)
                }, verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Prof. name: ${teacher.name}")
                Text("Subject: ${teacher.subject}")
            }
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {
                Row(verticalAlignment = Alignment.CenterVertically) {

                    if (rate == "NaN") {
                        Text("0.0")
                    } else {
                        Text(rate)
                    }
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Star Icon",
                        tint = Color(237, 138, 25)
                    )
                }
            }

        }
    }
}