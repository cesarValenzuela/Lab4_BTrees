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
        System.out.println("test");
    }
    public class BTreeNode{
        public int n;          // Actual number of keys on the node
        public boolean isLeaf; // Boolean indicator
        public int[] key;      // Keys stored in the node. They are sorted ion ascending order
        public BTreeNode[] c;  // Children of node. Keys in c[i] are less than key[i] (if it exists)
        // and greater than key[i-1] if it exists

        public  BTreeNode(int t){  // Build empty node
            isLeaf = true;
            key = new int[2*t-1];   // Array sizes are set to maximum possible size
            c = new BTreeNode[2*t];
            n=0;	                  // Number of elements is zero, since node is empty
        }

        public boolean isFull(){
            return n==key.length;
        }

        public void insert(int newKey){
            // Instert new key to current node
            // We make sure that the current node is not full by checking and
            // splitting if necessary before descending to node

            //System.out.println("inserting " + newKey); // Debugging code
            int t = c.length/2;
            int i=n-1;
            if (isLeaf){
                while ((i>=0)&& (newKey<key[i])) { // Shift key greater than newKey to left
                    key[i+1] = key[i];
                    i--;
                }
                n++;                    // Update number of keys in node
                key[i+1]=newKey;        // Insert new key
            }
            else{
                while ((i>=0)&& (newKey<key[i])) {
                    i--;
                }
                int insertChild = i+1;  // Subtree where new key must be inserted
                if (c[insertChild].isFull()){
                    // The root of the subtree where new key will be inserted has to be split
                    // We promote the median of that root to the current node and
                    // update keys and references accordingly

                    // Debugging code
                    // System.out.println("This is the full node we're going to break ");
                    // c[insertChild].printNodes();
                    // System.out.println("going to promote " + c[insertChild].key[T-1]);
                    n++;
                    c[n]=c[n-1];
                    for(int j = n-1;j>insertChild;j--){
                        c[j] =c[j-1];
                        key[j] = key[j-1];
                    }
                    key[insertChild]= c[insertChild].key[t-1];
                    c[insertChild].n = t-1;

                    BTreeNode newNode = new BTreeNode(t);
                    for(int k=0;k<t-1;k++){
                        newNode.c[k] = c[insertChild].c[k+t];
                        newNode.key[k] = c[insertChild].key[k+t];
                    }

                    newNode.c[t-1] = c[insertChild].c[2*t-1];
                    newNode.n=t-1;
                    newNode.isLeaf = c[insertChild].isLeaf;
                    c[insertChild+1]=newNode;

                    if (newKey <key[insertChild]){
                        c[insertChild].insert(newKey);					}
                    else{
                        c[insertChild+1].insert(newKey);				}
                }
                else
                    c[insertChild].insert(newKey);  //No need to split node
            }
        }

        public void print(){
            //Prints all keys in the tree in ascending order
            if (isLeaf){
                for(int i =0; i<n;i++)
                    System.out.print(key[i]+" ");
                //System.out.println();
            }
            else{
                for(int i =0; i<n;i++){
                    c[i].print();
                    System.out.print(key[i]+" ");
                }
                c[n].print();
            }
        }

        public void printNodes(){
            //Prints all keys in the tree, node by node, using preorder
            //It also prints the indicator of whether a node is a leaf
            //Used mostly for debugging purposes
            for(int i =0; i<n;i++)
                System.out.print(key[i]+" ");
            System.out.println(isLeaf);
            if (!isLeaf){
                for(int i =0; i<=n;i++){
                    c[i].printNodes();
                }
            }
        }
    }

    public class BTree{
        public BTreeNode root;
        private int t; //2t is the maximum number of childen a node can have
        private int height;

        public BTree(int t){
            root = new BTreeNode(t);
            this.t = t;
            height = 0;
        }

        public void printHeight(){
            System.out.println("Tree height is "+height);
        }

        public void insert(int newKey){
            if (root.isFull()){//Split root;
                split();
                height++;
            }
            root.insert(newKey);
        }

        public void print(){
            // Wrapper for node print method
            root.print();
        }

        public void printNodes(){
            // Wrapper for node print method
            root.printNodes();
        }

        public void split(){
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
            leftChild.n = t-1;
            rightChild.n = t-1;
            int median = t-1;
            for (int i = 0;i<t-1;i++){
                leftChild.c[i] = root.c[i];
                leftChild.key[i] = root.key[i];
            }
            leftChild.c[median]= root.c[median];
            for (int i = median+1;i<root.n;i++){
                rightChild.c[i-median-1] = root.c[i];
                rightChild.key[i-median-1] = root.key[i];
            }
            rightChild.c[median]=root.c[root.n];
            root.key[0]=root.key[median];
            root.n = 1;
            root.c[0]=leftChild;
            root.c[1]=rightChild;
            root.isLeaf = false;
            // System.out.println("After splitting root");
            // root.printNodes();
        }
    }

    public void printAsc(BTreeNode x){

    }

    public void printDesc(BTreeNode x, int d){

    }

    public Boolean isKeyPresent(BTreeNode x, int k){
        return null;
    }

    public int minElement(BTreeNode x){
        return -1;
    }

    public int minElementDepth(BTreeNode x, int d){
        return -1;
    }

    public int maxElement(BTreeNode x){
        return -1;
    }

    public int maxElementDepth(BTreeNode x, int d){
        return -1;
    }

    public int numNodes(BTreeNode x){
        return -1;
    }

    public int numKeys(BTreeNode x){
        return -1;
    }

    public int numKeysDepth(BTreeNode x, int d){
        return -1;
    }

    public int sumAllKeys(BTreeNode x){
        return -1;
    }

    public int sumAllKeysDepth(BTreeNode x, int d){
        return -1;
    }

    public int numLeaves(BTreeNode x){
        return -1;
    }

    public int numNodesDepth(BTreeNode x, int d){
        return -1;
    }

    public int numFullNodes(BTreeNode x){
        return -1;
    }

    public int depthOfKey(BTreeNode x, int k){
        return -1;
    }

    public int printKeysInNode(BTreeNode x, int k){
        return -1;
    }
}
