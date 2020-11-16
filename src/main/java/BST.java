import java.util.LinkedList;
import java.util.Queue;

public class BST<Key extends Comparable<Key>, Value> {
    private Node root;


    public int size(){
        return size(root);
    }

    private int size(Node x){
        if( x == null ){
            return 0;
        }else{
            return x.N;
        }
    }


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


    private Value get(Node node, Key key){
        if( node == null ){
            return null;
        }
        int cmp = key.compareTo(node.key);
        return cmp < 0 ? get(node.left, key) :(cmp == 0 ? node.val : get(node.right, key));
    }


    public void put(Key key, Value value){
        root = put(root, key, value);
    }

    private Node put(Node node, Key key, Value value){
        if( node == null ){
            // 如果查找不到则创建一个新节点
            return new Node(key, value ,1);
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
        node.N = size(node.left) + size(node.right) + 1;
        return node;
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


    public void deleteMin(){
        root = deleteMin(root);
    }

    private Node deleteMin(Node node){
        // 找到最小节点后将父节点的 left 指向自己的 right
        if( node.left == null ){
            return node.right;
        }
        node.left = deleteMin(node.left);
        node.N = 1 + size(node.left) + size(node.right);
        return node;
    }


    public void deleteMax(){
        root = deleteMax(root);
    }

    private Node deleteMax(Node node) {
        // 找到最大的节点后将 父节点的 right 执行当前节点的 left
        if (node.right == null) {
            return node.left;
        }
        node.right = deleteMax(node.right);
        node.N = 1 + size(node.left) + size(node.right);
        return node;
    }


    public void delete(Key key){
        root = delete(root, key);
    }

    /**
     * 对于有俩个子树的节点采用后继节点来代替当前节点的方式进行删除操作
     * @param node
     * @param key
     * @return
     */
    private Node delete(Node node, Key key){
        if ( node == null ){
            return null;
        }
        int cmp = key.compareTo(node.key);
        if( cmp < 0 ){
            node.left =  delete(node.left, key);
        }else if( cmp > 0){
            node.right =  delete(node.right, key);
        }else {
            // 如果当前节点有 0 或 1 个子节点
            if( node.right == null ){
                return node.left;
            }
            if( node.left == null ){
                return node.right;
            }
            // 当前节点有俩个子节点
            // 1 将当前节点保存为 t
            // 2 将 node 只想 min(t.right)
            // 3 将 node.right 指向 deleteMin(node.right)
            // 4 将 node.left 指向 t.left
            // 详见 《算法》 P 274
            Node t = node;
            node = min(t.right);
            node.right = deleteMin(t.right);
            node.left = t.left;
        }
        node.N = 1 + size(node.left) + size(node.right);
        return node;

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

    private class Node{
        private Key key;
        private Value val;
        private Node left, right;
        private int N;

        public Node(Key key, Value val, int n) {
            this.key = key;
            this.val = val;
            N = n;
        }
    }
}
