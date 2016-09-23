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
        return FormValidation.ok();
    }
}
