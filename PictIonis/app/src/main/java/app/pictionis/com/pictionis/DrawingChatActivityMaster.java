package app.pictionis.com.pictionis;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DrawingChatActivityMaster extends AppCompatActivity implements View.OnTouchListener {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private FirebaseAuth firebaseAuth;
    private Button mCancelBtn;
    private Button mSendBtn;
    private TextView mMessageText;
    ImageView imageView;
    Bitmap bitmap;
    Canvas canvas;
    Paint paint;
    float downx = 0, downy = 0, upx = 0, upy = 0;
    float startX = 0;
    float startY = 0;
    float endX = 0;
    float endY = 0;
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mGameRef = mRootRef.child("Games");
    private FirebaseRecyclerAdapter<Messages, ChatViewHolder> mFireBaseAdapter;
    Games game = new Games();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing_chat);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        //DRAW
        imageView = (ImageView) this.findViewById(R.id.imageView1);

        Display currentDisplay = getWindowManager().getDefaultDisplay();

        float dw = currentDisplay.getWidth();
        float dh = 500;

        bitmap = Bitmap.createBitmap((int) dw, (int) dh,
                Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth((float) 10);
        imageView.setImageBitmap(bitmap);

        imageView.setOnTouchListener(this);

        //CREATION DANS GAME DE LA PARTIE
        firebaseAuth = FirebaseAuth.getInstance();
        game.setMaster(new Users(firebaseAuth.getCurrentUser().getEmail().toString(), firebaseAuth.getCurrentUser().getUid().toString()));
        game.setPlayers(game.getMaster());
        mGameRef.child(game.getMaster().getId()).setValue(game);
        //mGameRef.child(game.getMaster().getId()).child("players").setValue(game.getMaster());

        mFireBaseAdapter = new FirebaseRecyclerAdapter<Messages, ChatViewHolder>(
                Messages.class,
                R.layout.item_message,
                ChatViewHolder.class,
                mGameRef.child(game.getMaster().getId()).child("messages")) {
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

    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                startX=event.getX();
                startY=event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                endX = event.getX();
                endY = event.getY();
                canvas.drawLine(startX,startY,endX,endY, paint);
                imageView.invalidate();
                startX=endX;
                startY=endY;
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            default:
                break;
        }
        return true;
    }
    @Override
    protected void onStart(){
        super.onStart();

        mCancelBtn = (Button) findViewById(R.id.cancelBtn);
        mSendBtn = (Button) findViewById(R.id.sendBtn);
        mMessageText = (TextView) findViewById(R.id.messageText);

        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGameRef.child(game.getMaster().getId()).removeValue();
                startActivity(new Intent(DrawingChatActivityMaster.this, AccountActivity.class));
            }
        });
        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game.setMessages(new Messages(game.getMaster().getEmail().toString(), mMessageText.getText().toString()));
                mGameRef.child(game.getMaster().getId()).setValue(game);
                mMessageText.setText("");
            }
        });
    }


}

