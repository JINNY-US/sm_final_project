package com.example.sm_project.listener;

import com.example.sm_project.PostInfo;

public interface OnPostListener {
    void onDelete(PostInfo postInfo);
    void onModify();
}
