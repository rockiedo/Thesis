package k2013.fit.hcmus.thesis.id539621.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import k2013.fit.hcmus.thesis.id539621.R;
import k2013.fit.hcmus.thesis.id539621.activity.GamePlayActivity;
import k2013.fit.hcmus.thesis.id539621.activity.GameSelectionActivity;
import k2013.fit.hcmus.thesis.id539621.activity.MainActivity;

/**
 * Created by cpu60011-local on 25/04/2017.
 */

public class GameSelectionAdapter extends RecyclerView.Adapter<GameSelectionAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mLevel;
        public TextView mTime;
        public TextView mBackgroundSound;
        public TextView mDistractingSound;
        public Button mButton;
        public View mDivider;

        private WeakReference<GameSelectionActivity> mWeakReference;

        public ViewHolder(View itemView) {
            super(itemView);

            mLevel = (TextView) itemView.findViewById(R.id.gameselection_recycleritem_level);
            mTime = (TextView) itemView.findViewById(R.id.gameselection_recycleritem_time);
            mBackgroundSound = (TextView) itemView.findViewById(R.id.gameselection_recycleritem_backgroundsound);
            mDistractingSound = (TextView) itemView.findViewById(R.id.gameselection_recycleritem_distractingsound);
            mButton = (Button) itemView.findViewById(R.id.gameselection_recycleritem_btn);
            mDivider = itemView.findViewById(R.id.gameselection_recycleritem_divider);
        }

        public void setContext(GameSelectionActivity activity) {
            mWeakReference = new WeakReference<GameSelectionActivity>(activity);
        }

        public GameSelectionActivity getContext() {
            GameSelectionActivity activity =  mWeakReference.get();

            if (activity.isDestroyed() || activity.isFinishing()) {
                return null;
            }

            return activity;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_game_selection, parent, false);
        ViewHolder vh = new ViewHolder(v);
        vh.setContext((GameSelectionActivity) parent.getContext());
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Context context = holder.mLevel.getContext();
        Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/pacifico-regular.ttf");
        holder.mLevel.setTypeface(tf);

        if (position == 4) {
            holder.mDivider.setVisibility(View.GONE);
        }

        holder.mLevel.setText("" + (position + 1));

        holder.mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameSelectionActivity activity = holder.getContext();
                if (activity != null) {
                    Intent intent = new Intent(activity, GamePlayActivity.class);
                    activity.startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}