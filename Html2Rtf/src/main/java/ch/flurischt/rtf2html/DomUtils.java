package ch.flurischt.rtf2html;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.nodes.Node;

public class DomUtils {

	/**
	 * Returns the least common node of a list of nodes
	 * 
	 * @return
	 */
	public static Node findCommonAncestor(List<Node> nodes) {

		if (nodes.isEmpty())
			return null;

		if (nodes.size() == 1)
			return nodes.get(0).parent();

		Node ancestor = nodes.get(0);
		for (int i = 1; i < nodes.size(); i++) {
			ancestor = findCommonAncestor(ancestor, nodes.get(i));
		}
		return ancestor;

	}

	/**
	 * Returns the least common node of two nodes
	 * 
	 * @return
	 */
	public static Node findCommonAncestor(Node n1, Node n2) {

		// If param is null
		if (n1 == null || n2 == null)
			return null;

		// If both nodes are identical, return their parent
		if (n1.equals(n2))
			return n1.parent();

		List<Node> l1 = path(n1);
		List<Node> l2 = path(n2);

		// Different root
		if (l1.get(0) != l2.get(0))
			return null;

		Node n = null;
		for (int i = 1; i < Math.min(l1.size(), l2.size()); i++) {
			if (l1.get(i) == l2.get(i))
				n = l1.get(i);
		}

		return n;

	}

	/**
	 * Returns the path (top-down) of a node, starting with the root
	 * 
	 * @param node
	 * @return path
	 */
	public static List<Node> path(Node node) {
		List<Node> parents = new LinkedList<Node>();

		parents.add(node);

		// Iterate till root is reached
		Node n = node.parent();

		while (n != null) {
			parents.add(n);
			n = n.parent();
		}

		Collections.reverse(parents);

		return parents;
	}

	/**
	 * Finds the highest node under a specific parent, staring from a leaf
	 * 
	 * @param parent
	 *            Subtree to consider
	 * @param leaf
	 *            Leaf to start from
	 * @return
	 */
	public static Node findSubNode(Node parent, Node leaf) {

		Node n = leaf;
		while (n != null && n.parent() != parent) {

			n = n.parent();
		}

		return n;
	}
}
