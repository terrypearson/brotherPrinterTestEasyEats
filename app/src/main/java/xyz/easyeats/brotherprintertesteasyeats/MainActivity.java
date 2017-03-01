package xyz.easyeats.brotherprintertesteasyeats;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.brother.ptouch.sdk.LabelInfo;
import com.brother.ptouch.sdk.Printer;
import com.brother.ptouch.sdk.PrinterInfo;
import com.brother.ptouch.sdk.PrinterStatus;

import static com.brother.ptouch.sdk.PrinterInfo.Model;
import static com.brother.ptouch.sdk.PrinterInfo.Port;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button bPrint = (Button) findViewById(R.id.button);

        //Listener Order Queue button
        bPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                print();
            }
        });

    }

    public void print() {
        class PrinterThread implements Runnable {
            public void run() {
//                String externalStorageDir = Environment.getExternalStorageDirectory().toString();
                String externalStorageDir = MainActivity.this.getExternalFilesDir(null).toString();
                // Toast.makeText(UserAreaActivity.this, externalStorageDir, Toast.LENGTH_LONG).show();
//print setting
                Printer printer = new Printer();
                PrinterInfo printInfo = new PrinterInfo();
                printInfo.port = Port.NET;

                printInfo.printerModel = Model. QL_710W ;
                printInfo.port = Port. NET ;
                printInfo.ipAddress = "192.168.1.149";
                printInfo.labelNameIndex = LabelInfo.QL700.W62 .ordinal();

//                printInfo.printMode = PrinterInfo.PrintMode.FIT_TO_PAPER;

                printer.setPrinterInfo(printInfo);
//PDF print
                String pdfFile = externalStorageDir + "/sample.pdf";
                printer.startCommunication();

                PrinterStatus mPrintResult = printer.printPdfFile(pdfFile, 1);

                //Load up the Android monitor tab and filter by ez_UserAreaActivity to see these messages
                Log.d("ez_UserAreaActivity","The external pdf file should be stored at: "+pdfFile);
                Log.d("ez_UserAreaActivity",mPrintResult.errorCode.toString());
                Log.d("ez_UserAreaActivity","Label info "+printer.getLabelInfo().toString());

                printer.endCommunication();



            }
        }
        PrinterThread p = new PrinterThread();
        new Thread(p).start();

    }
}
