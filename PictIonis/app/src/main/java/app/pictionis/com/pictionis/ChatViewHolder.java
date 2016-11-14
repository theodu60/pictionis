package app.pictionis.com.pictionis;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by theophilehemachandra on 14/11/2016.
 */

public  class ChatViewHolder extends RecyclerView.ViewHolder {
    TextView mEmail;
    TextView mMessage;
    public ChatViewHolder(View v){
        super(v);
        mEmail = (TextView) v.findViewById(R.id.email);
        mMessage = (TextView) v.findViewById(R.id.message);
    }

}