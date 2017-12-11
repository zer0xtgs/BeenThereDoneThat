package triviaquiz.androidbook.com.beentheredonethat;

import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Arrays;

public class QuizsScoreActivity extends QuizActivity {
    // Const
    private final String DEBUG_TAG = "Score:";
    public static final int RC_SIGN_IN = 1;
    private static final int RC_XLM_PICKER = 2;
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;

    // Firebase instance variables
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private FirebaseStorage mFirebaseStorage;
    private StorageReference mStorageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score);

        // Initialize Firebase components
        mAuth = FirebaseAuth.getInstance();

        mFirebaseStorage = FirebaseStorage.getInstance();
        mStorageReference = mFirebaseStorage.getReference().child("score_data");

        // Set up the tabs
        TabHost host = (TabHost) findViewById(R.id.TabHost1);
        host.setup();
        // All Scores tab
        TabHost.TabSpec allScoresTab = host.newTabSpec("allTab");
        allScoresTab.setIndicator(getResources().getString(R.string.all_scores), getResources().getDrawable(
                android.R.drawable.star_on));
        allScoresTab.setContent(R.id.ScrollViewAllScores);
        host.addTab(allScoresTab);
        // Friends Scores tab
        TabHost.TabSpec friendScoresTab = host.newTabSpec("friendsTab");
        friendScoresTab.setIndicator(getResources().getString(R.string.friends_scores), getResources().getDrawable(
                android.R.drawable.star_on));
        friendScoresTab.setContent(R.id.ScrollViewFriendScores);
        host.addTab(friendScoresTab);
        // Set the default tab
        host.setCurrentTabByTag("allTab");

        // Retrieve the TableLayout references
        TableLayout allScoresTable = (TableLayout) findViewById(R.id.TableLayout_AllScores);
        TableLayout friendScoresTable = (TableLayout) findViewById(R.id.TableLayout_FriendScores);
        // Give each TableLayout a yellow header row with the column names
        initializeHeaderRow(allScoresTable);
        initializeHeaderRow(friendScoresTable);
        XmlResourceParser mockAllScores = getResources().getXml(R.xml.allscores);
        XmlResourceParser mockFriendScores = getResources().getXml(R.xml.friendscores);
        try {
            processScores(allScoresTable, mockAllScores);
            processScores(friendScoresTable, mockFriendScores);
        } catch (Exception e) {
            Log.e(DEBUG_TAG, "Failed to load scores", e);
        }

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Toast.makeText(QuizsScoreActivity.this, "You are signed in", Toast.LENGTH_SHORT).show();
                } else {
                    // User is signed out
                    startActivityForResult(AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAllowNewEmailAccounts(false)
                                    .setAvailableProviders(
                                            Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                                    new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build())).build(),
                            RC_SIGN_IN);

                }
            }
        };


    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    /**
     * Add a header {@code TableRow} to the {@code TableLayout} (styled)
     *
     * @param scoreTable the {@code TableLayout} that the header row will be added to
     */
    private void initializeHeaderRow(TableLayout scoreTable) {
        // Create the Table header row
        TableRow headerRow = new TableRow(this);
        int textColor = getResources().getColor(R.color.mainFontColor);
        float textSize = 32;
        addTextToRowWithValues(headerRow, getResources().getString(R.string.username), textColor, textSize);
        addTextToRowWithValues(headerRow, getResources().getString(R.string.score), textColor, textSize);
        addTextToRowWithValues(headerRow, getResources().getString(R.string.rank), textColor, textSize);
        scoreTable.addView(headerRow);
    }

    /**
     * Churn through an XML score information and populate a {@code TableLayout}
     *
     * @param scoreTable The {@code TableLayout} to populate
     * @param scores     A standard {@code XmlResourceParser} containing the scores
     * @throws XmlPullParserException Thrown on XML errors
     * @throws IOException            Thrown on IO errors reading the XML
     */
    private void processScores(final TableLayout scoreTable, XmlResourceParser scores) throws XmlPullParserException,
            IOException {
        int eventType = -1;
        boolean bFoundScores = false;
        // Find Score records from XML
        while (eventType != XmlResourceParser.END_DOCUMENT) {
            if (eventType == XmlResourceParser.START_TAG) {
                // Get the name of the tag (eg scores or score)
                String strName = scores.getName();
                if (strName.equals("score")) {
                    bFoundScores = true;
                    String scoreValue = scores.getAttributeValue(null, "score");
                    String scoreRank = scores.getAttributeValue(null, "rank");
                    String scoreUserName = scores.getAttributeValue(null, "username");
                    insertScoreRow(scoreTable, scoreValue, scoreRank, scoreUserName);
                }
            }
            eventType = scores.next();
        }
        // Handle no scores available
        if (bFoundScores == false) {
            final TableRow newRow = new TableRow(this);
            TextView noResults = new TextView(this);
            noResults.setText(getResources().getString(R.string.no_scores));
            newRow.addView(noResults);
            scoreTable.addView(newRow);
        }
    }

    /**
     * {@code processScores()} helper method -- Inserts a new score {@code
     * TableRow} in the {@code TableLayout}
     *
     * @param scoreTable    The {@code TableLayout} to add the score to
     * @param scoreValue    The value of the score
     * @param scoreRank     The ranking of the score
     * @param scoreUserName The user who made the score
     */
    private void insertScoreRow(final TableLayout scoreTable, String scoreValue, String scoreRank, String scoreUserName) {
        final TableRow newRow = new TableRow(this);
        int textColor = getResources().getColor(R.color.mainFontColor);
        float textSize = 24;
        addTextToRowWithValues(newRow, scoreUserName, textColor, textSize);
        addTextToRowWithValues(newRow, scoreValue, textColor, textSize);
        addTextToRowWithValues(newRow, scoreRank, textColor, textSize);
        scoreTable.addView(newRow);
    }

    /**
     * {@code insertScoreRow()} helper method -- Populate a {@code TableRow} with
     * three columns of {@code TextView} data (styled)
     *
     * @param tableRow  The {@code TableRow} the text is being added to
     * @param text      The text to add
     * @param textColor The color to make the text
     * @param textSize  The size to make the text
     */
    private void addTextToRowWithValues(final TableRow tableRow, String text, int textColor, float textSize) {
        TextView textView = new TextView(this);
        textView.setTextSize(textSize);
        textView.setTextColor(textColor);
        textView.setText(text);
        tableRow.addView(textView);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // Sign-in succeeded, set up the UI
                Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                // Sign in was canceled by the user, finish the activity
                Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else if (requestCode == RC_XLM_PICKER && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();

            // Get a reference to store file at chat_photos/<FILENAME>
            StorageReference photoRef = mStorageReference.child(selectedImageUri.getLastPathSegment());

            // Upload file to Firebase Storage
            photoRef.putFile(selectedImageUri)
                    .addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(QuizsScoreActivity.this,"UPLOAD DONE", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(QuizsScoreActivity.this,"UPLOAD FAIL", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("text/xml");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select file"), RC_XLM_PICKER);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.upload_data:
                showFileChooser();
                return true;
            case R.id.logout:
                AuthUI.getInstance().signOut(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}


