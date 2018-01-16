package snykeclipseplugin.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.IProject;

/** <b>Warning</b> : 
As explained in <a href="http://wiki.eclipse.org/Eclipse4/RCP/FAQ#Why_aren.27t_my_handler_fields_being_re-injected.3F">this wiki page</a>, it is not recommended to define @Inject fields in a handler. <br/><br/>
<b>Inject the values in the @Execute methods</b>
*/
public class HelloWorldHandler {

	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell s) {
		SnykAPI snyk = new SnykAPI("3b5434ee-eb0c-4535-a84e-b0573f03df70"); // dev auth token
		String result = "Snyk test results for workspace projects:\n\n";
		// iterate over projects in workspace
		for (IProject project: ResourcesPlugin.getWorkspace().getRoot().getProjects()) {
			// fetch path to main pom.xml file of project
			final String pomXmlPath = project.getFile("pom.xml").getFullPath().toOSString();
			// run via API
			int projectVulns = 0;
			try {
				projectVulns = snyk.GetVulnCountFromPOMFile(pomXmlPath);
			} catch (Exception e) {
				System.out.println("Error on API call: " + e);
			}
			
			result += pomXmlPath + ": " + projectVulns + "\n";
		}

		MessageDialog.openInformation(s, "Snyk", result);
	}
}
