package lejarza.asier.cuisineasier.adapter.viewholder;

import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;

import lejarza.asier.cuisineasier.R;
import lejarza.asier.cuisineasier.adapter.models.Model_Group;

public class ViewHolder_Group extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener{

    private final TextView tvText_date;
    private final TextView tvText_name;
    private final ImageView image;

    public ViewHolder_Group(View itemView) {
        super(itemView);

        tvText_date = (TextView) itemView.findViewById(R.id.tvText_date);
        tvText_name = (TextView) itemView.findViewById(R.id.tvText_name);
        image = (ImageView) itemView.findViewById(R.id.imageView_photo);
        itemView.setOnCreateContextMenuListener(this);
    }

    public void bind(Model_Group model_date, Model_Group model_name, Model_Group model_image) {
        tvText_date.setText(model_date.getText());
        tvText_name.setText(model_name.getText());

        image.getLayoutParams().height = 180;

        new lejarza.asier.cuisineasier.DownloadImageTask(image).execute(model_image.getText());
    }



    @Override
    public void onClick(View v) {
        /*Intent intent = new Intent(context,DetailsActivity.class);
        intent.putExtra("name",txtViewName.getText().toString());
        intent.putExtra("number",txtViewNumber.getText().toString());
        context.startActivity(intent);
        Toast.makeText(RecyclerAdapter.context,"you have clicked Row "+getPosition(), Toast.LENGTH_LONG).show();*/
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        //Toast.makeText(v.getContext(), "menucontext", Toast.LENGTH_SHORT).show();
    }


}


