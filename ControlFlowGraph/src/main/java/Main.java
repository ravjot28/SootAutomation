import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.io.FileUtils;

public class Main {

	public static void main(String[] args) throws IOException,
			InterruptedException {
		if (args != null && args.length == 1) {
			String projectPath = args[0];
			String rtPath = args[1]; // /usr/lib/jvm/java-7-openjdk-amd64/jre/lib/rt.jar
			String sootDestPath = args[2]; // /home/ravjot/Desktop/temp/
			String mainJar = args[3];  //hadoop-common
			Main main = new Main();
			List<String> commitList = main.getCommitList(projectPath);

			for (String commitId : commitList) {
				main.checkoutCode(commitId, projectPath);
				MavenExecution mavenExecution = new MavenExecution();
				mavenExecution.execute();
				main.runSoot(commitId, rtPath, sootDestPath, mainJar);
				break;
			}
		} else {
			System.out.println("Not enough arguments");
		}
	}

	public List<String> getCommitList(String projectPath) throws IOException,
			InterruptedException {
		List<String> list = null;

		String cmd[] = { "/bin/sh", "-c",
				"cd " + projectPath + "; git rev-list --all --remotes" };

		Process p = Runtime.getRuntime().exec(cmd);

		BufferedReader b = new BufferedReader(new InputStreamReader(
				p.getInputStream()));
		BufferedReader b1 = new BufferedReader(new InputStreamReader(
				p.getErrorStream()));
		String line = "";
		list = new ArrayList<String>();
		while ((line = b.readLine()) != null) {
			list.add(line);
		}

		line = "";

		while ((line = b1.readLine()) != null) {
			System.out.println(line);
		}
		return list;

	}

	public List<String> checkoutCode(String commitId, String projectPath)
			throws IOException, InterruptedException {
		List<String> list = null;

		String cmd[] = { "/bin/sh", "-c",
				"cd " + projectPath + "; git checkout " + commitId };

		Process p = Runtime.getRuntime().exec(cmd);

		BufferedReader b = new BufferedReader(new InputStreamReader(
				p.getInputStream()));
		BufferedReader b1 = new BufferedReader(new InputStreamReader(
				p.getErrorStream()));
		String line = "";
		list = new ArrayList<String>();
		while ((line = b.readLine()) != null) {
			list.add(line);
		}

		line = "";

		while ((line = b1.readLine()) != null) {
			System.out.println(line);
		}
		return list;

	}

	public void logSootLogs(String line) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(
				"sootLogs.log"), true));
		bw.append(line);
		bw.newLine();
		bw.close();
	}

	public void runSoot(String commitId, String rtPath, String sootDestPath,
			String mainJarSearch) throws IOException, InterruptedException {

		String jars = new GenerateJarPath().getCommand();
		File f1 = new File("/home/ravjot/Desktop/temp/ttt");
		if (!f1.exists())
			f1.mkdir();
		String mainJar = "";
		StringTokenizer token = new StringTokenizer(jars, ":");
		while (token.hasMoreElements()) {
			String jar = token.nextToken();
			if (jar.contains(mainJarSearch) && !jar.contains("test")) {
				mainJar = jar;
			}
		}

		String cmd[] = {
				"/bin/sh",
				"-c",
				"cd " + sootDestPath + "; java -cp " + jars + " soot.Main -cp "
						+ rtPath + ":. " + "--pp -process-dir ./" + mainJar
						+ " -f j -d ttt" };

		System.out.println(cmd);

		Process p = Runtime.getRuntime().exec(cmd);

		BufferedReader b = new BufferedReader(new InputStreamReader(
				p.getInputStream()));
		BufferedReader b1 = new BufferedReader(new InputStreamReader(
				p.getErrorStream()));
		String line = "";
		while ((line = b.readLine()) != null) {
			System.out.println(line);
			logSootLogs(line);
		}

		line = "";

		while ((line = b1.readLine()) != null) {
			System.out.println(line);
		}
		File f = new File("/home/ravjot/Desktop/" + commitId);
		if (!f.exists())
			f.mkdir();
		FileUtils.copyDirectory(new File("/home/ravjot/Desktop/temp/ttt"), f);
		try {
			FileUtils.cleanDirectory(new File("/home/ravjot/Desktop/temp/"));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
