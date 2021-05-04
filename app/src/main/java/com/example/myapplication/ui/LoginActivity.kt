package com.example.myapplication.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import java.io.*
import java.net.Socket

class LoginActivity: AppCompatActivity() {

	private lateinit var signInButton : Button
	private lateinit var registerButton: Button
	private lateinit var passwordEdit: EditText
	private lateinit var loginEdit: EditText
	private lateinit var displayInf: TextView

	@SuppressLint("SetTextI18n")
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_login)

		signInButton = findViewById(R.id.Sign_In_Button)
		registerButton = findViewById(R.id.Register_Button)
		displayInf = findViewById(R.id.display_inf)
		passwordEdit = findViewById(R.id.password_Edit)
		loginEdit = findViewById(R.id.login_Edit)

		signInButton.setOnClickListener {

			sendMessage("201:(${loginEdit.text},${passwordEdit.text})")

			if(userID == "-1") {
				displayInf.text = "Incorrect Username/Password"
			}
		}

		registerButton.setOnClickListener {
			val intent = RegistrationActivity.newIntent(this@LoginActivity)
			startActivity(intent)
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

				userID = input.readLine()
				if(userID == "-1") {
					userID = "-1"
				}
				else {
					val intent = MainActivity.newIntent(this@LoginActivity)
					startActivity(intent)
				}
/*				when (input.readLine()) {
					//User Access Granted
					"1" -> {
						val intent = MainActivity.newIntent(this@LoginActivity)
						startActivity(intent)
					}
					//Admin Access Granted
					"104" -> {
						val intent = AdminActivity.newIntent(this@LoginActivity)
						startActivity(intent)
					}
					"-1" -> {
						test = 1
					}
				}
*/
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
		var userID = "0"
		fun newIntent(packageContext: Context) : Intent {

			return Intent(packageContext, LoginActivity::class.java)
		}
	}

}