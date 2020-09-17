/*
This source code file is part of the CASAA Treatment Coding System Utility
    Copyright (C) 2009  UNM CASAA
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package edu.unm.casaa.main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.prefs.Preferences;

/**
 * Interface for user config data. An XML file the user can edit outside of the
 * application to define codes and code layout.
 */
public class UserConfig {

	/* provide access to application preferences */
	private static final Preferences appPrefs = Preferences.userNodeForPackage(Main.class);
	/* default location of config file */
	private static final String defaultPath = String.format("%s%s%s", System.getProperty("user.home"),
			System.getProperty("file.separator"), "CactiUserConfiguration.xml");

	/**
	 * Writes out a default, minimal config file
	 */
	public static void writeDefault() throws IOException {

		File config = new File(getPath());

		try (PrintWriter writer = new PrintWriter(new FileWriter(config, false))) {
			writer.println("<userConfiguration>");
			writer.println("<codes><!-- Parent codes -->");
			writer.println("<code name=\"BLAME\" value=\"11\"/>");
			writer.println("<code name=\"PERSUADE\" value=\"12\"/>");
			writer.println("<code name=\"AFFIRM\" value=\"13\"/>");
			writer.println("<code name=\"ENCOURAGE\" value=\"14\"/>");
			writer.println("<code name=\"WISHES\" value=\"15\"/>");
			writer.println("<code name=\"NEUTRAL (PARENT)\" value=\"16\"/>");
			writer.println("<code name=\"NO CODE (PARENT)\" value=\"17\"/>");
			writer.println("<code name=\"CT\" value=\"18\"/>");
			writer.println("<code name=\"CT BP\" value=\"19\"/>");
			writer.println("<code name=\"CT REL\" value=\"20\"/>");
			writer.println("<code name=\"CT INT\" value=\"21\"/>");
			writer.println("<code name=\"ST\" value=\"22\"/>");
			writer.println("<code name=\"ST BP\" value=\"23\"/>");
			writer.println("<code name=\"ST REL\" value=\"24\"/>");
			writer.println("<code name=\"ST INT\" value=\"25\"/>");
			writer.println("</codes>");
			writer.println();
			writer.println("<codeControls panel=\"left\" label=\"Parent\"><!-- Parent button layout -->");
			writer.println("<row><button code=\"BLAME\"/><button code=\"PERSUADE\"/><button code=\"AFFIRM\"/><button code=\"ENCOURAGE\"/></row>");
			writer.println("<row><button code=\"WISHES\"/><button code=\"NEUTRAL (PARENT)\"/><button code=\"NO CODE (PARENT)\"/></row>");
			writer.println("<row><button code=\"CT\"/><button code=\"CT BP\"/><button code=\"CT REL\"/><button code=\"CT INT\"/></row>");
			writer.println("<row><button code=\"ST\"/><button code=\"ST BP\"/><button code=\"ST REL\"/><button code=\"ST INT\"/></row>");
			writer.println("</codeControls>");
			writer.println();
			writer.println("<codes><!-- Teen codes -->");
			writer.println("<code name=\"NO CODE (TEEN)\" value=\"31\"/>");
			writer.println("<code name=\"NEUTRAL (TEEN)\" value=\"32\"/>");
			writer.println("<code name=\"CONTRIBUTE\" value=\"33\"/>");
			writer.println("<code name=\"CONFRONT\" value=\"34\"/>");
			writer.println("<code name=\"CHANGE TALK\" value=\"35\"/>");
			writer.println("<code name=\"SUSTAIN TALK\" value=\"36\"/>");
			writer.println("</codes>");
			writer.println();
			writer.println("<codeControls panel=\"center\" label=\"Teen\"><!-- Teen button layout -->");
			writer.println("<row><button code=\"NO CODE (TEEN)\"/><button code=\"NEUTRAL (TEEN)\"/><button code=\"CONTRIBUTE\"/></row>");
			writer.println("<row><button code=\"CONFRONT\"/><button code=\"CHANGE TALK\"/><button code=\"SUSTAIN TALK\"/></row>");
			writer.println("</codeControls>");
			writer.println();
			writer.println("<codes><!-- Therapist codes -->");
			writer.println("<code name=\"NC-Th\" value=\"50\"/>");
			writer.println("<code name=\"GI\" value=\"51\"/>");
			writer.println("<code name=\"Q0\" value=\"52\"/>");
			writer.println("<code name=\"Q-\" value=\"53\"/>");
			writer.println("<code name=\"Q+\" value=\"54\"/>");
			writer.println("<code name=\"Q+/-\" value=\"55\"/>");
			writer.println("<code name=\"R0\" value=\"56\"/>");
			writer.println("<code name=\"R-\" value=\"57\"/>");
			writer.println("<code name=\"R+\" value=\"58\"/>");
			writer.println("<code name=\"R+/-\" value=\"59\"/>");
			writer.println("<code name=\"Ra\" value=\"60\"/>");
			writer.println("<code name=\"Rb\" value=\"61\"/>");
			writer.println("<code name=\"Ra/b\" value=\"62\"/>");
			writer.println("<code name=\"AF-Th\" value=\"63\"/>");
			writer.println("<code name=\"MIA\" value=\"64\"/>");
			writer.println("<code name=\"MINA\" value=\"65\"/>");
			writer.println("</codes>");
			writer.println();
			writer.println("<codeControls panel=\"right\" label=\"Therapist\"><!-- Therapist button layout -->");
			writer.println("<row><button code=\"NC-Th\"/><button code=\"GI\"/><button code=\"Q0\"/><button code=\"Q-\"/></row>");
			writer.println("<row><button code=\"Q+\"/><button code=\"Q+/-\"/><button code=\"R0\"/><button code=\"R-\"/></row>");
			writer.println("<row><button code=\"R+\"/><button code=\"R+/-\"/><button code=\"Ra\"/><button code=\"Rb\"/></row>");
			writer.println("<row><button code=\"Ra/b\"/><button code=\"AF-Th\"/><button code=\"MIA\"/><button code=\"MINA\"/></row>");
			writer.println("</codeControls>");
			writer.println();
			writer.println("<globals><!-- Global codes -->");
			writer.println("<global name=\"PARENTSOLUTIONFOCUSED\" label=\"Solution-Focused\" value=\"0\" defaultRating=\"-1\"/>");
			writer.println("<global name=\"PARENTENCOURAGINGTEENVOICE\" label=\"Encouraging Teen Voice\" value=\"1\" defaultRating=\"-1\"/>");
			writer.println("<global name=\"PARENTSELFEXPLORATION\" label=\"Parent Self Exploration\" value=\"2\" defaultRating=\"-1\"/>");
			writer.println("<global name=\"PARENTOPTIMISM\" label=\"Optimism\" value=\"3\" defaultRating=\"-1\"/>");
			writer.println("<global name=\"TEENPARTICIPATION\" label=\"Participation\" value=\"4\" defaultRating=\"-1\"/>");
			writer.println("<global name=\"TEENOPENNESS\" label=\"Openness\" value=\"5\" defaultRating=\"-1\"/>");
			writer.println("<global name=\"TEENSELFEXPLORATION\" label=\"Teen Self Exploration\" value=\"6\" defaultRating=\"-1\"/>");
			writer.println("<global name=\"TEENCONTEMPT\" label=\"Contempt\" value=\"7\" defaultRating=\"-1\"/>");
			writer.println("<global name=\"HOLDINGFOCUS\" label=\"Holding Focus\" value=\"8\" defaultRating=\"-1\"/>");
			writer.println("<global name=\"AUTONOMYSUPPORTPARENT\" label=\"Autonomy Support Parent\" value=\"9\" defaultRating=\"-1\"/>");
			writer.println("<global name=\"AUTONOMYSUPPORTTEEN\" label=\"Autonomy Support Teen\" value=\"10\" defaultRating=\"-1\"/>");
			writer.println("<global name=\"EMPATHY\" label=\"Empathy\" value=\"11\" defaultRating=\"-1\"/>");
			writer.println("<global name=\"ELICITINGPARENT\" label=\"Eliciting Parent\" value=\"12\" defaultRating=\"-1\"/>");
			writer.println("<global name=\"ELICITINGTEEN\" label=\"Eliciting Teen\" value=\"13\" defaultRating=\"-1\"/>");
			writer.println("<global name=\"BALANCE\" label=\"Balance\" value=\"14\" defaultRating=\"-3\"/>");
			writer.println("<global name=\"HWREVIEW\" label=\"HW Review\" value=\"15\" defaultRating=\"-1\"/>");
			writer.println("<global name=\"HWASSIGNMENT\" label=\"HW Assignment\" value=\"16\" defaultRating=\"-1\"/>");
			writer.println("<global name=\"HWCOMPLETIONPARENT\" label=\"HW Completion Parent\" value=\"17\" defaultRating=\"-1\"/>");
			writer.println("<global name=\"HWCOMPLETIONTEEN\" label=\"HW Completion Teen\" value=\"18\" defaultRating=\"-1\"/>");
			writer.println("</globals>");
			writer.println();
			writer.println("<!-- Global code layout -->");
			writer.println("<globalControls panel=\"left\"><slider global=\"PARENTSOLUTIONFOCUSED\"/></globalControls>");
			writer.println("<globalControls panel=\"left\"><slider global=\"PARENTENCOURAGINGTEENVOICE\"/></globalControls>");
			writer.println("<globalControls panel=\"left\"><slider global=\"PARENTSELFEXPLORATION\"/></globalControls>");
			writer.println("<globalControls panel=\"left\"><slider global=\"PARENTOPTIMISM\"/></globalControls>");
			writer.println("<globalControls panel=\"center\"><slider global=\"TEENPARTICIPATION\"/></globalControls>");
			writer.println("<globalControls panel=\"center\"><slider global=\"TEENOPENNESS\"/></globalControls>");
			writer.println("<globalControls panel=\"center\"><slider global=\"TEENSELFEXPLORATION\"/></globalControls>");
			writer.println("<globalControls panel=\"center\"><slider global=\"TEENCONTEMPT\"/></globalControls>");
			writer.println("<globalControls panel=\"right\"><slider global=\"HOLDINGFOCUS\"/></globalControls>");
			writer.println("<globalControls panel=\"right\"><slider global=\"AUTONOMYSUPPORTPARENT\"/></globalControls>");
			writer.println("<globalControls panel=\"right\"><slider global=\"AUTONOMYSUPPORTTEEN\"/></globalControls>");
			writer.println("<globalControls panel=\"right\"><slider global=\"EMPATHY\"/></globalControls>");
			writer.println("<globalControls panel=\"right\"><slider global=\"ELICITINGPARENT\"/></globalControls>");
			writer.println("<globalControls panel=\"right\"><slider global=\"ELICITINGTEEN\"/></globalControls>");
			writer.println("<globalControls panel=\"right\"><slider global=\"BALANCE\"/></globalControls>");
			writer.println("<globalControls panel=\"right\"><slider global=\"HWCOMPLETIONPARENT\"/></globalControls>");
			writer.println("<globalControls panel=\"center\"><slider global=\"HWCOMPLETIONTEEN\"/></globalControls>");
			writer.println("<globalControls panel=\"center\"><slider global=\"HWASSIGNMENT\"/></globalControls>");
			writer.println("<globalControls panel=\"center\"><slider global=\"HWREVIEW\"/></globalControls>");
			writer.println();
			writer.println("</userConfiguration>");

		}
	}

	/**
	 * Check for config file
	 * 
	 * @return
	 */
	public static boolean exists() {

		File file = new File(getPath());
		return file.canRead();

	}

	/**
	 * Set config file location
	 * 
	 * @param configPath
	 */
	public static void setPath(String configPath) {
		appPrefs.put("configFilePath", configPath);
	}

	/**
	 * Get config file location
	 * 
	 * @return
	 */
	public static String getPath() {

		// Don't want to send back empty path
		if (appPrefs.get("configFilePath", "").isEmpty()) {
			appPrefs.put("configFilePath", defaultPath);
		}
		return appPrefs.get("configFilePath", defaultPath);

	}

}
