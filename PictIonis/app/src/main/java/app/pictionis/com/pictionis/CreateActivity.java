package app.pictionis.com.pictionis;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private FirebaseAuth firebaseAuth;
    private Button mCancelBtn;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mGameRef = mRootRef.child("Games");
    private FirebaseRecyclerAdapter<Users, MessageViewHolder> mFireBaseAdapter;
    Games game = new Games();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        //CREATION DANS GAME DE LA PARTIE
        firebaseAuth = FirebaseAuth.getInstance();
        game.setMaster(new Users(firebaseAuth.getCurrentUser().getEmail().toString(), firebaseAuth.getCurrentUser().getUid().toString()));
        game.setPlayers(game.getMaster());
       mGameRef.child(game.getMaster().getId()).setValue(game);
      //  mGameRef.child(game.getMaster().getId()).child("players").setValue(game.getMaster());


        mFireBaseAdapter = new FirebaseRecyclerAdapter<Users, MessageViewHolder>(
                Users.class,
                R.layout.item_message,
                MessageViewHolder.class,
                mGameRef.child(game.getMaster().getId()).child("players")) {
            @Override
            protected void populateViewHolder(MessageViewHolder messageViewHolder, Users model, int position) {
                messageViewHolder.mEmail.setText(model.getEmail());
            }
        };

        mFireBaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver(){
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount){
                super.onItemRangeInserted(positionStart, itemCount);
                int roomCount = mFireBaseAdapter.getItemCount();
                int lastVisiblePosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                if (lastVisiblePosition == -1 || (positionStart >= (roomCount - 1) && lastVisiblePosition == (positionStart - 1))){
                    mRecyclerView.scrollToPosition(positionStart);
                }
            }
        });

        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mFireBaseAdapter);
    }

    @Override
    protected void onStart(){
        super.onStart();
        mCancelBtn = (Button) findViewById(R.id.cancelBtn);

        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGameRef.child(game.getMaster().getId()).removeValue();
                startActivity(new Intent(CreateActivity.this, AccountActivity.class));
            }
        });
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView mEmail;
        TextView mId;

        public MessageViewHolder(View v){
            super(v);
            mEmail = (TextView) v.findViewById(R.id.email);
        }

    }
}
