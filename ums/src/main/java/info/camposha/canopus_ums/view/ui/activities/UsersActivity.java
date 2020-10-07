package info.camposha.canopus_ums.view.ui.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.AbsListView;
import android.widget.ImageButton;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import info.camposha.canopus_ums.R;
import info.camposha.canopus_ums.common.UserConstants;
import info.camposha.canopus_ums.common.UserUtils;
import info.camposha.canopus_ums.data.model.entity.User;
import info.camposha.canopus_ums.databinding.ActivityListingBinding;
import info.camposha.canopus_ums.view.adapter.UsersAdapter;
import info.camposha.canopus_ums.view.ui.base.AccountBaseActivity;

import static info.camposha.canopus_ums.common.UserUtils.MEM_CACHE;

public class UsersActivity extends AccountBaseActivity {

    //We define our instance fields
    private ActivityListingBinding b;

    private final String ITEMS_PER_PAGE = "20";
    private Boolean isScrolling = false;
    private int current, total, scrolledOut;
    //    private ArrayList<User> Users = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private StaggeredGridLayoutManager slm;
    private boolean alreadyReachedEnd = false;
    private UsersAdapter adapter;
    private boolean isGrid = false;

    private void initializeViews() {
        slm = new StaggeredGridLayoutManager(2, RecyclerView.VERTICAL);
        slm.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        b.rv.setLayoutManager(slm);
        adapter = new UsersAdapter(MEM_CACHE);
        b.rv.setAdapter(adapter);

        swipeRefreshLayout = findViewById(R.id.swipe_to_refresh);
        ImageButton newBtn = findViewById(R.id.toggleBtn);
        newBtn.setOnClickListener(v -> {
            if (isGrid) {
                slm = new StaggeredGridLayoutManager(2, RecyclerView.VERTICAL);
                slm.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
                b.rv.setLayoutManager(slm);
                newBtn.setImageResource(R.drawable.list_24px);
            } else {
                b.rv.setLayoutManager(new LinearLayoutManager(this));
                newBtn.setImageResource(R.drawable.grid_24px);
            }
            adapter = new UsersAdapter(MEM_CACHE);
            b.rv.setAdapter(adapter);
            isGrid = !isGrid;
        });
        listenToRecyclerViewScroll();
        listenToRefresh();
    }

    private void listenToRefresh() {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            alreadyReachedEnd = false;
            MEM_CACHE.clear();
            if (MEM_CACHE.size() > Integer.parseInt(ITEMS_PER_PAGE)) {
                fetch("0", String.valueOf(MEM_CACHE.size()));
            } else {
                fetch("0", ITEMS_PER_PAGE);
            }
        });
    }

    /**
     * This method will download for us data from php mysql based on supplied query string
     * as well as pagination parameters. We are basiclally searching or selecting data
     * without searching. However all the arriving data is paginated at the server level.
     */
    private void fetch(final String start, String limit) {
        swipeRefreshLayout.post(() -> swipeRefreshLayout.setRefreshing(true));
        getViewModel().search("", start, limit).observe(this, r -> {
            swipeRefreshLayout.post(() -> swipeRefreshLayout.setRefreshing(false));
            int result = makeRequest(r, "Users DOWNLOAD");
            if (result == UserConstants.SUCCEEDED) {
                if (r.getUsers().size() > 0) {
                    for (User i : r.getUsers()) {
                        if (!UserUtils.itemExists(i.getId(), MEM_CACHE)) {
                            //Users.add(i);
                            MEM_CACHE.add(i);
                        }
                    }
                } else {
                    show("Reached End");
                    alreadyReachedEnd = true;
                }
            }
            adapter.notifyDataSetChanged();
        });
    }


    /**
     * We will listen to scroll events. This is important as we are implementing scroll to
     * load more data pagination technique
     */
    private void listenToRecyclerViewScroll() {
        b.rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView rv, int newState) {
                //when scrolling starts
                super.onScrollStateChanged(rv, newState);
                //check for scroll state
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(RecyclerView rv, int dx, int dy) {
                // When the scrolling has stopped
                super.onScrolled(rv, dx, dy);

                //getChildCount() returns the current number of child views attached to the
                // parent RecyclerView.
                current = slm.getChildCount();
                //getItemCount() returns the number of items in the adapter bound to the
                // parent RecyclerView.
                total = slm.getItemCount();
                //findFirstVisibleItemPosition() returns the adapter position of the first
                // visible view.
                scrolledOut = slm.findFirstVisibleItemPositions(null)[0];

                if (isScrolling && (current + scrolledOut ==
                        total)) {
                    isScrolling = false;

                    if (dy > 0) {
                        // Scrolling up
                        if (!alreadyReachedEnd) {
                            fetch(String.valueOf(total), ITEMS_PER_PAGE);
                        } else {
                            show("End reached!");
                        }

                    } else {
                        // Scrolling down
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.listings_page_menu, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (MEM_CACHE.size() > 0) {
            adapter.notifyDataSetChanged();
        } else {
            fetch("0", ITEMS_PER_PAGE);
        }
    }

    /**
     * When our activity is created, we will setup stuff
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_listing);

        this.initializeViews();

    }

}

//end

