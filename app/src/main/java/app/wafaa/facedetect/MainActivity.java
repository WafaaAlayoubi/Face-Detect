package app.wafaa.facedetect;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

public class MainActivity extends AppCompatActivity {

    private Button btn;
    private ImageView myImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = findViewById(R.id.button);
        myImageView = findViewById(R.id.imgView);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inMutable = true;
                Bitmap myBitmap = BitmapFactory.decodeResource(
                        getApplicationContext().getResources(),
                        R.drawable.test1,
                        options
                );

                Paint myRectPaint = new Paint();
                myRectPaint.setStrokeWidth(5);
                myRectPaint.setColor(Color.RED);
                myRectPaint.setStyle(Paint.Style.STROKE);

                Bitmap tempBitmap = Bitmap.createBitmap(myBitmap.getWidth(), myBitmap.getHeight(),
                        Bitmap.Config.RGB_565);
                Canvas tempCanvas = new Canvas(tempBitmap);
                tempCanvas.drawBitmap(myBitmap, 0, 0, null);

                FaceDetector faceDetector = new
                        FaceDetector.Builder(getApplicationContext()).setTrackingEnabled(false)
                        .build();

                if(!faceDetector.isOperational()) {
                    new AlertDialog.Builder(v.getContext()).setMessage("Could not set up the face detected!").show();
                    return;
                }

                Frame frame = new Frame.Builder().setBitmap(myBitmap).build();
                SparseArray<Face> faces = faceDetector.detect(frame);

                for (int i = 0; i < faces.size(); i++) {
                    Face thisFace = faces.valueAt(i);
                    float x1 = thisFace.getPosition().x;
                    float y1 = thisFace.getPosition().y;
                    float x2 = x1 + thisFace.getWidth();
                    float y2 = y1 + thisFace.getHeight();
                    tempCanvas.drawRoundRect(new RectF(x1, y1, x2, y2), 2, 2, myRectPaint);

                }
                myImageView.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
            }
        });
    }
}
