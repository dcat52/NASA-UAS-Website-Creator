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
*  File:         Flight.java    	                                        *
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
* File: Flight.java                                            		 	    *
*                                                                           *
* Purpose:                                                                  *
*   Contain the Flight object formatting									*
* Functionality:      														*     
* 	 1) hold Flight data													*
*    2) parses Flight data from deployment directory						*
*                                                                           *
*****************************************************************************/
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * @author Davis Catherman Christopher Newport University, class of 2018
 *         DavisCatherman.com
 * 
 *         date: Fall 2016
 */
public class Flight extends Deployment {

	protected String fNum;
	protected String fDir;
	protected ArrayList<String> fSubDirs;
	protected String fVeh;
	protected String fLoc;
	protected String fDate;
	protected String sysVerification;
	protected String imageTooltip;

	/**
	 * instantiates a new flight object with a deployment number and flight dir
	 * path, data then gathered from path
	 * 
	 * @param _depNum
	 *            deploy num the flight is a part of
	 * @param _flightDir
	 *            path of flight dir, most of data based on this info
	 */
	public Flight(int _depNum, String _flightDir) {
		super(_depNum);
		this.fSubDirs = new ArrayList<String>();
		this.fDir = _flightDir;
		this.fNum = "-1";
		this.sysVerification = "unknown";

		String[] splitDir = this.fDir.split("[ \\\\]");
		for (String piece : splitDir) {
			parseDate(piece);
			parseLoc(piece);
			parseVeh(piece);
			parseNum(piece);
		}

		parseSubDirs();

		this.imageTooltip = "SystemVerification.txt\n";
		parseSysVerification();

	}

	/**
	 * parses the flight date from dir path
	 * 
	 * @param piece
	 *            portion of directory path
	 */
	private void parseDate(String piece) {
		if (piece.length() >= 8 && piece.length() <= 10 && piece.contains("-") && piece.contains("20")) {
			String year = piece.substring(0, piece.indexOf("-"));
			piece = piece.substring(year.length() + 1);
			String month = piece.substring(0, piece.indexOf("-"));
			piece = piece.substring(month.length() + 1);
			String day = piece;
			if (month.length() == 1)
				month = "0" + month;
			if (day.length() == 1)
				day = "0" + day;
			piece = month + "/" + day + "/" + year;
			this.fDate = piece;
		}
	}

	/**
	 * parses the flight location from dir path
	 * 
	 * @param piece
	 *            portion of directory path
	 */
	private void parseLoc(String piece) {
		if (piece.equals("Aberdeen") || piece.equals("Pungo") || piece.equals("Atterbury"))
			this.fLoc = piece;
		if (piece.equals("AP") || piece.equals("APHill"))
			this.fLoc = "AP-Hill";
		if (piece.equals("OliverFarms"))
			this.fLoc = "Oliver-Farms";
	}

	/**
	 * parses the flight vehicle from dir path
	 * 
	 * @param piece
	 *            portion of directory path
	 */
	private void parseVeh(String piece) {
		String temp = piece.substring(0, 1);
		// had to add special cases for non-typical planes flown
		if (((temp.equals("R") || temp.equals("P") || (temp.equals("C") && !piece.equals("C:")))
				&& (piece.length() <= 3 || piece.contains("&")))
				|| (temp.equals("N") && (piece.length() <= 6 || piece.contains("&"))) || piece.contains("Cub")
				|| piece.contains("UltraStick")) {
			this.fVeh = piece;
		}
	}

	/**
	 * parses the flight num from dir path
	 * 
	 * @param piece
	 *            portion of directory path
	 */
	private void parseNum(String piece) {
		if (piece.substring(0, 1).equals("F") && piece.length() <= 5) {
			try {
				this.fNum = piece.substring(1);
			} catch (Exception e) {
				this.fNum = "-1";
			}
		}
	}

	/**
	 * gets the immediate sub dirs of the flight
	 */
	private void parseSubDirs() {
		File directory = new File(fDir);

		// get all the directories from a directory
		File[] fList = directory.listFiles();
		for (File file : fList) {
			if (file.isDirectory()) {
				this.fSubDirs.add(file.toString());
			}
		}
	}

	/**
	 * gets the sys verification status based on a .txt file in the flight dir
	 * 
	 * @return
	 */
	private int parseSysVerification() {
		File directory = new File(fDir);

		// get all the directories from a directory
		File[] fList = directory.listFiles();

		boolean fileExists = false;
		String filePath = "";
		for (File file : fList) {
			if (file.isFile() && file.toString().contains("SystemVerification.txt")) {
				filePath = file.toString();
				fileExists = true;
			}
		}

		if (!fileExists) {
			imageTooltip += "File does not exist...";
			sysVerification = imageTooltip;
			return 1;
		}

		BufferedReader Buff;
		try {
			Buff = new BufferedReader(new FileReader(filePath));
			String line;
			while ((line = Buff.readLine()) != null) {
				imageTooltip += line + "\n";
			}

			// use the second line of the tooltip for data verification info
			sysVerification = imageTooltip.split("\n")[1];

			Buff.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * @return flight Number
	 */
	public String getfNum() {
		return fNum;
	}

	/**
	 * @return main flight dir
	 */
	public String getfDir() {
		return fDir;
	}

	/**
	 * @return all the sub dirs (full path) of the flight dir
	 */
	public ArrayList<String> getfSubDirs() {
		return fSubDirs;
	}

	/**
	 * @return all the sub dirs (not full path) of the flight dir
	 */
	public ArrayList<String> getfNoPathSubDirs() {
		ArrayList<String> temp = new ArrayList<String>();
		for (String dir : fSubDirs) {
			temp.add(dir.substring(dir.lastIndexOf("\\") + 1));
		}
		return temp;
	}

	/**
	 * @return flight Vehicle
	 */
	public String getfVeh() {
		return fVeh;
	}

	/**
	 * @return flight location
	 */
	public String getfLoc() {
		return fLoc;
	}

	/**
	 * @return flight date
	 */
	public String getfDate() {
		return fDate;
	}
}
