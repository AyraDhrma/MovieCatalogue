package com.ayra.moviecatalogue.ui.view.main;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.ayra.moviecatalogue.R;
import com.ayra.moviecatalogue.ui.view.favorite.MyFavoriteFragment;
import com.ayra.moviecatalogue.ui.view.movie.MovieFragment;
import com.ayra.moviecatalogue.ui.view.notificationsetting.NotificationSettingActivity;
import com.ayra.moviecatalogue.ui.view.search.SearchFragment;
import com.ayra.moviecatalogue.ui.view.tvshow.TvShowFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {

    private BottomNavigationView bottomNavigationView;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        // Set Bottom Navigation
        bottomNavigationView = findViewById(R.id.bottom_navigation_main);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        // Setup HomeFragment
        if (savedInstanceState == null) {
            loadFragment(new MovieFragment());
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu, menu);
        searchView = (SearchView) (menu.findItem(R.id.action_search)).getActionView();
        // Set SearchView
        setSearchView();

        return super.onCreateOptionsMenu(menu);
    }

    private void setSearchView() {
        searchView.setQueryHint(getResources().getString(R.string.search_hint));
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.action_language) {
            Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(intent);
        } else if (item.getItemId() == R.id.action_notification) {
            Intent intent = new Intent(MainActivity.this, NotificationSettingActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment;
        switch (menuItem.getItemId()) {
            case R.id.movie_menu:
            case R.id.search_menu:
                fragment = new MovieFragment();
                loadFragment(fragment);
                return true;
            case R.id.tv_menu:
                fragment = new TvShowFragment();
                loadFragment(fragment);
                return true;
            case R.id.favorite_menu:
                fragment = new MyFavoriteFragment();
                loadFragment(fragment);
                return true;
        }
        return false;
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, fragment)
                .commit();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (query.length() > 0) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            Bundle bundle = new Bundle();
            bundle.putString("search", query);
            SearchFragment searchFragment = new SearchFragment();
            searchFragment.setArguments(bundle);
            transaction.replace(R.id.main_container, searchFragment);
            transaction.commit();
            bottomNavigationView.getMenu().getItem(2).setChecked(true);
        } else {
            bottomNavigationView.getMenu().getItem(2).setChecked(true);
            loadFragment(new MovieFragment());
        }
        searchView.clearFocus();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.length() > 0) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            Bundle bundle = new Bundle();
            bundle.putString("search", newText);
            SearchFragment searchFragment = new SearchFragment();
            searchFragment.setArguments(bundle);
            transaction.replace(R.id.main_container, searchFragment);
            transaction.commit();
            bottomNavigationView.getMenu().getItem(2).setChecked(true);
        } else {
            bottomNavigationView.getMenu().getItem(2).setChecked(true);
            loadFragment(new MovieFragment());
        }
        return false;
    }
}
