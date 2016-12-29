import static org.junit.Assert.*;

import org.junit.Test;


/**
 * Created by romanmayer on 24/12/16.
 */

public class AVLTreeTest {
    private AVLTree tree = new AVLTree();

    private void insert(Integer... integers) {
        for (Integer i : integers) {
            tree.insert(i);
        }
    }

    @Test
    public void simpleTestMin() {
        insert(16,24,36,19,44,28,61,74,83,64,52,65,86,93,88);
        //tree.insert(16);
        System.out.println(tree.min());
        System.out.println(tree.max());
        assertTrue(4 == 4);
        assertTrue(tree.min() == 16);
        assertTrue(tree.max() == 93);
        assertTrue(tree.predecessor(16) == 0);
        assertTrue(tree.predecessor(24) == 19);
        assertTrue(tree.predecessor(36) == 24);
    }

    @Test
    public void myTest() {
        insert(6,4,3,8,9,1,2,5,7,10);
        tree.inOrder();

        assertTrue(tree.min() == 1);
        System.out.println();
        System.out.println(tree.max());
        System.out.println(tree.min());
        assertTrue(tree.max() == 10);
        assertTrue(tree.predecessor(10) == 9);

    }

    @Test
    public void deletionTest() {
        /*
        insert(1);
        tree.inOrder();
        tree.delete(1);
        tree.inOrder();

        insert(1, 2);
        tree.inOrder();
        tree.delete(2);
        tree.inOrder();
        */

        insert(16,24,36,19,44,28,61,74,83,64,52,65,86,93,88);
        tree.inOrder();
        System.out.println();

        tree.delete(28);
        tree.delete(36);
        tree.delete(44);
        tree.delete(61);
        tree.delete(83);
        System.out.println();
        tree.inOrder();
        System.out.println();
    }

    @Test
    public void predecessorTest() {
        insert(2,1,4,3,5,6,7,9,8,11,13,12,20,14,19,18,17,10,15,16);
        tree.inOrder();
       // assertTrue(tree.predecessor(10) == 9);
        for (int i = 0; i < 20; i++) {
         //   assertTrue(tree.predecessor(i+1) == i);
            System.out.println(i + " " + tree.predecessor(i+1));
        }
    }

    @Test
    public void successorTest() {
        insert(2,1,4,3,5,6,7,9,8,11,13,12,20,14,19,18,17,10,15,16);
        tree.inOrder();
        // assertTrue(tree.predecessor(10) == 9);
        for (int i = 0; i < 20; i++) {
            //   assertTrue(tree.predecessor(i+1) == i);
            System.out.println(i+2 + " " + tree.successor(i+1));
        }
    }

    @Test
    public void insertSimple() {
        insert(1);
        insert(2);
        assertTrue(tree.min() == 1);
    }

    @Test
    public void testInsert() {
        tree.insert(41);
        tree.insert(20);
        tree.insert(11);
        tree.insert(26);
        tree.insert(23);
        tree.insert(29);
        tree.insert(65);
        tree.insert(50);

        assertTrue(tree.min() == 11);
        assertTrue(tree.max() == 65);
    }
}
