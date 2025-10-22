// Make sure the Node class is either in its own file or defined here
public class AVLTree {
    private Node root;

    // --- Utility functions (height, getBalance) ---
    private int height(Node N) {
        return N == null ? 0 : N.height;
    }

    private int getBalance(Node N) {
        return (N == null) ? 0 : height(N.left) - height(N.right);
    }

    // --- Rotations (left, right) ---
    private Node rightRotate(Node y) {
        Node x = y.left;
        Node T2 = x.right;
        x.right = y;
        y.left = T2;
        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;
        return x;
    }

    private Node leftRotate(Node x) {
        Node y = x.right;
        Node T2 = y.left;
        y.left = x;
        x.right = T2;
        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;
        return y;
    }

    // --- Insert ---
    public void insert(String key) {
        root = insertRec(root, key);
    }

    private Node insertRec(Node node, String key) {
        if (node == null)
            return new Node(key);

        if (key.compareTo(node.key) < 0)
            node.left = insertRec(node.left, key);
        else if (key.compareTo(node.key) > 0)
            node.right = insertRec(node.right, key);
        else
            return node; // Duplicate keys not allowed

        node.height = 1 + Math.max(height(node.left), height(node.right));
        int balance = getBalance(node);

        // Rebalance
        if (balance > 1 && key.compareTo(node.left.key) < 0)
            return rightRotate(node);
        if (balance < -1 && key.compareTo(node.right.key) > 0)
            return leftRotate(node);
        if (balance > 1 && key.compareTo(node.left.key) > 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }
        if (balance < -1 && key.compareTo(node.right.key) < 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    // --- Delete (NEWLY ADDED) ---
    public void delete(String key) {
        root = deleteRec(root, key);
    }

    private Node deleteRec(Node root, String key) {
        if (root == null)
            return root;

        // Find the node
        if (key.compareTo(root.key) < 0)
            root.left = deleteRec(root.left, key);
        else if (key.compareTo(root.key) > 0)
            root.right = deleteRec(root.right, key);
        else {
            // Node found. Handle cases:
            // 1. Node with only one child or no child
            if ((root.left == null) || (root.right == null)) {
                Node temp = (root.left != null) ? root.left : root.right;
                if (temp == null) { // No child
                    root = null;
                } else { // One child
                    root = temp;
                }
            } else {
                // 2. Node with two children
                Node temp = minValueNode(root.right);
                root.key = temp.key; // Copy inorder successor's data
                root.right = deleteRec(root.right, temp.key); // Delete successor
            }
        }

        if (root == null)
            return root; // If tree had only one node

        // Update height
        root.height = Math.max(height(root.left), height(root.right)) + 1;

        // Get balance
        int balance = getBalance(root);

        // Rebalance (4 cases)
        if (balance > 1 && getBalance(root.left) >= 0)
            return rightRotate(root);
        if (balance > 1 && getBalance(root.left) < 0) {
            root.left = leftRotate(root.left);
            return rightRotate(root);
        }
        if (balance < -1 && getBalance(root.right) <= 0)
            return leftRotate(root);
        if (balance < -1 && getBalance(root.right) > 0) {
            root.right = rightRotate(root.right);
            return leftRotate(root);
        }

        return root;
    }

    // Helper to find the node with the smallest key (inorder successor)
    private Node minValueNode(Node node) {
        Node current = node;
        while (current.left != null)
            current = current.left;
        return current;
    }

    // --- Traversal ---
    public void inOrder() {
        inOrderRec(root);
    }

    private void inOrderRec(Node node) {
        if (node != null) {
            inOrderRec(node.left);
            System.out.print(node.key + " ");
            inOrderRec(node.right);
        }
    }
}