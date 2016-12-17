package moe.haruue.walkee.ui.base;


/**
 * Base Present of MVP Framework - Contract (Bind) <br>
 *     Empty interface, only for a example of all Contract
 *
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

interface BaseContract {
    interface View extends BaseView<Presenter> {
    }

    interface Presenter extends BasePresenter {
    }
}
