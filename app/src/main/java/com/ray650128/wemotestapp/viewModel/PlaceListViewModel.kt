package com.ray650128.wemotestapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.ray650128.wemotestapp.db.asLiveData
import com.ray650128.wemotestapp.model.Place
import io.realm.Realm
import io.realm.Sort


class PlaceListViewModel : ViewModel() {

    val testData: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }

    private val realm = Realm.getDefaultInstance()

    private val _listData = realm.where(Place::class.java).sort("timestamp", Sort.DESCENDING).findAllAsync().asLiveData()

    val listData: LiveData<List<Place>> = Transformations.map(_listData) { realmResult ->
        realm.copyFromRealm(realmResult)
    }

    val placeData: MutableLiveData<Place?> by lazy {
        MutableLiveData<Place?>()
    }

    fun getData(id: Int) {
        val data = realm.where(Place::class.java).equalTo("id", id).findFirst()
        placeData.postValue(data)
    }

    fun addData(item: Place) = realm.executeTransaction {
        val maxId = it.where(Place::class.java).max("id")
        val nextId = if (maxId == null) 1 else maxId.toInt() + 1

        it.createObject(Place::class.java, nextId).apply {
            title = item.title
            content = item.content
            timestamp = item.timestamp
            photos = item.photos
            latitude = item.latitude
            longitude = item.longitude
        }
    }

    fun updateData(item: Place) = realm.executeTransaction {
        it.copyToRealmOrUpdate(item)
    }

    fun deleteData(id: Int) = realm.executeTransaction {
        it.where(Place::class.java).equalTo("id", id).findFirst()?.deleteFromRealm()
    }

    override fun onCleared() {
        realm.close()
        super.onCleared()
    }
}