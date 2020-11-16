import java.util.*;
/**
 * 红黑树
 * @author 小锅巴
 */
public class RBT {
    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private TreeNode root;

    private class TreeNode{
        String key;
        int value;
        TreeNode left, right;//左右链接
        boolean color;//节点的颜色

        TreeNode(String key, int value, boolean color){
            this.key = key;
            this.value = value;
            this.color = color;//新建节点，每次都是红色的
        }
    }

    private boolean isRed(TreeNode node){
        if(node == null) {
            return false;
        }
        return node.color == RED;
    }

    //左旋转，由于我先学得AVL，先入为主，我以维斯版的来命名，与algs4的相反
    private TreeNode rotateWithLeft(TreeNode node){
        TreeNode temp = node.left;
        //完成旋转
        node.left = temp.right;
        temp.right = node;
        //和AVL相比，旋转后要变换颜色，
        temp.color = node.color;
        node.color = RED;//被旋转的节点到了子树，所以肯定是设置被旋转的节点为红色节点
        return temp;
    }

    //右旋转
    private TreeNode rotateWithRight(TreeNode node){
        TreeNode temp = node.right;

        node.right = temp.left;
        temp.left = node;

        temp.color = node.color;
        node.color = RED;

        return temp;
    }

    //颜色转换的条件是左右子节点均为红节点，由红节点的定义，那么当前节点的的孩子均不会为null，不会出现空指针异常
    private void flipColors(TreeNode node){
//      node.color = RED;
//      node.left.color = BLACK;
//      node.right.color = BLACK;
        node.color = !node.color;
        node.left.color = !node.left.color;
        node.right.color = !node.right.color;
    }

    //修复红色右节点以及一个节点和两条红链接相连
    private TreeNode fixUp(TreeNode node) {
        if (!isRed(node.left) && isRed(node.right)) {
            node = rotateWithRight(node);
        }
        if (isRed(node.left) && isRed(node.left.left)) {
            node = rotateWithLeft(node);
        }
        if (isRed(node.left) && isRed(node.right)) {
            flipColors(node);
        }
        return node;
    }

    public int get(String key){
        return get(root, key);
    }
    private int get(TreeNode node, String key){
        if(node == null) {
            return -1;
        }
        int cmp = key.compareTo(node.key);
        if(cmp < 0) {
            return get(node.left, key);
        } else if(cmp > 0) {
            return get(node.right, key);
        } else {
            return node.value;
        }
    }
    public void insert(String key, int value){
        root = insert(root, key, value);
        root.color = BLACK;//根节点都是黑色
    }
    private TreeNode insert(TreeNode node, String key, int value){
        if( node == null) {
            return new TreeNode(key, value, RED);
        }

        int cmp = key.compareTo(node.key);
        if(cmp < 0) {
            node.left = insert(node.left, key, value);
        } else if(cmp > 0) {
            node.right = insert(node.right, key, value);
        } else {
            node.value = value;
        }

        //旋转
//      if(!isRed(node.left) && isRed(node.right))//左链接为黑，右链接为红，则右旋转
//          node = rotateWithRight(node);
//      if(isRed(node.left.left) && isRed(node.left))//左链接为红，左孩子的左链接也为红，则左旋转
//          node = rotateWithLeft(node);
//      if(isRed(node.left) && isRed(node.right))//左右链接均为红，调整颜色
//          flipColors(node);
//      return node;
        return fixUp(node);
    }

    //找出最小键
    private TreeNode min(TreeNode node){
        while(node.left != null) {
            node = node.left;
        }
        return node;
    }

    //删除最小键
    private TreeNode deleteMin(TreeNode node) {
        //递归结束条件，到达左边界
        if (node.left == null) {
            return null;
        }

        // 保证node或node.left为红节点，注意是从父节点入手
        // 要删除的 key 必须存在于一个 3-节点或一个 4-节点中
        if (!isRed(node.left) && !isRed(node.left.left)) {
            node = moveRedLeft(node);
        }

        //递归地在左子树中删除
        node.left = deleteMin(node.left);
        //删除后修复红色右节点（链接）
        return fixUp(node);
    }

    //对于当前节点，从其兄弟节点（父节点的右节点）中借一个节点
    private TreeNode moveRedLeft(TreeNode node) {
        //先颜色转换
        flipColors(node);
        if (isRed(node.right.left)) {
            //判断其兄弟节点的左孩子是否为红，若是，对当前节点的父节点进行左-右双旋转且颜色要转换
            node.right = rotateWithLeft(node.right);
            node = rotateWithRight(node);
            flipColors(node);
        }
        return node;
    }

    //找出最大键
    private TreeNode max(TreeNode node){
        while(node.right != null)
            node = node.right;
        return node;
    }

    //删除最大键
    private TreeNode deleteMax(TreeNode node){
        // 使树出现红色右链接
        if(isRed(node.left))
            node = rotateWithLeft(node);
        //注意上下两者的顺序不能错
        //递归结束条件，达到了右边界
        if(node.right == null)
            return null;

        //保证node或node.right为红节点，注意也是从父节点入手
        if(!isRed(node.right) && !isRed(node.right.left))
            //因为3节点是用红节点来模拟的，红节点不可能是右孩子，所以不可能是h.right.right，通过上面的一次左旋转，可以获得与h.right.right同样的效果
            node = moveRedRight(node);
        //递归在右子树中删除
        node.right = deleteMax(node.right);
        return fixUp(node);
    }

    //对于当前节点，从其兄弟节点（当前节点父节点左孩子）中借一个节点
    private TreeNode moveRedRight(TreeNode node){
        //颜色转换
        flipColors(node);
        if(isRed(node.left.left)){
            //判断其兄弟节点的左孩子是否为红，若是，对当前节点的父节点进行左旋转且颜色要转换
            //每次都是判断兄弟节点的左孩子，因为在借之前，在其父节点下面的节点只能有红色左链接
            node = rotateWithLeft(node);
            flipColors(node);
        }
        return node;
    }

    public void delete(String key){
        if (key == null) throw new NullPointerException("argument to delete() is null");
        if (!isRed(root.left) && !isRed(root.right))
            root.color = RED;
        root = delete(root, key);
        root.color = BLACK;//确保删除后头节点是黑色，因为可能把头节点变红，
    }
    private TreeNode delete(TreeNode node, String key){
//      int cmp = key.compareTo(node.key);//不可以这样，因为后面有旋转操作，可能改变了树的结构，
        if(key.compareTo(node.key) < 0){//递归在左子树中删除
            if(!isRed(node.left) && !isRed(node.left.left))//站在2-3树的角度看，确保删除的节点不为2节点
            {
                node = moveRedLeft(node);
            }
            node.left = delete(node.left, key);
        } else{
            //确保在右子树中能出现红色右孩子
            if(isRed(node.left)) {
                node = rotateWithLeft(node);
            }

            //待删除的节点在树底
            if(key.compareTo(node.key) == 0 && node.right == null) {
                return null;
            }
            //待删除的节点不在树底
            if(key.compareTo(node.key) == 0){
                TreeNode temp = min(node.right);
                node.key = temp.key;
                node.value = temp.value;
                node.right = deleteMin(node.right);
            } else{//递归在右子树中删除
                //若把 递归在右子树中删除 放在  等于  之前，node.right.left可能会出现空指针异常，因为缺少递归结束判断条件
                if(!isRed(node.right) && !isRed(node.right.left))//确保删除的节点不为2节点，
                {
                    node = moveRedRight(node);
                }
                node.right = delete(node.right, key);
            }
        }
        return fixUp(node);//修复红色右节点
    }

    //层序遍历所有节点的键
    private void layerTraversal (TreeNode node){
        Queue<TreeNode> s = new LinkedList<>();
        s.add(node);
        TreeNode curNode;
        TreeNode nlast = null;
        TreeNode last = node;
        while(!s.isEmpty()){
            curNode = s.poll();
            System.out.print(curNode.key+" ");
            if(curNode.left != null){
                nlast = curNode.left;
                s.add(curNode.left);
            }
            if(curNode.right != null){
                nlast = curNode.right;
                s.add(curNode.right);
            }
            if(curNode == last){
                System.out.println();
                last = nlast;
            }
        }
    }

    //层序遍历节点的颜色（是否为红）
    private void layerTraversalColor (TreeNode node){
        Queue<TreeNode> s = new LinkedList<>();
        s.add(node);
        TreeNode curNode;
        TreeNode nlast = null;
        TreeNode last = node;
        while(!s.isEmpty()){
            curNode = s.poll();
            System.out.print(curNode.color+" ");
            if(curNode.left != null){
                nlast = curNode.left;
                s.add(curNode.left);
            }
            if(curNode.right != null){
                nlast = curNode.right;
                s.add(curNode.right);
            }
            if(curNode == last){
                System.out.println();
                last = nlast;
            }
        }
    }

    //先序遍历所有节点的键，
    private void preOrderTraversal(TreeNode node){
        Stack<TreeNode> s = new Stack<>();
        TreeNode curNode = null;
        s.push(node);
        while(!s.isEmpty()){
            curNode = s.pop();
            System.out.print(curNode.key+" ");
            if(curNode.right != null)
                s.push(curNode.right);
            if(curNode.left != null)
                s.push(curNode.left);
        }
    }

    public static void main(String[] args) {
        RBT rbt = new RBT();
        // System.out.print("请输入节点个数：");
        Scanner scan = new Scanner(System.in);
        // int num = s.nextInt();
        // System.out.println("请依次输入"+num+"个字母");
        // for (int i = 1; i <= num; i++){
        //     String value = s.next();
        //     rbt.insert(value, i);
        // }
        String s = "SEARCXPL";
        for (char c : s.toCharArray()) {
            rbt.insert(String.valueOf(c), 1);
        }


        System.out.println("节点颜色是否为红：");
        rbt.layerTraversalColor(rbt.root);
        System.out.println();

        System.out.println("层序遍历");
        rbt.layerTraversal(rbt.root);
        System.out.println();

        System.out.println("先序遍历");
        rbt.preOrderTraversal(rbt.root);
        System.out.println();


//      rbt.deleteMin();
//      System.out.println("测试删除最小键：");
//      System.out.println("节点颜色是否为红：");
//      rbt.layerTraversalColor(rbt.root);
//      System.out.println();
//
//      System.out.println("层序遍历");
//      rbt.layerTraversal(rbt.root);
//      System.out.println();
//
//      System.out.println("先序遍历");
//      rbt.preOrderTraversal(rbt.root);
//      System.out.println();
//
//
//      rbt.deleteMax();
//      System.out.println("测试删除最大键");
//      System.out.println("节点颜色是否为红：");
//      rbt.layerTraversalColor(rbt.root);
//      System.out.println();
//
//      System.out.println("层序遍历");
//      rbt.layerTraversal(rbt.root);
//      System.out.println();
//
//      System.out.println("先序遍历");
//      rbt.preOrderTraversal(rbt.root);
//      System.out.println();

        System.out.println("\n"+"测试删除任意键");
        System.out.println("请输入要删除的键:");
        String key = scan.next();
        rbt.delete(key);
        System.out.println("节点颜色是否为红：");
        rbt.layerTraversalColor(rbt.root);
        System.out.println();

        System.out.println("层序遍历");
        rbt.layerTraversal(rbt.root);
        System.out.println();

        System.out.println("先序遍历");
        rbt.preOrderTraversal(rbt.root);
        System.out.println();
    }
}

