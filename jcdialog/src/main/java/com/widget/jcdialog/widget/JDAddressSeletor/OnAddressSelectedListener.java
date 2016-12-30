package com.widget.jcdialog.widget.JDAddressSeletor;


import com.widget.jcdialog.widget.pickerview.model.CityModel;
import com.widget.jcdialog.widget.pickerview.model.DistrictModel;
import com.widget.jcdialog.widget.pickerview.model.ProvinceModel;

public interface OnAddressSelectedListener {
//     Street street
    void onAddressSelected(ProvinceModel province, CityModel city, DistrictModel county);
}
