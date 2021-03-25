package barcodescanner;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.CompoundBarcodeView;
import com.sd.carc.R;

/**
 * Created by devaraj on 27/11/17.
 */

public class ScannerActivity extends Activity{

    CompoundBarcodeView barcodeView;
    boolean isScanned = true;
    String returnMethodName = "";
    private CaptureManager capture;
    Button backbutton;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.scanner_layout);

        returnMethodName = this.getIntent().getStringExtra("returnMethodName");


        barcodeView = findViewById(R.id.barcode_scanner);
        backbutton = findViewById(R.id.setup_macroCancelbtn);
        capture = new CaptureManager(this, barcodeView);
        capture.initializeFromIntent(getIntent(), savedInstanceState);
        capture.decode();

        backbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }

}
