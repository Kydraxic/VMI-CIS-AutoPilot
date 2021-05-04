package com.example.myapplication.admin

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class AdminActivity : AppCompatActivity() {

	private lateinit var recyclerView: RecyclerView

	@SuppressLint("WrongConstant")
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		//getting recyclerview from xml
		recyclerView = findViewById(R.id.admin_recyclerView)

		//adding a layoutmanager
		recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)


		//crating an arraylist to store users using the data class user
		val users = ArrayList<User>()

		//adding Dummy data to the list
		//Needs to pull from DB
		users.add(User("Sullivan Stern", "$1,102.37"))
		users.add(User("Evan Montieth", "$523.79"))
		users.add(User("Cole Curtis", "$25.07"))

		//creating our adapter
		val adapter = CustomAdapter(users)

		//now adding the adapter to recyclerview
		recyclerView.adapter = adapter
	}
	companion object{
		fun newIntent(packageContext: Context) : Intent {

			return Intent(packageContext, AdminActivity::class.java)
		}
	}
}