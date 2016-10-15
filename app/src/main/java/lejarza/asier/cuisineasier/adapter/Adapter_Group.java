package lejarza.asier.cuisineasier.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

import lejarza.asier.cuisineasier.R;
import lejarza.asier.cuisineasier.adapter.models.Model_Group;
import lejarza.asier.cuisineasier.adapter.viewholder.ViewHolder_Group;

public class Adapter_Group extends RecyclerView.Adapter<ViewHolder_Group> {

    private final LayoutInflater mInflater;
    private final List<Model_Group> mModels_id;
    private final List<Model_Group> mModels_earth_date;
    private final List<Model_Group> mModels_camera_name;
    private final List<Model_Group> mModels_image;


    public Adapter_Group(Context context, List<Model_Group> List_id, List<Model_Group> List_camera_name, List<Model_Group> List_earth_date, List<Model_Group> List_image) {
        mInflater = LayoutInflater.from(context);
        mModels_id = new ArrayList<>(List_id);
        mModels_camera_name = new ArrayList<>(List_camera_name);
        mModels_earth_date = new ArrayList<>(List_earth_date);
        mModels_image = new ArrayList<>(List_image);
    }

    @Override
    public ViewHolder_Group onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = mInflater.inflate(R.layout.item_layout, parent, false);
        return new ViewHolder_Group(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder_Group holder, int position) {
        final Model_Group model_id = mModels_id.get(position);
        final Model_Group model_camera_name = mModels_camera_name.get(position);
        final Model_Group model_earth_date = mModels_earth_date.get(position);
        final Model_Group model_image = mModels_image.get(position);
        holder.itemView.setLongClickable(true);

        final int pos = position;
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosition(pos);
                return false;
            }

        });

        holder.bind(model_camera_name, model_earth_date, model_image);
    }

    @Override
    public int getItemCount() {
        return mModels_id.size();
    }

    @Override
    public long getItemId(int position) {

        return super.getItemId(position);
    }


    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }




}
