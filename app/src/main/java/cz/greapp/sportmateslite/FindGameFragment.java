package cz.greapp.sportmateslite;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import cz.greapp.sportmateslite.Data.Adapters.GameAdapter;
import cz.greapp.sportmateslite.Data.Models.Game;
import cz.greapp.sportmateslite.Data.Models.User;
import cz.greapp.sportmateslite.Data.OnFirebaseQueryResultListener;
import cz.greapp.sportmateslite.Data.Parsers.GameSnapshotParser;
import cz.greapp.sportmateslite.Data.TableGateways.GameTableGateway;
import cz.greapp.sportmateslite.Data.TableGateways.TableGateway;
import cz.greapp.sportmateslite.Listeners.RecyclerItemClickListener;

import static android.view.View.GONE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FindGameFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FindGameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FindGameFragment extends Fragment implements OnFirebaseQueryResultListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int REQUEST_SPORTS = 5;
    public static final int REQUEST_GAMES = 6;

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

    TextView noGameText;

    SwipeRefreshLayout swipeRefreshLayout;

    OnFirebaseQueryResultListener listener;

    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;

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
        listener = this;

        preferences = getActivity().getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE);

        sportSpinner = (Spinner) view.findViewById(R.id.sportSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.sports_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sportSpinner.setAdapter(adapter);
        sportSpinner.setSelection(preferences.getInt("selected_filter",0));

        noGameText = view.findViewById(R.id.noGamesTextView);

        sportSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                prefEditor = preferences.edit();
                prefEditor.putInt("selected_filter", i);
                prefEditor.putString("selected_filter_name", adapterView.getItemAtPosition(i).toString());
                prefEditor.commit();

                GameTableGateway gw = new GameTableGateway();
                swipeRefreshLayout.setRefreshing(true);
                gw.getGames(null, REQUEST_GAMES);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        gamesListView = (RecyclerView) view.findViewById(R.id.gamesListView);

        gamesListLayoutManager = new LinearLayoutManager(getContext());
        gamesListView.setLayoutManager(gamesListLayoutManager);

        games = new ArrayList<Game>();
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

        swipeRefreshLayout = view.findViewById(R.id.gamesSwipeRefresh);
        swipeRefreshLayout.setRefreshing(true);
        GameTableGateway gw = new GameTableGateway();
        gw.getGames(this, REQUEST_GAMES);


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                GameTableGateway gw = new GameTableGateway();
                gw.getGames(listener, REQUEST_GAMES);
            }
        });

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

    @Override
    public void onFirebaseQueryResult(int resultCode, int requestCode, QuerySnapshot result) {

        if (requestCode == REQUEST_GAMES) {
            if (resultCode == TableGateway.RESULT_OK) {
                GameSnapshotParser gsp = new GameSnapshotParser();
                games = gsp.parseQuerySnapshot(result);
                Iterator<Game> gameIterator = games.iterator();
                Game g;
                User user = new User(preferences.getString("username", null), preferences.getString("useremail", null));
                SimpleDateFormat sdf =  new SimpleDateFormat("dd.MM.yyyy HH:mm");
                Date now = Calendar.getInstance().getTime();

                while (gameIterator.hasNext()) {
                    boolean remove = false;
                    g = gameIterator.next();
                    try {
                        if (g.getPlayers().size() == 2 || g.getPlayers().get(0).getEmail().equals(user.getEmail()) || g.getPlayers().get(1).getEmail().equals(user.getEmail())) {
                            remove = true;
                        }
                    }
                    catch(IndexOutOfBoundsException e) {

                    }
                    if (preferences.getInt("selected_filter", 0) != 0 && !g.getSport().getName().equals(preferences.getString("selected_filter_name", "")))
                    {
                        remove = true;
                    }

                    try {
                        if (sdf.parse(g.getDate() + " " + g.getTimeFrom()).before(now)) {
                            remove = true;
                        }

                        if (remove) {
                            gameIterator.remove();
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                if (games.size() == 0) {
                    gamesListView.setVisibility(GONE);
                    noGameText.setVisibility(View.VISIBLE);
                }

                else {
                    gamesListView.setVisibility(View.VISIBLE);
                    noGameText.setVisibility(GONE);
                }
                Collections.sort(games);
                gamesListAdapter = new GameAdapter(games);
                gamesListView.setAdapter(gamesListAdapter);
                swipeRefreshLayout.setRefreshing(false);
            }
            else {
                Toast.makeText(ctx, "Chyba při hledání her.", Toast.LENGTH_SHORT).show();
            }
        }
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
