package com.afterecho.jenkins.slacknotify;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.io.PrintStream;

class SlackMessage {

    final String text;
    final String username;
    final String channel;
    transient final String jobName;
    transient final String webhookUrl;
    transient final int buildNumber;
    transient final PrintStream logger;

    private SlackMessage(String text, String username, String channel, String jobName, String webhookUrl, int buildNumber, PrintStream logger) {
        this.text = text;
        this.username = username;
        this.channel = channel;
        this.jobName = jobName;
        this.webhookUrl = webhookUrl;
        this.buildNumber = buildNumber;
        this.logger = logger;
    }

    public void doSlackNotification() throws IOException {
        String webhookHost = webhookUrl.substring(0, webhookUrl.indexOf('/', 8) + 1);
        String webhookPath = webhookUrl.substring(webhookUrl.indexOf('/', 8) + 1);

        logger.println("Notifying " + (channel == null ? "default" : channel) + " on Slack: " + text);
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(webhookHost).build();
        SlackService slackService = retrofit.create(SlackService.class);
        slackService.postMessage(webhookPath, this).execute();
    }

    public static class SlackMessageBuilder {

        private String text;
        private String username;
        private String channel;
        private String jobName;
        private String webhookUrl;
        private int buildNumber;
        private PrintStream logger;

        public SlackMessageBuilder setText(String text) {
            this.text = text;
            return this;
        }

        public SlackMessageBuilder setUsername(String username) {
            this.username = username;
            return this;
        }

        public SlackMessageBuilder setChannel(String channel) {
            this.channel = channel;
            return this;
        }

        public SlackMessageBuilder setJobName(String jobName) {
            this.jobName = jobName;
            return this;
        }

        public SlackMessageBuilder setWebhookUrl(String webhookUrl) {
            this.webhookUrl = webhookUrl;
            return this;
        }

        public SlackMessageBuilder setBuildNumber(int buildNumber) {
            this.buildNumber = buildNumber;
            return this;
        }

        public SlackMessageBuilder setLogger(PrintStream logger) {
            this.logger = logger;
            return this;
        }

        public SlackMessage build() {
            return new SlackMessage(text, username, channel, jobName, webhookUrl, buildNumber, logger);
        }
    }
}
