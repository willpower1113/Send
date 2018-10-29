package com.willpower.send.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by Administrator on 2018/10/29.
 */

public class PagerFragment extends Fragment {

    public static PagerFragment newInstance(int index) {
        Bundle args = new Bundle();
        args.putInt("index", index);
        PagerFragment fragment = new PagerFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
