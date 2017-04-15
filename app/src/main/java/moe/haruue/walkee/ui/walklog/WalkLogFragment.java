package moe.haruue.walkee.ui.walklog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jude.utils.JTimeTransform;

import java.util.List;

import moe.haruue.walkee.R;
import moe.haruue.walkee.config.Const;
import moe.haruue.walkee.ui.main.BaseFragmentInMainActivity;
import moe.haruue.walkee.ui.widget.StatisticsBarGraph;
import moe.haruue.walkee.util.KVUtils;

/**
 * 步行记录
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class WalkLogFragment extends BaseFragmentInMainActivity {

    WalkLogPresenter presenter;
    TextView timeGraphDayAverageTextView;
    TextView timeGraphTodayTextView;
    TextView timeGraphLastTimeTextView;
    TextView stepGraphDayAverageTextView;
    TextView stepGraphTodayTextView;
    TextView stepGraphLastTimeTextView;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new WalkLogPresenter(this);
        timeGraphDayAverageTextView = $(R.id.tv_walklog_statistics_time_graph_subtitle);
        timeGraphTodayTextView = $(R.id.tv_walklog_statistics_time_graph_today);
        timeGraphLastTimeTextView = $(R.id.tv_walklog_statistics_time_graph_recent);
        stepGraphDayAverageTextView = $(R.id.tv_walklog_statistics_step_graph_subtitle);
        stepGraphTodayTextView = $(R.id.tv_walklog_statistics_step_graph_today);
        stepGraphLastTimeTextView = $(R.id.tv_walklog_statistics_step_graph_recent);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_walklog, container, false);
    }

    @SuppressWarnings("ConstantConditions")
    public void onLoadStepStatisticsGraphs(List<StatisticsBarGraph.Item> items) {
        int sum = 0;
        for (StatisticsBarGraph.Item i: items) {
            sum += (int) i.data;
        }
        if (items.size() != 0) {
            stepGraphDayAverageTextView.setText(getString(R.string.format_day_average, (int) (sum / items.size())));
            stepGraphTodayTextView.setText(getString(R.string.format_step, (int) items.get(items.size() - 1).data));
        } else {
            stepGraphDayAverageTextView.setText(getString(R.string.format_day_average, 0));
            stepGraphTodayTextView.setText(getString(R.string.format_step, 0));
        }
        long lastTime = (Long) KVUtils.get(getContext(), Const.KVKEY_LAST_STEP_TIMESTAMP, System.currentTimeMillis());
        stepGraphLastTimeTextView.setText(new JTimeTransform(lastTime / 1000).toString(new JTimeTransform.RecentDateFormat()));
        StatisticsBarGraph stepGraph = (StatisticsBarGraph) getView().findViewWithTag(getString(R.string.tag_sg_step));
        stepGraph.setData(items);
    }

    @SuppressWarnings("ConstantConditions")
    public void onLoadTimeStatisticsGraphs(List<StatisticsBarGraph.Item> items) {
        int sum = 0;
        for (StatisticsBarGraph.Item i: items) {
            sum += (int) i.data;
        }
        if (items.size() != 0) {
            timeGraphDayAverageTextView.setText(getString(R.string.format_day_average, (int) (sum / items.size())));
            timeGraphTodayTextView.setText(getString(R.string.format_times, (int) items.get(items.size() - 1).data));
        } else {
            timeGraphDayAverageTextView.setText(getString(R.string.format_day_average, 0));
            timeGraphTodayTextView.setText(getString(R.string.format_times, 0));
        }
        long lastTime = (Long) KVUtils.get(getContext(), Const.KVKEY_LAST_LOG_TIMESTAMP, System.currentTimeMillis());
        timeGraphLastTimeTextView.setText(new JTimeTransform(lastTime / 1000).toString(new JTimeTransform.RecentDateFormat()));
        StatisticsBarGraph timeGraph = (StatisticsBarGraph) getView().findViewWithTag(getString(R.string.tag_sg_time));
        timeGraph.setData(items);
    }

    @Override
    public void onResume() {
        super.onResume();
        getMainActivity().setNavigationMenuItemChecked(R.id.item_walk_log);
        presenter.start();
        presenter.requestStepStatisticsData();
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
