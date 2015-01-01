package fauxjsp.api;

import java.io.OutputStream;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import fauxjsp.api.parser.ELEvaluation;
import fauxjsp.api.renderer.JspRenderer;

public class RenderSession {

	public JspRenderer renderer;
	public ServletRequest request;
	public ServletResponse response;
	public OutputStream out;
	public ELEvaluation elEvaluation;

}
