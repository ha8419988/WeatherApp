package com.example.test;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test.Adapter.WeeatherForecastAdapter;
import com.example.test.Common.Common;
import com.example.test.Model.WeatherForecastResult;
import com.example.test.Model.WeatherResult;
import com.example.test.Retrofit.IOpenWeatherMap;
import com.example.test.Retrofit.RetrofitClient;
import com.squareup.picasso.Picasso;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ForecastFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForecastFragment extends Fragment {

    CompositeDisposable compositeDisposable;

    IOpenWeatherMap iOpenWeatherMap;
    TextView txt_city, txt_geocoor;
    RecyclerView rcv_forecast;

    static ForecastFragment instance;

    public static ForecastFragment getInstance() {
        if (instance == null)
            instance = new ForecastFragment();
        return instance;
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ForecastFragment() {
        // Required empty public constructor
        compositeDisposable = new CompositeDisposable();
        Retrofit retrofit = RetrofitClient.getInstance();
        iOpenWeatherMap = retrofit.create(IOpenWeatherMap.class);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ForecastFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ForecastFragment newInstance(String param1, String param2) {
        ForecastFragment fragment = new ForecastFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forecast, container, false);
        txt_city = view.findViewById(R.id.txt_cityname);
        txt_geocoor = view.findViewById(R.id.txt_coor);
        rcv_forecast = view.findViewById(R.id.rcv_forecast);
        rcv_forecast.setHasFixedSize(true);
        rcv_forecast.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        getForecastInfo();

        return view;

    }

    @Override
    public void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    private void getForecastInfo() {
        compositeDisposable.add(iOpenWeatherMap.getForecastWeatherByLatLng(String.valueOf(Common.current_location.getLatitude()),
                String.valueOf(Common.current_location.getLongitude()), Common.APP_ID,
                "metric").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).
                        subscribe(new Consumer<WeatherForecastResult>() {
                            @Override
                            public void accept(WeatherForecastResult weatherForecastResultt) throws Exception {
                                txt_city.setText(new StringBuilder(weatherForecastResultt.city.name));
                                txt_geocoor.setText(new StringBuilder(weatherForecastResultt.city.coord.toString()));
                                WeeatherForecastAdapter adapter=new WeeatherForecastAdapter(getContext(),weatherForecastResultt);
                                rcv_forecast.setAdapter(adapter);
                            }

                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.d("ERROR",""+throwable.getMessage());
                            }
                        })
        );
    }
}