package mirur.plugin.statsview;

import mirur.core.PrimitiveArray;
import mirur.plugin.ArraySelectListener;
import mirur.plugin.SelectListenerToggle;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.Page;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.part.PageSite;
import org.eclipse.ui.part.ViewPart;

public class ArrayStatsView extends ViewPart implements ArraySelectListener {
    public static final String ID = "mirur.views.Statistics";

    private SelectListenerToggle selectListenerToggle;
    private ShowArrayDataToggle showDataToggle;

    private PageBook book;
    private StatisticsPage statsPage;
    private DataTablePage dataPage;
    private Page activePage;

    @Override
    public void createPartControl(Composite parent) {
        book = new PageBook(parent, SWT.NONE);

        statsPage = new StatisticsPage();
        dataPage = new DataTablePage();

        initPage(statsPage);
        initPage(dataPage);
        showPage(statsPage);

        selectListenerToggle = new SelectListenerToggle(ID, this, this);
        showDataToggle = new ShowArrayDataToggle() {
            @Override
            public void run() {
                if (isChecked()) {
                    showPage(dataPage);
                } else {
                    showPage(statsPage);
                }
            }
        };
        getSite().getPage().addPartListener(selectListenerToggle);

        showDataToggle.setChecked(false);

        IToolBarManager tbm = getViewSite().getActionBars().getToolBarManager();
        tbm.add(showDataToggle);
        tbm.add(selectListenerToggle);
    }

    private void initPage(Page page) {
        page.createControl(book);
        page.init(new PageSite(getViewSite()));
    }

    private void showPage(Page page) {
        activePage = page;
        book.showPage(activePage.getControl());
    }

    @Override
    public void setFocus() {
        if (activePage != null) {
            activePage.setFocus();
        }
    }

    @Override
    public void dispose() {
        statsPage.dispose();
        dataPage.dispose();
        book.dispose();

        getSite().getPage().removePartListener(selectListenerToggle);
        super.dispose();
    }

    @Override
    public void arraySelected(PrimitiveArray array) {
        dataPage.setInput(array);
        statsPage.setInput(array);
    }
}
