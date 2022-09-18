package com.example.teamwork;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class LuntanAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    public static final int TYPE_Normal = 0;
    public static final int TYPE_Footer = 1;
    private LayoutInflater mInflater;
    private Context context;
    private List<LuntanBean> luntanlist=new ArrayList<>();
    public LuntanAdapter(Context context){
        this.context=context;
        mInflater=LayoutInflater.from(context);
    }
    public void setLuntanlist(List<LuntanBean> l){
        luntanlist=l;
    }
    @Override
    public int getItemViewType(int position) {
        if(position==getItemCount()-1)return TYPE_Footer;
        else return TYPE_Normal;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==TYPE_Normal) {
            View view = mInflater.inflate(R.layout.luntanitem, parent, false);
            return new LuntanAdapter.ViewHolder(view);
        }
        else if(viewType==TYPE_Footer){
            View view=mInflater.inflate(R.layout.loadingfooter,parent,false);
            return new LuntanAdapter.ViewHolder(view);
        }
        else return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position)==TYPE_Normal) {
            ((ViewHolder)holder).ftnicneg.setText(luntanlist.get(position).getZuozhe().substring(5));
            ((ViewHolder)holder).ftneirong.setText(luntanlist.get(position).getTitle());
            ((ViewHolder)holder).ftnum.setText(luntanlist.get(position).getLooknum());
        }
        if (mItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int adapterPosition = holder.getAdapterPosition();
                    mItemClickListener.onItemClick(adapterPosition, v);
                }
            });
        }
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        TextView ftnicneg;
        TextView ftneirong;
        TextView ftnum;
        ViewHolder(View view){
            super(view);
            ftnicneg=view.findViewById(R.id.fatienicheng);
            ftneirong=view.findViewById(R.id.fatietext);
            ftnum=view.findViewById(R.id.fatienum);
        }
    }
    @Override
    public int getItemCount() {
        if(luntanlist==null) return 0;
        else return luntanlist.size();
    }
    private OnItemClickListener mItemClickListener;
    public interface OnItemClickListener {
        void onItemClick(int position, View v);
    }

    void setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }
}
