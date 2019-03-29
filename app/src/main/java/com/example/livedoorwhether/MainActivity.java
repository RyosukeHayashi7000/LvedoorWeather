package com.example.livedoorwhether;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private IWeatherAPI weatherAPI;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private TextView mTextView_Telop = (TextView) findViewById(R.id.mTextView_Telop);
    private TextView mEditText_Desc = (TextView) findViewById(R.id.mTextView_Telop);
    private Button clickButton = (Button) findViewById(R.id.button_click);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        clickButton.onButtonClicked();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://weather.livedoor.com")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        weatherAPI = retrofit.create(IWeatherAPI.class);
    }

    private void onButtonClicked() {
        Disposable d = weatherAPI.getWhether("200010")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (response) -> {
                            mTextView_Telop.setText(response.forecasts.get(0).telop);
                            mEditText_Desc.setText(response.description.text);
                        },
                        (error) -> {
                            Log.d(TAG, error.toString());
                            Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT);
                        });
        compositeDisposable.add(d);

    }


}
