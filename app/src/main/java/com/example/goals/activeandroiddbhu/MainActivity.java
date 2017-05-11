package com.example.goals.activeandroiddbhu;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.goals.activeandroiddbhu.model.DaoMaster;
import com.example.goals.activeandroiddbhu.model.DaoSession;
import com.example.goals.activeandroiddbhu.model.User;
import com.example.goals.activeandroiddbhu.model.UserDao;

import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btnAdd)
    Button btnAdd;
    @BindView(R.id.btnDelete)
    Button btnDelete;
    @BindView(R.id.btnDelete1)
    Button btnDelete1;
    @BindView(R.id.btnDelete2)
    Button btnDelete2;
    @BindView(R.id.btnDelete3)
    Button btnDelete3;
    @BindView(R.id.btnModify)
    Button btnModify;
    @BindView(R.id.btnQuery)
    Button btnQuery;
    @BindView(R.id.btnUpdateDB)
    Button btnUpdateDB;

    UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        writeToSqlite();
    }


    //写入sharedPreference
    private void writeToSharedPreference() {
        SharedPreferences preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("name", "StethoDemo");
        editor.putString("version", "v1.0.0");
        editor.commit();

        Toast.makeText(this, "save to SharedPreference successfully!", Toast.LENGTH_SHORT).show();
    }

    //写入数据库
    private void writeToSqlite() {
        //初始化方法1，使用本身的方法初始化
        /*DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(getApplicationContext(), "lenve.db", null);
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDb());
        DaoSession daoSession = daoMaster.newSession();
        userDao = daoSession.getUserDao();*/


        //初始化方法2，使用DBHelper
        DBHelper devOpenHelper = new DBHelper(this);
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDb());
        DaoSession daoSession = daoMaster.newSession();

        userDao = daoSession.getUserDao();


        //5.数据库升级
//        数据库的升级其实就两个步骤我们来看看：
//        5.1 修改gradle文件
//        首先在module的gradle文件中修改版本号：
        //这里改为最新的版本号
//        schemaVersion 2
//        targetGenDir 'src/main/java'

        //5.2修改实体类


        Toast.makeText(this, "save to Sqlite successfully!", Toast.LENGTH_SHORT).show();
    }

    @OnClick({R.id.btnAdd, R.id.btnDelete, R.id.btnDelete1, R.id.btnDelete2, R.id.btnDelete3, R.id.btnModify, R.id.btnQuery, R.id.btnUpdateDB})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnAdd:
                //1增加数据
                Random random = new Random();
                User user = new User(null, "zhangsan" + random.nextInt(9999), "张三"+random.nextInt(1000));
                userDao.insert(user);
                break;
            case R.id.btnDelete:
                //2.1删除数据1
                List<User> userList = (List<User>) userDao.queryBuilder().where(UserDao.Properties.Id.le(10)).build().list();
                for (User user3 : userList) {
                    userDao.delete(user3);
                }
                break;
            case R.id.btnDelete1:
                //2.2删除数据2
                User user1 = userDao.queryBuilder().where(UserDao.Properties.Id.eq(16)).build().unique();
                if (user1 == null) {
                    Toast.makeText(MainActivity.this, "用户不存在", Toast.LENGTH_SHORT).show();
                } else {
                    userDao.deleteByKey(user1.getId());
                }
                break;
            case R.id.btnDelete2:
                //2.3删除所有数据
                userDao.deleteAll();
                break;
            case R.id.btnDelete3:
                break;
            case R.id.btnModify:
                //3修改数据
                User user2 = userDao.queryBuilder().where(UserDao.Properties.Id.ge(10), UserDao.Properties.Username.like("zhangsan")).build().unique();
                if (user2 == null) {
                    Toast.makeText(MainActivity.this, "用户不存在!", Toast.LENGTH_SHORT).show();
                } else {
                    user2.setUsername("王五");
                    userDao.update(user2);
                }
                break;
            case R.id.btnQuery:
                //4查询数据(2-13条，最多5条数据）
                List<User> list = userDao.queryBuilder().where(UserDao.Properties.Id.between(2, 13)).limit(5).build().list();
                for (int i = 0; i < list.size(); i++) {
                    Log.d("google_hu", "search: " + list.get(i).toString());
                }
                break;
            case R.id.btnUpdateDB:
                break;
        }
    }
}
