package fauxjsp.impl.renderer;

import java.beans.FeatureDescriptor;
import java.util.Iterator;

import javax.el.CompositeELResolver;
import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.StandardELContext;

/**
 * Resolves each property to null. This resolver should be set last on the {@link CompositeELResolver}
 * present in the {@link StandardELContext} so that JSP doesn't fail with non-existing properties.
 * @author george georgovassilis
 *
 */
public class AlwaysSucceedingPropertyResolver extends ELResolver{

	@Override
	public Object getValue(ELContext context, Object base, Object property) {
		context.setPropertyResolved(true);
		return null;
	}

	@Override
	public Class<?> getType(ELContext context, Object base, Object property) {
		return Object.class;
	}

	@Override
	public void setValue(ELContext context, Object base, Object property, Object value) {
	}

	@Override
	public boolean isReadOnly(ELContext context, Object base, Object property) {
		return true;
	}

	@Override
	public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {
		return null;
	}

	@Override
	public Class<?> getCommonPropertyType(ELContext context, Object base) {
		return Object.class;
	}

}
