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
*  File:         HTML_FlightList.java	                                    *
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
* File: HTML_FlightList.java                                       		    *
*                                                                           *
* Purpose:                                                                  *
*   Generate flight list table and output it								*
* Functionality:      														*     
* 	 1) creates table structure												*
* 	 2) populates table														*
* 	 3) writes to file														*
*                                                                           *
*****************************************************************************/
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Davis Catherman Christopher Newport University, class of 2018
 *         DavisCatherman.com
 * 
 *         date: Fall 2016
 */
public class HTML_FlightList {

	protected String test;
	/**
	 * WIP: This method is currently unused This is a feature to be added so
	 * that if chosen so, the operator can choose to only append to the flight
	 * list table instead of rewriting it. More code needs developed to be
	 * operational.
	 * 
	 * @param dep
	 *            current deployment being worked with
	 */
	public static void updateTemplate(Deployment dep) {
		// create a string containing the dep number
		String depStr = "DEP_" + dep.getDepNum();

		// use the string to get a substring (should be the flightList to near
		// the last created)
		String templateP1 = Vars.FL_template.substring(Vars.FL_template.indexOf(depStr));

		// finish template 1 based on above and the close of the table-row
		int rowIndex = templateP1.indexOf("</tr>");
		templateP1 = templateP1.substring(0, templateP1.indexOf(rowIndex));

		// create template 2 based on the end of the table
		int endTable = templateP1.indexOf("<!--END_FLIGHT LIST_TABLE-->");
		String templateP2 = Vars.FL_template.substring(endTable);

		// combine the two templates
		Vars.FL_template = templateP1 + templateP2;
	}

	/**
	 * bulk work for generating the flight List table. may need modification to
	 * work with the update template
	 * 
	 * @param dep
	 *            current deployment
	 */
	public static void buildPage(Deployment dep) {
		// use some basic template variables
		String row = Vars.DEP_ROW;
		String link = Vars.DEP_LINK;

		// if no flights are found, no vehicles were found
		if (dep.getFlights().size() == 0)
			link = "No Vehicles Found";
		else
			// if flight counter wasn't incremented. then no flights found
			if (dep.getFlightCount() == 0)
				link = "No Flights Found";

		// insert the link template, or msg about no data found based on previous if statements
		row = row.replace("INSERT_DEP_LINK_HERE", link);
		
		// insert dep num throughout the row
		row = row.replaceAll("INSERT_DEP_NUM_HERE", String.format("%d", dep.getDepNum()));

		// setup strings to contain list of vehicles and flights
		String fltNumsStr = "";
		String fltVehsStr = "";
		
		// create array lists for each list
		ArrayList<String> fltNums = new ArrayList<String>();
		ArrayList<String> fltVehs = new ArrayList<String>();

		// loop through the flights array list per deployment
		for (Flight flt : dep.getFlights()) {
			
			// if has flight num, add to list
			if (!flt.getfNum().equals("-1"))
				fltNums.add(removeLeadingZeros(flt.getfNum()));
			
			// add each vehicle too
			fltVehs.add(flt.getfVeh());
		}
		
		// sort the flight nums
		ArrayList<Str> data = new ArrayList<Str>();
		
		// add data to new format
		for(String x : fltNums)
			data.add(new Str(x));
		
		data.sort(null);
		fltNums.clear();
		
		for(Str x : data)
			fltNums.add(x.s);

		// remove duplicate flt vehs
		for (int i = 0; i < fltVehs.size(); i++) {
			for (int j = i + 1; j < fltVehs.size(); j++) {
				String is = fltVehs.get(i);
				String js = fltVehs.get(j);
				
				// if equal, than it is a dup, remove it
				if (is.equals(js)) {
					fltVehs.remove(j);
					j--;
				}
			}
		}

		// remove duplicate flt nums
		for (int i = 0; i < fltNums.size(); i++) {
			for (int j = i + 1; j < fltNums.size(); j++) {
				String ii = fltNums.get(i);
				String ji = fltNums.get(j);

				// if equal, than it is a dup, remove it
				if (ii.equals(ji)) {
					fltNums.remove(j);
					j--;
				}
			}
		}
		
		// convert the to lists to strings
		fltNumsStr = fltNums.toString();
		fltVehsStr = fltVehs.toString();
		
		// remove the last character
		fltNumsStr = fltNumsStr.substring(1, fltNumsStr.length() - 1);
		fltVehsStr = fltVehsStr.substring(1, fltVehsStr.length() - 1);

		// replace each comma with an HTML tab
		fltNumsStr = fltNumsStr.replaceAll(", ", " " + Vars.TAB + " ");
		fltVehsStr = fltVehsStr.replaceAll(", ", " " + Vars.TAB + " ");

		// insert the two stings in the correct locations for the row
		row = row.replace("INSERT_FLIGHT_NUMS_HERE", fltNumsStr);
		row = row.replace("INSERT_VEHICLE_HERE", fltVehsStr);
		
		// append a new INSERT tag to the row
		row += "\n<!--INSERT_TABLE_DATA_HERE-->";
		
		// replace the old INSERT tag with the new row
		Vars.FL_template = Vars.FL_template.replace("<!--INSERT_TABLE_DATA_HERE-->", row);
	}

	/**
	 * write the file to the HTML Path and print the message
	 */
	public static void writeFile() {
		PrintWriter writer;
		try {
			String fileName = String.format(Vars.HTML_PATH + "\\flight_list.html");
			writer = new PrintWriter(fileName, "UTF-8");
			writer.print(Vars.file);
			writer.close();
			System.out.println(Vars.HTML_PATH + "flight_list.html Written");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	static String removeLeadingZeros(String str) {
		  while (str.indexOf("0")==0)
		    str = str.substring(1);
		  return str;
		}
}
class Str<T extends Comparable<T>> implements Comparable<Str<T>> {
	protected String s;
	
	public Str(String x) {
		this.s = x;
	}

	@Override
	public int compareTo(Str<T> str2) {

		String fNum1 = "##########".substring(s.length()) + s;
		String fNum2 = "##########".substring(str2.s.length()) + str2.s;
		return (fNum1.compareTo(fNum2));
	}
}
