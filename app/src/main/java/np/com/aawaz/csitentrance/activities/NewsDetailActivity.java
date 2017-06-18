package np.com.aawaz.csitentrance.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;

import com.google.firebase.appindexing.FirebaseUserActions;
import com.google.firebase.appindexing.builders.Actions;

import np.com.aawaz.csitentrance.R;
import np.com.aawaz.csitentrance.objects.EventSender;

public class NewsDetailActivity extends AppCompatActivity {

    Bundle bundle;
    private String mUrl;
    private String mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_each_news);
        new EventSender().logEvent("each_news");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_each_news);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }

        WebView
                newsDetail = (WebView) findViewById(R.id.each_news_detail);
        TextView time = (TextView) findViewById(R.id.each_news_time),
                title = (TextView) findViewById(R.id.each_news_title);

        bundle = getIntent().getBundleExtra("data");
        title.setText(bundle.getString("title"));
        newsDetail.loadDataWithBaseURL("", readyWithCSS(bundle.getString("detail")), "text/html", "UTF-8", "");
        time.setText(bundle.getString("time"));
        appIndexing();
    }

    private String readyWithCSS(String string) {
        String initial = "<html lang=\"en\">" +
                "<head>" +
                "  <meta charset=\"UTF-8\">" +
                "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "  <meta http-equiv=\"X-UA-Compatible\" content=\"ie=edge\">" +
                "  <title>Document</title>" +
                "  <style>" +
                "    body{" +
                "      margin: 0;" +
                "      padding: 5px;" +
                "    }" +
                "    img {" +
                "      width: 100%;" +
                "    }" +
                "    p {" +
                "      margin: 5px ;" +
                "      font-size: 18px;" +
                "      line-height: 1.5em;" +
                "      text-align: justify;" +
                "    }" +
                "  </style>" +
                "</head>" +
                "<body>";
        String footer = "</body>" +
                "</html>";
        return initial + string + footer;
    }


    private void appIndexing() {
        mUrl = "http://csitentrance.brainants.com/news";
        mTitle = bundle.getString("title");
    }


    public com.google.firebase.appindexing.Action getAction() {
        return Actions.newView(mTitle, mUrl);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUserActions.getInstance().start(getAction());
    }

    @Override
    public void onStop() {
        FirebaseUserActions.getInstance().end(getAction());
        super.onStop();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
