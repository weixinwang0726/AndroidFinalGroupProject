package com.example.androidfinalgroupproject.audio;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.androidfinalgroupproject.MainActivity;
import com.example.androidfinalgroupproject.R;
import com.example.androidfinalgroupproject.audio.common.AlbumDataSource;
import com.example.androidfinalgroupproject.audio.fragment.FavoriteListPage;
import com.example.androidfinalgroupproject.audio.fragment.SearchMainPage;
import com.google.android.material.navigation.NavigationView;

/**
 * 只使用一个activity，多个fragment
 */
public class AudioMainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private Toolbar mToolbar;
    private NavigationView mNavigationView;

    private AlbumDataSource mDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_main);
        SearchMainPage page = new SearchMainPage();

        // 初始化各个widget
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        setSupportActionBar(mToolbar);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        mActionBarDrawerToggle.syncState();

        // 初始化数据库
        mDataSource = new AlbumDataSource(this);
        mDataSource.open();

        // 设置NavigationDrawer的点击响应事件
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    // 根据menu的id设置对应的响应事件
                    case R.id.back_to_main:
                        startActivity(new Intent(AudioMainActivity.this, MainActivity.class));
                        break;
                    case R.id.nav_search:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.main_content, page)
                                .addToBackStack(null)
                                .commit();
                        break;
                    case R.id.nav_favorite:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.main_content, new FavoriteListPage())
                                .addToBackStack(null)
                                .commit();
                        break;
                }

                return true;
            }
        });

        // 加载搜索界面
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_content, page)
                .addToBackStack(null)
                .commit();
    }

    // 使其他fragment能访问数据库
    public AlbumDataSource getDataSource() {
        return mDataSource;
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }
}