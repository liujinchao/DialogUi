package com.widget.jcdialog.widget.JDAddressSeletor;

import com.widget.jcdialog.widget.pickerview.model.CityModel;
import com.widget.jcdialog.widget.pickerview.model.DistrictModel;
import com.widget.jcdialog.widget.pickerview.model.ProvinceModel;
import java.util.List;


public interface AddressProvider {
    void provideProvinces(AddressReceiver<ProvinceModel> addressReceiver);
    void provideCitiesWith(int provinceId, AddressReceiver<CityModel> addressReceiver);
    void provideCountiesWith(int provinceId, int cityId, AddressReceiver<DistrictModel> addressReceiver);
//    void provideStreetsWith(int countyId, AddressReceiver<Street> addressReceiver);

    interface AddressReceiver<T> {
        void send(List<T> data);
    }
}