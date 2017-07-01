package com.byteshaft.cellexchange.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.byteshaft.cellexchange.R;
import com.byteshaft.cellexchange.utils.Helpers;
import com.byteshaft.requests.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by fi8er1 on 01/07/2017.
 */

public class WelcomeFragment extends Fragment {

    View baseViewWelcomeFragment;
    ListView lvWelcomeFragmentDevices;
    DevicesListAdapter devicesListAdapter;

    ArrayList<Integer> devicesListIds;
    HashMap<Integer, ArrayList<String>> hashMapDevicesList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        baseViewWelcomeFragment = inflater.inflate(R.layout.layout_welcome_fragment, container, false);
        lvWelcomeFragmentDevices = baseViewWelcomeFragment.findViewById(R.id.lv_welcome_fragment_devices);

        sendRegistrationRequest();

        return baseViewWelcomeFragment;
    }

    private void sendRegistrationRequest() {
        HttpRequest request = new HttpRequest(getActivity());
        Helpers.showProgressDialog(getActivity(), "Fetching data");
        request.setOnReadyStateChangeListener(new HttpRequest.OnReadyStateChangeListener() {
            @Override
            public void onReadyStateChange(HttpRequest request, int readyState) {
                switch (readyState) {
                    case HttpRequest.STATE_DONE:
                        Helpers.dismissProgressDialog();
                        switch (request.getStatus()) {
                            case HttpURLConnection.HTTP_OK:
                                onDataFetchSuccess(request.getResponseText());
                                break;
                            default:
                                onDataFetchFailed(request.getResponseText());
                                break;
                        }
                }
            }
        });
        request.setOnErrorListener(new HttpRequest.OnErrorListener() {
            @Override
            public void onError(HttpRequest request, int readyState, short error, Exception exception) {}
        });
        request.open("GET", "http://cxe.naeemawan.com/api/v1/feeds?_format=json");
        request.setRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
        request.send();
    }

    private void onDataFetchSuccess(String response) {
        Toast.makeText(getActivity(), "Data fetch successful", Toast.LENGTH_SHORT).show();
        Log.i("data", "" + response);
        devicesListIds = new ArrayList<>();
        hashMapDevicesList = new HashMap<>();
        try {
            JSONArray jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
                int id = jsonObject.getInt("id");
                Log.e("objecgt", "" + jsonObject);
                if (!devicesListIds.contains(id)) {
                    devicesListIds.add(id);
                    ArrayList<String> tempArrayListData = new ArrayList<>();
                    tempArrayListData.add(jsonObject.getString("ad_title"));
                    tempArrayListData.add(jsonObject.getString("model_number"));
                    tempArrayListData.add(jsonObject.getString("stock_type"));
                    tempArrayListData.add(jsonObject.getString("color"));
                    tempArrayListData.add(jsonObject.getString("storage_capacity"));
                    tempArrayListData.add(jsonObject.getString("product_condition"));
                    tempArrayListData.add(jsonObject.getString("specification"));
                    hashMapDevicesList.put(id, tempArrayListData);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        devicesListAdapter = new DevicesListAdapter(getActivity(), R.layout.row_list_devices,
                devicesListIds);
        lvWelcomeFragmentDevices.setAdapter(devicesListAdapter);
    }

    private void onDataFetchFailed(String response) {
        Toast.makeText(getActivity(), "Data fetch failed", Toast.LENGTH_SHORT).show();
        Log.e("failed", "" + response);
    }

    private class DevicesListAdapter extends ArrayAdapter<String> {

        ArrayList<Integer> arrayListIds;

        DevicesListAdapter(Context context, int resource, ArrayList<Integer> ids) {
            super(context, resource);
            arrayListIds = ids;
        }

        @Override
        public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater layoutInflater = getActivity().getLayoutInflater();
                convertView = layoutInflater.inflate(R.layout.row_list_devices, parent, false);
                viewHolder.tvDevicesTitle = convertView.findViewById(R.id.tv_lv_devices_title);
                viewHolder.tvDevicesModelNumber = convertView.findViewById(R.id.tv_lv_devices_model_number);
                viewHolder.tvDevicesStockType = convertView.findViewById(R.id.tv_lv_devices_stock_type);
                viewHolder.tvDevicesColor = convertView.findViewById(R.id.tv_lv_devices_color);
                viewHolder.tvDevicesStorageCapacity = convertView.findViewById(R.id.tv_lv_devices_storage_capacity);
                viewHolder.tvDevicesProductCondition = convertView.findViewById(R.id.tv_lv_devices_product_condition);
                viewHolder.tvDevicesSpecification = convertView.findViewById(R.id.tv_lv_devices_product_specification);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tvDevicesTitle.setText(hashMapDevicesList.get(arrayListIds.get(position)).get(0));
            viewHolder.tvDevicesModelNumber.setText("Model: " + hashMapDevicesList.get(arrayListIds.get(position)).get(1));
            viewHolder.tvDevicesStockType.setText("Stock Type: " + hashMapDevicesList.get(arrayListIds.get(position)).get(2));
            viewHolder.tvDevicesColor.setText("Color: " + hashMapDevicesList.get(arrayListIds.get(position)).get(3));
            viewHolder.tvDevicesStorageCapacity.setText("Storage: " + hashMapDevicesList.get(arrayListIds.get(position)).get(4));
            viewHolder.tvDevicesProductCondition.setText("Condition: " + hashMapDevicesList.get(arrayListIds.get(position)).get(5));
            viewHolder.tvDevicesSpecification.setText("Specification: " + hashMapDevicesList.get(arrayListIds.get(position)).get(6));
            return convertView;
        }

        @Override
        public int getCount() {
            return arrayListIds.size();
        }
    }

    private static class ViewHolder {
        TextView tvDevicesTitle;
        TextView tvDevicesModelNumber;
        TextView tvDevicesStockType;
        TextView tvDevicesColor;
        TextView tvDevicesStorageCapacity;
        TextView tvDevicesProductCondition;
        TextView tvDevicesSpecification;
    }
}