package com.students.preparation.matric.students.adapter;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.students.preparation.matric.students.R;
import com.students.preparation.matric.students.model.TeachersModel;
import com.students.preparation.matric.students.model.Worksheet;

import java.io.File;
import java.util.ArrayList;

import static android.content.Context.DOWNLOAD_SERVICE;

public class TeachersInboxAdapter extends RecyclerView.Adapter<TeachersInboxAdapter.ViewHolder> {

    private Context context;
    private ArrayList<TeachersModel> newsArrayList;

    public TeachersInboxAdapter(Context context, ArrayList<TeachersModel> tutorialsArrayList) {
        this.context = context;
        this.newsArrayList = tutorialsArrayList;
    }

    @NonNull
    @Override
    public TeachersInboxAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.teachers_inbox_card_layout,
                viewGroup, false);
        return new TeachersInboxAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TeachersInboxAdapter.ViewHolder viewHolder, int i) {
        final TeachersModel news = newsArrayList.get(i);
        viewHolder.image.setText(String.valueOf(news.getTitle().charAt(0)));
        viewHolder.title.setText(news.getTitle());
        viewHolder.subject.setText(news.getSubject() + ":");
        viewHolder.grade.setText("Grade " + news.getGrade() + ":");
        viewHolder.stream.setText(news.getStream());
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDownloading(news.getTitle(),news.getUrl());
            }
        });

    }

    @Override
    public int getItemCount() {
        return newsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView image;
        private final TextView title;
        private final CardView cardView;
        private final TextView subject, grade, stream;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.adminNotesImage);
            title = itemView.findViewById(R.id.inboxTitle);
            cardView = itemView.findViewById(R.id.inboxCard);
            subject = itemView.findViewById(R.id.inboxSubject);
            grade = itemView.findViewById(R.id.inboxGrade);
            stream = itemView.findViewById(R.id.inboxStream);

        }
    }

    public void startDownloading(String title,String url){
        File file = new File(context.getExternalFilesDir(null) + "/teachersInbox");

        //now if download complete file not visible now lets show it
        DownloadManager.Request request = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            request = new DownloadManager.Request(Uri.parse(url))
                    .setTitle(title)
                    .setDescription("Downloading...")
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationUri(Uri.fromFile(file))
                    .setRequiresCharging(false)
                    .setAllowedOverMetered(true)
                    .setAllowedOverRoaming(true);
        } else {
            request = new DownloadManager.Request(Uri.parse(url))
                    .setTitle(title)
                    .setDescription("Downloading...")
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationUri(Uri.fromFile(file))
                    .setAllowedOverRoaming(true);
        }

        DownloadManager downloadManager = (DownloadManager) context
                .getSystemService(DOWNLOAD_SERVICE);
        long downloadId = downloadManager.enqueue(request);
    }

}