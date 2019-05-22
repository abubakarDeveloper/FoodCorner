package ab_developer.com.foodcorner;

import android.content.Intent;
import android.graphics.Path;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.circulardialog.CDialog;
import com.example.circulardialog.extras.CDConstants;

import me.ibrahimsn.particle.ParticleView;
import yanzhikai.textpath.AsyncTextPathView;
import yanzhikai.textpath.painter.AsyncPathPainter;

public class SplashActivity extends AppCompatActivity {

    ImageView ivLogoImage;
    TextView tvAppName;
    //  private static int SPLASH_TIME_OUT = 4000;
    AsyncTextPathView astpv;
    ParticleView particleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        particleView = findViewById(R.id.particleView);
        ivLogoImage = findViewById(R.id.iv_logo_image);
        //tvAppName = findViewById(R.id.tv_app_name);
        astpv = findViewById(R.id.atpv_1);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.anim_move_top);
        ivLogoImage.setAnimation(animation);
/*
       Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),
               R.anim.transition_splash);
       tvAppName.setAnimation(animation1);
*/
       /*
        new Handler().postDelayed(new Runnable(){
            @Override
                    public void run(){

                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_TIME_OUT);
*/
        astpv.setPathPainter(new AsyncPathPainter() {
            @Override
            public void onDrawPaintPath(float x, float y, Path paintPath) {
                paintPath.addCircle(x, y, 6, Path.Direction.CCW);
            }
        });
        astpv.startAnimation(0, 1);
     if(internetIsConnected() == true) {
         final Intent intent = new Intent(SplashActivity.this, IntroActivity.class);

         final Thread timer = new Thread() {
             public void run() {
                 try {
                     sleep(9000);
                 } catch (InterruptedException ie) {
                     ie.printStackTrace();
                 } finally {
                     startActivity(intent);
                     finish();

                 }
             }
         };
         timer.start();
     }else{
/*         MaterialStyledDialog dialog = new MaterialStyledDialog.Builder(this)
                 .setTitle("Error!")
                 .setDescription("Check Your Internet Connection")
                 .setCancelable(false)
                 .setHeaderDrawable(R.drawable.img1)
                 .setPositiveText("Ok")
                 .onPositive(new MaterialDialog.SingleButtonCallback() {
                     @Override
                     public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                         dialog.dismiss();
                     }
                 })
                 .build();
         dialog.show();*/

         new CDialog(SplashActivity.this).createAlert("Check Your Internet Connection!",
                 CDConstants.ERROR,   // Type of dialog
                 CDConstants.LARGE)    //  size of dialog
                 .setAnimation(CDConstants.SCALE_FROM_BOTTOM_TO_TOP)     //  Animation for enter/exit
                 .setDuration(3000)   // in milliseconds
                 .setTextSize(CDConstants.LARGE_TEXT_SIZE)  // CDConstants.LARGE_TEXT_SIZE, CDConstants.NORMAL_TEXT_SIZE
                 .show();
         astpv.startAnimation(0, 1);
     }

    }

    public boolean internetIsConnected() {
        try {
            String command = "ping -c 1 google.com";
            return (Runtime.getRuntime().exec(command).waitFor() == 0);
        } catch (Exception e) {
            return false;
        }
    }
}
