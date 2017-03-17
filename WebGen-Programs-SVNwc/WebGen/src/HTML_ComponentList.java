import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class HTML_ComponentList {
	// TODO: write code here for generating the components list page
	
	public static void generateComponentsTable(Component c) {
		
		String cFileName = "component_ " + c.name + ".html";
		String cDispName = c.name.replaceAll("_", " ");
		String cLink = Vars.DATA_LINK2;
		cLink = cLink.replace("INSERT_LINKED_FILE_HERE", cFileName);
		cLink = cLink.replace("INSERT_DISPLAY_TEXT_HERE", cDispName);
		String cCell = Vars.DATA_CELL.replace("INSERT_HERE", cLink);
		String cRow = Vars.DATA_ROW.replace("INSERT_HERE", cCell);
		
		cRow += "\n<!--INSERT_COMPONENTS_TABLE_HERE-->";
		
		Vars.CL_template = Vars.CL_template.replace("<!--INSERT_COMPONENTS_TABLE_HERE-->", cRow);
		Vars.file = Vars.CL_template;
	}
	
	/**
	 * write the file to the HTML Path and print the message
	 */
	public static void writeFile() {
		PrintWriter writer;
		try {
			String fileName = String.format(Vars.HTML_PATH + "\\Components.html");
			writer = new PrintWriter(fileName, "UTF-8");
			writer.print(Vars.file);
			writer.close();
			System.out.println(Vars.HTML_PATH + "Components.html Written");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
