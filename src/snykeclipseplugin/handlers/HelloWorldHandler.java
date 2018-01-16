package snykeclipseplugin.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.IProject;

public class HelloWorldHandler {

	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell s) {
		SnykAPI snyk = new SnykAPI("3b5434ee-eb0c-4535-a84e-b0573f03df70"); // dev auth token
		String result = "Snyk test results for workspace projects:\n\n";

		for (IProject project: ResourcesPlugin.getWorkspace().getRoot().getProjects()) {
			final String pomXmlPath = project.getFile("pom.xml").getRawLocation().toString();
			int projectVulns = 0;
			
			try {
				System.out.println("Checking vulns for: " + pomXmlPath);
				projectVulns = snyk.GetVulnCountFromPOMFile(pomXmlPath);
			} catch (Exception e) {
				MessageDialog.openInformation(s, "Snyk", "Error on API call: " + e);
				System.out.println("Error on API call: " + e);
			}
			
			result += pomXmlPath + ": " + projectVulns + "\n";
		}

		MessageDialog.openInformation(s, "Snyk", result);
	}
}
