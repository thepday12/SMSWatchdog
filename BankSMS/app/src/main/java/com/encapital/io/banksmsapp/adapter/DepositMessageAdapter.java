package com.encapital.io.banksmsapp.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.encapital.io.banksmsapp.R;
import com.encapital.io.banksmsapp.model.DepositEntity;
import com.encapital.io.banksmsapp.model.DepositMessageContent;

import java.util.ArrayList;
import java.util.List;

public class DepositMessageAdapter extends RecyclerView.Adapter<DepositMessageAdapter.MyViewHolder> {
  private List<DepositEntity> mDataset;

  public static class MyViewHolder extends RecyclerView.ViewHolder {

    public TextView tvId,tvFrom,tvContent,tvDate,tvBankTime,tvStatus;
    public MyViewHolder(View view) {
      super(view);
      tvId = view.findViewById(R.id.tvId);
      tvFrom = view.findViewById(R.id.tvFrom);
      tvContent = view.findViewById(R.id.tvContent);
      tvBankTime = view.findViewById(R.id.tvBankTime);
      tvDate = view.findViewById(R.id.tvDate);
      tvStatus = view.findViewById(R.id.tvStatus);
    }
  }

  public DepositMessageAdapter() {
    this.mDataset = new ArrayList<>();
  }

  public DepositMessageAdapter(List<DepositEntity> depositEntities) {
    this.mDataset = depositEntities;
  }

  // Create new views (invoked by the layout manager)
  @Override
  public DepositMessageAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
    LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
    View view = layoutInflater.inflate(R.layout.deposit_message_item, parent, false);
    return new MyViewHolder(view);
  }

  @Override
  public void onBindViewHolder(MyViewHolder holder, int position) {
    DepositEntity depositEntity = mDataset.get(position);
    holder.tvId.setText(String.valueOf(position+1));
    holder.tvFrom.setText(depositEntity.getName());

    DepositMessageContent depositMessageContent = new DepositMessageContent(depositEntity.getContent());

    holder.tvContent.setText(depositMessageContent.getIncurred());
    if(!depositMessageContent.isDeposit()){
      holder.tvContent.setTextColor(Color.parseColor("#e64a19"));
    }
    holder.tvBankTime.setText(depositMessageContent.getBankReceivedTime());
    holder.tvStatus.setText(depositEntity.getStatus());
    holder.tvDate.setText(depositEntity.getDate());

  }





  @Override
  public int getItemCount() {
    return mDataset.size();
  }

  public void set(List<DepositEntity> depositEntities) {
    mDataset.clear();
    mDataset.addAll(depositEntities);
    notifyDataSetChanged();
  }
}