package com.example.myapplication.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.ui.LoginActivity.Companion.userID
import java.io.*
import java.net.Socket

class withdrawFundsFragment : Fragment()  {

	private lateinit var withdrawButton: Button
	private lateinit var sellButton: Button
	private lateinit var passwordEditText: EditText
	private lateinit var amountWithdrawn: TextView

	@SuppressLint("SetTextI18n")
	override fun onCreateView(
			inflater: LayoutInflater,
			container: ViewGroup?,
			savedInstanceState: Bundle?
	): View? {
		val view = inflater.inflate(R.layout.fragment_withdraw_funds, container, false)

		withdrawButton = view.findViewById(R.id.withdraw_button) as Button
		sellButton = view.findViewById(R.id.sell_Button) as Button
		passwordEditText = view.findViewById(R.id.password_edit_withdrawalFunds) as EditText
		amountWithdrawn = view.findViewById(R.id.amount_withdrawn) as TextView


		sellButton.setOnClickListener {

			sendMessage("105:($userID)")
			Toast.makeText(activity,"Started selling! This might take a few minutes", Toast.LENGTH_SHORT).show();
		}

		withdrawButton.setOnClickListener {

			sendMessage("304:($userID")
			updateView()
		}

		return view
	}
	@SuppressLint("SetTextI18n")
	private fun updateView() {
		if(amountString != "-1") {
			amountWithdrawn.text = "$amountString withdrawn"
		}
		else {
			amountWithdrawn.text = "Withdraw Failed"
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

				amountString = input.readLine()

				output.close()
				out.close()
				s.close()
			} catch (e: IOException) {
				e.printStackTrace()
			}
		}
		thread.start()
	}

	companion object {
		var amountString = "0"

	}
}