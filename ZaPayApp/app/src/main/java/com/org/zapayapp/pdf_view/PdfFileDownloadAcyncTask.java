package com.org.zapayapp.pdf_view;
import android.content.Context;
import android.os.AsyncTask;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class PdfFileDownloadAcyncTask extends AsyncTask<String, Void, InputStream> {
   private Context context;

   private PdfResponseListener pdfResponseListener;

   public interface PdfResponseListener{
      void pdfResponse(InputStream inputStream);
   }

   public PdfFileDownloadAcyncTask(Context context,PdfResponseListener pdfResponseListener){
       this.context=context;
       this.pdfResponseListener=pdfResponseListener;
   }

    @Override
    protected InputStream doInBackground(String... strings) {
        String fileUrl = strings[0];
        return downloadFile(fileUrl);
    }

    @Override
    protected void onPostExecute(InputStream inputStream) {
        super.onPostExecute(inputStream);
        pdfResponseListener.pdfResponse(inputStream);
   }


    public InputStream downloadFile(String fileUrl) {
        InputStream inputStream = null;
        try {
            URL url = new URL(fileUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.connect();
            inputStream = urlConnection.getInputStream();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputStream;

    }



}
