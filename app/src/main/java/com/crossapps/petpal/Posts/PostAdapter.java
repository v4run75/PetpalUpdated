package com.crossapps.petpal.Posts;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.crossapps.petpal.OnSnapPositionChangeListener;
import com.crossapps.petpal.R;
import com.crossapps.petpal.Server.Request.DeletePostRequest;
import com.crossapps.petpal.Server.Response.DefaultResponse;
import com.crossapps.petpal.Server.Response.LoginResponseData;
import com.crossapps.petpal.Server.Response.PostResponseData;
import com.crossapps.petpal.Server.RetrofitApiAuthSingleTon;
import com.crossapps.petpal.Server.TCApi;
import com.crossapps.petpal.SnapOnScrollListener;
import com.crossapps.petpal.Util.Constant;
import com.crossapps.petpal.Util.Logger;
import com.crossapps.petpal.Util.PrefernceFile;
import com.crossapps.petpal.Util.UtilityofActivity;
import com.crossapps.petpal.Util.custom.TextViewOpenSans;
import com.crossapps.petpal.Util.custom.TextViewPoppin;
import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<PostResponseData> postArrayList = new ArrayList<>();
    private RequestManager glide;
    private MediaController mediaController;
    private AppCompatActivity activity;
    private int currentpos = 0;
    TextView[] mDotsText;
    private int mDotsCount = 0;
    ArrayList<Uri> videoPaths = new ArrayList<>();
    private UtilityofActivity utilityofActivity;


    public PostAdapter(Context context, ArrayList<PostResponseData> postArrayList, AppCompatActivity activity) {

        this.context = context;
        this.postArrayList = postArrayList;
        glide = Glide.with(context);
        this.activity = activity;
        this.utilityofActivity = new UtilityofActivity(activity);

    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {

        View view;

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_multi_post, parent, false);
        return new MyViewHolder(view);

    }


    private void deletePost(final Context context, String postId, final MyViewHolder holder) {


        LoginResponseData loginResponse = new Gson().fromJson(PrefernceFile.Companion.getInstance(context).getString(Constant.INSTANCE.getPREF_KEY_USER_DATA()), LoginResponseData.class);

        TCApi tcApi = RetrofitApiAuthSingleTon.createService(TCApi.class, "token");

        DeletePostRequest deletePostRequest = new DeletePostRequest(postId, loginResponse.getUserId());
        Call<DefaultResponse> call = tcApi.callDeletePost(deletePostRequest);


        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {

                try {
                    Toast.makeText(context, "Post Deleted Successfully", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                call.cancel();
            }
        });

    }


    private byte[] postid, userid;

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        final MyViewHolder holder = ((MyViewHolder) viewHolder);


        final PostResponseData posts = postArrayList.get(holder.getAdapterPosition());

        LoginResponseData loginResponse = new Gson().fromJson(PrefernceFile.Companion.getInstance(context).getString(Constant.INSTANCE.getPREF_KEY_USER_DATA()), LoginResponseData.class);


        if (posts.getUser().getUserId().equals(loginResponse.getUserId())) {
            holder.menu.setVisibility(View.VISIBLE);
        } else {
            holder.menu.setVisibility(View.GONE);
        }

        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);

                // Set the alert dialog title
                builder.setTitle("Delete Post");

                builder.setMessage("Are you sure want want to delete this post?");


                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deletePost(context, posts.getPostId(), holder);

                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });


                // Finally, make the alert dialog using builder
                AlertDialog dialog = builder.create();

                // Display the alert dialog on app interface
                dialog.show();

            }
        });


        if (!posts.getMedias().isEmpty()) {
            holder.mediaFrame.setVisibility(View.VISIBLE);
            mDotsCount = posts.getMedias().size();
        } else {
            holder.mediaFrame.setVisibility(View.GONE);
        }


        if (posts.getUser().getProfilePicture() != null) {
            glide.load(posts.getUser().getProfilePicture()).into(holder.imgView_proPic);
        } else
            glide.load(R.drawable.ic_done).into(holder.imgView_proPic);

        holder.tv_name.setText(posts.getUser().getName());
        holder.tv_time.setText(getTimeElapsed(posts.getCreated()));

        if (!posts.getDescription().isEmpty()) {
            holder.tv_status.setText(posts.getDescription());
            holder.tv_status.setVisibility(View.VISIBLE);
        } else {
            holder.tv_status.setVisibility(View.GONE);
        }


        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.recyclerView.setHasFixedSize(false);
//        holder.recyclerView.setAdapter(new MediaAdapter(context, posts.getMedias(), activity,posts.getUser().getFirst_name()+posts.getDescription().substring(0,5)));
        holder.recyclerView.setAdapter(new MediaAdapter(context, posts.getMedias(), activity, posts.getUser().getName()));

        final SnapHelper snapHelper = new PagerSnapHelper();
        if (holder.recyclerView.getOnFlingListener() == null)
            snapHelper.attachToRecyclerView(holder.recyclerView);

        final OnSnapPositionChangeListener onSnapPositionChangeListener = new OnSnapPositionChangeListener() {
            @Override
            public void onSnapPositionChange(int position) {

//                Toast.makeText(context,"POS: "+position,Toast.LENGTH_LONG).show();
                holder.mDotsLayout.setVisibility(View.VISIBLE);
                currentpos = position;
                holder.mDotsLayout.setText((position + 1) + "/" + posts.getMedias().size());
//                for (int i = 0; i < mDotsCount; i++) {
//                    mDotsText[i].setTextColor(Color.GRAY);
//                }
//
//                mDotsText[position].setTextColor(Color.BLUE);
            }
        };


        SnapOnScrollListener snapOnScrollListener = new SnapOnScrollListener(snapHelper, SnapOnScrollListener.Behavior.NOTIFY_ON_SCROLL, onSnapPositionChangeListener);
        holder.recyclerView.addOnScrollListener(snapOnScrollListener);

//        Bitmap image= ((BitmapDrawable)holder.imgView_postPic.getDrawable()).getBitmap();

        final UtilityofActivity utilityofActivity = new UtilityofActivity(activity);

//        holder.share.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(final View v) {
//
//
//                try {
//                    SecretKey secretkey = generateKey();
//                    postid = encryptMsg(posts.getPostId(), secretkey);
//                    userid = encryptMsg(posts.getUser().getUserId(), secretkey);
//
//                    Logger.d("Decrypt: ", decryptMsg(postid, secret));
//                    Logger.d("Decrypt: ", decryptMsg(userid, secret));
//
//                    Logger.d("Secret Key: ", secretkey.toString());
//
//                } catch (NoSuchAlgorithmException e) {
//                    e.printStackTrace();
//                } catch (NoSuchPaddingException e) {
//                    e.printStackTrace();
//                } catch (InvalidKeyException e) {
//                    e.printStackTrace();
//                } catch (InvalidParameterSpecException e) {
//                    e.printStackTrace();
//                } catch (IllegalBlockSizeException e) {
//                    e.printStackTrace();
//                } catch (BadPaddingException e) {
//                    e.printStackTrace();
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                } catch (InvalidKeySpecException e) {
//                    e.printStackTrace();
//                } catch (InvalidAlgorithmParameterException e) {
//                    e.printStackTrace();
//                }
//
//                String link = "http://www.pathprahari.co/post?postid=" + new String(postid) + "?userid=" + new String(userid);
//
//
//                Intent i = new Intent(Intent.ACTION_SEND);
//                i.setType("text/plain");
//                i.putExtra(Intent.EXTRA_TEXT, link);
//                context.startActivity(Intent.createChooser(i, "Share Post"));
//
//
//            }
//        });


    }




    @NotNull
    private static String getTimeElapsed(String date) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            format.setTimeZone(TimeZone.getTimeZone("GMT+05:30"));
            Date past = format.parse(date);
            Logger.d("Server", date);
            Logger.d("Past", past.toString());
            Date now = new Date();
            Logger.d("Now", now.toString());
            long seconds = TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime());
            long minutes = TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());
            long hours = TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());
            long days = TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());
            if (seconds < 60)
                return (seconds + "s");
            else if (minutes < 60)
                return (minutes + "m");
            else if (hours < 24)
                return (hours + "h");
            else
                return (days + "d");
        } catch (Exception j) {
            j.printStackTrace();
        }
        return "";
    }

    @Override
    public int getItemCount() {
        return postArrayList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextViewOpenSans tv_time, tv_status;
        ImageView imgView_proPic, menu;
//        RelativeLayout share;
        RecyclerView recyclerView;
        TextView mDotsLayout;
        TextViewPoppin tv_name;
        FrameLayout mediaFrame;

        public MyViewHolder(View itemView) {
            super(itemView);

            imgView_proPic = itemView.findViewById(R.id.profilePic);
            tv_name = itemView.findViewById(R.id.name);
            menu = itemView.findViewById(R.id.menu);
            tv_time = itemView.findViewById(R.id.postTime);
            tv_status = itemView.findViewById(R.id.status);
            recyclerView = itemView.findViewById(R.id.mediaContainer);
            mDotsLayout = itemView.findViewById(R.id.image_count);
            mediaFrame = itemView.findViewById(R.id.mediaFrame);
        }
    }
}
