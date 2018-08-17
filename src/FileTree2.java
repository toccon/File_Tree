import java.util.Iterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FileTree2 {
	
	private TreeNode<FileObject> root;
	
	/**
	 * creates directory tree from the given filename
	 * @param fileObjectName the name of the file to be the root of the tree
	 * @throws FileObjectException
	 */
	public FileTree2 (String fileObjectName) throws FileObjectException{
		
		FileObject fileObj = new FileObject(fileObjectName);
		root = new TreeNode<FileObject>(fileObj, null);
		
		//if file is a directory, then go into it recursively to get all sub-folders and files
		if (fileObj.isDirectory()) {
			recursiveSearch(root);
		}
	}
	/**
	 * gets the root of the tree
	 * @return
	 */
	public TreeNode<FileObject> getRoot(){
		return root;
	}
	/**
	 * uses a helper function to get all file of a given file type
	 * @param type: the file to search for 
	 * @return
	 */
	public Iterator<String> filesOfType (String type){
		ArrayList<String> matchingFiles = recursiveFileTypeSearch(new ArrayList<String>(), getRoot(), type);
		return matchingFiles.iterator();
		
	}
	/**
	 * uses a helper function to get a file with the given filename
	 * @param name the filename to search for
	 * @return
	 */
	public String findFile(String name){
		ArrayList<String> nameFile = recursiveNameSearch(new ArrayList<String>(), getRoot(), name);
		if (nameFile.isEmpty()){
			return ""; // if no matches were found return empty string 
			
		}else{
			return nameFile.get(0); // just return the first found file of this type
		}
	}
	/**
	 * searches directory recursively to examine all sub-folders and files 
	 * @param file: the current file node to search in 
	 */
	private void recursiveSearch (TreeNode<FileObject> file){
		
		FileObject newFile;
		
		if (file.getData().isDirectory()){
			// if directory, keep searching recursively to get all files 
			Iterator<FileObject> dir = file.getData().directoryFiles();
			//loop through all items within the directory
			while (dir.hasNext()){
				newFile = dir.next();
				TreeNode<FileObject> newNode = new TreeNode<FileObject>(newFile, file);
				file.addChild(newNode);
				//Recursively examine each item so that all sub-folders can be examined too
				recursiveSearch(newNode);
			}
		}
	}
	/**
	 * searches directory recursively to examine all sub-folders and files and keep track of those with the given file extension
	 * @param matchFiles: arraylist containing all files that contain the extension
	 * @param file: the current file node to search in
	 * @param ext: the file extension to search for
	 * @return
	 */
	private ArrayList<String> recursiveFileTypeSearch (ArrayList<String> matchFiles, TreeNode<FileObject> file, String ext){
		
		TreeNode<FileObject> newFile;
		
		if (file.getData().isDirectory()){
			// if directory, keep searching recursively to get all files 
			Iterator<TreeNode<FileObject>> dir = file.getChildren();
			//loop through all items within the directory
			while (dir.hasNext()){
				newFile = dir.next();
				//Recursively examine each item so that all sub-folders can be examined too
				recursiveFileTypeSearch(matchFiles, newFile, ext);
			}
		}else{
			//if file, check if file type is a match
			if (file.getData().getName().endsWith(ext)){
				matchFiles.add(file.getData().getLongName());
			}
		}
		return matchFiles;
		
	}
	/**
	 * searches directory recursively to examine all sub-folders and files and keep track of those with the given file name
	 * @param matchFiles: array list containing all files with the given file name
	 * @param file: the current file node to search in 
	 * @param name: the file name to search for
	 * @return
	 */
	private ArrayList<String> recursiveNameSearch (ArrayList<String> matchFiles, TreeNode<FileObject> file, String name){
		
		TreeNode<FileObject> newFile;
		
		if (file.getData().isDirectory()){
			// if directory, keep searching recursively to get all files 
			Iterator<TreeNode<FileObject>> dir = file.getChildren();
			//loop through all items within the directory
			while (dir.hasNext()){
				newFile = dir.next();
				//Recursively examine each item so that all sub-folders can be examined too
				recursiveNameSearch(matchFiles, newFile, name);
			}
		}else{
			//if file, check if file type is a match
			if (file.getData().getName().equals(name)){
				matchFiles.add(file.getData().getLongName());
			}
		}
		return matchFiles;
		
	}
	
	
	// --  BONUS  --
	
	
	/**
	 * uses a helper function to find all duplicate files within the directory 
	 * @return
	 */
	public Iterator<String> duplicatedFiles(){
		
		Map<String,ArrayList<String>> dupNames = new HashMap<String,ArrayList<String>>();
		recursiveDuplicateFileSearch(dupNames, getRoot());
		
		Iterator<String> iter = dupNames.keySet().iterator();
		int i;
		String key;
		ArrayList<String> duplicateFileNames = new ArrayList<String>();
		
		//loop through all files in map
		while(iter.hasNext()){
			key = iter.next();
			//the entry's arraylist size indicates how many files are found with that name. Check if this size is > 1 to get the duplicates
			if (dupNames.get(key).size() > 1){
				for (i = 0; i < dupNames.get(key).size(); i++){
					duplicateFileNames.add(dupNames.get(key).get(i));
				}
			}
		}
		return duplicateFileNames.iterator();
	}
	
	/**
	 * recursively searches through folder and keeps track of files that are identical
	 * @param dupNames: arraylist of duplicate files 
	 * @param file: the current file node to search in 
	 */
	private void recursiveDuplicateFileSearch (Map<String,ArrayList<String>> dupNames, TreeNode<FileObject> file){
		
		TreeNode<FileObject> newFile;
		
		if (file.getData().isDirectory()){
			// if directory, keep searching recursively to get all files 
			Iterator<TreeNode<FileObject>> dir = file.getChildren();
			//loop through all items within the directory
			while (dir.hasNext()){
				newFile = dir.next();
				//Recursively examine each item so that all sub-folders can be examined too
				recursiveDuplicateFileSearch(dupNames, newFile);
			}
		}else{
			//if file, check if file type is a match
			if (dupNames.containsKey(file.getData().getName())){
				dupNames.get(file.getData().getName()).add(file.getData().getLongName());
			}else{
				ArrayList<String> newEntry = new ArrayList<String>();
				newEntry.add(file.getData().getLongName());
				dupNames.put(file.getData().getName(), newEntry);
			}
			
		}
		
		
	}
	
	/**
	 * uses a helper function to find all duplicate folders (same name and same contents) within the directory
	 * @return
	 */
	public Iterator<String> duplicatedFolders(){
		
		
		Map<String,ArrayList<TreeNode<FileObject>>> dupFolders = new HashMap<String,ArrayList<TreeNode<FileObject>>>();
		recursiveDuplicateFolderSearch(dupFolders, getRoot());
		
		Iterator<String> iter = dupFolders.keySet().iterator();
		int i;
		String key;
		ArrayList<String> duplicateFolderNames = new ArrayList<String>();
		
		//loop through all files in map
		while(iter.hasNext()){
			key = iter.next();
			//the entry's arraylist size indicates how many files are found with that name. Check if this size is > 1 to get the duplicates
			if (dupFolders.get(key).size() > 1){
				for (i = 0; i < dupFolders.get(key).size(); i++){
					duplicateFolderNames.add(dupFolders.get(key).get(i).getData().getLongName());
				}
			}
		}
		
		return duplicateFolderNames.iterator();
	
	}
	
	/**
	 * recursively searches through folder and keeps track of folders that are identical
	 * @param dupFolders: arraylist of all folders that are identical 
	 * @param file: current file node to search in 
	 */
	private void recursiveDuplicateFolderSearch (Map<String,ArrayList<TreeNode<FileObject>>> dupFolders, TreeNode<FileObject> file){
		
		
		TreeNode<FileObject> newFile;
		
		if (file.getData().isDirectory()) {
			// if directory, keep searching recursively to get all files 
			Iterator<TreeNode<FileObject>> dir = file.getChildren();
			//loop through all items within the directory
			
			while (dir.hasNext()){
				newFile = dir.next();
				//Recursively examine each item so that all sub-folders can be examined too
				recursiveDuplicateFolderSearch(dupFolders, newFile);
			}
			
			//check if folder is a match
			if (dupFolders.containsKey(file.getData().getName())){
				//check if current folder of this name is actually identical to the new folder of this name
				TreeNode<FileObject> duplicateFolder = findIdenticalFolder(dupFolders.get(file.getData().getName()), file);
				if (duplicateFolder != null) {
					//use actual duplicate folder as key in arraylist
					dupFolders.get(duplicateFolder.getData().getName()).add(file);
				} else {
					//this is when the folder has the same name but is not a match based on contents
					ArrayList<TreeNode<FileObject>> newEntry = new ArrayList<TreeNode<FileObject>>();
					newEntry.add(file);
					dupFolders.put(file.getData().getName(), newEntry);
				}
			
		}else{
			//if the folder name does not exist in the hashmap at all
			ArrayList<TreeNode<FileObject>> newEntry = new ArrayList<TreeNode<FileObject>>();
			newEntry.add(file);
			dupFolders.put(file.getData().getName(), newEntry);
			
		}
		
		}	
			
		
	
		
	}
	
	/**
	 * search through list and compare each folder with the given folder A
	 * @param list: arraylist of folders with same name as A
	 * @param A: new folder that we want to compare to
	 * @return
	 */
	private TreeNode<FileObject> findIdenticalFolder (ArrayList<TreeNode<FileObject>> list, TreeNode<FileObject> A) {
		int i;
		//for each list item compare with fold A to see if identical
		for (i = 0; i < list.size(); i++) {
			if(areFoldersIdentical(list.get(i), A)){
			return list.get(i);
			}
		}
		return null;
	}
	
	/**
	 * starts recursive comparison between folders A and B
	 * @param A: first folder in the comparison 
	 * @param B: second folder in the comparison 
	 * @return
	 */
	private boolean areFoldersIdentical (TreeNode<FileObject> A, TreeNode<FileObject> B){
		
		return (identical(A.getData(), B.getData()));
	}
	
	/**
	 * this will check if folders N and M are identical in terms of there content
	 * @param M: first folder in the comparison 
	 * @param N: second folder in the comparison 
	 * @return
	 */
	private boolean identical (FileObject M, FileObject N){
		
		// insure M and N are both folders 
		if (M.isDirectory() && N.isDirectory()) {
			
			Iterator<FileObject> x = M.directoryFiles();
			Iterator<FileObject> y = N.directoryFiles();
			
			//  insure M and N have the same number of files 
			if (M.numFilesInDirectory() == N.numFilesInDirectory()) {
				
				boolean allFilesIdentical = true;
				//insure all files within M and N are the same 
				while (x.hasNext()) {
					if (!identical(x.next(), y.next())) {
						allFilesIdentical = false;
					}
				}
				return allFilesIdentical;
			} else{
				//the 2 folders have different numbers of files so they cannot be identical
				return false;
			}
		} else if (M.isFile() && N.isFile()) {
			
			//check if the 2 file names are identical
			return (M.getName().equals(N.getName()));
		} else{
			return false;
		}
	}
	
	
	
}
