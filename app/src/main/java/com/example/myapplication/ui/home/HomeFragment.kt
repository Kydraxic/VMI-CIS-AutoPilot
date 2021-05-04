package com.example.myapplication.ui.home

import android.os.Bundle
import android.os.Handler
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ScrollView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.ui.LoginActivity.Companion.userID
import java.io.*
import java.net.Socket
import java.util.*


class HomeFragment : Fragment() {

	private lateinit var getDataButton: Button
	private lateinit var stockName: TextView
	private lateinit var price: TextView
	private lateinit var homeTextView: TextView
	private lateinit var homeScrollView: ScrollView


	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		val view = inflater.inflate(R.layout.fragment_home, container, false)

		getDataButton = view.findViewById(R.id.get_data_button) as Button
		stockName = view.findViewById(R.id.stock_name_header) as TextView
		price = view.findViewById(R.id.price_header) as TextView
		homeTextView = view.findViewById(R.id.text_home) as TextView


		//val lstValues: List<String> = serverData.split(",").map { it.trim() }


		homeTextView.movementMethod = ScrollingMovementMethod()

		getDataButton.setOnClickListener {
			//val userID = test
			sendMessage("202:($userID)")

			//val lstValues: List<String> = serverData.split(",").map { it.trim() }

			//val stock = serverData.lines()

			val stock = serverData.split(",")

			for (item in stock.indices - 1) {
				if (item % 2 == 0) {
					homeTextView.append("Stock: " + stock[item] + "\n")
				}
				else
					homeTextView.append("Price: " + stock[item] + "\n\n")
			}

			//homeTextView.text = "${stock} \n"

			//homeTextView.text = lstValues.forEach(println(it)).toString()
			//homeTextView.text = serverData

		}

		//homeTextView.text = serverData
		return view
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

				serverData = input.readLine()

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
		var serverData = ""
	}
}

