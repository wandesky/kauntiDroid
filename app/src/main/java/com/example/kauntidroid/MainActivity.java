package com.example.kauntidroid;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


//Extra imports for http requests
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONException;
import org.json.JSONObject;

//for the sake of intents
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private TextView reportData;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
//                    reportData.setText(resultReport);
                    return true;
//                case R.id.navigation_dashboard:
//                    mTextMessage.setText(R.string.title_dashboard);
//                    return true;
//                case R.id.navigation_notifications:
//                    mTextMessage.setText(R.string.title_notifications);
//                    return true;
            }
            return false;
        }
    };

    private ZXingScannerView scannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        reportData = (TextView)findViewById(R.id.permitsTextView);
//        reportData.setText(resultReport);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


    }

    public void scanCode(View view){
        scannerView = new ZXingScannerView(this);
        scannerView.setResultHandler(new ZXingScannerResultHandler());

        setContentView(scannerView);
        scannerView.startCamera();
    }

    @Override
    public void onPause(){
        super.onPause();
        scannerView.stopCamera();
    }
    String resultCode, response, businessType, validTill, businessPremises, plotNumber;
    String resultReport = "NO RESULT RECEIVED";
    String customerName;

    public class Connection extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... args){
            //Start of http request process
            try {
                sendRequest();
//                reportData.setText(resultReport);
            } catch (IOException e) {
//                Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } catch (JSONException e) {
//                Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
//            reportData.setText(resultReport);
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            super.onPostExecute(result);
//            reportData.setText(resultReport);

//            reportData = (TextView)findViewById(R.id.permitsTextView);
//            reportData.setText(resultReport);
            System.out.println("*****ON POST EXECUTE CALLED****** ");
            generateReport();
        }
    }

    public void generateReport() {
        Intent intent = new Intent(MainActivity.this, ReportingActivity.class);
        intent.putExtra("response", response);
        intent.putExtra("id", resultReport);
        intent.putExtra("customer", customerName);
        intent.putExtra("businessType", businessType);
        intent.putExtra("validTill", validTill);
        intent.putExtra("businessPremises", businessPremises);
        intent.putExtra("plotNumber", plotNumber);

        startActivity(intent);


//        Intent intent = new Intent(this, ReportingActivity.class);
////        EditText editText = (EditText) findViewById(R.id.editText);
//        String message = resultReport;
//        intent.putExtra("ID", message);
//        intent.putExtra
//        startActivity(intent);
    }


    class ZXingScannerResultHandler implements ZXingScannerView.ResultHandler{
        @Override
        public void handleResult(Result result){
            resultCode = result.getText();
            Toast.makeText(MainActivity.this, resultCode, Toast.LENGTH_SHORT).show();

            setContentView(R.layout.activity_main);
            reportData = (TextView)findViewById(R.id.permitsTextView);
            reportData.setText("Please wait for the results to load");

            scannerView.stopCamera();

            new Connection().execute();

//            Toast.makeText(MainActivity.this, resultReport, Toast.LENGTH_SHORT).show();

//            setContentView(R.layout.activity_main);
//            scannerView.stopCamera();

        }
    }


    //methods to retrieve data from server
    private String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }


    public void sendRequest() throws IOException, JSONException {
//        resultCode = "TCG-SBP-2018-2019-1276";
//        reportData.setText("SEND REQUEST METHOD CALLED");
        String url = "https://kaunti.herokuapp.com/api/v1/permits/" + resultCode ;
        JSONObject json = readJsonFromUrl(url);


//        resultReport = "RESULTS \n";
        resultReport = json.get("permitID").toString();
        System.out.println("*****RESULT REPORT ****** " + resultReport);

        //Checking if results are valid
        if(resultReport.contains("invalid")){
            response = "FAKE";
//            return;
        }
        else{
            response = "GENUINE";
            customerName = json.get("customer").toString();
            businessType = json.get("businessType").toString();
            validTill = json.get("validTill").toString();
            businessPremises = json.get("businessAddress").toString();
            plotNumber = json.get("plotNumber").toString();
        }

//        reportData.setText(resultReport);
//        return null;
//        reportData.setText(resultReport);


//        System.out.println(json.toString());
//        System.out.println(json.get("permitID"));

//        Toast.makeText(MainActivity.this, json.toString(), Toast.LENGTH_LONG).show();
    }
}
