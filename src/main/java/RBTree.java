import java.util.LinkedList;
import java.util.Queue;

public class RBTree<Key extends Comparable<Key>, Value> {
    private static final boolean RED = true;
    private static final boolean BLACK = false;
    private Node root;


    public Value get(Key key){
        // return get(root, key);
        // 非递归实现 get
        Node node = root;
        while (node != null){
            int cmp = key.compareTo(node.key);
            if( cmp == 0 ){
                return node.val;
            }else if( cmp  < 0 ){
                node = node.left;
            }else if( cmp > 0 ){
                node = node.right;
            }
        }
        return null;
    }

    public Node getNode(Key key){
        // return get(root, key);
        // 非递归实现 get
        Node node = root;
        while (node != null){
            int cmp = key.compareTo(node.key);
            if( cmp == 0 ){
                return node;
            }else if( cmp  < 0 ){
                node = node.left;
            }else if( cmp > 0 ){
                node = node.right;
            }
        }
        return null;
    }


    private Value get(Node node, Key key){
        if( node == null ){
            return null;
        }
        int cmp = key.compareTo(node.key);
        return cmp < 0 ? get(node.left, key) :(cmp == 0 ? node.val : get(node.right, key));
    }

    public void put(Key key, Value value){
        root = put(root, key, value);
        root.color = BLACK;
    }

    private Node put(Node node, Key key, Value value){
        if( node == null ){
            // 如果查找不到则创建一个新节点
            return new Node(key, value ,1, RED);
        }

        // 如果能查找到则更新节点的 value 值
        int cmp = key.compareTo(node.key);
        if( cmp < 0 ){
            node.left =  put(node.left, key, value);
        }else if( cmp > 0 ){
            node.right =  put(node.right, key, value);
        }else{
            node.val = value;
        }
        // // 红色的右节点
        // if( isRed(node.right) && !isRed(node.left) ) node = rotateLeft(node);
        // // 连续的红节点
        // if( isRed(node.left) && isRed(node.left.left) ) node = rotateRight(node);
        // // 俩个子节点都为红节点
        // if( isRed(node.left) && isRed(node.right) ) flipColor(node);
        // node.N = size(node.left) + size(node.right) + 1;

        return balance(node);
    }

    private Node rotateLeft(Node node){
        Node right = node.right;
        node.right = right.left;
        right.left = node;
        right.color = node.color;
        node.color = RED;
        right.N = node.N;
        node.N = 1 + size(node.left) + size(node.right);
        return right;
    }

    private Node rotateRight(Node node){
        Node left = node.left;
        node.left = left.right;
        left.right = node;
        left.color = node.color;
        node.color = RED;
        left.N = node.N;
        node.N = 1 + size(node.left) + size(node.right);
        return left;
    }


    /**
     * 当前节点的两个子节点都是红节点，将两个红色子节点变为黑色，当前节点变为红色
     *
     * @param node
     */
    private void flipColor(Node node){
        node.color = !node.color;
        node.left.color = !node.left.color;
        node.right.color = !node.right.color;
    }

    public int size(){
        return size(root);
    }

    private int size(Node node){
        if( node == null ){
            return 0;
        }else{
            return node.N;
        }
    }

    /**
     * 红黑节点，被红连接指向的节点为红节点
     */
    private class Node{
        Node left, right;
        int N;
        Key key;
        Value val;
        boolean color;

         Node(Key key, Value val, int n, boolean color) {
            N = n;
            this.key = key;
            this.val = val;
            this.color = color;
        }
    }

    private boolean isRed(Node node){
        if( node == null ){
            return false;
        }
        return node.color == RED;
    }

    public Key min(){
        return min(root).key;
    }

    private Node min(Node node){
        if( node.left == null ){
            return node;
        }
        return min(node.left);
    }

    public Key max(){
        return max(root).key;
    }

    private Node max(Node node){
        if( node.right == null ){
            return node;
        }
        return max(node.right);
    }

    public Key floor(Key key){
        Node node = floor(root, key);
        return node == null ? null : node.key;
    }

    private Node floor(Node node, Key key){
        if( node == null ){
            return null;
        }
        int cmp = key.compareTo(node.key);

        if( cmp == 0){
            return node;
        }else if( cmp < 0 ){
            return floor(node.left, key);
        }

        Node t = floor(node.right, key);
        if( t != null ){
            return t;
        }else{
            return node;
        }
    }

    public Key celling(Key key){
        Node node = celling(root, key);
        return node == null ? null : node.key;
    }

    private Node celling(Node node, Key key){
        if( node == null ){
            return null;
        }
        int cmp = key.compareTo(node.key);
        if ( cmp == 0 ){
            return node;
        }else if( cmp > 1 ){
            return celling(node.right, key);
        }

        Node t = floor(node.left, key);
        if( t != null ){
            return t;
        }else {
            return node;
        }
    }

    public Key select(int k){
        Node node = select(root, k);
        return node == null ? null : node.key;
    }

    /**
     * 查找当前子树中排名为 k 的元素
     * @param node
     * @param k
     * @return
     */
    private Node select(Node node, int k){
        if( node == null ){
            return null;
        }
        int t = size(node.left);
        if( t > k ){
            return select(node.left, k);
        }else if( t < k ){
            return select(node.right, k - t - 1);
        }else {
            return node;
        }
    }

    public int rank(Key key){
        return rank(root, key);
    }

    /**
     * 找到当前子树中 小于 key 的节点数量
     * @param node 当前子树根节点
     * @param key 查找的 key
     * @return
     */
    private int rank(Node node, Key key){

        if( node == null ){
            return 0;
        }
        int cmp = key.compareTo(node.key);
        if( cmp < 0 ){
            return rank(node.left, key);
        }else if( cmp > 0 ){
            // 当前节点 + 左子树 + 右递归
            return 1 + size(node.left) + rank(node.right, key);
        }else {
            return size(node.left);
        }
    }

    public Iterable<Key>  keys(){
        return keys(min(), max());
    }

    public Iterable<Key> keys(Key low, Key high){
        int cmp = low.compareTo(high);
        if( cmp <= 0){
            Queue<Key> queue = new LinkedList<Key>();
            keys(root, queue, low, high);
            return queue;
        }else{
            throw new IllegalArgumentException("low bound great than high bound");
        }
    }

    public String getColor(Key key){
        Node node = getNode(key);
        return isRed(node) ? "RED" : "BLACK";
    }

    private void keys(Node node, Queue<Key> queue, Key low, Key high){
        if( node == null ){
            return;
        }
        int cmpLow = low.compareTo(node.key);
        int cmpHigh = high.compareTo(node.key);
        // 带有上下界的中序遍历
        // 大于下界
        if( cmpLow < 0 ){
            keys(node.left, queue, low,high);
        }

        if( cmpLow <= 0 &&  0 <= cmpHigh ){
            queue.offer(node.key);
        }

        if ( 0 < cmpHigh ){
            keys(node.right, queue, low, high);
        }

    }

    public boolean isEmpty(){
        return root == null;
    }

    private Node moveRedLeft(Node node){
        // 假设节点 node 为红色， node.left 和 node.left.left 都是黑色
        // 将 node.left 或者 node.left 的子节点之一变红
        // 被删除的节点必须存在于一个 3-节点或者 4-节点中
        flipColor(node);
        if (isRed(node.right.left)){
            node.right = rotateRight(node.right);
            node = rotateLeft(node);
            // flipColor(node);
        }
        return node;
    }

    private Node moveRedRight(Node node){
        // 假设节点 node 为红色，node.right 和 node.right.left 都是黑色
        // 将 node.right 或者 node.right 的子节点之一变红
        flipColor(node);
        if (!isRed(node.left.left)){
            node = rotateRight(node);
            // flipColor(node);
        }
        return node;
    }

    private Node balance(Node node){
        // if (isRed(node.right)) node = rotateLeft(node);
        // 红色的右节点
        if( isRed(node.right) && !isRed(node.left) ) {
            node = rotateLeft(node);
        }
        // 连续的红节点
        if( isRed(node.left) && isRed(node.left.left) ) {
            node = rotateRight(node);
        }
        // 俩个子节点都为红节点
        if( isRed(node.left) && isRed(node.right) ) {
            flipColor(node);
        }
        node.N = size(node.left) + size(node.right) + 1;
        return node;
    }

    private Node deleteMin(Node node){
        if (node.left == null ){
            return null;
        }
        if( !isRed(node.left) && !isRed(node.left.left) ){
            node = moveRedLeft(node);
        }
        node.left = deleteMin(node.left);
        return balance(node);
    }

    public void deleteMin(){
        if ( !isRed(root.left) && !isRed(root.right) ){
            root.color = RED;
        }
        root = deleteMin(root);
        if (!isEmpty()){
            root.color = BLACK;
        }
    }

    private Node deleteMax(Node node){
        if( isRed(node.left) ){
            node = rotateRight(node);
        }

        if (node.right == null){
            return null;
        }

        if ( !isRed(node.right) && !isRed(node.right.left) ){
            node = moveRedRight(node);
        }

        node.right = deleteMax(node.right);
        return balance(node);
    }

    public void deleteMax(){
        if (!isRed(root.left) && !isRed(root.right)){
            root.color = RED;
        }
        root = deleteMax(root);
        if(!isEmpty()) {
            root.color = BLACK;
        }
    }

    private Node delete(Node node, Key key){
        // int cmp = key.compareTo(node.key); 不可以这样，因为后面有旋转操作，可能改变了树的结构，
        if( key.compareTo(node.key) < 0){
            // 在左子树中 按照 deleteMin 方法进行删除
            if( !isRed(node.left) && !isRed(node.left.left) ){
                // 要删除的 key 必须处在一个 3-节点 或 4-节点中，即其必须是一个红节点
                node = moveRedLeft(node);
            }
            // 改变节点结构后当前节点的左节点在当前递归中为最小节点
            node.left = delete(node.left, key);
        }else{
            if( isRed(node.left) ){
                node = rotateRight(node);
            }
            if( key.compareTo(node.key) == 0 && node.right == null ){
                return null;
            }
            if( key.compareTo(node.key) == 0 ){
                // 用后继节点（右子树中的最小节点）代替当前节点
                // BST 的删除方法
                // 右子树中最小的节点
                Node minNode = min(node.right);
                node.val = minNode.val;
                node.key = minNode.key;
                node.right = deleteMin(node.right);
            }else{
                // 在右子树中 按照 deleteMax 方法进行删除
                if( !isRed(node.right) && !isRed(node.right.left) ){
                    node = moveRedRight(node);
                }
                node.right = delete(node.right, key);
            }
        }
        return balance(node);
    }

    public void delete(Key key){
        if( !isRed(root.left) && !isRed(root.right) ){
            root.color = RED;
        }
        root = delete(root, key);
        if ( !isEmpty() ) {
            root.color = BLACK;
        }
    }

    public void layerTraversal(){
        Queue<Node> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()){
            int n = queue.size();
            int i = 0;
            while ( i < n ){
                Node node = queue.poll();
                System.out.print(node.key + ": " + node.color + "\t");
                if(node.left != null) {
                    queue.add(node.left);
                }
                if (node.right != null) {
                    queue.add(node.right);
                }
                i += 1;
            }
            System.out.println();
        }
    }

}
