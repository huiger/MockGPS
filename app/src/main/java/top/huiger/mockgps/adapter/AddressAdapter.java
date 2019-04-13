package top.huiger.mockgps.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huige.library.interfaces.OnItemClickListener;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import top.huiger.mockgps.R;
import top.huiger.mockgps.entity.LocationEntity;
import top.huiger.mockgps.utils.SpannableStringUtils;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/4/12 3:30 PM.
 *  Email  : zhihuiemail@163.com
 *  Desc   :
 * </pre>
 */
public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {

    private Context mContext;
    private List<LocationEntity> mData;
    private OnItemClickListener mOnItemClickListener;

    public AddressAdapter(Context ctx, List<LocationEntity> list) {
        mContext = ctx;
        this.mData = list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLayout = LayoutInflater.from(mContext).inflate(R.layout.adapter_address_item, null);
        return new ViewHolder(itemLayout);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LocationEntity locationEntity = mData.get(position);
        holder.llItem.setTag(position);
        holder.tvAddress.setText(locationEntity.getAddress());
        holder.tvCount.setText(
                SpannableStringUtils.getBuilder("使用次数：")
                        .append(String.valueOf(locationEntity.getUsedCount()))
                            .setForegroundColor(Color.BLUE)
                        .create()
        );
        holder.tvTime.setText("最后一次使用时间：" + formatTime(locationEntity.getLastTime()));

    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.ll_item)
        LinearLayout llItem;
        @Bind(R.id.tv_address)
        TextView tvAddress;
        @Bind(R.id.tv_count)
        TextView tvCount;
        @Bind(R.id.tv_time)
        TextView tvTime;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.ll_item)
        public void onItemClick(View v) {
            int position = (int) v.getTag();
            if(mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(position, v);
            }
        }
    }

    private String formatTime(long time) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(time);
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener){
        this.mOnItemClickListener = itemClickListener;
    }

}
