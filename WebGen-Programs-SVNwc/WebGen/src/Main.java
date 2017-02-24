/*****************************************************************************
* WebGen Suite - Web Site Creator											*
*      __       __            __         ______                      		*
*     |\ \  _  |\ \          |\ \       /\     \                     		*
*     | $$ /\\ | $$  ______  | $$____  |\ $$$$$$\  ______   _______  		*
*     | $$/  $\| $$ /\     \ | $$    \ | $$ __\$$ /\     \ |\      \ 		*
*     | $$  $$$\ $$|\ $$$$$$\| $$$$$$$\| $$|\   \|\ $$$$$$\| $$$$$$$\		*
*     | $$ $$\$$\$$| $$    $$| $$  | $$| $$ \$$$$| $$    $$| $$  | $$		*
*     | $$$$  \$$$$| $$$$$$$$| $$__/ $$| $$__| $$| $$$$$$$$| $$  | $$		*
*     | $$$    \$$$ \$$     \| $$    $$ \$$  \ $$ \$$     \| $$  | $$		*
*      \$$      \$$  \$$$$$$$ \$$$$$$$   \$$$$$$   \$$$$$$$ \$$   \$$		*
*  																			*
*  File:         Main.java		                                            *
*                                                                           *
*****************************************************************************
*                                                                           *
*  NOTICE:                                                                  *
*                                                                           *
*    THIS SOFTWARE MAY BE USED, COPIED AND PROVIDED TO OTHERS ONLY AS       *
*    PERMITTED UNDER THE TERMS OF THE CONTRACT OR OTHER AGREEMENT UNDER     *
*    WHICH IT WAS ACQUIRED FROM THE U.S. GOVERNMENT.  NEITHER TITLE TO      *
*    NOR OWNERSHIP OF THE SOFTWARE IS HEREBY TRANSFERRED.  THIS NOTICE      *
*    SHALL REMAIN ON ALL COPIES OF THE SOFTWARE.                            *
*                                                                           *
*****************************************************************************/
/*****************************************************************************
* File: Main.java                                             		        *
*                                                                           *
* Purpose:                                                                  *
*   This file contains the main source code for the Website Generator       *
* Functionality:                                                            *
*    1) WebGen will go through the given directories to parse data.			*
*    2) Only the specified range of deployment numbers will be parsed.		*
*    3) Data parsed will will be stored in Deployment and Flight objects.	*
*    4) WebGen will read template HTML files into variables					*
*    5) The Flight Tables and IndexOf pages will be auto-generated.			*
*    6) The Flight List table will append new data unless overwrite is set.	*
*    7) The output contains info about deployments being written or skipped.*
*                                                                           *
*****************************************************************************/

import java.io.File;
import java.util.ArrayList;

/**
 * @author Davis Catherman Christopher Newport University, class of 2018
 *         DavisCatherman.com
 * 
 *         date: Fall 2016
 */
public class Main {

	/**
	 * TODO: components page automation; add software pages (GNDWatch, EdgeAOS)
	 */
	public static void main(String[] args) {

		// call the Config file
		// edit the Config to change minor settings
		Config.settingsManager();

		// paths to search for flight/deployment data
		// for edgeshare, mounted as Z: drive
		String[] paths = Vars.paths;

		// create array list and get dirs recursively for all paths above
		ArrayList<File> dirs = new ArrayList<File>();
		for (String singlePath : paths) {
			getDirs(singlePath, dirs);
		}

		// process only these deployment numbers and between
		// setting has been moved to Config.java
		int depMin = Vars.depMin;
		int depMax = Vars.depMax;

		// array list to hold deployment objects
		ArrayList<Deployment> deps = new ArrayList<Deployment>();

		// for all the deployment numbers to be searched
		for (int i = depMin; i <= depMax; i++) {

			// create a new deployment with that number
			Deployment tempD = new Deployment(i);
			deps.add(tempD);

			// add parent dirs that correspond to that deployment (based on dir
			// naming conventions)
			ArrayList<String> depFlights = findDeploy(i, dirs);

			// for each dir that was pulled (it most likely is a flight dir)
			for (String flightDir : depFlights) {

				// create a new flight object with the dir and it will parse the
				// data on creation
				Flight tempF = new Flight(i, flightDir);

				// add the flight to the deployment specific array list of
				// flights
				tempD.addFlight(tempF);

				// if it has a valid flight num, then is an actual flight
				// directory
				if (!tempF.getfNum().equals("-1")) {

					// increment flight counter
					tempD.incFlightCount();

					// for each sub directory parsed in the flight directory
					for (String fDir : tempF.getfSubDirs()) {

						// split the sub dir so the last portion can be
						// harvested for the FlightTestTable
						String[] splitDir = fDir.split("\\\\");

						// get that last portion
						String lastSplit = splitDir[splitDir.length - 1];

						// if the deployment does not already contains this one,
						// then add it
						if (!tempD.getAllSubDirs().contains(lastSplit)) {
							tempD.addToAllSubDirs(lastSplit);
						}
					}
				}
			}
		}

		// call to read in the template files for the HTML outputting
		HTML.main(null);

		// go through deployment array list for building flight test tables
		for (Deployment singleDep : deps) {

			// if the program found a flight for that deployment, build the file
			// and write it
			if (singleDep.getFlights().size() != 0) {

				// sort the flights for that deployment so they are in order
				singleDep.sortFlights();

				if (singleDep.getFlightCount() != 0) {
					HTML_FlightTestTable.buildFile(singleDep);
					HTML_FlightTestTable.writeFile(singleDep.getDepNum());
				}
			}

			if (singleDep.getFlightCount() == 0) {

				// else alert the user that it is being skipped
				System.out.println(String.format("Warning! Skipping Dep %d", singleDep.getDepNum()));
			}
		}

		// go through deployments for building index of pages
		for (Deployment singleDep : deps) {

			// go through each flight of each deployment
			for (Flight flt : singleDep.getFlights()) {

				// go through each sub dir for each flight
				for (String dir : flt.getfSubDirs()) {

					// build a page for each sub dir (even if sub dir is empty
					// [for now])
					HTML_IndexOf.buildPageForFlights(singleDep, flt, dir);
				}
			}
		}

		// go through deployments for building flight list table
		for (Deployment singleDep : deps) {

			// build new table row from scratch
			HTML_FlightList.buildPage(singleDep);

		}

		// save to file var to be written
		Vars.file = Vars.FL_template;
		HTML_FlightList.writeFile();
	}

	/**
	 * This method recursively begins at the initial directory name passed and
	 * navigates through sub-directories, returning directories with complete
	 * paths in the array list
	 * 
	 * @param directoryName
	 *            ArrayList of filenames (passed as arg to work recursively)
	 * @param dirs
	 *            String of directory path used to recursively go through
	 *            directories
	 * 
	 */
	public static void getDirs(String directoryName, ArrayList<File> dirs) {
		File directory = new File(directoryName);

		// get all the directories from a directory
		File[] fList = directory.listFiles();
		for (File file : fList) {
			if (file.isDirectory()) {
				dirs.add(file);
				getDirs(file.getAbsolutePath(), dirs);
			}
		}
	}

	/**
	 * creates an array list of the directories immediately concerning a
	 * specified deployment number Note: based on directory naming convention
	 * 
	 * @param depNumber
	 *            current deploy being worked with
	 * @param dirs
	 *            all main dirs to be worked with
	 * @return ArrayList<String> with deployment directories
	 * 
	 */
	public static ArrayList<String> findDeploy(int depNumber, ArrayList<File> dirs) {
		ArrayList<String> list = new ArrayList<String>();
		String deployment = Integer.toString(depNumber);
		for (File x : dirs) {
			String[] singleDir = x.getPath().split("\\\\");
			String y = singleDir[singleDir.length - 1];
			if ((y.contains("D" + deployment + " ") || y.contains("D0" + deployment + " ")
					|| y.contains("D00" + deployment + " ")) || y.contains("Deployment " + deployment + " ")) {
				list.add(x.getPath());
			}
		}
		return list;
	}
}
