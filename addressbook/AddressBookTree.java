/* Name: Jennifer Nguyen
 * Dr. Andrew Steinberg
 * COP3503 Fall 2021
 * Programming Assignment 2
 */
package addressbook;

import java.util.Random;
import java.util.Stack;

class Node<T,U>{
	T name;
	U office;
	Node<T,U> parent;
	Node<T,U> left;
	Node<T,U> right;
	int color;
	
	// default for nodes
	public Node(T name, U office){
		this.name = name;
		this.office = office;
		Node<T,U> parent = null;
		Node<T,U> left = null;
		Node<T,U> right = null;
		int color = 1;		//red: 1, black: 0
	}
}

public class AddressBookTree<T extends Comparable<T>,U>{
	private Node<T,U> root;	
	private Node<T,U> nil;
	
	public AddressBookTree() {
		nil = new Node<T,U>(null, null);
		nil.color = 0;
		root = nil;
	}
	
	public Node<T,U> getRoot() {
		return root;
	}
	
	// Stack method for inorder transversal
	public void display() {
        Stack<Node<T,U>> stack = new Stack<Node <T,U>>();
        String N, O;
    
        Node<T,U> current = root;
    
        while (!stack.empty() || current != nil)
        {
            if (current != nil)
            {
                stack.push(current);
                current = current.left;
            }
            else {
            	current = stack.pop();
            	
                N = String.valueOf(current.name);
       		 	O = String.valueOf(current.office);
                System.out.printf(N);
                System.out.printf(" ");
                System.out.printf(O);
                System.out.printf("\n");

                current = current.right;
            }
        }
	}
	
	public void leftRotate(Node<T,U> x) {
		  Node<T,U> y = x.right;
		  x.right = y.left;
		  
		  if(y.left != nil) 
		    y.left.parent = x;
		  
		  y.parent = x.parent;
		  
		  if(x.parent == nil)  // if x is root
		    this.root = y;
		  else if(x == x.parent.left)  // if x is left child
		    x.parent.left = y;
		  else  // if x is right child
		    x.parent.right = y;
		  
		  y.left = x;
		  x.parent = y;
	}

	
	public void rightRotate(Node<T,U> x) {
		  Node<T,U> y = x.left;
		  x.left = y.right;
		  
		  if(y.right != nil)
		    y.right.parent = x;
		  
		  y.parent = x.parent;
		  
		  if(x.parent == nil) 
		    this.root = y;
		  else if(x == x.parent.right) 
		    x.parent.right = y;
		  else 
		    x.parent.left = y;
		  
		  y.right = x;
		  x.parent = y;
	}

	
	public void insertfix(Node<T,U> current) {
		// adjust nodes and colors of tree to compensate for new node
		while (current.parent.color == 1) {
			if (current.parent == current.parent.parent.left) {		// parent is left child
				Node<T,U> y = current.parent.parent.right;			// uncle to newly inserted node
				
				// Case 1 - if uncle is red
				if (y.color == 1) {
					current.parent.color = 0;
					y.color = 0;
					current.parent.parent.color = 1;
					current = current.parent.parent;
				}
				//Case 2/3 - uncle is black and new node is a right/left child
				else {
					// Case 2
					if (current == current.parent.right) {
						current = current.parent;
						leftRotate(current);
					}
						
					current.parent.color = 0;
					current.parent.parent.color = 1;
					rightRotate(current.parent.parent);
				}
			}
			else {
				Node<T,U> y = current.parent.parent.left;			// relative node to newly inserted node
				
				// Case 1 - if uncle is red
				if (y.color == 1) {
					current.parent.color = 0;
					y.color = 0;
					current.parent.parent.color = 1;
					current = current.parent.parent;
				}
				//Case 2/3 - uncle is black and new node is a right/left child
				else {
					if (current == current.parent.left) {
						current = current.parent;
						rightRotate(current);
					}
						
					current.parent.color = 0;
					current.parent.parent.color = 1;
					leftRotate(current.parent.parent);
				}
			}
			
		}
		root.color = 0;
	}
	
	public void insert(T person, U place) {
		Node<T,U> newnode = new Node<T,U>(person,place);		// newnode is new node to be inserted
		Node<T,U> y = nil;			// y is current pointer to parent			
		Node<T,U> x = root;			// x is pointer to current transversal
		
		while(x != nil) {
			y = x;
			if (newnode.name.compareTo(x.name) < 0) {
				x = x.left;
			}
			else
				x = x.right;
		}
		
		newnode.parent = y;
		
		if (y == nil) 
			root = newnode;
		else if (newnode.name.compareTo(y.name) < 0)
			y.left = newnode;
		else
			y.right = newnode;
		
		newnode.left = nil;
		newnode.right = nil;
		newnode.color = 1;
		
		insertfix(newnode);
	}
	
	public void transplant(Node<T,U> a, Node<T,U> b) {
		if (a.parent == nil)
			root = b;
		else if (a == a.parent.left)
			a.parent.left = b;
		else
			a.parent.right = b;
		b.parent = a.parent;
	}
	
	public Node<T,U> minimum(Node<T,U> x) {
		while(x.left != nil)
		  x = x.left;
		return x;
		}

	public void deleteFix(Node<T,U> x)
	{
		while (x != root && x.color == 0) {
			if(x == x.parent.left) {
				  Node<T,U> w = x.parent.right;
				  
			      if(w.color == 1) {
			        w.color = 0;
			        x.parent.color = 1;
			        leftRotate(x.parent);
			        w = x.parent.right;
			      }
			      if(w.left.color == 0 && w.right.color == 1) {
			        w.color = 1;
			        x = x.parent;
			      }
			      else {
			        if(w.right.color == 0) {
			          w.left.color = 0;
			          w.color = 1;
			          rightRotate(w);
			          w = x.parent.right;
			        }
			        w.color = x.parent.color;
			        x.parent.color = 0;
			        w.right.color = 0;
			        leftRotate(x.parent);
			        x = root;
			      }
			    }
			    else {
			      Node<T,U> w = x.parent.left;
			      if(w.color == 1) {
			        w.color = 0;
			        x.parent.color = 1;
			        rightRotate(x.parent);
			        w = x.parent.left;
			      }
			      if(w.right.color == 0 && w.left.color == 0) {
			        w.color = 1;
			        x = x.parent;
			      }
			      else {
			        if(w.left.color == 0) {
			          w.right.color = 0;
			          w.color = 1;
			          leftRotate(w);
			          w = x.parent.left;
			        }
			        w.color = x.parent.color;
			        x.parent.color = 0;
			        w.left.color = 0;
			        rightRotate(x.parent);
			        x = this.root;
			      }
			    }
			  }
			  x.color = 0;
	}
	
	public void deleteNode(T name) {
		 Node<T,U> x = root;		// temp
		 Node<T,U> z;
		 Node<T,U> y;
		 int ogcolor;
		 
		 while (name.compareTo(x.name) != 0) {
			 if (name.compareTo(x.name) < 0)
				 x = x.left;
			 else
				 x = x.right;
		 }
		 z = x;
		 y = z;
		 ogcolor = y.color;
		 
		 if (z.left == nil) {			// no children or right child
			 x = z.right;
			 transplant(z, z.right);
		 }
		 else if (z.right == nil) {		// left child
			 x = z.left;
			 transplant(z, z.left);
		 }
		 else {							// both children
			 y = minimum(z.right);
			 ogcolor = y.color;
			 x = y.right;
			 if (y.parent == z)
				 x.parent = y;
			 else {
				 transplant(y,y.right);
				 y.right = z.right;
				 y.right.parent = y;
			 }
			 transplant(z, y);
			 y.left = z.left;
			 y.left.parent = y;
			 y.color = z.color;
		 }
		 if (ogcolor == 0)
			 deleteFix(x);
	}
	
	// Stack method of transerval to count nodes
	public int countBlack(Node<T,U> root) {
		// create an empty stack
        Stack<Node<T,U>> stack = new Stack<Node <T,U>>();
        int counter = 0;
    
        Node<T,U> current = root;
    
        // if the current node is not null and the stack is not empty continue
        while (!stack.empty() || current != nil)
        {
            if (current != nil)
            {
                stack.push(current);
                current = current.left;
            }
            else 
            {
            	current = stack.pop();
            	
            	// counter for black nodes encountered
            	if (current.color == 0)
            		counter += 1;
    
                current = current.right;
            }
        }
        return counter;
	}
	
	public int countRed(Node<T,U> root) {
        Stack<Node<T,U>> stack = new Stack<Node <T,U>>();
        int counter = 0;
    
        Node<T,U> current = root;
    
        while (!stack.empty() || current != nil)
        {
            if (current != nil)
            {
                stack.push(current);
                current = current.left;
            }
            else {
               
            	current = stack.pop();
            	
            	// counter for red nodes encountered
            	if (current.color == 1)
            		counter += 1;
    
                current = current.right;
            }
        }
        return counter;
	}
}
