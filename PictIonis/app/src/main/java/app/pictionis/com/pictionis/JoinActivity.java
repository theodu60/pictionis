package app.pictionis.com.pictionis;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

public class JoinActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private FirebaseAuth firebaseAuth;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mGameRef = mRootRef.child("Games");
    private FirebaseRecyclerAdapter<Object, JoinActivity.MessageViewHolder> mFireBaseAdapter;
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
        mFireBaseAdapter = new FirebaseRecyclerAdapter<Object, JoinActivity.MessageViewHolder>(
                Object.class,
                R.layout.item_message,
                JoinActivity.MessageViewHolder.class,
                mGameRef) {
            @Override
            protected void populateViewHolder(JoinActivity.MessageViewHolder messageViewHolder, Object model, int position) {
                Gson gson = new Gson();
                Games game = new Games();
                game = gson.fromJson(model.toString() , Games.class);
                messageViewHolder.mEmail.setText(game.getMaster().getEmail());
                messageViewHolder.mId.setText(game.getMaster().getId());
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
    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView mEmail;
        TextView mId;
        Button mJoinGame;

        public MessageViewHolder(View v){
            super(v);
            mId = (TextView) v.findViewById(R.id.idGame);
            mEmail = (TextView) v.findViewById(R.id.email);
            mJoinGame = (Button) v.findViewById(R.id.join);
            mJoinGame.setOnClickListener(new View.OnClickListener() {
                private FirebaseAuth firebaseAuth;
                Users currentUser = new Users();

                @Override
                public void onClick(View v) {
                    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
                    final DatabaseReference mGameChoosenRef = mRootRef.child("Games").child(mId.getText().toString());

                    firebaseAuth = FirebaseAuth.getInstance();
                    currentUser.setEmail(firebaseAuth.getCurrentUser().getEmail().toString());
                    currentUser.setId(firebaseAuth.getCurrentUser().getUid().toString());

                    mGameChoosenRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            Object mMaster = dataSnapshot.getValue();


                            Gson gson = new Gson();
                            Games game = new Games();
                            game = gson.fromJson(mMaster.toString() , Games.class);
                            game.setPlayers(currentUser);

                            System.out.println("OKOKOKKOOKK");
                            System.out.println("OKOKOKKOOKK");
                            System.out.println("OKOKOKKOOKK");
                            System.out.println("OKOKOKKOOKK");
                            System.out.println("OKOKOKKOOKK");
                            System.out.println("OKOKOKKOOKK");
                            System.out.println("OKOKOKKOOKK");
                            System.out.println("OKOKOKKOOKK");
                            System.out.println("OKOKOKKOOKK");
                            System.out.println("OKOKOKKOOKK");
                            System.out.println("OKOKOKKOOKK");
                            System.out.println("OKOKOKKOOKK");
                            System.out.println("OKOKOKKOOKK");
                            System.out.println("OKOKOKKOOKK");
                            System.out.println("OKOKOKKOOKK");
                            System.out.println("OKOKOKKOOKK");
                            System.out.println("OKOKOKKOOKK");
                            System.out.println("OKOKOKKOOKK");
                            mGameChoosenRef.setValue(game);
                            System.out.println("GAME MASTER:" + game.getMaster().getEmail().toString());

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            });
        }

    }
}
