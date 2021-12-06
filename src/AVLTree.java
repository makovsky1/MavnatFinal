import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * AVLTree
 *
 * An implementation of a׳� AVL Tree with
 * distinct integer keys and info.
 *
 */

public class AVLTree {
    public IAVLNode root;
    private IAVLNode min;
    private IAVLNode max;

    public AVLTree(){
        this.root = new AVLNode();
    }
    public AVLTree(int k,String i){
        AVLNode node= new AVLNode(k,i);
        this.root = node;
        this.root.setHeight(0);
        this.root.setLeft(new AVLNode());
        this.root.setRight(new AVLNode());
        this.root.getLeft().setParent(this.root);
        this.root.getRight().setParent(this.root);
        this.min = this.root;
        this.max = this.root;
    }
    /**
     * public boolean empty()-
     *
     * Returns true if and only if the tree is empty.
     *
     */
    public boolean empty() {
        if (this.root.getHeight() == -1){
            return true;
        }
        return false;
    }

    /**
     * public String search(int k)
     *
     * Returns the info of an item with key k if it exists in the tree.
     * otherwise, returns null.
     */
    public String search(int k)
    {
        if (this.root.getHeight() == -1){
            return null;
        }


        IAVLNode ret=recSearch(this.root, k);
        if( ret==null)
        {
            return null;
        }
        return ret.getValue();


    }
    private IAVLNode recSearch(IAVLNode node, int key){// returns the node with the key
        if (node.getHeight() == -1){
            return null;
        }
        if (node.getKey() == key){
            return node;
        }
        if (node.getKey() > key){
            return recSearch(node.getLeft(), key);
        }
        else{
            return recSearch(node.getRight(), key);
        }
    }

    /**
     * public int insert(int k, String i)
     *
     * Inserts an item with key k and info i to the AVL tree.
     * The tree must remain valid, i.e. keep its invariants.
     * Returns the number of re-balancing operations, or 0 if no re-balancing operations were necessary.
     * A promotion/rotation counts as one re-balance operation, double-rotation is counted as 2.
     * Returns -1 if an item with key k already exists in the tree.
     */
    private IAVLNode TreePosition(int k,IAVLNode node)
    {
        if (k>node.getKey())
        {
            if (node.getRight().getHeight()==-1)
            {
                return node;
            }
            else
            {
                return TreePosition(k,node.getRight());
            }
        }

        else {
            if (node.getLeft().getHeight()==-1)
            {
                return node;
            }
            else
            {
                return TreePosition(k,node.getLeft());
            }

        }

    }
    private IAVLNode TreePosition1(int key,IAVLNode node){
        if (node == null){
            return null;
        }
        IAVLNode temp = node;
        while(node.isRealNode()){
            temp = node;
            if (node.getKey() == key){
                return node;
            }
            else{
                if (node.getKey() < key){
                    node = node.getRight();
                }
                else{
                    node = node.getLeft();
                }
            }
        }
        return temp;
    }
    public int insert(int k, String i) {
        if (search(k) != null){
            return -1;
        }
        if (this.root.getHeight() == -1)
        {
            this.root=new AVLNode(k,i);
            this.root.setLeft(new AVLNode());
            this.root.setRight(new AVLNode());
            this.root.getLeft().setParent(this.root);
            this.root.getRight().setParent(this.root);
            this.min=this.root;
            this.max=this.root;
            return 0;

        }
        IAVLNode parent=TreePosition(k,this.root);// where to insert the node
        IAVLNode node_to_insert=new AVLNode(k,i);
        if(parent.getKey()>k)
        {
            parent.setLeft(node_to_insert);


        }
        else
        {

            parent.setRight(node_to_insert);


        }
        node_to_insert.setRight(new AVLNode());
        node_to_insert.setLeft(new AVLNode());
        node_to_insert.setParent(parent);
        node_to_insert.getLeft().setParent(node_to_insert);
        node_to_insert.getRight().setParent(node_to_insert);

        UpdateSize(node_to_insert);

        if (k > this.max.getKey()) {
            this.max = node_to_insert;
        }
        if (k < this.min.getKey()) {
            this.min = node_to_insert;
        }


        return Balance(node_to_insert.getParent());
    }


    private void UpdateSize(IAVLNode node)//updates size
    {
        while (node!=null)
        {

            node.setSize(node.getLeft().getSize()+node.getRight().getSize()+1);
            node=node.getParent();
        }

    }
    private int Balance(IAVLNode node)// gets the pointer to the parent of the node that was inserted
    {
        int counter=0;
        int height_parent=node.getHeight();
        int height_left=node.getLeft().getHeight();
        int height_right=node.getRight().getHeight();
        int left_dif=height_parent-height_left;
        int right_dif=height_parent-height_right;
        if (right_dif==0 && left_dif==1||right_dif==1 && left_dif==0)// first check if there is need to promote
        {
            node.setHeight(height_parent+1);
            counter++;
            while (node.getParent()!=null)// node is not the root
            {
                node=node.getParent();
                height_parent=node.getHeight();
                height_left=node.getLeft().getHeight();
                height_right=node.getRight().getHeight();
                left_dif=height_parent-height_left;
                right_dif=height_parent-height_right;
                if (right_dif==0 && left_dif==1||right_dif==1 && left_dif==0)
                {
                    node.setHeight(height_parent+1);
                    counter++;
                }

                else
                {
                    break;
                }

            }

        }
        if (left_dif==0 && right_dif==2)// case 2 or 3  left subtree
        {
            int height_left_grand=node.getLeft().getLeft().getHeight();//grandchild left height
            int height_right_grand=node.getLeft().getRight().getHeight();// grandchild left right height
            int left_left_dif=height_left-height_left_grand;//height dif between left
            int left_right_dif=height_left-height_right_grand;//height dif between left and right grandchild
            if (left_left_dif==1 && left_right_dif==2)
            {
                rotateRight(node);
                node.setHeight(node.getHeight()-1);// demote the right child
                counter=counter+2;
            }

            if (left_left_dif==2 && left_right_dif==1)// case 3 left right rotation
            {
                rotateLeft(node.getLeft());
                rotateRight(node);
                node.setHeight(node.getHeight()-1);// demote the right child
                node.getParent().getLeft().setHeight(node.getParent().getLeft().getHeight()-1);
                node.getParent().setHeight(node.getParent().getHeight()+1);
                counter=counter+5;


            }
            if (left_left_dif==1 &&  left_right_dif==1)// Special case for join
            {
                rotateLeft(node);
                node.getParent().setHeight(node.getParent().getHeight()+1);

            }
        }

        if (left_dif==2 && right_dif==0)// case 2 or 3  right subtree	   	{
        {
            int right_right_grand=node.getRight().getRight().getHeight();//right son of right son
            int height_right_left=node.getRight().getLeft().getHeight();// left son of right son
            int right_right_dif=height_right-right_right_grand;//height dif of right son and right son
            int right_left_dif=height_right-height_right_left;//height dif of right son and left right son

            if (right_right_dif==1 &&  right_left_dif==2)// case 2 left rotation
            {
                rotateLeft(node);
                node.setHeight(node.getHeight()-1);// demote the left child
                counter=counter+2;
            }
            if (right_right_dif==2 &&  right_left_dif==1)// case 3 right left rotation
            {
                rotateRight(node.getRight());
                rotateLeft(node);


                node.setHeight(node.getHeight()-1);// demote the left child
                node.getParent().getRight().setHeight(node.getParent().getRight().getHeight()-1);
                node.getParent().setHeight(node.getParent().getHeight()+1);
                counter=counter+5;

            }
            if (right_right_dif==1 &&  right_left_dif==1)// Special case for join
            {
                rotateRight(node);
                node.getParent().setHeight(node.getParent().getHeight()+1);

            }

        }
        return counter;
    }
    private void rotateRight(IAVLNode node){ // Node is the parent of the firstly promoted node


        node.getLeft().setParent(node.getParent());
        node.setParent(node.getLeft());
        node.setLeft(node.getParent().getRight());
        node.getLeft().setParent(node);
        node.getParent().setRight(node);
        if (node.getParent().getParent()==null)// the rotation was on the root needs to be changed
        {
            this.root=node.getParent();
        }
        else
        {
            if (node.getParent().getParent().getLeft().getKey()==node.getKey())
            {
                node.getParent().getParent().setLeft(node.getParent());
            }
            else
            {
                node.getParent().getParent().setRight(node.getParent());
            }
        }

        node.setSize(node.getLeft().getSize()+node.getRight().getSize()+1);
        node.getParent().setSize(node.getSize()+node.getParent().getLeft().getSize()+1);



    }
    private void rotateLeft(IAVLNode node){// like in powerpoint node is x



        node.getRight().setParent(node.getParent());
        node.setParent(node.getRight());
        node.setRight(node.getParent().getLeft());
        node.getParent().getLeft().setParent(node);
        node.getParent().setLeft(node);
        if (node.getParent().getParent()==null)// the rotation was on the root needs to be changed
        {
            this.root=node.getParent();
        }
        else
        {
            if (node.getParent().getParent().getLeft().getKey()==node.getKey())
            {
                node.getParent().getParent().setLeft(node.getParent());
            }
            else
            {
                node.getParent().getParent().setRight(node.getParent());
            }
        }

        node.setSize(node.getLeft().getSize()+node.getRight().getSize()+1);
        node.getParent().setSize(node.getSize()+node.getParent().getRight().getSize()+1);

    }


    /**
     * public int delete(int k)
     *
     * Deletes an item with key k from the binary tree, if it is there.
     * The tree must remain valid, i.e. keep its invariants.
     * Returns the number of re-balancing operations, or 0 if no re-balancing operations were necessary.
     * A promotion/rotation counts as one re-balance operation, double-rotation is counted as 2.
     * Returns -1 if an item with key k was not found in the tree.
     */
    public int delete(int k)
    {
        IAVLNode node_to_delete=recSearch(this.root,k);
        if(node_to_delete==null)
        {
            return -1;
        }
        if(this.size()==1)//create a null tree
        {
            this.root = new AVLNode();
            this.max=null;
            this.min=null;
            return 0;
        }

        int height_left=node_to_delete.getLeft().getHeight();
        int height_right=node_to_delete.getRight().getHeight();
        IAVLNode parent=node_to_delete.getParent();
        if(parent==null)//node_to_delete is the root
        {
            if(height_left==-1&&height_right!=-1) //there is a right son, no left son
            {
                this.root=node_to_delete.getRight();
                this.root.setParent(null);
                UpdateSize(this.root);

            }

            if(height_left!=-1&&height_right==-1) //there is a left son, no right son		  {
            {
                this.root=node_to_delete.getLeft();
                this.root.setParent(null);
                UpdateSize(this.root);

            }
            if (height_left!=-1 && height_right!=-1)
            {
                IAVLNode replace=succesor(node_to_delete);
                IAVLNode tobalance;
                if (node_to_delete==replace.getParent())// checks if node to start balance from is replace
                {
                    tobalance=replace;

                }
                else
                {
                    tobalance=replace.getParent();

                }


                if(replace.getParent().getKey()>replace.getKey())//replace is a left son, has no left son
                {
                    replace.getParent().setLeft(replace.getRight());
                    replace.getParent().getLeft().setParent(replace.getParent());


                }

                if(replace.getParent().getKey()<replace.getKey())//replace is a right son, has no left son
                {
                    replace.getParent().setRight(replace.getRight());
                    replace.getParent().getRight().setParent(replace.getParent());


                }


                UpdateSize(replace.getParent());//update size
                replace.setHeight(this.root.getHeight());//update height
                replace.getRight().setParent(replace.getParent());
                replace.getLeft().setParent(replace.getParent());
                replace.setParent(null);
                replace.setRight(node_to_delete.getRight());
                replace.setLeft(node_to_delete.getLeft());
                replace.getRight().setParent(replace);
                replace.getLeft().setParent(replace);
                this.root=replace;
                UpdateSize(replace);
                return BalanceDelete(tobalance);

            }
            return BalanceDelete(this.root);

        }



        if(height_left==-1&&height_right==-1)// node is a leaf remove it
        {
            if(node_to_delete.getKey()>parent.getKey())
            {
                parent.setRight(new AVLNode());
                parent.getRight().setParent(parent);
            }

            else
            {
                parent.setLeft(new AVLNode());
                parent.getLeft().setParent(parent);


            }
            UpdateSize(parent);
            return BalanceDelete(parent);

        }

        if(height_left==-1 && height_right!=-1)// unary node with a right son
        {
            if (parent.getKey()>node_to_delete.getKey())// left son
            {
                parent.setLeft(node_to_delete.getRight());
                parent.getLeft().setParent(parent);
            }

            else // right son
            {
                parent.setRight(node_to_delete.getRight());
                parent.getRight().setParent(parent);

            }

            UpdateSize(parent);
            return BalanceDelete(parent);

        }


        if(height_left!=-1 && height_right==-1)// unary node with a left son
        {
            if (parent.getKey()>node_to_delete.getKey())// left son
            {
                parent.setLeft(node_to_delete.getLeft());
                parent.getLeft().setParent(parent);
            }

            else // right son
            {
                parent.setRight(node_to_delete.getLeft());
                parent.getRight().setParent(parent);

            }

            UpdateSize(parent);
            return BalanceDelete(parent);
        }

        if(height_left!=-1 && height_right!=-1)// node with a right and left son
        {
            IAVLNode tobalance;
            IAVLNode replace=succesor(node_to_delete);
            if (node_to_delete==replace.getParent())// checks if node to start balance from is replace
            {
                tobalance=replace;

            }
            else
            {
                tobalance=replace.getParent();

            }


            if(replace.getParent().getKey()>replace.getKey())//replace is a left son, has no left son
            {
                replace.getParent().setLeft(replace.getRight());
                replace.getParent().getLeft().setParent(replace.getParent());
            }

            if(replace.getParent().getKey()<replace.getKey())//replace is a right son, has no left son
            {
                replace.getParent().setRight(replace.getRight());
                replace.getParent().getRight().setParent(replace.getParent());

            }
            replace.setHeight(node_to_delete.getHeight());//update height
            replace.setParent(node_to_delete.getParent());
            replace.setRight(node_to_delete.getRight());
            replace.setLeft(node_to_delete.getLeft());
            replace.getRight().setParent(replace);
            replace.getLeft().setParent(replace);
            if (replace.getParent().getKey()>replace.getKey())// left son of the parent
            {
                replace.getParent().setLeft(replace);
            }
            else
            {
                replace.getParent().setRight(replace);

            }
            UpdateSize(tobalance);//update size
            return BalanceDelete(tobalance);
        }


        return 0;// should never get here

    }
    private int BalanceDelete(IAVLNode node)// node to start the balance from
    {
        this.min=minrec(this.root);// update minimum after delete
        this.max=maxrec(this.root);// update maximum after delete
        int counter=0;
        do
        {
            int height=node.getHeight();
            int height_left=node.getLeft().getHeight();
            int height_right=node.getRight().getHeight();
            int H_left_dif=height-height_left;
            int H_right_dif=height-height_right;
            if((H_left_dif==1 && H_right_dif==1)||(H_left_dif==2 && H_right_dif==1)||(H_left_dif==1 && H_right_dif==2))// checks if tree is balanced
            {
                return counter;
            }

            if (H_left_dif==2 && H_right_dif==2)
            {
                node.setHeight(node.getHeight()-1);//demote case 1
                counter++;
            }
            if (H_left_dif==3 && H_right_dif==1)// case 2,3,4 right subtree
            {
                int height_right_right=node.getRight().getRight().getHeight();
                int height_right_left=node.getRight().getLeft().getHeight();
                int right_right_dif=height_right-height_right_right;
                int right_left_dif=height_right-height_right_left;
                if(right_right_dif==1 && right_left_dif==1)// case 2 left rotation
                {
                    rotateLeft(node);
                    node.setHeight(node.getHeight()-1);// demote node
                    node.getParent().setHeight(node.getParent().getHeight()+1);// promote parent of node
                    counter=counter+3;
                    node=node.getParent();//rotation moves node down
                }

                if(right_right_dif==1 && right_left_dif==2)// case 3 left rotation
                {
                    rotateLeft(node);
                    node.setHeight(node.getHeight()-2);// demote node twice
                    counter=counter+2;
                    node=node.getParent();//rotation moves node down

                }
                if(right_right_dif==2 && right_left_dif==1) // case 4 right left rotation
                {
                    rotateRight(node.getRight());
                    rotateLeft(node);
                    node.setHeight(node.getHeight()-2);// demote node twice
                    node=node.getParent();// rotation moves node down
                    node.setHeight(node.getHeight()+1);// promote a
                    node.getRight().setHeight(node.getRight().getHeight()-1);
                    counter=counter+5;

                }


            }
            if (H_left_dif==1 && H_right_dif==3)// cases 2,3,4 left subtree
            {
                int height_left_left=node.getLeft().getLeft().getHeight();
                int height_left_right=node.getLeft().getRight().getHeight();
                int left_left_dif=height_left-height_left_left;
                int left_right_dif=height_left-height_left_right;
                if(left_left_dif==1 && left_right_dif==1)// case 2 right rotation
                {
                    rotateRight(node);
                    node.setHeight(node.getHeight()-1);// demote node
                    node.getParent().setHeight(node.getParent().getHeight()+1);// promote parent of node
                    counter=counter+3;
                    node=node.getParent();//rotation moves node down

                }

                if(left_left_dif==1 && left_right_dif==2)// case 3 right rotation
                {
                    rotateRight(node);
                    node.setHeight(node.getHeight()-2);// demote node twice
                    counter=counter+2;
                    node=node.getParent();//rotation moves node down


                }

                if(left_left_dif==2 && left_right_dif==1)// case 4 left Right rotation
                {

                    rotateLeft(node.getLeft());
                    rotateRight(node);
                    node.setHeight(node.getHeight()-2);// demote node twice
                    node=node.getParent();// rotation moves node down
                    node.setHeight(node.getHeight()+1);// promote a
                    node.getLeft().setHeight(node.getLeft().getHeight()-1);
                    counter=counter+5;


                }




            }


            node=node.getParent();
        }while(node!=null);
        return counter;
    }

    /**
     * public String min()
     *
     * Returns the info of the item with the smallest key in the tree,
     * or null if the tree is empty.
     */
    public String min()
    {
        if(this.empty()==true)
        {
            return null;
        }
        return minrec(this.root).getValue(); //
    }
    private IAVLNode minrec(IAVLNode node)// return minimum node
    {
        if (this.empty())
        {
            return null;
        }
        if (node.getLeft().getHeight()!=-1)
        {
            return minrec(node.getLeft());
        }
        return node;

    }

    /**
     * public String max()
     *
     * Returns the info of the item with the largest key in the tree,
     * or null if the tree is empty.
     */
    public String max()
    {
        if(this.empty()==true)
        {
            return null;
        }
        return maxrec(this.root).getValue();
    }
    private IAVLNode maxrec(IAVLNode node)// return minimum node
    {
        if (this.empty())
        {
            return null;
        }
        if (node.getRight().getHeight()!=-1)
        {
            return maxrec(node.getRight());
        }
        return node;

    }

    /**
     * public int[] keysToArray()
     *
     * Returns a sorted array which contains all keys in the tree,
     * or an empty array if the tree is empty.
     */
    public int[] keysToArray()
    {
        int [] keys= new int [this.root.getSize()];
        int [] index= {0};
        KeysToArrayRec(keys,this.root,index);
        return keys;

    }
    private void KeysToArrayRec(int [] keys,IAVLNode node,int [] index )
    {
        if (node.getHeight()!=-1)
        {
            KeysToArrayRec(keys,node.getLeft(),index);
            keys[index[0]]=node.getKey();
            index[0]++;
            KeysToArrayRec(keys,node.getRight(),index);
        }
    }
    /**
     * public String[] infoToArray()
     *
     * Returns an array which contains all info in the tree,
     * sorted by their respective keys,
     * or an empty array if the tree is empty.
     */
    public String[] infoToArray()
    {

        String [] infos= new String [this.root.getSize()];
        int [] index= {0};
        infoToArrayRec(infos,this.root,index);
        return infos;



    }
    private void infoToArrayRec(String [] infos,IAVLNode node,int [] index)
    {
        if (node.getHeight()!=-1)
        {
            infoToArrayRec(infos,node.getLeft(),index);
            infos[index[0]]=node.getValue();
            index[0]++;
            infoToArrayRec(infos,node.getRight(),index);
        }
    }
    /**
     * public int size()
     *
     * Returns the number of nodes in the tree.
     */
    public int size()
    {
        return this.root.getSize(); //size of tree
    }

    private IAVLNode succesor(IAVLNode node)
    {
        if(node.getRight().getHeight()!=-1)
        {
            return minrec(node.getRight());
        }
        IAVLNode parent=node.getParent();
        while(parent!=null && node==parent.getRight())
        {
            node=parent;
            parent=node.getParent();
        }

        return parent;
    }
    private IAVLNode predecessor(IAVLNode node)
    {
        if(node.getLeft().getHeight()!=-1)
        {
            return minrec(node.getLeft());
        }
        IAVLNode parent=node.getParent();
        while(parent!=null && node==parent.getLeft())
        {
            node=parent;
            parent=node.getParent();
        }
        return parent;
    }

    /**
     * public int getRoot()
     *
     * Returns the root AVL node, or null if the tree is empty
     */
    public IAVLNode getRoot()
    {
        if(this.empty()==true)
        {
            return null;
        }
        return this.root;
    }

    /**
     * public AVLTree[] split(int x)
     *
     * splits the tree into 2 trees according to the key x.
     * Returns an array [t1, t2] with two AVL trees. keys(t1) < x < keys(t2).
     *
     * precondition: search(x) != null (i.e. you can also assume that the tree is not empty)
     * postcondition: none
     */


    public AVLTree[] split(int x)
    {
        IAVLNode node = TreePosition1(x, this.root);
        int key=x;
        AVLTree smallKeys = new AVLTree();
        AVLTree bigKeys = new AVLTree();
        AVLTree smallKeysHelper = new AVLTree(); //smallKeys tree helper
        AVLTree bigKeysHelper = new AVLTree(); //bigKeys tree helper

        if (node.getLeft().getKey() != -1){
            smallKeys.root = node.getLeft();
            node.getLeft().setParent(null);
        }
        if (node.getRight().getKey() != -1){
            bigKeys.root = node.getRight();
            node.getRight().setParent(null);
        }

        if (node.getParent()==null)
        {
            AVLTree[] finalTree = new AVLTree[2];
            updateMax(smallKeys);
            updateMin(smallKeys);
            updateMax(bigKeys);
            updateMin(bigKeys);
            finalTree[0]=smallKeys;
            finalTree[1]=bigKeys;
            return finalTree;
        }
        node=node.getParent();
        while (node!= null){
            IAVLNode nextNode = node.getParent();
            if (node.getKey() <key){
                if(node.getLeft().isRealNode()==false)
                {
                    continue;
                }
                smallKeysHelper.root = node.getLeft();
                smallKeysHelper.root.setParent(null);
                smallKeysHelper.root.setSize(node.getLeft().getSize());
                smallKeys.join(node,smallKeysHelper);
            }
            else{
                if(node.getRight().isRealNode()==false)
                {
                    continue;
                }
                bigKeysHelper.root = node.getRight();
                bigKeysHelper.root.setParent(null);
                bigKeysHelper.root.setSize(node.getRight().getSize()) ;
                bigKeys.join(node, bigKeysHelper);


            }
            key=node.getKey();
            node = nextNode;

        }
        AVLTree[] finalTree = new AVLTree[2];
        updateMax(smallKeys);
        updateMin(smallKeys);
        updateMax(bigKeys);
        updateMin(bigKeys);
        finalTree[0] = smallKeys;
        finalTree[1] = bigKeys;

        return finalTree;
    }

    /**
     * public int join(IAVLNode x, AVLTree t)
     *
     * joins t and x with the tree.
     * Returns the complexity of the operation (|tree.rank - t.rank| + 1).
     *
     * precondition: keys(t) < x < keys() or keys(t) > x > keys(). t/tree might be empty (rank = -1).
     * postcondition: none
     */
    public int join(IAVLNode x, AVLTree t)
    {
        int n = Math.abs(t.root.getHeight() - this.root.getHeight()) + 1;
        updateMax(this);
        updateMin(this);
        updateMin(t);
        updateMax(t);
        if (t.root.getKey() == -1) {//t is empty
            this.insert(x.getKey(),x.getValue());
            return n;
        }
        if (this.root.getKey() == -1) { //this is empty
            t.insert(x.getKey(), x.getValue());
            this.root = t.root;
            return n;
        }
        if (this.root.getHeight() >= t.root.getHeight()){
            if (this.root.getKey() > t.root.getKey()){
                IAVLNode node = this.root;
                while (node.getLeft().getHeight() > t.root.getHeight()){
                    node = node.getLeft();
                }
                IAVLNode leftSon = node.getLeft();
                node.setLeft(x);
                x.setParent(node);
                x.setRight(leftSon);
                node.getLeft().setParent(node);
                x.getRight().setParent(x);
                x.setLeft(t.root);
                t.root.setParent(x);
            }
            else{
                IAVLNode node = this.root;
                while (node.getRight().getHeight() > t.root.getHeight()){
                    node = node.getRight();
                }
                IAVLNode rightSon = node.getRight();
                node.setRight(x);
                x.setParent(node);
                x.setLeft(rightSon);
                node.getRight().setParent(node);
                x.getLeft().setParent(x);
                x.setRight(t.root);
                t.root.setParent(x);
            }
        }
        else{
            if (this.root.getKey() < t.root.getKey()){
                IAVLNode node = t.root;
                while (node.getLeft().getHeight() > this.root.getHeight()){
                    node = node.getLeft();
                }
                IAVLNode leftSon = node.getLeft();
                node.setLeft(x);
                x.setParent(node);
                x.setRight(leftSon);
                x.getRight().setParent(x);
                node.getLeft().setParent(node);
                x.setLeft(this.root);
                this.root.setParent(x);
            }
            else{
                IAVLNode node = t.root;
                while (node.getRight().getHeight() > this.root.getHeight()){
                    node = node.getRight();
                }
                IAVLNode rightSon = node.getRight();
                node.setRight(x);
                x.setParent(node);
                x.setLeft(rightSon);
                node.getRight().setParent(node);
                x.getLeft().setParent(x);
                x.setRight(this.root);
                this.root.setParent(x);
            }
            this.root = t.root;
            UpdateSize(x);
        }
        Balance(x.getParent());
        Balance(x);
        updateMin(this);
        updateMax(this);
        return n;
    }
    private IAVLNode findMax(AVLTree tree){
        IAVLNode node = tree.root;
        while (node.getRight().isRealNode()){
            node = node.getRight();
        }
        return node;
    }
    private void updateMax(AVLTree tree){
        if (findMax(tree) != null){
            tree.max = findMax(tree);
        }
        else{
            tree.max = null;
        }
    }
    private IAVLNode findMin(AVLTree tree){
        IAVLNode node = tree.root;
        while (node.getLeft().isRealNode()){
            node = node.getLeft();
        }
        return node;
    }
    private void updateMin(AVLTree tree){
        if (findMin(tree) != null) {
            tree.min = findMin(tree);
        }
        else{
            tree.min = null;
        }


    }
    //prints the tree level by level until the last virtual node
    // V - virtual node
    //N - null
    public void treePrinter(){
        ArrayList<IAVLNode> currList = new ArrayList<>();
        currList.add(root);
        int level = root.getHeight();

        while (currList.size() > 0) {

            String space = "  ";

            for (int i = 0; i < level; i++) {
                space = space + space;
            }
            level--;
            System.out.print(space);


            ArrayList<IAVLNode> childrenList = new ArrayList<>();

            for (IAVLNode node: currList) {
                if (node != null && node.isRealNode()) {
                    System.out.print(node.getValue() + space);
                    childrenList.add(node.getLeft());
                    childrenList.add(node.getRight());
                }
                else if (node != null) {
                    System.out.print("V");
                    System.out.print(space);
                    childrenList.add(null);
                    childrenList.add(null);

                }
                else { //node == null
                    System.out.print("N");
                    System.out.print(space);

                    childrenList.add(null);
                    childrenList.add(null);
                }

            }
            boolean onlyNull = true;

            for (int i = 0; i < childrenList.size(); i++) {
                if (childrenList.get(i) != null) {
                    onlyNull = false;
                    break;
                }
            }
            if (onlyNull) {
                break;
            }
            currList = childrenList;
            childrenList = new ArrayList<>();

            System.out.println();
            System.out.println();
        }
    }
    public boolean checkB()// checks if tree is balanced
    {
        return checkButil(this.root);
    }
    public boolean checkButil(IAVLNode node)
    {
        if (node.getHeight()==-1 && node.getKey()==-1)
        {
            return true;
        }
        if (node.getHeight()==-1)
        {
            return false;
        }
        int height=node.getHeight();
        int L_height=node.getLeft().getHeight();
        int R_height=node.getRight().getHeight();
        int L_dif=height-L_height;
        int R_dif=height-R_height;
        if(L_dif!=1 &L_dif!=2)
        {
            return false;
        }
        if (R_dif!=1 & R_dif!=2)
        {
            return false;
        }
        if (R_dif==2 & L_dif!=1)
        {
            return false;
        }
        if (R_dif!=1 &L_dif==2)
        {
            return false;
        }
        return checkButil(node.getLeft())&& checkButil(node.getRight());
    }
    public boolean checkSize()
    {
        return checkSizeUtil(this.root);

    }
    private boolean checkSizeUtil(IAVLNode node)
    {
        if (!node.isRealNode())
        {
            return true;
        }
        int size=node.getSize();
        int L_size=node.getLeft().getSize();
        int R_size=node.getRight().getSize();
        if(size!=L_size+R_size+1)
        {
            return false;
        }
        return checkSizeUtil(node.getRight())&&checkButil(node.getRight());
    }
    public boolean checkMaxMin()// checks if min and max are right
    {
        if (this.max!=maxrec(this.root))
        {
            return false;
        }
        if(this.min!=minrec(this.root))
        {
            return false;
        }
        return true;
    }

    /**
     * public interface IAVLNode
     * ! Do not delete or modify this - otherwise all tests will fail !
     */
    public interface IAVLNode{
        public int getKey(); // Returns node's key (for virtual node return -1).
        public String getValue(); // Returns node's value [info], for virtual node returns null.
        public void setLeft(IAVLNode node); // Sets left child.
        public IAVLNode getLeft(); // Returns left child, if there is no left child returns null.
        public void setRight(IAVLNode node); // Sets right child.
        public IAVLNode getRight(); // Returns right child, if there is no right child return null.
        public void setParent(IAVLNode node); // Sets parent.
        public IAVLNode getParent(); // Returns the parent, if there is no parent return null.
        public boolean isRealNode(); // Returns True if this is a non-virtual AVL node.
        public void setHeight(int height); // Sets the height of the node.
        public int getHeight(); // Returns the height of the node (-1 for virtual nodes).
        public int getSize(); // return the size of the subtree of the node without the node
        public void setSize(int size); // changes the size field in the node
    }

    /**
     * public class AVLNode
     *
     * If you wish to implement classes other than AVLTree
     * (for example AVLNode), do it in this file, not in another file.
     *
     * This class can and MUST be modified (It must implement IAVLNode).
     */
    public class AVLNode implements IAVLNode{
        private int key;
        private String value;
        private int height;
        private int size;
        private IAVLNode parent;
        private IAVLNode right;
        private IAVLNode left;
        public AVLNode(){
            this.key = -1;
            this.value = null;
            this.parent = null;
            this.right = null;
            this.left = null;
            this.height = -1;
            this.size=0;
        }
        public AVLNode(int key, String value){
            this.key=key;
            this.value=value;
            this.parent = null;
            this.right = null;
            this.left = null;
            this.height = 0;//default height for a real node
            this.size=1;
        }
        public int getKey()
        {
            return this.key;
        }
        public String getValue()
        {
            return this.value;
        }
        public void setLeft(IAVLNode node)
        {
            this.left = node;
        }
        public IAVLNode getLeft()
        {
            return this.left;
        }
        public void setRight(IAVLNode node)
        {
            this.right = node;
        }
        public IAVLNode getRight()
        {

            return this.right;

        }
        public void setParent(IAVLNode node)
        {
            this.parent = node;
        }
        public IAVLNode getParent()
        {
            if (this.parent != null){
                return this.parent;
            }
            return null;
        }
        public boolean isRealNode()
        {
            if (this.height == -1){
                return false;
            }
            return true;
        }
        public void setHeight(int height)
        {
            this.height = height;
        }
        public int getHeight()
        {
            return this.height;
        }
        public void setSize(int size)
        {
            this.size=size;
        }

        public int getSize()
        {
            return this.size;
        }
    }

}

