package cz.greapp.sportmateslite;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FindGameFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FindGameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FindGameFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    Spinner sportSpinner;
    RecyclerView gamesListView;
    RecyclerView.Adapter gamesListAdapter;
    RecyclerView.LayoutManager gamesListLayoutManager;
    List<Game> games;
    Context ctx;

    public FindGameFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FindGameFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FindGameFragment newInstance(String param1, String param2) {
        FindGameFragment fragment = new FindGameFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_find_game, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ctx = getContext();

        sportSpinner = (Spinner) view.findViewById(R.id.sportSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.sports_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sportSpinner.setAdapter(adapter);

        gamesListView = (RecyclerView) view.findViewById(R.id.gamesListView);

        gamesListLayoutManager = new LinearLayoutManager(getContext());
        gamesListView.setLayoutManager(gamesListLayoutManager);

        games = new ArrayList<Game>();
        games.add(new Game(new Sport("Posilování"), "Buly Aréna Kravaře", "21.12.2018", "16:00", "18:00"));
        games.add(new Game(new Sport("Badminton"), "Buly Aréna Kravaře", "21.12.2018", "16:00", "18:00"));
        games.add(new Game(new Sport("Tenis"), "Buly Aréna Kravaře", "22.12.2018", "16:00", "18:00"));
        games.add(new Game(new Sport("Posilování"), "Buly Aréna Kravaře", "23.12.2018", "16:00", "18:00"));
        games.add(new Game(new Sport("Squash"), "Buly Aréna Kravaře", "23.12.2018", "16:00", "18:00"));
        games.add(new Game(new Sport("Posilování"), "Buly Aréna Kravaře", "23.12.2018", "16:00", "18:00"));
        games.add(new Game(new Sport("Horolezectví"), "Buly Aréna Kravaře", "23.12.2018", "16:00", "18:00"));
        games.add(new Game(new Sport("Tenis"), "Buly Aréna Kravaře", "23.12.2018", "16:00", "18:00"));
        games.add(new Game(new Sport("Posilování"), "Buly Aréna Kravaře", "23.12.2018", "16:00", "18:00"));
        gamesListAdapter = new GameAdapter(games);
        gamesListView.setAdapter(gamesListAdapter);

        gamesListView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), gamesListView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Intent intent = new Intent(ctx, GameActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("game", games.get(position));
                        intent.putExtras(bundle);
                        startActivity(intent);

                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
