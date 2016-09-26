/*
 * This file is part of Mirur.
 *
 * Mirur is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Mirur is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Mirur.  If not, see <http://www.gnu.org/licenses/>.
 */
package mirur.plugin.statsview;

import mirur.core.PrimitiveArray;
import mirur.core.VariableObject;
import mirur.plugin.SelectListenerToggle;
import mirur.plugin.VarObjectSelectListener;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.Page;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.part.PageSite;
import org.eclipse.ui.part.ViewPart;

public class ArrayStatsView extends ViewPart implements VarObjectSelectListener {
    public static final String ID = "mirur.views.Statistics";

    private SelectListenerToggle selectListenerToggle;
    private SaveArrayToFileAction saveArrayAction;

    private PageBook book;
    private StatisticsPage statsPage;
    private Page activePage;

    @Override
    public void createPartControl(Composite parent) {
        book = new PageBook(parent, SWT.NONE);

        statsPage = new StatisticsPage();

        initPage(statsPage);
        showPage(statsPage);

        selectListenerToggle = new SelectListenerToggle(ID, this, this);
        getSite().getPage().addPartListener(selectListenerToggle);

        saveArrayAction = new SaveArrayToFileAction();
        saveArrayAction.setEnabled(false);

        IToolBarManager tbm = getViewSite().getActionBars().getToolBarManager();
        tbm.add(saveArrayAction);
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
        book.dispose();

        getSite().getPage().removePartListener(selectListenerToggle);
        super.dispose();
    }

    @Override
    public void variableSelected(VariableObject obj) {
        PrimitiveArray array = obj instanceof PrimitiveArray ? (PrimitiveArray) obj : null;

        statsPage.setInput(array);
        saveArrayAction.variableSelected(obj);
    }
}
