package mirur.test;

import javax.media.opengl.GLProfile;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import com.jogamp.opengl.util.AnimatorBase;
import com.jogamp.opengl.util.FPSAnimator;
import com.metsci.glimpse.canvas.GlimpseCanvas;
import com.metsci.glimpse.gl.GLCapabilityEventListener;
import com.metsci.glimpse.swt.canvas.NewtSwtGlimpseCanvas;

import mirur.core.VariableObject;
import mirur.plugin.painterview.MirurLAF;
import mirur.plugins.DataPainter;
import mirur.plugins.MirurView;

public class Harness {
    private Display display;
    private Shell shell;
    private AnimatorBase animator;

    private GlimpseCanvas canvas;
    private DataPainter painter;

    public Harness() {
        display = new Display();
        shell = new Shell(display);
        shell.setLayout(new GridLayout());

        final ToolBar toolBar = new ToolBar(shell, SWT.NONE);
        Rectangle clientArea = shell.getClientArea();
        toolBar.setLocation(clientArea.x, clientArea.y);

        NewtSwtGlimpseCanvas canvas = new NewtSwtGlimpseCanvas(shell, GLProfile.get(GLProfile.GL2), SWT.DOUBLE_BUFFERED);
        canvas.getGLDrawable().addGLEventListener(new GLCapabilityEventListener());
        animator = new FPSAnimator(canvas.getGLDrawable(), 20);

        canvas.setLayoutData(new GridData(GridData.FILL_BOTH));
        this.canvas = canvas;

        final Menu menu = new Menu(shell, SWT.POP_UP);
        final ToolItem item = new ToolItem(toolBar, SWT.DROP_DOWN);
        item.setText("Menu");
        item.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
                if (event.detail == SWT.ARROW) {
                    for (MenuItem item : menu.getItems()) {
                        item.dispose();
                    }

                    Rectangle rect = item.getBounds();
                    Point pt = new Point(rect.x, rect.y + rect.height);
                    pt = toolBar.toDisplay(pt);

                    if (painter != null) {
                        painter.populateMenu(menu);
                    }

                    menu.setLocation(pt.x, pt.y);
                    menu.setVisible(true);
                }
            }
        });

        ToolItem nextButton = new ToolItem(toolBar, SWT.PUSH);
        nextButton.setText("Next");
        nextButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                nextPushed();
            }
        });

        shell.open();
        animator.start();
    }

    public void loop() {
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        animator.stop();
        display.dispose();
        System.exit(0);
    }

    protected void nextPushed() {
    }

    public void clear() {
        if (painter != null) {
            painter.uninstall(canvas);
            painter = null;
        }
    }

    public void setView(MirurView view, VariableObject obj) {
        clear();

        painter = view.install(canvas, obj);
        canvas.setLookAndFeel(new MirurLAF());
    }
}
