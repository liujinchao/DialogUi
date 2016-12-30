package com.widget.jcdialog.widget.JDAddressSeletor;

import android.content.Context;
import android.content.res.AssetManager;


import com.widget.jcdialog.widget.pickerview.model.CityModel;
import com.widget.jcdialog.widget.pickerview.model.DistrictModel;
import com.widget.jcdialog.widget.pickerview.model.IPickerViewData;
import com.widget.jcdialog.widget.pickerview.model.PickerViewData;
import com.widget.jcdialog.widget.pickerview.model.ProvinceModel;
import com.widget.jcdialog.widget.pickerview.utils.XmlParserHandlerUtil;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class DefaultAddressProvider implements AddressProvider {
    private ArrayList<ProvinceModel> mProvinceDatas = new ArrayList<>();
    private ArrayList<ArrayList<CityModel>> mCityDatas = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<DistrictModel>>> mDistrictDatas = new ArrayList<>();
    protected String mCurrentProviceName;
    protected String mCurrentCityName;
    protected String mCurrentDistrictName ="";
    protected String mCurrentZipCode ="";
    private ArrayList<String> mProvinceStr = new ArrayList<>();
    private ArrayList<ArrayList<String>> mCityStr = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<IPickerViewData>>> mDistrictStr = new ArrayList<>();
    /**
     * key - 区 values - 邮编
     */
    protected Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();
    public DefaultAddressProvider(Context context) {
        initProvinceDatas(context);
    }


    @Override
    public void provideProvinces(AddressReceiver<ProvinceModel> addressReceiver) {
        addressReceiver.send(mProvinceDatas);
    }

    @Override
    public void provideCitiesWith(int provinceId, AddressReceiver<CityModel> addressReceiver) {
        addressReceiver.send(mCityDatas.get(provinceId));
    }

    @Override
    public void provideCountiesWith(int provinceId,int cityId, AddressReceiver<DistrictModel> addressReceiver) {
        addressReceiver.send(mDistrictDatas.get(provinceId).get(cityId));
    }

    /**
     * 解析省市区的XML数据
     */

    protected void initProvinceDatas(Context context)
    {
        List<ProvinceModel> provinceList = null;
        AssetManager asset = context.getAssets();
        try {
            InputStream input = asset.open("province_data.xml");
            // 创建一个解析xml的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // 解析xml
            SAXParser parser = spf.newSAXParser();
            XmlParserHandlerUtil handler = new XmlParserHandlerUtil();
            parser.parse(input, handler);
            input.close();
            // 获取解析出来的数据
            provinceList = handler.getDataList();
            //*/ 初始化默认选中的省、市、区
            if (provinceList!= null && !provinceList.isEmpty()) {
                mCurrentProviceName = provinceList.get(0).getName();
                List<CityModel> cityList = provinceList.get(0).getCityList();
                if (cityList!= null && !cityList.isEmpty()) {
                    mCurrentCityName = cityList.get(0).getName();
                    List<DistrictModel> districtList = cityList.get(0).getDistrictList();
                    mCurrentDistrictName = districtList.get(0).getName();
                    mCurrentZipCode = districtList.get(0).getZipcode();
                }
            }
            //*/
            for (int i=0; i< provinceList.size(); i++) {
                // 遍历所有省的数据
                mProvinceDatas.add(provinceList.get(i));
                mProvinceStr.add(provinceList.get(i).getName());
                List<CityModel> cityList = provinceList.get(i).getCityList();
                ArrayList<CityModel> cityNames = new ArrayList<>();//存放当前省份下面的市
                ArrayList<ArrayList<DistrictModel>> options3Items_01 = new ArrayList<>();//存放市所有的区县
                ArrayList<String> cityNameStr = new ArrayList<>();//存放当前省份下面的市
                ArrayList<ArrayList<IPickerViewData>> options3Items_01Str = new ArrayList<>();//存放市所有的区县
                for (int j=0; j< cityList.size(); j++) {
                    // 遍历省下面的所有市的数据
//					cityNames[j] = cityList.get(j).getName();
                    cityNames.add(cityList.get(j));
                    cityNameStr.add(cityList.get(j).getName());
                    List<DistrictModel> districtList = cityList.get(j).getDistrictList();
                    ArrayList<DistrictModel> options3Items_01_01 = new ArrayList<>();
                    ArrayList<IPickerViewData> options3Items_01_01Str = new ArrayList<>();
                    for (int k=0; k<districtList.size(); k++) {
                        // 遍历市下面所有区/县的数据
                        DistrictModel districtModel = new DistrictModel(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        // 区/县对于的邮编，保存到mZipcodeDatasMap
                        mZipcodeDatasMap.put(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        options3Items_01_01.add(districtList.get(k));//存放区
                        options3Items_01_01Str.add(new PickerViewData(districtList.get(k).getName()));//存放区
                    }
                    options3Items_01.add(options3Items_01_01);
                    options3Items_01Str.add(options3Items_01_01Str);
                    // 市-区/县的数据，保存到mDistrictDatasMap
                }
                mDistrictDatas.add(options3Items_01);
                mCityDatas.add(cityNames);
                mCityStr.add(cityNameStr);
                mDistrictStr.add(options3Items_01Str);
                // 省-市的数据，保存到mCitisDatasMap
//                mDistrictDatasMap.add(i, mCitisDatasMap);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {

        }
    }

    public ArrayList<String> provideProvince(){
        return mProvinceStr;
    }

    public ArrayList<ArrayList<String>> provideCities() {
        return mCityStr;
    }

    public ArrayList<ArrayList<ArrayList<IPickerViewData>>> provideCounties() {
        return mDistrictStr;
    }
    public Map<String, String> provideZipCode() {
        return mZipcodeDatasMap;
    }
}
