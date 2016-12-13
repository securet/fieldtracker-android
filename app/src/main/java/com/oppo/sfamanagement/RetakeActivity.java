package com.oppo.sfamanagement;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by allsmartlt218 on 13-12-2016.
 */

public class RetakeActivity extends AppCompatActivity {
    Bitmap bmp;
    Button confirm,cancel;
    ImageView imageView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_retake_request);
        confirm = (Button) findViewById(R.id.btConfirm);
        cancel = (Button) findViewById(R.id.btRetake);
        imageView = (ImageView) findViewById(R.id.ivRetake);
        final String imagePath = getIntent().getStringExtra("image_taken");
        bmp = rotateBmp(BitmapFactory.decodeFile(imagePath));
        imageView.setImageBitmap(bmp);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.putExtra("response",imagePath);
                setResult(1,i);
                finish();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.putExtra("response","Cancel");
                setResult(1,i);
                finish();
            }
        });
    }
    public Bitmap rotateBmp(Bitmap bmp){
        Matrix matrix = new Matrix();
        //set image rotation value to 90 degrees in matrix.
        matrix.postRotate(270);
        //supply the original width and height, if you don't want to change the height and width of bitmap.
        bmp = Bitmap.createBitmap(bmp, 0, 0, 1080, 1080, matrix, true);
        return bmp;
    }

}
