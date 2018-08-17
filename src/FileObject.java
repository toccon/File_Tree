import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class FileObject {

	// Instance variables used for testing
	private static boolean testingMode;
	private static boolean testFailed = false;

	// File structure of test file system
	private String[] files = { "folder1:0:", "folder2:3:file1.java:file2.txt:file3.java:", "folder2/file1.java:0:",
			"folder2/file2.txt:0:", "folder2/file3.java:0:", "folder3:3:folderA:folderB:folderC:",
			"folder3/folderA:2:folderB:folderD:", "folder3/folderB:1:file1.gif:",
			"folder3/folderC:2:folderE:file7.java:", "folder3/folderA/folderB:1:file1.gif:",
			"folder3/folderA/folderD:2:folderF:file2.java:", "folder3/folderB/file1.gif:0:",
			"folder3/folderC/folderE:2:file6.java:folderD:", "folder3/folderC/file7.java:0:",
			"folder3/folderA/folderB/file1.gif:0:", "folder3/folderA/folderD/folderF:3:file5.gif:file4.txt:file3.java:",
			"folder3/folderA/folderD/file2.java:0:", "folder3/folderC/folderE/file6.java:0:",
			"folder3/folderC/folderE/folderD:2:folderF:file2.java:", "folder3/folderA/folderD/folderF/file5.gif:0:",
			"folder3/folderA/folderD/folderF/file4.txt:0:", "folder3/folderA/folderD/folderF/file3.java:0:",
			"folder3/folderC/folderE/folderD/folderF:3:file3.java:file4.txt:file5.gif:",
			"folder3/folderC/folderE/folderD/file2.java:0:", "folder3/folderC/folderE/folderD/folderF/file3.java:0:",
			"folder3/folderC/folderE/folderD/folderF/file4.txt:0:",
			"folder3/folderC/folderE/folderD/folderF/file5.gif:0:" };

	private int numChildren;
	private String name;
	private int numFile;
	private ArrayList<FileObject> children;

	private File fileRef; // File object representing this FileObject
	public static int numFileObjects = 0; // Number of file objects in the file
											// system
	private static final int WAITING_INTERVAL = 1000; // Number of files to scan
														// before the program
														// prints
														// an indication that it
														// is still running

	public static void setTestingMode() {
		testingMode = true;
	}

	public static boolean testFailed() {
		return testFailed;
	}

	/* Find the position of name in the files array */
	private int findFile(String name) {
		int i = 0;
		for (i = 0; i < files.length; ++i) {
			int pos = 0;

			// Name of file object ends at character before the colon symbol
			while (files[i].charAt(pos) != ':')
				++pos;
			if (name.equals(files[i].substring(0, pos)))
				break;
		}
		if (i == files.length)
			testFailed = true;
		return i;

	}

	/**
	 * Create a FileObject for the file object with the given name
	 * 
	 * @param name:
	 *            name of the file or folder to be represented by this
	 *            FileObject
	 * @throws FileObjectException
	 *             when FileObject cannot be created
	 */
	public FileObject(String name) throws FileObjectException {
		if (testingMode) {
			numFile = findFile(name);
			// Get the attributes of this file object
			this.name = name;
			children = new ArrayList<FileObject>();
			numChildren = 0;
			int begin = 0, end = 0;
			int segment = 0;
			String file = files[numFile];
			while (begin < file.length()) {
				while (file.charAt(end) != ':')
					++end;
				if (segment == 1)
					numChildren = Integer.parseInt(file.substring(begin, end));
				else if (segment > 1)
					children.add(new FileObject(name + "/" + file.substring(begin, end)));
				++segment;
				begin = end + 1;
				end = begin + 1;
			}

		} else
			try {
				fileRef = new File(name);
				if (!fileRef.exists())
					throw new FileObjectException("File object " + name + " could not be created");
				++numFileObjects;
				if ((numFileObjects % WAITING_INTERVAL) == WAITING_INTERVAL - 1) {
					String text = SplitPanel.getPageText();
					text = text + ".";
					SplitPanel.setPageText(text);
				}
			} catch (NullPointerException e) {
				throw new FileObjectException("File object " + name + " could not be created");
			}
	}

	/**
	 * Create a FileObject for the given File
	 * 
	 * @param f:
	 *            File representing a file or folder
	 * @throws FileObjectException
	 *             when FileObject cannot be created
	 */
	public FileObject(File f) throws FileObjectException {
		try {
			fileRef = f;
			++numFileObjects;
			if ((numFileObjects % WAITING_INTERVAL) == WAITING_INTERVAL - 1) {
				String text = SplitPanel.getPageText();
				text = text + ".";
				SplitPanel.setPageText(text);
			}
		} catch (Exception e) {
			throw new FileObjectException("File object could not be created");
		}
	}

	/**
	 * 
	 * @return True if this FileObject is a file.
	 */
	public boolean isFile() {
		if (testingMode)
			return (numChildren == 0);
		return fileRef.isFile();
	}

	/**
	 * 
	 * @return True if this FileObject is a directory or folder
	 */
	public boolean isDirectory() {
		if (testingMode)
			return (numChildren > 0);
		return fileRef.isDirectory();
	}

	/**
	 * 
	 * @return Short name of this file, without the absolute path to it
	 */
	public String getName() {
		if (testingMode) {
			int i = name.length() - 1;
			while ((i >= 0) && name.charAt(i) != '/')
				--i;
			if (i < 0)
				return name;
			else
				return name.substring(i + 1);
		}
		return fileRef.getName();
	}

	/**
	 * 
	 * @return Long name for this file including the absolute path to it
	 */
	public String getLongName() {
		if (testingMode)
			return name;
		try {
			return fileRef.getCanonicalPath();
		} catch (IOException e) {
			System.out.println("Error getting name of object file");
			return "";
		}
	}

	/**
	 * 
	 * @return Size of the file in bytes. Returns zero if thif FIleObject is a
	 *         directory.
	 */
	public long getSize() {
		if (testingMode)
			return 0;
		if (isDirectory())
			return fileRef.length();
		else
			return fileRef.length();
	}

	/**
	 * 
	 * @return Number of files in the directory represented by this FileObject.
	 *         If this FileObject represents a file the value returned is zero.
	 */
	public int numFilesInDirectory() {
		if (testingMode)
			return numChildren;
		if (isDirectory())
			return fileRef.list().length;
		else
			return 0;
	}

	/**
	 * 
	 * @return List of FileObjects inside the directory represented by this
	 *         FileObject. If this FileObject represents a file, a null value is
	 *         returned.
	 */
	public Iterator<FileObject> directoryFiles() {
		if (testingMode)
			return children.iterator();
		try {
			if (isDirectory()) {
				File[] files = fileRef.listFiles();
				ArrayList<FileObject> fileObjects = new ArrayList<FileObject>();
				if (files == null)
					return fileObjects.iterator();
				for (int i = 0; i < files.length; ++i)
					if (files[i] != null)
						fileObjects.add(new FileObject(files[i]));
				return fileObjects.iterator();
			} else
				return null;
		} catch (FileObjectException e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	public static void resetNumFileObjects() {
		numFileObjects = 0;
	}

	public static int getNumFileObjects() {
		return numFileObjects;
	}
}
