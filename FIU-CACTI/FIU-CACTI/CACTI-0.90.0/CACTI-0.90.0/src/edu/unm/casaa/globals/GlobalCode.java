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

package edu.unm.casaa.globals;

import java.util.ListIterator;
import java.util.Vector;
import edu.unm.casaa.main.MainController;


public class GlobalCode { 


    // List of available codes. Built when we parse XML file.
    private static Vector< GlobalCode > list             = new Vector<>();

    public int                          id               = 0;
    public String                       name;                                         // Name for use in file. Ex: "ACCEPTANCE".
    public String                       label;                                        // Human-readable label for use in UI. Ex: "Acceptance".
    public int                          defaultRating    = 1;
    public int                          minRating        = 1;
    public int                          maxRating        = 5;
    public int 							minHW			 = 0;
    public int 							maxHW			 = 4;
    public int 							minBalance       =-2;
    public int 							maxBalance       = 2;
    
    // Class:


    /**
     * Expose list iterator
     * @return
     */
    public static ListIterator<GlobalCode> getIterator() {
        return list.listIterator();
    }


    // Add new code.  Returns true on success, shows warning dialog on failure.
    public static boolean   addCode( GlobalCode newCode ) {
        // Check that we're not duplicating an existing id or label.
        for( int i = 0; i < list.size(); i++ ) {
            GlobalCode code = list.get( i );

            if( code.id == newCode.id || code.name.equals( newCode.name ) ) {
                MainController.showFatalWarning(
                        "User Code Error",
                        "New global code " + 
                        newCode.toDisplayString() + " conflicts with existing global code " + code.toDisplayString() );
                return false;
            }
        }
        list.add( newCode );
        return true;
    }

    public static int numCodes() {
        return list.size();
    }

    // PRE: index < numCodes().
    public static GlobalCode codeAtIndex( int index ) {
        return list.get( index );
    }

    // PRE: code exists with given id.
    public static GlobalCode codeWithId( int value ) {
        // Check user codes.
        for (GlobalCode code : list) {
            if (code.id == value) {
                return code;
            }
        }
        assert false : "Global code with given id not found: " + value;
        return null;
    }

    // PRE: code exists with given name.
    public static GlobalCode codeWithName( String name ) {
        // Check user codes.
        for( int i = 0; i < list.size(); i++ ) {
            GlobalCode code = list.get( i );

            if( code.name.equals( name ) ) {
                return code;
            }
        }
        assert false : "Global code with given name not found: " + name;
        return null;
    }

    // Instance:

    public GlobalCode(int id, String name, String label ) {
        this.id     = id;
        this.name   = name;
        this.label  = label;
    }

    public GlobalCode() {
    }

    // Get string representation for use in user dialogs.
    public String toDisplayString() {
        return "(label: " + label + ", id: " + id + ")";
    }

    public static void clear() {
        list.clear();
    }

    @Override
    public String toString() {
        return this.name;
    }
}
