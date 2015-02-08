package com.github.vaclavpalik.pewpewpew;


import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.github.vaclavpalik.pewpewpew.fragments.AboutFragment;
import com.github.vaclavpalik.pewpewpew.fragments.GameFragment;
import com.github.vaclavpalik.pewpewpew.fragments.MenuFragment;
import com.github.vaclavpalik.pewpewpew.fragments.UpgradeFragment;
import com.github.vaclavpalik.pewpewpew.model.Game;
import com.github.vaclavpalik.pewpewpew.model.Player;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;


public class MainActivity extends ActionBarActivity implements MenuFragment.OnFragmentInteractionListener, GameFragment.OnFragmentInteractionListener, UpgradeFragment.OnFragmentInteractionListener, AboutFragment.OnFragmentInteractionListener {

    private static MainActivity instance;

    private MenuFragment menuFragment;
    private volatile GameFragment gameFragment;
    private volatile UpgradeFragment upgradeFragment;
    private volatile AboutFragment aboutFragment;
    private volatile Fragment activeFragment = getGameFragment();
    private Lock fragmentLock = new ReentrantLock();

    public MainActivity() {
        instance = this;
    }

    public static MainActivity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            menuFragment = new MenuFragment();
            getFragmentManager().beginTransaction()
                    .add(R.id.container, menuFragment).add(R.id.fragmentHolder, getGameFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public MenuFragment getMenuFragment() {
        return menuFragment;
    }

    public GameFragment getGameFragment() {
        if (gameFragment == null)
            synchronized (this) {
                if (gameFragment == null)
                    gameFragment = new GameFragment();
            }
        return gameFragment;
    }

    public UpgradeFragment getUpgradeFragment() {
        if (upgradeFragment == null)
            synchronized (this) {
                if (upgradeFragment == null)
                    upgradeFragment = new UpgradeFragment();
            }
        return upgradeFragment;
    }

    public AboutFragment getAboutFragment() {
        if (aboutFragment == null)
            synchronized (this) {
                if (aboutFragment == null)
                    aboutFragment = new AboutFragment();
            }
        return aboutFragment;
    }

    @Override
    public void onFragmentInteraction(String id) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onPause() {
        fragmentLock.lock();
        try {
            if (Game.isStarted())
                Game.getInstance().haltSpawn();
        } finally {
            super.onPause();
            fragmentLock.unlock();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        fragmentLock.lock();
        try {
            getFragmentManager().beginTransaction().replace(R.id.fragmentHolder, getActiveFragment()).commit();
            if (Game.isStarted()) {
                Game.getInstance().resumeSpawn();
            }
        } finally {
            fragmentLock.unlock();
        }

    }

    public Fragment getActiveFragment() {
        return activeFragment;
    }

    public void setActiveFragment(Fragment newFragment) {
        if (newFragment != activeFragment) {
            synchronized (this) {
                if (newFragment != activeFragment) {
                    fragmentLock.lock();
                    try {
                        getFragmentManager().beginTransaction().replace(R.id.fragmentHolder, newFragment).commit();
                        activeFragment = newFragment;
                    } finally {
                        fragmentLock.unlock();
                    }
                }
            }
        }
    }

    public Lock getFragmentLock() {
        return fragmentLock;
    }
}
