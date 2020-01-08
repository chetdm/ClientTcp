package com.example.clienttcp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void clearMessage(View view) {
        EditText edt = (EditText) findViewById(R.id.edtSendMessage);
        edt.setText("");
    }

    public void sendMessage(View view) {
        EditText edt = (EditText) findViewById(R.id.edtSendMessage);
        String msg = edt.getText().toString();
        TextView txt = (TextView) findViewById(R.id.textView2);
        txt.append(msg + "  ");
        new Connection(this).execute(msg);
    }

    public class Connection extends AsyncTask<String, String, NetworkResponse> {
        MainActivity mainActivity;

        public Connection(MainActivity mainActivity) {
            this.mainActivity = mainActivity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected NetworkResponse doInBackground(String... messages) {
            String hostName = "chetdm.keenetic.pro";//"chet.sytes.net"
            int port = 13000;
            NetworkCommand networkCommand = new NetworkCommand(0);
            try {
                InetAddress inetAddress = InetAddress.getByName(hostName);
                networkCommand.text = messages[0];
                ClientSocket clientSocket = new ClientSocket(new NetworkServerSocketAddress(inetAddress, port), networkCommand);
                clientSocket.run();
                try(OutputStream outputStream = mainActivity.openFileOutput("snapshot.png", MODE_PRIVATE)) {
                    clientSocket.getResponse().getBitmap().compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                   e.printStackTrace();
                }
                return clientSocket.getResponse();
            } catch (Exception e) {
                e.getMessage();
                return null;
            }
        }

        @Override
        protected void onPostExecute(NetworkResponse response) {
            super.onPostExecute(response);
            ImageView imageView = (ImageView) mainActivity.findViewById(R.id.imageView);
            imageView.setImageBitmap(response.getBitmap());
            TextView txt = (TextView) mainActivity.findViewById(R.id.textView2);
            txt.append(response.getText() + "  ");
        }
    }
}

