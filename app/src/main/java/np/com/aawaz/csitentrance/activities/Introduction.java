package np.com.aawaz.csitentrance.activities;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import np.com.aawaz.csitentrance.R;
import np.com.aawaz.csitentrance.advance.CirclePageIndicator;
import np.com.aawaz.csitentrance.fragments.PageFragment;


public class Introduction extends AppCompatActivity {

    ViewPager introViewPager;
    CirclePageIndicator indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);

        indicator = (CirclePageIndicator) findViewById(R.id.pagerIndicator);
        introViewPager = (ViewPager) findViewById(R.id.introViewPager);

        final FragmentPagerAdapter viewPager = new CustomPagerAdapter(getSupportFragmentManager());
        introViewPager.setAdapter(viewPager);
        final int colors[] = {Color.parseColor("#b93791"), Color.parseColor("#409bd6"), Color.parseColor("#ed2669"), Color.parseColor("#996c5a"),
                Color.parseColor("#4f51b5"), Color.parseColor("#e37654"), Color.parseColor("#394f64"), 0};
        indicator.setViewPager(introViewPager);
        introViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (android.os.Build.VERSION.SDK_INT >= 11) {
                    ArgbEvaluator argbEvaluator = new ArgbEvaluator();
                    introViewPager.setBackgroundColor((Integer) argbEvaluator.evaluate(positionOffset, colors[position], colors[position + 1]));
                } else {
                    introViewPager.setBackgroundColor(colors[position]);
                }

            }


            @Override
            public void onPageSelected(int position) {


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    public void startMainActivity(View v) {
        Intent main = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(main);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private class CustomPagerAdapter extends FragmentPagerAdapter {


        public CustomPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            PageFragment frag = new PageFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("position", position);
            frag.setArguments(bundle);
            return frag;
        }

        @Override
        public int getCount() {
            return 7;
        }
    }
}
