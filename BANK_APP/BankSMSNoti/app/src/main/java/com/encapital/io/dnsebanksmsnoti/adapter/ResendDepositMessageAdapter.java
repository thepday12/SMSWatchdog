package com.encapital.io.dnsebanksmsnoti.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.encapital.io.dnsebanksmsnoti.R;
import com.encapital.io.dnsebanksmsnoti.model.DepositEntity;
import com.encapital.io.dnsebanksmsnoti.model.DepositMessageContent;
import com.encapital.io.dnsebanksmsnoti.model.DepositResponse;
import com.encapital.io.dnsebanksmsnoti.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Response;

import static com.encapital.io.dnsebanksmsnoti.model.DepositEntity.FAIL_STATUS;
import static com.encapital.io.dnsebanksmsnoti.model.DepositEntity.SENDING_STATUS;
import static com.encapital.io.dnsebanksmsnoti.model.DepositEntity.SUCCESS_STATUS;
import static com.encapital.io.dnsebanksmsnoti.utils.CommonUtils.FORMAT_DISPLAY_DATE;
import static com.encapital.io.dnsebanksmsnoti.utils.CommonUtils.formatDateTime;
import static com.encapital.io.dnsebanksmsnoti.utils.CommonUtils.handlerSendSMS2Server;

public class ResendDepositMessageAdapter extends RecyclerView.Adapter<ResendDepositMessageAdapter.MyViewHolder> {

    private final String SENDED_STATUS="SENDED";
    private List<DepositEntity> mDataset;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvId, tvFrom, tvContent, tvDate, tvBankTime, tvStatus;
        ImageButton btnResend;
        ProgressBar pbSending;
        RelativeLayout rlResend;

        MyViewHolder(View view) {
            super(view);
            tvId = view.findViewById(R.id.tvId);
            tvFrom = view.findViewById(R.id.tvFrom);
            tvContent = view.findViewById(R.id.tvContent);
            tvBankTime = view.findViewById(R.id.tvBankTime);
            tvDate = view.findViewById(R.id.tvDate);
            tvStatus = view.findViewById(R.id.tvStatus);
            rlResend = view.findViewById(R.id.rlResend);
            btnResend = view.findViewById(R.id.btnResend);
            pbSending = view.findViewById(R.id.pbSending);

        }
    }

    public ResendDepositMessageAdapter() {
        this.mDataset = new ArrayList<>();
    }

    public ResendDepositMessageAdapter(List<DepositEntity> depositEntity) {
        this.mDataset = depositEntity;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ResendDepositMessageAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                       int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.resend_deposit_message_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final DepositEntity depositEntity = mDataset.get(position);
        holder.tvId.setText(String.valueOf(position + 1));
        holder.tvFrom.setText(depositEntity.getName());

        DepositMessageContent depositMessageContent = new DepositMessageContent(depositEntity.getContent());

        holder.tvContent.setText(depositEntity.getContent());
//        if (depositMessageContent.isDeposit()) {
//            holder.tvContent.setTextColor(Color.parseColor("#00e676"));
//        } else {
//            holder.tvContent.setTextColor(Color.parseColor("#e64a19"));
//        }
        holder.tvBankTime.setText(depositMessageContent.getBankReceivedTime());
        holder.tvDate.setText(formatDateTime(depositEntity.getDate(), FORMAT_DISPLAY_DATE));
        String resendStatus = depositEntity.getStatus();
        holder.pbSending.setVisibility(View.GONE);
        holder.btnResend.setVisibility(View.VISIBLE);
        holder.btnResend.setImageResource(R.drawable.ic_send_white_36dp);
        switch (resendStatus) {
            case SENDING_STATUS:
                holder.tvStatus.setTextColor(Color.WHITE);
                holder.pbSending.setVisibility(View.VISIBLE);
                holder.btnResend.setVisibility(View.INVISIBLE);
                break;
            case SUCCESS_STATUS:
                holder.rlResend.setVisibility(View.GONE);
                holder.tvStatus.setTextColor(Color.GREEN);
                break;
            case SENDED_STATUS:
                holder.rlResend.setVisibility(View.GONE);
                holder.tvStatus.setTextColor(Color.YELLOW);
                break;
            case FAIL_STATUS:
                holder.tvStatus.setTextColor(Color.RED);
                holder.btnResend.setImageResource(R.drawable.ic_replay_white_36dp);
                break;
        }
        holder.tvStatus.setText(resendStatus);
        holder.btnResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendSMS2Server(position);
            }
        });

    }


    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void set(List<DepositEntity> depositEntity) {
        mDataset.clear();
        mDataset.addAll(depositEntity);
        notifyDataSetChanged();
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

    public void updateSendedStatus(int position) {
        updateStatusAtPosition(position, SENDED_STATUS);
    }

    private void updateStatusAtPosition(int position, String status) {
        DepositEntity updateDepositEntity = mDataset.get(position);
        updateDepositEntity.setStatus(status);
        mDataset.set(position, updateDepositEntity);
        notifyItemChanged(position);
    }

    public void resendAllSMS2Server() {
        List<DepositEntity> depositEntities = this.getDataset();
        for (AtomicInteger index = new AtomicInteger(); index.get() < depositEntities.size(); index.getAndIncrement()) {
            DepositEntity depositEntity = depositEntities.get(index.get());
            if (!depositEntity.getStatus().equals(SUCCESS_STATUS)) {
                resendSMS2Server(index.get());
            }
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
                    updateSendedStatus(position);
                }
            }

            @Override
            public void onFailure(Call<DepositResponse> call, Throwable t) {
                updateFailStatus(position);
            }
        });
    }

    public boolean isSending() {
        for (DepositEntity depositEntity : this.mDataset) {
            if (depositEntity.getStatus().equals(SENDING_STATUS)) {
                return true;
            }
        }
        return false;
    }


}