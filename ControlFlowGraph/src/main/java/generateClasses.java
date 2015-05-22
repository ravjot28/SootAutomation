import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class generateClasses {
	
	public static void main(String[] args) throws IOException {
		new generateClasses().arrangeHadoopFiles();
	}

	public List<String> getClasses() {
		List<String> command =null;
		return command;
	}

	public String getPath() {
		String command = "";
		try {
			arrangeSootFiles();
			arrangeHadoopFiles();
			copyAdditionalJars();
			List<File> files = new ArrayList<File>();
			getClassFiles("/home/ravjot/Desktop/temp/", files);

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
		getClassFiles("/home/ravjot/Desktop/hadoop/hadoop", files);

		for (File file : files) {
			copyFile(file);
		}
	}

	public void arrangeSootFiles() throws IOException {
		List<File> files = new ArrayList<File>();
		getClassFiles("/home/ravjot/Desktop/soot", files);

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

	public void getClassFiles(String path, List<File> files) {
		File directory = new File(path);

		File[] fList = directory.listFiles();
		for (File file : fList) {
			if (file.isFile() && file.getAbsolutePath().endsWith(".class")) {
				files.add(file.getAbsoluteFile());
			} else if (file.isDirectory()) {
				getClassFiles(file.getAbsolutePath(), files);
			}
		}
	}

}
