import com.sun.xml.internal.bind.v2.runtime.RuntimeUtil;

import java.util.*;

public class mytest {
    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            splitAndJoin();
            randomTest();

        }


        AVLTree tree= new AVLTree();
        tree.insert(3,"5");
        tree.insert(2,"2");
        tree.insert(4,"4");
        tree.insert(1,"3");
        tree.insert(5,"1");
        AVLTree tr=new AVLTree();
        tr.insert(1,"2");
        AVLTree [] ttt=tree.split(1);
        ttt[0].join(tr.root,ttt[1]);
        int x=1;
        int [] keys=ttt[0].keysToArray();
        System.out.println(Arrays.toString(keys));

        /*
        System.out.println("*** Test 7 - split ***");
        String abc = "abcdefghijklmnopqrstuvwxyz";
        int n = 1000;
        for (int i = 0; i < n; i++) {
            t1.insert(i + 1,""+i );
        }
        System.out.println("Tree structure: " + toString(t1));
        System.out.println("Consistency check: " + isConsistentTree(t1));
        // System.out.println("Delete " + "(5)" + ": " + t1.delete(5));
        // System.out.println("Tree structure" + toString(t1));

        int k = 800;
        System.out.println("Split by key: " + k);
        AVLTree[] ts = t1.split(k);
        System.out.println("Tree structure of t1: " + toString(ts[0]));
        System.out.println("Consistency check: " + isConsistentTree(ts[0]));

        System.out.println("Tree structure of t2: " + toString(ts[1]));
        System.out.println("Consistency check: " + isConsistentTree(ts[1]));

        AVLTree tt = new AVLTree();
        tt.insert(k, "new");
        AVLTree.IAVLNode d = tt.root;
        System.out.println("join: " + ts[0].join(d, ts[1]));
        System.out.println("Tree structure of t2: " + toString(ts[0]));
        System.out.println("Consistency check: " + isConsistentTree(ts[0]));

        System.out.println("************* FINISH *************");
        System.out.println();
        */

    }

    public static void splitAndJoin()
    {

        AVLTree tree=new AVLTree();
        List<Integer> lst1=new ArrayList<Integer>();
        for (int i=1; i<=10; i++ )
        {
            lst1.add(i);
        }
        System.out.println(lst1);
        Collections.shuffle(lst1);
        for (int i = 0; i < lst1.size() ; i++) {
            tree.insert(lst1.get(i),""+i);
        }
        System.out.println(lst1);
        Random number=new Random();
        int x=number.nextInt(10)+1;
        System.out.println("this is where split happens  "+x);
        AVLTree [] trees= tree.split(x);
        System.out.println("Tree structure: " + toString(trees[0]));
        System.out.println("Consistency check: " + isConsistentTree(trees[1]));
        AVLTree tt = new AVLTree();
        tt.insert(x,"a");
        AVLTree.IAVLNode d = tt.root;

        checkLegal(trees[0]);
        checkLegal(trees[1]);
        trees[0].join(d,trees[1]);
        System.out.println("Tree structure of t2: " + toString(trees[0]));
        System.out.println("Consistency check: " + isConsistentTree(trees[0]));
        checkLegal(trees[0]);
    }

    public static int getRandomInt(Random number){
        int key = number.nextInt(499);
        return key;
    }
    public static boolean checkIfBfs(AVLTree tree)
    {
        int [] keys=tree.keysToArray();
        System.out.println("this is keys in order "+Arrays.toString(keys) );
        for (int i=0;i<keys.length-1;i++)
        {
            if (keys[i]>=keys[i+1])
            {
                return false;
            }
        }

        return true;
    }
    public static void insertRandom(Random number,AVLTree tree){// inserts a random number
        int key = mytest.getRandomInt(number); //get random number under 500
        String info = "String of" + key;
        tree.insert(key, info);
    }
    public static boolean checkLegal(AVLTree tree)// checks if tree is legal
    {


        if (checkIfBfs(tree) == false) {
            System.out.println("Tree is not a legal Bfs Tree");
            return false;
        }


        return true;

    }
    public static int deleteRandom(Random number,AVLTree tree)
    {
        if (tree.empty())
        {
            return 0;
        }
        int [] arrKeys=tree.keysToArray();
        int key1 = number.nextInt(arrKeys.length);
        tree.delete(arrKeys[key1]);
        return arrKeys[key1];
    }
    public static void randomTest()
    {

        AVLTree tree = new AVLTree();
        Random treeSize = new Random();
        Random number = new Random();
        int treeSize1 = treeSize.nextInt(100) + 10;// creats a tree at a random size
        for (int i = 0; i < treeSize1; i++) { //create tree
            insertRandom(number, tree);
            if (!checkLegal(tree)) {
                break;
            }

        }
        for (int k = 0; k < 1000; k++) {

            if (number.nextInt(500) % 2 == 0) {
                insertRandom(number, tree);
            } else {
                deleteRandom(number, tree);

            }
            if (!checkLegal(tree)) {
                return;
            }


        }
   }
    public static boolean isConsistentTree(AVLTree tree) {
        if (tree.getRoot() == null) {
            return true;
        }

        AVLTree.IAVLNode root = tree.getRoot();

        if (root.getParent() != null) {
            System.out.println("Not null parent of root - fail");
            return false;
        }

        if (!isConsistent(root)) {
            System.out.println("consistent fail");
            return false;
        }
                return true;
    }

    public static boolean isConsistent(AVLTree.IAVLNode root) {
        if (root == null)
            return true;

        if (containsLoops(root, new HashSet<Integer>())) {
            System.out.println("Loop");
            return false;
        }
        boolean ret = true;
        if (root.getLeft().isRealNode()) {
            if (root.getLeft().getParent() != root) {
                System.out.println(root.getKey() + " Left son.");
                return false;
            }
            ret = ret && isConsistent(root.getLeft());
        }
        if (root.getRight().isRealNode()) {
            if (root.getRight().getParent() != root) {
                System.out.println(root.getKey() + " Right son.");
                return false;
            }
            ret = ret && isConsistent(root.getRight());
        }

        return ret;
    }

    public static boolean containsLoops(AVLTree.IAVLNode cur, Set<Integer> set) {
        if (cur == null)
            return false;
        if (!cur.isRealNode())
            return false;
        if (set.contains(cur.getKey()))
            return true;
        set.add(cur.getKey());
        boolean leftLoops = containsLoops(cur.getLeft(), set);
        boolean rightLoops = containsLoops(cur.getRight(), set);
        return leftLoops || rightLoops;
    }

    private static String toString(AVLTree t) {
        LinkedList<LinkedList<Integer>> rowsOfTree = new LinkedList<LinkedList<Integer>>();
        if (t.getRoot() == null) {
            return rowsOfTree.toString();
        }

        int rootH = t.getRoot().getHeight();

        for (int i = 0; i <= rootH; i++) {
            rowsOfTree.add(new LinkedList<Integer>());
        }

        fillRows(rowsOfTree, t.root, rootH);

        return rowsOfTree.toString();
    }

    private static void fillRows(LinkedList<LinkedList<Integer>> rowsOfTree, AVLTree.IAVLNode node, int h) {
        if (!node.isRealNode()) {
            return;
        }
        if (node.getLeft().isRealNode()) {
            fillRows(rowsOfTree, node.getLeft(), h);
        }

        rowsOfTree.get(h - node.getHeight()).add(node.getKey());

        if (node.getRight().isRealNode()) {
            fillRows(rowsOfTree, node.getRight(), h);
        }

    }


    public static void deleteTests()
    {
        Random number=new Random();
        AVLTree tree= new AVLTree();
        tree.insert(20,"20");
        tree.insert(10,"10");
        tree.insert(5,"5");
        tree.insert(11,"11");
        tree.insert(15,"15");

        for (int i=0; i<100;i++)
        {
            int key=deleteRandom(number,tree);
            checkLegal(tree);
            tree.insert(key,""+key);
            checkLegal(tree);
        }
    }
}
