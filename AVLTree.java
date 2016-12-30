import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;



public class AVLTree {
    protected AVLNode root;
    private Boolean rebalance;

    private AVLNode max(AVLNode n) {
        if (n == null) {
            return n;
        }
        if (n.right == null) {
            return n;
        } else {
            return max(n.right);
        }
    }

    public int max() {
        return max(root).key;
    }

    private AVLNode min(AVLNode n) {
        if (n == null) {
            return n;
        }
        if (n.left == null) {
            return n;
        } else {
            return min(n.left);
        }
    }

    public int min() {
        return min(root).key;
    }

    /*

    private void inOrder(AVLNode n) {
        if (n != null) {
            inOrder(n.left);
            //Visit the node by Printing the node key
            System.out.printf("%d ", n.key);
            inOrder(n.right);
        }
    }

    public void inOrder() {
        inOrder(root);
    }

    */

    private AVLNode findNode(AVLNode v, int x) {
        if (v == null)
            return null;
        if (v.key == x)
            return v;
        if (x < v.key)
            return this.findNode(v.left, x);        // dive into left subtree (may be empty)
        else
            return this.findNode(v.right, x);    // dive into right subtree (may be empty)
    }

    private AVLNode findNode(int x) {
        return this.findNode(this.root, x);
    }

    public boolean find(int x) {
        return (this.findNode(x) != null);
    }


    private AVLNode delete(AVLNode n, int x) {
        if (n == null) {   // checking it first time
            return n;
        }
        //	System.out.println("Remove starts... Current Node: " + n.key + ", to delete: " + x);
        AVLNode tmp;
        if (n.key < x) {
            // walk to the right
            if (n.right != null) {  // i'm checking this twice... not really necessary (actually thrice)
                // delete right
                n.right = delete(n.right, x); // n = ... delete should return parent of deleted note, so we can return up the tree

                // --------------------------------- rebalance
                if (n != root && rebalance) {
                    // rebalance necessary
                    switch (n.balance) {
                        // we deleted on right side, so cases should be like inserting on left side
                        case -1:
                            if (n.left.balance == -1) {
                                // rotate to the right
                                tmp = rotateRight(n);
                                tmp.right.balance = 0;
                            } else {
                                // double rotation right-left
                                int b = n.left.right.balance;
                                n.left = rotateLeft(n.left);
                                tmp = rotateRight(n);
                                // left and right are swapped in a double rotation,
                                // so you swap balance values as well
                                tmp.left.balance = (b == -1) ? 1 : 0;
                                tmp.right.balance = (b == 1) ? -1 : 0;
                            }
                            tmp.balance = 0;
                            rebalance = false;
                            return tmp;
                        case 0:
                            n.balance = -1;
                            rebalance = false;
                            return n;
                        case 1:
                            n.balance = 0;
                            rebalance = false;
                            return n;
                    }
                } else {
                    return n;
                }
                // ----------------------------------------------------
            } else {
                // actual deletion
                //	return removeTheNode(n);
            }
        } else if (n.key > x) {
            // walk to the left
            if (n.left != null) {
                // delete left
                n.left = delete(n.left, x);
                // --------------------------------- rebalance
                if (n != root && rebalance) {
                    // rebalance necessary
                    switch (n.balance) {
                        // we deleted on left side, so cases should be like inserting on right side
                        case 1:
                            if (n.right.balance == 1) {
                                // rotate to the left, because right child is right-heavy
                                tmp = rotateLeft(n);
                                tmp.left.balance = 0;
                            } else {
                                // double rotation left-right
                                int b = n.right.left.balance;
                                n.right = rotateRight(n.right);
                                tmp = rotateLeft(n);
                                // left and right are swapped in a double rotation,
                                // so you swap balance values as well
                                tmp.right.balance = (b == -1) ? 1 : 0;
                                tmp.left.balance = (b == 1) ? -1 : 0;
                            }
                            tmp.balance = 0;
                            rebalance = false;
                            return tmp;
                        case 0:
                            n.balance = -1;
                            rebalance = false;
                            return n;
                        case -1:
                            n.balance = 0;
                            rebalance = false;
                            return n;
                    }
                } else {
                    return n;
                }
                // ----------------------------------------------------
            } else {
                // actual deletion
                //return removeTheNode(n);
                // TODO: n = removeTheNood? what to return?
            }
        } else {
            return removeTheNode(n);
        }
        return null;
    }

    private AVLNode removeTheNode(AVLNode n) {
        AVLNode temp;
        if (n.left == null && n.right == null) {
            n = null;
        }
        // 1: toDelete has 1 child --> delete toDelete and parent(toDelete) become parent(toDelete.Child) etc
        else if (n.left == null) {
            n.right.parent = n.parent;
            n = n.right;

        } else if (n.right == null) {
            n.left.parent = n.parent;
            n = n.left;
        } else {
            // 2 children
            temp = successor(n);
            n.key = temp.key;
            n.right = removeTheNode(n.right);
        }
        return n;
    }

    public void delete(int x) {
        if (!this.find(x))    // cannot delete if element is not present
            return;
        //root = delete(root, x);
        delete(root, x);
    }

    public void insert(int x) {
        if (this.find(x))    // no element may be inserted more than once
            return;
        AVLNode n = new AVLNode(x);
        insert(root, n);
    }

    private AVLNode insert(AVLNode n, AVLNode x) {
        // FIRST node of the tree
        if (n == null) {
            root = x;
            return x;
        }
        AVLNode tmp;
        // walk to the right
        if (n.key < x.key) {
            if (n.right != null) {
                // insert right
                n.right = insert(n.right, x);
                if (n != root && rebalance) {
                    // rebalance necessary
                    switch (n.balance) {
                        case 1:
                            if (n.right.balance == 1) {
                                // rotate to the left, because the right child is right-heavy
                                tmp = rotateLeft(n);
                                tmp.left.balance = 0;
                            } else {
                                // double rotation right-left
                                int b = n.right.left.balance;
                                n.right = rotateRight(n.right);
                                tmp = rotateLeft(n);
                                tmp.right.balance = (b == -1) ? 1 : 0;
                                tmp.left.balance = (b == 1) ? -1 : 0;
                            }
                            tmp.balance = 0;
                            rebalance = false;
                            return tmp;
                        case 0:
                            n.balance = 1;
                            return n;
                        case -1:
                            n.balance = 0;
                            rebalance = false;
                            return n;
                    }
                } else {
                    return n;  // here we've reached the root and return it
                }
            } else {
                //TODO: ...
                x.parent = n;
                n.right = x;
                n.balance = n.balance + 1;
                rebalance = (n.balance >= 1);
                return n;
            }
        } else {
            // walk to the left
            if (n.left != null) {
                // insert to the left
                n.left = insert(n.left, x);
                if (n != root && rebalance) {
                    // rebalance necessary
                    switch (n.balance) {
                        case -1:
                            if (n.left.balance == -1) {
                                // rotate to the right
                                tmp = rotateRight(n);
                                tmp.right.balance = 0;
                            } else {
                                // double rotation left-right
                                int b = n.left.right.balance;
                                n.left = rotateLeft(n.left);
                                tmp = rotateRight(n);
                                // left and right are swapped in a double rotation,
                                // so you swap the balance value as well
                                tmp.left.balance = (b == -1) ? 1 : 0;
                                tmp.right.balance = (b == 1) ? -1 : 0;
                            }
                            tmp.balance = 0;
                            rebalance = false;
                            return tmp;
                        case 0:
                            n.balance = -1;
                            return n;
                        case 1:
                            n.balance = 0;
                            rebalance = false;
                            return n;
                    }
                } else {
                    return n;
                }
            } else {
                // create new AVLNode
                x.parent = n;
                n.left = x;
                n.balance = n.balance - 1;
                rebalance = (n.balance <= -1);
                return n;
            }
        }
        return n;
    }

    private void setBalance(AVLNode n) {
        n.balance = height(n.right)-height(n.left);
    }

    private int height(AVLNode n) {
        if(n==null) {
            return -1;
        }
        if(n.left==null && n.right==null) {
            return 0;
        } else if(n.left==null) {
            return 1+height(n.right);
        } else if(n.right==null) {
            return 1+height(n.left);
        } else {
            return 1+Math.max(height(n.left),height(n.right));
        }
    }

    public ArrayList<AVLNode> inOrder() {
        ArrayList<AVLNode> ret = new ArrayList<>();
        inOrder(root, ret);
        return ret;
    }

    private void inOrder(AVLNode n, ArrayList<AVLNode> io) {
        if (n == null) {
            return;
        }
        inOrder(n.left, io);
        io.add(n);
        inOrder(n.right, io);
    }


    private AVLNode rotateLeft(AVLNode n) {
        AVLNode v = n.right;
        v.parent = n.parent;

        n.right = v.left;

        if(n.right!=null) {
            n.right.parent=n;
        }

        v.left = n;
        n.parent = v;

        if(v.parent!=null) {
            if(v.parent.right==n) {
                v.parent.right = v;
            } else if(v.parent.left==n) {
                v.parent.left = v;
            }
        }
        return v;
    }

    private AVLNode rotateRight(AVLNode n) {
        AVLNode tmp = n.left;
        tmp.parent = n.parent;
        n.left = tmp.right;
        if(n.left != null) {
            n.left.parent = n;
        }
        tmp.right = n;
        n.parent = tmp;

        if(tmp.parent!=null) {
            if(tmp.parent.right==n) {
                tmp.parent.right = tmp;
            } else if(tmp.parent.left==n) {
                tmp.parent.left = tmp;
            }
        }
        return tmp;
    }


    public int predecessor(int x) {
        if (!this.find(x))    // not possible if element is not present
            return 0;
        try {
            return predecessor(findNode(x)).key;
        } catch (NullPointerException e) {
            return 0;
        }
    }

    private AVLNode predecessor(AVLNode n) {
        if (n == null) {
            return n;
        }
        if (n.left != null) {
            return max(n.left);
        }
        AVLNode p = n.parent;
        while (p != null && n == p.left) {
            n = p;
            p = p.parent;
        }
        return p;
    }
/*
    public int successor(int x) {
        if (!this.find(x))
            return 0;
        try {
            return successor(root, x, false).key;
        } catch (NullPointerException e) {
            return 0;
        }
    }

    private AVLNode successor(AVLNode n, int x, boolean done) {
        if (n == null) {
            return n;
        }
        AVLNode result = successor(n.left, x, done);
        if (result != null) {
            return result;
        }
        if (done) {
            return n;
        } else {
            if (n.key == x) {
                done = true;
            }
        }
        return successor(n.right, x, done);
    }
*/



    public int successor(int x) {
        if (!this.find(x))
            return 0;
        try {
            return successor(findNode(x)).key;
        } catch (NullPointerException e) {
            return 0;
        }
    }

    private AVLNode successor(AVLNode n) {
        if (n.right != null) {
            return min(n.right);
        }
        AVLNode p = n.parent;
        while (p != null && n == p.right) {
            n = p;
            p = p.parent;
        }
        return p;
    }


    public static void main(String[] args) {
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        try {
            AVLTree sortedSet = new AVLTree();
            int numCommands = Integer.parseInt(bf.readLine());
            for (int i = 0; i < numCommands; i++) {
                String command = bf.readLine();
                String[] split = command.split(" ");
                command = split[0];
                int param = Integer.parseInt(split[1]);
                switch (command) {
                    case "d":
                        sortedSet.delete(param);
                        break;
                    case "f":
                        System.out.println(sortedSet.find(param));
                        break;
                    case "i":
                        sortedSet.insert(param);
                        break;
                    case "max":
                        System.out.println(sortedSet.max());
                        break;
                    case "min":
                        System.out.println(sortedSet.min());
                        break;
                    case "p":
                        System.out.println(sortedSet.predecessor(param));
                        break;
                    case "s":
                        System.out.println(sortedSet.successor(param));
                        break;

                    default:
                        throw new RuntimeException("Invalid command");
                }
            }
            //   sortedSet.inOrder();
            bf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class AVLNode {
    protected AVLNode left;
    protected AVLNode right;
    protected AVLNode parent;
    public int key;
    public int balance;

    public AVLNode(int k) {
        left = null;
        right = null;
        parent = null;
        balance = 0;
        key = k;
    }

    public String toString() {
        return "" + key;
    }
}