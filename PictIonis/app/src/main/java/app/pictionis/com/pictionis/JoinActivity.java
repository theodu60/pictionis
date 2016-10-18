package app.pictionis.com.pictionis;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

public class JoinActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private FirebaseAuth firebaseAuth;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mGameRef = mRootRef.child("Games");
    private FirebaseRecyclerAdapter<Object, JoinActivity.MessageViewHolder2> mFireBaseAdapter;
    Users currentUser = new Users();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView2);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        //INIT OBJECT USER
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser.setEmail(firebaseAuth.getCurrentUser().getEmail().toString());
        currentUser.setId(firebaseAuth.getCurrentUser().getUid().toString());
        mFireBaseAdapter = new FirebaseRecyclerAdapter<Object, JoinActivity.MessageViewHolder2>(
                Object.class,
                R.layout.item_message,
                JoinActivity.MessageViewHolder2.class,
                mGameRef) {
            @Override
            protected void populateViewHolder(JoinActivity.MessageViewHolder2 messageViewHolder, Object model, int position) {
                System.out.println("UCUCUCUCUCUCUCUCU");
                System.out.println("UCUCUCUCUCUCUCUCU");
                System.out.println("UCUCUCUCUCUCUCUCU");
                System.out.println("UCUCUCUCUCUCUCUCU");
                Gson gson = new Gson();
                Games game = new Games();
                game = gson.fromJson(model.toString() , Games.class);

                System.out.println("UCUCUCUCUCUCUCUCU");
                System.out.println("UCUCUCUCUCUCUCUCU");
                System.out.println("UCUCUCUCUCUCUCUCU");
                System.out.println("UCUCUCUCUCUCUCUCU");
                System.out.println("UCUCUCUCUCUCUCUCU");
                messageViewHolder.mEmail.setText(game.getMaster().getEmail());
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

    }

    public static class MessageViewHolder2 extends RecyclerView.ViewHolder {
        TextView mEmail;
        TextView mId;

        public MessageViewHolder2(View v){
            super(v);
            mEmail = (TextView) v.findViewById(R.id.email);
        }

    }
}
