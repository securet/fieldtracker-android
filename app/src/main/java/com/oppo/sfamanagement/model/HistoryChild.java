package com.oppo.sfamanagement.model;

import java.util.ArrayList;

/**
 * Created by allsmartlt218 on 16-12-2016.
 */

public class HistoryChild {
    private String comments;
    private String fromDate;
    private int background;

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    @Override
    public String toString() {
        return this.fromDate + " " + this.comments;
    }

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }
}
