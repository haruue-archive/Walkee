package moe.haruue.walkee.model;

import android.content.pm.ApplicationInfo;

import moe.haruue.walkee.App;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class ApplicationCheckedInfo extends ApplicationInfo implements Comparable<ApplicationCheckedInfo> {

    public boolean checked = false;

    public ApplicationCheckedInfo(ApplicationInfo orig) {
        super(orig);
        this.name = App.getInstance().getPackageManager().getApplicationLabel(orig).toString();
    }

    @Override
    public int compareTo(ApplicationCheckedInfo o) {
        if (this.checked & !o.checked) {
            return -1;
        } else if (!this.checked & o.checked) {
            return 1;
        } else {
            if (this.name == null) {
                this.name = this.packageName;
            }
            if (o.name == null) {
                o.name = o.packageName;
            }
            return this.name.compareToIgnoreCase(o.name);
        }
    }
}
