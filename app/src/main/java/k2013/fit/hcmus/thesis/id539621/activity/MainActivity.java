package k2013.fit.hcmus.thesis.id539621.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import k2013.fit.hcmus.thesis.id539621.R;
import k2013.fit.hcmus.thesis.id539621.game_operation.GamePlayParams;

public class MainActivity extends BaseActivity {

    private final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1231;
    private final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1232;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView title = (TextView) findViewById(R.id.main_title);
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/pacifico-regular.ttf");
        title.setTypeface(tf);

        loadGameSetting();

    }


    private void loadGameSetting()  {
        SharedPreferences sharedPreferences= this.getSharedPreferences("gameSetting", Context.MODE_PRIVATE);
        if(sharedPreferences != null)  {
            int gameMode = sharedPreferences.getInt("gameMode", -1);
            if(gameMode == -1) {
                SharedPreferences.Editor editor = sharedPreferences.edit();

                SensorManager mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
                if (mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE) != null) {
                    gameMode = GamePlayParams.MODE_SENSOR;
                } else {
                    gameMode = GamePlayParams.MODE_TOUCH;
                }
                editor.apply();
            }
            boolean hasData = sharedPreferences.getBoolean("gameData", false);
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            } else {
                if(hasData == false){
                    final int[] mDatas = new int[] { R.raw.pcm };
                    for (int i = 0; i < mDatas.length; i++) {
                        try {
                            String path = Environment.getExternalStorageDirectory() + "/FindItData";
                            File dir = new File(path);
                            if (dir.mkdirs() || dir.isDirectory()) {
                                String str_song_name = i + ".wav";
                                CopyRAWtoSDCard(mDatas[i], path + File.separator + str_song_name);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                hasData = true;
            }

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("gameMode", gameMode);
            editor.putBoolean("gameData", hasData);
            editor.apply();
        }

    }

    public void mainOnClick(View v) {
        switch (v.getId()) {
            case R.id.main_btn_play: {

                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                } else {

                    Intent intent = new Intent(this, GameSelectionActivity.class);
                    startActivity(intent);
                }
                break;
            }

            case R.id.main_btn_help: {

                break;
            }

            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Intent intent = new Intent(this, GamePlayActivity.class);
                    startActivity(intent);

                } else {

                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    final int[] mDatas = new int[] { R.raw.pcm };
                    for (int i = 0; i < mDatas.length; i++) {
                        try {
                            String path = Environment.getExternalStorageDirectory() + "/FindItData";
                            File dir = new File(path);
                            if (dir.mkdirs() || dir.isDirectory()) {
                                String str_song_name = i + ".wav";
                                CopyRAWtoSDCard(mDatas[i], path + File.separator + str_song_name);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    SharedPreferences sharedPreferences= this.getSharedPreferences("gameSetting", Context.MODE_PRIVATE);

                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putBoolean("gameData", true);
                    editor.apply();

                } else {

                }
                return;
            }
        }
    }

    private void CopyRAWtoSDCard(int id, String path) throws IOException {
        InputStream in = getResources().openRawResource(id);
        FileOutputStream out = new FileOutputStream(path);
        byte[] buff = new byte[1024];
        int read = 0;
        try {
            while ((read = in.read(buff)) > 0) {
                out.write(buff, 0, read);
            }
        } finally {
            in.close();
            out.close();
        }
    }
}