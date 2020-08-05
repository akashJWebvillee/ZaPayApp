package com.org.zapayapp.viewModel;

import android.app.Application;
import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.org.zapayapp.webservices.APICalling;

public class ProjectViewModel extends AndroidViewModel {

    private ProjectRepository projectRepository;

    public ProjectViewModel(@NonNull Application application) {
        super(application);
        projectRepository = new ProjectRepository();
    }

    public MutableLiveData<JsonObject> checkAppVersion(Context context, APICalling apiCalling, Gson gson, View view) {
        return projectRepository.checkAppVersion(context, apiCalling, gson, view);
    }
}
