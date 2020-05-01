package com.hecvd19.pas.home.tabs.posts.viewmodel;

import com.hecvd19.pas.model.Post;

public class Operation {
    public Post post;
    public int type;

    public Operation(Post post, int type) {
        this.post = post;
        this.type = type;
    }
}
