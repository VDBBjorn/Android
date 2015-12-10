package be.ugent.oomt.labo3;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import be.ugent.oomt.labo3.contentprovider.MessageProvider;
import be.ugent.oomt.labo3.contentprovider.database.DatabaseContract;

/**
 * Created by Bjorn on 10/12/15.
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String CONTACT_KEY = "contact";
    private TextView summary;
    private TextView title;

    public static DetailFragment newInstance(String contact) {
        DetailFragment f = new DetailFragment();

        Bundle args = new Bundle();
        args.putString(CONTACT_KEY, contact);
        f.setArguments(args);
        return f;
    }

    public String getShownContact() {
        return getArguments().getString(CONTACT_KEY, "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // TODO: change DetailFragment to show selected user feed and initialize loader

        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        title = (TextView) view.findViewById(R.id.detail_title);
        summary = (TextView) view.findViewById(R.id.detail_summary);
        title.setText(getShownContact());
        summary.setText("");

        getLoaderManager().initLoader(0, null, this);

        return view;
    }

    // TODO: implement LoaderManager.LoaderCallbacks<Cursor> interface

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                DatabaseContract.Message._ID,
                DatabaseContract.Message.COLUMN_NAME_MESSAGE
        };
        String orderBy = DatabaseContract.Message.COLUMN_NAME_DATE + " ASC";
        return new CursorLoader(getActivity(), MessageProvider.MESSAGES_CONTENT_URL, projection, DatabaseContract.Message.COLUMN_NAME_CONTACT + " =?", new String[]{getShownContact()}, orderBy);
    }

    // TODO: on cursor load finish append all messages to text view

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        summary.setText("");
        while (data.moveToNext()) {
            summary.append(data.getString(data.getColumnIndex(DatabaseContract.Message.COLUMN_NAME_MESSAGE)) + "\n");
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}
