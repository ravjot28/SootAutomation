import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class GenerateJarPath {

	public String getCommand() {
		String command = new GenerateJarPath().getPath();
		System.out.println(command);
		// System.exit(1);
		return command;
	}

	public String getPath() {
		String command = "";
		try {
			arrangeSootFiles();
			arrangeHadoopFiles();
			copyAdditionalJars();
			List<File> files = new ArrayList<File>();
			getJarFiles("/home/ravjot/Desktop/temp/", files,false);

			for (File file : files) {
				command += file.getName() + ":";
			}
			command = command.substring(0, command.length() - 1);
			System.out.println(command);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return command;
	}

	public void arrangeHadoopFiles() throws IOException {
		List<File> files = new ArrayList<File>();
		getJarFiles("/home/ravjot/Desktop/hadoop/hadoop", files,true);

		for (File file : files) {
			copyFile(file);
		}
	}

	public void arrangeSootFiles() throws IOException {
		List<File> files = new ArrayList<File>();
		getJarFiles("/home/ravjot/Desktop/soot", files,false);

		for (File file : files) {
			copyFile(file);
		}
	}

	public void copyFile(File f) throws IOException {
		FileUtils.copyFile(f,
				new File("/home/ravjot/Desktop/temp/" + f.getName()));
	}

	public void copyAdditionalJars() throws IOException {
		FileUtils.copyDirectory(
				new File("/home/ravjot/Desktop/additionalJars"), new File(
						"/home/ravjot/Desktop/temp/"));
	}

	public void getJarFiles(String path, List<File> files, boolean httpclient) {
		File directory = new File(path);
		if (httpclient) {
			File[] fList = directory.listFiles();
			for (File file : fList) {
				if (file.isFile() && file.getAbsolutePath().endsWith(".jar") && !file.getAbsolutePath().contains("httpclient-4.2.5.jar")) {
					files.add(file.getAbsoluteFile());
				} else if (file.isDirectory()) {
					getJarFiles(file.getAbsolutePath(), files,httpclient);
				}
			}
		} else {
			File[] fList = directory.listFiles();
			for (File file : fList) {
				if (file.isFile() && file.getAbsolutePath().endsWith(".jar")) {
					files.add(file.getAbsoluteFile());
				} else if (file.isDirectory()) {
					getJarFiles(file.getAbsolutePath(), files,httpclient);
				}
			}

		}
	}

}
