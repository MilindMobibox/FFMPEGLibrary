package com.videomaker.photovideoeditorwithanimation.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.universevideomaker.R;
import com.videomaker.photovideoeditorwithanimation.MyApplication;
import com.videomaker.photovideoeditorwithanimation.data.ImageData;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

public class ImageByAlbumAdapter extends Adapter<ImageByAlbumAdapter.Holder> {
    private MyApplication application = MyApplication.getInstance();
    private OnItemClickListner<Object> clickListner;
    private RequestManager glide;
    private LayoutInflater inflater;

    class Holder extends ViewHolder {
        View clickableView;
        ImageView imageView;
        TextView textView;

        Holder(View view) {
            super(view);
            this.imageView = view.findViewById(R.id.imageView1);
            this.textView = view.findViewById(R.id.textView1);
            this.clickableView = view.findViewById(R.id.clickableView);
        }
    }

    public ImageByAlbumAdapter(Context activity) {
        this.inflater = LayoutInflater.from(activity);
        this.glide = Glide.with(activity);
    }

    public void setOnItemClickListner(OnItemClickListner<Object> clickListner) {
        this.clickListner = clickListner;
    }

    @Override
    public int getItemCount() {
        return this.application.getImageByAlbum(this.application.getSelectedFolderId()).size();
    }

    private ImageData getItem(int pos) {
        return this.application.getImageByAlbum(this.application.getSelectedFolderId()).get(pos);
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, @SuppressLint("RecyclerView") final int pos) {
        CharSequence charSequence;
        int i;
        final ImageData data = getItem(pos);
        holder.textView.setSelected(true);
        if (data.imageCount == 0) {
            charSequence = "";
        } else {
            charSequence = String.valueOf(data.imageCount);
        }
        holder.textView.setText(charSequence);
        this.glide.load(data.imagePath).into(holder.imageView);
        if (data.imageCount == 0) {
            i = Color.TRANSPARENT;
        } else {
            i = Color.parseColor("#50000000");
        }
        holder.textView.setBackgroundColor(i);
        holder.clickableView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (holder.imageView.getDrawable() == null) {
                    Toast.makeText(application, "Image currpted or not support.", Toast.LENGTH_LONG).show();
                    return;
                }
                application.addSelectedImage(data);
                notifyItemChanged(pos);
                if (clickListner != null) {
                    clickListner.onItemClick(v, data);
                }
            }
        });
    }

    @NonNull
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int pos) {
        return new Holder(this.inflater.inflate(R.layout.items_by_folder, parent, false));
    }
}
