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
*  File:         Config.java                                  				*
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
* File: Config.java                                  		    			*
*                                                                           *
* Purpose:                                                                  *
*   Provide single method in a single file for all of the settings			*
* Functionality:      														*     
* 	 1) Use Vars.html to get some default options							*
* 	 2) Allow spot for user to change settings								*
* 	 3) Store changed settings in Vars.html									*
*                                                                           *
*****************************************************************************/

/**
 * @author Davis Catherman Christopher Newport University, class of 2018
 *         DavisCatherman.com
 * 
 *         date: Fall 2016
 */
public class Config {

	/**
	 * Method allows user to change settings in single location could add prompt
	 * feature to here and satisfy entire program
	 */
	public static void settingsManager() {
		String EdgeShare = "Z:\\EDGESite\\HTML\\"; // Edit this ONLY if
													// Edgeshare is mapped to a
													// different Drive Letter
		String localCopy = "C:\\HTML\\";

		// -----------------------------------------------------------------------------------------
		// *****************************************************************************************
		// -----------------------------------------------------------------------------------------
		// --------------------------------- EDIT WITHIN HERE ONLY ---------------------------------
		// -----------------------------------------------------------------------------------------

		// process only these deployment numbers and between
		int depMin = 0;
		int depMax = 67;

		// path to the HTML
		// change between 'localCopy' and 'EdgeShare'
		String htmlPath = localCopy;

		// list of paths for vehicle data from Z: Drive (EdgeShare)
		String[] paths = { "Z:\\Veh_R1", "Z:\\Veh_R2", "Z:\\Veh_R3", "Z:\\Veh_R4", "Z:\\Veh_R5", "Z:\\Veh_R6",
				"Z:\\Veh_Other" };

		// -----------------------------------------------------------------------------------------
		// *****************************************************************************************
		// -----------------------------------------------------------------------------------------

		// Below stores variables into Vars.html
		// Do NOT edit below this line
		Vars.depMin = depMin;
		Vars.depMax = depMax;
		Vars.HTML_PATH = htmlPath;
		Vars.DRIVE_LETTER = htmlPath.substring(0, 2);
		Vars.paths = paths;

	}
}
