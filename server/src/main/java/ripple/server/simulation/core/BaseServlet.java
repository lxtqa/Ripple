package ripple.server.simulation.core;

import ripple.server.simulation.core.emulator.Emulator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/***************************************************************************
 *
 * @author qingzhou.sjq
 * @date 2019/1/3
 *
 ***************************************************************************/
public class BaseServlet extends HttpServlet {
    private Emulator emulator;
    private Context context;

    public BaseServlet(Emulator emulator, Context context) {
        this.emulator = emulator;
        this.context = context;
    }

    public Emulator getEmulator() {
        return emulator;
    }

    private void setEmulator(Emulator emulator) {
        this.emulator = emulator;
    }

    public Context getContext() {
        return context;
    }

    private void setContext(Context context) {
        this.context = context;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().println("<h1>base servlet</h1>");
    }

}
