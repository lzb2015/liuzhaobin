package com.liuzhaobin.example;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.liuzhaobin.example.zxing.activity.CaptureActivity;
import com.liuzhaobin.example.zxing.decoding.RGBLuminanceSource;
import com.liuzhaobin.example.zxing.util.BitmapUtil;
import com.liuzhaobin.example.zxing.util.Constant;
import com.liuzhaobin.example.zxing.util.QRCodeUtil;

import java.util.Hashtable;

public class UserLoginActivity extends AppCompatActivity {

    Button button;
    Button creat_imag;
    Button cbitmap;
    ImageView imag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        //动态权限申请
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        }
        button = findViewById(R.id.but_id);
        creat_imag = findViewById(R.id.creatbitmap_id);
        cbitmap = findViewById(R.id.clbitmap_id);
        imag = findViewById(R.id.imag_id);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 二维码扫码
                Intent intent = new Intent(UserLoginActivity.this, CaptureActivity.class);
                startActivityForResult(intent, Constant.REQ_QR_CODE);

            }
        });



        //生成二维码
        final Bitmap[] bitmap = {null};
        creat_imag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
         //       String path = Environment.getExternalStorageState() + "/" + "erweima";
                bitmap[0] = QRCodeUtil.createQRImage("333333" ,200 , 200 , null ,null );
                imag.setImageBitmap(bitmap[0]);
            }
        });

        //解析现有的bitmap
        cbitmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hashtable<DecodeHintType, String> hints = new Hashtable<>();
                hints.put(DecodeHintType.CHARACTER_SET, "UTF8"); //设置二维码内容的编码
                //rgb颜色计算
                RGBLuminanceSource source = new RGBLuminanceSource(bitmap[0]);
                BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
                QRCodeReader reader = new QRCodeReader();
                try {
                    Log.e("tag" ,"" + reader.decode(bitmap1).getText());
                } catch (NotFoundException e) {
                    e.printStackTrace();
                } catch (ChecksumException e) {
                    e.printStackTrace();
                } catch (FormatException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.e("tag" ,"申请权限");
        switch (requestCode) {

            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //扫码
                    Toast.makeText(this, "申请权限成功！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "你拒绝了权限申请，无法打开相机扫码哟！", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //扫描结果回调
        if (requestCode == Constant.REQ_QR_CODE && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString(Constant.INTENT_EXTRA_KEY_QR_SCAN);
            //将扫描出的信息显示出来
            Log.e("tag" ,"扫码结果= " + scanResult );
        }
    }



}
