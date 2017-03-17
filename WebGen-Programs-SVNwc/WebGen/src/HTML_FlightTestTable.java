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
*  File:         HTML_FlightTestTable.java                                  *
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
* File: HTML_FlightTestTable.java                                  		    *
*                                                                           *
* Purpose:                                                                  *
*   Generate each flight test table and output it
* Functionality:      														*     
* 	 1) creates table structure												*
* 	 2) populates table														*
* 	 3) writes to file														*
*                                                                           *
*****************************************************************************/
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * @author 	Davis Catherman
 * 			Christopher Newport University, class of 2018
 * 			DavisCatherman.com
 * 
 * date:	Fall 2016
 */
public class HTML_FlightTestTable {

	/**
	 * the basic structure of building the file is here
	 * 
	 * @param dep
	 *            current deployment being added
	 */
	public static void buildFile(Deployment dep) {
		String pgTitle = "Dep " + String.format("%03d", dep.getDepNum());
		String table = buildTable(dep);
		
		String str = Vars.FT_template;
		str = str.replace("INSERT_TITLE_HERE", pgTitle);
		str = str.replace("INSERT_FLIGHT_TABLE_HERE", table);
		
		Vars.file = str;
	}

	/**
	 * generates the contents of the table with the table cells and such
	 * 
	 * @param dep
	 *            current deployment table being made
	 * @return string with table contents
	 */
	private static String buildTable(Deployment dep) {
		StringBuilder tblTitle = new StringBuilder();
		tblTitle.append(dep.getFlights().get(0).fVeh);
		tblTitle.append(String.format(" DEP-%d FLIGHT TEST TABLE", dep.getDepNum()));

		StringBuilder tbl = new StringBuilder();
		tbl.append(Vars.TABLE_NAME.replace("INSERT_HERE", tblTitle));
		tbl.append(Vars.TABLE_GENERIC);

		int rowLength = 6 + dep.getAllSubDirs().size();
		String[] headers = new String[rowLength];
		headers[0] = "DEP";
		headers[1] = "Date";
		headers[2] = "Location";
		headers[3] = "VEH";
		headers[4] = "FLT #";
		headers[5] = "Systems Verfied?";

		for (int i = 0; i < rowLength - 6; i++) {
			headers[i + 6] = dep.getAllSubDirs().get(i);
		}
		//for (String header : headers) {
		//	tbl.append(Vars.COLUMN_WIDTH);
		//}

		tbl.append(Vars.TBL_ROW_1);

		int n = 0;
		for (String header : headers) {
			String width;
			if(n==0 || n==3 || n==4 || n==5)
				width = "50";
			else
				width = "100";
			
			tbl.append(Vars.COLUMN_NAME.replace("INSERT_NAME_HERE", header).replace("INSERT_WIDTH_HERE", width));
			n++;
		}
		tbl.append(Vars.TBL_ROW_2);

		for (Flight flt : dep.getFlights()) {
			if (!flt.getfNum().equals("-1")) {
				String[] temp = new String[rowLength];

				StringBuilder row = new StringBuilder();

				temp[0] = String.format("%02d", dep.getDepNum());
				temp[1] = flt.getfDate();
				temp[2] = flt.getfLoc();
				temp[3] = flt.getfVeh();
				
				temp[4] = String.format("%s", flt.getfNum());
				
				String imgTooltip = flt.imageTooltip.replaceAll("\n", Vars.NEW_LINE);
				String img;
				if(flt.sysVerification.equalsIgnoreCase("verified"))
					img = String.format("<img src=\"source_files/Checkmark.png\" width=\"20\" height=\"14\" title=\"%s\">", imgTooltip);
				else
					img = String.format("<img src=\"source_files/warning.png\" width=\"20\" height=\"14\" title=\"%s\">", imgTooltip);
				temp[5] = img;

				for (String s : temp) {
					if (s != null) {
						String cell = Vars.DATA_CELL.replace("INSERT_HERE", s);
						row.append(cell);
					}
				}

				for (int i = 6; i < headers.length; i++) {
					String c = "";
					if (flt.getfNoPathSubDirs().contains(headers[i])) {
						String link = Vars.DATA_LINK;
						StringBuilder dest = new StringBuilder();
						dest.append(headers[i]);
						dest.append(String.format("_D%03d_F%s", flt.getDepNum(), flt.getfNum()));
						dest.append(".html");

						c = link.replace("INSERT_HERE", dest);
						if (headers[i].contains("Not Operated"))
							c = "X";

					}
					String cell = Vars.DATA_CELL.replace("INSERT_HERE", c);
					row.append(cell);
				}

				tbl.append(Vars.DATA_ROW.replace("INSERT_HERE", row.toString()));
			}
		}
		
		tbl.append(Vars.END_TABLE);
		
		//String retroBrowse = Vars.GENERIC_LINK;
		//retroBrowse = retroBrowse.replace("INSERT_LINKED_LOCATION_HERE", dep.depDir);
		
		return tbl.toString();
	}

	/**
	 * writes the generated file to the correct name
	 * 
	 * @param depNum
	 *            current dep num being operated on
	 */
	public static void writeFile(int depNum) {
		PrintWriter writer;
		try {
			String fileName = String.format(Vars.HTML_PATH+"DEP_%d_table.html", depNum);
			writer = new PrintWriter(fileName, "UTF-8");
			writer.print(Vars.file);
			writer.close();
			System.out.println(String.format("Dep %d Written", depNum));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
