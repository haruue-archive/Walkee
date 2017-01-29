package moe.haruue.walkee.ui.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import moe.haruue.walkee.ui.widget.NavigationBarMarginView;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

@SuppressWarnings({"WeakerAccess", "UnusedParameters"})
public abstract class NavigationBarMarginRecyclerAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final static int VIEW_TYPE_NAVIGATION_MARGIN = -213542;

    @Override
    public final int getItemCount() {
        return getArrayItemCount() + 1;
    }

    public abstract int getArrayItemCount();

    @Override
    public final int getItemViewType(int position) {
        if (position == getArrayItemCount()) {
            return VIEW_TYPE_NAVIGATION_MARGIN;
        } else {
            return getArrayItemViewType(position);
        }
    }

    public int getArrayItemViewType(int position) {
        return 0;
    }

    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_NAVIGATION_MARGIN) {
            return new NavigationBarMarginViewHolder(parent);
        } else {
            return onCreateArrayViewHolder(parent, viewType);
        }
    }

    public abstract VH onCreateArrayViewHolder(ViewGroup parent, int viewType);

    @SuppressWarnings("unchecked")
    @Override
    public final void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position < getArrayItemCount()) {
            onBindArrayViewHolder((VH) holder, position);
        }
    }

    public abstract void onBindArrayViewHolder(VH holder, int position);

    public class NavigationBarMarginViewHolder extends RecyclerView.ViewHolder {

        public NavigationBarMarginViewHolder(View itemView) {
            super(new NavigationBarMarginView(itemView.getContext()));
        }

    }

}
