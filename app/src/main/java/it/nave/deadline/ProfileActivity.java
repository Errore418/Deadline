package it.nave.deadline;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {
    private AppDatabase database;
    private String username;
    private final int SCORE_CODE = 42;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        database = AppDatabase.getDatabase(getApplicationContext());

        if (savedInstanceState == null) {
            username = getIntent().getStringExtra("username");
        } else {
            username = savedInstanceState.getString("username");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        User user = database.userDao().getUser(username);

        TextView textView4 = findViewById(R.id.textView4);
        TextView textView5 = findViewById(R.id.textView5);
        TextView textView6 = findViewById(R.id.textView6);

        setUserInformation(textView4, R.string.textView4, "USERNAME", user.username);
        setUserInformation(textView5, R.string.textView5, "XX", Long.toString(user.games));
        setUserInformation(textView6, R.string.textView6, "XX", Long.toString(user.bestScore));
    }

    private void setUserInformation(TextView textView, int strId, String target, String replacement) {
        String oldString = getResources().getString(strId);
        String newString = oldString.replace(target, replacement);
        textView.setText(newString);
    }

    public void onClickButton4(View view) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        Editor edit = settings.edit();
        edit.remove("username").apply();
        Intent i = new Intent(this, WelcomeActivity.class);
        startActivity(i);
    }

    public void onClickButton5(View view) {
        Intent service = new Intent(this, GamePlayedService.class);
        service.putExtra("username", username);
        startService(service);

        Intent i = new Intent(this, GameActivity.class);
        startActivityForResult(i, SCORE_CODE);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstance) {
        savedInstance.putString("username", username);
        super.onSaveInstanceState(savedInstance);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SCORE_CODE) {
            if (resultCode == RESULT_OK) {
                User user = database.userDao().getUser(username);
                long bestScore = user.bestScore;
                int score = data.getIntExtra("score", -1);
                if (score > bestScore) {
                    user.bestScore = score;
                    database.userDao().updateUser(user);
                }
            }
        }
    }


}
