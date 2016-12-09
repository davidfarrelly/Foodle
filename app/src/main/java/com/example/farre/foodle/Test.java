package com.example.farre.foodle;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;


public class Test extends AppCompatActivity {
    private Context mContext;

    LinearLayout mRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


            // Get the application context
            mContext = getApplicationContext();
            // Get the widgets reference from XML layout
            mRelativeLayout = (LinearLayout) findViewById(R.id.rl);
            // Initialize a new CardView
            CardView card = new CardView(mContext);
            // Set the CardView layoutParams
            LayoutParams params = new LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT
            );
            card.setLayoutParams(params);

            card.setRadius(9);
            card.setContentPadding(15, 15, 15, 15);
            card.setCardBackgroundColor(Color.parseColor("#f1f1f1"));
            card.setMaxCardElevation(15);
            card.setCardElevation(9);
            card.setUseCompatPadding(true);

            // Initialize a new TextView to put in CardView
            TextView tv = new TextView(mContext);
            tv.setLayoutParams(params);
            tv.setText("CardView\nProgrammatically");
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
            tv.setTextColor(Color.BLACK);
            card.addView(tv);

            // Finally, add the CardView in root layout
            mRelativeLayout.addView(card);

    }
}