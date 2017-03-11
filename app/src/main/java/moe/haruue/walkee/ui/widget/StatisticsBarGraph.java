package moe.haruue.walkee.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import moe.haruue.walkee.BuildConfig;
import moe.haruue.walkee.R;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class StatisticsBarGraph extends FrameLayout {

    public static final String TAG = "StatisticsBarGraph";
    public static final boolean DEBUG = BuildConfig.DEBUG;

    private int textColor;
    private Drawable barBackground;
    private int hrColor;
    private float textSize;
    private float barWidth;

    private RecyclerView recycler;
    private Adapter adapter;

    public StatisticsBarGraph(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.StatisticsBarGraph);
        textColor = t.getColor(R.styleable.StatisticsBarGraph_textColor, 0x00000000);
        hrColor = t.getColor(R.styleable.StatisticsBarGraph_hrColor, 0x00000000);
        barBackground = t.getDrawable(R.styleable.StatisticsBarGraph_barBackground);
        textSize = t.getDimension(R.styleable.StatisticsBarGraph_textSize, 17);
        barWidth = t.getDimension(R.styleable.StatisticsBarGraph_barWidth, 30);
        t.recycle();
        recycler = new RecyclerView(context, attrs);
        addView(recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycler.setLayoutManager(linearLayoutManager);
        adapter = new Adapter();
        recycler.setAdapter(adapter);
    }

    public void setData(List<Item> data) {
        adapter.setData(data);
    }

    private class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

        List<Item> items = new ArrayList<>(0);

        public void setData(List<Item> items) {
            this.items = items;
            // find the max
            double max = 0;
            for (Item i: items) {
                if (max < i.data) {
                    max = i.data;
                }
            }
            // refresh height
            max += max * 0.05;
            for (Item i: items) {
                i.height = i.data / max;
            }
            // refresh view
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(parent);
        }

        @SuppressWarnings("deprecation")
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Item i = items.get(position);
            holder.weekday.setText(i.name.split("-")[2]);
            holder.weekday.setTextColor(textColor);
            holder.weekday.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            holder.hr.setBackgroundColor(hrColor);
            //holder.bar.setBackgroundDrawable(barBackground);
            holder.bar.setProgress((int) (i.height * holder.bar.getMax()));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView weekday;
            View hr;
            ProgressBar bar;

            ViewHolder(ViewGroup parent) {
                super(LayoutInflater.from(getContext()).inflate(R.layout.item_stat_graph_bar, parent, false));
                weekday = (TextView) itemView.findViewById(R.id.tv_stat_graph_bar_weekday);
                hr = itemView.findViewById(R.id.hr_stat_graph_bar);
                bar = (ProgressBar) itemView.findViewById(R.id.bar_stat_graph_bar);
            }
        }

    }

    public static class Item {
        public String name;
        public double data;
        double height;

        public Item(String name, double data) {
            this.name = name;
            this.data = data;
        }
    }

}
