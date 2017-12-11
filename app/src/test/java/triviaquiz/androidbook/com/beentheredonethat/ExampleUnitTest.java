package triviaquiz.androidbook.com.beentheredonethat;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void test1(){
        assertEquals(5, QuizGameActivity.getUserId());
    }

    @Test
    public void test2(){
        assertEquals(11, QuizActivity.QUESTION_BATCH_SIZE);
    }

    @Test
    public void test3(){
        assertEquals(0, QuizSettingsActivity.DATE_DIALOG_ID);
    }


}