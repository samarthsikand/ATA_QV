package com.main.ATA_QV;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.main.ATA_QV.Tree.ANode;


public class AutomationATA {
	public Map<WebElement,ANode<WebElement>> mapWebElementToNode = new HashMap<WebElement,ANode<WebElement>>();
	public WebDriver driver = null;
	public String URL = "";
	public ANode<WebElement> rootNode = null;
	
	public void printAllChildren(ANode<Element> root, int i) {
		Elements rows = root.data.children();
		for(Element columns : rows) {
			if(!(columns.tagName().equalsIgnoreCase("script") || columns.tagName().equalsIgnoreCase("style") || columns.tagName().equalsIgnoreCase("meta"))) {
				ANode<Element> node = new ANode(columns,root);
				for(int j=0; j<i; j++) {
					System.out.print(" ");
				}
				System.out.println(columns.text());
				printAllChildren(node,i+1);
			}
		}
	}
	
	public void checkIfURLChanged(boolean forceReset) {
		if(!URL.equals(driver.getCurrentUrl()) || forceReset) {
			List<WebElement> listOfElement = driver.findElements(By.xpath("//html"));
			System.out.println("URL is not the same");
			URL = driver.getCurrentUrl();
			mapWebElementToNode.clear();
			System.out.println("Creating child nodes..!");
			for(WebElement ele : listOfElement) {
				rootNode = null;
				rootNode = new ANode<WebElement>(ele,null);
				System.out.println(ele.getTagName());
				try {
					Thread.sleep(10000);
				} catch(Exception e) {
					e.printStackTrace();
				}
				
				createChildNodes(ele,1,rootNode);
			}
		}
	}
	
	public void executeTuple(Tuple tuple) {
		checkIfURLChanged(false);
		WebElement element = null;
		
		if(!tuple.action.equalsIgnoreCase("open")) {
			if(tuple.target.contains("xpath=")) {
				tuple.target = tuple.target.substring(tuple.target.indexOf("=")+1);
			}
			element = driver.findElement(By.xpath(tuple.target));
			List<String> listOfAnchors = generateAnchors(element);
			if(listOfAnchors != null) {
				System.out.println("The output is as follows: ");
				for(String str : listOfAnchors) {
					System.out.println(str);
				}
			} else {
				System.out.println("The output is as follows: There is no such anchor!!");
				checkIfURLChanged(true);
				element = driver.findElement(By.xpath(tuple.target));
				listOfAnchors = generateAnchors(element);
				if(listOfAnchors != null) {
					System.out.println("The output is as follows: ");
					for(String str : listOfAnchors) {
						System.out.println(str);
					}
				}
			}
			
			for(WebElement eleNode : mapWebElementToNode.keySet()) {
				mapWebElementToNode.get(eleNode).value = 0;
			}
		}
	}
	
	public List<String> generateAnchors(WebElement ele) {
		String label = "";
		List<WebElement> otherLabels = null;
		List<String> listOfAnchors = new ArrayList<String>();
		Set<ANode<WebElement>> otherLabelTrees = new HashSet<ANode<WebElement>>();
		ANode<WebElement> otherTreeNode = null;
		ANode<WebElement> closestTree = null;
		List<ANode<WebElement>> listPathFromTargetToRoot = null;
		Set<ANode<WebElement>> setPathFromTargetToRoot = new HashSet<ANode<WebElement>>();
		if(ele.getTagName().equals("a") || ele.getTagName().equals("button") || (ele.getTagName().equals("input") && ele.getAttribute("type").equals("submit")) || ele.getTagName().equals("td")) {
			label = ele.getText();
			otherLabels = driver.findElements(By.xpath("//*[contains(text(),'"+label+"')]"));
		} else {
			label = ele.getTagName();
			System.out.println("The tag name of target element is: "+ label);
			otherLabels = driver.findElements(By.xpath("//"+label+""));
		}
		ANode<WebElement> subtreeTargetNode = getInterestingSubtree(otherLabels,ele);
		System.out.println("Other Labels size: "+otherLabels.size());
		
		if(subtreeTargetNode != null) {
			System.out.println("The target tree is : "+subtreeTargetNode.data.getTagName()+" and its value is: "+ subtreeTargetNode.value + " ,class: "+subtreeTargetNode.data.getAttribute("class"));
			listPathFromTargetToRoot = getPathFromTargetToRoot(ele);
			setPathFromTargetToRoot.addAll(listPathFromTargetToRoot);
			
			for(WebElement otherEle : otherLabels) {
				if(!otherEle.equals(ele) && !(otherEle.getTagName().equals("script") || otherEle.getTagName().equals("style") || otherEle.getTagName().equals("meta"))) {
					otherTreeNode = getOtherSubTree(otherEle,setPathFromTargetToRoot);
					System.out.println("Other Label Node: "+otherTreeNode.data.getTagName()+" Name: "+otherTreeNode.data.getText()+" ID:" +otherTreeNode.data.getAttribute("id") + " Class:" + otherTreeNode.data.getAttribute("class"));
					
					if(!otherLabelTrees.contains(otherTreeNode)) {
						otherLabelTrees.add(otherTreeNode);
					}
				}
			}
			
			while(otherLabelTrees.size() != 0) {
				String distinctLabel = getDistinctLabel(subtreeTargetNode,otherLabelTrees);
				System.out.println("Distinct label:"+distinctLabel);
				if(distinctLabel != null) {
					listOfAnchors.add(distinctLabel);
					return listOfAnchors;
				}
				closestTree = getClosestTree(subtreeTargetNode,otherLabelTrees,listPathFromTargetToRoot,setPathFromTargetToRoot);
				System.out.println("The closest tree details: Tag Name: "+closestTree.data.getTagName() + ", Id:"+closestTree.data.getAttribute("id") + ", Class" + closestTree.data.getAttribute("class"));
				Set<ANode<WebElement>> closestTreeList = new HashSet<ANode<WebElement>>();
				closestTreeList.add(closestTree);
				distinctLabel = getDistinctLabel(subtreeTargetNode,closestTreeList);
				
				if(distinctLabel == null) {
					return null;
				}
				listOfAnchors.add(distinctLabel);
				subtreeTargetNode = mergeSubtrees(subtreeTargetNode,closestTree,otherLabelTrees);
				otherLabelTrees.remove(subtreeTargetNode);
			}
		}
		
		return null;
	}
	
	public ANode<WebElement> getInterestingSubtree(List<WebElement> otherLabels, WebElement ele) {
		if(!mapWebElementToNode.containsKey(ele)) {
			System.out.println("The Element is not in the dom structure.!");
			return null;
		}
		mapWebElementToNode.get(ele).value = 1;
		for(WebElement element : otherLabels) {
			if(mapWebElementToNode.containsKey(element)) {
				mapWebElementToNode.get(element).value = 1;
			} else {
				System.out.println("Why did this element not be in tree? "+ element.getTagName()+" , Id: "+element.getAttribute("id"));
			}
		}
		calculateValueOfNode(rootNode);
		
		ANode<WebElement> targetNode = mapWebElementToNode.get(ele);
		while(targetNode.parent.value == 1) {
			targetNode = targetNode.parent;
		}
		return targetNode;
	}
	
	public List<ANode<WebElement>> getPathFromTargetToRoot(WebElement ele) {
		List<ANode<WebElement>> listFromTargetToNode = new ArrayList<ANode<WebElement>>();
		ANode<WebElement> node = mapWebElementToNode.get(ele);
		while(node.parent != null) {
			listFromTargetToNode.add(node);
			node = node.parent;
		}
		return listFromTargetToNode;
	}
	
	public ANode<WebElement> getOtherSubTree(WebElement element, Set<ANode<WebElement>> pathFromTargetToRoot) {
		ANode<WebElement> otherNode = mapWebElementToNode.get(element);
		while(otherNode.parent != null && !pathFromTargetToRoot.contains(otherNode.parent)) {
			otherNode = otherNode.parent;
		}
		return otherNode;
	}
	
	public int calculateValueOfNode(ANode<WebElement> rootNode) {
		if(rootNode.children.size() == 0) {
			return rootNode.value;
		}
		for(ANode<WebElement> childNode : rootNode.children) {
			rootNode.value = rootNode.value + calculateValueOfNode(childNode);
		}
		return rootNode.value;
	}
	
	public String getDistinctLabel(ANode<WebElement> subtreeTargetNode, Set<ANode<WebElement>> otherLabelTrees) {
		Set<String> listOfLabelsTargetTree = new HashSet<String>();
		System.out.println("Labels for target tree:");
		List<String> targetSubtreeLabels = getLabelsOfTree(subtreeTargetNode);
		System.out.println("Labels for Other Subtree:");
		List<String> listOtherSubtreeLabels = getLabelsOfTree(otherLabelTrees);
		Set<String> setOtherSubtreeLabels = new HashSet(listOtherSubtreeLabels);
		
		for(String str : targetSubtreeLabels) {
			if(!setOtherSubtreeLabels.contains(str)) {
				return str;
			}
		}
		
		return null;
	}
	
	public List<String> getLabelsOfTree(ANode<WebElement> subTreeTargetNode) {
		List<String> labels = new ArrayList<String>();
		List<WebElement> listLabels = subTreeTargetNode.data.findElements(By.xpath("./descendant::h1 | ./descendant::h2 | ./descendant::h3 | ./descendant::h3 | ./descendant::h4"));
		if(listLabels.size() != 0) {
			for(WebElement ele : listLabels) {
				System.out.println(ele.getTagName() +": "+ele.getText());
				if(!labels.contains(ele.getText().trim())) {
					labels.add(ele.getText().trim());
				}
			}
		}
		
		listLabels = subTreeTargetNode.data.findElements(By.xpath("./descendant::label | ./descendant::td | ./descendant::a | ./descendant::button"));
		if(listLabels.size() != 0) {
			for(WebElement ele : listLabels) {
				System.out.println(ele.getTagName()+": "+ele.getText());
				if(!labels.contains(ele.getText()) && !ele.getText().trim().equals("")) {
					labels.add(ele.getText().trim());
				}
			}
		}
		
		listLabels = subTreeTargetNode.data.findElements(By.xpath("./descendant::span"));
		if(listLabels.size() != 0) {
			for(WebElement ele : listLabels) {
				System.out.println(ele.getTagName()+": "+ele.getText());
				if(!labels.contains(ele.getText()) && !ele.getText().trim().equals("")) {
					labels.add(ele.getText().trim());
				}
			}
		}
		
		return labels;
	}
	
	public List<String> getLabelsOfTree(Set<ANode<WebElement>> otherLabelTrees) {
		List<String> labels = new ArrayList<String>();
		for(ANode<WebElement> nodeEle : otherLabelTrees) {
			labels.addAll(getLabelsOfTree(nodeEle));
		}
		return labels;
	}
	
	public ANode<WebElement> getClosestTree(ANode<WebElement> subTreeTargetNode, Set<ANode<WebElement>> otherLabelTrees, List<ANode<WebElement>> listFromTargetToRoot, Set<ANode<WebElement>> setOfTargetToRoot) {
		ANode<WebElement> nodeEle = null;
		ANode<WebElement> closestSubtreeNode = null;
		int dist = 0;
		int minDist = 9999999;
		for(ANode<WebElement> node : otherLabelTrees) {
			nodeEle = node;
			dist=0;
			while(nodeEle.parent != null || setOfTargetToRoot.contains(nodeEle.parent)) {
				nodeEle = nodeEle.parent;
			}
			if(setOfTargetToRoot.contains(nodeEle.parent)) {
				dist = listFromTargetToRoot.indexOf(nodeEle.parent);
			}
			if(dist < minDist) {
				minDist = dist;
				closestSubtreeNode = node;
			}
		}
		return closestSubtreeNode;
	}
	
	public List<ANode<WebElement>> getLowestCommonAncestorPath(List<ANode<WebElement>> pathOfTargetToRoot, List<ANode<WebElement>> pathOfClosestTreeToRoot) {
		int i = 0;
		int lca = 0;
		List<ANode<WebElement>> pathOfLowestCommonAncestor = new ArrayList<ANode<WebElement>>();
		Collections.reverse(pathOfTargetToRoot);
		Collections.reverse(pathOfClosestTreeToRoot);
		while(i < pathOfTargetToRoot.size() && i < pathOfClosestTreeToRoot.size()) {
			if(pathOfTargetToRoot.get(i) == pathOfClosestTreeToRoot.get(i)) {
				System.out.println("The ancestor has matched...");
				pathOfLowestCommonAncestor.add(pathOfTargetToRoot.get(i));
				lca++;
			} else {
				break;
			}
			i++;
		}
		
		return pathOfLowestCommonAncestor;
	}
	
	public ANode<WebElement> mergeSubtrees(ANode<WebElement> subtreeTarget, ANode<WebElement> closestTree, Set<ANode<WebElement>> otherLabelTrees) {
		int nodeIndex = 0;
		List<ANode<WebElement>> pathOfTargetToRoot = getPathFromTargetToRoot(subtreeTarget.data);
		List<ANode<WebElement>> pathOfClosestTreeToRoot = getPathFromTargetToRoot(closestTree.data);
		Map<ANode<WebElement>,Set<ANode<WebElement>>> mapOtherNodeToPaths = new HashMap<ANode<WebElement>,Set<ANode<WebElement>>>();
		
		for(ANode<WebElement> node : otherLabelTrees) {
			if(!mapOtherNodeToPaths.containsKey(node)) {
				mapOtherNodeToPaths.put(node, new HashSet<ANode<WebElement>>());
			}
			Set<ANode<WebElement>> setOfNodes = new HashSet<ANode<WebElement>>();
			setOfNodes.addAll(getPathFromTargetToRoot(node.data));
			mapOtherNodeToPaths.put(node, setOfNodes);
		}
		
		List<ANode<WebElement>> commonAncestorPath = getLowestCommonAncestorPath(pathOfTargetToRoot,pathOfClosestTreeToRoot);
		
		for(ANode<WebElement> node : commonAncestorPath) {
			for(Set<ANode<WebElement>> pathOfNode : mapOtherNodeToPaths.values()) {
				if(pathOfNode.contains(node)) {
					return commonAncestorPath.get(nodeIndex-1);
				}else {
					nodeIndex++;
				}
			}
		}
		return null;
	}
	
	public void createChildNodes(WebElement ele,int i,ANode<WebElement> parentNode) {
		int j = 0;
		List<WebElement> listChild = ele.findElements(By.xpath("*"));
		//System.out.println(listChild.size());
		for(WebElement webEle : listChild) {
			if(!(webEle.getTagName().equalsIgnoreCase("script") || webEle.getTagName().equalsIgnoreCase("style") || webEle.getTagName().equalsIgnoreCase("meta"))) {
				j=0;
				ANode<WebElement> currentNode = new ANode<WebElement>(webEle,parentNode);
				if(!mapWebElementToNode.containsKey(webEle)) {
					mapWebElementToNode.put(webEle, currentNode);
				}
				/*while(j < i) {
					System.out.print(" ");
					j++;
				}
				System.out.println(webEle.getTagName());*/
				createChildNodes(webEle,i+1,currentNode);
				//System.out.println(currentNode+ " " + currentNode.data.getTagName());
				parentNode.children.add(currentNode);
			}
		}
	}
	
	static class Tuple {
		String action;
		String target;
		String data;
		
		public Tuple() {
			
		}
		
		public Tuple(String action, String target, String data) {
			this.action = action;
			this.target = target;
			this.data = data;
		}
	}
}
