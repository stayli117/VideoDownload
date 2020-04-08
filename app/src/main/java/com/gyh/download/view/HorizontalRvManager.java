package com.gyh.download.view;

import android.graphics.Color;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gyh.download.R;
import com.gyh.download.bilibili.bibili.QualityData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by  yahuigao
 * Date: 2020/4/7
 * Time: 6:09 PM
 * Description:
 */
public final class HorizontalRvManager<T> {


    private final ArrayList<T> mDataList;
    private final HorAdapter mHorAdapter;
    private final HolderProvider<T> mHolderProvider;
    private final OnItemClickListener mItemClickListener;

    //单击事件
    private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(int position, View childView) {
            mItemClickListener.onItemClick(position, childView);
        }
    };


    public HorizontalRvManager(final RecyclerView recyclerView, HolderProvider<T> holderProvider, OnItemClickListener clickListener) {
        mHolderProvider = holderProvider;
        mItemClickListener = clickListener;
        mDataList = new ArrayList<>();
        initGes(recyclerView);
        mHorAdapter = new HorAdapter();
        initRv(recyclerView);

    }

    private void initRv(final RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), RecyclerView.HORIZONTAL, false));
        recyclerView.setAdapter(mHorAdapter);

    }

    private void initGes(final RecyclerView recyclerView) {
        final GestureDetector mGestureDetector = new GestureDetector(recyclerView.getContext(), new GestureDetector.SimpleOnGestureListener() {
            //单击事件
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                if (mOnItemClickListener != null) {
                    View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (childView != null) {
                        int position = recyclerView.getChildLayoutPosition(childView);
                        mOnItemClickListener.onItemClick(position, childView);
                        return true;
                    }
                }

                return super.onSingleTapUp(e);
            }
        });
        recyclerView.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                return mGestureDetector.onTouchEvent(e);
            }
        });
    }

    public void updateList(List<T> data) {
        mDataList.clear();
        mDataList.addAll(data);
        mHorAdapter.notifyDataSetChanged();
    }


    class HorAdapter extends RecyclerView.Adapter<HorHolder<T>> {
        @NonNull
        @Override
        public HorHolder<T> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new HorHolder<>(LayoutInflater.from(parent.getContext()).inflate(R.layout.hor_layout, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull HorHolder<T> holder, int position) {
            mHolderProvider.bindData(holder, mDataList.get(position));
        }

        @Override
        public int getItemCount() {
            return mDataList.size();
        }
    }

    public static class HorHolder<T> extends RecyclerView.ViewHolder {

        public TextView textView;

        HorHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_text);
        }
    }

    //单击事件接口
    public interface OnItemClickListener {
        public void onItemClick(int position, View childView);
    }


    public interface HolderProvider<T> {

        void bindData(HorHolder<T> holder, T t);
    }
}
