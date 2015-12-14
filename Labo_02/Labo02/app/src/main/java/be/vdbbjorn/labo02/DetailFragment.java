package be.vdbbjorn.labo02;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {

    public int getShownIndex() {
        return getArguments().getInt("index", 0);
    }

    public static DetailFragment newInstance(int index) {
        DetailFragment f = new DetailFragment();

        Bundle args = new Bundle();
        args.putInt("index", index);
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        TextView title = (TextView) view.findViewById(R.id.superhero_name);
        TextView summary = (TextView) view.findViewById(R.id.superhero_history);
        title.setText(getResources().getStringArray(R.array.superheroes_names)[getShownIndex()]);
        summary.setText(getResources().getStringArray(R.array.superheroes_history)[getShownIndex()]);
        return view;
    }
}
