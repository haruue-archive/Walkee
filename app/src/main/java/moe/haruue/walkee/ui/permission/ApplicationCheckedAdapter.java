package moe.haruue.walkee.ui.permission;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

import moe.haruue.walkee.App;
import moe.haruue.walkee.R;
import moe.haruue.walkee.model.ApplicationCheckedInfo;
import moe.haruue.walkee.ui.base.NavigationBarMarginRecyclerAdapter;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class ApplicationCheckedAdapter extends NavigationBarMarginRecyclerAdapter<ApplicationCheckedAdapter.ViewHolder> {

    private List<ApplicationCheckedInfo> list;
    private OnSwitchListener listener;

    @Override
    public int getArrayItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    @Override
    public ViewHolder onCreateArrayViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    @Override
    public void onBindArrayViewHolder(ViewHolder holder, int position) {
        ApplicationCheckedInfo i = list.get(position);
        holder.nameView.setText(i.name);
        holder.iconView.setImageDrawable(App.getInstance().getPackageManager().getApplicationIcon(i));
        holder.enableSwitch.setChecked(i.checked);
        holder.enableSwitch.setOnClickListener(v -> {
            if (this.listener != null) {
                this.listener.onSwitch(i);
            }
        });
    }

    public void setData(List<ApplicationCheckedInfo> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void setOnSwitchListener(OnSwitchListener listener) {
        this.listener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView iconView;
        TextView nameView;
        Switch enableSwitch;

        ViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_application_list_checkable, parent, false));
            iconView = (ImageView) itemView.findViewById(R.id.iv_item_application_list_icon);
            nameView = (TextView) itemView.findViewById(R.id.tv_item_application_list_name);
            enableSwitch = (Switch) itemView.findViewById(R.id.sw_item_application_list_switch);
        }
    }

    interface OnSwitchListener {
        void onSwitch(ApplicationCheckedInfo i);
    }

}
