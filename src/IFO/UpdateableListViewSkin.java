package IFO;

import com.sun.javafx.scene.control.skin.ListViewSkin;
import javafx.scene.control.ListView;

public class UpdateableListViewSkin<T> extends ListViewSkin<T> {

    public UpdateableListViewSkin(ListView<T> listView) {
        super(listView);
    }

    public void refresh() {
        super.flow.rebuildCells();
        //super.flow.recreateCells();
    }

    @SuppressWarnings("unchecked")
    public static <T> UpdateableListViewSkin<T> cast(Object obj) {
        return (UpdateableListViewSkin<T>)obj;
    }
}
