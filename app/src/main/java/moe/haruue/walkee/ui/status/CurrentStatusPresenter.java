package moe.haruue.walkee.ui.status;

import moe.haruue.walkee.App;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

class CurrentStatusPresenter {

    CurrentStatusFragment fragment;

    CurrentStatusPresenter(CurrentStatusFragment fragment) {
        this.fragment = fragment;
    }

    public void changeUsername(String newUsername) {
        App.getInstance().getUser().username = newUsername;
        App.getInstance().saveUser();
        fragment.getMainActivity().refreshNavigationHeader();
    }

}
