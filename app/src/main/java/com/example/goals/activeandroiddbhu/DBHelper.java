package com.example.goals.activeandroiddbhu;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.goals.activeandroiddbhu.model.DaoMaster;

/**
 * Created by huyongqiang on 2017/5/10.
 */

public class DBHelper extends DaoMaster.DevOpenHelper {
    public static final String DBNAME = "hu.db";

    public DBHelper(Context context) {
        super(context, DBNAME, null);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
    }
}
