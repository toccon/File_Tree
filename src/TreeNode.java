
/*################################## Assignment 4: File System Tree  ###############################################
# Nicholas Tocco
# Student Number: 250909544
# James Hughes
# April 5th 2017
#
###################################################################################################################*/
import java.util.Comparator;
import java.util.Iterator;

public class TreeNode<T> {

	/**
	 * declare instance variables
	 */
	private TreeNode<T> parent;
	private ListNodes<TreeNode<T>> children;
	private T data;

	/**
	 * first constructor will set parent and data to null, while children will be set to an empty ListNodes<TreeNode<T>> object
	 */
	public TreeNode() {
		parent = null;
		data = null;
		children = new ListNodes<TreeNode<T>>();
	}
	/**
	 * second constructor will also set children to an empty ListNodes<TreeNode<T>>, while data will be set to d and parent to p.
	 * @param d will set data to d
	 * @param p will set parent to p
	 */
	public TreeNode (T d, TreeNode<T> p){
		parent = p;
		data = d;
		children = new ListNodes<TreeNode<T>>();
	}
	/**
	 * returns the parent of this node
	 * @return
	 */
	public TreeNode<T> getParent() {
		return parent;
	}
	/**
	 * sets the parent of this node to p
	 * @param p
	 */
	public void setParent(TreeNode<T> p) {
		this.parent = p;
	}
	/**
	 * returns the data stored in this node
	 * @return
	 */
	public T getData() {
		return data;
	}
	/**
	 * Stores in this node the data object referenced by d.
	 * @param d
	 */
	public void setData(T d) {
		this.data = d;
	}
	/**
	 * Adds the given node c to the list of children of this node. Node c must have its parent set to this node
	 * @param c
	 */
	public void addChild(TreeNode<T> c){
		children.add(c);
		c.setParent(this); 
	}

	/**
	 * Returns an iterator containing the children nodes of this node.
	 * @return
	 */
	public Iterator<TreeNode<T>> getChildren(){
		return children.getList();
		
	}
	/**
	 * Returns an iterator containing the children nodes of this node sorted in the order specified by the parameter sorter. 
	 * @param sorter
	 * @return
	 */
	public Iterator<TreeNode<T>> getChildren(Comparator<TreeNode<T>> sorter){
		return children.sortedList(sorter);
	}
}
