package com.ayra.moviecatalogue.data.response;

import com.ayra.moviecatalogue.data.model.TvShow;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TvShowResponse {

    @SerializedName("page")
    @Expose
    private int page;
    @SerializedName("total_results")
    @Expose
    private int totalResults;
    @SerializedName("total_pages")
    @Expose
    private int totalPages;
    @SerializedName("results")
    @Expose
    private ArrayList<TvShow> results = null;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public ArrayList<TvShow> getResults() {
        return results;
    }

    public void setResults(ArrayList<TvShow> results) {
        this.results = results;
    }


}
