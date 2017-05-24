package tv.accedo.colourmemory;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kimharold on 5/24/17.
 */

public class RankingAdapter extends ArrayAdapter<Player> {

    private ArrayList<Player> players;
    private Context mContext;

    public RankingAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<Player> players) {
        super(context, resource, players);
        this.players = players;
        this.mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        // Get the data item for this position
        Player Player = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.txtRank = (TextView) convertView.findViewById(R.id.rank);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.name);
            viewHolder.txtScore = (TextView) convertView.findViewById(R.id.score);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txtRank.setText(position+1+"");
        viewHolder.txtName.setText(Player.getName());
        viewHolder.txtScore.setText(Player.getScore()+"");

        return convertView;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView txtRank;
        TextView txtName;
        TextView txtScore;
    }
}
