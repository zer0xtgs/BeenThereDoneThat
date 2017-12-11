package triviaquiz.androidbook.com.beentheredonethat;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class QuizMenuActivity extends QuizActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        LayoutInflater ltInflater = getLayoutInflater();
        ListView menuList = (ListView) findViewById(R.id.ListView_Menu);
        String[] menuItems = {
                getResources().getString(R.string.menu_item_play),
                getResources().getString(R.string.scores),
                getResources().getString(R.string.settings),
                getResources().getString(R.string.help)};
        Integer[] imgId = {R.drawable.play,
                R.drawable.scores,
                R.drawable.settings,
                R.drawable.help};
        CustomListAdapter adapter = new CustomListAdapter(this, menuItems, imgId);
        menuList.setAdapter(adapter);

//        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(this, R.layout.menu_item, menuItems);
//        menuList.setAdapter(stringArrayAdapter);

        menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {
                TextView textView = (TextView) itemClicked.findViewById(R.id.list_text_view);
                String strText = textView.getText().toString();
                if (strText.equalsIgnoreCase(getResources().getString(R.string.menu_item_play))) {
                    startActivity(new Intent(QuizMenuActivity.this, QuizGameActivity.class));
                } else if (strText.equalsIgnoreCase(getResources().getString(R.string.help))) {
                    startActivity(new Intent(QuizMenuActivity.this, QuizHelpActivity.class));
                } else if (strText.equalsIgnoreCase(getResources().getString(R.string.settings))) {
                    startActivity(new Intent(QuizMenuActivity.this, QuizSettingsActivity.class));
                } else if (strText.equalsIgnoreCase(getResources().getString(R.string.scores))) {
                    startActivity(new Intent(QuizMenuActivity.this, QuizsScoreActivity.class));
                }
            }
        });
    }
}
