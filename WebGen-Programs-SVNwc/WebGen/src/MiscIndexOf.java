import java.io.File;
import java.util.ArrayList;

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
*  File:         MiscIndexOf.java	         	                            *
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
 * File: MiscIndexOf.java * * Purpose: * Hold the objects for the miscellaneous
 * index of pages * Functionality: * 1) *
 *****************************************************************************/
public class MiscIndexOf<T extends Comparable<T>> implements Comparable<MiscIndexOf<T>> {
	protected String path;
	protected String veh;
	protected String dataType;
	protected String pageTitle;
	protected String pageName;

	public MiscIndexOf(File d) {
		String f = d.getAbsolutePath();
		this.path = f;
		
		String[] split = f.split("\\\\|\\_");

		parseVeh(split);
		parseDataType(split);
		constructPgTitle();
		constructPgName();

	}

	private void constructPgName() {
		String temp = this.pageTitle.replaceFirst(" ", "_");
		this.pageName = temp + ".html";
	}

	private void constructPgTitle() {
		this.pageTitle = this.veh + " " + this.dataType;
	}

	private void parseDataType(String[] split) {
		this.dataType = split[split.length-1];
	}

	private void parseVeh(String[] split) {
		for (String v : split) {
			String tv = Main.parseVeh(v);
			if (tv != null)
				this.veh = tv;
		}
		if(this.veh == null)
			this.veh = "nullVeh";
	}

	public static void genMiscIndexOfPages() {
			
		String[] paths = Vars.paths;
		ArrayList<File> dirs = new ArrayList<File>();
		ArrayList<MiscIndexOf> mioList = new ArrayList<MiscIndexOf>();

		// note that an index of page is being generated for every p, however
		// only R2-R5 will be accessible.

		for (String p : paths) {
			Main.getDirs(p, dirs, false);
		}

		for (File d : dirs) {
			MiscIndexOf temp = new MiscIndexOf(d);
			mioList.add(temp);
		}
		
		// Generate Dropdown Menus for Template Files
		mioList.sort(null);
		
		ArrayList<MiscIndexOf[]> sepByVeh = new ArrayList<MiscIndexOf[]>();
		ArrayList<MiscIndexOf> temp = new ArrayList<MiscIndexOf>();
		
		for(int i = 0; i < mioList.size(); i++) {
			if(temp.size() == 0) {
				temp.add(mioList.get(i));
				i++;
				if(i == mioList.size())
					break;
			}
			if(mioList.get(i).veh.equals(temp.get(0).veh))
				temp.add(mioList.get(i));
			else {
				
				MiscIndexOf[] t = temp.toArray(new MiscIndexOf[temp.size()]);
				sepByVeh.add(t);
				temp.clear();
			}
		}
		
		// generate the HTML Code
		HTML_IndexOf.buildDropDownMenus(sepByVeh);
		

		// generate the Mio Pages
		for(MiscIndexOf mio : mioList) {
			HTML_IndexOf.buildPageForVehInfo(mio);
		}
	}

	@Override
	public int compareTo(MiscIndexOf<T> mio) {
		int i = this.veh.compareTo(mio.veh);
		if(i == 0)
			i = this.pageTitle.compareTo(mio.pageTitle);
		return i;
	}
}
