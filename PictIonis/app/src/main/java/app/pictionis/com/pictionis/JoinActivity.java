package app.pictionis.com.pictionis;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

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
    private FirebaseRecyclerAdapter<Object, JoinViewHolder> mFireBaseAdapter;
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
        mFireBaseAdapter = new FirebaseRecyclerAdapter<Object, JoinViewHolder>(
                Object.class,
                R.layout.item_join,
                JoinViewHolder.class,
                mGameRef) {
            @Override
            protected void populateViewHolder(JoinViewHolder joinViewHolder, Object model, int position) {
                Gson gson = new Gson();
                Games game = new Games();
                game = gson.fromJson(model.toString() , Games.class);
                joinViewHolder.mEmail.setText(game.getMaster().getEmail());
                joinViewHolder.mId.setText(game.getMaster().getId());
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

}
