package com.example.miranda.monitoringpdam.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Miranda on 08/02/2018.
 */

public class History {

    @SerializedName("time")
    @Expose
    private String time;

    @SerializedName("severity")
    @Expose
    private String severity;

    @SerializedName("recovery_time")
    @Expose
    private String recovery_time;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("info")
    @Expose
    private String info;

    @SerializedName("host")
    @Expose
    private String host;

    @SerializedName("problem")
    @Expose
    private String problem;

    @SerializedName("duration")
    @Expose
    private String duration;

    @SerializedName("ack")
    @Expose
    private String ack;

    @SerializedName("actions")
    @Expose
    private String actions;

    @SerializedName("tags")
    @Expose
    private String tags;


    public History(String s, String s1, String s2, String s3, String s4, String s5, String s6, String s7, String s8, String s9, String s10) {
    }

    public String getTime() {

        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSeverity() {

        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getRecovery_time() {

        return recovery_time;
    }

    public void setRecovery_time(String recovery_time) {
        this.recovery_time = recovery_time;
    }

    public String getStatus() {

        return status;
    }

    public void setStatus(String status) {

        this.status = status;
    }

    public String getInfo() {

        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getHost() {

        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getProblem() {

        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getDuration() {

        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getAck() {

        return ack;
    }

    public void setAck(String ack) {
        this.ack = ack;
    }

    public String getActions() {

        return actions;
    }

    public void setActions(String actions) {
        this.actions = actions;
    }

    public String getTags() {

        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }



}
