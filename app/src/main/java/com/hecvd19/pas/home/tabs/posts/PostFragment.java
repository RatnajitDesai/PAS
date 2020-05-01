package com.hecvd19.pas.home.tabs.posts;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.hecvd19.pas.R;
import com.hecvd19.pas.home.tabs.posts.bottomsheet.PostFilterBottomSheet;
import com.hecvd19.pas.home.tabs.posts.viewmodel.PostAdapter;
import com.hecvd19.pas.home.tabs.posts.viewmodel.PostListLiveData;
import com.hecvd19.pas.home.tabs.posts.viewmodel.PostViewModel;
import com.hecvd19.pas.model.Citizen;
import com.hecvd19.pas.model.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class PostFragment extends Fragment implements PostAdapter.IntentListener, PostFilterBottomSheet.SearchWithPinCode, PostFilterBottomSheet.SearchWithState, PostFilterBottomSheet.SearchWithDistrict {
    private static final String TAG = "PostFragment";

    //var
    private List<Post> postList = new ArrayList<>();
    private RecyclerView postRecyclerView;
    private PostAdapter postAdapter;
    private PostViewModel postViewModel;
    private boolean isScrolling;
    private String PIN_CODE;
    private RelativeLayout mFilerLayout;
    private TextView mFilterText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_posts, container, false);
        postRecyclerView = view.findViewById(R.id.rvPostsList);
        mFilerLayout = view.findViewById(R.id.filterSheet);
        mFilterText = view.findViewById(R.id.filterText);
        postRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        initProductsAdapter();
        initProductListViewModel();
        initRecyclerViewOnScrollListener();

        mFilerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showModalSheet();
            }
        });

        return view;
    }

    private void showModalSheet() {
        PostFilterBottomSheet sheet = new PostFilterBottomSheet();
        sheet.setSearchWithPinCode(this);
        sheet.setSearchWithDistrict(this);
        sheet.setSearchWithState(this);
        sheet.show(getParentFragmentManager(), "BottomSheet");

    }

    private void initProductsAdapter() {
        postAdapter = new PostAdapter(postList, this);
        postRecyclerView.setAdapter(postAdapter);
    }

    private void initProductListViewModel() {
        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);
        postViewModel.getUserInfo().observe(Objects.requireNonNull(getActivity()), new Observer<Citizen>() {
            @Override
            public void onChanged(Citizen citizen) {
                if (citizen != null) {
                    PIN_CODE = citizen.getPin_code();
                    Log.d(TAG, "onChanged: initProductListViewModel :" + PIN_CODE);
                    mFilterText.setText(String.format(Locale.US, "My Pin code : %s", PIN_CODE));
                    getPostsWithPinCode(PIN_CODE, true);

                } else {
                    Toast.makeText(getContext(), "Unexpected Error occurred!", Toast.LENGTH_SHORT).show();
                    FirebaseAuth.getInstance().signOut();
                    getActivity().finish();
                }
            }
        });
    }


    private void addPost(Post addedPost) {
        if (!postList.contains(addedPost)) {
            postList.add(addedPost);
        }
    }

    private void modifyPost(Post modifiedPost) {
        for (int i = 0; i < postList.size(); i++) {
            Post currentPost = postList.get(i);
            if (currentPost.getPostId().equals(modifiedPost.getPostId())) {
                postList.remove(currentPost);
                postList.add(i, modifiedPost);
            }
        }
    }

    private void removePost(Post removedPost) {
        for (int i = 0; i < postList.size(); i++) {
            Post currentPost = postList.get(i);
            if (currentPost.getPostId().equals(removedPost.getPostId())) {
                postList.remove(currentPost);
            }
        }
    }

    private void getPostsWithPinCode(String PIN_CODE, boolean isNewQuery) {

        PostListLiveData postListLiveData = postViewModel.getPostListLiveData(PIN_CODE, isNewQuery);
        if (postListLiveData != null) {
            postListLiveData.observe(Objects.requireNonNull(getActivity()), operation -> {
                Log.d(TAG, "getPosts: operation :" + operation.post.toString());
                switch (operation.type) {
                    case R.string.added:
                        Post addedProduct = operation.post;
                        addPost(addedProduct);
                        break;

                    case R.string.modified:
                        Post modifiedPost = operation.post;
                        modifyPost(modifiedPost);
                        break;

                    case R.string.removed:
                        Post removedPost = operation.post;
                        removePost(removedPost);
                }
                postAdapter.notifyDataSetChanged();
            });
        }
    }


    /**
     * get post with state filter
     *
     * @param state      - state
     * @param isNewQuery - if true - Recreates query, else load after last item
     */

    private void getPostsWithState(String state, boolean isNewQuery) {

        PostListLiveData postListLiveData = postViewModel.getPostListLiveDataForState(state, isNewQuery);
        if (postListLiveData != null) {
            postListLiveData.observe(Objects.requireNonNull(getActivity()), operation -> {
                Log.d(TAG, "getPosts: operation :" + operation.post.toString());
                switch (operation.type) {
                    case R.string.added:
                        Post addedProduct = operation.post;
                        addPost(addedProduct);
                        break;

                    case R.string.modified:
                        Post modifiedPost = operation.post;
                        modifyPost(modifiedPost);
                        break;

                    case R.string.removed:
                        Post removedPost = operation.post;
                        removePost(removedPost);
                }
                postAdapter.notifyDataSetChanged();
            });
        }
    }

    private void getPostsWithDistrict(String district, String state, boolean isNewQuery) {
        PostListLiveData postListLiveData = postViewModel.getPostsWithDistrict(district, state, isNewQuery);
        if (postListLiveData != null) {
            postListLiveData.observe(Objects.requireNonNull(getActivity()), operation -> {
                Log.d(TAG, "getPosts: operation :" + operation.post.toString());
                switch (operation.type) {
                    case R.string.added:
                        Post addedProduct = operation.post;
                        addPost(addedProduct);
                        break;

                    case R.string.modified:
                        Post modifiedPost = operation.post;
                        modifyPost(modifiedPost);
                        break;

                    case R.string.removed:
                        Post removedPost = operation.post;
                        removePost(removedPost);
                }
                postAdapter.notifyDataSetChanged();
            });
        }
    }

    private void initRecyclerViewOnScrollListener() {
        RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
                if (layoutManager != null) {
                    int firstVisiblePostPosition = layoutManager.findFirstVisibleItemPosition();
                    int visiblePostCount = layoutManager.getChildCount();
                    int totalPostCount = layoutManager.getItemCount();
                    if (isScrolling && (firstVisiblePostPosition + visiblePostCount == totalPostCount)) {
                        isScrolling = false;
                        getPostsWithPinCode(PIN_CODE, false);
                    }
                }
            }
        };
        postRecyclerView.addOnScrollListener(onScrollListener);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void startIntent(Intent intent) {
        startActivity(intent);
    }

    @Override
    public void search(String text) {
        //search posts with pin
        Log.d(TAG, "search: " + text);
        if (text.matches("[0-9]{6}")) {
            postList.clear();
            postAdapter.notifyDataSetChanged();
            PIN_CODE = text;
            mFilterText.setText(String.format(Locale.US, "Pin code: %s", text));
            getPostsWithPinCode(PIN_CODE, true);
        } else {
            //search with state
            postList.clear();
            postAdapter.notifyDataSetChanged();
            mFilterText.setText(String.format(Locale.US, "State: %s", text));
            getPostsWithState(text, true);
        }
    }

    @Override
    public void search(String district, String state) {
        postList.clear();
        postAdapter.notifyDataSetChanged();
        mFilterText.setText(String.format(Locale.US, "District: %s(%s)", district, state));
        getPostsWithDistrict(district, state, true);
    }

}
