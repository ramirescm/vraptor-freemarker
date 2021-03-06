package br.com.caelum.vraptor.freemarker;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.View;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.RequestScoped;
import br.com.caelum.vraptor.view.ResultException;
import freemarker.template.TemplateException;

@Component
@RequestScoped
public class FreemarkerView implements View {
	private final HttpServletRequest request;
	private final Freemarker freemarker;

	public FreemarkerView(HttpServletRequest request, Freemarker freemarker) {
		this.request = request;
		this.freemarker = freemarker;
	}

	public void withTemplate(String path) {
		try {
			Template template = freemarker.use(path);
			includeRequestAttributes(template);
			template.render();
		} catch (IOException e) {
			throw new ResultException(e);
		} catch (TemplateException e) {
			throw new ResultException(e);
		}
	}

	private void includeRequestAttributes(Template template) {
		Enumeration<String> attributeNames = request.getAttributeNames();
		while (attributeNames.hasMoreElements()) {
			String attributeName = attributeNames.nextElement();
			template.with(attributeName, request.getAttribute(attributeName));
		}
	}

	public static Class<FreemarkerView> freemarker() {
		return FreemarkerView.class;
	}
}
