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
*  File:         Vars.java		                                            *
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
* File: Vars.java                                             		        *
*                                                                           *
* Purpose:                                                                  *
*   Global Variables used throughout the program are stored here	        *
* Functionality:                                                            *
*    1) Hold template file strings											*
*    2) Hold specific table formatting strings								*
*    3) Hold file data types for icons										*
*                                                                           *
*****************************************************************************/
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author 	Davis Catherman
 * 			Christopher Newport University, class of 2018
 * 			DavisCatherman.com
 * 
 * date:	Fall 2016
 */
public final class Vars {
	private Vars() {
		// restrict instantiation
	}

	// Settings configs
	public static String HTML_PATH = "";
	public static String DRIVE_LETTER = "";
	public static int depMin = 0;
	public static int depMax = 0;
	public static String[] paths = {};

	// template holders
	public static String FT_template = "";
	public static String IO_template = "";
	public static String FL_template = "";

	// file holder for writing
	public static String file = "";

	// HTML table stuff
	public static final String TABLE_NAME = "<h2 class=\"content\">INSERT_HERE</h2>\n";
	public static final String TABLE_GENERIC = "&nbsp;<br />\n"
			+ "<table cellspacing=\"0\" cellpadding=\"0\" class=\"test_table\">\n";
	public static final String COLUMN_WIDTH = "\t<col width=\"50\" />\n";
	public static final String TBL_ROW_1 = "\t<tr>\n";
	public static final String COLUMN_NAME = "\t\t<th width=\"INSERT_WIDTH_HERE\" rowspan=\"2\"><p>INSERT_NAME_HERE<br></p></th>\n";
	public static final String TBL_ROW_2 = "\t</tr>\n" + "\t<tr>\n" + "\t</tr>\n";
	public static final String DATA_ROW = "\t<tr>\n" + "INSERT_HERE" + "\t</tr>\n";
	public static final String DATA_CELL = "\t\t<td>INSERT_HERE</td>\n";
	public static final String DATA_LINK = "<a href=\"INSERT_HERE\">X</a>";
	public static final String END_TABLE = "</table>";

	public static final String DEP_ROW = "\t<tr>\n" + "\t\t<td><a href=\"DEP_INSERT_DEP_NUM_HERE_misc.html\">INSERT_DEP_NUM_HERE</a></td>\n"
			+ "\t\t<td>INSERT_VEHICLE_HERE</td>\n" + "\t\t<td>INSERT_FLIGHT_NUMS_HERE</td>\n"
			+ "\t\t<td>INSERT_DEP_LINK_HERE</td>\n" + "\t</tr>\n";
	public static final String DEP_LINK = "<a href=\"DEP_INSERT_DEP_NUM_HERE_table.html\">X</a>";
	public static final String GENERIC_LINK = "<a href=\"INSERT_LINKED_LOCATION_HERE\">INSERT_DISPLAY_TEXT_HERE</a>";
	public static final String TAB = "&nbsp;";
	public static final String NEW_LINE = "&#10;";
	
	// file types for index of
	public static final ArrayList<String> FILE_TYPE = new ArrayList<String>(
			Arrays.asList("bmp", "gif", "jpg", "png", "avi", "mov", "mpg", "wmv", "mp3", "wav", "wma", "aac", "real",
					"vbs", "css", "js", "xml", "html", "rtf", "pdf", "reg", "ini"));

	// this is for file types that have various extensions
	public static final ArrayList<String> ALT_TYPE = new ArrayList<String>(
			Arrays.asList("doc", "word", "ppt", "powerpoint", "xls", "excel", "acc", "access", "txt", "text"));
}
