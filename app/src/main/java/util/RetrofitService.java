package util;

import java.util.Map;


import co.kr.itforone.team930.bbs.BannerData;
import co.kr.itforone.team930.bbs.BoardData;
import co.kr.itforone.team930.bbs.BoardListData;
import co.kr.itforone.team930.bbs.TelData;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by 투덜이2 on 2017-02-14.
 */
// GET 방식 POST 방식 변수 세팅등등 설정
public interface RetrofitService {

    @FormUrlEncoded
    @POST("adm/json/query.php")
    Call<BoardData> getBbsBoardList(
            @FieldMap Map<String, String> option
    );
    @FormUrlEncoded
    @POST("adm/json/query.php")
    Call<TelData> getTel(
            @FieldMap Map<String, String> option
    );
    @FormUrlEncoded
    @POST("adm/json/query.php")
    Call<BannerData> getBanner(
            @FieldMap Map<String, String> option
    );

}
