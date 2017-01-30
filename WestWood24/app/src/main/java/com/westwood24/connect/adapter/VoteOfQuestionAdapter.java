package com.westwood24.connect.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.westwood24.R;
import com.westwood24.connect.activity.CommentListOfArticleActivity;
import com.westwood24.connect.activity.DetailOfVideoImageActivity;
import com.westwood24.connect.model.AllArticles;
import com.westwood24.connect.model.VoteOfQuestion;

import java.util.ArrayList;
import java.util.HashMap;

public class VoteOfQuestionAdapter extends BaseAdapter {
    private ArrayList<VoteOfQuestion> listData;
    private LayoutInflater layoutInflater;
    Context context;
    String TAG = "VoteOfQuestionAdapter";

    public VoteOfQuestionAdapter(Context context, ArrayList<VoteOfQuestion> listData) {
        this.context = context;
        this.listData = listData;
        layoutInflater = LayoutInflater.from(context);
        Log.e("vote", "add " + listData.size());
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.row_voteof_question, null);
            holder = new ViewHolder();
            holder.mQuestionTv = (TextView) convertView.findViewById(R.id.row_vote_question_tv);
            // holder.mQueCb = (CheckBox) convertView.findViewById(R.id.row_vote_que_checkbox);
            // holder.mQueRb = (RadioButton) convertView.findViewById(R.id.row_vote_que_radiobtn);
            //holder.mVoteAnsTv = (TextView) convertView.findViewById(R.id.row_vote_answer_tv);
            holder.mVoteTopLn = (LinearLayout) convertView.findViewById(R.id.row_vote_top_ln);
            //holder.mRadioGroup=(RadioGroup)convertView.findViewById(R.id.row_voteof_radiogroup);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mQuestionTv.setText(listData.get(position).getQuestiontitle());

       /* if (listData.get(position).getVotetype().equals("checkbox")) {
            holder.mQueCb.setVisibility(View.VISIBLE);

        } else {
            holder.mQueRb.setVisibility(View.VISIBLE);
        }*/

        Log.e(TAG, "vote!! " + listData.get(position).getVoteoptions());
        String voteOptionArray[] = listData.get(position).getVoteoptions().toString().split(",");


        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout ll = new LinearLayout(context);
        for (int j = 0; j < voteOptionArray.length; j++) {
            Log.e(TAG, "j value " + j);
            Log.e(TAG, "11 " + voteOptionArray[j]);

            LinearLayout ll2 = new LinearLayout(context);
            ll.setOrientation(LinearLayout.VERTICAL);
            ll2.setOrientation(LinearLayout.HORIZONTAL);
            holder.mRadioGroup = new RadioGroup(context);
            holder.mTextView = new TextView(context);
            holder.mVoteCb = new CheckBox(context);

            holder.mVoteRb = new RadioButton(context);


            holder.mVoteCb.setVisibility(View.GONE);
            holder.mVoteRb.setVisibility(View.GONE);
            if (listData.get(position).getVotetype().equals("checkbox")) {
                holder.mVoteCb.setVisibility(View.VISIBLE);

            } else {
                holder.mVoteRb.setVisibility(View.VISIBLE);
            }
            holder.mTextView.setText(voteOptionArray[j]);
            if (listData.get(position).getVotetype().equals("checkbox")) {
                ll2.addView(holder.mVoteCb);

            } else {
                //holder.mRadioGroup.addView(holder.mVoteRb);
                // holder.mVoteRb.setChecked(true);
                ll2.addView(holder.mVoteRb);
            }


            ll2.addView(holder.mTextView);
            ll.addView(ll2);
            //holder.mVoteTopRl.addView(layout2);

        }
        //layout2.addView(layout3);
        holder.mVoteTopLn.addView(ll);

        holder.mVoteRb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
        return convertView;
    }

    static class ViewHolder {
        TextView mQuestionTv;
        CheckBox mQueCb;
        RadioButton mQueRb;
        TextView mVoteAnsTv;
        LinearLayout mVoteTopLn;
        TextView mTextView;
        CheckBox mVoteCb;
        RadioButton mVoteRb;
        RadioGroup mRadioGroup;

    }


}
