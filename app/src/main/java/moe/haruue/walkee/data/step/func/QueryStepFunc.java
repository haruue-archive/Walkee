package moe.haruue.walkee.data.step.func;

import com.base.basepedo.pojo.StepData;
import com.base.basepedo.service.StepService;
import com.base.basepedo.utils.DbUtils;

import java.util.List;

import moe.haruue.walkee.App;
import rx.functions.Func1;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class QueryStepFunc implements Func1<Integer, List<StepData>> {
    @Override
    public List<StepData> call(Integer integer) {
        DbUtils.createDb(App.getInstance(), StepService.DB_NAME_BASEPEDO);
        return DbUtils.getQueryAll(StepData.class);
    }
}
