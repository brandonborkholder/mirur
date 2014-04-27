package testplugin.views;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IVariable;
import org.eclipse.debug.ui.AbstractDebugView;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.contexts.DebugContextEvent;
import org.eclipse.debug.ui.contexts.IDebugContextListener;
import org.eclipse.debug.ui.contexts.IDebugContextService;
import org.eclipse.jdt.internal.debug.core.model.JDIStackFrame;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ILazyTreeContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.IViewSite;

import com.sun.jdi.AbsentInformationException;
import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.StackFrame;

public class VariablesView extends AbstractDebugView implements IDebugContextListener {
  private TreeViewer treeViewer;
  private IContentProvider treeContentProvider;

  @Override
  public Viewer createViewer(Composite parent) {
    treeContentProvider = new ILazyTreeContentProvider() {
      TreeViewer viewer;

      IVariable[] vars;

      @Override
      public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        this.viewer = (TreeViewer) viewer;

        if (newInput == null) {
          vars = null;
        } else {
          try {
            vars = ((JDIStackFrame)newInput).getVariables();
          } catch (DebugException ex) {
            throw new RuntimeException(ex);
          }
        }
      }

      @Override
      public void dispose() {
        viewer = null;
        vars = null;
      }

      @Override
      public void updateElement(Object parent, int index) {
        viewer.replace(parent, index, vars[index]);
      }

      @Override
      public void updateChildCount(Object element, int currentChildCount) {
        if (element instanceof JDIStackFrame) {
          if (vars == null) {
            viewer.setChildCount(element, 0);
          } else {
            viewer.setChildCount(element, vars.length);
          }
        } else {
          viewer.setChildCount(element, 0);
        }
      }

      @Override
      public Object getParent(Object element) {
        return null;
      }
    };

    int style = SWT.VIRTUAL | SWT.V_SCROLL | SWT.H_SCROLL | SWT.SINGLE | SWT.FULL_SELECTION;
    treeViewer = new TreeViewer(parent, style);
    Tree tree = treeViewer.getTree();
    tree.setHeaderVisible(true);
    tree.setLinesVisible(true);

    TreeColumn col = new TreeColumn(tree, SWT.LEFT);
    col.setText("Name");
    col.setWidth(200);
    col = new TreeColumn(tree, SWT.LEFT);
    col.setText("Value");
    col.setWidth(300);

    treeViewer.setContentProvider(treeContentProvider);
    ITableLabelProvider lblProvider = new ITableLabelProvider() {
      @Override
      public void removeListener(ILabelProviderListener listener) {
      }

      @Override
      public boolean isLabelProperty(Object element, String property) {
        return false;
      }

      @Override
      public void dispose() {
      }

      @Override
      public void addListener(ILabelProviderListener listener) {
      }

      @Override
      public String getColumnText(Object element, int columnIndex) {
        try {
          return ((IVariable)element).getName();
        } catch (DebugException ex) {
          // TODO Auto-generated catch block
          ex.printStackTrace();
          return null;
        }
      }

      @Override
      public Image getColumnImage(Object element, int columnIndex) {
        return null;
      }
    };

    treeViewer.setLabelProvider(lblProvider);

    treeViewer.addPostSelectionChangedListener(new ISelectionChangedListener() {
      @Override
      public void selectionChanged(SelectionChangedEvent event) {
        if (event.getSelectionProvider() == treeViewer) {
          treeSelectionChanged(event.getSelection());
        }
      }
    });

    DebugUITools.addPartDebugContextListener(getSite(), this);

    return treeViewer;
  }

  protected void treeSelectionChanged(ISelection selection) {

  }

  @Override
  public void dispose() {
    DebugUITools.removePartDebugContextListener(getSite(), this);
    super.dispose();
  }

  protected ISelection getDebugContext() {
    IViewSite site = (IViewSite) getSite();
    IDebugContextService contextService = DebugUITools.getDebugContextManager().getContextService(site.getWorkbenchWindow());
    return contextService.getActiveContext(site.getId(), site.getSecondaryId());
  }

  @Override
  protected String getHelpContextId() {
    // TODO
    return "TODO";
  }

  @Override
  protected void createActions() {
  }

  @Override
  protected void fillContextMenu(IMenuManager menu) {
  }

  @Override
  public void debugContextChanged(DebugContextEvent event) {
    if ((event.getFlags() & DebugContextEvent.ACTIVATED) > 0) {
      contextActivated(event.getContext());
    }
  }

  private void contextActivated(ISelection selection) {
    Object treeData = null;
    if (selection instanceof IStructuredSelection) {
      Object source = ((IStructuredSelection) selection).getFirstElement();
      if (source instanceof JDIStackFrame) {
        JDIStackFrame frame = (JDIStackFrame) source;
        treeData = source;
      }
    }

    treeViewer.setInput(treeData);
  }

  @Override
  protected void becomesHidden() {
    contextActivated(null);
    super.becomesHidden();
  }

  @Override
  protected void becomesVisible() {
    super.becomesVisible();
    ISelection selection = getDebugContext();
    contextActivated(selection);
  }

  @Override
  protected void configureToolBar(IToolBarManager tbm) {
  }
}
