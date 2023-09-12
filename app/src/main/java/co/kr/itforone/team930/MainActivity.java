package co.kr.itforone.team930;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Message;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieSyncManager;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.kr.itforone.team930.bbs.BannerData;
import co.kr.itforone.team930.bbs.BoardData;
import co.kr.itforone.team930.bbs.TelData;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import util.BackPressCloseHandler;
import util.Common;
import util.RetrofitService;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.mainImg1)
    ImageView mainImg1;
    @BindView(R.id.mainImg2)
    ImageView mainImg2;
    @BindView(R.id.mainTitleTxt1)
    TextView mainTitleTxt1;
    @BindView(R.id.mainTitleTxt2)
    TextView mainTitleTxt2;
    @BindView(R.id.mainTitleTxt3)
    TextView mainTitleTxt3;

    @BindView(R.id.mainWebView)
    WebView mainWebView;
    @BindView(R.id.webViewLayout)
    RelativeLayout webViewLayout;
    Map linkMap=new HashMap();

    String team_tel="",astel="",buytel="";
    private BackPressCloseHandler backPressCloseHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent=new Intent(this,SplashActivity.class);
        backPressCloseHandler = new BackPressCloseHandler(this);
        startActivity(intent);
        ButterKnife.bind(this);

        setBoard("notice",1);
        setTel();
        setBanner();

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
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.createInstance(this);
        }
        webViewSetting();
        mainWebView.loadUrl(getString(R.string.popup_url));
    }
    @Override
    protected void onResume() {
        super.onResume();
        /* cookie */
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.getInstance().startSync();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        /* cookie */
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.getInstance().stopSync();
        }
    }
    //클릭이벤트
    @OnClick({R.id.menu1Layout,R.id.menu2Layout,R.id.menu3Layout,
              R.id.telBtnLayout,R.id.telBtnLayout2,R.id.noticeListTxt,
              R.id.mainGameLayout1,R.id.mainGameLayout2,
              R.id.mainTitleTxt3,R.id.telImg,
              R.id.btmMenuLayout2,R.id.btmMenuLayout3,R.id.btmMenuLayout4,R.id.btmMenuLayout5,
              R.id.btmTextBtn1,R.id.btmTextBtn2
              })
    public void onClick(View view){
        Intent intent=new Intent(this,IndexActivity.class);
        switch (view.getId()){
            case R.id.btmTextBtn1:
                intent.putExtra("title","이용약관");
                intent.putExtra("no",7);
                startActivityForResult(intent,9000);
                break;
            case R.id.btmTextBtn2:
                intent.putExtra("title","개인정보처리방침");
                intent.putExtra("no",8);
                startActivityForResult(intent,9000);
                break;
            case R.id.menu1Layout:
                intent.putExtra("title","A/S접수");
                intent.putExtra("no",2);
                startActivityForResult(intent,9000);
                break;
            case R.id.menu2Layout:
                intent.putExtra("title","출시게임");
                intent.putExtra("no",5);
                startActivityForResult(intent,9000);
                break;
            case R.id.menu3Layout:
                intent.putExtra("title","중고직거래");
                intent.putExtra("no",3);
                startActivityForResult(intent,9000);
                break;
                //AS문의
            case R.id.telBtnLayout:
                try{
                    if(astel.equals("")){
                        Toast.makeText(this, "전화번호 수집중에 있습니다.", Toast.LENGTH_SHORT).show();
                    }else {
                        Intent call_phone = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+astel));
                        startActivity(call_phone);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
                //판매문의
            case R.id.telBtnLayout2:
                try{
                    if(buytel.equals("")){
                        Toast.makeText(this, "전화번호 수집중에 있습니다.", Toast.LENGTH_SHORT).show();
                    }else {
                        Intent call_phone = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+buytel));
                        startActivity(call_phone);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;

            case R.id.mainGameLayout1:


                if(linkMap.get("param1").toString().equals(getString(R.string.domain))) {
                    intent.putExtra("title","배너");
                    intent.putExtra("goUrl", linkMap.get("param1").toString());
                    intent.putExtra("no",5);
                    startActivityForResult(intent,9000);
                }else{
                    Intent intent2 = new Intent(Intent.ACTION_VIEW,Uri.parse(linkMap.get("param1").toString()));
                    intent2.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY|Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                    intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    startActivity(intent2);

                }

                break;
            case R.id.mainGameLayout2:
                if(linkMap.get("param2").toString().equals(getString(R.string.domain))) {
                    intent.putExtra("title","배너");
                    intent.putExtra("goUrl", linkMap.get("param2").toString());
                    intent.putExtra("no",5);
                    startActivityForResult(intent,9000);
                }else{
                    Intent intent2 = new Intent(Intent.ACTION_VIEW,Uri.parse(linkMap.get("param2").toString()));
                    intent2.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY|Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                    intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    startActivity(intent2);

                }
                break;
            case R.id.noticeListTxt:
                intent.putExtra("title","공지사항");
                intent.putExtra("goUrl",getString(R.string.domain)+"bbs/board.php?bo_table=notice");
                intent.putExtra("no",4);
                startActivityForResult(intent,9000);
                break;
            case R.id.mainTitleTxt3:
                intent.putExtra("title","공지사항");
                intent.putExtra("goUrl",getString(R.string.domain)+"bbs/board.php?"+linkMap.get("param3").toString());
                intent.putExtra("no",4);
                startActivityForResult(intent,9000);
                break;
            case R.id.telImg:
                try{
                    if(team_tel.equals("")){
                        Toast.makeText(this, "전화번호 수집중에 있습니다.", Toast.LENGTH_SHORT).show();
                    }else {
                        Intent call_phone = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+team_tel));
                        startActivity(call_phone);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

                break;
            case R.id.btmMenuLayout2:
                intent.putExtra("title","마이페이지");
                intent.putExtra("no",1);
                startActivityForResult(intent,9000);
                break;
            case R.id.btmMenuLayout3:
                intent.putExtra("title","A/S접수ㆍ현황");
                intent.putExtra("no",2);
                startActivityForResult(intent,9000);
                break;
            case R.id.btmMenuLayout4:
                intent.putExtra("title","중고직거래");
                intent.putExtra("no",3);
                startActivityForResult(intent,9000);
                break;
            case R.id.btmMenuLayout5:
                intent.putExtra("title","공지사항");
                intent.putExtra("no",4);
                startActivityForResult(intent,9000);
                break;
        }

    }
    //최근게시물 세팅
    public void setBoard(String bo_table,int limit){
        //httpok 로그 보기
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //클라이언트 설정
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .build();
        //레트로핏 설정
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(getString(R.string.domain))
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        //파라미터 넘길 값 설정
        Map map=new HashMap();
        map.put("division","bbs_board_list");
        map.put("bo_table",bo_table);
        map.put("limit",limit);

        //레트로핏 서비스 실행하기
        RetrofitService retrofitService=retrofit.create(RetrofitService.class);
        //데이터 불러오기
        Call<BoardData> call=retrofitService.getBbsBoardList(map);
        call.enqueue(new Callback<BoardData>() {

            @Override
            public void onResponse(Call<BoardData> call, Response<BoardData> response) {
                //서버에 데이터 받기가 성공할시
                if(response.isSuccessful()){
                    BoardData repo=response.body();
                    Log.d("response",response+"");
                    if(Boolean.parseBoolean(repo.getSuccess())==false){
                    }else{


                        if(bo_table.equals("game")){
                            for(int i=0;i<repo.getData().size();i++){
                                if(i==0) {
                                    Glide.with(getApplicationContext())
                                            .load(repo.getData().get(i).getFile1())
                                            .override(400, 300)
                                            .centerCrop()
                                            .into(mainImg1);
                                    mainTitleTxt1.setText(repo.getData().get(i).getWr_subject());

                                }else{
                                    Glide.with(getApplicationContext())
                                            .load(repo.getData().get(i).getFile1())
                                            .override(400, 300)
                                            .centerCrop()
                                            .into(mainImg2);
                                    mainTitleTxt2.setText(repo.getData().get(i).getWr_subject());
                                    linkMap.put("param2","bo_table="+bo_table+"&wr_id="+repo.getData().get(i).getWr_id());
                                }
                            }
                        }else{

                            mainTitleTxt3.setText(repo.getData().get(0).getWr_subject());
                            linkMap.put("param3","bo_table="+bo_table+"&wr_id="+repo.getData().get(0).getWr_id());
                        }

                    }
                }else{

                }
            }

            @Override
            public void onFailure(Call<BoardData> call, Throwable t) {

            }
        });
    }
    //전화번호 세팅
    public void setTel(){
        //httpok 로그 보기
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //클라이언트 설정
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .build();
        //레트로핏 설정
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(getString(R.string.domain))
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        //파라미터 넘길 값 설정
        Map map=new HashMap();
        map.put("division","tel_config");


        //레트로핏 서비스 실행하기
        RetrofitService retrofitService=retrofit.create(RetrofitService.class);
        //데이터 불러오기
        Call<TelData> call=retrofitService.getTel(map);
        call.enqueue(new Callback<TelData>() {

            @Override
            public void onResponse(Call<TelData> call, Response<TelData> response) {
                //서버에 데이터 받기가 성공할시
                if(response.isSuccessful()){
                    TelData repo=response.body();
                    Log.d("response",response+"");
                    if(Boolean.parseBoolean(repo.getSuccess())==false){
                    }else {
                        team_tel=repo.getTel();
                        astel=repo.getAstel();
                        buytel=repo.getBuytel();

                    }
                }else{

                }
            }

            @Override
            public void onFailure(Call<TelData> call, Throwable t) {

            }
        });
    }
    //배너 불러오기
    public void setBanner(){
        //httpok 로그 보기
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //클라이언트 설정
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .build();
        //레트로핏 설정
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(getString(R.string.domain))
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        //파라미터 넘길 값 설정
        Map map=new HashMap();
        map.put("division","banner");


        //레트로핏 서비스 실행하기
        RetrofitService retrofitService=retrofit.create(RetrofitService.class);
        //데이터 불러오기
        Call<BannerData> call=retrofitService.getBanner(map);
        call.enqueue(new Callback<BannerData>() {

            @Override
            public void onResponse(Call<BannerData> call, Response<BannerData> response) {
                //서버에 데이터 받기가 성공할시
                if(response.isSuccessful()){
                    BannerData repo=response.body();
                    Log.d("response",response+"");
                    if(Boolean.parseBoolean(repo.getSuccess())==false){
                    }else {
                        Glide.with(getApplicationContext())
                                .load(repo.getImage1())
                                .fitCenter()
                                .into(mainImg1);
                        linkMap.put("param1",repo.getUrl1());
                        mainTitleTxt1.setText(repo.getSubject1());
                        Glide.with(getApplicationContext())
                                .load(repo.getImage2())
                                .fitCenter()
                                .into(mainImg2);
                        mainTitleTxt2.setText(repo.getSubject2());
                        linkMap.put("param2",repo.getUrl2());

                    }
                }else{

                }
            }

            @Override
            public void onFailure(Call<BannerData> call, Throwable t) {

            }
        });
    }

    //뒤로가기를 눌렀을 때
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }

    private void refreshToken(){
        FirebaseMessaging.getInstance().subscribeToTopic("Team930");
        Common.TOKEN= FirebaseInstanceId.getInstance().getToken();

    }

    @SuppressLint("JavascriptInterface")
    @RequiresApi(api = Build.VERSION_CODES.ECLAIR_MR1)
    public void webViewSetting() {
        WebSettings setting = mainWebView.getSettings();//웹뷰 세팅용
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
        mainWebView.setWebChromeClient(chrome);//웹에서 경고창이나 또는 컴펌창을 띄우기 위한 메서드
        mainWebView.setWebViewClient(client);//웹페이지 관련된 메서드 페이지 이동할 때 또는 페이지가 로딩이 끝날 때 주로 쓰임
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            setting.setTextZoom(100);
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setting.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW); // 혼합된 컨텐츠 허용//
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mainWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            // older android version, disable hardware acceleration
            mainWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
            //기기에 따라서 동작할수도있는걸 확인
            setting.setRenderPriority(WebSettings.RenderPriority.HIGH);

            //최신 SDK 에서는 Deprecated 이나 아직 성능상에서는 유용하다
            setting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

            //부드러운 전환 또한 아직 동작
            setting.setEnableSmoothTransition(true);
        }

        mainWebView.addJavascriptInterface(new WebJavascriptEvent(), "Android");



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
                new AlertDialog.Builder(MainActivity.this)
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
                new AlertDialog.Builder(MainActivity.this)
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
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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


        };
    }

    WebViewClient client;

    {
        client = new WebViewClient() {
            //페이지 로딩중일 때 (마시멜로) 6.0 이후에는 쓰지 않음
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {


                return false;
            }

            //페이지 로딩이 다 끝났을 때
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);


            }

            //페이지 오류가 났을 때 6.0 이후에는 쓰이지 않음
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

            }
        };
    }

    //팝업창 닫기
    class WebJavascriptEvent{
        @JavascriptInterface
        public void closePopup(){
            mainWebView.post(new Runnable() {
                @Override
                public void run() {
                    webViewLayout.setVisibility(View.GONE);
                }
            });
        }

    }
}
