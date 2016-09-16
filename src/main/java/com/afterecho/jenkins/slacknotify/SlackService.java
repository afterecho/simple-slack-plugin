package com.afterecho.jenkins.slacknotify;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface SlackService {
    @POST("/{postPath}")
    Call<Void> postMessage(@Path(value = "postPath", encoded = true) String path, @Body SlackMessage message);
}
