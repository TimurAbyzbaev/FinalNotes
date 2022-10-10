package ru.abyzbaev.finalnotes

import android.os.Parcelable
import android.os.Parcel
import android.os.Parcelable.Creator

class Node : Parcelable {
    var title: String?
    var description: String?
    var id: String? = null

    constructor(title: String?, description: String?) {
        this.title = title
        this.description = description
    }

    constructor(id: String?, title: String?, description: String?) {
        this.id = id
        this.title = title
        this.description = description
    }

    protected constructor(`in`: Parcel) {
        title = `in`.readString()
        description = `in`.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(title)
        dest.writeString(description)
    }

    /*companion object {
        val CREATOR: Creator<Node?> = object : Creator<Node?> {
            override fun createFromParcel(`in`: Parcel): Node? {
                return Node(`in`)
            }

            override fun newArray(size: Int): Array<Node?> {
                return arrayOfNulls(size)
            }
        }
    }*/

    companion object CREATOR : Creator<Node> {
        override fun createFromParcel(parcel: Parcel): Node {
            return Node(parcel)
        }

        override fun newArray(size: Int): Array<Node?> {
            return arrayOfNulls(size)
        }
    }
}