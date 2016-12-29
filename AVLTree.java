
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class AVLTree {
    private AVLNode root;// = new AVLNode();
    //	private AVLNode nullNode = new AVLNode(0);
    private Boolean rebalance;


    public AVLNode getRoot() {
        return root;
    }

    static class AVLNode {
        private AVLNode parent = null;// parent node
        private AVLNode left = null;    // left child
        private AVLNode right = null;    // right child
        private int data;    // holds a positive integer.
        private int height;    // instead of this, you could also store two values for the left and right subtree heights, or a single balance value (as in the lecture), maybe that makes coding easier because you have to worry less about null pointers (?)
        private int balance = 0;
        private Boolean rebalance = false;

        // feel free to add constructors etc.

        public AVLNode(int d) {
            data = d;
            balance = 0;
            parent = null;
            left = null;
            right = null;
        }

        public AVLNode() {

        }

        public AVLNode getParent() {
            return parent;
        }

        public AVLNode getLeft() {
            return left;
        }

        public AVLNode getRight() {
            return right;
        }

        public int getData() {
            return data;
        }

        public int getBalance() {
            return balance;
        }

        public void setBalance(int b) {
            balance = b;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int h) {
            height = h;
        }

        public void setRight(AVLNode r) {
            right = r;
        }

        public void setLeft(AVLNode l) {
            left = l;
        }

        public void setParent(AVLNode p) {
            parent = p;
        }


    }

    public AVLNode leftmostDescendant(AVLNode v) {
        if (v == null)
            return null;
        if (v.left == null)    // it is the leftmost descendant itself
            return v;
        return this.leftmostDescendant(v.left);
    }

    // get maximum value of AVLNode n and its subtrees
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
        return max(root).data;
    }
/*
    private int min(AVLNode n) {
		if (n == null) {
			return 0;
		}
		if (n.left == null) {
			return n.data;
		}
		else {
			return min(n.left);
		}
	}
*/

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
        return min(root).data;
    }

    private void inOrder(AVLNode n) {
        if (n != null) {
            inOrder(n.left);
            //Visit the node by Printing the node data
            System.out.printf("%d ", n.data);
            inOrder(n.right);
        }
    }

    public void inOrder() {
        inOrder(root);
    }

    private AVLNode findNode(AVLNode v, int x) {
        if (v == null)
            return null;
        if (v.data == x)
            return v;
        if (x < v.data)
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
/*
	private boolean isLeftChild(AVLNode n) {
		if (n.parent.data > n.data) {
			return true;
		}
		return false;
	}
*/

	/*
	 * Now your tasks:
	 */

	/*
	private AVLNode delete(AVLNode n, int x) {
		AVLNode toDelete = findNode(n, x);
		// 3 cases:
		// case 0: toDelete as 0 children --> toDelete is leaf --> just delete
		if (toDelete.left == null && toDelete.right == null) {
			// kill parent reference
			toDelete.parent = null;
			if (isLeftChild(toDelete)) {
				// toDelete is left child, so kill the reference
				toDelete.parent.left = null;
			}
			else {
				// toDelete is right child, so kill the reference
				toDelete.parent.right = null;
			}
			// now rebalance from toDelete.parent up (but the ref has already been killed, so save
			// a reference to the node beforehand
		}

		// 1: toDelete has 1 child --> delete toDelete and parent(toDelete) become parent(toDelete.Child) etc
		else if (toDelete.right != null && toDelete.left == null) {
			if (isLeftChild(toDelete)) {
				toDelete.parent.left = toDelete.right;
				toDelete.left.parent = null;
			}
			else {
				toDelete.parent.right = toDelete.right;
				toDelete.right.parent = null;
			}
			toDelete.parent = null;
			toDelete.right = null;
		}
		else if (toDelete.left != null && toDelete.right == null) {
			if (isLeftChild(toDelete)) {
				toDelete.parent.left = toDelete.left;
				toDelete.left.parent = null;
			}
			else {
				toDelete.parent.right = toDelete.left;
				toDelete.right.parent = null;
			}
			toDelete.parent = null;
			toDelete.left = null;
		}

		// 2: toDelete has 2 children --> find min(right_subtree) or max(left_subtree) and replace
		return n;
	}

	*/

    private AVLNode delete(AVLNode n, int x) {
        if (n == null) {   // checking it first time
            return n;
        }
        //	System.out.println("Remove starts... Current Node: " + n.data + ", to delete: " + x);
        AVLNode tmp;
        if (n.data < x) {
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
                                tmp.right.setBalance(0);
                            } else {
                                // double rotation right-left
                                int b = n.left.right.balance;
                                n.left = rotateLeft(n.left);
                                tmp = rotateRight(n);
                                // left and right are swapped in a double rotation,
                                // so you swap balance values as well
                                tmp.left.setBalance((b == -1) ? 1 : 0);
                                tmp.right.setBalance((b == 1) ? -1 : 0);
                            }
                            tmp.setBalance(0);
                            rebalance = false;
                            return tmp;
                        case 0:
                            n.setBalance(-1);
                            rebalance = false;
                            return n;
                        case 1:
                            n.setBalance(0);
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
        } else if (n.data > x) {
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
                                tmp.left.setBalance(0);
                            } else {
                                // double rotation left-right
                                int b = n.right.left.balance;
                                n.right = rotateRight(n.right);
                                tmp = rotateLeft(n);
                                // left and right are swapped in a double rotation,
                                // so you swap balance values as well
                                tmp.right.setBalance((b == -1) ? 1 : 0);
                                tmp.left.setBalance((b == 1) ? -1 : 0);
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
		/*
		if (n == null) {
			return n;
		}
		*/
        // 3 cases:
        // case 0: toDelete as 0 children --> toDelete is leaf --> just delete
        if (n.left == null && n.right == null) {
            // not necessary to kill parent reference, because this will be taken care of in one of the
            // recursive calls of the delete function
			/*
			if (isRightChild) {
				// n is right child, so kill the reference
				n.parent.right = null;
			}
			else {
				// toDelete is left child, so kill the reference
				n.parent.left = null;
			}
			*/
            // set reference of Node n to null;
            n = null;
            // or: return null;

            // now nothing should be pointing to it anymore, and it will be delete by garbage collector
            //...
            // now rebalance from toDelete.parent up (but the ref has already been killed, so save
            // a reference to the node beforehand
        }
        // 1: toDelete has 1 child --> delete toDelete and parent(toDelete) become parent(toDelete.Child) etc
        else if (n.left == null) {
            n = n.right;
        } else if (n.right == null) {
            n = n.left;
        } else {
            // 2 children
            AVLNode temp = min(n.right);
            n.data = temp.data;
            n.right = delete(n.right, temp.data);  //TODO: or delete() oder removeTheNode()?
        }


        // TODO: what to return?
        return n;
    }

    public void delete(int x) {
        if (!this.find(x))    // cannot delete if element is not present
            return;
        // TODO implement
        root = delete(root, x);
    }


    /*
     INSERTION:
     Position of new node in the tree will be determined by comparison with
     existing data; like in any other binary search tree (BST).
     However, after insertion you must check for AVL-balance:
     b(k) = height(right_subtree) - height(left_subtree)
          = {-1, 0, 1}
     but after insertion it can be = {-2, -1, 0, 1, 2}
     -> Rotation may be necessary
     4 cases:
         1. Insertion in left subtree of left child
         2. Insertion in right subtree of left child
         3. Insertion in right subtree of right child
         4. Insertion in left subtree of right child.
         Cases 1, 3 and 2, 4 are symmetrical.
         1, 3 --> simple rotation
         2, 4 --> double rotation

     */
    public void insert(int x) {
        if (this.find(x))    // no element may be inserted more than once
            return;

        // find place to insert, start on the left outermost leaf
        //root = insert(leftmostDescendant(root), x);
        root = insert(root, x);
    }

    private AVLNode insert(AVLNode n, int x) {
        if (n == null) {
            // create FIRST node

            AVLNode newNode = new AVLNode(x);
            newNode.setLeft(null);
            newNode.setRight(null);
            //	newNode.setParent(root);
            newNode.setBalance(0);
            rebalance = false;
            //	root = newNode;
            return newNode;

			/*
			root = new AVLNode(x);
			root.setLeft(null);
			root.setRight(null);
			//root.setParent(root);
			//newNode.setBalance(0);
			rebalance = false;
			return root;
			*/
        }
        AVLNode tmp;
        // walk to the right
        if (n.data < x) {
            if (n.getRight() != null) {
                // insert right
                n.setRight(insert(n.getRight(), x));
                if (n != root && rebalance) {
                    // rebalance necessary
                    switch (n.getBalance()) {
                        case 1:
                            if (n.getRight().getBalance() == 1) {
                                // rotate to the left, because the right child is right-heavy
                                tmp = rotateLeft(n);
                                tmp.getLeft().setBalance(0);
                            } else {
                                // double rotation right-left
                                int b = n.getRight().getLeft().getBalance();
                                n.setRight(rotateRight(n.getRight()));
                                tmp = rotateLeft(n);
                                tmp.getRight().setBalance((b == -1) ? 1 : 0);
                                tmp.getLeft().setBalance((b == 1) ? -1 : 0);
                            }
                            tmp.setBalance(0);
                            rebalance = false;
                            return tmp;
                        case 0:
                            n.setBalance(1);
                            return n;
                        case -1:
                            n.setBalance(0);
                            rebalance = false;
                            return n;
                    }
                } else {
                    return n;
                }
            } else {
                // create new AVLNode
                AVLNode newNode = new AVLNode(x);
                newNode.setLeft(null);
                newNode.setRight(null);
                newNode.setParent(n);
                n.setRight(newNode);
                n.setBalance(n.getBalance() + 1);
                rebalance = (n.getBalance() >= 1);
                return n;
            }
        } else {
            // walk to the left
            if (n.getLeft() != null) {
                // insert to the left
                n.setLeft(insert(n.getLeft(), x));
                if (n != root && rebalance) {
                    // rebalance necessary
                    switch (n.getBalance()) {
                        case -1:
                            if (n.getLeft().getBalance() == -1) {
                                // rotate to the right
                                tmp = rotateRight(n);
                                tmp.getRight().setBalance(0);
                            } else {
                                // double rotation left-right
                                int b = n.getLeft().getRight().getBalance();
                                n.setLeft(rotateLeft(n.getLeft()));
                                tmp = rotateRight(n);
                                // left and right are swapped in a double rotation,
                                // so you swap the balance value as well
                                tmp.getLeft().setBalance((b == -1) ? 1 : 0);
                                tmp.getRight().setBalance((b == 1) ? -1 : 0);
                            }
                            tmp.setBalance(0);
                            rebalance = false;
                            return tmp;
                        case 0:
                            n.setBalance(-1);
                            return n;
                        case 1:
                            n.setBalance(0);
                            rebalance = false;
                            return n;
                    }
                } else {
                    return n;
                }
            } else {
                // create new AVLNode
                AVLNode newNode = new AVLNode(x);
                newNode.setLeft(null);
                newNode.setRight(null);
                newNode.setParent(n);
                n.setLeft(newNode);
                n.setBalance(n.getBalance() - 1);
                rebalance = (n.getBalance() <= -1);
                return n;
            }
        }
        return n;
    }

    private AVLNode rotateLeft(AVLNode n) {
        AVLNode tmp = n.getRight();
        n.setRight(n.getRight().getLeft());
        tmp.setLeft(n);
        return tmp;
    }

    private AVLNode rotateRight(AVLNode n) {
        AVLNode tmp = n.getLeft();
        n.setLeft(n.getLeft().getRight());
        tmp.setRight(n);
        // return new parent node
        return tmp;
    }

	public int successor(int x) {
		if (!this.find(x))	// not possible if element is not present
			return 0;

		try {
 			return successor(root, x).getData();
		}
		catch (NullPointerException e) {
			return 0;
		}
	}

	private AVLNode successor(AVLNode n, int x) {
		if (x >= n.data) {
			// x must be in right subtree
			if (n.right == null) {
				// x is highest value
				return null;
			}
			else {
				return successor(n.right, x);
			}
		}
		else {
			// x must be in left subtree
			if (n.left == null) {
				// x is smallest value
				return null;
			}
			else {
				// successor is minimum of the successor of the left subtree and the current node
				AVLNode recurse = successor(n.left, x);
				if (recurse == null) {
					return n;
				}
				/*
				else {
					// find minimum
					if (recurse.getData() < n.getData()) {
						return recurse;
					}
					else {
						return n;
					}
					*/
				else {
                    AVLNode min = min(n.right);
                    return min.data < recurse.data ? min : recurse;
                }
			}
		}
	}


    private AVLNode predecessor(AVLNode n, int x) {
        if (x < n.getData()) {
            // x must be in left subtree
            if (n.left == null) {
                // x is smallest value
                return n;
            } else {
                return predecessor(n.left, x);
            }
        } else if (x > n.getData()) {
            // x must be in right subtree
            if (n.right == null) {
                // x is highest value
                return null;
            } else {
                return predecessor(n.right, x);
            }
        } else {
            // predecessor is minimum of the predecessor of the right subtree and the current node
            AVLNode recurse = predecessor(n.getRight(), x);
            if (recurse == null) {
                return n;
            } else {
                // find minimum
					/*
					if (recurse.getData() > n.getData()) {
						return recurse;
					}
					else {
						return n;
					}
					*/
                AVLNode max = max(n.left);
                return max.data < recurse.data ? max : recurse;
            }
        }
    }



    public int predecessor(int x) {
        if (!this.find(x))    // not possible if element is not present
            return 0;
        try {
            return predecessor(root, x).data;
        } catch (NullPointerException e) {
            return 0;
        }
    }

	/*
	public int predecessor(int x) {
		if (!this.find(x))	// not possible if element is not present
			return 0;
		try {
			return predecessor(findNode(x)).getData();
		}
		catch (NullPointerException e) {
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
		// Intuition: as we traverse left up the tree we traverse smaller values
		// The first node on the right is the next larger number
		return p;
	}
*/

	/*
    public int successor(int x) {
        if (!this.find(x))
            return 0;
        try {
            return successor(findNode(x)).data;
        } catch (NullPointerException e) {
            return 0;
        }
    }

    private AVLNode successor(AVLNode n) {
        if (n == null) {
            return n;
        }
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

*/
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
            sortedSet.inOrder();
            //		System.out.println("yo");
            bf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}