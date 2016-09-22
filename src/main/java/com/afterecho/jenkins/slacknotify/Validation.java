package com.afterecho.jenkins.slacknotify;

import hudson.util.FormValidation;
import org.kohsuke.stapler.QueryParameter;

import javax.servlet.ServletException;
import java.io.IOException;

class Validation {

    static FormValidation doCheckMessage(@QueryParameter String value)
            throws IOException, ServletException {
        if (value.length() == 0)
            return FormValidation.error("Please set a message");
        return FormValidation.ok();
    }

    static FormValidation doCheckWebhookUrl(@QueryParameter String value)
            throws IOException, ServletException {
        if (value.length() == 0)
            return FormValidation.error("Please set an incoming webhook URL");
        if (!value.startsWith("https://") && !value.startsWith("http://"))
            return FormValidation.error("Please supply a full URL (http[s]://....)");
        return FormValidation.ok();
    }

    static FormValidation doCheckChannel(@QueryParameter String value)
            throws IOException, ServletException {
        if (value.length() == 0)
            return FormValidation.ok();
        if (!value.startsWith("#") && !value.startsWith("@"))
            return FormValidation.warning("Should start with # for a channel or @ for a user");
        if (value.length() == 1)
            return FormValidation.error("Please supply a #channel or @user");
        return FormValidation.ok();
    }
}
