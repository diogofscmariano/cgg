/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.webdetails.cgg;

import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.logging.Log;
import org.pentaho.platform.api.engine.IParameterProvider;
import org.pentaho.platform.engine.services.solution.BaseContentGenerator;
import pt.webdetails.cgg.CggScript;

/**
 *
 * @author pdpi
 */
public class CggContentGenerator extends BaseContentGenerator {

    private enum methods {

        DRAW
    }
    private static final String MIME_HTML = "text/html";
    private static final String MIME_SVG = "image/svg+xml";

    @Override
    public Log getLogger() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void createContent() throws Exception {
        try {
            final IParameterProvider requestParams = parameterProviders.get(IParameterProvider.SCOPE_REQUEST);
            final IParameterProvider pathParams = parameterProviders.get("path");
            OutputStream out = outputHandler.getOutputContentItem("response", "content", "", instanceId, MIME_SVG).getOutputStream(null);
            final String method = pathParams.getStringParameter("path", null);
            try {
                switch (methods.valueOf(method.replaceAll("/", "").toUpperCase())) {
                    case DRAW:
                        draw(requestParams, out);
                }
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(CggContentGenerator.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (Exception ex) {
            Logger.getLogger(CggContentGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void draw(final IParameterProvider requestParams, final OutputStream out) {
        try {
            CggScript script = new CggScript();
            out.write(script.execute().getBytes("UTF8"));
        } catch (Exception ex) {
            Logger.getLogger(CggContentGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}