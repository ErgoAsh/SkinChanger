package net.hytekgames.skinchanger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.profile.property.ProfileProperty;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class SkinAPI {

	private static final String uuidurl = "https://api.minetools.eu/uuid/%name%";
	private static final String skinurl = "https://api.minetools.eu/profile/%uuid%";

	public static String getUUID(String name) throws SkinRequestException {
		String output;
		try {
			output = readURL(uuidurl.replace("%name%", name));

			JsonElement element = new JsonParser().parse(output);
			JsonObject obj = element.getAsJsonObject();

			if (obj.has("status")) {
				if (obj.get("status").getAsString().equalsIgnoreCase("ERR")) {
					return null;
				}
			}

			if (obj.get("id").getAsString().equalsIgnoreCase("null"))
				throw new SkinRequestException("USER_DOESNT_EXIST");

			return obj.get("id").getAsString();
		} catch (IOException e) {
			return null;
		}
	}

	public static Optional<ProfileProperty> getSkinProperty(String uuid) {
		String output;
		try {
			output = readURL(skinurl.replace("%uuid%", uuid));
			JsonElement element = new JsonParser().parse(output);
			JsonObject obj = element.getAsJsonObject();

			Property property = new Property();
			if (obj.has("raw")) {
				JsonObject raw = obj.getAsJsonObject("raw");

				if (property.valuesFromJson(raw)) {
					Optional<ProfileProperty> textures = Optional
							.<ProfileProperty>of(Sponge.getServer().getGameProfileManager()
									.createProfileProperty("textures", property.getValue(), property.getSignature()));
					return textures;
				}
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	private static String readURL(String url) throws IOException {
		HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();

		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", "SkinChanger-API");
		con.setConnectTimeout(5000);
		con.setReadTimeout(5000);
		con.setDoOutput(true);

		String line;
		StringBuilder output = new StringBuilder();
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

		while ((line = in.readLine()) != null)
			output.append(line);

		in.close();
		return output.toString();
	}

	private static class Property {
		private String value;
		private String signature;

		boolean valuesFromJson(JsonObject obj) {
			if (obj.has("properties")) {
				JsonArray properties = obj.getAsJsonArray("properties");
				JsonObject propertiesObject = properties.get(0).getAsJsonObject();

				String signature = propertiesObject.get("signature").getAsString();
				String value = propertiesObject.get("value").getAsString();

				this.setSignature(signature);
				this.setValue(value);

				return true;
			}

			return false;
		}

		String getValue() {
			return value;
		}

		void setValue(String value) {
			this.value = value;
		}

		String getSignature() {
			return signature;
		}

		void setSignature(String signature) {
			this.signature = signature;
		}
	}

	public static class SkinRequestException extends Exception {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String reason;

		public SkinRequestException(String reason) {
			this.reason = reason;
		}

		public String getReason() {
			return reason;
		}

		public String getMessage() {
			return reason;
		}

	}

}
