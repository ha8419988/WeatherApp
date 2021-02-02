package com.example.test;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test.Common.Common;
import com.example.test.Model.WeatherResult;
import com.example.test.Retrofit.IOpenWeatherMap;
import com.example.test.Retrofit.RetrofitClient;
import com.squareup.picasso.Picasso;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TodayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TodayFragment extends Fragment {
    ImageView img_weather;
    TextView txt_city, txt_humidity, txt_sunrise, txt_sunset, txt_pressure, txt_temperature, txt_description, txt_date_time, txt_wind, txt_coor;
    LinearLayout weather_panel;
    ProgressBar loading;
    CompositeDisposable compositeDisposable;
    IOpenWeatherMap mService;

    static TodayFragment instance;

    public static TodayFragment getInstance() {
        if (instance == null)
            instance = new TodayFragment();
        return instance;
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TodayFragment() {
        // Required empty public constructor
        compositeDisposable = new CompositeDisposable();
        Retrofit retrofit = RetrofitClient.getInstance();
        mService = retrofit.create(IOpenWeatherMap.class);

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TodayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TodayFragment newInstance(String param1, String param2) {
        TodayFragment fragment = new TodayFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    //
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.fragment_today, container, false);
        img_weather = itemView.findViewById(R.id.imd_weather);
        txt_city = itemView.findViewById(R.id.city_name);
        txt_humidity = itemView.findViewById(R.id.humidity);
        txt_sunrise = itemView.findViewById(R.id.sunrise);
        txt_sunset = itemView.findViewById(R.id.sunset);
        txt_pressure = itemView.findViewById(R.id.pressure);
        txt_temperature = itemView.findViewById(R.id.temp);
        txt_description = itemView.findViewById(R.id.txt_description);
        txt_wind = itemView.findViewById(R.id.windspeed);
        txt_coor = itemView.findViewById(R.id.coor);
        weather_panel = itemView.findViewById(R.id.weather_panel);
        loading = itemView.findViewById(R.id.loading);
        getWeatherInformation();
        return itemView;
    }

    private void getWeatherInformation() {
        compositeDisposable.add(mService.getWeatherByLatLng(String.valueOf(Common.current_location.getLatitude()),
                String.valueOf(Common.current_location.getLongitude()), Common.APP_ID,
                "metric").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).
                        subscribe(new Consumer<WeatherResult>() {
                            @Override
                            public void accept(WeatherResult weatherResult) throws Exception {
//load image
                                Picasso.get().load(new StringBuilder("https://openweathermap.org/img/w/")
                                        .append(weatherResult.getWeather().get(0).getIcon()).append(".png").toString()).into(img_weather);
                                txt_city.setText(weatherResult.getName());
                                txt_description.setText(weatherResult.getName());
                                txt_temperature.setText(new StringBuilder(String.valueOf(weatherResult.getMain().getTemp())).append("°C"));
                                txt_pressure.setText(new StringBuilder(String.valueOf(weatherResult.getMain().getPressure())));
                                txt_humidity.setText(new StringBuilder(String.valueOf(weatherResult.getMain().getHumidity())).append(" % ").toString());
                                txt_sunrise.setText(Common.convertToHour(weatherResult.getSys().getSunrise()));
                                txt_sunset.setText(Common.convertToHour(weatherResult.getSys().getSunset()));
                                txt_coor.setText(new StringBuilder("[").append(weatherResult.getCoord().toString()).
                                        append("]").toString());

                                //display panel
                                weather_panel.setVisibility(View.VISIBLE);
                                loading.setVisibility(View.GONE);
                            }

                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Toast.makeText(getActivity(), "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
        );
    }

    @Override
    public void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }
}