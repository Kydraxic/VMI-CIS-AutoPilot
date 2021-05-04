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
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.ui.LoginActivity.Companion.userID
import java.io.*
import java.net.Socket


class AddFundsFragment : Fragment()  {

	private lateinit var wallet : EditText
	private lateinit var submitButton : Button
	private lateinit var displayInf : TextView

	@SuppressLint("SetTextI18n")
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		val view = inflater.inflate(R.layout.fragment_add_funds, container, false)

		wallet = view.findViewById(R.id.deposit_amount) as EditText
		submitButton = view.findViewById(R.id.submit_Button) as Button
		displayInf = view.findViewById(R.id.display_funds_inf)

		submitButton.setOnClickListener {

			val amount = wallet.text
			sendMessage("204:($userID,$amount)")

			if (returnVal != "-1") {
				displayInf.text = "Added $${wallet.text} to your wallet"
			} else {
				displayInf.text = "Failed to add funds"
			}
		}

		return view
	}

	@SuppressLint("SetTextI18n")
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
		var returnVal = "0"
	}
}