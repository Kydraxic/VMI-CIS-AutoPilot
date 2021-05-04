package com.example.myapplication;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.myapplication.ui.LoginActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ClientConnect extends AppCompatActivity {

	public static final int SERVER_PORT = 9880;

	public static final String SERVER_IP = "199.244.104.202";
	private ClientThread clientThread;
	private Thread thread;
	private LinearLayout msgList;
	private Handler handler;
	private int clientTextColor;
	private EditText edMessage;

	public ClientConnect(){

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		setTitle("Client");
		clientTextColor = ContextCompat.getColor(this, R.color.green);
		handler = new Handler();
		msgList = findViewById(R.id.msgList);
		edMessage = findViewById(R.id.edMessage);

	}

	public void onStart() {
		super.onStart();

		clientThread = new ClientThread();
		thread = new Thread(clientThread);
		thread.start();
	}

	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
	public TextView textView(String message, int color, Boolean value) {
		if (null == message || message.trim().isEmpty()) {
			message = "<Empty Message>";
		}
		TextView tv = new TextView(this);
		tv.setTextColor(color);
		tv.setText(message + " [" + getTime() + "]");
		tv.setTextSize(20);
		tv.setPadding(0, 5, 0, 0);
		tv.setLayoutParams(new LinearLayout.LayoutParams
				(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 0));
		if (value) {
			tv.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
		}
		return tv;
	}

	public void showMessage(final String message, final int color, final Boolean value) {
		handler.post(new Runnable() {
			@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
			@Override
			public void run() {
				msgList.addView(textView(message, color, value));
			}
		});
	}

	@SuppressLint("SetTextI18n")
	public void onClick(View view) {

		if (view.getId() == R.id.submit_Button) {
			msgList.removeAllViews();
			clientThread = new ClientThread();
			thread = new Thread(clientThread);
			thread.start();
			return;
		}

		if (view.getId() == R.id.send_data) {
			String clientMessage = edMessage.getText().toString().trim();
			showMessage(clientMessage, Color.BLUE, false);
			if (null != clientThread) {
				if (clientMessage.length() > 0){
					clientThread.sendMessage(clientMessage);
				}
				edMessage.setText("");
			}
		}

		if (view.getId() == R.id.Submit_Registration_Button) {
			edMessage.setText("101");
			String clientMessage = edMessage.getText().toString().trim();
			showMessage(clientMessage, Color.BLUE, false);
			if (null != clientThread) {
				if (clientMessage.length() > 0){
					clientThread.sendMessage(clientMessage);
				}
				edMessage.setText("");
			}
		}
	}

	public void sendMessage(String code) {
;
		if (null != clientThread) {
			if (code.length() > 0){
				clientThread.sendMessage(code);
			}
		}
	}

	/* clientThread class defined to run the client connection to the socket network using the server ip and port
	 * and send message */
	class ClientThread implements Runnable {

		private Socket socket;
		private BufferedReader input;

		@Override
		public void run() {

			try {

				InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
				//showMessage("Connecting to Server...", clientTextColor, true);

				socket = new Socket(serverAddr, SERVER_PORT);

				if (socket.isBound()){

					//showMessage("Connected to Server...", clientTextColor, true);
				}


				while (!Thread.currentThread().isInterrupted()) {


					this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					String message = input.readLine();
					if (null == message || "Disconnect".contentEquals(message)) {
						Thread.interrupted();
						message = "Server Disconnected...";
						//showMessage(message, Color.RED, false);
						break;
					}
					//showMessage("Server: " + message, clientTextColor, true);
				}

			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				//showMessage("Problem Connecting to server... Check your server IP and Port and try again", Color.RED, false);
				Thread.interrupted();
				e1.printStackTrace();
			} catch (NullPointerException e3) {
				//showMessage("error returned", Color.RED,true);
			}

		}

		void sendMessage(final String message) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						if (null != socket) {
							PrintWriter out = new PrintWriter(new BufferedWriter(
									new OutputStreamWriter(socket.getOutputStream())),
									true);
							out.println(message);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
		}

	}

	String getTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		return sdf.format(new Date());
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (null != clientThread) {
			clientThread.sendMessage("Disconnect");
			clientThread = null;
		}
	}
}
