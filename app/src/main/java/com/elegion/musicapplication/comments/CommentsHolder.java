package com.elegion.musicapplication.comments;

import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.TextView;

import com.elegion.musicapplication.R;
import com.elegion.musicapplication.model.Comment;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CommentsHolder extends RecyclerView.ViewHolder {

    private TextView mText;
    private TextView mAuthor;
    private TextView mDate;



    public CommentsHolder(View itemView) {
        super(itemView);
        mText = itemView.findViewById(R.id.tv_text);
        mAuthor = itemView.findViewById(R.id.tv_author);
        mDate = itemView.findViewById(R.id.tv_date);
    }

    public void bind(Comment item) {
        mText.setText(item.getText());
        mAuthor.setText(item.getAuthor());

//yyyy-MM-dd'T'HH:mm:ss.SSSXXX

//        DateFormat dateFormat = new SimpleDateFormat("DD-MM-YYYY");
//        Date date = new Date(item.getTimestamp());
//        mDate.setText(dateFormat.format(date));


        String timestamp = item.getTimestamp();
        if (timestamp != null) {
            try {
                Date commentDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US).parse(timestamp);

                String stringDate;
                if (DateUtils.isToday(commentDate.getTime())) {
                    Format formatter = new SimpleDateFormat("HH:mm", Locale.US);
                    stringDate = formatter.format(commentDate);
                } else {
                    Format formatter = new SimpleDateFormat("dd.MM.yyyy", Locale.US);
                    stringDate = formatter.format(commentDate);
                }
                mDate.setText(stringDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

}