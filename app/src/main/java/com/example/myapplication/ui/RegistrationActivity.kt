package com.example.myapplication.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import java.io.*
import java.net.Socket

class RegistrationActivity : AppCompatActivity() {
	
	private lateinit var fullName : EditText
	private lateinit var emailAddress : EditText
	private lateinit var address : EditText
	private lateinit var phoneNumber : EditText
	private lateinit var username : EditText
	private lateinit var password : EditText
	private lateinit var passwordRetype : EditText

	private lateinit var backButton: Button
	private lateinit var createButton: Button
	
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_registration)
		
		fullName = findViewById(R.id.create_full_name_editText)
		emailAddress = findViewById(R.id.create_email_address_editText)
		address = findViewById(R.id.create_address_editText)
		phoneNumber = findViewById(R.id.create_phone_number_editText)
		username = findViewById(R.id.create_username_editText)
		password = findViewById(R.id.create_password_editText)
		passwordRetype = findViewById(R.id.retype_password_editText)

		backButton = findViewById(R.id.Back_Button)
		createButton = findViewById(R.id.Submit_Registration_Button)
		
		
		backButton.setOnClickListener {
			val intent = LoginActivity.newIntent(this@RegistrationActivity)
			startActivity(intent)
		}
		
		createButton.setOnClickListener {

			if (password.text.toString() == passwordRetype.text.toString()) {

				sendMessage("303:${username.text},${password.text},${fullName.text},${address.text},${phoneNumber.text},${emailAddress.text},0")
			}
			else {
				val toast: Toast = Toast.makeText(this, "Passwords do not match, retype passwords", Toast.LENGTH_SHORT)
				toast.show()
			}
		}
		
	}
	private fun sendMessage(code: String) {
		Handler()
		val thread = Thread {
			try {
				//Replace below IP with the IP of that device in which server socket open.
				//If you change port then change the port number in the server side code also.
				val s = Socket("199.244.104.202", 9880)
				val out: OutputStream = s.getOutputStream()
				val output = PrintWriter(out)
				output.println(code)
				output.flush()
				val input = BufferedReader(InputStreamReader(s.getInputStream()))

				when (input.readLine()) {
					//Query Accepted, Move to LoginActivity
					"102" -> {
						val intent = LoginActivity.newIntent(this@RegistrationActivity)
						startActivity(intent)
					}

				}

				output.close()
				out.close()
				s.close()
			} catch (e: IOException) {
				e.printStackTrace()
			}
		}
		thread.start()
	}

	companion object{
		fun newIntent(packageContext: LoginActivity) : Intent {

			return Intent(packageContext, RegistrationActivity::class.java)
		}
	}

}