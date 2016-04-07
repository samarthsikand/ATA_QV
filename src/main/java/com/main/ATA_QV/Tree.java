package com.main.ATA_QV;

import java.util.ArrayList;
import java.util.List;

public class Tree {
	
	public static class Node<T> {
		public T data;
		public Node<T> parent;
		public List<Node<T>> children;
		
		public Node () {
			this.children = new ArrayList<Node<T>>();
		}
		
		public Node(T ele, Node<T> node) {
			this.data = ele;
			this.parent = node;
			this.children = new ArrayList<Node<T>>();
		}
	}
	
	public static class ANode<T> extends Node<T> {
		public int value;
		public ANode<T> parent;
		public List<ANode<T>> children;
		
		public ANode() {
			this.value = 0;
			this.children = new ArrayList<ANode<T>>();
		}
		
		public ANode(T ele, ANode<T> node) {
			this.value = 0;
			this.data = ele;
			this.parent = node;
			this.children = new ArrayList<ANode<T>>();
		}
	}
	
	public static class StackNode<T> {
		public String label;
		public List<T> listInstances;
		
		public StackNode() {
			
		}
		
		public StackNode(String label) {
			this.label = label;
			listInstances = new ArrayList<T>();
		}
	}
}
