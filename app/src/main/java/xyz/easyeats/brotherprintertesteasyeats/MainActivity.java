package xyz.easyeats.brotherprintertesteasyeats;

import android.graphics.Color;
import android.graphics.pdf.PdfDocument;
import android.graphics.pdf.PdfDocument.PageInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.brother.ptouch.sdk.LabelInfo;
import com.brother.ptouch.sdk.Printer;
import com.brother.ptouch.sdk.PrinterInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.brother.ptouch.sdk.PrinterInfo.Model;
import static com.brother.ptouch.sdk.PrinterInfo.Port;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button bPdfPrint = (Button) findViewById(R.id.pdfButton);

        //Listener Order Queue button
        bPdfPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printPdf();
            }
        });


    }

    public void printPdf() {
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
                printInfo.labelNameIndex = LabelInfo.QL700.W62.ordinal();

                printInfo.printMode = PrinterInfo.PrintMode.FIT_TO_PAPER;
                printInfo.isAutoCut=false;
                printer.setPrinterInfo(printInfo);
//PDF print

                String pdfFile=buildPdf("reciept.pdf","Cactus truck","Funny taco\t$8.99\nKiddo food\t$4.99");
                printer.startCommunication();

//                PrinterStatus mPrintResult = printer.printPdfFile(pdfFile, 1);

                //Load up the Android monitor tab and filter by ez_UserAreaActivity to see these messages
                Log.d("ez_UserAreaActivity","The external pdf file should be stored at: "+pdfFile);
//                Log.d("ez_UserAreaActivity",mPrintResult.errorCode.toString());
                Log.d("ez_UserAreaActivity","Label info "+printer.getLabelInfo().toString());

                printer.endCommunication();

//                deleteFileName(pdfFile);

            }
        }
        PrinterThread p = new PrinterThread();
        new Thread(p).start();

    }


    public String buildPdf(String filename,String headingText,String bodyText){
        // create a new document
        PdfDocument document = new PdfDocument();

        //TODO: Calculate the height based on number of lines fed in
        int headerTextSize=20;
        int bodyTextSize=10;

        //Calulate total pixels height by finding newlines
        int lastIndex = 0;
        int count = 0;

        while(lastIndex != -1){

            lastIndex = bodyText.indexOf("\n",lastIndex);

            if(lastIndex != -1){
                count ++;
                lastIndex += "\n".length();
            }
        }



        int pageWidth=200;

        int pageHeight=headerTextSize*2+count*bodyTextSize+bodyTextSize;

        // crate a page description
        PageInfo pageInfo = new PageInfo.Builder(pageWidth,pageHeight, 1).create();

        // start a page
        PdfDocument.Page page = document.startPage(pageInfo);

        // draw something on the page


        //TODO: Add a second textview and paint and everything

        //Adding title
        TextView textViewTitle = new TextView(MainActivity.this);
        textViewTitle.layout(0, 0, pageWidth, pageHeight); //text box size heightpx x widthpx
        textViewTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, headerTextSize);
        textViewTitle.setTextColor(Color.BLACK);
        //textView.setShadowLayer(5, 2, 2, Color.CYAN); //text shadow
        textViewTitle.setText(headingText+"\n_________________");
        textViewTitle.setDrawingCacheEnabled(true);
        page.getCanvas().drawBitmap(textViewTitle.getDrawingCache(), 1, 1, null);


        //Adding body
        TextView textView = new TextView(MainActivity.this);
        textView.layout(0, 0, pageWidth, pageHeight); //text box size heightpx x widthpx
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, bodyTextSize);
        textView.setTextColor(Color.BLACK);
        //textView.setShadowLayer(5, 2, 2, Color.CYAN); //text shadow
        textView.setText(bodyText);
                textView.setDrawingCacheEnabled(true);
        page.getCanvas().drawBitmap(textView.getDrawingCache(), 1, headerTextSize+headerTextSize/2, null);
        //text box top left position 50,50

//        canvas.save();
//        canvas.translate(50, 20); //position text on the canvas

        // finish the page
        document.finishPage(page);

        String filepath=null;
        try{
            File mypath=new File( MainActivity.this.getExternalFilesDir(null).toString()+"/"+filename);
            filepath=mypath.toString();
            document.writeTo(new FileOutputStream(mypath));
        }
        catch (FileNotFoundException e){
            Log.d("ez_UserAreaActivity","File not found exception");
        }
        catch (IOException e){
            Log.d("ez_UserAreaActivity","IOException");
        }


        // close the document
        document.close();

        return filepath;
    }

    boolean deleteFileName(String filename){
        File mypath=new File( MainActivity.this.getExternalFilesDir(null).toString()+"/"+filename);
        boolean result= mypath.delete();

        if(!result){
            Log.d("ez_UserAreaActivity","Could not delete "+mypath.toString());
        }
        return result;

    }

}
