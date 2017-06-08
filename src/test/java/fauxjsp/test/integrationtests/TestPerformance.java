package fauxjsp.test.integrationtests;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.Servlet;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import fauxjsp.api.nodes.JspPage;
import fauxjsp.api.renderer.RenderSession;
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
public class TestPerformance extends BaseTest {

	final long WARMUP_MS = 2000;
	final long RUNS_MS = 5000;

	protected int run(Runnable r, long duration) {
		int loops = 0;
		long start = System.currentTimeMillis();
		while (start + duration > System.currentTimeMillis()) {
			r.run();
			loops++;
		}
		return loops;
	}

	@Test
	public void testJspParser() {
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

	protected Item makeTree(Item parent, int id, int depth, int childrenPerLevel) {
		if (depth < 1)
			return null;
		Item item = new Item();
		item.setId("" + id);
		item.setParent(parent);
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
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(new ByteArrayInputStream(text
					.getBytes()));
			compare(doc.getDocumentElement(), tree);
		} catch (Exception e) {
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

	@Test
	public void testJspRenderer() {
		final JspPage page = newParser().parseJsp("WEB-INF/jsp/big.jsp");
		Item root = new Item();
		root.setId("0");
		final Item tree = makeTree(root, 0, 5, 10);
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
				session.response = new ServletResponseWrapper(response,
						response.getBaos());
				session.request.setAttribute("tree", tree);
				session.servlet = servlet;

				session.renderer.render(page, session);
				String text = text(baos);
				int t = turn.incrementAndGet();
				if ((t-1) % COMPARE_EVERY_SO_MANY_TURNS == 0){
					compare(text, tree);
					assertEquals(text, "796c9c440c4f44e223852d272c18cd0f",
							TestSupportUtils.MD5(text));
				}
			}
		};
		run(r, WARMUP_MS);
		int loops = run(r, RUNS_MS);
		System.out.println("Render runs / sec : " + loops * 1000 / RUNS_MS);
	}

}
