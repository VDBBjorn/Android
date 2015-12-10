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
import android.widget.CursorAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import be.ugent.oomt.labo3.contentprovider.MessageProvider;
import be.ugent.oomt.labo3.contentprovider.database.DatabaseContract;

/**
 * Created by bjorn on 10/12/2015.
 */
public class MainFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String STATE_ACTIVATED_POSITION = "activated_position";
    private final String TAG = "MainFragment";
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
        String[] from = new String[]{
                DatabaseContract.Contact.COLUMN_NAME_CONTACT,
                DatabaseContract.Contact.COLUMN_NAME_STATE,
        };
        int[] to = new int[]{
                android.R.id.text1, android.R.id.text2
        };
        ListAdapter listAdapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_activated_2, null, from, to, 0);
        setListAdapter(listAdapter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mCurCheckPosition = savedInstanceState.getInt(STATE_ACTIVATED_POSITION, mCurCheckPosition);
        }

        View detailsFrame = getActivity().findViewById(R.id.detail_container);
        mDuelPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;
        if (mDuelPane) {
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_ACTIVATED_POSITION, mCurCheckPosition);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        showDetails(position);
    }

    // TODO: implement LoaderManager.LoaderCallbacks<Cursor> interface

    private void showDetails(int index) {
        final Cursor c = (Cursor) getListView().getItemAtPosition(index);
        if (c != null) {
            final String contact = c.getString(c.getColumnIndex(DatabaseContract.Contact.COLUMN_NAME_CONTACT));

            if (mDuelPane) {
                getListView().setItemChecked(index, true);

                DetailFragment details = (DetailFragment) getFragmentManager().findFragmentById(R.id.detail_container);
                if (details == null || details.getShownContact() != contact) {
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.detail_container, DetailFragment.newInstance(contact))
                            .commit();
                }
            } else {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("contact", contact);
                startActivity(intent);
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "create loader");
        String[] projection = {
                DatabaseContract.Contact.COLUMN_NAME_CONTACT,
                DatabaseContract.Contact.COLUMN_NAME_STATE,
        };
        String orderBy = DatabaseContract.Contact.COLUMN_NAME_CONTACT + " ASC";
        return new CursorLoader(getActivity(), MessageProvider.CONTACTS_CONTENT_URL, projection, null, null, orderBy);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, "load finished");
        ((CursorAdapter) getListAdapter()).swapCursor(data);
        if (mDuelPane) {
            handler.sendEmptyMessage(2);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(TAG, "loader reset");
        ((CursorAdapter) getListAdapter()).swapCursor(null);
    }
}
