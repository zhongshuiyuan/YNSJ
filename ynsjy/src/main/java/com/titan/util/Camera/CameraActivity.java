package com.titan.util.Camera;

import android.app.Activity;
import android.os.Bundle;

import com.titan.ynsjy.R;

/**
 * Created by whs on 2017/9/18
 * 自定义相机
 */

public class CameraActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        if (null == savedInstanceState) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, CameraFragment.newInstance())
                    .commit();
        }
    }

}
