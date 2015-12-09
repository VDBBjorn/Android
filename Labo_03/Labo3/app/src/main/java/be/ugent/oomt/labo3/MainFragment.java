package be.ugent.oomt.labo3;


import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import be.ugent.oomt.labo3.contentprovider.MessageProvider;
import be.ugent.oomt.labo3.contentprovider.database.DatabaseContract;

/**
 * Created by elias on 12/01/15.
 */
public class MainFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "MainFragment";
    boolean mDuelPane;
    int mCurCheckPosition = 0;
    private Handler handler = new Handler() { // handler for commiting fragment after data is loaded
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 2) {
                Log.d(TAG, "Onload finished : handler called. setting the fragment.");
                // commit the fragment
                showDetails(mCurCheckPosition);
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: initialize asynchronous loader
        getLoaderManager().initLoader(0, null, this);

        // TODO: Change ArrayAdapter to SimpleCursorAdapter to access the ContentProvider
        ListAdapter listAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_activated_1,
                getResources().getStringArray(R.array.superheroes_names));
        setListAdapter(listAdapter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mCurCheckPosition = savedInstanceState.getInt("curChoice", 0);
        }

        View detailsFrame = getActivity().findViewById(R.id.detail_container);
        mDuelPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;
        if (mDuelPane) {
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            showDetails(mCurCheckPosition);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("curChoice", mCurCheckPosition);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        showDetails(position);
    }

    private void showDetails(int index) {
        mCurCheckPosition = index;

        if (mDuelPane) {
            getListView().setItemChecked(index, true);

            DetailFragment details = (DetailFragment) getFragmentManager().findFragmentById(R.id.detail_container);
            if (details == null || details.getShownIndex() != index) {
                details = DetailFragment.newInstance(index);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.detail_container, details)
                        .commit();
            }
        } else {
            Intent intent = new Intent(getActivity(), DetailActivity.class);
            intent.putExtra("index", index);
            startActivity(intent);
        }
    }

    // TODO: implement LoaderManager.LoaderCallbacks<Cursor> interface
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                DatabaseContract.Contact.COLUMN_NAME_CONTACT,
                DatabaseContract.Contact.COLUMN_NAME_STATE,
        };
        //String orderBy = DatabaseContract.Contact.COLUMN_NAME_CONTACT + " = \"" + MqttHandler.clientId +  "\" DESC," + DatabaseContract.Contact.COLUMN_NAME_CONTACT + " ASC";
        return new CursorLoader(getActivity(), MessageProvider.CONTACTS_CONTENT_URL, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        ((CursorAdapter) getListAdapter()).swapCursor(data);
        if (mDuelPane) {
            handler.sendEmptyMessage(2);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        ((CursorAdapter) getListAdapter()).swapCursor(null);
    }
}
