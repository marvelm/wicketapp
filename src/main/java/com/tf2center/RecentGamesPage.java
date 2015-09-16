package com.tf2center;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;

import com.tf2center.util.Game;
import com.tf2center.util.Steam;

public class RecentGamesPage extends AuthRequiredPage {
	public RecentGamesPage() {
		super();

		final ListView<Game> recentlyPlayedGames = new ListView<Game>("games") {
			@Override
			protected void populateItem(ListItem<Game> item) {
				Game game = item.getModelObject();
				item.add(new Label("name", Model.of(game.getName())));
				item.add(new Label("hours", Model.of(game.getPlayTimeForever() / 60)));
			}
		};

		final Label message = new Label("message", Model.of("Enter a Steam id"));

		final TextField<String> steamId = new TextField<String>("steamId", Model.of(""));

		Form form = new Form("search") {
			@Override
			protected void onSubmit() {
				if (!StringUtils.isBlank(steamId.getInput())) {
					String id = steamId.getInput();
					try {
						recentlyPlayedGames.setList(Steam.getRecentlyPlayed(id));
						recentlyPlayedGames.render();
						message.setDefaultModel(Model
								.of(String.format("Recently played by SteamID(%s)", id)));
					} catch (Exception e) {
						e.printStackTrace();
						message.setDefaultModel(Model.of("Invalid SteamID"));
					} finally {
						message.render();
					}
				}
			};
		};

		form.add(steamId);

		this.add(message);
		this.add(recentlyPlayedGames);
		this.add(form);
	}
}