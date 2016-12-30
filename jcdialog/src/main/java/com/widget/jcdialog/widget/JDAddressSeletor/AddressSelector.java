package com.widget.jcdialog.widget.JDAddressSeletor;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.widget.jcdialog.widget.pickerview.model.CityModel;
import com.widget.jcdialog.widget.pickerview.model.DistrictModel;
import com.widget.jcdialog.widget.pickerview.model.ProvinceModel;
import com.widget.jcdialog.R;

import java.util.List;

public class AddressSelector implements AdapterView.OnItemClickListener {
    private static final int INDEX_TAB_PROVINCE = 0;
    private static final int INDEX_TAB_CITY = 1;
    private static final int INDEX_TAB_COUNTY = 2;
    private static final int INDEX_TAB_STREET = 3;

    private static final int INDEX_INVALID = -1;

    private static final int WHAT_PROVINCES_PROVIDED = 0;
    private static final int WHAT_CITIES_PROVIDED = 1;
    private static final int WHAT_COUNTIES_PROVIDED = 2;
    private static final int WHAT_STREETS_PROVIDED = 3;

    @SuppressWarnings("unchecked")
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_PROVINCES_PROVIDED:
                    provinces = (List<ProvinceModel>) msg.obj;
                    provinceAdapter.notifyDataSetChanged();
                    listView.setAdapter(provinceAdapter);
                    break;

                case WHAT_CITIES_PROVIDED:
                    cities = (List<CityModel>) msg.obj;
                    cityAdapter.notifyDataSetChanged();
                    if (!cities.isEmpty()) {
                        // 以次级内容更新列表
                        listView.setAdapter(cityAdapter);
                        // 更新索引为次级
                        tabIndex = INDEX_TAB_CITY;
                    } else {
                        // 次级无内容，回调
                        callbackInternal();
                    }

                    break;

                case WHAT_COUNTIES_PROVIDED:
                    counties = (List<DistrictModel>) msg.obj;
                    countyAdapter.notifyDataSetChanged();
                    if (!counties.isEmpty()) {
                        listView.setAdapter(countyAdapter);
                        tabIndex = INDEX_TAB_COUNTY;
                    } else {
                        callbackInternal();
                    }

                    break;

//                case WHAT_STREETS_PROVIDED:
//                    streets = (List<DistrictModel>) msg.obj;
//                    streetAdapter.notifyDataSetChanged();
//                    if (Lists.notEmpty(streets)) {
//                        listView.setAdapter(streetAdapter);
//                        tabIndex = INDEX_TAB_STREET;
//                    } else {
//                        callbackInternal();
//                    }
//
//                    break;
            }

            updateTabsVisibility();
            updateProgressVisibility();
            updateIndicator();

            return true;
        }
    });

    private static AddressProvider DEFAULT_ADDRESS_PROVIDER;

    private final Context context;
    private OnAddressSelectedListener listener;
    private AddressProvider addressProvider;

    private View view;

    private View indicator;

    private TextView textViewProvince;
    private TextView textViewCity;
    private TextView textViewCounty;
    private TextView textViewStreet;

    private ProgressBar progressBar;

    private ListView listView;
    private ProvinceAdapter provinceAdapter;
    private CityAdapter cityAdapter;
    private CountyAdapter countyAdapter;
//    private StreetAdapter streetAdapter;

    private List<ProvinceModel> provinces;
    private List<CityModel> cities;
    private List<DistrictModel> counties;
//    private List<Street> streets;

    private int provinceIndex = INDEX_INVALID;
    private int cityIndex = INDEX_INVALID;
    private int countyIndex = INDEX_INVALID;
    private int streetIndex = INDEX_INVALID;

    private int tabIndex = INDEX_TAB_PROVINCE;

    public AddressSelector(Context context) {
        this.context = context;

        DEFAULT_ADDRESS_PROVIDER = new DefaultAddressProvider(context);
        addressProvider = DEFAULT_ADDRESS_PROVIDER;

        initViews();
        initAdapters();
        retrieveProvinces();
    }

    private void initAdapters() {
        provinceAdapter = new ProvinceAdapter();
        cityAdapter = new CityAdapter();
        countyAdapter = new CountyAdapter();
//        streetAdapter = new StreetAdapter();
    }

    private void initViews() {
        view = LayoutInflater.from(context).inflate(R.layout.address_selector, null);

        this.progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        this.listView = (ListView) view.findViewById(R.id.listView);
        this.indicator = view.findViewById(R.id.indicator);

        this.textViewProvince = (TextView) view.findViewById(R.id.textViewProvince);
        this.textViewCity = (TextView) view.findViewById(R.id.textViewCity);
        this.textViewCounty = (TextView) view.findViewById(R.id.textViewCounty);
        this.textViewStreet = (TextView) view.findViewById(R.id.textViewStreet);

        this.textViewProvince.setOnClickListener(new OnProvinceTabClickListener());
        this.textViewCity.setOnClickListener(new OnCityTabClickListener());
        this.textViewCounty.setOnClickListener(new OnCountyTabClickListener());
        this.textViewStreet.setOnClickListener(new OnStreetTabClickListener());

        this.listView.setOnItemClickListener(this);

        updateIndicator();
    }

    public View getView() {
        return view;
    }

    private void updateIndicator() {
        view.post(new Runnable() {
            @Override
            public void run() {
                switch (tabIndex) {
                    case INDEX_TAB_PROVINCE:
                        buildIndicatorAnimatorTowards(textViewProvince).start();
                        break;
                    case INDEX_TAB_CITY:
                        buildIndicatorAnimatorTowards(textViewCity).start();
                        break;
                    case INDEX_TAB_COUNTY:
                        buildIndicatorAnimatorTowards(textViewCounty).start();
                        break;
//                    case INDEX_TAB_STREET:
//                        buildIndicatorAnimatorTowards(textViewStreet).start();
//                        break;
                }
            }
        });
    }

    private AnimatorSet buildIndicatorAnimatorTowards(TextView tab) {
        ObjectAnimator xAnimator = ObjectAnimator.ofFloat(indicator, "X", indicator.getX(), tab.getX());

        final ViewGroup.LayoutParams params = indicator.getLayoutParams();
        ValueAnimator widthAnimator = ValueAnimator.ofInt(params.width, tab.getMeasuredWidth());
        widthAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                params.width = (int) animation.getAnimatedValue();
                indicator.setLayoutParams(params);
            }
        });

        AnimatorSet set = new AnimatorSet();
        set.setInterpolator(new FastOutSlowInInterpolator());
        set.playTogether(xAnimator, widthAnimator);

        return set;
    }

    private class OnProvinceTabClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            tabIndex = INDEX_TAB_PROVINCE;
            listView.setAdapter(provinceAdapter);

            if (provinceIndex != INDEX_INVALID) {
                listView.setSelection(provinceIndex);
            }

            updateTabsVisibility();
            updateIndicator();
        }
    }

    private class OnCityTabClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            tabIndex = INDEX_TAB_CITY;
            listView.setAdapter(cityAdapter);

            if (cityIndex != INDEX_INVALID) {
                listView.setSelection(cityIndex);
            }

            updateTabsVisibility();
            updateIndicator();
        }
    }

    private class OnCountyTabClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            tabIndex = INDEX_TAB_COUNTY;
            listView.setAdapter(countyAdapter);

            if (countyIndex != INDEX_INVALID) {
                listView.setSelection(countyIndex);
            }

            updateTabsVisibility();
            updateIndicator();
        }
    }

    private class OnStreetTabClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            tabIndex = INDEX_TAB_STREET;
//            listView.setAdapter(streetAdapter);

            if (streetIndex != INDEX_INVALID) {
                listView.setSelection(streetIndex);
            }

            updateTabsVisibility();
            updateIndicator();
        }
    }

    private void updateTabsVisibility() {
        textViewProvince.setVisibility((!provinces.isEmpty()) ? View.VISIBLE : View.GONE);
        textViewCity.setVisibility((cities!=null) ? View.VISIBLE : View.GONE);
        textViewCounty.setVisibility((counties!=null) ? View.VISIBLE : View.GONE);
//        textViewStreet.setVisibility(Lists.notEmpty(streets) ? View.VISIBLE : View.GONE);

        textViewProvince.setEnabled(tabIndex != INDEX_TAB_PROVINCE);
        textViewCity.setEnabled(tabIndex != INDEX_TAB_CITY);
        textViewCounty.setEnabled(tabIndex != INDEX_TAB_COUNTY);
        textViewStreet.setEnabled(tabIndex != INDEX_TAB_STREET);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (tabIndex) {
            case INDEX_TAB_PROVINCE:
                ProvinceModel province = provinceAdapter.getItem(position);

                // 更新当前级别及子级标签文本
                textViewProvince.setText(province.getName());
                textViewCity.setText("请选择");
                textViewCounty.setText("请选择");
//                textViewStreet.setText("请选择");

                // 清空子级数据
                cities = null;
                counties = null;
//                streets = null;
                cityAdapter.notifyDataSetChanged();
                countyAdapter.notifyDataSetChanged();
//                streetAdapter.notifyDataSetChanged();

                // 更新已选中项
                this.provinceIndex = position;
                this.cityIndex = INDEX_INVALID;
                this.countyIndex = INDEX_INVALID;
                this.streetIndex = INDEX_INVALID;

                // 更新选中效果
                provinceAdapter.notifyDataSetChanged();

                retrieveCitiesWith(provinceIndex);

                break;

            case INDEX_TAB_CITY:
                CityModel city = cityAdapter.getItem(position);

                textViewCity.setText(city.getName());
                textViewCounty.setText("请选择");
                textViewStreet.setText("请选择");

                counties = null;
//                streets = null;
                countyAdapter.notifyDataSetChanged();
//                streetAdapter.notifyDataSetChanged();

                this.cityIndex = position;
                this.countyIndex = INDEX_INVALID;
                this.streetIndex = INDEX_INVALID;

                cityAdapter.notifyDataSetChanged();

                retrieveCountiesWith(provinceIndex,position);
                break;

            case INDEX_TAB_COUNTY:
                DistrictModel county = countyAdapter.getItem(position);

                textViewCounty.setText(county.getName());
                textViewStreet.setText("请选择");

//                streets = null;
//                streetAdapter.notifyDataSetChanged();

                this.countyIndex = position;
                this.streetIndex = INDEX_INVALID;

                countyAdapter.notifyDataSetChanged();

//                retrieveStreetsWith(county.getName());
                callbackInternal();
                break;

//            case INDEX_TAB_STREET:
//                Street street = streetAdapter.getItem(position);
//
//                textViewStreet.setText(street.name);
//
//                this.streetIndex = position;
//
//                streetAdapter.notifyDataSetChanged();
//
//                callbackInternal();
//
//                break;
        }

        updateTabsVisibility();
        updateIndicator();
    }

    private void callbackInternal() {
        if (listener != null) {
            ProvinceModel province = provinces == null || provinceIndex == INDEX_INVALID ? null : provinces.get(provinceIndex);
            CityModel city = cities == null || cityIndex == INDEX_INVALID ? null : cities.get(cityIndex);
            DistrictModel county = counties == null || countyIndex == INDEX_INVALID ? null : counties.get(countyIndex);
//            Street street = streets == null || streetIndex == INDEX_INVALID ? null : streets.get(streetIndex);
//            listener.onAddressSelected(province, city, county, street);
            listener.onAddressSelected(province, city, county);
        }
    }

    private void updateProgressVisibility() {
        ListAdapter adapter = listView.getAdapter();
        int itemCount = adapter.getCount();
        progressBar.setVisibility(itemCount > 0 ? View.GONE : View.VISIBLE);
    }

    private void retrieveProvinces() {
        progressBar.setVisibility(View.VISIBLE);
        addressProvider.provideProvinces(new AddressProvider.AddressReceiver<ProvinceModel>() {
            @Override
            public void send(List<ProvinceModel> data) {
                handler.sendMessage(Message.obtain(handler, WHAT_PROVINCES_PROVIDED, data));
            }
        });
    }

    private void retrieveCitiesWith(int provinceId) {
        progressBar.setVisibility(View.VISIBLE);
        addressProvider.provideCitiesWith(provinceId, new AddressProvider.AddressReceiver<CityModel>() {
            @Override
            public void send(List<CityModel> data) {
                handler.sendMessage(Message.obtain(handler, WHAT_CITIES_PROVIDED, data));
            }
        });
    }

    private void retrieveCountiesWith(int provinceId,int cityId) {
        progressBar.setVisibility(View.VISIBLE);
        addressProvider.provideCountiesWith(provinceId,cityId, new AddressProvider.AddressReceiver<DistrictModel>() {
            @Override
            public void send(List<DistrictModel> data) {
                handler.sendMessage(Message.obtain(handler, WHAT_COUNTIES_PROVIDED, data));
            }
        });
    }

    private void retrieveStreetsWith(String countyId) {
//        progressBar.setVisibility(View.VISIBLE);
//        addressProvider.provideStreetsWith(countyId, new AddressProvider.AddressReceiver<Street>() {
//            @Override
//            public void send(List<Street> data) {
//                handler.sendMessage(Message.obtain(handler, WHAT_STREETS_PROVIDED, data));
//            }
//        });
    }

    private class ProvinceAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return provinces == null ? 0 : provinces.size();
        }

        @Override
        public ProvinceModel getItem(int position) {
            return provinces.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;

            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_area, parent, false);

                holder = new Holder();
                holder.textView = (TextView) convertView.findViewById(R.id.textView);
                holder.imageViewCheckMark = (ImageView) convertView.findViewById(R.id.imageViewCheckMark);

                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }

            ProvinceModel item = getItem(position);
            holder.textView.setText(item.getName());

            boolean checked = provinceIndex != INDEX_INVALID && provinces.get(provinceIndex).getName() == item.getName();
            holder.textView.setEnabled(!checked);
            holder.imageViewCheckMark.setVisibility(checked ? View.VISIBLE : View.GONE);

            return convertView;
        }

        class Holder {
            TextView textView;
            ImageView imageViewCheckMark;
        }
    }

    private class CityAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return cities == null ? 0 : cities.size();
        }

        @Override
        public CityModel getItem(int position) {
            return cities.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;

            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_area, parent, false);

                holder = new Holder();
                holder.textView = (TextView) convertView.findViewById(R.id.textView);
                holder.imageViewCheckMark = (ImageView) convertView.findViewById(R.id.imageViewCheckMark);

                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }

            CityModel item = getItem(position);
            holder.textView.setText(item.getName());

            boolean checked = cityIndex != INDEX_INVALID && cities.get(cityIndex).getName() == item.getName();
            holder.textView.setEnabled(!checked);
            holder.imageViewCheckMark.setVisibility(checked ? View.VISIBLE : View.GONE);

            return convertView;
        }

        class Holder {
            TextView textView;
            ImageView imageViewCheckMark;
        }
    }

    private class CountyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return counties == null ? 0 : counties.size();
        }

        @Override
        public DistrictModel getItem(int position) {
            return counties.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;

            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_area, parent, false);

                holder = new Holder();
                holder.textView = (TextView) convertView.findViewById(R.id.textView);
                holder.imageViewCheckMark = (ImageView) convertView.findViewById(R.id.imageViewCheckMark);

                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }

            DistrictModel item = getItem(position);
            holder.textView.setText(item.getName());

            boolean checked = countyIndex != INDEX_INVALID && counties.get(countyIndex).getName() == item.getName();
            holder.textView.setEnabled(!checked);
            holder.imageViewCheckMark.setVisibility(checked ? View.VISIBLE : View.GONE);

            return convertView;
        }

        class Holder {
            TextView textView;
            ImageView imageViewCheckMark;
        }
    }

//    private class StreetAdapter extends BaseAdapter {
//
//        @Override
//        public int getCount() {
//            return streets == null ? 0 : streets.size();
//        }
//
//        @Override
//        public Street getItem(int position) {
//            return streets.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return getItem(position).id;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            Holder holder;
//
//            if (convertView == null) {
//                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_area, parent, false);
//
//                holder = new Holder();
//                holder.textView = (TextView) convertView.findViewById(R.id.textView);
//                holder.imageViewCheckMark = (ImageView) convertView.findViewById(R.id.imageViewCheckMark);
//
//                convertView.setTag(holder);
//            } else {
//                holder = (Holder) convertView.getTag();
//            }
//
//            Street item = getItem(position);
//            holder.textView.setText(item.name);
//
//            boolean checked = streetIndex != INDEX_INVALID && streets.get(streetIndex).id == item.id;
//            holder.textView.setEnabled(!checked);
//            holder.imageViewCheckMark.setVisibility(checked ? View.VISIBLE : View.GONE);
//
//            return convertView;
//        }
//
//        class Holder {
//            TextView textView;
//            ImageView imageViewCheckMark;
//        }
//    }

    public OnAddressSelectedListener getOnAddressSelectedListener() {
        return listener;
    }

    public void setOnAddressSelectedListener(OnAddressSelectedListener listener) {
        this.listener = listener;
    }

    public void setAddressProvider(AddressProvider addressProvider) {
        this.addressProvider = addressProvider;
        if (addressProvider == null) {
            this.addressProvider = DEFAULT_ADDRESS_PROVIDER;
        }
    }

}
