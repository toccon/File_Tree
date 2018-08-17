import java.util.Comparator;

public class SizeComparator implements Comparator<TreeNode<FileObject>>{
	
	public int compare(TreeNode<FileObject> obj1, TreeNode<FileObject> obj2) {
		if (obj1.getData().getSize() < obj2.getData().getSize()) return 1;
		else if (obj1.getData().getSize() == obj2.getData().getSize()) return 0;
		else return -1;
	}

}
