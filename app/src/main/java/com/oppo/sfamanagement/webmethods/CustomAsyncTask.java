package com.oppo.sfamanagement.webmethods;

import android.content.AsyncTaskLoader;
import android.content.Context;

/**
 * Created by AllSmart-LT008 on 8/8/2016.
 */

public abstract class CustomAsyncTask<D> extends AsyncTaskLoader<D> {

    private D data;

    public CustomAsyncTask(Context context) {
        super(context);
    }

    @Override
    public void deliverResult(D data) {
        if (isReset()) {
            // An async query came in while the loader is stopped
            return;
        }

        this.data = data;

        super.deliverResult(data);
    }


    @Override
    protected void onStartLoading() {
        if (data != null) {
            deliverResult(data);
        }

        if (takeContentChanged() || data == null) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        // Attempt to cancel the current load task if possible.
        cancelLoad();
    }

    @Override
    protected void onReset() {
        super.onReset();

        // Ensure the loader is stopped
        onStopLoading();

        data = null;
    }
}
