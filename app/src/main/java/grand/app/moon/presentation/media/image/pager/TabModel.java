package grand.app.moon.presentation.media.image.pager;

import androidx.fragment.app.Fragment;

import java.io.Serializable;

public class TabModel implements Serializable {
    public int position = -1;
    public String name;
    public Fragment fragment;
    public String hashTag;

    public TabModel(String name, Fragment fragment) {
        this.name = name;
        this.fragment = fragment;
    }



    public TabModel(String name, Fragment fragment, String hashTag) {
        this.name = name;
        this.fragment = fragment;
        this.hashTag = hashTag;
    }


    public TabModel(int position , String name, Fragment fragment) {
        this.position = position;
        this.name = name;
        this.fragment = fragment;
    }
}
