package com.guillaume.project9;


import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import com.guillaume.project9.ui.MainActivity;
import com.guillaume.project9.utils.Utils;


@RunWith(AndroidJUnit4.class)
public class UtilsInstrumentedTest {

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class);


    @Test
    public void network_is_available(){
        MainActivity activity = rule.getActivity();

        //boolean internetIsOk = Utils.isInternetAvailable(activity);

        assertTrue(Utils.isInternetAvailable(activity));
    }
}
