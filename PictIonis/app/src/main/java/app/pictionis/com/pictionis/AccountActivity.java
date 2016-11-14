package app.pictionis.com.pictionis;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

public class AccountActivity extends AppCompatActivity {
    private Button mLogOutBtn;
    private Button mCreateBtn;
    private Button mJoinBtn;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String urlImg = "";
    private ImageView image;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);


        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
          @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null){
                    startActivity(new Intent(AccountActivity.this, MainActivity.class));
                } else {
                    //Initialize ImageView
                    ImageView imageView = (ImageView) findViewById(R.id.imageView);
                    //Loading image from below url into imageView
                    Picasso.with(AccountActivity.this)
                            .load(firebaseAuth.getCurrentUser().getPhotoUrl().toString())
                            .resize(200, 200)
                            .transform(new CircleTransform()).into(imageView);
                    //Initialize email
                    TextView emailTxt = (TextView) findViewById(R.id.emailTxt);
                    emailTxt.setText(firebaseAuth.getCurrentUser().getEmail().toString());
                }
          }
        };
        mLogOutBtn = (Button) findViewById(R.id.logOutBtn);

        mLogOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
            }
        });

        mCreateBtn = (Button) findViewById(R.id.createBtn);

        mCreateBtn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {

                startActivity(new Intent(AccountActivity.this, DrawingChatActivityMaster.class));
            }
        });
        mJoinBtn = (Button) findViewById(R.id.joinBtn);

        mJoinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AccountActivity.this, JoinActivity.class));
            }
        });
    }


    @Override
    protected  void onStart(){
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);
    }
}
