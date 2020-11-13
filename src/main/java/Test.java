public class Test {
    public static void main(String[] args) {
        RBTree<Integer, Integer> rbTree = new RBTree<>();

        for(int i = 0; i <= 58; i++ ){
            rbTree.put(i, i);
        }
        // bst.delete(3);
        // rbTree.put("S",1);
        // rbTree.put("E",2);
        // rbTree.put("A",3);
        // rbTree.put("R",4);
        // rbTree.put("C",5);
        // rbTree.put("H",6);
        // rbTree.put("X",7);
        // rbTree.put("M",8);
        // rbTree.put("P",9);
        // rbTree.put("L",10);


        // rbTree.deleteMax();
        // rbTree.deleteMax();
        // System.out.println(rbTree.max());
        // rbTree.deleteMin();
        // rbTree.deleteMin();
        // rbTree.deleteMin();
        // rbTree.delete("M");
        // rbTree.delete("C");
        // rbTree.delete("R");
        // rbTree.delete("E");
        // System.out.println(rbTree.min());
        System.out.println(rbTree.size());
        rbTree.layerTraversal();
        // System.out.println(rbTree.min());
        // for (String key : rbTree.keys()) {
        //     System.out.println(key + " color: " + rbTree.getColor(key));
        // }


    }
}
