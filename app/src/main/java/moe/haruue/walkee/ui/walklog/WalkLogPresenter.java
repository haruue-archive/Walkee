package moe.haruue.walkee.ui.walklog;

import com.base.basepedo.pojo.StepData;

import java.util.ArrayList;
import java.util.List;

import moe.haruue.walkee.BuildConfig;
import moe.haruue.walkee.data.log.func.QueryAllLogFunc;
import moe.haruue.walkee.data.step.func.QueryStepFunc;
import moe.haruue.walkee.ui.base.BasePresenter;
import moe.haruue.walkee.ui.base.BaseSubscriber;
import moe.haruue.walkee.ui.widget.StatisticsBarGraph;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class WalkLogPresenter implements BasePresenter {

    public static final String TAG = "WalkLogP";
    public static final boolean DEBUG = BuildConfig.DEBUG;

    private WalkLogFragment fragment;

    public WalkLogPresenter(WalkLogFragment fragment) {
        this.fragment = fragment;
    }

    public void requestStepStatisticsData() {
        Observable.just(0)
                .map(new QueryStepFunc())
                .map(steps -> {
                    List<StatisticsBarGraph.Item> items = new ArrayList<>();
                    for (StepData s: steps) {
                        items.add(new StatisticsBarGraph.Item(s.getToday(), Integer.parseInt(s.getStep())));
                    }
                    return items;
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<List<StatisticsBarGraph.Item>>(TAG, "requestStepStatisticsData") {
                    @Override
                    public void onNext(List<StatisticsBarGraph.Item> items) {
                        fragment.onLoadStepStatisticsGraphs(items);
                    }
                });
    }

    public void requestTimeStatisticsData() {
        Observable.just(0)
                .map(new QueryAllLogFunc())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<List<StatisticsBarGraph.Item>>(TAG, "requestTimeStatisticsData") {
                    @Override
                    public void onNext(List<StatisticsBarGraph.Item> items) {
                        fragment.onLoadTimeStatisticsGraphs(items);
                    }
                });
    }

    @Override
    public void start() {

    }

    public void stop() {

    }
}
