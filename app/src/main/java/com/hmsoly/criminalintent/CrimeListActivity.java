package com.hmsoly.criminalintent;

import android.support.v4.app.Fragment;

/**
 * Created by HarMachine on 3/25/2015.
 */
public class CrimeListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
