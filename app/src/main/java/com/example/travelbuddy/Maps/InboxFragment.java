package com.example.travelbuddy.Maps;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelbuddy.R;

import java.util.ArrayList;
import java.util.List;

public class InboxFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private RecyclerView rvInbox;
    private InboxAdapter adapter;

    public InboxFragment() {
        // Required empty public constructor
    }

    public static InboxFragment newInstance(String param1, String param2) {
        InboxFragment fragment = new InboxFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    // GIVES FRAGMENT PADDING
    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_inbox, container, false);

        View root = view.findViewById(R.id.inbox_root);
        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            int top = insets.getInsets(WindowInsetsCompat.Type.systemBars()).top;
            v.setPadding(
                    v.getPaddingLeft(),
                    top + dpToPx(16),
                    v.getPaddingRight(),
                    v.getPaddingBottom()
            );
            return insets;
        });

        // RecyclerView
        rvInbox = view.findViewById(R.id.rv_inbox);
        rvInbox.setLayoutManager(new LinearLayoutManager(getContext()));

        // Tabs
        LinearLayout tabAlerts = view.findViewById(R.id.tab_alerts);
        LinearLayout tabTips   = view.findViewById(R.id.tab_tips);
        LinearLayout tabOffers = view.findViewById(R.id.tab_offers);

        TextView tvAlerts = view.findViewById(R.id.tv_tab_alerts);
        TextView tvTips   = view.findViewById(R.id.tv_tab_tips);
        TextView tvOffers = view.findViewById(R.id.tv_tab_offers);

        View underlineAlerts = view.findViewById(R.id.underline_alerts);
        View underlineTips   = view.findViewById(R.id.underline_tips);
        View underlineOffers = view.findViewById(R.id.underline_offers);

        // initial state = Alerts selected
        adapter = new InboxAdapter(buildAlerts());
        rvInbox.setAdapter(adapter);
        setSelectedTab(tvAlerts, underlineAlerts,
                tvTips, underlineTips,
                tvOffers, underlineOffers);

        tabAlerts.setOnClickListener(v -> {
            adapter.setData(buildAlerts());
            setSelectedTab(tvAlerts, underlineAlerts,
                    tvTips, underlineTips,
                    tvOffers, underlineOffers);
        });

        tabTips.setOnClickListener(v -> {
            adapter.setData(buildTips());
            setSelectedTab(tvTips, underlineTips,
                    tvAlerts, underlineAlerts,
                    tvOffers, underlineOffers);
        });

        tabOffers.setOnClickListener(v -> {
            adapter.setData(buildOffers());
            setSelectedTab(tvOffers, underlineOffers,
                    tvAlerts, underlineAlerts,
                    tvTips, underlineTips);
        });

        return view;
    }

    // Moves the underline + changes colors
    private void setSelectedTab(TextView selectedTv, View selectedLine,
                                TextView otherTv1, View otherLine1,
                                TextView otherTv2, View otherLine2) {

        int skyblue = ContextCompat.getColor(requireContext(), R.color.skyblue);
        int defaultColor = ContextCompat.getColor(requireContext(), R.color.darkblack);

        selectedTv.setTextColor(skyblue);
        selectedTv.setTypeface(selectedTv.getTypeface(), Typeface.BOLD);
        selectedLine.setBackgroundColor(skyblue);

        otherTv1.setTextColor(defaultColor);
        otherTv1.setTypeface(null, Typeface.NORMAL);
        otherLine1.setBackgroundColor(Color.TRANSPARENT);

        otherTv2.setTextColor(defaultColor);
        otherTv2.setTypeface(null, Typeface.NORMAL);
        otherLine2.setBackgroundColor(Color.TRANSPARENT);
    }

    // ---------- dummy data ----------

    private List<InboxItem> buildAlerts() {
        List<InboxItem> list = new ArrayList<>();
        list.add(new InboxItem("Your 2025 stats are here!",
                "The year I die will be the best year"));
        list.add(new InboxItem("Flight delay",
                "Your Tokyo flight has been delayed by 30 minutes."));
        return list;
    }

    private List<InboxItem> buildTips() {
        List<InboxItem> list = new ArrayList<>();
        list.add(new InboxItem("Tokyo metro tip",
                "Use IC cards to switch lines without buying new tickets."));
        list.add(new InboxItem("Restaurant peak times",
                "Most places are packed 7–9pm. Consider early dinners."));
        return list;
    }

    private List<InboxItem> buildOffers() {
        List<InboxItem> list = new ArrayList<>();
        list.add(new InboxItem("Hotel discount",
                "Get 10% off IVE Hotel when you book through TravelBuddy."));
        return list;
    }

    // ---------- model + adapter ----------

    static class InboxItem {
        String title, body;
        InboxItem(String t, String b) { title = t; body = b; }
    }

    static class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.VH> {

        private List<InboxItem> data;

        InboxAdapter(List<InboxItem> data) {
            this.data = data;
        }

        void setData(List<InboxItem> newData) {
            this.data = newData;
            notifyDataSetChanged();
        }

        static class VH extends RecyclerView.ViewHolder {
            TextView tvTitle, tvBody;
            VH(@NonNull View itemView) {
                super(itemView);
                tvTitle = itemView.findViewById(R.id.tv_title);
                tvBody  = itemView.findViewById(R.id.tv_body);
            }
        }

        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_inbox_message, parent, false);
            return new VH(v);
        }

        @Override
        public void onBindViewHolder(@NonNull VH holder, int position) {
            InboxItem item = data.get(position);
            holder.tvTitle.setText(item.title);
            holder.tvBody.setText(item.body);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }
}
