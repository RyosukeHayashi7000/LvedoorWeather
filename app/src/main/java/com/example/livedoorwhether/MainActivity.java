package com.example.livedoorwhether;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.livedoorwhether.databinding.ActivityMainBinding;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private IWeatherAPI weatherAPI;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    Single<WeatherResponse> weatherAPISingle;


    private TextView mTextView_Telop;
    private TextView mEditText_Desc;
    private Button clickButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        mTextView_Telop = findViewById(R.id.mTextView_Telop);
//        mEditText_Desc = findViewById(R.id.mEditText_Desc);
        clickButton = findViewById(R.id.buttonClick);


        clickButton.setOnClickListener(v -> onButtonClicked());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://weather.livedoor.com") //基本のドメイン
                .addConverterFactory(GsonConverterFactory.create()) //JsonをデータにするGsonコンバーター
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) //RxJavaを導入
                .build();

        weatherAPI = retrofit.create(IWeatherAPI.class);
    }

    private void onButtonClicked() {

        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        weatherAPISingle = weatherAPI.getWhether("200010");
        compositeDisposable.add(weatherAPISingle
                .subscribeOn(Schedulers.io()) //別スレッドにデータを実行
                .observeOn(AndroidSchedulers.mainThread()) //結果をメインスレッドに反映
                .subscribeWith(new DisposableSingleObserver<WeatherResponse>() {
                    @Override
                    public void onSuccess(WeatherResponse weatherResponse) {
                        binding.mTextViewTelop.setText(weatherResponse.forecasts.get(0).telop);
                        binding.mEditTextDesc.setText(weatherResponse.description.text);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("error", e.toString());
                        Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT);
                    }

                }));

    }


//                        (response) -> {
//                            mTextView_Telop.setText(response.forecasts.get(0).telop);
//                            mEditText_Desc.setText(response.description.text);
//                        },
//                        (error) -> {
//                            Log.d("error", error.toString());
//                            Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT);
//                        });LENGTH_SHORT


    @Override
    public void onDestroy() {
        super.onDestroy();

        //RxJavaでの非同期処理の場合
        compositeDisposable.clear();

    }
}
