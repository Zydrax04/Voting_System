package com.example.votingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.util.ArrayList;

public class ChooseTemplateActivity extends AppCompatActivity {

    public void openTemplate2Activity(Intent i) {
        startActivity(i);
    }

    public void openTemplateActivity(int number) {
        Intent intent;
        if(number == 1) {
            intent = new Intent(this, Template1.class);
            intent.putExtra("USER_NAME", "Admin");
        }
        else if(number == 2)
            intent = new Intent(this, Template2.class);
        else
            intent = new Intent(this, Template3.class);
        startActivity(intent);
    }

    public ArrayList<ImageView> getTemplates() {
        ArrayList<ImageView> templates = new ArrayList<ImageView>();
        templates.add((ImageView) findViewById(R.id.template1));
        templates.add((ImageView) findViewById(R.id.template2));
        templates.add((ImageView) findViewById(R.id.template3));
        return templates;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_template);

        //Animations
        Animation scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        Animation scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);

        ArrayList<ImageView> templates = getTemplates();
        //add EventListener for each template
        for(int i = 0; i < templates.size(); i++){

            final ImageView template = templates.get(i);
            int finalI = i + 1;
            template.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    template.startAnimation(scaleUp);
                    if(finalI == 1) {
                        Intent intent = new Intent(getBaseContext(), Template1.class);
                        intent.putExtra("USER_NAME", "Admin");
                        openTemplate2Activity(intent);
                    }
                    else if(finalI == 2){
                        Intent intent = new Intent(getBaseContext(), Template2.class);
                        intent.putExtra("USER_NAME", "Admin");
                        openTemplate2Activity(intent);
                    }
                    else if(finalI == 3) {
                        Intent intent = new Intent(getBaseContext(), Template3.class);
                        intent.putExtra("USER_NAME", "Admin");
                        openTemplate2Activity(intent);
                    }
                    //openTemplateActivity(finalI);
                }
            });

        }
    }
}