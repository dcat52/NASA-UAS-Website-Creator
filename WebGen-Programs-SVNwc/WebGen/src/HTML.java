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
*  File:         HTML.java	         	                                    *
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
* File: HTML.java     	                                        		    *
*                                                                           *
* Purpose:                                                                  *
*   Provide general functions for HTML output code							*
* Functionality:      														*     
* 	 1) Reads in the template files											*
* 	 2) Puts current date in last edited field								*
*                                                                           *
*****************************************************************************/
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Davis Catherman Christopher Newport University, class of 2018
 *         DavisCatherman.com
 * 
 *         date: Fall 2016
 */
public class HTML {

	public static void main(String[] args) {
		readTemplates();

		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);

		String dateString = String.format("%d/%d/%d", month, day, year);
		Vars.FT_template = Vars.FT_template.replace("INSERT_DATE_HERE", dateString);
		Vars.IO_template = Vars.IO_template.replace("INSERT_DATE_HERE", dateString);
		Vars.FL_template = Vars.FL_template.replace("INSERT_DATE_HERE", dateString);
		Vars.CL_template = Vars.CL_template.replace("INSERT_DATE_HERE", dateString);
	}

	/**
	 * reads in the template files and puts them in public vars
	 */
	public static void readTemplates() {
		Vars.FT_template = readFile(Vars.HTML_PATH + "TEMPLATE_Flight-Table.html").trim();
		Vars.IO_template = readFile(Vars.HTML_PATH + "TEMPLATE_Index-Of.html").trim();
		Vars.FL_template = readFile(Vars.HTML_PATH + "TEMPLATE_Flight-List.html").trim();
		Vars.CL_template = readFile(Vars.HTML_PATH + "TEMPLATE_Components-List.html").trim();
	}

	/**
	 * helper method for reading the template files
	 * 
	 * @param path
	 *            path to template
	 * @return file contents
	 */
	private static String readFile(String path) {
		String temp = "";
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(path));
			String line;
			while ((line = br.readLine()) != null) {
				temp += line + "\n";
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return temp;
	}
}
