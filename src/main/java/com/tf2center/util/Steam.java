package com.tf2center.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.json.JSONArray;
import org.apache.wicket.ajax.json.JSONObject;
import org.springframework.context.ApplicationContext;

public class Steam {
	private static ApplicationContext ctx = ApplicationContextProvider.applicationContext;

	private static String steamApiKey = (String) ctx.getBean("steamApiKey");

	private static Boolean offlineMode = (Boolean) ctx.getBean("offlineMode");

	public static List<Game> getRecentlyPlayed(String steamId) throws IOException {
		String recentlyPlayedResponse;
		if (offlineMode) {
			recentlyPlayedResponse = (String) ctx.getBean("mockRecentlyPlayedResponse");
		} else {
			String urlString = String.format(
					"http://api.steampowered.com/IPlayerService/GetRecentlyPlayedGames/v0001/?key=%s&steamid=%s&format=json",
					steamApiKey, steamId);

			BufferedReader reader = null;
			try {
				URL url = new URL(urlString);
				reader = new BufferedReader(new InputStreamReader(url.openStream()));
				StringBuffer buffer = new StringBuffer();
				int read;
				char[] chars = new char[1024];
				while ((read = reader.read(chars)) != -1)
					buffer.append(chars, 0, read);
				recentlyPlayedResponse = buffer.toString();
			} finally {
				if (reader != null)
					reader.close();
			}
		}

		JSONArray games = new JSONObject(recentlyPlayedResponse).getJSONObject("response")
				.getJSONArray("games");
		List<Game> output = new ArrayList<Game>(games.length());
		for (int i = 0; i < games.length(); i++) {
			JSONObject game = games.getJSONObject(i);
			output.add(new Game(game.getString("name"), game.getInt("playtime_2weeks"),
					game.getInt("playtime_forever"), game.getInt("appid")));
		}
		return output;
	}
}