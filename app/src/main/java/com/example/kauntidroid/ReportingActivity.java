package com.example.kauntidroid;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ReportingActivity extends AppCompatActivity {

    TextView results, tv1, customer, businessType, validTill, businessPremises, plotNumber, info;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporting);

        results = (TextView) findViewById(R.id.results_view);
        tv1 = (TextView) findViewById(R.id.textView2);
        customer = (TextView) findViewById(R.id.customer_view);
        businessType = (TextView) findViewById(R.id.businessType_view);
        validTill = (TextView) findViewById(R.id.expiry_view);
        businessPremises = (TextView) findViewById(R.id.premises_view);
        plotNumber = (TextView) findViewById(R.id.plotNumber_view);
        info = (TextView) findViewById(R.id.info_view);


        if(getIntent().getStringExtra("response").contains("FAKE")){
            results.setTextColor(Color.RED);
            results.setText("FAKE PERMIT");
            tv1.setText("INVALID");
            customer.setText("INVALID");
            businessType.setText("INVALID");
            validTill.setText("INVALID");
            businessPremises.setText("INVALID");
            plotNumber.setText("INVALID");
            info.setText("WARNING: "
            + "This permit does not exist in the county records ");
        }
        else{
            results.setTextColor(Color.GREEN);
            results.setText("GENUINE PERMIT");
            tv1.setText(getIntent().getStringExtra("id"));
            customer.setText(getIntent().getStringExtra("customer"));
            businessType.setText(getIntent().getStringExtra("businessType"));
            validTill.setText(getIntent().getStringExtra("validTill"));
            businessPremises.setText(getIntent().getStringExtra("businessPremises"));
            plotNumber.setText(getIntent().getStringExtra("plotNumber"));
            info.setText("MESSAGE: "
                    + "A copy of this permit was found in the county records "
            + "If the details below match that of the printed permit "
            + "Then the scanned permit is genuine");

        }

    }
}
