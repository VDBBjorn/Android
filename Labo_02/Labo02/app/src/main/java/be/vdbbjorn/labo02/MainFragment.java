package be.vdbbjorn.labo02;


import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends ListFragment {

    boolean mDuelPane;
    int mCurCheckPosition = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ListAdapter listAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_activated_1,
                getResources().getStringArray(R.array.superheroes_names));
        setListAdapter(listAdapter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mCurCheckPosition = savedInstanceState.getInt("curChoice", 0);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("curChoice", mCurCheckPosition);
    }

}
