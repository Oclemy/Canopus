package info.camposha.canopus_ums.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import info.camposha.canopus_ums.R;
import info.camposha.canopus_ums.data.model.entity.User;

import static info.camposha.canopus_ums.common.UserUtils.SEARCH_STRING;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder>  {
    private Context c;
    private int[] mMaterialColors;
    public List<User> users = new ArrayList<>();

    interface ItemClickListener {
        void onItemClick(int pos);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        private final TextView quoteTxt, authorTxt;
        private final RelativeLayout mLayout;
        private ItemClickListener itemClickListener;

        ViewHolder(View itemView) {
            super(itemView);
            quoteTxt = itemView.findViewById(R.id.quoteTxt);
            authorTxt = itemView.findViewById(R.id.nameTxt);
            mLayout = itemView.findViewById(R.id.layout);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            this.itemClickListener.onItemClick(this.getLayoutPosition());
        }

        void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }
    }

    public UsersAdapter(List<User> users) {
        this.users = users;
    }
    public UsersAdapter() {
        if(users ==null){
            users =new ArrayList<>();
        }
    }

    public void addAll(List<User> newUsers) {
        int initialSize = users.size();
        users.addAll(newUsers);
        notifyItemRangeInserted(initialSize, newUsers.size());
    }
    public String getLastItemId() {
        if(users.size() > 0){
            return users.get(users.size() - 1).getId();
        }
        return null;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.c=parent.getContext();
        mMaterialColors = c.getResources().getIntArray(R.array.colors);
        View view = LayoutInflater.from(c).inflate(R.layout.model, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        //get current scientist
        final User q = users.get(position);

        //bind data to widgets
        h.mLayout.setBackgroundColor(mMaterialColors[new Random().nextInt(
                mMaterialColors.length)]);
        h.quoteTxt.setText(q.getName());
        h.authorTxt.setText(q.getEmail());


        //get name and galaxy
        String name = q.getName().toLowerCase(Locale.getDefault());
        String galaxy = q.getEmail().toLowerCase(Locale.getDefault());

        //highlight name text while searching
        if (name.contains(SEARCH_STRING) && !(SEARCH_STRING.isEmpty())) {
            int startPos = name.indexOf(SEARCH_STRING);
            int endPos = startPos + SEARCH_STRING.length();

            Spannable spanString = Spannable.Factory.getInstance().
                    newSpannable(h.quoteTxt.getText());
            spanString.setSpan(new ForegroundColorSpan(Color.RED), startPos, endPos,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            h.quoteTxt.setText(spanString);
        }

        //highligh galaxy text while searching
        if (galaxy.contains(SEARCH_STRING) && !(SEARCH_STRING.isEmpty())) {

            int startPos = galaxy.indexOf(SEARCH_STRING);
            int endPos = startPos + SEARCH_STRING.length();

            Spannable spanString = Spannable.Factory.getInstance().
                    newSpannable(h.authorTxt.getText());
            spanString.setSpan(new ForegroundColorSpan(Color.BLUE), startPos, endPos,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            h.authorTxt.setText(spanString);
        }

        //open detailactivity when clicked
//        h.setItemClickListener(pos -> UserUtils.sendToActivity(c, q,
//         DetailActivity.class));

    }
    @Override
    public int getItemCount() {
        return users.size();
    }

}
//end