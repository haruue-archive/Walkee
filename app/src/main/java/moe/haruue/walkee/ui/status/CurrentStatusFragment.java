package moe.haruue.walkee.ui.status;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import moe.haruue.walkee.BuildConfig;
import moe.haruue.walkee.R;
import moe.haruue.walkee.ui.main.BaseFragmentInMainActivity;
import moe.haruue.walkee.ui.widget.StatisticsBarGraph;

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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        statusContainer = $(R.id.rl_status_current_status_container);
        statusTextView = $(R.id.tv_status_current_status_text);
        presenter = new CurrentStatusPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_status, container, false);
    }

    private void loadStatisticsGraphs() {
        StatisticsBarGraph timeGraph = (StatisticsBarGraph) getView().findViewWithTag(getString(R.string.tag_sg_time));
        // Generate test data
        List<StatisticsBarGraph.Item> times = new ArrayList<>();
        times.add(new StatisticsBarGraph.Item("六", 9016));
        times.add(new StatisticsBarGraph.Item("日", 17361));
        times.add(new StatisticsBarGraph.Item("一", 9844));
        times.add(new StatisticsBarGraph.Item("二", 16963));
        times.add(new StatisticsBarGraph.Item("三", 8690));
        times.add(new StatisticsBarGraph.Item("四", 4181));
        times.add(new StatisticsBarGraph.Item("五", 4387));
        timeGraph.setData(times);
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
        loadStatisticsGraphs();
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
