package triviaquiz.androidbook.com.beentheredonethat;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class QuizHelpActivity extends QuizActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);
        InputStream iFile = getResources().openRawResource(R.raw.quizhelp);
        TextView helpText = (TextView) findViewById(R.id.textview_helptext);
        helpText.setMovementMethod(new ScrollingMovementMethod());
        try {
            String helpStr = inputStreamToString(iFile);
            helpText.setText(helpStr);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public String inputStreamToString(InputStream inputStream) throws IOException {
        StringBuffer stringBuffer = new StringBuffer();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null){
                stringBuffer.append(line);
                line = reader.readLine();
            }
        }
        return stringBuffer.toString();
    }

}