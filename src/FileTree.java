
/*################################## Assignment 4: File System Tree  ###############################################
# Nicholas Tocco
# Student Number: 250909544
# James Hughes
# April 5th 2017
#
###################################################################################################################*/
import java.util.ArrayList;
import java.util.Iterator;

public class FileTree {

	/**
	 * declare instance variables
	 */
	private TreeNode<FileObject> root;
	private ArrayList<String> filenames;
	private ArrayList<String> filelongnames;

	/**
	 * creates tree from gives file name
	 * 
	 * @param fileObjectName
	 *            name of file which will be root of tree
	 * @throws FileObjectException
	 */
	public FileTree(String fileObjectName) throws FileObjectException {
		FileObject file = new FileObject(fileObjectName);
		root = new TreeNode<FileObject>(file, null);

		// if the file represents a directory, go into helper method to create
		// tree
		if (file.isDirectory()) {
			CreateTree(root);
		}
	}

	/**
	 * returns the root of the tree
	 * 
	 * @return the root of the tree
	 */
	public TreeNode<FileObject> getRoot() {
		return root;
	}

	/**
	 * filesOfType method returns all files of a given filetype
	 * 
	 * @param type
	 *            given file type to return files of
	 * @return
	 */
	public Iterator<String> filesOfType(String type) {
		ArrayList<String> fileNames = findFilesOfType(new ArrayList<String>(), root, type);
		return fileNames.iterator();
	}

	/**
	 * findFile returns the file with the given file name
	 * 
	 * @param name
	 *            is the file name that will be searched for
	 * @return file with given file name
	 */
	public String findFile(String name) {
		ArrayList<String> nameFile = findFilesOfName(new ArrayList<String>(), root, name);
		// if no file with the given name is found, return empty string
		if (nameFile.isEmpty()) {
			return "";
		} else {
			// if a file with the given filename is found, return it
			return nameFile.get(0);
		}
	}
	/**
	 * Method duplicatedFiles returns an iterator of all files with the same
	 * file name within the directory
	 * 
	 * @return
	 */
	public Iterator<String> duplicatedFiles() {
		filenames = new ArrayList<String>();
		filelongnames = new ArrayList<String>();
		ArrayList<String> dupfiles = DupFileSearch(new ArrayList<String>(), root);
		return dupfiles.iterator();
	}

	/**
	 * this method returns null because I could not figure it out
	 * @return
	 */
	public Iterator<String> duplicatedFolders(){
		return null;
	}

	/**
	 * searches the directory for all sub-folders and files
	 * 
	 * @param r
	 *            is the current file node
	 */
	private void CreateTree(TreeNode<FileObject> r) {
		FileObject filenew;
		// if the file represents a directory, keep searching to get all the
		// files
		if (r.getData().isDirectory()) {
			FileObject temp = r.getData();
			Iterator<FileObject> iter = temp.directoryFiles();
			// loops through all items
			while (iter.hasNext()) {
				filenew = iter.next();
				TreeNode<FileObject> n = new TreeNode<FileObject>(filenew, r);
				r.addChild(n);
				// recursively goes through each item to see all files and
				// sub-folders
				CreateTree(n);
			}
		}
	}

	/**
	 * findFilesOfType helper method searches the directory and returns an
	 * ArrayList of files with the specified file type
	 * 
	 * @param fileNames
	 *            is the ArrayList of file names with the specified file type
	 *            extension
	 * @param r
	 *            is the current node to search
	 * @param filetype
	 *            is the given file type extension to search for
	 * @return
	 */
	private ArrayList<String> findFilesOfType(ArrayList<String> fileNames, TreeNode<FileObject> r, String filetype) {
		TreeNode<FileObject> filenew;

		// if the fileobject is a directory, loop through each item and expand
		// each subfolder
		if (r.getData().isDirectory()) {
			Iterator<TreeNode<FileObject>> itr = r.getChildren();
			while (itr.hasNext()) {
				filenew = itr.next();
				findFilesOfType(fileNames, filenew, filetype);
			}
			// if the item is a file, and the file ends with the specified
			// extension, add it to the arraylist of matching files
		} else {
			if (r.getData().getName().endsWith(filetype)) {
				fileNames.add(r.getData().getLongName());
			}
		}
		return fileNames;
	}

	/**
	 * findFilesOfName is a helper method that searches through all items in the
	 * specified directory and returns a file with the same name
	 * 
	 * @param nameFile
	 *            is the ArrayList returned with either a matching file, or no
	 *            file appended
	 * @param r
	 *            is the current file searched
	 * @param filename
	 *            is the specified name of the file to search for
	 * @return
	 */
	private ArrayList<String> findFilesOfName(ArrayList<String> nameFile, TreeNode<FileObject> r, String filename) {
		TreeNode<FileObject> filenew;

		if (r.getData().isDirectory()) {
			Iterator<TreeNode<FileObject>> itr = r.getChildren();
			while (itr.hasNext()) {
				filenew = itr.next();
				findFilesOfName(nameFile, filenew, filename);
			}
		} else {
			if (r.getData().getName().equals(filename)) {
				nameFile.add(r.getData().getLongName());
			}
		}
		return nameFile;

	}

	/**
	 * helper for duplicated files. Traverses through tree finding files with
	 * the same file name
	 * 
	 * @param dupfiles
	 *            array list of duplicated files
	 * @param r
	 *            current tree node being searched
	 * @return an ArrayList of strings containing the duplicated files
	 */
	private ArrayList<String> DupFileSearch(ArrayList<String> dupfiles, TreeNode<FileObject> r) {
		TreeNode<FileObject> filenew;

		if (r.getData().isDirectory()) {
			Iterator<TreeNode<FileObject>> itr = r.getChildren();
			while (itr.hasNext()) {
				filenew = itr.next();
				DupFileSearch(dupfiles, filenew);
			}
			// if the filenames arraylist contains the same name as the current file, get the long name and add
			// it to the duplicated files arraylist
		} else {
			if (filenames.contains(r.getData().getName())) {
				int x = filenames.indexOf(r.getData().getName());
				dupfiles.add(filelongnames.get(x));
				dupfiles.add(r.getData().getLongName());
			} else {
				filenames.add(r.getData().getName());
				filelongnames.add(r.getData().getLongName());

			}
		}
		return dupfiles;
	}

	
	
	/*
	 * attempt at test 10 / bonus 2
	 * 
	 * public Iterator<String> duplicatedFolders() {
		foldnames = new ArrayList<String>();
		foldlongnames = new ArrayList<String>();
		ArrayList<String> dupfiles = DupFolderSearch(new ArrayList<String>(), root);
		return dupfiles.iterator();
	}

	private ArrayList<String> DupFolderSearch(ArrayList<String> dupfolds, TreeNode<FileObject> r) {
		TreeNode<FileObject> filenew;

		if (r.getData().isDirectory()) {
			Iterator<TreeNode<FileObject>> itr = r.getChildren();
			while (itr.hasNext()) {
				filenew = itr.next();
				if (areDuplicates(dupfolds, r, filenew)) {
					dupfolds.add(r.getData().getLongName());
					dupfolds.add(filenew.getData().getLongName());
				} else {
					DupFileSearch(dupfolds, filenew);
				}
			}
		}
		return dupfolds;
	}

	private boolean areDuplicates(ArrayList<String> dupfolds, TreeNode<FileObject> r, TreeNode<FileObject> n) {
		boolean flag = true;
		TreeNode<FileObject> filenew;
		TreeNode<FileObject> filenew2;
		NameComparator sorter = new NameComparator();
		if (r.getData().getName().equals(n.getData().getName())) {
			Iterator<TreeNode<FileObject>> itr1 = r.getChildren(sorter);
			Iterator<TreeNode<FileObject>> itr2 = n.getChildren(sorter);
			while (itr1.hasNext() && itr2.hasNext()) {
				filenew = itr1.next();
				filenew2 = itr2.next();
				if(filenew.getData().isDirectory()){
					DupFolderSearch(dupfolds,filenew);
				}
				if(filenew2.getData().isDirectory()){
					DupFolderSearch(dupfolds,filenew2);
				}
				else{
					if(!filenew.getData().getName().equals(filenew2.getData().getName())){
						flag = false;
					}
				}
			}
		}
		return flag;
	}
	*/
}
