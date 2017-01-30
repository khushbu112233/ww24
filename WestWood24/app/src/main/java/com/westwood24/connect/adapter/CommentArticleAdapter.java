package com.westwood24.connect.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.provider.MediaStore;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.westwood24.R;
import com.westwood24.connect.activity.CommentListOfArticleActivity;
import com.westwood24.connect.model.AllArticles;
import com.westwood24.connect.model.CommentDataList;
import com.westwood24.connect.utils.CircularImageView;
import com.westwood24.connect.utils.Constants;
import com.westwood24.connect.utils.FontCustom;
import com.westwood24.connect.utils.Pref;
import com.westwood24.connect.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;

public class CommentArticleAdapter extends BaseAdapter {
    private ArrayList<CommentDataList> listData;
    private LayoutInflater layoutInflater;
    TextView txt_client_name,txt_des;
    Context context;

    public CommentArticleAdapter(Context context, ArrayList<CommentDataList> listData) {
        this.context = context;
        this.listData = listData;
        layoutInflater = LayoutInflater.from(context);
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

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.row_comment_list, null);
            holder = new ViewHolder();
            holder.mUnameComTv = (TextView) convertView.findViewById(R.id.row_comment_uname_tv);

            holder.mDataComTv = (TextView) convertView.findViewById(R.id.row_comment_data_tv);
            holder.mProfileUserCImg = (CircularImageView) convertView.findViewById(R.id.row_comment_user_img);
            //holder.mCommentTv = (TextView) convertView.findViewById(R.id.list_row_comment_tv);
            txt_client_name = (TextView)convertView.findViewById(R.id.txt_client_name);
            txt_des = (TextView)convertView.findViewById(R.id.txt_des);
            holder.mUnameComTv.setTypeface(FontCustom.setFontBold(context));
            holder.mDataComTv.setTypeface(FontCustom.setFont(context));
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.mUnameComTv.setText(listData.get(position).getCommentUserName()+listData.get(position).getCommentText());
        holder.mDataComTv.setText(listData.get(position).getCommentText());



        SpannableStringBuilder ssb = new SpannableStringBuilder(listData.get(position).getCommentUserName()+"  "+listData.get(position).getCommentText());
        //ssb.append("By registering you are agreeing to our privacy policy and terms of use.");

        ssb.setSpan(new ClickableSpan() {

            @Override
            public void onClick(View v) {
                txt_client_name.callOnClick();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(context.getResources().getColor(R.color.white));
                ds.setUnderlineText(false);
                ds.setTextSize(context.getResources().getDimension(R.dimen.edittextsize));
            }

        }, 0, listData.get(position).getCommentUserName().toString().length(), 0);

        ssb.setSpan(new ClickableSpan() {

            @Override
            public void onClick(View v) {
                txt_des.callOnClick();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(context.getResources().getColor(R.color.white));
                ds.setUnderlineText(false);
                ds.setTextSize(context.getResources().getDimension(R.dimen.description_size));
            }

        }, listData.get(position).getCommentUserName().toString().length() + 1, holder.mUnameComTv.getText().toString().length()+2, 0);
        holder.mUnameComTv.setMovementMethod(LinkMovementMethod.getInstance());
        holder.mUnameComTv.setText(ssb);
        Log.e("pict",""+Pref.getValue(context, Constants.TAG_PROFILE_PICTURE, ""));
      /*  if(Pref.getValue(context, Constants.TAG_PROFILE_PICTURE, "")!="") {
            Bitmap userProfilePath = Utils.base64ToImage(Pref.getValue(context, Constants.TAG_PROFILE_PICTURE, ""));
            holder.mProfileUserCImg.setImageBitmap(userProfilePath);
        }*/

        return convertView;
    }

    static class ViewHolder {
        TextView mUnameComTv;
        TextView mDataComTv;
        TextView mCommentTv;
        CircularImageView mProfileUserCImg;
    }


}
