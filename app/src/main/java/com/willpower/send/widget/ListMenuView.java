package com.willpower.send.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.willpower.send.R;

import java.util.List;

/**
 * Created by Administrator on 2018/10/25.
 */

public class ListMenuView extends TabLayout implements IMenuView {

    private List<HeaderBean> headers;

    private PopupWindow dropWindow;

    private RecyclerView mList;

    private Adapter adapter;

    public ListMenuView(Context context) {
        super(context);
        init();
    }

    public ListMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ListMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setSelectedTabIndicatorHeight(0);
        setBackgroundColor(Color.WHITE);
    }

    public void initMenu(List<HeaderBean> headers) {
        this.headers = headers;

        for (int i = 0; i < headers.size(); i++) {
            addTab(newTab().setText(headers.get(i).getTitle()));
        }

        View view = LayoutInflater.from(getContext()).inflate(R.layout.menu_list, null);
        dropWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mList = view.findViewById(R.id.menuList);
        mList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        adapter = new Adapter();
        mList.setAdapter(adapter);
        mList.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                getTabAt(getSelectedTabPosition()).setText(((Adapter) adapter).getItem(position));
                hideWindow();
            }
        });
        dropWindow.setBackgroundDrawable(new ColorDrawable());
        dropWindow.setFocusable(true);
        addOnTabSelectedListener(selectedListener);
    }


    OnTabSelectedListener selectedListener = new OnTabSelectedListener() {
        @Override
        public void onTabSelected(Tab tab) {
            adapter.setNewData(headers.get(tab.getPosition()).getmData());
            showWindow();
        }

        @Override
        public void onTabUnselected(Tab tab) {
        }

        @Override
        public void onTabReselected(Tab tab) {
            adapter.setNewData(headers.get(tab.getPosition()).getmData());
            showWindow();
        }
    };


    private void showWindow() {
        if (dropWindow != null && !dropWindow.isShowing()) {
            dropWindow.showAsDropDown(this);
        }
    }

    private void hideWindow() {
        if (dropWindow != null && dropWindow.isShowing()) {
            dropWindow.dismiss();
        }
    }

    static class Adapter extends BaseQuickAdapter<String, BaseViewHolder> {
        public Adapter() {
            super(R.layout.item_menu);
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
            helper.setText(R.id.menuContent, item);
            helper.addOnClickListener(R.id.menuContent);
        }
    }

}
