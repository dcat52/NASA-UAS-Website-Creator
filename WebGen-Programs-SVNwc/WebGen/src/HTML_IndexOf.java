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
		// TODO: Write code here for generating the individual components
		// indexOf pages
	}
	
	public static String generateLinks(String path) {
		File f = new File(path);
		File[] files = f.listFiles();
		
		StringBuilder links = new StringBuilder();
		
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
			links.append(entry);
		}
		String s = links.toString();
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
