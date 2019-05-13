package com.crossapps.petpal.Posts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.crossapps.petpal.R;
import com.crossapps.petpal.Timeline.TimelineImage;
import com.crossapps.petpal.Timeline.TimelineVideo;
import com.crossapps.petpal.Util.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;


public class MediaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<Media> modelFeedArrayList = new ArrayList<>();
    private RequestManager glide;
    private MediaController mediaController;
    private Activity activity;
    private int currentpos = 0;
    private String filename;

    interface OnItemClickListener {
        void OnItemClick();
    }


    public MediaAdapter(Context context, ArrayList<Media> modelFeedArrayList, Activity activity, String filename) {

        this.context = context;
        this.modelFeedArrayList = modelFeedArrayList;
        glide = Glide.with(context);
        this.activity = activity;
        this.filename = filename;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int getItemViewType(int position) {
        switch (modelFeedArrayList.get(position).getType()) {
            case "img":
                return 101;
            case "video":
                return 102;
            default:
                return -1;
        }
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {

        View view;

        switch (viewType) {
            case 101: {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_image, parent, false);
                return new MyImageViewHolder(view);
            }
            case 102: {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_video, parent, false);
                return new MyVideoViewHolder(view);
            }
        }

        return null;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        final Media modelFeed = modelFeedArrayList.get(i);


        switch (modelFeed.getType()) {
            case "img": {
                final MyImageViewHolder holder = ((MyImageViewHolder) viewHolder);


                holder.imgView_postPic.setVisibility(View.VISIBLE);
                glide.load(modelFeed.getLink()).into(holder.imgView_postPic);


                holder.imgView_postPic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, TimelineImage.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("Url", modelFeed.getLink());
                        bundle.putInt("pos", currentpos);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }
                });


                break;
            }
            case "video": {
                final MyVideoViewHolder holder = ((MyVideoViewHolder) viewHolder);


                File file = new File("storage/emulated/0/Android/data/com.crossapps.petpal/files/Videos", filename + i + ".mp4");
                if (file.exists()) {
//                    holder.postVideo.setVideoPath("storage/emulated/0/Android/data/com.crossapps.petpal/files/Videos"+filename+i+".mp4");
                    holder.postVideo.setVideoURI(FileProvider.getUriForFile(context,
                            "com.crossapps.petpal.fileprovider",
                            file));
                    Logger.d(" Path: ", FileProvider.getUriForFile(context,
                            "com.crossapps.petpal.fileprovider",
                            file).getPath());
                } else {

//                    Toast.makeText(context,"Playing Live",Toast.LENGTH_LONG).show();
                    holder.postVideo.setVideoURI(Uri.parse(modelFeed.getLink()));

//                    DownloadAsync downloadAsync=new DownloadAsync(new DownloadAsync.Callback() {
//                        @Override
//                        public void started() {
//                            Toast.makeText(context,"Downloading",Toast.LENGTH_LONG).show();
//                        }
//
//                        @Override
//                        public void finished() {
//                            Toast.makeText(context,"Finish",Toast.LENGTH_LONG).show();
//                        }
//                    });
//
//                    downloadAsync.execute(new VideoFile(filename+i,modelFeed.link));

                }

                holder.postVideo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        currentpos = holder.postVideo.getCurrentPosition();
                        Intent intent = new Intent(context, TimelineVideo.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("Url", modelFeed.getLink());
                        bundle.putInt("pos", currentpos);
                        intent.putExtras(bundle);
                        context.startActivity(intent);

                    }
                });

                holder.postVideo.requestFocus();


                holder.postVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                    public void onPrepared(MediaPlayer mediaPlayer) {
                        holder.buffering.setVisibility(VideoView.GONE);
                        holder.postVideo.start();
                    }

                });

                break;
            }
        }

    }


    @Override
    public int getItemCount() {
        return modelFeedArrayList.size();
    }

    public class MyVideoViewHolder extends RecyclerView.ViewHolder {


        TextView mBufferingTextView, pauseVideo;
        VideoView postVideo;
        ProgressBar buffering;

        MyVideoViewHolder(View itemView) {
            super(itemView);

            postVideo = itemView.findViewById(R.id.postVideo);

            mBufferingTextView = itemView.findViewById(R.id.buffering_textview);

            pauseVideo = itemView.findViewById(R.id.pausedVideo);

            buffering = itemView.findViewById(R.id.buffering);

        }
    }

    public class MyImageViewHolder extends RecyclerView.ViewHolder {


        ImageView imgView_postPic;

        MyImageViewHolder(View itemView) {
            super(itemView);


            imgView_postPic = itemView.findViewById(R.id.postPic);

        }
    }
}
