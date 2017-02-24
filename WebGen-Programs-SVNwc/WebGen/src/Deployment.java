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
*  File:         Deployment.java                                            *
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
* File: Deployment.java                                            		    *
*                                                                           *
* Purpose:                                                                  *
*   Contain the Deployment object formatting								*
* Functionality:      														*     
* 	 1) hold Deployment data												*
*    2) hold arrayList of sub flight objects								*
*                                                                           *
*****************************************************************************/
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author 	Davis Catherman
 * 			Christopher Newport University, class of 2018
 * 			DavisCatherman.com
 * 
 * date:	Fall 2016
 */
public class Deployment {

	private int depNum;
	private ArrayList<Flight> flights;
	private ArrayList<String> allSubDirs;
	private int flightCount;

	/**
	 * @param _depNum
	 *            deployment number of the instance
	 */
	public Deployment(int _depNum) {
		this.depNum = _depNum;
		this.flights = new ArrayList<Flight>();
		this.allSubDirs = new ArrayList<String>();
		this.flightCount = 0;
	}

	/**
	 * @param _flight
	 *            adds the Flight to the ArrayList
	 */
	public void addFlight(Flight _flight) {
		this.flights.add(_flight);
	}

	/**
	 * @return ArrayList of Flight objects
	 */
	public ArrayList<Flight> getFlights() {
		return flights;
	}

	/**
	 * @return deployment number object was set to
	 */
	public int getDepNum() {
		return depNum;
	}

	/**
	 * @param subDir
	 *            appends sub dir to arrayList of all sub dirs for deployment,
	 *            path is stripped before adding
	 */
	public void addToAllSubDirs(String subDir) {
		this.allSubDirs.add(subDir);
	}

	/**
	 * @return all the sub dirs of the deployment, note this does not include
	 *         path
	 */
	public ArrayList<String> getAllSubDirs() {
		this.allSubDirs.sort(String.CASE_INSENSITIVE_ORDER);
		return allSubDirs;
	}
	
	public void sortFlights() {
		ArrayList<StringData> flts = new ArrayList<StringData>();
		for (Flight f : flights) {
			StringData<?> temp = new StringData();
			temp.flight = f;
			temp.fVeh = f.getfVeh();
			temp.fNum = f.getfNum();
			if(temp.fVeh != null && !temp.fNum.equals("-1"))
				flts.add(temp);
		}

		flts.sort(null);
		flights.clear();
		Iterator<StringData> it = flts.iterator();
		while (it.hasNext()) {
			StringData<?> temp = it.next();
			flights.add(temp.flight);
		}
	}
	
	public void incFlightCount() {
		this.flightCount++;
	}
	
	public int getFlightCount() {
		return this.flightCount;
	}
}

class StringData<T extends Comparable<T>> implements Comparable<StringData<T>> {
	protected Flight flight;
	protected String fVeh;
	protected String fNum;

	// debugging method
	public void print() {
		System.out.println(flight + " " + fVeh + " " + fNum);
	}

	@Override
	public int compareTo(StringData<T> sd) {
		if (fVeh.compareTo(sd.fVeh) < 0) {
			return -1;
		} else if (fVeh.compareTo(sd.fVeh) > 0) {
			return 1;
		}
		return fNum.compareTo(sd.fNum);
		//String fNum1 = "##########".substring(fNum.length()) + fNum;
		//String fNum2 = "##########".substring(sd.fNum.length()) + sd.fNum;
		//return (fNum1.compareTo(fNum2));
	}
}
