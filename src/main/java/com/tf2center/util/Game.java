package com.tf2center.util;

import java.io.Serializable;

public class Game implements Serializable {
	private static final long serialVersionUID = -3890956579909647631L;

	private int appId;
	private String name;
	private int playTime2Weeks;
	private int playTimeForever;

	public Game(String name, int playTime2Weeks, int playTimeForever, int appId) {
		this.name = name;
		this.playTime2Weeks = playTime2Weeks;
		this.playTimeForever = playTimeForever;
		this.appId = appId;
	}

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPlayTime2Weeks() {
		return playTime2Weeks;
	}

	public void setPlayTime2Weeks(int playTime2Weeks) {
		this.playTime2Weeks = playTime2Weeks;
	}

	public int getPlayTimeForever() {
		return playTimeForever;
	}

	public void setPlayTimeForever(int playTimeForever) {
		this.playTimeForever = playTimeForever;
	}
}