package fauxjsp.test.integrationtests;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.Servlet;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import fauxjsp.api.nodes.JspPage;
import fauxjsp.api.renderer.RenderSession;
import fauxjsp.impl.Utils;
import fauxjsp.impl.parser.JspParserImpl;
import fauxjsp.impl.renderer.JspRendererImpl;
import fauxjsp.servlet.JspServlet;
import fauxjsp.servlet.ServletRequestWrapper;
import fauxjsp.servlet.ServletResponseWrapper;
import fauxjsp.test.support.MockHttpServletRequest;
import fauxjsp.test.support.MockHttpServletResponse;
import fauxjsp.test.support.TestSupportUtils;
import fauxjsp.test.unittests.BaseTest;
import fauxjsp.test.webapp.dto.Item;

import static org.junit.Assert.*;

/**
 * Performance test of {@link JspParserImpl} and {@link JspRendererImpl}
 * 
 * @author George Georgovassilis
 *
 */
@Ignore
public class TestPerformance extends BaseTest {

	final long WARMUP_MS = 10000;
	final long RUNS_MS = 10000;
	final int TREE_DEPTH = 4;
	final int CHILDREN_PER_LEVEL = 4;
	final String RECORDED_CHECKSUM = "9269cd586cf4c801f058b3c60fb114b2";

	protected int run(Runnable r, long duration) throws Exception {
		int loops = 0;
		Thread.sleep(1000); // give GC time to run
		long start = System.currentTimeMillis();
		while (start + duration > System.currentTimeMillis()) {
			r.run();
			loops++;
		}
		return loops;
	}

	@Test // 15810 without method wrapping, 15623 runs/sec with method wrapping
	public void testJspParser() throws Exception {
		Runnable r = new Runnable() {

			@Override
			public void run() {
				newParser().parseJspFragment("WEB-INF/jsp/big.jsp");
			}
		};
		run(r, WARMUP_MS);
		int loops = run(r, RUNS_MS);
		System.out.println("Parse runs / sec : " + loops * 1000 / RUNS_MS);
	}

	protected Item makeTree(Item parent, int orderInSiblings, int depth, int childrenPerLevel) {
		if (depth < 1)
			return null;
		Item item = new Item();
		item.setId(parent.getId() + "/" + orderInSiblings);
		item.setParent(parent);
		item.setText("Item #" + item.getId());
		for (int i = 0; i < childrenPerLevel; i++) {
			Item subTree = makeTree(item, i, depth - 1, childrenPerLevel);
			if (subTree != null) {
				item.getItems().add(subTree);
				subTree.setParent(item);
			}
		}
		return item;
	}

	protected void compare(String text, Item tree) {
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(new ByteArrayInputStream(text.getBytes()));
			compare(doc.getDocumentElement(), tree);
		} catch (Exception e) {
			System.err.println(text);
			throw new RuntimeException(e);
		}
	}

	protected void compare(Element node, Item tree) {
		assertEquals(node.getAttribute("id"), tree.getId());
		NodeList children = node.getChildNodes();
		Iterator<Item> childItem = tree.getItems().iterator();
		for (int i = 0; i < children.getLength(); i++) {
			Node n = children.item(i);
			if (n instanceof Element) {
				Element e = (Element) n;
				if (e.getTagName().equals("item")) {
					Item child = childItem.next();
					compare(e, child);
				}
			}
		}
	}

	@Test //290 runs/sec - 330 with method wrapping
	public void testJspRenderer() throws Exception {
		final JspPage page = newParser().parseJsp("WEB-INF/jsp/big.jsp");
		Item root = new Item();
		root.setId("0");
		final Item tree = makeTree(root, 0, TREE_DEPTH, CHILDREN_PER_LEVEL);
		final int COMPARE_EVERY_SO_MANY_TURNS = 100;
		final AtomicInteger turn = new AtomicInteger(-1);
		final Servlet servlet = new JspServlet();
		Runnable r = new Runnable() {

			@Override
			public void run() {
				MockHttpServletRequest request = new MockHttpServletRequest();
				MockHttpServletResponse response = new MockHttpServletResponse();
				ByteArrayOutputStream baos = response.getBaos();
				RenderSession session = new RenderSession();
				session.request = new ServletRequestWrapper(request);
				session.renderer = renderer;
				session.elEvaluation = elEvaluation;
				session.elContext = elFactory.newElContext();
				session.response = new ServletResponseWrapper(response);
				session.request.setAttribute("tree", tree);
				session.servlet = servlet;

				session.renderer.render(page, session);
				try {
					session.response.flushBuffer();
				} catch (IOException e) {
					throw Utils.softenException(e);
				}
				String text = text(baos);
				int t = turn.incrementAndGet();
				if ((t - 1) % COMPARE_EVERY_SO_MANY_TURNS == 0) {
					compare(text, tree);
					assertEquals(text, RECORDED_CHECKSUM, TestSupportUtils.MD5(text));
				}
			}
		};
		run(r, WARMUP_MS);
		int loops = run(r, RUNS_MS);
		System.out.println("Render runs / sec : " + loops * 1000 / RUNS_MS);
	}

}
