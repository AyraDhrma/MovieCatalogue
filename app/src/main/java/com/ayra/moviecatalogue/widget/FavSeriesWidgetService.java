package com.ayra.moviecatalogue.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class FavSeriesWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new FavSeriesRemoteViewsFactory(this.getApplicationContext());
    }
}
