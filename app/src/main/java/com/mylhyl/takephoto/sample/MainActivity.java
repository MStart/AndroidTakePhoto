package com.mylhyl.takephoto.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null)
            getSupportFragmentManager().beginTransaction()
                    .add(android.R.id.content, MainFragment.newInstance())
                    .commitAllowingStateLoss();
    }

    public static class MainFragment extends ListFragment {

        public static MainFragment newInstance() {
            return new MainFragment();
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            setListAdapter(new ArrayAdapter<>(getActivity(),
                    android.R.layout.simple_list_item_1,
                    new String[]{"TakeList", "TakeThumbnail"}));
        }

        @Override
        public void onListItemClick(ListView l, View v, int position, long id) {
            switch (position) {
                case 0:
                    TakeListActivity.gotoActivity(getActivity());
                    break;
                case 1:
                    TakeThumbnailActivity.gotoActivity(getActivity());
                    break;
            }
        }
    }
}
