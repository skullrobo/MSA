package one.mstudio.qrbar.adapter;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;

import one.mstudio.qrbar.R;
import one.mstudio.qrbar.data.constant.Constants;
import one.mstudio.qrbar.utility.AppUtils;

/**
 * Created by Ashiq on 3/1/16.
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<String> mList;

    private ClickListener clickListener;

    public HistoryAdapter(Context context, ArrayList<String> list) {
        this.mContext = context;
        this.mList = list;
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }


    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new ViewHolder(view, viewType);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView result;
        private ImageButton copyButton;
        private ImageView actionIcon;

        public ViewHolder(View v, int viewType) {
            super(v);
            result = (TextView) v.findViewById(R.id.result);
            copyButton = (ImageButton) v.findViewById(R.id.copyButton);
            actionIcon = (ImageView) v.findViewById(R.id.actionIcon);

            result.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener != null) {
                        clickListener.onItemClicked(getLayoutPosition());
                    }
                }
            });

            copyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener != null) {
                        clickListener.onCopyClicked(getLayoutPosition());
                    }
                }
            });

            result.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (clickListener != null) {
                        clickListener.onItemLongClicked(getLayoutPosition());
                    }
                    return false;
                }
            });
        }

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String result = mList.get(position);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.result.setText(Html.fromHtml(result, Html.FROM_HTML_MODE_LEGACY));
        } else {
            holder.result.setText(Html.fromHtml(result));
        }
        holder.result.setMovementMethod(LinkMovementMethod.getInstance());

        int type = AppUtils.getResourceType(result);
        if (type == Constants.TYPE_TEXT) {
            holder.actionIcon.setImageResource(R.drawable.ic_plain_text);
        } else if (type == Constants.TYPE_WEB) {
            holder.actionIcon.setImageResource(R.drawable.ic_web);
        } else if (type == Constants.TYPE_YOUTUBE) {
            holder.actionIcon.setImageResource(R.drawable.ic_video);
        } else if (type == Constants.TYPE_PHONE) {
            holder.actionIcon.setImageResource(R.drawable.ic_call);
        } else if (type == Constants.TYPE_EMAIL) {
            holder.actionIcon.setImageResource(R.drawable.ic_email);
        } else {
            holder.actionIcon.setImageResource(R.drawable.ic_plain_text);
        }
    }

    @Override

    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public interface ClickListener {
        public void onCopyClicked(int position);
        public void onItemClicked(int position);
        public void onItemLongClicked(int position);
    }
}