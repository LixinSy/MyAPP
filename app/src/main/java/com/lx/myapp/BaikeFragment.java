package com.lx.myapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BaikeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BaikeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BaikeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private GridView historySearch;
    private GridView hotSearch;
    private TextView keyInput;
    private Context context;

    private String [] historyPlantNames = new String[]{"红花檵木", "郁金香","光叶子花","金盏菊","紫荆","爪哇树角苔"};
    private List<Map<String, Object>> historyPlantList;
    private SimpleAdapter historyPlantAdapter;
    private String [] hotPlantNames = new String[]{"日本晚樱", "天竺葵","光叶子花","金盏菊","紫荆","迎春花"};
    private List<Map<String, Object>> hotPlantList;
    private SimpleAdapter hotPlantAdapter;

    public BaikeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BaikeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BaikeFragment newInstance(String param1, String param2) {
        BaikeFragment fragment = new BaikeFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_baike, container, false);
        context = getActivity().getApplication();
        initView(view);
        return view;
    }

    private void initView(View view){
        historyPlantList = new LinkedList<Map<String, Object>>();
        for (int i=0;i<historyPlantNames.length;i++){
            Map<String, Object> showitem = new HashMap<String, Object>();
            showitem.put("name", historyPlantNames[i]);
            historyPlantList.add(showitem);
        }
        historyPlantAdapter = new SimpleAdapter(context,  historyPlantList,R.layout.plant_name_item,
                new String[]{"name"},new int[]{R.id.plant_name});

        hotPlantList = new LinkedList<Map<String, Object>>();
        for (int i=0;i<hotPlantNames.length;i++){
            Map<String, Object> showitem = new HashMap<String, Object>();
            showitem.put("name", hotPlantNames[i]);
            hotPlantList.add(showitem);
        }
        hotPlantAdapter = new SimpleAdapter(context,  hotPlantList,R.layout.plant_name_item,
                new String[]{"name"},new int[]{R.id.plant_name});

        historySearch  = view.findViewById(R.id.his_sear_grid);
        hotSearch  = view.findViewById(R.id.hot_plant_grid);
        keyInput = view.findViewById(R.id.key_input);

        historySearch.setAdapter(historyPlantAdapter);
        hotSearch.setAdapter(hotPlantAdapter);

        historySearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = view.findViewById(R.id.plant_name);
                String content = textView.getText().toString();
                Toast.makeText(context, "你点击了~" + position + "~项 "
                        +content, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(context,PlantActivity.class);
                Bundle data = new Bundle();
                data.putString("plant_key",content);
                intent.putExtra("plant_data",data);
                startActivity(intent);
            }
        });
        hotSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = view.findViewById(R.id.plant_name);
                String content = textView.getText().toString();
                Toast.makeText(context, "你点击了~" + position + "~项 "
                        +content, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(),PlantActivity.class);
                Bundle data = new Bundle();
                data.putString("plant_key",content);
                intent.putExtra("plant_data",data);
                startActivity(intent);
            }
        });

        keyInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchKeyActivity.class);
                startActivity(intent);
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
