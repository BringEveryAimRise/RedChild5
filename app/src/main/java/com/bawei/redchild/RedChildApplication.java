package com.bawei.redchild;
import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.widget.TextView;

import com.baidu.location.LocationClient;
import com.bawei.redchild.home.home_activity.H_xq_bdu_dwei_Activity;
import com.greendao.dao.DaoMaster;
import com.greendao.dao.DaoSession;
import com.bawei.redchild.me.me.Utils.Utils;
import com.iflytek.cloud.SpeechUtility;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

/**
*日期:2017/5/25 
 * 时间:8:29
*类描述：
*/
public class RedChildApplication extends Application {
    public DaoSession daoSession;
    public SQLiteDatabase db;
    public DaoMaster.DevOpenHelper helper;
    public DaoMaster daoMaster;

    {
        PlatformConfig.setQQZone("1106106081", "ETz0UvZADX01YwuH");
        PlatformConfig.setSinaWeibo("447858330", "1b7a78e7a1f6455d9426ad87c8bacb28","http://sns.whalecloud.com");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SpeechUtility.createUtility(this, "appid=" + getString(R.string.app_id));
        UMShareAPI.get(this);
        Utils.init(getApplicationContext());
        setupDatabase();

    }
    private void setupDatabase() {
        // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
        // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
        helper = new DaoMaster.DevOpenHelper(this,"data.db", null);
        db = helper.getWritableDatabase();
        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public SQLiteDatabase getDb() {
        return db;
    }
}
