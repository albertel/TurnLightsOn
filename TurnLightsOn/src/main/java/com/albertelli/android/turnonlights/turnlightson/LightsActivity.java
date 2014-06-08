package com.albertelli.android.turnonlights.turnlightson;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class LightsActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new LightsFragment();
    }
}
