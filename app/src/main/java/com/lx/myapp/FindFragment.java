package com.lx.myapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FindFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FindFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FindFragment extends Fragment implements View.OnTouchListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private Context context;

    private ImageHandler mHandler = new ImageHandler(new WeakReference<FindFragment>(this));
    private ViewPager mViewPager;
    // 自定义轮播图的资源
    private int[] mImageResIds = {R.drawable.img1, R.drawable.img2, R.drawable.img3, R.drawable.img4, R.drawable.img5};
    // 放轮播图片的ImageView 的list
    private List<ImageView> mImageList = new ArrayList<ImageView>();
    // 放圆点的View的list
    private List<View> mDotList = new ArrayList<View>();

    public FindFragment() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FindFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FindFragment newInstance(String param1, String param2) {
        FindFragment fragment = new FindFragment();
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
        View view = inflater.inflate(R.layout.fragment_find, container, false);
        context = getActivity().getApplication();
        initView(view);
        return view;
    }

    private void initView(View view){
        mViewPager = view.findViewById(R.id.view_pager);
        LinearLayout dotLayout = view.findViewById(R.id.dotLayout);
        dotLayout.removeAllViews();
        for (int i = 0; i < mImageResIds.length; i++) {
            ImageView imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            // view.setTag(mImageResId[i]);
            imageView.setImageResource(mImageResIds[i]);
            mImageList.add(imageView);

            View dotView = new View(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.dot_width),
                    getResources().getDimensionPixelSize(R.dimen.dot_width));
            params.setMargins(4, 0, 4, 0);
            dotView.setLayoutParams(params);
//          if (i == 0) {
//              dotView.setBackgroundResource(R.mipmap.af_01_icon_greencircle720);
//          } else {
//              dotView.setBackgroundResource(R.mipmap.af_01_icon_graycircle720);
//          }
            dotLayout.addView(dotView);
            mDotList.add(dotView);
        }
        mViewPager.setAdapter(new ImageAdapter(mImageList));
        mViewPager.setOnPageChangeListener(new PageChangeListener());
        mViewPager.setFocusable(true);
        mViewPager.setCurrentItem(Integer.MAX_VALUE / 2);// 默认在中间，使用户看不到边界
        mViewPager.setOnTouchListener(this);
        // 开始轮播效果
        mHandler.sendEmptyMessageDelayed(ImageHandler.MSG_UPDATE_IMAGE, ImageHandler.MSG_DELAY);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mHandler.sendEmptyMessage(ImageHandler.MSG_KEEP_SILENT);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mHandler.sendEmptyMessageDelayed(ImageHandler.MSG_UPDATE_IMAGE, ImageHandler.MSG_DELAY);
                break;
        }
        return false;
    }

    private class PageChangeListener implements ViewPager.OnPageChangeListener {

        // 配合Adapter的currentItem字段进行设置。
        @Override
        public void onPageSelected(int position) {
            mHandler.sendMessage(Message.obtain(mHandler, ImageHandler.MSG_PAGE_CHANGED, position, 0));
        }

        @Override
        public void onPageScrolled(int position, float arg1, int arg2) {
        }

        // 覆写该方法实现轮播效果的暂停和恢复
        @Override
        public void onPageScrollStateChanged(int state) {
            switch (state) {
                case ViewPager.SCROLL_STATE_DRAGGING:
                    mHandler.sendEmptyMessage(ImageHandler.MSG_KEEP_SILENT);
                    break;
                case ViewPager.SCROLL_STATE_IDLE:
                    mHandler.sendEmptyMessageDelayed(ImageHandler.MSG_UPDATE_IMAGE, ImageHandler.MSG_DELAY);
                    break;
                default:
                    break;
            }
        }
    }

    private class ImageAdapter extends PagerAdapter {
        private List<ImageView> viewlist;

        public ImageAdapter(List<ImageView> viewlist) {
            this.viewlist = viewlist;
        }

        @Override
        public int getCount() {
            // 设置成最大，使用户看不到边界
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // Warning：不要在这里调用removeView
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // 对ViewPager页号求模取出View列表中要显示的项
            position %= viewlist.size();
            if (position < 0) {
                position = viewlist.size() + position;
            }
            ImageView view = viewlist.get(position);
            // 如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
            ViewParent vp = view.getParent();
            if (vp != null) {
                ViewGroup parent = (ViewGroup) vp;
                parent.removeView(view);
            }
            container.addView(view);
            // 此处可添加监听事件
            return view;
        }
    }

    private static class ImageHandler extends Handler {
        /**
         * 请求更新显示的View。
         */
        protected static final int MSG_UPDATE_IMAGE = 1;
        /**
         * 请求暂停轮播。
         */
        protected static final int MSG_KEEP_SILENT = 2;
        /**
         * 请求恢复轮播。
         */
        protected static final int MSG_BREAK_SILENT = 3;
        /**
         * 记录最新的页号，当用户手动滑动时需要记录新页号，否则会使轮播的页面出错。
         * 例如当前如果在第一页，本来准备播放的是第二页，而这时候用户滑动到了末页，
         * 则应该播放的是第一页，如果继续按照原来的第二页播放，则逻辑上有问题。
         */
        protected static final int MSG_PAGE_CHANGED = 4;

        // 轮播间隔时间
        protected static final long MSG_DELAY = 3000;

        //         使用弱引用避免Handler泄露.这里的泛型参数可以不是Activity，也可以是Fragment等
        private WeakReference<FindFragment> weakReference;
        private int currentItem = 0;

        // private boolean isOnce = true;

        protected ImageHandler(WeakReference<FindFragment> wk) {
            weakReference = wk;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final FindFragment mainActivity = weakReference.get();
            if (mainActivity == null) {
                // Activity已经回收，无需再处理UI了
                return;
            }
            // 第一次不删重复的消息
            if (currentItem != 0) {
                // 检查消息队列并移除未发送的消息，这主要是避免在复杂环境下消息出现重复等问题。
                if (mainActivity.mHandler.hasMessages(MSG_UPDATE_IMAGE)) {
                    mainActivity.mHandler.removeMessages(MSG_UPDATE_IMAGE);
                }
            }
            switch (msg.what) {
                case MSG_UPDATE_IMAGE:
                    currentItem++;
                    mainActivity.mViewPager.setCurrentItem(currentItem);
                    // 准备下次播放
                    mainActivity.mHandler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
                    break;
                case MSG_KEEP_SILENT:
                    // 只要不发送消息就暂停了
                    break;
                case MSG_BREAK_SILENT:
                    mainActivity.mHandler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
                    break;
                case MSG_PAGE_CHANGED:
                    // 记录当前的页号，避免播放的时候页面显示不正确。
                    int position = msg.arg1;
                    int lastIndex = 0;
                    int index = position % mainActivity.mImageList.size();
                    for (int i = 0; i < mainActivity.mDotList.size(); i++) {
                        mainActivity.mDotList.get(i).setBackgroundResource(R.drawable.ic_dot);
                    }
                    if (mainActivity.mDotList.get(index) != null) {
                        mainActivity.mDotList.get(index).setBackgroundResource(R.drawable.ic_dot1);
                    }
                    lastIndex = index;
                    currentItem = position;
                    break;
                default:
                    break;
            }
        }
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
