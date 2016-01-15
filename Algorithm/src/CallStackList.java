import java.util.LinkedList;
import java.util.List;

/**
 * Created by Jamie on 06/01/2016.
 */
public class CallStackList<T> extends LinkedList<T> {

    ICallstackCallback callback;

    public void setCallback(ICallstackCallback callback){
        this.callback = callback;
    }

    @Override
    public boolean add(T object) {
        if(this.contains(object)){
           return true;
        }
        boolean success = super.add(object);
        // Do some action here
        if (success){
            callback.stackUpdated();
        }

        return success;
    };

    @Override
    public void add(int index, T object) {
        if(this.contains(object)){
            return;
        }
        super.add(index, object);
        // Do some action here
        callback.stackUpdated();
    };

    @Override
    public T remove(int index) {
        // Do some action here
        callback.stackUpdated();
        return super.remove(index);
    }
}
