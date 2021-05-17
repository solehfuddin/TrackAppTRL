package com.sofudev.trackapptrl;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.nj.imagepicker.ImagePicker;
import com.nj.imagepicker.listener.ImageMultiResultListener;
import com.nj.imagepicker.result.ImageResult;
import com.nj.imagepicker.utils.DialogConfiguration;

import java.util.ArrayList;

import static com.github.mikephil.charting.charts.Chart.LOG_TAG;

public class TestCamActivity extends AppCompatActivity {

    private static final int REQUEST_TAKE_PHOTO = 1;

    Button btnTakePhoto;
    ImageView ivPreview;

    String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_cam);

        btnTakePhoto =  findViewById(R.id.btnTakePhoto);
        ivPreview    =  findViewById(R.id.ivPreview);

        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.build(new DialogConfiguration()
                        .setTitle("Choose")
                        .setOptionOrientation(LinearLayoutCompat.HORIZONTAL), new ImageMultiResultListener() {
                    @Override
                    public void onImageResult(ArrayList<ImageResult> imageResult) {
                        Log.e(LOG_TAG, "onImageResult:Number of image picked " + imageResult.size());

                        if (imageResult.size() > 0) {
                            ivPreview.setImageBitmap(imageResult.get(0).getBitmap());
                        }
                    }
                }).show(getSupportFragmentManager());
            }
        });
    }


}
