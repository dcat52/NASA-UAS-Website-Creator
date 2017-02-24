/*****************************************************************************
* WebGen Suite - Delete Empty Directories Tool								*
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
*  File:         DelEmptyDirs_Main.java                                     *
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
* File: DelEmptyDirs_Main.java												*
*                                                                           *
* CAUTION:																	*
* 	This program is inherently dangerous, test on copy of data before use	*
* Purpose:                                                                  *
*   Delete sub directories that contain no folders or files					*
* Functionality:      														*     
* 	 1) uses root path provided to start from								*
* 	 2) gets list of files/directories in a folder							*
* 	 3) check if folder/file is a file, set to not delete if so				*
* 	 4) check if it is a folder, recursively navigate if so					*
* 	 5) delete dir that does not match these cases							*
*                                                                           *
*****************************************************************************/
import java.io.File;

/**
 * @author 	Davis Catherman
 * 			Christopher Newport University, class of 2018
 * 			DavisCatherman.com
 * 
 * @date	Fall 2016
 */
public class DelEmptyDirs_Main {
	public static void main(String[] args) {
		String path = "C:\\TestDir";
		File root = new File(path);
		deleteDirs(root);
	}

	/**
	 * recursively goes through and deletes empty sub dirs
	 * 
	 * @param dir
	 *            to start from for initial call then recursively used
	 * @return if it contains sub directories
	 */
	public static boolean deleteDirs(File dir) {

		// get all the directories from a directory
		boolean containsFile = false;
		File[] fList = dir.listFiles();
		
		// for folder/file in dir
		for (File file : fList) {
			
			// check if contains file, change boolean if so
			if (file.isFile()) {
				containsFile = true;
			}
			
			// check if contains dir, recusively navigate if so, change var based on result
			if (file.isDirectory()) {
				containsFile = deleteDirs(file);
			}
		}
		
		// give print out for the folder
		System.out.println("Contains File: " + containsFile + "\t Path: " + dir.getAbsolutePath());
		
		// delete the dir if no file
		if(containsFile == false)
			dir.delete();
		
		// return whether it contains a file
		return containsFile;
	}
}
