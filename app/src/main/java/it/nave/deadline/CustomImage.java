package it.nave.deadline;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.appcompat.widget.AppCompatImageView;

public class CustomImage extends AppCompatImageView {
    private int index;

    public CustomImage(Context context) {
        super(context);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void swap(CustomImage other) {
        Drawable tempDrawable = this.getDrawable();
        int tempId = this.getIndex();
        this.setImageDrawable(other.getDrawable());
        this.setIndex(other.getIndex());
        other.setImageDrawable(tempDrawable);
        other.setIndex(tempId);

    }
}
