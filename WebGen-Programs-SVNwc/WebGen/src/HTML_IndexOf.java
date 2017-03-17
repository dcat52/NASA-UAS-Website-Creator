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
*  File:         HTML_IndexOf.java              	                        *
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
* File: HTML_IndexOf.java		                                  		    *
*                                                                           *
* Purpose:                                                                  *
*   Generate each index of page and output it								*
* Functionality:      														*     
* 	 1) creates page structure												*
* 	 2) populates page														*
* 	 3) writes to file														*
*                                                                           *
*****************************************************************************/
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author 	Davis Catherman
 * 			Christopher Newport University, class of 2018
 * 			DavisCatherman.com
 * 
 * date:	Fall 2016
 */
public class HTML_IndexOf {
	
	public static void buildPageForFlights(Deployment dep, Flight flt, String dir) {
		String page = Vars.IO_template;
		dir = dir.replace("\\", "\\\\");
		page = page.replaceAll("INSERT_INDEX_LOCATION_HERE", dir.replace(Vars.DRIVE_LETTER, "..\\..\\..\\"));

		String table = generateLinks(dir);
		page = page.replace("INSERT_TABLE_HERE", table);
		
		StringBuilder pgName = new StringBuilder();
		pgName.append(Vars.HTML_PATH);
		pgName.append(dir.substring(dir.lastIndexOf("\\")+1));
		
		pgName.append(String.format("_D%03d_F%s.html", flt.getDepNum(), flt.getfNum()));
		
		String temp = pgName.toString().toLowerCase();
		if(!temp.contains("not operated") && !temp.contains("no data"))
			writeFile(pgName.toString(), page);
	}
	
	public static void buildPageForComponents(Component c) {
		String page = Vars.IO_template;
		String tbl = generateLinks(c.path);
		String dir = c.path.replaceAll("\\\\", "\\\\\\\\");
		page = page.replaceAll("INSERT_INDEX_LOCATION_HERE", dir.replace(Vars.DRIVE_LETTER, "..\\"));
		page = page.replace("INSERT_TABLE_HERE", tbl);
		
		StringBuilder pgName = new StringBuilder();
		pgName.append(Vars.HTML_PATH);
		pgName.append("component_ " + c.name + ".html");
		writeFile(pgName.toString(), page);
		
	}
	
	public static void buildPageForVehInfo(MiscIndexOf mio) {
		String page = Vars.IO_template;
		String tbl = generateLinks(mio.path);
		String dir = mio.path.replaceAll("\\\\", "\\\\\\\\");
		page = page.replaceAll("INSERT_INDEX_LOCATION_HERE", dir.replace(Vars.DRIVE_LETTER, "..\\"));
		page = page.replace("INSERT_TABLE_HERE", tbl);
		
		StringBuilder pgName = new StringBuilder();
		pgName.append(Vars.HTML_PATH);
		pgName.append(mio.pageName);
		writeFile(pgName.toString(), page);
	}
	
	public static void buildDropDownMenus(ArrayList<MiscIndexOf[]> sepByVeh) {
		StringBuilder dropDown = new StringBuilder();
		for(MiscIndexOf[] vehMios : sepByVeh) {
			StringBuilder allLI = new StringBuilder();
			for(MiscIndexOf mio : vehMios) {
				String li = Vars.SUB_LIST_ITEM;
				li = li.replace("INSERT_LINK_HERE", Vars.GENERIC_LINK);
				li = li.replace("INSERT_LINKED_LOCATION_HERE", mio.pageName);
				li = li.replace("INSERT_DISPLAY_TEXT_HERE", mio.dataType);
				allLI.append(li);
			}
			String vi = Vars.VEH_LIST_ITEM;
			vi = vi.replace("INSERT_SUB_LIST_ITEMS_HERE", allLI.toString());
			vi = vi.replace("INSERT_VEHICLE_HERE", vehMios[0].veh);
			dropDown.append(vi);
		}
			
		Vars.FL_template = Vars.FL_template.replace("INSERT_VEHICLE_MENUS_HERE", dropDown.toString());
		Vars.FT_template = Vars.FT_template.replace("INSERT_VEHICLE_MENUS_HERE", dropDown.toString());
		Vars.IO_template = Vars.IO_template.replace("INSERT_VEHICLE_MENUS_HERE", dropDown.toString());
		Vars.CL_template = Vars.CL_template.replace("INSERT_VEHICLE_MENUS_HERE", dropDown.toString());
	}
	
	public static String generateLinks(String path) {
		File f = new File(path);
		File[] files = f.listFiles();
		
		ArrayList<String> links = new ArrayList<String>();
		
		for (File file : files) {

			long millisec;
			millisec = file.lastModified();
			Date dt = new Date(millisec);
			
			String entry = "";
			if (file.isDirectory()) {
				entry = "<tr><td valign=\"top\"><img src=\"..\\"
						+ "HTML\\source_files\\usiiik_Folder_Icon.png\" alt=\"[DIR]\" style=\"height:20px;width:20px;\"></td><td><a href= \""
						+ file.getPath() + "\">" + file.getName() + "</a></td><td align=\"right\">" + dt
						+ "</td><td align=\"right\">" + "</td><td>&nbsp;</td></tr>\n";
			} else {
				entry = "<tr><td valign=\"top\"><img src=\"..\\"
						+ "HTML\\source_files\\FineFiles\\default.ico\" alt=\"[DIR]\" style=\"height:20px;width:20px;\"></td><td><a href= \""
						+ file.getPath() + "\">" + file.getName() + "</a></td><td align=\"right\">" + dt
						+ "</td><td align=\"right\">" + "</td><td>&nbsp;</td></tr>\n";
				String ft = file.getName().substring(file.getName().lastIndexOf('.')+1).toLowerCase();
				
				if(Vars.FILE_TYPE.contains(ft)) {
					entry = entry.replace("default.ico", ft + ".ico");
				}
				if(ft.length() > 3) {
					ft = ft.substring(0, 3);
				}
				if(Vars.ALT_TYPE.contains(ft)) {
					int i = Vars.ALT_TYPE.indexOf(ft) + 1;
					entry = entry.replace("default.ico", Vars.ALT_TYPE.get(i) + ".ico");
				}
			}
			links.add(entry);
			
		}

		links.sort(null);
		
		StringBuilder linksString = new StringBuilder();
		
		for(String l : links)
			linksString.append(l);
		
		String s = linksString.toString();
		s = s.replaceAll(Vars.DRIVE_LETTER, "../../../");
		return s;
	}
	
	
	public static void writeFile(String fileName, String data) {
		PrintWriter writer;
		try {
			writer = new PrintWriter(fileName, "UTF-8");
			writer.print(data);
			writer.close();
			System.out.println(String.format("File %s Written", fileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
