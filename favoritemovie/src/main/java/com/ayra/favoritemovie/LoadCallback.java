package com.ayra.favoritemovie;

import java.util.ArrayList;

public interface LoadCallback {
    void preExecute();

    void postExecute(ArrayList<Movie> movies);
}
