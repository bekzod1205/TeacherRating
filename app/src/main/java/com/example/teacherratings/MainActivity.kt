package com.example.teacherratings

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.teacherratings.database.Database
import com.example.teacherratings.database.Student
import com.example.teacherratings.database.Teacher
import com.example.teacherratings.screens.Login
import com.example.teacherratings.ui.theme.TeacherRatingsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TeacherRatingsTheme {
//                val teachers = listOf(
//                    Teacher("T001", "Stefchko Dokov", "Calculus"),
//                    Teacher("T002", "Abdullayev Sarvar", "Intro2IT"),
//                    Teacher("T003", "Toshxo'jayev Rustam", "Physics"),
//                    Teacher("T004", "Kesenbayev Sultan", "English"),
//                    Teacher("T005", "Suvanov Sharof", "OOP"),
//                    Teacher("T006", "Atamuratov Farruh", "Physics"),
//                    Teacher("T007", "Halilova Alie", "English"),
//                    Teacher("T008", "Naseer Abdul Rahim", "Software Engineering"),
//                )
//                val students = listOf(
//                    Student("U001", "admin1234"),
//                    Student("U002", "admin1234"),
//                    Student("U003", "admin1234"),
//                    Student("U004", "admin1234"),
//                    Student("U005", "admin1234"),
//                    Student("U006", "admin1234"),
//                    Student("U007", "admin1234"),
//                    Student("U008", "admin1234")
//                )
//                teachers.forEach {
//                    Database.setTeacher(it)
//                }
//                students.forEach{
//                    Database.setStudents(it)
//                }
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
            }
        }
    }
}

