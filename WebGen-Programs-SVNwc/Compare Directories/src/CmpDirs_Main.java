/*****************************************************************************
* WebGen Suite - Compare Directories Tool                                   *
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
*  File:         CmpDirs_Main.java		                           		    *
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
* File: CmpDirs_Main.java                       	                      	*
*                                                                           *
* Purpose:                                                                  *
*   Compare two directory structures and report what is missing from each	*
* Functionality:                                                            *
*    1) uses path1 and path2 as roots of directories to be compared			*
*    2) 
*                                                                           *
*****************************************************************************/

import java.io.File;
import java.util.ArrayList;

/**
 * @author 	Davis Catherman
 * 			Christopher Newport University, class of 2018
 * 			DavisCatherman.com
 * 
 * @date	Fall 2016
 */
public class CmpDirs_Main {
	public static void main(String[] args) {
		
		System.out.println("starting:");
		String path1 = "Z:\\R1";
		String path2 = "C:\\R1";
		String folderName1 = path1.substring(path1.lastIndexOf("\\")+1);
		String folderName2 = path1.substring(path1.lastIndexOf("\\")+1);
		
		ArrayList<String> dirs1 = new ArrayList<String>();
		ArrayList<String> dirs2 = new ArrayList<String>();
		
		File dir1 = new File(path1);
		File dir2 = new File(path2);

		// get all the directories from a directory
//		File[] fList1 = dir1.listFiles();
//		for (File file : fList1) {
//			if (file.isDirectory()) {
//				dirs1.add(file.toString().substring(file.toString().lastIndexOf("\\")));
//			}
//		}
		getDirs(path1, dirs1, folderName1);

		getDirs(path2, dirs2, folderName2);
		// get all the directories from a directory
//		File[] fList2 = dir2.listFiles();
//		for (File file : fList2) {
//			if (file.isDirectory()) {
//				dirs2.add(file.toString().substring(file.toString().lastIndexOf("\\")));
//			}
//		}
		
		System.out.printf("Z: has %d dirs.\n", dirs1.size());
		System.out.printf("C: has %d dirs.\n", dirs2.size());
		
		int count1 = 0;
		int count2 = 0;
		
		System.out.println("\nZ: has these but C: does not.");
		for(String dir : dirs1) {
			if(!dirs2.contains(dir)) {
				System.out.println(dir);
				count1++;
			}
		}
		
		System.out.println("\nC: has these but Z: does not.");
		for(String dir : dirs2) {
			if(!dirs1.contains(dir)) {
				System.out.println(dir);
				count2++;
			}
		}

		System.out.printf("\nZ: has %d dir(s) or file(s) that C: does not have.\n", count1);
		System.out.printf("C: has %d dir(s) or file(s) that Z: does not have.\n", count2);
	}
	
	
	/**
	 * This method recursively begins at the initial directory name passed and
	 * navigates through sub-directories, returning directories with complete
	 * paths in the array list
	 * 
	 * @param directoryName
	 *            ArrayList of filenames (passed as arg to work recursively)
	 * @param dirs
	 *            String of directory path used to recursively go through
	 *            directories
	 * @param indexOf
	 *            String of root folder name
	 */
	public static void getDirs(String directoryName, ArrayList<String> dirs, String indexOf) {
		
		// Enables recursion Vs. just compare root level
		Boolean recursionEnabled = true;
		
		// Enables compare full path Vs. just the page name
		// Disable this if dir structures are different, however it makes program less accurate
		Boolean cmpFullPath = true;
		
		File directory = new File(directoryName);

		// get all the directories from a directory
		File[] fList = directory.listFiles();
		
		// for every file/dir found
		for (File file : fList) {
			
			if(cmpFullPath)
				// if full path, do to subFolder#
				dirs.add(file.toString().substring(file.toString().indexOf(indexOf)));
			else
				// else, compare to the last sub file
				dirs.add(file.toString().substring(file.toString().lastIndexOf("\\")));
			
			// if a folder and recursion enabled 
			if (file.isDirectory() && recursionEnabled) {
				// recursively call this method
				getDirs(file.getAbsolutePath(), dirs, indexOf);
			}
		}
	}

}
