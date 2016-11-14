package app.pictionis.com.pictionis;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;


/**
 * Created by theophilehemachandra on 14/11/2016.
 */

public  class JoinViewHolder extends RecyclerView.ViewHolder {
    TextView mEmail;
    TextView mId;
    Button mJoinGame;

    public JoinViewHolder(View v){
        super(v);
        mId = (TextView) v.findViewById(R.id.idGame);
        mEmail = (TextView) v.findViewById(R.id.email);
        mJoinGame = (Button) v.findViewById(R.id.join);
        mJoinGame.setOnClickListener(new View.OnClickListener() {
            private FirebaseAuth firebaseAuth;
            Users currentUser = new Users();

            @Override
            public void onClick(final View v) {
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

                        mGameChoosenRef.setValue(game);
                        System.out.println("GAME MASTER:" + game.getMaster().getEmail().toString());

                        Intent openThree = new Intent(v.getContext(), DrawingChatActivityClient.class);
                        openThree.putExtra("GAME_ID", game.getMaster().getId().toString());
                        v.getContext().startActivity(openThree);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }

}
