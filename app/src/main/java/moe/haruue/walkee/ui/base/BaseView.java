package moe.haruue.walkee.ui.base;

/**
 * Base View for MVP Framework - View <br>
 *     implement this interface in all View of MVP
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public interface BaseView<P extends BasePresenter> {

    void setPresenter(P presenter);

}
