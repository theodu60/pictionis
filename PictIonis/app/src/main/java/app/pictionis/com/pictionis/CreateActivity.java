package app.pictionis.com.pictionis;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateActivity extends AppCompatActivity {
    TextView mConditionTextView;
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mGameRef = mRootRef.child("Games");
    private ArrayAdapter<String> mUsersAdapter;
    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
    }
    protected void onItemAdded(String item){
       mGameRef.push().setValue(item);
    }
    protected void onStart(){
        super.onStart();
        mListView = (ListView) findViewById(R.id.listView);
        //LIST
        FirebaseListAdapter<String> adapter =
                new FirebaseListAdapter<String>(
                        this,
                        String.class,
                        android.R.layout.simple_list_item_1,
                        mGameRef) {
                    @Override
                    protected void populateView(View view, String s, int i) {
                        TextView text = (TextView)view.findViewById(android.R.id.text1);
                        text.setText(s);
                    }
                };
        mListView.setAdapter(adapter);
    }
}
