import java.util.Stack;

public class CheckBST {
    // Add input validation
    boolean checkBST(Node root) {
        if (root == null) return true;
        return checkBST(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    boolean checkBST(Node root, long min, long max) {
        if (root == null) return true;
        
        // Convert to long to handle integer overflow
        long data = root.data;
        if (data <= min || data >= max) return false;
        
        return checkBST(root.left, min, data) && checkBST(root.right, data, max);
    }

    static class Node {
        int data;
        Node left;
        Node right;
        
        Node(int data) {
            this.data = data;
            left = null;
            right = null;
        }
        
        // Add toString method for better debugging
        @Override
        public String toString() {
            return "Node(" + data + ")";
        }
    }

    // Helper method to create and test a tree
    private static void testTree(CheckBST bst, Node root, String testCase, boolean expectedResult) {
        boolean result = bst.checkBST(root);
        System.out.println("Test Case: " + testCase);
        System.out.println("Expected Result: " + expectedResult);
        System.out.println("Actual Result: " + result);
        System.out.println("Status: " + (result == expectedResult ? "✅ PASSED" : "❌ FAILED"));
        System.out.println("--------------------");
    }

    // Helper method to print tree structure (useful for debugging)
    private static void printTree(Node root, String prefix, boolean isLeft) {
        if (root == null) return;
        
        System.out.println(prefix + (isLeft ? "└── " : "┌── ") + root.data);
        printTree(root.left, prefix + (isLeft ? "    " : "│   "), true);
        printTree(root.right, prefix + (isLeft ? "    " : "│   "), false);
    }

    public static void main(String[] args) {
        CheckBST bst = new CheckBST();
        int totalTests = 0;
        int passedTests = 0;

        // Test Case 1: Simple valid BST
        Node root1 = new Node(5);
        root1.left = new Node(3);
        root1.right = new Node(7);
        totalTests++;
        if (bst.checkBST(root1) == true) passedTests++;
        testTree(bst, root1, "Simple Valid BST", true);
        System.out.println("Tree Structure:");
        printTree(root1, "", true);

        // Test Case 2: Complex valid BST
        Node root2 = new Node(20);
        root2.left = new Node(10);
        root2.right = new Node(30);
        root2.left.left = new Node(5);
        root2.left.right = new Node(15);
        root2.right.left = new Node(25);
        root2.right.right = new Node(35);
        root2.left.left.left = new Node(3);
        root2.left.right.right = new Node(17);
        totalTests++;
        if (bst.checkBST(root2) == true) passedTests++;
        testTree(bst, root2, "Complex Valid BST", true);
        System.out.println("Tree Structure:");
        printTree(root2, "", true);

        // Test Case 3: Invalid BST (violates BST property)
        Node root3 = new Node(20);
        root3.left = new Node(10);
        root3.right = new Node(30);
        root3.left.right = new Node(25);  // Invalid: 25 is greater than parent 10
        totalTests++;
        if (bst.checkBST(root3) == false) passedTests++;
        testTree(bst, root3, "Invalid BST (Violates BST Property)", false);
        System.out.println("Tree Structure:");
        printTree(root3, "", true);

        // Test Case 4: BST with duplicate value
        Node root4 = new Node(20);
        root4.left = new Node(20);  // Duplicate value
        root4.right = new Node(30);
        totalTests++;
        if (bst.checkBST(root4) == false) passedTests++;
        testTree(bst, root4, "BST with Duplicate Value", false);
        System.out.println("Tree Structure:");
        printTree(root4, "", true);

        // Test Case 5: Single node BST
        Node root5 = new Node(1);
        totalTests++;
        if (bst.checkBST(root5) == true) passedTests++;
        testTree(bst, root5, "Single Node BST", true);
        System.out.println("Tree Structure:");
        printTree(root5, "", true);

        // Test Case 6: Empty BST
        totalTests++;
        if (bst.checkBST(null) == true) passedTests++;
        testTree(bst, null, "Empty BST", true);

        // Test Case 7: BST with negative values
        Node root7 = new Node(0);
        root7.left = new Node(-10);
        root7.right = new Node(10);
        root7.left.left = new Node(-20);
        root7.left.right = new Node(-5);
        totalTests++;
        if (bst.checkBST(root7) == true) passedTests++;
        testTree(bst, root7, "BST with Negative Values", true);
        System.out.println("Tree Structure:");
        printTree(root7, "", true);

        // Test Case 8: Unbalanced but valid BST
        Node root8 = new Node(50);
        root8.left = new Node(30);
        root8.left.left = new Node(20);
        root8.left.left.left = new Node(10);
        totalTests++;
        if (bst.checkBST(root8) == true) passedTests++;
        testTree(bst, root8, "Unbalanced but Valid BST", true);
        System.out.println("Tree Structure:");
        printTree(root8, "", true);

        // Test Case 9: Edge case with Integer.MIN_VALUE and Integer.MAX_VALUE
        Node root9 = new Node(0);
        root9.left = new Node(Integer.MIN_VALUE);
        root9.right = new Node(Integer.MAX_VALUE);
        totalTests++;
        if (bst.checkBST(root9) == true) passedTests++;
        testTree(bst, root9, "Edge Case with Integer Limits", true);
        System.out.println("Tree Structure:");
        printTree(root9, "", true);

        // Print test summary
        System.out.println("\n=== Test Summary ===");
        System.out.println("Total Tests: " + totalTests);
        System.out.println("Tests Passed: " + passedTests);
        System.out.println("Tests Failed: " + (totalTests - passedTests));
        System.out.println("Success Rate: " + (passedTests * 100 / totalTests) + "%");
        System.out.println("==================");
    }

    int sum = 0;
    
    void convertToGreaterSum(Node root) {
        if (root == null) return;
        convertToGreaterSum(root.right);
        sum += root.data;
        root.data = sum;
        convertToGreaterSum(root.left);
    }

    Node findLCA(Node root, int n1, int n2) {
        if (root == null) return null;
        if (root.data > n1 && root.data > n2)
            return findLCA(root.left, n1, n2);
        if (root.data < n1 && root.data < n2)
            return findLCA(root.right, n1, n2);
        return root;
    }

    int count = 0;
    int result = -1;
    
    int kthSmallest(Node root, int k) {
        count = 0;
        result = -1;
        kthSmallestHelper(root, k);
        return result;
    }

    private void kthSmallestHelper(Node root, int k) {
        if (root == null) return;
        kthSmallestHelper(root.left, k);
        count++;
        if (count == k) {
            result = root.data;
            return;
        }
        kthSmallestHelper(root.right, k);
    }

    class BSTIterator {
        Stack<Node> stack = new Stack<>();
        
        public BSTIterator(Node root) {
            pushAll(root);
        }
        
        void pushAll(Node node) {
            while (node != null) {
                stack.push(node);
                node = node.left;
            }
        }
        
        public boolean hasNext() {
            return !stack.isEmpty();
        }
        
        public int next() {
            Node node = stack.pop();
            pushAll(node.right);
            return node.data;
        }
    }

    boolean isBST(Node root, long min, long max) {
        if (root == null) return true;
        if (root.data < min || root.data > max) return false;
        return isBST(root.left, min, root.data - 1) && 
               isBST(root.right, root.data + 1, max);
    }

    int findClosestValue(Node root, double target) {
        if (root == null) return -1;
        int closest = root.data;
        while (root != null) {
            if (Math.abs(target - root.data) < Math.abs(target - closest))
                closest = root.data;
            root = target < root.data ? root.left : root.right;
        }
        return closest;
    }
}
