/*****************************************************************************
* WebGen Suite - Link Checker Tool											*
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
*  File:         LinkChecker_Main.java		                                *
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
* File: LinkChecker_Main.java                                             	*
*                                                                           *
* Purpose:                                                                  *
*   Report the status of every link on the website.							*
* Functionality:                                                            *
*    1) Open a browser Driver. Default is Chrome							*
*    2) Append the Main (root) page to List<Links>							*
*    3) Navigate to that page												*
*    4) Check the status of that page										*
*    5) Find all new links on that page										*
*    6) Append them to the List												*
*    7) Repeats 3-6 as moves through the List								*
*    8) Prints the final status to output.txt								*
*                                                                           *
*****************************************************************************/

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * status enum for reporting the link status, either UNKNOWN, GOOD, or BAD
 */
enum status {
	UNKNOWN, GOOD, BAD
}

/**
 * Uses Selenium browser automation tool to work with pages
 * 
 * @author Davis Catherman Christopher Newport University, class of 2018
 *         DavisCatherman.com
 * 
 * @date Fall 2016
 */
public class LinkChecker_Main {
	private static WebDriver driver;
	private static String root = "file:///Z:/EDGESite/HTML/flight_list.html";
	private static List<LinkStatus> links = new ArrayList<LinkStatus>();

	public static void main(String[] args) throws IOException {
		// this is for Firefox - other code may be changed to make Firefox Work
		// System.setProperty("webdriver.gecko.driver","geckodriver.exe");
		// driver = new FirefoxDriver();

		// this is for Chrome
		System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
		driver = new ChromeDriver();

		driver.get("file:///Z:/EDGESite/HTML/flight_list.html");

		// Set timeout
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		// setup root link
		LinkStatus ls = new LinkStatus();
		ls.link = root;
		ls.status = status.UNKNOWN;
		ls.parent = "NULL";

		// add the root Link
		links.add(ls);

		// counter to keep track of current position in List
		int i = 0;
		while (i < links.size()) {
			try {
				// better option would be to use a connection pool, otherwise
				// needs sleep to not error out
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}

			// call method to do main work
			navigateLinks(i);

			// print out a basic status message
			System.out.println(i + 1 + " tested; Number of Links: " + links.size());

			// uncomment to iteratively print to the file at the given interval
			// if(i % 50.0 == 0)
			// printFile(i);

			i++;
		}

		// print final results to output file
		printFile(i);

		System.out.println("Done.");
		driver.close();
	}

	/**
	 * does main work for link info gathering 1) checks current link 2) gathers
	 * links on page 3) appends new unique links to List
	 * 
	 * @param i
	 *            the current index in the list to work from
	 */
	public static void navigateLinks(int i) {

		// check if i has been checked already - should not be
		if (links.get(i).status == status.UNKNOWN) {

			// navigate to the link
			driver.navigate().to(links.get(i).link);

			// set status to output of method
			links.get(i).status = getLinkStatus();

			// if link is Good, find potential links on that page
			if (links.get(i).status == status.GOOD) {
				List<WebElement> elements = driver.findElements(By.tagName("a"));

				// for each element (link of some sort)
				for (WebElement we : elements) {

					// check if link is an html link (for other sites, will need
					// to check if http instead)
					if (!(we.getAttribute("href") == null) && we.getAttribute("href").contains("html")) {

						// create new object to hold the link, parent, and
						// status
						LinkStatus ls = new LinkStatus();
						ls.link = we.getAttribute("href");

						// remove the # ending that appears for visited links
						if (ls.link.endsWith("#"))
							ls.link = ls.link.split("#")[0];

						// default the status to UNKNOWN
						ls.status = status.UNKNOWN;

						// get the parent URL (means link at minimum exists on
						// this page, could exist elsewhere)
						String currURL = driver.getCurrentUrl();

						// eliminate full path for parent, only keep the page
						// name
						ls.parent = currURL.substring(currURL.lastIndexOf("/") + 1);

						// check if this link already exists, uses equals()
						// method
						if (!links.contains(ls))
							links.add(ls);
					}
				}
			}
		}
	}

	/**
	 * checks the link status, using browsers default 404 page, returns result
	 * 
	 * @return status enum, either GOOD or BAD
	 */
	public static status getLinkStatus() {

		// check if page is browser's 404
		if (driver.getPageSource().contains("ERR_FILE_NOT_FOUND"))
			return status.BAD;

		return status.GOOD;
	}

	public static void printFile(int i) {

		// open file to write to
		try (FileWriter fw = new FileWriter("output.txt", true);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out = new PrintWriter(bw)) {

			// print out a divider line
			for (int j = 0; j < 3; j++)
				out.println("---------------------------------------------------");

			// print the link info (uses toString method)
			for (LinkStatus ls1 : links) {
				out.println(ls1.toString());
			}

		} catch (IOException e) {
			// exception handling left as an exercise for the reader
		}

	}
}

/**
 * object to keep link data together
 *
 */
class LinkStatus {
	/**
	 * link - the link being tested and status being reported about
	 */
	protected String link;
	/**
	 * status - status of the link (uses status enum)
	 */
	protected status status;
	/**
	 * parent - contains the name of the parent page
	 */
	protected String parent;

	/**
	 * create a uniform string for each LinkStatus object
	 * 
	 * @return string of link info
	 */
	public String toString() {
		return "Link: " + link.toString() + ", Parent: " + parent.toString() + ", Status: " + status.toString();
	}

	/**
	 * compare if link object already exists only compares the link itself
	 * 
	 * @return if two links are equal or not
	 */
	public boolean equals(Object o) {
		if (o instanceof LinkStatus) {
			LinkStatus ls = (LinkStatus) o;
			return this.link.equals(ls.link);
		}
		return false;
	}
}