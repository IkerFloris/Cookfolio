package com.example.cookfolio.Search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.cookfolio.R;

public class SearchActivity extends AppCompatActivity {

    // Parameters
    private static final String EXTRA_PARAM1 = "param1";
    private static final String EXTRA_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_search);

        if (getIntent() != null) {
            mParam1 = getIntent().getStringExtra(EXTRA_PARAM1);
            mParam2 = getIntent().getStringExtra(EXTRA_PARAM2);
        }
    }

    public static Intent newIntent(Context context, String param1, String param2) {
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        return intent;
    }
}
