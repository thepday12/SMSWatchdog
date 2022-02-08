package com.encapital.io.dnsebanksmsnoti.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.encapital.io.dnsebanksmsnoti.R;
import com.encapital.io.dnsebanksmsnoti.database.AppDatabaseHelper;
import com.encapital.io.dnsebanksmsnoti.model.DepositEntity;
import com.encapital.io.dnsebanksmsnoti.model.DepositMessageContent;
import com.encapital.io.dnsebanksmsnoti.model.DepositResponse;
import com.encapital.io.dnsebanksmsnoti.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Response;

import static com.encapital.io.dnsebanksmsnoti.model.DepositEntity.FAIL_STATUS;
import static com.encapital.io.dnsebanksmsnoti.model.DepositEntity.SENDING_STATUS;
import static com.encapital.io.dnsebanksmsnoti.model.DepositEntity.SUCCESS_STATUS;
import static com.encapital.io.dnsebanksmsnoti.utils.CommonUtils.FORMAT_DISPLAY_DATE;
import static com.encapital.io.dnsebanksmsnoti.utils.CommonUtils.formatDateTime;
import static com.encapital.io.dnsebanksmsnoti.utils.CommonUtils.handlerSendSMS2Server;

public class DepositMessageAdapter extends RecyclerView.Adapter<DepositMessageAdapter.MyViewHolder> {
    private List<DepositEntity> mDataset;
    private AppDatabaseHelper mDb;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvId, tvFrom, tvContent, tvDate, tvBankTime, tvStatus;
        RelativeLayout rlAction;
        ImageButton btnResend;
        ProgressBar pbSending;

        MyViewHolder(View view) {
            super(view);
            tvId = view.findViewById(R.id.tvId);
            tvFrom = view.findViewById(R.id.tvFrom);
            tvContent = view.findViewById(R.id.tvContent);
            tvBankTime = view.findViewById(R.id.tvBankTime);
            tvDate = view.findViewById(R.id.tvDate);
            tvStatus = view.findViewById(R.id.tvStatus);
            rlAction = view.findViewById(R.id.rlAction);
            btnResend = view.findViewById(R.id.btnResend);
            pbSending = view.findViewById(R.id.pbSending);
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
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.deposit_message_item, parent, false);
        mDb = AppDatabaseHelper.getInstance(parent.getContext());

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        DepositEntity depositEntity = mDataset.get(position);
        holder.tvId.setText(String.valueOf(position + 1));
        holder.tvFrom.setText(depositEntity.getName());

        DepositMessageContent depositMessageContent = new DepositMessageContent(depositEntity.getContent());
        String depositEntityStatus = depositEntity.getStatus();
        holder.tvContent.setText(depositEntity.getContent().replace("\n", "").replace("\r", ""));
//        if (depositMessageContent.isDeposit()) {
//            holder.tvContent.setTextColor(Color.parseColor("#00e676"));
//        } else {
//            holder.tvContent.setTextColor(Color.parseColor("#e64a19"));
//        }
        holder.tvBankTime.setText(depositMessageContent.getBankReceivedTime());
        holder.tvStatus.setText(depositEntityStatus);
        holder.tvDate.setText(formatDateTime(depositEntity.getDate(), FORMAT_DISPLAY_DATE));

        switch (depositEntityStatus) {
            case FAIL_STATUS:
                holder.rlAction.setVisibility(View.VISIBLE);
                holder.btnResend.setVisibility(View.VISIBLE);
                holder.pbSending.setVisibility(View.GONE);
                holder.btnResend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        resendSMS2Server(position);
                    }
                });
                break;
            case SENDING_STATUS:
                holder.rlAction.setVisibility(View.VISIBLE);
                holder.btnResend.setVisibility(View.GONE);
                holder.pbSending.setVisibility(View.VISIBLE);
                break;
            default:
                holder.rlAction.setVisibility(View.GONE);
                break;
        }


    }


    public List<DepositEntity> getDataset() {
        return this.mDataset;
    }

    public void updateSendingStatus(int position) {
        updateStatusAtPosition(position, SENDING_STATUS);
    }

    public void updateSuccessStatus(int position) {
        updateStatusAtPosition(position, SUCCESS_STATUS);
    }

    public void updateFailStatus(int position) {
        updateStatusAtPosition(position, FAIL_STATUS);
    }

    private void updateStatusAtPosition(int position, String status) {
        DepositEntity updateDepositEntity = mDataset.get(position);
        updateDepositEntity.setStatus(status);
        mDataset.set(position, updateDepositEntity);
        notifyItemChanged(position);
        if (!Objects.isNull(updateDepositEntity.getId())) {
            mDb.updateMessageStatus(updateDepositEntity);
        }
    }

    private void resendSMS2Server(final int position) {
        DepositEntity depositEntity = mDataset.get(position);
        updateSendingStatus(position);
        handlerSendSMS2Server(depositEntity, new CommonUtils.ISendSMSResponse() {
            @Override
            public void onResponse(Call<DepositResponse> call, Response<DepositResponse> response) {
                if (response.isSuccessful()) {
                    updateSuccessStatus(position);
                } else {
                    updateFailStatus(position);
                }
            }

            @Override
            public void onFailure(Call<DepositResponse> call, Throwable t) {
                updateFailStatus(position);
            }
        });
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