package com.afterecho.jenkins.slacknotify;

import hudson.FilePath;
import hudson.Launcher;
import hudson.Util;
import hudson.model.Run;
import hudson.model.TaskListener;
import org.jenkinsci.plugins.tokenmacro.MacroEvaluationException;
import org.jenkinsci.plugins.tokenmacro.TokenMacro;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.io.IOException;

class SlackStepDelegate {

    private
    @CheckForNull
    String message;
    private
    @CheckForNull
    String webhookUrl;
    private String channel;

    SlackStepDelegate(String message, String webhookUrl, String channel) {
        this.message = message;
        this.webhookUrl = webhookUrl;
        this.channel = channel;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(@CheckForNull String message) {
        this.message = Util.fixNull(message);
    }

    public String getWebhookUrl() {
        return webhookUrl;
    }

    public void setWebhookUrl(@CheckForNull String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = (channel != null && channel.length() != 0) ? channel : null;
    }

    public void perform(@Nonnull Run<?, ?> run, @Nonnull FilePath filePath, @Nonnull Launcher launcher, @Nonnull TaskListener taskListener) throws InterruptedException, IOException {
        String expandedText;
        try {
            expandedText = TokenMacro.expandAll(run, filePath, taskListener, getMessage());
        } catch (MacroEvaluationException e) {
            expandedText = e.getMessage();
        }

        String expandedChannel;
        try {
            expandedChannel = TokenMacro.expandAll(run, filePath, taskListener, getChannel());
        } catch (MacroEvaluationException e) {
            expandedChannel = e.getMessage();
        }

        String expandedWebhook;
        try {
            expandedWebhook = TokenMacro.expandAll(run, filePath, taskListener, getWebhookUrl());
        } catch (MacroEvaluationException e) {
            expandedWebhook = e.getMessage();
        }

        SlackMessage slackMessage = new SlackMessage.SlackMessageBuilder()
                .setBuildNumber(run.getNumber())
                .setText(expandedText)
                .setWebhookUrl(expandedWebhook)
                .setLogger(taskListener.getLogger())
                .setJobName(run.getDisplayName())
                .setChannel(expandedChannel)
                .build();
        slackMessage.doSlackNotification();
    }
}
