package app.pictionis.com.pictionis;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.util.UUID;

public class DrawingChatActivityClient extends AppCompatActivity{

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private FirebaseAuth firebaseAuth;
    private Button mCancelBtn;
    private Button mSendBtn;
    private TextView mMessageText;
    private String GAME_ID;
    private FirebaseRecyclerAdapter<Messages, ChatViewHolder> mFireBaseAdapter;
    private FirebaseStorage storage;
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mGameRef = mRootRef.child("Games");
    StorageReference storageRef = storage.getInstance().getReference();
    Games game = new Games();
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing_chat_client);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        Intent intent = getIntent();
        GAME_ID = intent.getStringExtra("GAME_ID");
        //DRAW


        //CREATION DANS GAME DE LA PARTIE
        firebaseAuth = FirebaseAuth.getInstance();
        mGameRef.child(GAME_ID);

        mGameRef.child(GAME_ID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                Gson gson = new Gson();

                try {
                    game = gson.fromJson(snapshot.getValue().toString() , Games.class);
                    if (game.getImgBase64() == 100){
                        Glide.with(DrawingChatActivityClient.this)
                                .using(new FirebaseImageLoader())
                                .load(storageRef.child("images/" + GAME_ID+ ".jpg"))
                                .skipMemoryCache(true)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .signature(new StringSignature(UUID.randomUUID().toString()))
                                .into(imageView);

                    }
                } catch (Exception e) {
                    System.out.println("error");
                    System.out.println("error");
                    System.out.println("error");
                    System.out.println("error");
                    System.out.println("error");
                    System.out.println(e);
                    System.out.println("error");
                    System.out.println("error");
                    System.out.println("error");
                    System.out.println("error");
                    System.out.println("error");
                    System.out.println("error");

                    e.printStackTrace();
                }




            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });



        imageView = (ImageView) this.findViewById(R.id.imageView1);


        try {
            Glide.with(this /* context */)
                    .using(new FirebaseImageLoader())
                    .load(storageRef.child("images/" + GAME_ID+ ".jpg"))
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .signature(new StringSignature(UUID.randomUUID().toString()))
                    .into(imageView);

        } catch (Exception e) {
            e.printStackTrace();
        }

        mFireBaseAdapter = new FirebaseRecyclerAdapter<Messages, ChatViewHolder>(
                Messages.class,
                R.layout.item_message,
                ChatViewHolder.class,
                mGameRef.child(GAME_ID).child("messages")) {
            @Override
            protected void populateViewHolder(ChatViewHolder messageViewHolder, Messages model, int position) {
                messageViewHolder.mMessage.setText(model.getMessage());
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
        mSendBtn = (Button) findViewById(R.id.sendBtn);
        mMessageText = (TextView) findViewById(R.id.messageText);

        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMessageText.getText() != "") {
                    game.setMessages(new Messages(game.getMaster().getEmail().toString(), mMessageText.getText().toString()));
                    mGameRef.child(GAME_ID).setValue(game);
                    mMessageText.setText("");
                }
            }
        });
    }

}

