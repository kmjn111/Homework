package com.example.user.homework;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.homework.data.DBHelper;
import com.example.user.homework.detailfragment.DetailMainActivity;
import com.example.user.homework.detailfragment.DetailMainFragment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity {

    private DBHelper mDbHelper;

    final int REQUEST_EXTERNAL_STORAGE_FOR_MULTIMEDIA=1;
    final int REQUEST_IMAGE_CAPTURE = 1;
    File mPhotoFile =null;
    String mPhotoFileName = null;
    EditText name, addr, phone, optime;
    TextView path;
    ImageView image;
    Button registerBtn;
    double mlati;
    double mlong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //권한체크
        checkDangerousPermissions();

        //DB셋팅
        mDbHelper = new DBHelper(this);

        //버튼 및 컴포넌트들 셋팅
        name = (EditText)findViewById(R.id.name);
        addr = (EditText)findViewById(R.id.addr);
        phone = (EditText)findViewById(R.id.phone);
        path = (TextView)findViewById(R.id.path);
        optime = (EditText)findViewById(R.id.optime);

        //이미지 셋팅및 클릭시
        image = (ImageView)findViewById(R.id.image_view);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // checkDangerousPermissions();
                if(isExternalStorageWritable()){
                    dispatchTakePictureIntent();
                }

            }
        });
        //SearchActivtiy 에서 intent를 받음
        Intent intent = getIntent();
        String msg =intent.getStringExtra("addr");
        mlati = intent.getDoubleExtra("lati",0);
        mlong = intent.getDoubleExtra("long",0);
        addr.setText(msg);

        //등록버튼 셋팅 및 클릭시
        registerBtn = (Button) findViewById(R.id.register_btn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent resultintent = new Intent();
                long num = insertRecord();
                String id = Long.toString(num);
                resultintent.putExtra("resulting",id);
                setResult(11,resultintent);

                /*long id = insertRecord();
                //상세화면으로 이동
                Intent intent = new Intent(getApplicationContext(), DetailMainActivity.class);
                DetailMainFragment.parentId = id+"";
                startActivity(intent);*/
            }
        });
    }



    private void checkDangerousPermissions() {

        String[] permissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        };

        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int i = 0; i < permissions.length; i++) {
            permissionCheck = ContextCompat.checkSelfPermission(this, permissions[i]);
            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                break;
            }
        }

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_EXTERNAL_STORAGE_FOR_MULTIMEDIA);

        }

    }

    //카메라앱 찍기
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            //1. 카메라 앱으로 찍은 이미지를 저장할 파일 객체 생성
            mPhotoFileName = "IMG"+currentDateFormat()+".jpg";
            mPhotoFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), mPhotoFileName);

            if (mPhotoFile !=null) {
                //2. 생성된 파일 객체에 대한 Uri 객체를 얻기
                Uri imageUri = FileProvider.getUriForFile(this, "com.example.user.homework", mPhotoFile);

                //3. Uri 객체를 Extras를 통해 카메라 앱으로 전달
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            } else
                Toast.makeText(getApplicationContext(), "file null", Toast.LENGTH_SHORT).show();
        }
    }

    //일시분초가져오기
    private String currentDateFormat(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
        String  currentTimeStamp = dateFormat.format(new Date());
        return currentTimeStamp;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            if (mPhotoFileName != null) {
                try{

                    mPhotoFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), mPhotoFileName);

                    path.setText(mPhotoFile.getAbsolutePath());

                    Bitmap myBitmap = BitmapFactory.decodeFile(mPhotoFile.getAbsolutePath());
                    //myBitmap.compress(Bitmap.CompressFormat.JPEG,200,new FileOutputStream(mPhotoFile));
                    ImageView myImage = (ImageView) findViewById(R.id.image_view);
                    myImage.setImageBitmap(myBitmap);

                }catch(Exception e){

                }

            } else
                Toast.makeText(getApplicationContext(), "mPhotoFile is null", Toast.LENGTH_SHORT).show();

        }
    }


    /*
        카메라 앱을 통해 이미지를 저장하고 다시 현재 앱으로 돌아오는 경우, 예기치 않게 액티비티가 재시작되는 경우
        기존 상태 (mPhotoFileName)을 저장하는 메소드. 안드로이드 프레임워크에 의해서 자동으로 호출
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("mPhotoFileName",mPhotoFileName);
        super.onSaveInstanceState(outState);
    }


    private long insertRecord() {
        long nOfRows = mDbHelper.insertHomeworkByMethod(
                name.getText().toString()
                ,addr.getText().toString()
                ,phone.getText().toString()
                ,path.getText().toString()
                ,optime.getText().toString()
                ,mlati
                ,mlong
        );
        if (nOfRows >0)
            Toast.makeText(this,nOfRows+" Record Inserted", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this,"No Record Inserted", Toast.LENGTH_SHORT).show();

        return nOfRows;
    }


    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
           // Toast.makeText(this,"가능", Toast.LENGTH_SHORT).show();
            return true;
        }
        Toast.makeText(this,"외부저장소 마운트 안됨", Toast.LENGTH_SHORT).show();
        return false;
    }
}
