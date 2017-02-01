package moe.haruue.walkee.ui.status;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import moe.haruue.walkee.R;
import moe.haruue.walkee.ui.main.BaseFragmentInMainActivity;
import moe.haruue.walkee.ui.widget.StatisticsBarGraph;

/**
 * 当前状态 & MainFragment
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class CurrentStatusFragment extends BaseFragmentInMainActivity {

    CurrentStatusPresenter presenter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new CurrentStatusPresenter(this);
        presenter.start();
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

    @Override
    public void onResume() {
        super.onResume();
        getMainActivity().setNavigationMenuItemChecked(R.id.item_current_status);
        loadStatisticsGraphs();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        presenter = null;
    }
}
