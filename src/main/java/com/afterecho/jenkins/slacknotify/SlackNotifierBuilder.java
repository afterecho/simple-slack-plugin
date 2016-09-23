package com.afterecho.jenkins.slacknotify;

import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Builder;
import hudson.util.FormValidation;
import jenkins.tasks.SimpleBuildStep;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.QueryParameter;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.logging.Logger;

public class SlackNotifierBuilder extends Builder implements SimpleBuildStep {

    private final static Logger LOG = Logger.getLogger(SlackNotifierBuilder.class.getName());

    private SlackStepDelegate slackStepDelegate;

    @DataBoundConstructor
    public SlackNotifierBuilder(String message, String webhookUrl, String channel) {
        slackStepDelegate = new SlackStepDelegate(message, webhookUrl, channel);
    }

    @Deprecated
    public SlackNotifierBuilder() {
    }

    public String getMessage() {
        return slackStepDelegate.getMessage();
    }

    @DataBoundSetter
    public void setMessage(@CheckForNull String message) {
        slackStepDelegate.setMessage(message);
    }

    public String getWebhookUrl() {
        return slackStepDelegate.getWebhookUrl();
    }

    @DataBoundSetter
    public void setWebhookUrl(@CheckForNull String webhookUrl) {
        slackStepDelegate.setWebhookUrl(webhookUrl);
    }

    public String getChannel() {
        return slackStepDelegate.getChannel();
    }

    @DataBoundSetter
    public void setChannel(String channel) {
        slackStepDelegate.setChannel(channel);
    }

    @Override
    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.NONE;
    }

    @Override
    public void perform(@Nonnull Run<?, ?> run, @Nonnull FilePath filePath, @Nonnull Launcher launcher, @Nonnull TaskListener taskListener) throws InterruptedException, IOException {
        slackStepDelegate.perform(run, filePath, launcher, taskListener);
    }

    @Symbol("notify2slack")
    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {

        @Override
        public String getDisplayName() {
            return "Send a message to a Slack incoming webhook";
        }

        @Override
        public boolean isApplicable(Class aClass) {
            return true;
        }

        public FormValidation doCheckMessage(@QueryParameter String value)
                throws IOException, ServletException {
            return Validation.doCheckMessage(value);
        }

        public FormValidation doCheckWebhookUrl(@QueryParameter String value)
                throws IOException, ServletException {
            return Validation.doCheckWebhookUrl(value);
        }
    }
}
