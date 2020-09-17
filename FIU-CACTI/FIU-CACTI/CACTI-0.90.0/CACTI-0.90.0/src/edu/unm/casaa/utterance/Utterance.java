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

package edu.unm.casaa.utterance;

import edu.unm.casaa.misc.MiscCode;
import javafx.util.Duration;

public interface Utterance {

	void setID(String id);
	void setMiscCodeByValue(int value);
	void setMiscCode(MiscCode code);
	void setStartTime(double startTime);
	void setAnnotation(String annotation);

	String getID();
	Duration getStartTime();
	MiscCode getMiscCode();

	String displayCoded();
    String getAnnotation();
	// TODO: rating ids and annotation are bypassing data model for utterance MiscDataItem which really sucks. RESOLVE
}
