/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.webdetails.cgg;

import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hsqldb.lib.StringInputStream;

import org.mozilla.javascript.*;
import org.mozilla.javascript.tools.shell.Global;
import org.mozilla.javascript.tools.shell.Main;

/**
 *
 * @author pdpi
 */
public class CggScript {

    private static final Log logger = LogFactory.getLog(CggContentGenerator.class);
    private Context cx;
    private Scriptable scope;
    private String[] dependencies;

    public CggScript() {
    }

    public void setDependencies(String[] files) {
        dependencies = files;
    }

    public String execute() {
        cx = ContextFactory.getGlobal().enterContext();
        try {
            // env.js has methods that pass the 64k Java limit, so we can't compile
            // to bytecode. Interpreter mode to the rescue!
            cx.setOptimizationLevel(-1);
            cx.setLanguageVersion(Context.VERSION_1_5);
            Global global = Main.getGlobal();
            scope = cx.initStandardObjects();
            if (!global.isInitialized()) {
                global.init(cx);
            }
            Main.processSource(cx, "/Volumes/Projectos/cgg/resources/lib/env.js");
            
            dependencies = new String[2];
            dependencies[0] = "/Volumes/Projectos/cgg/resources/lib/protovis.js";
            dependencies[1] = "/Volumes/Projectos/cgg/resources/lib/vis.js";
//            try {
            for (String file : dependencies) {
                 Main.processSource(cx, file);
            //cx.evaluateReader(scope, new FileReader(file), "<fileInput>", 1, null);
            }
  /*          } catch (IOException e) {
            logger.error("ops");
            }
*/

            Object result = cx.evaluateString(global, "document.body.innerHTML", "<cmd>", 1, null);
            String output = Context.toString(result).replaceAll("</?div.*?>","");
            return output;
        } catch (Exception e) {
            logger.error(e);
        } finally {
            Context.exit();
        }
        return "";
    }
}