package com.example.teacherratings.database

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class Database {
    companion object {
        val ref = Firebase.database.reference.child("teachers")
        val ref1 = Firebase.database.reference.child("students")
        val ref2 = Firebase.database.reference.child("ratings")
        val ref3 = Firebase.database.reference.child("comments")

        fun setTeacher(teacher: Teacher) {
            ref.child(teacher.teacherId!!).setValue(teacher)
        }

        fun setStudents(student: Student) {
            ref1.child(student.userId!!).setValue(student)
        }

        fun getTeachers(callback: (List<Teacher>) -> Unit) {
            ref.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val teacherList =
                        snapshot.children.mapNotNull { it.getValue(Teacher::class.java) }
                    callback(teacherList)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("Firebase", "Error fetching teachers: ${error.message}")
                    callback(emptyList())
                }
            })
        }

        fun checkStudent(student: Student, callback: (String) -> Unit) {
            ref1.child(student.userId!!)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val user = dataSnapshot.getValue(Student::class.java)!!
                        if (user.userId == student.userId && user.password == student.password) {
                            callback("Logged in Successfully")
                        } else {
                            callback("Incorrect Username or Password")
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        callback("Database Error")
                    }
                })
        }

        fun getTeacher(id:String,callback: (Teacher) -> Unit){
            ref.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val children = snapshot.children
                    var teacher = Teacher()
                    children.forEach {
                        val t = it.getValue(Teacher::class.java)
                        if (id == t!!.teacherId){
                            teacher = t
                        }
                    }
                    callback(teacher)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("Firebase", "Error fetching teachers: ${error.message}")
                    callback(Teacher())
                }
            })
        }

        fun setRating(rating: Rating){
            val key = ref.push().key.toString()
            ref2.child(key).setValue(rating)
        }

        fun getRatings(callback: (List<Rating>) -> Unit){
            ref2.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val ratingList =
                        snapshot.children.mapNotNull { it.getValue(Rating::class.java) }
                    callback(ratingList)
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(emptyList())
                }
            })
        }

        fun sendComment(comments: Comments){
            val key = ref.push().key.toString()
            ref3.child(key).setValue(comments)
        }

        fun getComment(callback: (List<Comments>) -> Unit){
            ref3.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val commentList =
                        snapshot.children.mapNotNull { it.getValue(Comments::class.java) }
                    callback(commentList)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("Firebase", "Error fetching teachers: ${error.message}")
                    callback(emptyList())
                }
            })
        }

    }
}

data class Teacher(val teacherId: String?, val name: String?, val subject: String?) {
    constructor() : this(null, null, null)
}

data class Student(val userId: String?, val password: String?) {
    constructor() : this(null, null)
}

data class Rating(val from: String?, val to: String?, val rate: Double?) {
    constructor() : this(null, null, null)
}
data class Comments(val from:String?,val to:String?,val comment:String?){
    constructor() : this(null, null, null)
}