package haui.android.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

import haui.android.App;
import haui.android.DatabaseHelper;
import haui.android.R;
import haui.android.dialogs.NoticeDialog;
import haui.android.fragments.HomeFragment;
import haui.android.manager.MyBroadcastReceiver;

public class MainActivity extends AppCompatActivity {
    AlarmManager alarmManager;

    static {
        System.setProperty(
                "org.apache.poi.javax.xml.stream.XMLInputFactory",
                "com.fasterxml.aalto.stax.InputFactoryImpl"
        );
        System.setProperty(
                "org.apache.poi.javax.xml.stream.XMLOutputFactory",
                "com.fasterxml.aalto.stax.OutputFactoryImpl"
        );
        System.setProperty(
                "org.apache.poi.javax.xml.stream.XMLEventFactory",
                "com.fasterxml.aalto.stax.EventFactoryImpl"
        );
    }

    private static String fileType = "";
    private static String extensionXLS = "XLS";
    private static String extensionXLXS = "XLXS";
    ActivityResultLauncher<Intent> filePicker;
    private static final int PERMISSION_REQUEST_MEMORY_ACCESS = 0;
    private static final int REQUEST_CAMERA_PERMISSION_RESULT = 1;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileType = extensionXLXS;
                OpenFilePicker();
//                startAlert();
            }
        });
//        DatabaseHelper helper = new DatabaseHelper(MainActivity.this);
//        helper.insertData();
//        try {
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        filePicker = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {

                        Intent intent1 = result.getData();

                        Uri uri = intent1.getData();
                        ReadExcelFile(MainActivity.this, uri);
                    }
                });
    }

//    public void startAlert(){
//        Log.d("phuc", "startAlert: ");
//        long time;
//            Calendar calendar = Calendar.getInstance();
//
//            // calendar is called to get current time in hour and minute
//            calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getCurrentHour());
//            calendar.set(Calendar.MINUTE, alarmTimePicker.getCurrentMinute());
//
//            // using intent i have class AlarmReceiver class which inherits
//            // BroadcastReceiver
//            Intent intent = new Intent(this, MyBroadcastReceiver.class);
//
//            // we call broadcast using pendingIntent
//            pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
//
//            time = (calendar.getTimeInMillis() - (calendar.getTimeInMillis() % 60000));
//            if (System.currentTimeMillis() > time) {
//                // setting time as AM and PM
//                if (Calendar.AM_PM == 0)
//                    time = time + (1000 * 60 * 60 * 12);
//                else
//                    time = time + (1000 * 60 * 60 * 24);
//            }
//            // Alarm rings continuously until toggle button is turned off
//            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, 10000, pendingIntent);
//            // alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (time * 1000), pendingIntent);
//        Log.d("phuc", "startAlert: adasdad");
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_MEMORY_ACCESS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                OpenFilePicker();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private void requestStoragePermission() {

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.MANAGE_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.FOREGROUND_SERVICE,
                            Manifest.permission.RECORD_AUDIO,
                    },
                    PERMISSION_REQUEST_MEMORY_ACCESS);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_MEMORY_ACCESS);
        }
    }

    private void requestRecordPermission() {
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.RECORD_AUDIO},
                PERMISSION_REQUEST_MEMORY_ACCESS);
    }

    private boolean CheckPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestStoragePermission();
            return false;
        }

        return true;
    }

    public void OpenFilePicker() {
        try {
            if (CheckPermission()) {
                ChooseFile();
            }
        } catch (ActivityNotFoundException e) {

        }
    }

    public void ChooseFile() {
        try {
            Intent fileIntent = new Intent(Intent.ACTION_GET_CONTENT);
            fileIntent.addCategory(Intent.CATEGORY_OPENABLE);

            if (fileType == extensionXLS)
                fileIntent.setType("application/vnd.ms-excel");
            else
                fileIntent.setType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

            filePicker.launch(fileIntent);
        } catch (Exception ex) {


        }
    }

    public void ReadExcelFile(Context context, Uri uri) {
        try {
            InputStream inStream;
            Workbook wb = null;

            try {
                inStream = context.getContentResolver().openInputStream(uri);

                if (fileType == extensionXLS)
                    wb = new HSSFWorkbook(inStream);
                else
                    wb = new XSSFWorkbook(inStream);

                inStream.close();
            } catch (IOException e) {
                Log.d("phuc", "ReadExcelFile: error " + e.toString());
                e.printStackTrace();
            }

            DatabaseHelper dbAdapter = new DatabaseHelper(MainActivity.this);
            Sheet sheet1 = wb.getSheetAt(0);
            Log.d("phuc", "ReadExcelFile");
            dbAdapter.insertExcelQuestionToSqlite(sheet1);

            dbAdapter.close();

        } catch (Exception ex) {
            Log.d("phuc", "ReadExcelFile: loi " + ex.toString());
        }
    }

    private void initComponents() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.bg_circle_rotate);
        animation.setDuration(3000);
        findViewById(R.id.load).startAnimation(animation);
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                .replace(R.id.frame_main, new HomeFragment())
                .commit();
    }

    @Override
    public void onBackPressed() {
        final NoticeDialog noticeDialog = new NoticeDialog(this);
        noticeDialog.setCancelable(true);
        noticeDialog.setNotification("Bạn muốn thoát trò chơi ?", "Đồng ý", "Hủy", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.btn_ready) {
                    App.getMusicPlayer().stopBgMusic();
                    finish();
                }
                noticeDialog.dismiss();
            }
        });
        noticeDialog.show();
    }
}
