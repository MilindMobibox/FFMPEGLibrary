package com.videomaker.photovideoeditorwithanimation.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.universevideomaker.R;
import com.videomaker.photovideoeditorwithanimation.MyApplication;
import com.videomaker.photovideoeditorwithanimation.data.ImageData;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ImageByIdAdapter extends Adapter<ImageByIdAdapter.Holder> {
    private MyApplication application = MyApplication.getInstance();
    private OnItemClickListner<Object> clickListner;
    private ArrayList<String> folderId = new ArrayList<>(this.application.getAllAlbum().keySet());
    private RequestManager glide;
    private LayoutInflater inflater;

    public class Holder extends ViewHolder {
        CheckBox cbSelect;
        private View clickableView;
        ImageView imageView;
        View parent;
        TextView textView;

        Holder(View v) {
            super(v);
            this.parent = v;
            this.cbSelect = v.findViewById(R.id.cbSelect);
            this.imageView = v.findViewById(R.id.imageView1);
            this.textView = v.findViewById(R.id.textView1);
            this.clickableView = v.findViewById(R.id.clickableView);
        }

        public void onItemClick(View view, Object item) {
            if (clickListner != null) {
                clickListner.onItemClick(view, item);
            }
        }
    }

    public ImageByIdAdapter(Context activity) {
        this.glide = Glide.with(activity);
        Collections.sort(this.folderId, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareToIgnoreCase(s2);
            }
        });
        if (folderId.size() > 0)
            this.application.setSelectedFolderId(this.folderId.get(0));
        this.inflater = LayoutInflater.from(activity);
    }

    public void setOnItemClickListner(OnItemClickListner<Object> clickListner) {
        this.clickListner = clickListner;
    }

    @Override
    public int getItemCount() {
        return this.folderId.size();
    }

    private String getItem(int pos) {
        return this.folderId.get(pos);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int pos) {
        final String currentFolderId = getItem(pos);
        final ImageData data = this.application.getImageByAlbum(currentFolderId).get(0);
        holder.textView.setSelected(true);
        holder.textView.setText(data.folderName);
        this.glide.load(data.imagePath).into(holder.imageView);
        holder.cbSelect.setChecked(currentFolderId.equals(this.application.getSelectedFolderId()));
        holder.clickableView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                application.setSelectedFolderId(currentFolderId);
                if (clickListner != null) {
                    clickListner.onItemClick(v, data);
                }
                notifyDataSetChanged();
            }
        });
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int pos) {
        return new Holder(this.inflater.inflate(R.layout.items_image, parent, false));
    }
}
