package com.gitsearchapp.util;

import android.app.ProgressDialog;
import android.content.Context;

public class ProjectPreferences {

    public static ProgressDialog dialogShow(Context cntx, String title, String msg) {
        ProgressDialog pdialog = ProgressDialog.show(cntx, title, msg);
        pdialog.show();
        return  pdialog;
    }
}
