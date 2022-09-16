package ru.abyzbaev.finalnotes;

import android.os.Parcel;
import android.os.Parcelable;

public class Node implements Parcelable {
    private String title;
    private String description;

    public Node(String title, String description) {
        this.title = title;
        this.description = description;
    }

    protected Node(Parcel in) {
        title = in.readString();
        description = in.readString();
    }

    public static final Creator<Node> CREATOR = new Creator<Node>() {
        @Override
        public Node createFromParcel(Parcel in) {
            return new Node(in);
        }

        @Override
        public Node[] newArray(int size) {
            return new Node[size];
        }
    };

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
    }
}
