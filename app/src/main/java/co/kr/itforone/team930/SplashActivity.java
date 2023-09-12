package co.kr.itforone.team930;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {
    private static final int APP_PERMISSION_STORAGE = 9787;
    private final int APPS_PERMISSION_REQUEST=1000;
    final int SEC=11000;//다음 화면에 넘어가기 전에 머물 수 있는 시간(초)
    public static boolean isStart=true;
    @BindView(R.id.videoView)
    VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // 비디오뷰를 커스텀하기 위해서 미디어컨트롤러 객체 생성

        MediaController mediaController = new MediaController(this);

        // 비디오뷰에 연결

        mediaController.setAnchorView(videoView);

        // 안드로이드 res폴더에 raw폴더를 생성 후 재생할 동영상파일을 넣습니다.

        // 경로에 주의할 것

        // 실제 모바일에서 테스트 할 것

        // 위 두가지를 대충 넘겼다가 많은 시간을 허비했다. ㅜㅜ...

        Uri video = Uri.parse("android.resource://" + getPackageName()+ "/"+R.raw.logo);

/*

외부파일의 경우

Uri video = Uri.parse("http://해당 url/mp4_file_name.mp4") 와 같이 사용한다.

*/



        //비디오뷰의 컨트롤러를 미디어컨트롤로러 사용

        videoView.setMediaController(mediaController);



        //비디오뷰에 재생할 동영상주소를 연결

        videoView.setVideoURI(video);

        //비디오뷰를 포커스하도록 지정

        videoView.requestFocus();

        //동영상 재생

        videoView.start();


        Toast.makeText(this, "뒤로가기 누르시면 스킨이 됩니다.", Toast.LENGTH_SHORT).show();

        //버전별 체크를 한 후 마시멜로 이상이면 퍼미션 체크 여부
        try {
            if (Build.VERSION.SDK_INT >= 23) {
                checkPermission();
            } else {
                goHandler();
            }
        } catch (Exception e) {
        }

    }
    @TargetApi(Build.VERSION_CODES.M)
    public void checkPermission(){
        try {

            //권한이 없는 경우
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED
            )
            {
                //최초 거부를 선택하면 두번째부터 이벤트 발생 & 권한 획득이 필요한 이유를 설명
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(this, "shouldShowRequestPermissionRationale", Toast.LENGTH_SHORT).show();
                }

                //요청 팝업 팝업 선택시 onRequestPermissionsResult 이동
                requestPermissions(new String[]{
                                Manifest.permission.READ_PHONE_STATE,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.CALL_PHONE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        },
                        APP_PERMISSION_STORAGE);

            }
            //권한이 있는 경우
            else {
                goHandler();

                //writeFile();
            }
        }catch(Exception e){
            goHandler();
        }
    }
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case APP_PERMISSION_STORAGE:
                if(grantResults.length>0&&grantResults[0]== PackageManager.PERMISSION_GRANTED) {
                    goHandler();
                }else{

                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.setData(Uri.parse("package:" + getPackageName()));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                    startActivityForResult(intent,APPS_PERMISSION_REQUEST);
                    //startActivity(intent);

                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==APPS_PERMISSION_REQUEST){
            try {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermission();
                } else {
                    goHandler();
                }
            } catch (Exception e) {

            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    //핸들러로 이용해서 3초간 머물고 이동이 됨
    public void goHandler() {
        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isStart=true;
                finish();

            }
        }, SEC);
    }


}
