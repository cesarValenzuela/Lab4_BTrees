/**
 * Author: Cesar Valenzuela
 * Date: 10/13/2017
 * Course: CS 2302
 * Assignment:
 * Instructor: Diego Aguirre
 * T.A: Ismael Villanueva-Miranda
 */
public class Valenzuela_Cesar_Lab4 {
    public static void main(String[] args) {
        int [] S ={1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20};

        BTree B = new BTree(3);
        for (int i=0;i<S.length;i++){
            B.insert(S[i]);
//            B.printNodes();
//            System.out.println(" "/*"*********************"*/);
        }
        BTreeNode T = B.root;
        printAsc(T);
        System.out.println("");
        System.out.println("sum all keys: " +sumAllKeys(T));
        System.out.println("sum all keys at depth: " + sumAllKeysDepth(T,3));
        System.out.println("max element: " + maxElement(T));
        System.out.println("max element at depth: " + maxElementDepth(T,1));
        System.out.println("min element: " + minElement(T));
        System.out.println("min element at depth: " + minElementDepth(T,1));
        System.out.println("number of keys in tree: " + numKeys(T));
        System.out.println("number of keys at depth: " + numKeysDepth(T,2));
        System.out.println("is key present: " + isKeyPresent(T,16));
        System.out.println("depth of key: " + depthOfKey(T,16));
        System.out.println("number of nodes: " + numNodes(T));
        //printDesc(T, 1);
        B.printHeight();
        B.printNumNodes();
        System.out.println("");
        T.printNodes();
        System.out.println("");

        //Build B-tree with random elements
//        Random rn = new Random();
//        BTree R = new BTree(4);
//        for (int i=0;i<30;i++){
//            R.insert(rn.nextInt(100));
//            R.printNodes();
//            System.out.println(" "/*"*********************"*/);
//        }
//        T = R.root;
//        printAsc(T);

    }

    public static class BTreeNode {
        public int n;          // Actual number of keys on the node
        public boolean isLeaf; // Boolean indicator
        public int[] key;      // Keys stored in the node. They are sorted in ascending order
        public BTreeNode[] c;  // Children of node. Keys in c[i] are less than key[i] (if it exists)
                               // and greater than key[i-1] if it exists

        public BTreeNode(int t) {  // Build empty node
            isLeaf = true;
            key = new int[(2 * t) - 1];   // Array sizes are set to maximum possible size
            c = new BTreeNode[2 * t];
            n = 0;                      // Number of elements is zero, since node is empty
        }

        public boolean isFull() {
            return n == key.length;
        }

        public void insert(int newKey) {
            // Instert new key to current node
            // We make sure that the current node is not full by checking and
            // splitting if necessary before descending to node

            //System.out.println("inserting " + newKey); // Debugging code
            int t = c.length / 2;
            int i = n - 1;
            if (isLeaf) {
                while ((i >= 0) && (newKey < key[i])) { // Shift key greater than newKey to left
                    key[i + 1] = key[i];
                    i--;
                }
                n++;                    // Update number of keys in node
                key[i + 1] = newKey;        // Insert new key
            } else {
                while ((i >= 0) && (newKey < key[i])) {
                    i--;
                }
                int insertChild = i + 1;  // Subtree where new key must be inserted
                if (c[insertChild].isFull()) {
                    // The root of the subtree where new key will be inserted has to be split
                    // We promote the median of that root to the current node and
                    // update keys and references accordingly

                    // Debugging code
                    // System.out.println("This is the full node we're going to break ");
                    // c[insertChild].printNodes();
                    // System.out.println("going to promote " + c[insertChild].key[T-1]);
                    n++;
                    c[n] = c[n - 1];
                    for (int j = n - 1; j > insertChild; j--) {
                        c[j] = c[j - 1];
                        key[j] = key[j - 1];
                    }
                    key[insertChild] = c[insertChild].key[t - 1];
                    c[insertChild].n = t - 1;

                    BTreeNode newNode = new BTreeNode(t);
                    for (int k = 0; k < t - 1; k++) {
                        newNode.c[k] = c[insertChild].c[k + t];
                        newNode.key[k] = c[insertChild].key[k + t];
                    }

                    newNode.c[t - 1] = c[insertChild].c[2 * t - 1];
                    newNode.n = t - 1;
                    newNode.isLeaf = c[insertChild].isLeaf;
                    c[insertChild + 1] = newNode;

                    if (newKey < key[insertChild]) {
                        c[insertChild].insert(newKey);
                    } else {
                        c[insertChild + 1].insert(newKey);
                    }
                } else
                    c[insertChild].insert(newKey);  //No need to split node
            }
        }

        public void print() {
            //Prints all keys in the tree in ascending order
            if (isLeaf) {
                for (int i = 0; i < n; i++)
                    System.out.print(key[i] + " ");
                //System.out.println();
            } else {
                for (int i = 0; i < n; i++) {
                    c[i].print();
                    System.out.print(key[i] + " ");
                }
                c[n].print();
            }
        }

        public void printNodes() {
            //Prints all keys in the tree, node by node, using preorder
            //It also prints the indicator of whether a node is a leaf
            //Used mostly for debugging purposes
            for (int i = 0; i < n; i++)
                System.out.print(key[i] + " ");
            System.out.println(isLeaf);
            if (!isLeaf) {
                for (int i = 0; i <= n; i++) {
                    c[i].printNodes();
                }
            }
        }
    }

    public static class BTree {
        public BTreeNode root;
        private int t; //2t is the maximum number of childen a node can have
        private int height;
        private int numNodes;

        public BTree(int t) {
            root = new BTreeNode(t);
            this.t = t;
            height = 0;
            numNodes = 0;
        }

        public void printHeight() {
            System.out.println("Tree height is " + height);
        }

        public void printNumNodes(){
            System.out.println("Number of nodes in tree: " + numNodes);
        }

        public void insert(int newKey) {
            if (root.isFull()) {//Split root;
                split();
                height++;
                numNodes = numNodes + 2;
            }
            root.insert(newKey);
        }

        public void print() {
            // Wrapper for node print method
            root.print();
        }

        public void printNodes() {
            // Wrapper for node print method
            root.printNodes();
        }

        public void split() {
            // Splits the root into three nodes.
            // The median element becomes the only element in the root
            // The left subtree contains the elements that are less than the median
            // The right subtree contains the elements that are larger than the median
            // The height of the tree is increased by one

            // System.out.println("Before splitting root");
            // root.printNodes(); // Code used for debugging
            BTreeNode leftChild = new BTreeNode(t);
            BTreeNode rightChild = new BTreeNode(t);
            leftChild.isLeaf = root.isLeaf;
            rightChild.isLeaf = root.isLeaf;
            leftChild.n = t - 1;
            rightChild.n = t - 1;
            int median = t - 1;
            for (int i = 0; i < t - 1; i++) {
                leftChild.c[i] = root.c[i];
                leftChild.key[i] = root.key[i];
            }
            leftChild.c[median] = root.c[median];
            for (int i = median + 1; i < root.n; i++) {
                rightChild.c[i - median - 1] = root.c[i];
                rightChild.key[i - median - 1] = root.key[i];
            }
            rightChild.c[median] = root.c[root.n];
            root.key[0] = root.key[median];
            root.n = 1;
            root.c[0] = leftChild;
            root.c[1] = rightChild;
            root.isLeaf = false;
            // System.out.println("After splitting root");
            // root.printNodes();
        }
    }

    public static void printAsc(BTreeNode x) {
        if(x.isLeaf){
            for (int i = 0; i < x.n ; i++) {
                System.out.print(x.key[i] + " ");
            }
        }
        else{
            for (int i = 0; i < x.n ; i++) {
                printAsc(x.c[i]);
                System.out.print(x.key[i] + " ");
            }
            printAsc(x.c[x.n]);
        }
    }

    public static void printDesc(BTreeNode x, int d) {
        if(x == null || d <= 0){
            for (int i = x.n-1; i >= 0 ; i--) {
                System.out.print(x.key[i] + " ");
            }
        }
        else{
            for (int i = x.n-1; i >= 0 ; i--) {
                printDesc(x.c[i],d -1);
                System.out.print(x.key[i] + " ");
            }
        }
    }

    public static Boolean isKeyPresent(BTreeNode x, int k) {
        for (int i = 0; i < x.n ; i++) {
            if(x.key[i] == k){
                return true;
            }
            if(x.key[i] > k){
                return isKeyPresent(x.c[i],k);
            }
            if(x.key[x.n-1] < k){
                return isKeyPresent(x.c[x.n],k);
            }
        }
        return false;
    }

    public static int minElement(BTreeNode x) {
        if(x.isLeaf){
            return x.key[0];
        }
        return minElement(x.c[0]);
    }

    public static int minElementDepth(BTreeNode x, int d) {
        if(x.isLeaf || d <= 0){
            return x.key[0];
        }
        return minElementDepth(x.c[0], d-1);
    }

    public static int maxElement(BTreeNode x) {
        if(x.isLeaf){
            return x.key[x.n-1];
        }
        return maxElement(x.c[x.n]);
    }

    public static int maxElementDepth(BTreeNode x, int d) {
        if(x.isLeaf || d <= 0){
            return x.key[x.n-1];
        }
        return maxElementDepth(x.c[x.n], d-1);
    }
    //needs fix
    public static int numNodes(BTreeNode x) {
        if(x == null){
            return 0;
        }
        int count = 1;
        for (int i = 0; i < x.n ; i++) {
            count += numNodes(x.c[i]);
        }
        return count;
    }

    public static int numKeys(BTreeNode x) {
        if(x.isLeaf){
            return x.n;
        }
        int sum = 0;
        for(int i = 0; i <= x.n ; i++){
            sum += numKeys(x.c[i]);
        }
        return sum + x.n;
    }
    //needs fix
    public static int numKeysDepth(BTreeNode x, int d) {
        if(x.isLeaf || d <= 0){
            return x.n;
        }
        int sum = 0;
        for (int i = 0; i <= x.n ; i++) {
            sum += numKeysDepth(x.c[i], d-1);
        }
        return sum + x.n;
    }

    public static int sumAllKeys(BTreeNode x) {
        if(x == null){
            return 0;
        }
        int sum = 0;
        for (int i = 0; i < x.n ; i++) {
            sum += x.key[i];
        }
        if(!x.isLeaf){
            for (int i = 0; i <= x.n ; i++) {
                sum += sumAllKeys(x.c[i]);
            }
        }
        return sum;
    }

    public static int sumAllKeysDepth(BTreeNode x, int d) {
        if(x == null || d <= 0){
            return 0;
        }
        int sum = 0;
        for (int i = 0; i < x.n ; i++) {
            sum += x.key[i];
        }
        if(!x.isLeaf){
            for (int i = 0; i <= x.n ; i++) {
                sum += sumAllKeysDepth(x.c[i],d-1);
            }
        }
        return sum;
    }

    public static int numLeaves(BTreeNode x) {
        return -1;
    }

    public static int numNodesDepth(BTreeNode x, int d) {
        return -1;
    }

    public static int numFullNodes(BTreeNode x) {
        return -1;
    }
    //needs fix
    public static int depthOfKey(BTreeNode x, int k) {
        int i = 0;
        while((i<x.n)&&(k>x.key[i])) {
            i++;
        }
        if((i==x.n)||(k<x.key[i])){
            if(x.isLeaf){
                return -1;
            }
            else{
                int d = depthOfKey(x.c[i], k);
                if (d==1){
                    return -1;
                }
                else{
                    return d+1;
                }
            }
        }
        else{
            return 0;
        }
    }

    public static void printKeysInNode(BTreeNode x, int k) {

    }
}
