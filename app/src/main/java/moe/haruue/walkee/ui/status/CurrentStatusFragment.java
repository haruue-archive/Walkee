package moe.haruue.walkee.ui.status;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jude.utils.JTimeTransform;

import java.util.List;

import moe.haruue.walkee.BuildConfig;
import moe.haruue.walkee.R;
import moe.haruue.walkee.config.Const;
import moe.haruue.walkee.ui.main.BaseFragmentInMainActivity;
import moe.haruue.walkee.ui.widget.StatisticsBarGraph;
import moe.haruue.walkee.util.SPUtils;

/**
 * 当前状态 & MainFragment
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class CurrentStatusFragment extends BaseFragmentInMainActivity {

    public static final String TAG = "CurrentStatusF";
    public static final boolean DEBUG = BuildConfig.DEBUG;

    CurrentStatusPresenter presenter;

    RelativeLayout statusContainer;
    TextView statusTextView;
    TextView timeGraphDayAverageTextView;
    TextView timeGraphTodayTextView;
    TextView timeGraphLastTimeTextView;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        statusContainer = $(R.id.rl_status_current_status_container);
        statusTextView = $(R.id.tv_status_current_status_text);
        timeGraphDayAverageTextView = $(R.id.tv_status_statistics_time_graph_subtitle);
        timeGraphTodayTextView = $(R.id.tv_status_statistics_time_graph_today);
        timeGraphLastTimeTextView = $(R.id.tv_status_statistics_time_graph_recent);
        presenter = new CurrentStatusPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_status, container, false);
    }

    @SuppressWarnings("ConstantConditions")
    public void onLoadTimeStatisticsGraphs(List<StatisticsBarGraph.Item> data) {
        int sum = 0;
        for (StatisticsBarGraph.Item i: data) {
            sum += (int) i.data;
        }
        if (data.size() != 0) {
            timeGraphDayAverageTextView.setText(getString(R.string.format_day_average, (int) (sum / data.size())));
            timeGraphTodayTextView.setText(getString(R.string.format_times, (int) data.get(data.size() - 1).data));
        } else {
            timeGraphDayAverageTextView.setText(getString(R.string.format_day_average, 0));
            timeGraphTodayTextView.setText(getString(R.string.format_times, 0));
        }
        long lastTime = (Long) SPUtils.get(getContext(), Const.SPKEY_LAST_LOG_TIMESTAMP, System.currentTimeMillis());
        timeGraphLastTimeTextView.setText(new JTimeTransform(lastTime / 1000).toString(new JTimeTransform.RecentDateFormat()));
        StatisticsBarGraph timeGraph = (StatisticsBarGraph) getView().findViewWithTag(getString(R.string.tag_sg_time));
        timeGraph.setData(data);
    }

    @SuppressWarnings("deprecation")
    public void setStatusWalk() {
        if (DEBUG) {
            Log.d(TAG, "setStatusWalk");
        }
        try {
            statusContainer.setBackgroundColor(getMainActivity().getResources().getColor(R.color.bg_sc_card_status_walk));
            statusTextView.setText(R.string.walk);
        } catch (Exception ignored) {

        }
    }

    @SuppressWarnings("deprecation")
    public void setStatusStand() {
        if (DEBUG) {
            Log.d(TAG, "setStatusStand");
        }
        try {
            statusContainer.setBackgroundColor(getMainActivity().getResources().getColor(R.color.bg_sc_card_status_stand));
            statusTextView.setText(R.string.stand);
        } catch (Exception ignored) {

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getMainActivity().setNavigationMenuItemChecked(R.id.item_current_status);
        presenter.start();
        presenter.requestTimeStatisticsData();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.stop();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        presenter = null;
    }
}
