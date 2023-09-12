package co.kr.itforone.team930;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Message;
import android.os.Parcelable;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieSyncManager;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.w3c.dom.Text;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URISyntaxException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import util.BackPressCloseHandler;
import util.Common;

public class IndexActivity extends AppCompatActivity {
    @BindView(R.id.topTitleTxt)
    TextView topTitleTxt;
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.btmMenuImg1)
    ImageView btmMenuImg1;
    @BindView(R.id.btmMenuImg2)
    ImageView btmMenuImg2;
    @BindView(R.id.btmMenuImg3)
    ImageView btmMenuImg3;
    @BindView(R.id.btmMenuImg4)
    ImageView btmMenuImg4;
    @BindView(R.id.btmMenuImg5)
    ImageView btmMenuImg5;
    @BindView(R.id.btmText1)
    TextView btmText1;
    @BindView(R.id.btmText2)
    TextView btmText2;
    @BindView(R.id.btmText3)
    TextView btmText3;
    @BindView(R.id.btmText4)
    TextView btmText4;
    @BindView(R.id.btmText5)
    TextView btmText5;
    @BindView(R.id.btmMenuLayout1)
    LinearLayout btmMenuLayout1;
    @BindView(R.id.btmMenuLayout2)
    LinearLayout btmMenuLayout2;
    @BindView(R.id.btmMenuLayout3)
    LinearLayout btmMenuLayout3;
    @BindView(R.id.btmMenuLayout4)
    LinearLayout btmMenuLayout4;
    @BindView(R.id.btmMenuLayout5)
    LinearLayout btmMenuLayout5;
    @BindView(R.id.backImg)
    ImageView backImg;
    @BindView(R.id.rightBtnImg)
    ImageView rightBtnImg;





    public static boolean execBoolean = true;

    boolean isIndex = true;
    private final int AUDIO_RECORED_REQ_CODE=1500,SNS_REQ_CODE=2000;
    String firstUrl = "";
    final int REQUEST_IMAGE_CODE = 1010;
    Context mContext;
    Activity mActivity;

    private Uri cameraImageUri;
    public static String no;
    int gpsCount=0;

    private BackPressCloseHandler backPressCloseHandler;

    private WebView mWebviewPop;


    final int FILECHOOSER_NORMAL_REQ_CODE = 1200,FILECHOOSER_LOLLIPOP_REQ_CODE=1300;
    private final int REQUEST_VIEWER=1000;
    ValueCallback<Uri> filePathCallbackNormal;
    ValueCallback<Uri[]> filePathCallbackLollipop;
    Uri mCapturedImageURI;
    boolean nextForwardBoolean=true;


    String[] PERMISSIONS = {
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.ACCESS_COARSE_LOCATION",
            "android.permission.READ_PHONE_STATE"
    };

    static final int PERMISSION_REQUEST_CODE = 1;

    int urlString[] = {R.string.login_url,
                        R.string.mypage_url,
                        R.string.as_url,
                        R.string.second_hand_url,
                        R.string.notice_url,
                        R.string.game_url,
                        R.string.register_url,
                        R.string.provision_url,
                        R.string.privacy_url

                        };
    String titleArray[] = {"로그인","마이페이지","A/S접수","중고직거래","공지사항","회원가입","출시게임","이용약관","개인정보처리방침"};
            //권한 설정이 되어있는 알아보는 메서드
            private boolean hasPermissions(String[] permissions) {
                // 퍼미션 확인
                int result = -1;
                for (int i = 0; i < permissions.length; i++) {
                    result = ContextCompat.checkSelfPermission(getApplicationContext(), permissions[i]);
        }
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;

        } else {
            return false;
        }
    }

    //권한 팝업창 띄워주는 메서드
    private void requestNecessaryPermissions(String[] permissions) {
        ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
    }
    //허용된 권한이 값을 받을 때 메서드
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE: {
                //퍼미션을 거절했을 때 메시지 출력 후 종료
                if (!hasPermissions(PERMISSIONS)) {

                }else{
                   /* LocationPosition.act=MainActivity.this;
                    LocationPosition.setPosition(this);
                    if(LocationPosition.lng==0.0){
                        LocationPosition.setPosition(this);
                    }
                    String place= LocationPosition.getAddress(LocationPosition.lat,LocationPosition.lng);
                    webView.loadUrl("javascript:getAddress('"+place+"')");*/
                }
                return;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        ButterKnife.bind(this);
        Intent intent=getIntent();
        topTitleTxt.setText(intent.getExtras().getString("title"));

        if (Build.VERSION.SDK_INT >= 24) {
            try {
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }



        int no = intent.getExtras().getInt("no");
        String title = intent.getExtras().getString("title");
        topTitleTxt.setText(title);
        try{
            if(intent.getExtras().getString("goUrl").equals("")) {
                firstUrl = getString(urlString[no]);
            }else {
                firstUrl = intent.getExtras().getString("goUrl");
            }
        }catch (Exception e){
            firstUrl = getString(urlString[no]);
        }

        Log.d("firstUrl",firstUrl);
        if(0<no&&no<5){
            int no2=no+1;
            int imageViewId=getResources().getIdentifier("btmMenuImg"+no2,"id",getPackageName());
            int textViewId=getResources().getIdentifier("btmText"+no2,"id",getPackageName());
            int layoutId=getResources().getIdentifier("btmMenuLayout"+no2,"id",getPackageName());
            ((ImageView)findViewById(imageViewId)).setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_IN);
            ((TextView)findViewById(textViewId)).setTextColor(Color.parseColor("#ffffff"));
            ((LinearLayout)findViewById(layoutId)).setBackgroundColor(Color.parseColor("#232b74"));
        }

        /* cookie */
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.createInstance(this);
        }

        webView.loadUrl(firstUrl);
        webViewSetting();

        //토큰 값 생성
        FirebaseApp.initializeApp(this);//firebase 등록함
        FirebaseMessaging.getInstance().subscribeToTopic("Team930");
        //토큰 생성
        Common.TOKEN= FirebaseInstanceId.getInstance().getToken();
        try {
            if (Common.TOKEN.equals("") || Common.TOKEN.equals(null)) {
                //토큰 값 재생성
                refreshToken();
            } else {

            }
        }catch (Exception e){
            //토큰 값 재생성
            refreshToken();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.ECLAIR_MR1)
    public void webViewSetting() {
        WebSettings setting = webView.getSettings();//웹뷰 세팅용
        setting.setAllowFileAccess(true);//웹에서 파일 접근 여부

        setting.setGeolocationEnabled(true);//위치 정보 사용여부
        setting.setDatabaseEnabled(true);//HTML5에서 db 사용여부
        setting.setDomStorageEnabled(true);//HTML5에서 DOM 사용여부
        setting.setCacheMode(WebSettings.LOAD_NO_CACHE);//캐시 사용모드 LOAD_NO_CACHE는 캐시를 사용않는다는 뜻
        setting.setJavaScriptEnabled(true);//자바스크립트 사용여부
        setting.setSupportMultipleWindows(false);//윈도우 창 여러개를 사용할 것인지의 여부 무조건 false로 하는 게 좋음
        setting.setUseWideViewPort(true);//웹에서 view port 사용여부
        setting.setSupportZoom(false);
        setting.setBuiltInZoomControls(false);
        webView.setWebChromeClient(chrome);//웹에서 경고창이나 또는 컴펌창을 띄우기 위한 메서드
        webView.setWebViewClient(client);//웹페이지 관련된 메서드 페이지 이동할 때 또는 페이지가 로딩이 끝날 때 주로 쓰임
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            setting.setTextZoom(100);
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setting.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW); // 혼합된 컨텐츠 허용//
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            // older android version, disable hardware acceleration
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
            //기기에 따라서 동작할수도있는걸 확인
            setting.setRenderPriority(WebSettings.RenderPriority.HIGH);

            //최신 SDK 에서는 Deprecated 이나 아직 성능상에서는 유용하다
            setting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

            //부드러운 전환 또한 아직 동작
            setting.setEnableSmoothTransition(true);
        }
        setting.setUserAgentString("Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Mobile Safari/537.36"+"/APP");
        webView.addJavascriptInterface(new IndexActivity.WebJavascriptEvent(), "Android");
        //뒤로가기 버튼을 눌렀을 때 클래스로 제어함
        backPressCloseHandler = new BackPressCloseHandler(this);



    }

    WebChromeClient chrome;
    {
        chrome = new WebChromeClient() {
            //새창 띄우기 여부
            @Override
            public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {


                return true;


            }
            @Override
            public void onCloseWindow(WebView window) {

            }
            //경고창 띄우기
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                new AlertDialog.Builder(IndexActivity.this)
                        .setMessage("\n" + message + "\n")
                        .setCancelable(false)
                        .setPositiveButton("확인",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        result.confirm();
                                    }
                                }).create().show();
                return true;
            }

            //컴펌 띄우기
            @Override
            public boolean onJsConfirm(WebView view, String url, String message,
                                       final JsResult result) {
                new AlertDialog.Builder(IndexActivity.this)
                        .setMessage("\n" + message + "\n")
                        .setCancelable(false)
                        .setPositiveButton("확인",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        result.confirm();
                                    }
                                })
                        .setNegativeButton("취소",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        result.cancel();
                                    }
                                }).create().show();
                return true;
            }

            //현재 위치 정보 사용여부 묻기
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                // Should implement this function.
                final String myOrigin = origin;
                final GeolocationPermissions.Callback myCallback = callback;
                AlertDialog.Builder builder = new AlertDialog.Builder(IndexActivity.this);
                builder.setTitle("Request message");
                builder.setMessage("Allow current location?");
                builder.setPositiveButton("Allow", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        myCallback.invoke(myOrigin, true, false);
                    }

                });
                builder.setNegativeButton("Decline", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        myCallback.invoke(myOrigin, false, false);
                    }

                });
                AlertDialog alert = builder.create();
                alert.show();
            }
            // For Android < 3.0
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                openFileChooser(uploadMsg, "");
            }

            // For Android 3.0+
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                filePathCallbackNormal = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_NORMAL_REQ_CODE);
            }

            // For Android 4.1+
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                openFileChooser(uploadMsg, acceptType);
            }


            // For Android 5.0+
            public boolean onShowFileChooser(
                    WebView webView, ValueCallback<Uri[]> filePathCallback,
                    FileChooserParams fileChooserParams) {
                if (filePathCallbackLollipop != null) {
//                    filePathCallbackLollipop.onReceiveValue(null);
                    filePathCallbackLollipop = null;
                }
                filePathCallbackLollipop = filePathCallback;


                // Create AndroidExampleFolder at sdcard
                File imageStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "AndroidExampleFolder");
                if (!imageStorageDir.exists()) {
                    // Create AndroidExampleFolder at sdcard
                    imageStorageDir.mkdirs();
                }

                // Create camera captured image file path and name
                File file = new File(imageStorageDir + File.separator + "IMG_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
                mCapturedImageURI = Uri.fromFile(file);

                Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);

                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");

                // Create file chooser intent
                Intent chooserIntent = Intent.createChooser(i, "Image Chooser");
                // Set camera intent to file chooser
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Parcelable[]{captureIntent});

                // On select image call onActivityResult method of activity
                startActivityForResult(chooserIntent, FILECHOOSER_LOLLIPOP_REQ_CODE);
                return true;

            }

        };
    }

    WebViewClient client;
    {
        client = new WebViewClient() {
            //페이지 로딩중일 때 (마시멜로) 6.0 이후에는 쓰지 않음
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                Log.d("url",url);

                if (url.startsWith("tel")) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse(url));

                    try {
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.


                        }
                        startActivity(intent);
                        return true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }



                }else if(url.startsWith("https://youtu.be")){
                    Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(url));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY|Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    startActivity(intent);
                    return true;
                }else if (url.startsWith("intent:")) {

                    try {
                        Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                        Intent existPackage = getPackageManager().getLaunchIntentForPackage(intent.getPackage());
                        if (existPackage != null) {
                            getBaseContext().startActivity(intent);
                        } else {
                            Intent marketIntent = new Intent(Intent.ACTION_VIEW);
                            marketIntent.setData(Uri.parse("market://details?id=" + intent.getPackage()));
                            startActivity(marketIntent);
                        }
                        return true;
                    } catch (Exception e) {
                        Log.d("error1",e.toString());
                        e.printStackTrace();
                    }
                }else if(!url.startsWith(getString(R.string.domain))){
                    Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(url));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY|Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    startActivity(intent);
                    return true;
                }else{



                }



                return false;
            }
            //페이지 로딩이 다 끝났을 때
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if(url.startsWith(getString(R.string.mypage_url))){
                    backImg.setImageResource(R.drawable.baseline_close_white_36);
                    rightBtnImg.setVisibility(View.INVISIBLE);
                }else{
                    backImg.setImageResource(R.drawable.baseline_keyboard_backspace_white_36);
                    rightBtnImg.setVisibility(View.VISIBLE);
                }
                if(!nextForwardBoolean) {
                    nextForwardBoolean=true;
                    webView.clearHistory();
                }
                webView.loadUrl("javascript:setFcm('"+Common.TOKEN+"')");
                //webLayout.setRefreshing(false);
/*
                if (url.startsWith(getString(R.string.url)+"index.php") || url.startsWith(getString(R.string.domain)+"/currentLat")||url.equals(getString(R.string.url)+"bbs/board.php?bo_table=short")||url.startsWith(getString(R.string.domain)+"bbs/login.php")) {
                    isIndex=true;
                } else {
                    isIndex=false;
                }*/

            }
            //페이지 오류가 났을 때 6.0 이후에는 쓰이지 않음
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                //super.onReceivedError(view, request, error);
                //view.loadUrl("");
                //페이지 오류가 났을 때 오류메세지 띄우기
                /*AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                builder.setMessage("네트워크 상태가 원활하지 않습니다. 잠시 후 다시 시도해 주세요.");
                builder.show();*/
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();

    }


    //다시 들어왔을 때
    @Override
    protected void onResume() {
        super.onResume();
        /* cookie */
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.getInstance().startSync();
        }



        execBoolean=true;
        Log.d("newtork","onResume");
        try{
            Intent intent=getIntent();
            Uri data=intent.getData();
            Log.d("data111",data.toString());
        }catch (Exception e){

        }

        //netCheck.networkCheck();
    }
    //홈버튼 눌러서 바탕화면 나갔을 때
    @Override
    protected void onPause() {
        super.onPause();
        /* cookie */
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.getInstance().stopSync();
        }

        execBoolean=false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // unregisterReceiver(receiver);


    }
    //뒤로가기를 눌렀을 때
    public void onBackPressed() {
        if(webView.canGoBack()){
            webView.goBack();
        }else {
            Intent intent2 = new Intent();
            intent2.putExtra("result", "OK");
            setResult(1234, intent2);
            finish();
        }

    }
    //로그인 로그아웃
    class WebJavascriptEvent{


        @JavascriptInterface
        public void setLogin(String mb_id){
            Common.savePref(IndexActivity.this,"mb_id",mb_id);
        }
        @JavascriptInterface
        public void setLogout(){
            Common.savePref(IndexActivity.this,"mb_id","");
        }
        @JavascriptInterface
        public void setPosition(){
            webView.post(new Runnable() {
                @Override
                public void run() {

                }
            });
        }
        @JavascriptInterface
        public void finish(){
            IndexActivity.this.finish();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK) {
            if (requestCode == FILECHOOSER_NORMAL_REQ_CODE) {
                if (filePathCallbackNormal == null) return;
                Uri result = (data == null || resultCode != RESULT_OK) ? null : data.getData();
                filePathCallbackNormal.onReceiveValue(result);
                filePathCallbackNormal = null;

            } else if (requestCode == FILECHOOSER_LOLLIPOP_REQ_CODE) {
                Uri[] result = new Uri[0];
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (resultCode == RESULT_OK) {
                        result = (data == null) ? new Uri[]{mCapturedImageURI} : WebChromeClient.FileChooserParams.parseResult(resultCode, data);
                    }

                    filePathCallbackLollipop.onReceiveValue(result);

                }
            }
        }else{
            try {
                if (filePathCallbackLollipop != null) {
                    filePathCallbackLollipop.onReceiveValue(null);
                    filePathCallbackLollipop = null;
                }
            }catch (Exception e){

            }
        }
    }

    @OnClick({R.id.backImg,R.id.btmMenuLayout1,R.id.btmMenuLayout2,R.id.btmMenuLayout3,R.id.btmMenuLayout4,R.id.btmMenuLayout5})
    public void onClick(View view){
        //하단 메뉴 클릭시 색상 미리 초기화화
       for(int i=2;i<=5;i++) {
            int imageViewId=getResources().getIdentifier("btmMenuImg"+i,"id",getPackageName());
            int textViewId=getResources().getIdentifier("btmText"+i,"id",getPackageName());
            int layoutId=getResources().getIdentifier("btmMenuLayout"+i,"id",getPackageName());
            ((ImageView)findViewById(imageViewId)).setColorFilter(null);
            ((TextView)findViewById(textViewId)).setTextColor(Color.parseColor("#333331"));
           ((LinearLayout)findViewById(layoutId)).setBackgroundColor(Color.parseColor("#ffffff"));
        }
       int no=0;
        switch (view.getId()){
            case R.id.backImg:
                if(webView.canGoBack()){
                    webView.goBack();
                }else {
                    Intent intent2 = new Intent();
                    intent2.putExtra("result","OK");
                    setResult(1234, intent2);

                    finish();
                }
                break;
            case R.id.btmMenuLayout1:
                Intent intent2 = new Intent();
                intent2.putExtra("result","OK");
                setResult(1234, intent2);

                finish();
                break;
            case R.id.btmMenuLayout2:
                nextForwardBoolean=false;
                btmMenuImg2.setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_IN);
                btmText2.setTextColor(Color.parseColor("#ffffff"));
                btmMenuLayout2.setBackgroundColor(Color.parseColor("#232b74"));
                topTitleTxt.setText(titleArray[1]);
                webView.loadUrl(getString(urlString[1]));
                webViewSetting();
                break;
            case R.id.btmMenuLayout3:
                nextForwardBoolean=false;
                btmMenuImg3.setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_IN);
                btmText3.setTextColor(Color.parseColor("#ffffff"));
                btmMenuLayout3.setBackgroundColor(Color.parseColor("#232b74"));
                topTitleTxt.setText(titleArray[2]);
                webView.loadUrl(getString(urlString[2]));
                webViewSetting();
                break;
            case R.id.btmMenuLayout4:
                nextForwardBoolean=false;
                btmMenuImg4.setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_IN);
                btmText4.setTextColor(Color.parseColor("#ffffff"));
                btmMenuLayout4.setBackgroundColor(Color.parseColor("#232b74"));
                topTitleTxt.setText(titleArray[3]);
                webView.loadUrl(getString(urlString[3]));
                webViewSetting();
                break;
            case R.id.btmMenuLayout5:
                nextForwardBoolean=false;
                btmMenuImg5.setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_IN);
                btmText5.setTextColor(Color.parseColor("#ffffff"));
                btmMenuLayout5.setBackgroundColor(Color.parseColor("#232b74"));
                topTitleTxt.setText(titleArray[4]);
                webView.loadUrl(getString(urlString[4]));
                webViewSetting();
                break;


        }

//        overridePendingTransition(R.anim.anim_slide_out_right, R.anim.anim_slide_in_left);

    }
    @OnClick(R.id.rightBtnImg)
    public void rightOnClick(){
        topTitleTxt.setText(titleArray[1]);
        webView.loadUrl(getString(urlString[1]));
    }
    private void refreshToken(){
        FirebaseMessaging.getInstance().subscribeToTopic("Team930");
        Common.TOKEN= FirebaseInstanceId.getInstance().getToken();

    }

}
