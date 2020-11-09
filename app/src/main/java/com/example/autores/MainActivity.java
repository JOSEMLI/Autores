package com.example.autores;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {

    private EditText inputbook;
    private TextView booktitle, bookAuthor,imagelinks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputbook = findViewById(R.id.inputbook);
        booktitle = findViewById(R.id.tittle);
        bookAuthor = findViewById(R.id.bookAuthor);
        imagelinks = findViewById(R.id.imagelinks);


    }

    public void searchBook(View view) {

        String searchtring = inputbook.getText().toString();

        //todo requerir el uso del servicio externo

        //String searchString = inputbook.getText().toString();
        new GetBook(booktitle,bookAuthor,imagelinks).execute(searchtring);

    }

    public class GetBook extends AsyncTask<String , Void , String>
    {

        private WeakReference<TextView> mTextTittle, mTextAuthor, mimagelinks;

        public GetBook(TextView mTextTittle, TextView mTextAuthor , TextView mimagelinks) {

            this.mTextTittle = new WeakReference<>(mTextTittle);
            this.mTextAuthor = new WeakReference<>(mTextAuthor);
            this.mimagelinks = new WeakReference<>(mimagelinks);
        }

        @Override
        protected String doInBackground(String... strings) {
            return NewUtilities.getBookInfo(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try{
                JSONObject jsonObject = new JSONObject(s);
                JSONArray itemsArray = jsonObject.getJSONArray("items");

                int i =0;
                String title = null;
                String author = null;
                String image = null;

                while (i < itemsArray.length() && title == null && author==null && image == null )
                {
                    JSONObject book = itemsArray.getJSONObject(i);
                    JSONObject volumeInfo = book.getJSONObject("volumeInfo");

                    try {
                        title = volumeInfo.getString("title");
                        author = volumeInfo.getString("authors");
                        image = volumeInfo.getString("imageLinks");

                    }catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                    i++;

                }
                if (title != null && author != null )
                {
                    mTextTittle.get().setText(title);
                    mTextAuthor.get().setText(author);
                    mimagelinks.get().setText(image);
                }
                else
                {
                    mTextTittle.get().setText("No existe resultado para la consulta");
                    mTextAuthor.get().setText("");
                    mimagelinks.get().setText("");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}