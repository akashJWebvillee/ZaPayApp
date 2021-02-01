package com.org.zapayapp.activity;

import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.org.zapayapp.R;
import com.org.zapayapp.pdf_view.PdfFileDownloadAcyncTask;

import java.io.InputStream;

public class PdfViewActivity extends BaseActivity implements View.OnClickListener, PdfFileDownloadAcyncTask.PdfResponseListener, OnPageChangeListener, OnLoadCompleteListener {
    private Toolbar toolbar;
    private TextView titleTV;
    private ImageView backArrowImageView;
    private ProgressBar progressBar;
    private ImageView closeTV;

    private PDFView pdfView;
    private String myPdfUrl;
    private int pageNumber = 0;
    private int pageCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_view);
        inIt();
        getIntentFinc();
        inItAction();
    }

    private void inIt() {
        toolbar = findViewById(R.id.customToolbar);
        titleTV = toolbar.findViewById(R.id.titleTV);
        backArrowImageView = toolbar.findViewById(R.id.backArrowImageView);
        backArrowImageView.setVisibility(View.VISIBLE);
        backArrowImageView.setOnClickListener(this);
        backArrowImageView.setVisibility(View.GONE);


        pdfView = findViewById(R.id.pdfView);
        progressBar = findViewById(R.id.progressBar);
        closeTV = findViewById(R.id.closeTV);
        closeTV.setOnClickListener(this);

        apiCalling.setRunInBackground(true);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void getIntentFinc() {
        if (getIntent().getStringExtra("pdf_url") != null) {
            String pdf_url = getIntent().getStringExtra("pdf_url");
            if (pdf_url != null && pdf_url.length() > 0) {
                myPdfUrl = pdf_url;
            }
        }
    }

    private void inItAction() {
        if (myPdfUrl != null && myPdfUrl.length() > 0) {
            new PdfFileDownloadAcyncTask(this, this).execute(myPdfUrl);

        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void pdfResponse(InputStream inputStream) {
        pdfView.fromStream(inputStream)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(PdfViewActivity.this))
                .defaultPage(pageNumber)
                .swipeHorizontal(false)
                .enableSwipe(true)
                .enableDoubletap(true)
                .load();
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
        this.pageCount = pageCount;
    }

    @Override
    public void loadComplete(int nbPages) {
        apiCalling.setRunInBackground(true);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backArrowImageView:
                finish();
                break;
            case R.id.closeTV:
                finish();
                break;
        }
    }
}