package com.example.teacherratings.screens

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowCircleUp
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.teacherratings.database.Comments
import com.example.teacherratings.database.Database
import com.example.teacherratings.database.Rating
import com.example.teacherratings.database.Teacher
import com.example.teacherratings.ui.theme.TeacherRatingsTheme
import com.example.teacherratings.ui.theme.b
import kotlinx.coroutines.launch

class Rate : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TeacherRatingsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val i = intent

                    val scaffoldState = rememberBottomSheetScaffoldState()
                    val scope = rememberCoroutineScope()


                    val sheetState = rememberModalBottomSheetState()

                    var isSheetOpen by rememberSaveable {
                        mutableStateOf(false)
                    }

                    val teacherId = i.extras?.getString("teacherId")
                    val studentId = i.extras?.getString("studentId")

                    var teacher by remember {
                        mutableStateOf(Teacher())
                    }
                    var comments by remember{
                        mutableStateOf<List<Comments>>(emptyList())
                    }
                    Database.getComment {
                        comments = it
                    }

                    if (teacherId != null) {
                        Database.getTeacher(teacherId) {
                            teacher = it
                        }
                    }
                    var rating1 by remember {
                        mutableDoubleStateOf(0.0)
                    }

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            modifier = Modifier.size(200.dp),
                            imageVector = Icons.Filled.AccountCircle,
                            contentDescription = "",
                            tint = b
                        )
                        Text("${teacher.name}", fontSize = 28.sp, fontWeight = FontWeight.Bold)
                        Text("${teacher.subject}", fontSize = 24.sp)
                        Spacer(modifier = Modifier.padding(10.dp))
                        Text("INHA University", fontSize = 24.sp)
                        Spacer(modifier = Modifier.padding(10.dp))
                        RatingBar(
                            modifier = Modifier
                                .size(50.dp),
                            rating = rating1,
                            onRatingChanged = {
                                rating1 = it
                            },
                            starsColor = Color(237, 138, 25)
                        )
                        Button(
                            modifier = Modifier
                                .width(200.dp)
                                .padding(top = 20.dp),
                            onClick = {
                                if (studentId != null && teacherId != null) {
                                    Toast.makeText(
                                        this@Rate,
                                        "Rated successfully",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                    Database.setRating(Rating(studentId, teacherId, rating1))

                                    finish()
                                }
                            }) {
                            Text("Rate")
                        }
                        Spacer(modifier = Modifier.padding(10.dp))
                        Text(modifier = Modifier.clickable {
                            isSheetOpen = true
                            scope.launch {
                                scaffoldState.bottomSheetState.expand()
                            }
                        }, text = "Read comments", color = b)

                        if (isSheetOpen) {
                            ModalBottomSheet(
                                sheetState = sheetState,
                                onDismissRequest = {
                                    isSheetOpen = false
                                },
                            ) {
                                Column(modifier = Modifier.height(200.dp).padding(horizontal = 18.dp)) {
                                    LazyColumn() {
                                        items(comments.filter { it.to == teacherId }){
                                            Row(modifier = Modifier.padding(bottom = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    modifier = Modifier.size(50.dp),
                                                    imageVector = Icons.Filled.AccountCircle,
                                                    contentDescription = "",
                                                    tint = b
                                                )
                                                Column(modifier = Modifier.padding(start = 10.dp)) {
                                                    it.from?.let { it1 -> Text(text = it1, fontWeight = FontWeight.Bold) }
                                                    it.comment?.let { it1 -> Text(it1) }
                                                }
                                            }
                                        }
                                    }
                                }
                                var comment by remember {
                                    mutableStateOf("")
                                }
                                OutlinedTextField(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    value = comment,
                                    onValueChange = { comment = it },
                                    trailingIcon = {
                                        Icon(modifier = Modifier.clickable {
                                            Database.sendComment(Comments(studentId,teacherId, comment))
                                            comment = ""
                                        },
                                            imageVector = Icons.Filled.ArrowCircleUp,
                                            contentDescription = "Search Icon"
                                        )
                                    },
                                    textStyle = TextStyle(fontSize = 18.sp),
                                    label = { Text(text = "Comment") },
                                    placeholder = { Text(text = "Leave a comment") }
                                )
                            }
                        }
                    }

                }
            }
        }
    }

}