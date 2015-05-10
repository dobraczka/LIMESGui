package swp15.link_discovery.model.metric;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.Vector;

public abstract class Node
{
	public Double param1 = null;
	public Double param2 = null;
	public final String id;
	public abstract Set<String> identifiers();
	protected static int colors = 0;
	protected int color;
	public byte getMaxChilds() {return 0;}
	public abstract Set<Class<? extends Node>> validChildClasses();
	public final boolean isValidParentOf(Node node) {return validChildClasses().contains(node.getClass());}
	protected Node parent = null;
	protected List<Node> childs = new Vector<Node>();

	public List<Node> getChilds() {return Collections.unmodifiableList(childs);}

	public enum Acceptance
	{
		OK
		,INVALID_PARENT,ALREADY_HAS_A_PARENT,CANNOT_ACCEPT_ANYMORE_CHILDREN,SOURCE_PROPERTY_EXPECTED,TARGET_PROPERTY_EXPECTED}

	public boolean addChild(Node child)
	{
		if(!acceptsChild(child)) {return false;}
		childs.add(child);
		child.parent=this;
		child.pushDownColor(this.color);
		return true;
	}

	public void removeChild(Node child)
	{
		childs.remove(child);
		child.parent=null;
		child.pushDownColor(colors++);
	}

	public boolean acceptsChild(Node n) {return isValidParentOf(n)&&n.parent==null&&childs.size()<getMaxChilds()&&this.color!=n.color;}

	public Acceptance acceptsChildWithReason(Node n)
	{
		if(!isValidParentOf(n)) {return Acceptance.INVALID_PARENT;}
		if(n.parent!=null) {return Acceptance.ALREADY_HAS_A_PARENT;}
		if(childs.size()>=getMaxChilds()) {return Acceptance.CANNOT_ACCEPT_ANYMORE_CHILDREN;}
		return Acceptance.OK;
	}

	public boolean isComplete()
	{
		if(childs.size()<getMaxChilds()) {return false;}
		for(Node child : childs) {if(!child.isComplete()) return false;}
		return true;
	}

	public Node(String id)
	{
		this.id=id;
		color = colors++;
	}

	protected void pushDownColor(int color)
	{
		this.color = color;
		for(Node child : childs) {child.pushDownColor(color);}
	}

	@Override 
	public String toString()
	{
		StringBuilder sb = new StringBuilder(id);
		synchronized(this)
		{
			if(getChilds().isEmpty()) {return sb.toString();}
			int i=0;
			for(Node child: getChilds())
			{
				if(i==0) {sb.append('(');} else {sb.append(',');}
				if(this.hasFrontParameters())
				{
					Double p;
					if((p=(i==0?param1:param2))!=null) 	{sb.append(p);sb.append('*');}
				}
				sb.append(child.toString());

				if(!this.hasFrontParameters())
				{
					Double p;
					if((p=(i==0?param1:param2))!=null) 	{sb.append('|');sb.append(p);}
				}
				i++;
			}
		}
		sb.append(')');
		return sb.toString();
	}
	public static Node createNode(String id)
	{
		id = id.toLowerCase();
		if(Measure.identifiers.contains(id)) return new Measure(id);
		if(Operator.identifiers.contains(id)) return new Operator(id);
		throw new MetricFormatException("There is no node with id \""+id+"\", creation is not possible.");
	}

	public boolean hasFrontParameters() {return "add".equalsIgnoreCase(id);}

	public boolean equals(Object o) {
		return equals(o, false);
	}

	@SuppressWarnings("unused")
	public boolean equals(Object o, boolean strict)
	{
		if(o==null||!(this.getClass()==o.getClass()))
			return false;
		Node n2 = (Node) o;
		boolean stringTest = false, parentTest = true, childTest = true, completeTest;
		stringTest = this.toString().equals(o.toString());
		if((this.parent == null && n2.parent != null) || (n2.parent==null && this.parent != null)) {
		}
		else if(this.parent != null && n2.parent != null)
			if(this.parent == n2.parent) { // same parent

			} else {
				parentTest = false;
			}
		if(this.getMaxChilds() == n2.getMaxChilds() && this.getMaxChilds()>0) {
			List<Node> childrens1 = this.getChilds();
			List<Node> childrens2 = n2.getChilds();
			for(int i = 0; i<childrens1.size(); i++) {
				for(int j = 0; j < childrens2.size(); j++)
					if(!childrens1.get(i).equals(childrens2.get(j)))
							childTest = false;
			}
		}else {
			childTest = false;
		}
		completeTest = this.isComplete() == n2.isComplete();
		if(strict)
			return stringTest && parentTest;
		else
			return stringTest;
	}
}
