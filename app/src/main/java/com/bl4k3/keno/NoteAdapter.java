package com.bl4k3.keno;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private List<Note> mDataList;
    private HashMap<String, Integer> userHasColor;
    private List<Integer> userColors;
    private Context mContext;
    private int userCounter = 0;

    public NoteAdapter(Context context, List<Note> data) {
        this.mDataList = data;
        this.mContext = context;

        userHasColor = new HashMap<>();
        fullUserColorsList();
    }

    private void fullUserColorsList() {
        userColors = new ArrayList<>();
        this.userColors.add(R.color.user1);
        this.userColors.add(R.color.user2);
        this.userColors.add(R.color.user3);
        this.userColors.add(R.color.user4);
        this.userColors.add(R.color.user5);
        this.userColors.add(R.color.user6);
        this.userColors.add(R.color.user7);
        this.userColors.add(R.color.user8);
        this.userColors.add(R.color.user9);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.card_note, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(mDataList.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mTitleLable, mMessageLabel, mDateLabel;
        private CircleImageView mCircleImageView;

        ViewHolder(View itemView) {
            super(itemView);
            mTitleLable = (TextView)itemView.findViewById(R.id.title);
            mMessageLabel = (TextView) itemView.findViewById(R.id.message);
            mDateLabel = (TextView) itemView.findViewById(R.id.timestamp);
            mCircleImageView = (CircleImageView) itemView.findViewById(R.id.userCircle);
        }

        void bind(final Note note) {

            for (Note item : mDataList) {
                if (!userHasColor.containsKey(item.getUserId())) {
                    userHasColor.put(item.getUserId(),userColors.get(userCounter));
                    if (userCounter == 8) {
                        userCounter = 0;
                    } else {
                        userCounter++;
                    }

                }
            }

            mTitleLable.setText(note.getTitle());
            mMessageLabel.setText(note.getMessage());
            mDateLabel.setText(DateFormatter.instance.convertDateToString(note.getTimestamp()));
            mCircleImageView.setImageResource(userHasColor.get(note.getUserId()));
        }
    }

}
