import java.util.Comparator;

public class StringComparator implements Comparator<TreeNode<String>> {

	public int compare(TreeNode<String> obj1, TreeNode<String> obj2) {
		return obj1.getData().compareTo(obj2.getData());
	}
}
