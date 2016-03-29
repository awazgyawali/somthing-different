package np.com.aawaz.csitentrance.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import np.com.aawaz.csitentrance.R;
import np.com.aawaz.csitentrance.fragments.other_fragments.AnswersDrawer;
import np.com.aawaz.csitentrance.fragments.other_fragments.QuestionFragment;
import np.com.aawaz.csitentrance.interfaces.QuizInterface;
import np.com.aawaz.csitentrance.misc.CustomViewPager;


public class QuizActivity extends AppCompatActivity implements QuizInterface {

    ArrayList<String> questions = new ArrayList<>();
    ArrayList<String> a = new ArrayList<>();
    ArrayList<String> b = new ArrayList<>();
    ArrayList<String> c = new ArrayList<>();
    ArrayList<String> d = new ArrayList<>();
    ArrayList<String> answer = new ArrayList<>();

    DrawerLayout drawerLayout;
    AnswersDrawer answersDrawer;
    CustomViewPager customViewPager;

    String[] titles = {"", "2069", "2070", "2071", "2072", "Model 1",
            "Model 2", "Model 3", "Model 4"};

    int qNo = 0;
    int score = 0;
    int code;

    public static String AssetJSONFile(String filename, Context c) throws IOException {
        AssetManager manager = c.getAssets();

        InputStream file = manager.open(filename);
        byte[] formArray = new byte[file.available()];
        file.read(formArray);
        file.close();

        return new String(formArray);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        //Toolbar as support action bar
        setTitle("");
        setSupportActionBar((Toolbar) findViewById(R.id.quizToolbar));
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        code = getIntent().getIntExtra("position", 1);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayoutQuiz);
        customViewPager = (CustomViewPager) findViewById(R.id.viewPagerQuestion);
        answersDrawer = (AnswersDrawer) getSupportFragmentManager().findFragmentById(R.id.answerFragment);

        //Last Stage of the game
        fetchFromSp();

        //Load and Options to the ArrayList
        setDataToArrayList();

        initilizeViewPager();
    }

    private void initilizeViewPager() {
        customViewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return QuestionFragment.newInstance(position, questions.get(position), a.get(position),
                        b.get(position), c.get(position), d.get(position), answer.get(position))
                        .setListener(QuizActivity.this);
            }

            @Override
            public int getCount() {
                return 100;
            }
        });
        customViewPager.setCurrentItem(qNo);
    }

    private void fetchFromSp() {
        qNo = getSharedPreferences("values", MODE_PRIVATE).getInt("played" + code, 0);
        score = getSharedPreferences("values", MODE_PRIVATE).getInt("score" + code, 0);

        answersDrawer.setInitialData(qNo, code);
    }

    @Override
    public void selected(boolean correct) {
        qNo++;
        if (correct)
            score++;
        answersDrawer.increaseSize();
        customViewPager.setCurrentItem(customViewPager.getCurrentItem() + 1);
    }

    public void setDataToArrayList() {
        try {
            JSONObject obj = new JSONObject(AssetJSONFile("question" + code + ".json", this));
            JSONArray m_jArry = obj.getJSONArray("questions");
            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                questions.add(jo_inside.getString("question"));
                a.add(jo_inside.getString("a"));
                b.add(jo_inside.getString("b"));
                c.add(jo_inside.getString("c"));
                d.add(jo_inside.getString("d"));
                answer.add(jo_inside.getString("ans"));
            }
        } catch (Exception ignored) {
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = getSharedPreferences("values", MODE_PRIVATE).edit();
        editor.putInt("played" + code, qNo);
        editor.putInt("score" + code, score);
        editor.apply();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END);
        } else {
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.action_answer) {
            if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                drawerLayout.closeDrawer(GravityCompat.END);
            } else {
                drawerLayout.openDrawer(GravityCompat.END);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_quiz, menu);
        return super.onCreateOptionsMenu(menu);
    }

}