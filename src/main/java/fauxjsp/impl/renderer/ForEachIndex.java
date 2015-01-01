package fauxjsp.impl.renderer;

import javax.servlet.jsp.jstl.core.LoopTagStatus;

public class ForEachIndex implements LoopTagStatus{

	protected Object current;
	protected int index;
	protected int count;
	protected Integer begin;
	protected Integer end;
	protected Integer step;
	protected boolean first;
	protected boolean last;
	
	public ForEachIndex(Object current, int index, int count, Integer begin, Integer end, Integer step, boolean first, boolean last) {
		this.current = current;
		this.index = index;
		this.count = count;
		this.begin = begin;
		this.end = end;
		this.step = step;
		this.first = first;
		this.last = last;
	}
	
	@Override
	public Object getCurrent() {
		return current;
	}

	@Override
	public int getIndex() {
		return index;
	}

	@Override
	public int getCount() {
		return count;
	}

	@Override
	public boolean isFirst() {
		return first;
	}

	@Override
	public boolean isLast() {
		return last;
	}

	@Override
	public Integer getBegin() {
		return begin;
	}

	@Override
	public Integer getEnd() {
		return end;
	}

	@Override
	public Integer getStep() {
		return step;
	}

}
