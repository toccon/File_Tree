import java.util.Comparator;

public class NameComparator implements Comparator<TreeNode<FileObject>> {

	public int compare(TreeNode<FileObject> obj1, TreeNode<FileObject> obj2) {
		return obj1.getData().getName().compareTo(obj2.getData().getName());
	}
	
}
