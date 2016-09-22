# simple-slack-plugin

This is a simple  plugin for Jenkins that allows you to send messages to Slack
or something with a Slack-compatible API.  It is a very simple plugin that does
little to no error checking but it works.

## Build instructions

Install maven3.  If you don't already have it you'll need to install a Java SDK
and potentially set JAVA_HOME.  Clone the repository and run

    mvn package

Grab the .hpi file from target/ and install to your Jenkins server.

*Note* the plugin requires the structs plugin.  Install it by hand from the
 available plugins if it is not installed automatically.

To test without installing, run

    mvn hpi:run
    
which will fire up a local Jenkins on port 8080 and install the plugin.

## Usage

The plugin can be used as a build step and a post-build step in a freestyle
project, and as a step in a pipeline project.  It has not been tested on any
other project type.

Firstly set up an incoming webhook.  For Slack see [https://api.slack.com/incoming-webhooks](https://api.slack.com/incoming-webhooks).


There are three parameters, two of which are mandatory.

The *message* is the message
you want to send.  At this time it is a simple text string; no substitutions
or fancy stuff like that.  The message is run through the [Token Macro Plugin](https://wiki.jenkins-ci.org/display/JENKINS/Token+Macro+Plugin)
for your token expansion pleasure.

The *webhookUrl* is the URL from Slack you get when
you configure a webhook.  It must start with http:// or https://

The *channel* is optional.  When you configure a webhook in Slack you specify a default
channel.  If you leave it blank in the Jenkins job it will use that one.  If you
wish to override it you can do it here.  If you start it with a '#' then it is
a channel.  If you start it with an '@' then it is a user (and will appear
to come from slackbot).

For a pipeline job the parameters are the same and are run with

    notify2slack message: 'My message', webhookUrl: 'https://hooks.slack.com/services/....', channel: '#channel', 

You can build this using the "pipeline syntax" help and choosing the "step" sample
step followed by the "Send a message to a Slack incoming webhook" "Build Step".
