import java.io.File;
import java.util.Collections;

import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.Invoker;

public class MavenExecution {

	public void execute() {
		InvocationRequest request = new DefaultInvocationRequest();
		request.setPomFile(new File(
				"/home/ravjot/Desktop/hadoop/hadoop/pom.xml"));
		request.setGoals(Collections
				.singletonList("clean package -DskipTests=true"));

		Invoker invoker = new DefaultInvoker();
		invoker.setMavenHome(new File("/usr/share/maven"));

		try {
			invoker.execute(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new MavenExecution().execute();
	}

}
